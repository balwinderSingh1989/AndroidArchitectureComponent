package er.arch.design.bluerprint.di.component;


import android.app.Application;
import android.support.annotation.NonNull;

import er.MyApp;
import er.arch.design.bluerprint.di.module.ActivityBuilderModule;
import er.arch.design.bluerprint.di.module.ApplicationModule;
import er.arch.design.bluerprint.di.module.KizadApplicaitonScope;
import er.arch.design.bluerprint.di.module.NetworkModule;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;


@KizadApplicaitonScope
@Component(modules = {ApplicationModule.class, NetworkModule.class,
        AndroidInjectionModule.class,
        ActivityBuilderModule.class
})
public interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        @NonNull
        @BindsInstance
        Builder application(Application application);
        @NonNull
        ApplicationComponent build();
    }
    void inject(MyApp app);
}


