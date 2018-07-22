package er.arch.design.bluerprint.data.PrefManager;

import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.security.auth.x500.X500Principal;

import static android.content.ContentValues.TAG;
import static android.security.keystore.KeyProperties.KEY_ALGORITHM_RSA;

public class KeyStoreManager {

    private static final String KEYSTORE = "AndroidKeyStore";
    private static final String TYPE_RSA = "RSA";
    private static final String CYPHER = "RSA/ECB/PKCS1Padding";
    private static final String ENCODING = "UTF-8";

   // private static KeyStoreManager keyStoreManager;
    private Context mContext;

    public KeyStoreManager(Context ctx) {
        mContext = ctx;
    }

  /*  public static KeyStoreManager getKeyStoreManager(Context ctx) {
        if (keyStoreManager == null) {
            intializeKeyStore(ctx);
        }
        return keyStoreManager;
    }
    private static void intializeKeyStore(Context ctx) {
        keyStoreManager = new KeyStoreManager(ctx);
    }*/

    @NonNull
    public String encryptString(String textToEncrypt, @NonNull String alias) {

        String encryptedText = "";
        try {
            java.security.KeyStore.PrivateKeyEntry privateKeyEntry =    getPrivateKey(mContext,alias);
            RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();

            // Encrypt the text
//            String initialText = startText.getText().toString();
            String initialText = textToEncrypt;
            if (initialText.isEmpty()) {
                Toast.makeText(mContext, "Enter text in the 'Initial Text' widget", Toast.LENGTH_LONG).show();
                return "";
            }

            Cipher input = Cipher.getInstance(CYPHER, "AndroidOpenSSL");
            input.init(Cipher.ENCRYPT_MODE, publicKey);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(
                    outputStream, input);
            cipherOutputStream.write(initialText.getBytes("UTF-8"));
            cipherOutputStream.close();
            byte[] vals = outputStream.toByteArray();
//            encryptedText.setText(Base64.encodeToString(vals, Base64.DEFAULT));
            encryptedText = Base64.encodeToString(vals, Base64.DEFAULT);
        } catch (Exception e) {
            Toast.makeText(mContext, "Exception " + e.getMessage() + " occured", Toast.LENGTH_LONG).show();
            Log.e(TAG, Log.getStackTraceString(e));
        }
        return encryptedText.trim();
    }

