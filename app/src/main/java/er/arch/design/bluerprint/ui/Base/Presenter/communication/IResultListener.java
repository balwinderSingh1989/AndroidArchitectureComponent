package er.arch.design.bluerprint.ui.Base.Presenter.communication;

import android.view.MenuItem;

/**
 * Interface to establish communication between fragment and activity.
 *
 * @author Balwinder
 */
public interface IResultListener {

    boolean onFragmentBackPressed();

    boolean onMenuItemClickListener(MenuItem item);

  /*  void onActionReceviedFromBottomSheet(Object obj);

    void updateFragment(Object obj);

    void showErrorAtActivity(int action, Object data, String error_code);

    void goPremium();*/

}