package wannabit.io.cosmostaion.base;

import static wannabit.io.cosmostaion.base.BaseConstant.CONST_PW_TX_HTLS_REFUND;
import static wannabit.io.cosmostaion.base.BaseConstant.CONST_PW_TX_SIMPLE_SEND;
import static wannabit.io.cosmostaion.base.BaseConstant.FEE_BNB_SEND;
import static wannabit.io.cosmostaion.base.BaseConstant.SUPPORT_BEP3_SWAP;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.fulldive.wallet.di.IEnrichableActivity;
import com.fulldive.wallet.extensions.ViewExtensionsKt;
import com.fulldive.wallet.interactors.chains.StationInteractor;
import com.fulldive.wallet.interactors.chains.binance.BinanceInteractor;
import com.fulldive.wallet.interactors.chains.grpc.GrpcInteractor;
import com.fulldive.wallet.interactors.chains.okex.OkexInteractor;
import com.fulldive.wallet.models.BaseChain;
import com.fulldive.wallet.presentation.accounts.restore.MnemonicRestoreActivity;
import com.fulldive.wallet.presentation.system.WaitDialogFragment;
import com.fulldive.wallet.rx.AppSchedulers;
import com.joom.lightsaber.Injector;

import java.math.BigDecimal;
import java.net.URLEncoder;

import cosmos.auth.v1beta1.Auth;
import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.activities.AppLockActivity;
import wannabit.io.cosmostaion.activities.HtlcSendActivity;
import wannabit.io.cosmostaion.activities.MainActivity;
import wannabit.io.cosmostaion.activities.PasswordCheckActivity;
import wannabit.io.cosmostaion.activities.SendActivity;
import wannabit.io.cosmostaion.activities.chains.ibc.IBCSendActivity;
import wannabit.io.cosmostaion.dao.Account;
import wannabit.io.cosmostaion.dialog.Dialog_Buy_Select_Fiat;
import wannabit.io.cosmostaion.dialog.Dialog_Buy_Without_Key;
import wannabit.io.cosmostaion.dialog.Dialog_WatchMode;
import wannabit.io.cosmostaion.task.FetchTask.MoonPayTask;
import wannabit.io.cosmostaion.utils.FetchCallBack;
import wannabit.io.cosmostaion.utils.WLog;
import wannabit.io.cosmostaion.utils.WUtil;

public class BaseActivity extends AppCompatActivity implements IEnrichableActivity {
    private Injector injector;

    protected BaseApplication mApplication;
    protected BaseData mData;
    protected WaitDialogFragment waitDialogFragment;

    public View rootView;
    public Account account;
    public BaseChain baseChain;

    protected int mTaskCount;

    protected final CompositeDisposable compositeDisposable = new CompositeDisposable();

    // tmp
    private BinanceInteractor binanceInteractor;
    private OkexInteractor okexInteractor;
    private GrpcInteractor grpcInteractor;
    private StationInteractor stationInteractor;

    @Override
    public void setAppInjector(@NonNull Injector appInjector) {
        this.injector = appInjector;
    }

    @NonNull
    @Override
    public Injector getAppInjector() {
        return injector;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = findViewById(android.R.id.content);
        binanceInteractor = getAppInjector().getInstance(BinanceInteractor.class);
        okexInteractor = getAppInjector().getInstance(OkexInteractor.class);
        grpcInteractor = getAppInjector().getInstance(GrpcInteractor.class);
        stationInteractor = getAppInjector().getInstance(StationInteractor.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!(this instanceof PasswordCheckActivity)) {
            if (getBaseApplication().needShowLockScreen()) {
                Intent intent = new Intent(BaseActivity.this, AppLockActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.fade_out);
            }
        }
    }

    @Override
    protected void onDestroy() {
        ViewExtensionsKt.clearUi(this);
        compositeDisposable.clear();
        super.onDestroy();
    }

    public BaseApplication getBaseApplication() {
        if (mApplication == null)
            mApplication = (BaseApplication) getApplication();
        return mApplication;
    }