    @NonNull
    public String decryptString(String textToDecrypt, @NonNull String alias) {

        String textDecrypted = "";
        try {
            java.security.KeyStore.PrivateKeyEntry privateKeyEntry =getPrivateKey(mContext,alias);
            Cipher output = Cipher.getInstance(CYPHER);
            output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());
//            String cipherText = encryptedText.getText().toString();
            String cipherText = textToDecrypt;
            CipherInputStream cipherInputStream = new CipherInputStream(
                    new ByteArrayInputStream(Base64.decode(cipherText, Base64.DEFAULT)), output);
            ArrayList<Byte> values = new ArrayList<>();
            int nextByte;
            while ((nextByte = cipherInputStream.read()) != -1) {
                values.add((byte) nextByte);
            }
            byte[] bytes = new byte[values.size()];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = values.get(i).byteValue();
            }
            String finalText = new String(bytes, 0, bytes.length, "UTF-8");
            textDecrypted = finalText;
        } catch (Exception e) {
            Toast.makeText(mContext, "Exception " + e.getMessage() + " occured", Toast.LENGTH_LONG).show();
            Log.e(TAG, Log.getStackTraceString(e));
        }
        return textDecrypted.trim();
    }

    private static KeyStore.PrivateKeyEntry getPrivateKey(@NonNull Context context, @NonNull String alias) throws KeyStoreException,
            CertificateException, NoSuchAlgorithmException,
            IOException, UnrecoverableEntryException {
        KeyStore ks = KeyStore.getInstance(KEYSTORE);
        // Weird artifact of Java API.  If you don't have an InputStream to load, you still need
        // to call "load", or it'll crash.
        ks.load(null);
        // Load the key pair from the Android Key Store
        KeyStore.Entry entry = ks.getEntry(alias, null);
        /* If the entry is null, keys were never stored under this alias.
         */
        if (entry == null) {
            Log.w(TAG, "No key found under alias: " + alias);
            Log.w(TAG, "Generating new key...");
            try {
                createKeys(context,alias);
                // reload keystore
                ks = KeyStore.getInstance(KEYSTORE);
                ks.load(null);
                // reload key pair
                entry = ks.getEntry(alias, null);
                if (entry == null) {
                    Log.w(TAG, "Generating new key failed...");
                    return null;
                }
            } catch (NoSuchProviderException e) {
                Log.w(TAG, "Generating new key failed...");
                e.printStackTrace();
                return null;
            } catch (InvalidAlgorithmParameterException e) {
                Log.w(TAG, "Generating new key failed...");
                e.printStackTrace();
                return null;
            }
        }
        /* If entry is not a KeyStore.PrivateKeyEntry, it might have gotten stored in a previous
         * iteration of your application that was using some other mechanism, or been overwritten
         * by something else using the same keystore with the same alias.
         * You can determine the type using entry.getClass() and debug from there.
         */
        if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
            Log.w(TAG, "Not an instance of a PrivateKeyEntry");
            Log.w(TAG, "Exiting signData()...");
            return null;
        }
        return (KeyStore.PrivateKeyEntry) entry;
    }

    /**
     * Creates a public and private key and stores it using the Android Key Store, so that only
     * this application will be able to access the keys.
     */
    private static void createKeys(@NonNull Context context, @NonNull String alias) throws NoSuchProviderException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(KEY_ALGORITHM_RSA, "AndroidKeyStore");
            kpg.initialize(new KeyGenParameterSpec.Builder(alias,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setDigests(KeyProperties.DIGEST_SHA256,
                            KeyProperties.DIGEST_SHA512)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                    .setUserAuthenticationRequired(false)
                    .build());
            final KeyPair kp = kpg.generateKeyPair();
            Log.d(TAG, "Public Key is: " + kp.getPublic().toString());
        } else {
            // Create a start and end time, for the validity range of the key pair that's about to be
            // generated.
            Calendar start = new GregorianCalendar();
            Calendar end = new GregorianCalendar();
            end.add(Calendar.YEAR, 25);
            // The KeyPairGeneratorSpec object is how parameters for your key pair are passed
            // to the KeyPairGenerator.  For a fun home game, count how many classes in this sample
            // start with the phrase "KeyPair".
            KeyPairGeneratorSpec spec =
                    new KeyPairGeneratorSpec.Builder(context)
                            // You'll use the alias later to retrieve the key.  It's a key for the key!
                            .setAlias(alias)
                            // The subject used for the self-signed certificate of the generated pair
                            //.setSubject(new X500Principal("CN=" + ALIAS))
                            .setSubject(new X500Principal("CN=Your Company ," +
                                    " O=Your Organization" +
                                    " C=Your Coountry"))
                            // The serial number used for the self-signed certificate of the
                            // generated pair.
                            .setSerialNumber(BigInteger.ONE)
                            // Date range of validity for the generated pair.
                            .setStartDate(start.getTime())
                            .setEndDate(end.getTime())
                            .build();
            // Initialize a KeyPair generator using the the intended algorithm (in this example, RSA
            // and the KeyStore.  This example uses the AndroidKeyStore.
            final KeyPairGenerator kpGenerator = KeyPairGenerator.getInstance(TYPE_RSA, KEYSTORE);
            kpGenerator.initialize(spec);
            final KeyPair kp = kpGenerator.generateKeyPair();
            Log.d(TAG, "Public Key is: " + kp.getPublic().toString());
        }
    }


}

