package er.arch.design.bluerprint.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import er.arch.design.bluerprint.AppExecutors;
import er.arch.design.bluerprint.data.PrefManager.PrefManager;
import er.arch.design.bluerprint.data.dao.UserDao;
import er.arch.design.bluerprint.data.database.GitHubDatabase;
import er.arch.design.bluerprint.di.scope.ApplicationContext;
import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    @Provides
    @ApplicationContext
    @KizadApplicaitonScope
    Application providesContext(Application application)
    {
        return application;
    }

    @NonNull
    @Provides
    @KizadApplicaitonScope
    PrefManager providesPrefmanager(@ApplicationContext Application context)
    {
        return  new PrefManager(context);
    }

    @NonNull
    @KizadApplicaitonScope
    @Provides
    GitHubDatabase provideCache(@NonNull Application app) {
        return Room.databaseBuilder(app, GitHubDatabase.class,"arch_github.db").build();
    }


    @KizadApplicaitonScope
    @Provides
    UserDao provideUserDao(@NonNull GitHubDatabase cache) {
        return cache.userDao();
    }

    @Provides
    @KizadApplicaitonScope
    public AppExecutors providesAppExecutors() {
        return  AppExecutors.getInstance();
    }




}
