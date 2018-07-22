package er.arch.design.bluerprint.ui.Base.Presenter.communication;

import android.content.Context;

import er.arch.design.bluerprint.ui.Base.BaseFragment;

/**
 * Progress dialog callback interface. This needs to be implemented by
 * {@link er.arch.design.bluerprint.ui.Base.BaseActivity} or {@link BaseFragment} and then need to pass it
 * on to the {@link }'s constructor
 *
 * @author Balwinder
 */
public interface IProgressListner {

    /**
     * This method will get a call when the progress dialog need to be shown
     *
     * @param msg of the dialog
     * @param msg   which will be shown in the progress dialog
     */
    void showLoading(Context context, String msg);

    /**
     * This method will get triggered when {@link } needs to hide the
     * progress dialog
     */
    void hideLoading();


}