    public BaseData getBaseDao() {
        if (mData == null)
            mData = getBaseApplication().getBaseDao();
        return mData;
    }

    public void showWaitDialog() {
        if (waitDialogFragment == null) {
            waitDialogFragment = WaitDialogFragment.Companion.newInstance();
        }
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("wait");
        if (fragment == null || !fragment.isAdded()) {
            showDialog(waitDialogFragment, "wait", false);
        }
    }

    public void hideWaitDialog() {
        if (waitDialogFragment != null) {
            waitDialogFragment.dismissAllowingStateLoss();
        }
    }

    public void startMainActivity(int page) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("page", page);
        startActivity(intent);
    }

    public void startSendMainDenom() {
        if (account == null) return;
        if (!account.hasPrivateKey) {
            Dialog_WatchMode add = Dialog_WatchMode.newInstance();
            showDialog(add);
            return;
        }

        Intent intent = new Intent(getBaseContext(), SendActivity.class);
        BigDecimal mainAvailable;
        if (baseChain.isGRPC()) {
            mainAvailable = getBaseDao().getAvailable(baseChain.getMainDenom());
        } else {
            mainAvailable = getBaseDao().availableAmount(baseChain.getMainDenom());
        }
        BigDecimal feeAmount = WUtil.getEstimateGasFeeAmount(getBaseContext(), baseChain, CONST_PW_TX_SIMPLE_SEND, 0);
        if (mainAvailable.compareTo(feeAmount) <= 0) {
            Toast.makeText(getBaseContext(), R.string.error_not_enough_budget, Toast.LENGTH_SHORT).show();
            return;
        }
        intent.putExtra("sendTokenDenom", baseChain.getMainDenom());
        startActivity(intent);

    }

    public void startHTLCSendActivity(String sendDenom) {
//        WLog.w("onStartHTLCSendActivity " + mBaseChain.getChain() + " " + sendDenom);
        if (account == null) return;
        if (!SUPPORT_BEP3_SWAP) {
            Toast.makeText(getBaseContext(), R.string.error_bep3_swap_temporary_disable, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!account.hasPrivateKey) {
            Dialog_WatchMode add = Dialog_WatchMode.newInstance();
            showDialog(add);
            return;
        }

        boolean hasBalance = true;
        BigDecimal mainDenomAvailable = getBaseDao().availableAmount(baseChain.getMainDenom());
        if (baseChain.equals(BaseChain.BNB_MAIN.INSTANCE.INSTANCE)) {
            if (mainDenomAvailable.compareTo(new BigDecimal(FEE_BNB_SEND)) <= 0) {
                hasBalance = false;
            }
        } else if (baseChain.equals(BaseChain.KAVA_MAIN.INSTANCE.INSTANCE)) {
            BigDecimal mainAvailable = getBaseDao().getAvailable(baseChain.getMainDenom());
            BigDecimal feeAmount = WUtil.getEstimateGasFeeAmount(getBaseContext(), baseChain, CONST_PW_TX_HTLS_REFUND, 0);
            if (mainAvailable.subtract(feeAmount).compareTo(BigDecimal.ZERO) <= 0) {
                hasBalance = false;
            }
        }
        if (!hasBalance) {
            Toast.makeText(getBaseContext(), R.string.error_not_enough_budget_bep3, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(getBaseContext(), HtlcSendActivity.class);
        intent.putExtra("toSwapDenom", sendDenom);
        startActivity(intent);
    }

    public void onAddMnemonicForAccount() {
        startActivity(new Intent(BaseActivity.this, MnemonicRestoreActivity.class));
    }

    public void onCheckIbcTransfer(String denom) {
        Intent intent = new Intent(BaseActivity.this, IBCSendActivity.class);
        intent.putExtra("sendTokenDenom", denom);
        startActivity(intent);
    }

    public void fetchAllData(FetchCallBack callback) {
        getBaseDao().clear();

        Completable completable;

        if (baseChain.isGRPC()) {
            completable = updateGrpcAccount();
        } else if (baseChain == BaseChain.BNB_MAIN.INSTANCE) {
            completable = updateBinanceAccount();
        } else if (baseChain == BaseChain.OKEX_MAIN.INSTANCE) {
            completable = updateOkexAccount();
        } else {
            completable = Completable.error(new IllegalStateException());
        }

        Disposable disposable = completable
                .andThen(stationInteractor.updatePrices(baseChain))
                .subscribeOn(AppSchedulers.INSTANCE.io())
                .observeOn(AppSchedulers.INSTANCE.ui())
                .doOnError(error -> {
                    WLog.e(error.toString());
                    error.printStackTrace();
                })
                .subscribe(
                        callback::fetchFinished,
                        error -> {
                            Toast.makeText(getBaseContext(), R.string.str_network_error_msg, Toast.LENGTH_SHORT).show();
                            callback.fetchBusy();
                        }
                );
        compositeDisposable.add(disposable);
    }

    private Completable updateBinanceAccount() {
        return binanceInteractor
                .update(account, baseChain)
                .andThen(updateBalance());
    }

    private Completable updateOkexAccount() {
        return okexInteractor
                .update(account, baseChain)
                .andThen(updateBalance());
    }

    private Completable updateGrpcAccount() {
        return grpcInteractor
                .update(account, baseChain)
                .andThen(updateGrpcBalance());
    }

    private Completable updateBalance() {
        return Completable.fromCallable(() -> {
            // TODO: It will be refactored
            getBaseDao().mBalances = getBaseDao().onSelectBalance(account.id);
            return true;
        });
    }

    private Completable updateGrpcBalance() {
        return Completable.fromCallable(() -> {
            // TODO: It will be refactored
            if (getBaseDao().mGRpcAccount != null && !getBaseDao().mGRpcAccount.getTypeUrl().contains(Auth.BaseAccount.getDescriptor().getFullName())) {
                WUtil.onParseVestingAccount(getBaseDao(), baseChain);
            }
            return true;
        });
    }

    public void showDialog(DialogFragment dialogFragment) {
        showDialog(dialogFragment, "dialog", true);
    }

    public void showDialog(DialogFragment dialogFragment, String tag, boolean cancelable) {
        dialogFragment.setCancelable(cancelable);
        getSupportFragmentManager()
                .beginTransaction()
                .add(dialogFragment, tag)
                .commitNowAllowingStateLoss();
    }

    public void onShowBuyWarnNoKey() {
        Dialog_Buy_Without_Key dialog = Dialog_Buy_Without_Key.newInstance();
        showDialog(dialog, "wait", true);
    }

    public void onShowBuySelectFiat() {
        Dialog_Buy_Select_Fiat dialog = Dialog_Buy_Select_Fiat.newInstance();
        showDialog(dialog, "wait", true);
    }

    public void onStartMoonpaySignature(String fiat) {
        String query = "?apiKey=" + getString(R.string.moon_pay_public_key);
        if (baseChain.equals(BaseChain.COSMOS_MAIN.INSTANCE)) {
            query = query + "&currencyCode=atom";
        } else if (baseChain.equals(BaseChain.BNB_MAIN.INSTANCE)) {
            query = query + "&currencyCode=bnb";
        } else if (baseChain.equals(BaseChain.KAVA_MAIN.INSTANCE)) {
            query = query + "&currencyCode=kava";
        } else if (baseChain.equals(BaseChain.BAND_MAIN.INSTANCE)) {
            query = query + "&currencyCode=band";
        }
        query = query + "&walletAddress=" + account.address + "&baseCurrencyCode=" + fiat;
        final String data = query;

        new MoonPayTask(getBaseApplication(), result -> {
            if (result.isSuccess) {
                try {
                    String en = URLEncoder.encode((String) result.resultData, "UTF-8");
                    Intent guideIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_moon_pay) + data + "&signature=" + en));
                    startActivity(guideIntent);
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), R.string.error_network_error, Toast.LENGTH_SHORT).show();

                }

            } else {
                Toast.makeText(getBaseContext(), R.string.error_network_error, Toast.LENGTH_SHORT).show();

            }
        }, query).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }
}
