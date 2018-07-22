package er.arch.design.bluerprint.ui.Base;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import er.arch.design.bluerprint.data.utils.ConnectionUtils;
import er.arch.design.bluerprint.ui.Base.Presenter.FragmentTransactionManger;
import er.arch.design.bluerprint.ui.Base.Presenter.communication.IProgressListner;
import er.arch.design.bluerprint.ui.Base.Presenter.communication.IResultListener;
import dagger.android.AndroidInjection;


public abstract class BaseActivity<DB extends ViewDataBinding> extends AppCompatActivity implements IProgressListner, IResultListener, MenuItem.OnMenuItemClickListener {


    public DB dataBinding;
    private FragmentTransactionManger manager;
    @Nullable
    protected ActionBar mActionBar;
    protected IResultListener iResultListener;
    ActionBarDrawerToggle actionBarDrawerToggle;
    private ProgressDialog progressDialog;
    private boolean mIsNetworkAvailable = false;


    public FragmentTransactionManger getManager() {
        return manager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        manager = new FragmentTransactionManger(this);
        dataBinding = DataBindingUtil.setContentView(this, getLayoutRes());

        mIsNetworkAvailable = ConnectionUtils.INSTANCE.isNetworkAvailable(this);
    }

    @LayoutRes
    public abstract int getLayoutRes();


    protected void initialiseActionBar() {
        if (null == mActionBar) {
            mActionBar = getSupportActionBar();
        }
        mActionBar.setDisplayHomeAsUpEnabled(false);
        mActionBar.setHomeButtonEnabled(false);
    }

    /**
     * Check if the network is available
     *
     * @return return true if the network is available
     */
    protected boolean isNetworkAvailable() {
        return mIsNetworkAvailable;
    }


    /**
     * Public method which will get called from the child fragment as well as
     * Derived activity classes.
     *
     * @param title
     */
    public void setActionBarTitle(String title) {
        if (null != mActionBar) {
            mActionBar = getSupportActionBar();
        }
        mActionBar.setTitle(title);
    }


    /**
     * Public method which will get called from the child fragment as well as
     * Derived activity classes.
     *
     * @param logo
     */
    public void setActionBarLogo(String logo) {
       /* if (null != mActionBar) {
            mActionBar = getSupportActionBar();
        }
        if(!TextUtils.isEmpty(logo)) {
            mActionBar.setDisplayUseLogoEnabled(true);
            mActionBar.setLogo(R.drawable.ripple_logo);
        }else {
            mActionBar.setDisplayUseLogoEnabled(false);
        }*/
    }


    public void setDrawerArrowDrawable(ActionBarDrawerToggle actionBarDrawerToggle) {
        this.actionBarDrawerToggle = actionBarDrawerToggle;
    }

    public ActionBarDrawerToggle getmDrawerArrowDrawable() {
        return actionBarDrawerToggle;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      /*  getMenuInflater().inflate(R.menu.main, menu);
        MenuItem actionClose = menu.findItem(R.id.action_filter);
        actionClose.setOnMenuItemClickListener(this);
        menu.findItem(R.id.switch_view).setVisible(false).setOnMenuItemClickListener(this);
        actionClose.setVisible(false);*/
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        saveCurrentStateToBundle(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        restoreCurrentStateFromBundle(savedInstanceState);
    }

    protected void saveCurrentStateToBundle(Bundle outState) {
        //To be overridden by sub-classes, if needed
    }

    protected void restoreCurrentStateFromBundle(Bundle savedInstanceState) {
        //To be overridden by sub-classes, if needed
    }

    public void setResultListener(IResultListener iResultListener) {
        this.iResultListener = iResultListener;
    }


    @Override
    public void onBackPressed() {
        boolean handled = false;
        if (null != iResultListener) {
            handled = iResultListener.onFragmentBackPressed();
        }
        if (!handled) {
            super.onBackPressed();
        }
    }


    @Override
    public void showLoading(Context context, String msg) {
        hideLoading();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMax(100);
        progressDialog.setMessage(msg);
        progressDialog.setTitle("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        try {
           if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

       /* try {
            if (kProgressHUD != null)
                kProgressHUD.dismiss();
        } catch (Exception ex) {
            ex.printStackTrace();
        }*/
    }


    @Override
    public boolean onFragmentBackPressed() {
        return false;
    }

    @Override
    public boolean onMenuItemClickListener(MenuItem item) {
        if (null != iResultListener) {
            return iResultListener.onMenuItemClickListener(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public String getFragmentAtTop() {
        return manager.getFragmentAtTop();
    }

    public void cleaBackStack() {
        manager.clearBackStack();
    }

    /**
     * @return {@link Integer} of the fragment entries present in the back stack.
     */
    public int getBackStackEntryCount() {
        return manager.getBackStackEntryCount();
    }

    /**
     * Pop the last fragment transition from the manager's fragment back stack.
     *
     * @param fragment
     * @param flags
     * @see FragmentManager#popBackStack(String, int);
     */
    public void popBackStack(@NonNull BaseFragment fragment, int flags) {
        manager.popBackStack(fragment, flags);
    }

    /**
     * Pops up the fragment if already exists in the back stack otherwise
     * replaces the current fragment.
     *
     * @param fragment object of which is to be displayed.
     */
    public void replaceFragment(BaseFragment fragment, boolean addToBackStack, @IdRes int containerViewId) {
        manager.replaceFragment(fragment, addToBackStack, containerViewId);
    }

    /**
     * Dumps all the previous back stack entries and creates fresh back stack to
     * work with.
     *
     * @param fragment
     */
    public void createFreshBackStack(BaseFragment fragment, @IdRes int containerViewId) {
        manager.createFreshBackStack(fragment, containerViewId);

    }

    /**
     * @param fragId
     * @return true if fragment with the provided fragId is already present in
     * the back stack false otherwise
     */
    public boolean isInBackStack(String fragId) {
        return manager.isInBackStack(fragId);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (null != iResultListener) {
            return iResultListener.onMenuItemClickListener(menuItem);
        }
        return super.onOptionsItemSelected(menuItem);
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
