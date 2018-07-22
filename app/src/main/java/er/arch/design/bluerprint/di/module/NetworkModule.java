package er.arch.design.bluerprint.di.module;


import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import er.arch.design.bluerprint.BuildConfig;
import er.arch.design.bluerprint.data.network.api.GithubService;
import er.arch.design.bluerprint.data.network.factory.LiveDataCallAdapterFactory;
import er.arch.design.bluerprint.data.utils.ConnectionUtils;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    @NonNull
    @Provides
    @KizadApplicaitonScope
    Cache provieHttpCache(@NonNull Application application) {
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    @NonNull
    @Provides
    @KizadApplicaitonScope
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @NonNull
    @Provides
    @KizadApplicaitonScope
    HttpLoggingInterceptor providehttpLoggingInterceptor() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("network logg = ", message);
            }
        });
    }

    @NonNull
    @Provides
    @KizadApplicaitonScope
    OkHttpClient provideOkhttpClient(Cache cache, @NonNull HttpLoggingInterceptor httpLoggingInterceptor,
                                     @NonNull Interceptor OfflineCacheInterceptor) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(httpLoggingInterceptor);
        client.addInterceptor(OfflineCacheInterceptor);
        client.cache(cache);
        return client.build();
    }

    @NonNull
    @KizadApplicaitonScope
    @Provides
    Interceptor providesOfflineCacheInterceptor(@NonNull Application application) {

        return new Interceptor() {
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {

                Request request = chain.request();

                if (!ConnectionUtils.INSTANCE.isNetworkAvailable(application)) {

                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxStale(7, TimeUnit.DAYS)
                            .build();

                    request = request.newBuilder().
                            cacheControl(cacheControl).build();
                }
                return chain.proceed(request);
            }
        };
    }

    @Provides
    @KizadApplicaitonScope
    GithubService provideRetrofit(@NonNull Gson gson, @NonNull OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build().create(GithubService.class);
    }

}
