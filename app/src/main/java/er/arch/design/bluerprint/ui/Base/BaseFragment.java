package er.arch.design.bluerprint.ui.Base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import er.arch.design.bluerprint.ui.Base.Presenter.communication.IProgressListner;
import er.arch.design.bluerprint.ui.Base.Presenter.communication.IResultListener;

/**
 * Created by ahsaniqbal on 11/10/16.
 */

public class BaseFragment extends Fragment implements
        IProgressListner, IResultListener {

    protected Context mContext;
    protected View mView;
    protected FragmentManager fragmentManager;
    protected BaseFragment currentFragment;
    protected FragmentTransaction fragmentTransaction;
    @Nullable
    protected Bundle globalSavedInstanceState = null;




    @Nullable
    public String viewTitle() {
        return null;
    }
    @Override
    public void onAttach(Context context) {
        mContext = getActivity();
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).setResultListener(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getActivity() != null ){
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).setResultListener(null);
        }
    }


    /**
     * Replace any fragment from the current fragment. Will call
     * {@link BaseActivity#replaceFragment(BaseFragment, boolean,int)} method
     * internally
     *
     * @param fragment
     */
    protected void replaceFragment(BaseFragment fragment, boolean addToBackStack, @IdRes int containerViewId) {
        ((BaseActivity) getActivity()).replaceFragment(fragment, addToBackStack,containerViewId);
    }

    protected void createFreshBackStack(BaseFragment fragment , @IdRes int containerViewId) {
        ((BaseActivity) getActivity()).createFreshBackStack(fragment , containerViewId);

    }

    /**
     * @param fragId
     * @return true if fragment with the provided fragId is already present in
     * the back stack false otherwise
     */
    public boolean isInBackStack(String fragId) {
       return  ((BaseActivity) getActivity()).isInBackStack(fragId);
    }


    public String getFragmentAtTop() {
        return ((BaseActivity) getActivity()).getFragmentAtTop();
    }

    /**
     * Change the title from the actionbar title from fragment. Which will in
     * turn call {@link BaseActivity#setActionBarTitle(String)}
     *
     * @param title
     */
    protected void setActionBarTitle(String title) {
        if (null != getActivity())
            ((BaseActivity) getActivity()).setActionBarTitle(title);
    }

    /**
     * Change the title from the actionbar title from fragment. Which will in
     * turn call {@link BaseActivity#setActionBarLogo(String)}
     *
     * @param logo
     */
    protected void setActionBarLogo(String logo) {
        if (null != getActivity())
            ((BaseActivity) getActivity()).setActionBarLogo(logo);
    }

    /**
     * Pop the last fragment transition from the manager's fragment back stack.
     *
     * @param fragment
     * @param flags
     * @see FragmentManager#popBackStack(String, int);
     */
    protected void popBackStack(@NonNull BaseFragment fragment, int flags) {
        if (null != getActivity()) {
            ((BaseActivity) getActivity()).popBackStack(fragment, flags);
        }
    }


    public void saveCurrentStateToBundle(Bundle outState) {
        //To be overridden by sub-classes, if needed
    }

    public void restoreCurrentStateFromBundle(Bundle savedInstanceState) {
        //To be overridden by sub-classes, if needed
    }

   /* public void commitFragment() {
        fragmentTransaction.addToBackStack(null);  // calling this method, the replace transaction is saved to the back stack so the user can reverse the
        fragmentTransaction.commit();             // transaction and bring back the previous fragment by pressing the Back button
    }*/

    @Override
    public boolean onFragmentBackPressed() {
        if(getActivity() != null){
         //   MGKizadUtility.hideKeyboard(getContext());
        }
        return false;
    }

    @Override
    public boolean onMenuItemClickListener(MenuItem item) {
        return false;
    }

    @Override
    public void showLoading(Context context,String msg ) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showLoading(context,msg);
        }
    }

    @Override
    public void hideLoading() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).hideLoading();
        }
    }
    /**
     * To pass arguments to the current fragments
     */
    public void refreshArguments(Bundle bundle) {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
