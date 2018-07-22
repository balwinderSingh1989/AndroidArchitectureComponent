package er.arch.design.bluerprint.data.PrefManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import er.arch.design.bluerprint.di.scope.ApplicationContext;

public class PrefManager {
    private SharedPreferences pref;
  //  private  PrefManager prefManager;
    private Context context;

    public PrefManager(@ApplicationContext  Context ctx) {
        context = ctx;
    }

    public void setPrefManager(String prefManager) {
        pref = context.getSharedPreferences(prefManager, Context.MODE_PRIVATE);

    }

  /*  public static PrefManager getPrefManager(Context ctx) {
        if (prefManager == null) {
            intializePrefManager(ctx);
        }
        return prefManager;
    }

    private static void intializePrefManager(Context ctx) {
        prefManager = new PrefManager(ctx);
    }*/


    public void saveString(String key, String value) {
        Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
        editor.apply();
    }

    public void saveInt(String key, int value) {
        Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void saveBoolean(String key, boolean value) {
        Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void saveLong(String key, long value) {
        Editor editor = pref.edit();
        editor.putLong(key, value);
        editor.commit();
    }


    public void deleteString(String key) {
        Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();
    }

    @Nullable
    public String getString(String key) {
        return pref.getString(key, "");
    }

    public int getInt(String key) {
        return pref.getInt(key, -1);
    }

    public boolean getBoolean(String key) {
        return pref.getBoolean(key, false);
    }

    public long getLong(String key) {
        return pref.getLong(key, -1);
    }

    public void clearPref() {
        pref.edit().clear().commit();
    }
}