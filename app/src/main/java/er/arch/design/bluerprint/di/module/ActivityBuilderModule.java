
package er.arch.design.bluerprint.di.module;

import android.support.annotation.NonNull;

import er.arch.design.bluerprint.ui.Main.UserActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by balwinder on 30/05/2017.
 */
@Module
public abstract class ActivityBuilderModule {

   /* @ContributesAndroidInjector(modules = FragmentBuilderModule.class)
    abstract MainActivity mainActivity();*/
    @NonNull
    @ContributesAndroidInjector(modules = ActivityModule.class)
    abstract UserActivity bindUserActivity();
}
