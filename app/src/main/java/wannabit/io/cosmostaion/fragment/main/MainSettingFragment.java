package wannabit.io.cosmostaion.fragment.main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.fulldive.wallet.interactors.settings.SettingsInteractor;
import com.fulldive.wallet.models.Currency;
import com.fulldive.wallet.presentation.chains.choicenet.ChoiceChainDialogFragment;
import com.fulldive.wallet.presentation.main.MainActivity;
import com.fulldive.wallet.presentation.main.currency.CurrencyDialogFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import wannabit.io.cosmostaion.BuildConfig;
import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.activities.AccountListActivity;
import wannabit.io.cosmostaion.activities.AppLockSetActivity;
import wannabit.io.cosmostaion.activities.chains.starname.StarNameWalletConnectActivity;
import wannabit.io.cosmostaion.base.BaseFragment;
import wannabit.io.cosmostaion.base.IRefreshTabListener;

public class MainSettingFragment extends BaseFragment implements IRefreshTabListener {

    public final static int SELECT_CURRENCY = 9034;
    public final static int SELECT_STARNAME_WALLET_CONNECT = 9036;

    private SettingsInteractor settingsInteractor;

    private TextView appLockTextView;
    private TextView currencyTextView;

    public static MainSettingFragment newInstance(Bundle bundle) {
        MainSettingFragment fragment = new MainSettingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsInteractor = getAppInjector().getInstance(SettingsInteractor.class);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_setting, container, false);

        appLockTextView = rootView.findViewById(R.id.appLockTextView);
        currencyTextView = rootView.findViewById(R.id.currencyTextView);

        rootView.findViewById(R.id.addWalletButton).setOnClickListener(v -> {
            showDialog(ChoiceChainDialogFragment.Companion.newInstance(true, "", new ArrayList<>()));
        });
        rootView.findViewById(R.id.walletButton).setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), AccountListActivity.class));
        });
        rootView.findViewById(R.id.adblockButton).setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), AppLockSetActivity.class));
        });
        rootView.findViewById(R.id.currencyButton).setOnClickListener(v -> {
            CurrencyDialogFragment currency_dialog = CurrencyDialogFragment.newInstance("");
            currency_dialog.setTargetFragment(this, SELECT_CURRENCY);
            showDialog(currency_dialog);
        });
        rootView.findViewById(R.id.discordButton).setOnClickListener(v -> {
            Intent telegram = new Intent(Intent.ACTION_VIEW, Uri.parse("https://discord.gg/BW2unf5s8X"));
            startActivity(telegram);
        });
        rootView.findViewById(R.id.guideButton).setOnClickListener(v -> {
            Intent guideIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.fulldive.com/faq"));
            startActivity(guideIntent);
        });
        rootView.findViewById(R.id.termsButton).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.fulldive.com/terms-of-use"));
            startActivity(intent);
        });
        rootView.findViewById(R.id.githubButton).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/imversed/imversed-wallet-android"));
            startActivity(intent);
        });
        rootView.findViewById(R.id.versionButton).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + requireContext().getPackageName()));
            startActivity(intent);
        });

        ((TextView) rootView.findViewById(R.id.version_text)).setText(getString(R.string.str_version_short, BuildConfig.VERSION_NAME));
        if (getBaseDao().getUsingAppLock()) {
            appLockTextView.setText(R.string.str_app_applock_enabled);
        } else {
            appLockTextView.setText(R.string.str_app_applock_diabeld);
        }

        return rootView;
    }


    @Override
    public void onRefreshTab() {
        if (!isAdded()) return;
        currencyTextView.setText(settingsInteractor.getCurrency().getTitle());
        if (getBaseDao().getUsingAppLock()) {
            appLockTextView.setText(R.string.str_app_applock_enabled);
        } else {
            appLockTextView.setText(R.string.str_app_applock_diabeld);
        }
    }

    public MainActivity getMainActivity() {
        return (MainActivity) getBaseActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_CURRENCY && resultCode == Activity.RESULT_OK) {
            Currency currency = Currency.Companion.getCurrency(data.getIntExtra("currency", -1));
            if (currency == null) {
                currency = Currency.Companion.getDefault();
            }
            currencyTextView.setText(currency.getTitle());
        } else if (requestCode == SELECT_STARNAME_WALLET_CONNECT && resultCode == Activity.RESULT_OK) {
            new TedPermission(requireContext()).setPermissionListener(new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                    IntentIntegrator integrator = IntentIntegrator.forSupportFragment(MainSettingFragment.this);
                    integrator.setOrientationLocked(true);
                    integrator.initiateScan();
                }

                @Override
                public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                    Toast.makeText(requireContext(), R.string.error_permission, Toast.LENGTH_SHORT).show();
                }
            })
                    .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .setRationaleMessage(getString(R.string.str_permission_qr))
                    .check();

        } else {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null && result.getContents() != null && result.getContents().trim().contains("bridge.walletconnect.org")) {
                Intent wcIntent = new Intent(requireActivity(), StarNameWalletConnectActivity.class);
                wcIntent.putExtra("wcUrl", result.getContents().trim());
                startActivity(wcIntent);

            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }

    }
}
