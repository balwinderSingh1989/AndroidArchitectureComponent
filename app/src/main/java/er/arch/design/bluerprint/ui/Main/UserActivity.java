package er.arch.design.bluerprint.ui.Main;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;

import javax.inject.Inject;

import er.arch.design.bluerprint.R;
import er.arch.design.bluerprint.data.network.response.Status;
import er.arch.design.bluerprint.databinding.LoginActivityBinding;
import er.arch.design.bluerprint.ui.Base.BaseActivity;

public class UserActivity extends BaseActivity<LoginActivityBinding> {

    private UserActivityViewModel mViewModel;

    @Inject
    UserViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserActivityViewModel.class);
        dataBinding.btnLogin.setOnClickListener(view -> observeData());
        mViewModel.setUserName("er.vicky1989");
    }

    @Override
    public int getLayoutRes() {
        return R.layout.login_activity;
    }

    void observeData() {
        showLoading(this,"Loading");
        mViewModel.getUser().observe(this, userResource -> {
            if (userResource != null &&
                    userResource.getData() != null && userResource.getStatus().equals(Status.SUCCESS)) {
                hideLoading();
            } else {
                if (userResource != null && userResource.getStatus().equals(Status.SUCCESS)) {
                }
            }
        });
    }
}
