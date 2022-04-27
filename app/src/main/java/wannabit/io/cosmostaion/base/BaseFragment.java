package wannabit.io.cosmostaion.base;


import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.fulldive.wallet.extensions.ViewExtensionsKt;
import com.joom.lightsaber.Injector;

public class BaseFragment extends Fragment {

    protected BaseApplication mApplication;

    protected BaseApplication getBaseApplication() {
        if (mApplication == null)
            mApplication = getBaseActivity().getBaseApplication();
        return mApplication;

    }

    @NonNull
    public Injector getAppInjector() {
        return getBaseApplication().getInjector();
    }

    protected BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    protected BaseData getBaseDao() {
        if (getBaseActivity() != null && getBaseActivity().getBaseDao() != null) {
            return getBaseActivity().getBaseDao();

        } else {
            return getBaseApplication().getBaseDao();
        }
    }

    public void showDialog(DialogFragment dialogFragment) {
        showDialog(dialogFragment, "dialog", true);
    }

    public void showDialog(DialogFragment dialogFragment, String tag, boolean cancelable) {
        dialogFragment.setCancelable(cancelable);
        getParentFragmentManager()
                .beginTransaction()
                .add(dialogFragment, tag)
                .commitNowAllowingStateLoss();
    }

    @Override
    public void onDestroyView() {
        ViewExtensionsKt.clearUi(this);
        super.onDestroyView();
    }
}
