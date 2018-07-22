package er;

import android.app.Activity;
import android.app.Application;

import javax.inject.Inject;

import er.arch.design.bluerprint.di.component.DaggerApplicationComponent;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class MyApp extends Application implements HasActivityInjector {


    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerApplicationComponent.builder()
                .application(this).build().inject(this);

    }


    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }
}
