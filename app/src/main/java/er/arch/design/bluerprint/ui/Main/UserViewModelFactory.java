package er.arch.design.bluerprint.ui.Main;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import er.arch.design.bluerprint.data.network.loader.UserLoader;

public class UserViewModelFactory extends ViewModelProvider.NewInstanceFactory {
   @Inject
   UserLoader userLoader;

   @Inject
    public UserViewModelFactory()
     {

    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new UserActivityViewModel(userLoader);
    }
}
