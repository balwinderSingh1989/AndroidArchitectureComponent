package er.arch.design.bluerprint.ui.Main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import er.arch.design.bluerprint.data.AbsentLiveData;
import er.arch.design.bluerprint.data.model.User;
import er.arch.design.bluerprint.data.network.loader.UserLoader;
import er.arch.design.bluerprint.data.network.response.Resource;

public class UserActivityViewModel extends ViewModel {

    private MutableLiveData<String> mLiveUserName;
    private LiveData<Resource<User>> mUser;

    public UserActivityViewModel(@NonNull UserLoader loader) {
        mLiveUserName = new MutableLiveData<>();
            mUser = Transformations.switchMap(mLiveUserName , userName -> {
                     if(userName.isEmpty())
                         return AbsentLiveData.create();
                     else
                         return loader.load(userName);

            });
    }


    public LiveData<Resource<User>> getUser() {
        return mUser;
    }

    public void setUserName(String userName) {
        mLiveUserName.setValue(userName);
    }

}

