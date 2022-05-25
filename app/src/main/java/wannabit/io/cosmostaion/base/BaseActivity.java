package wannabit.io.cosmostaion.base;

import static wannabit.io.cosmostaion.base.BaseConstant.CONST_PW_TX_HTLS_REFUND;
import static wannabit.io.cosmostaion.base.BaseConstant.CONST_PW_TX_SIMPLE_SEND;
import static wannabit.io.cosmostaion.base.BaseConstant.FEE_BNB_SEND;
import static wannabit.io.cosmostaion.base.BaseConstant.SUPPORT_BEP3_SWAP;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.fulldive.wallet.di.IEnrichableActivity;
import com.fulldive.wallet.extensions.ViewExtensionsKt;
import com.fulldive.wallet.interactors.accounts.AccountsInteractor;
import com.fulldive.wallet.interactors.balances.BalancesInteractor;
import com.fulldive.wallet.interactors.chains.StationInteractor;
import com.fulldive.wallet.interactors.chains.binance.BinanceInteractor;
import com.fulldive.wallet.interactors.chains.grpc.GrpcInteractor;
import com.fulldive.wallet.interactors.chains.okex.OkexInteractor;
import com.fulldive.wallet.interactors.settings.SettingsInteractor;
import com.fulldive.wallet.models.BaseChain;
import com.fulldive.wallet.models.WalletBalance;
import com.fulldive.wallet.presentation.accounts.restore.MnemonicRestoreActivity;
import com.fulldive.wallet.presentation.main.MainActivity;
import com.fulldive.wallet.presentation.system.WaitDialogFragment;
import com.fulldive.wallet.rx.AppSchedulers;
import com.joom.lightsaber.Injector;

import java.math.BigDecimal;
import java.net.URLEncoder;

import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.activities.AppLockActivity;
import wannabit.io.cosmostaion.activities.HtlcSendActivity;
import wannabit.io.cosmostaion.activities.PasswordCheckActivity;
import wannabit.io.cosmostaion.activities.SendActivity;
import wannabit.io.cosmostaion.activities.chains.ibc.IBCSendActivity;
import wannabit.io.cosmostaion.dao.Account;
import wannabit.io.cosmostaion.dao.Price;
import wannabit.io.cosmostaion.dialog.Dialog_Buy_Select_Fiat;
import wannabit.io.cosmostaion.dialog.Dialog_Buy_Without_Key;
import wannabit.io.cosmostaion.dialog.Dialog_WatchMode;
import wannabit.io.cosmostaion.task.FetchTask.MoonPayTask;
import wannabit.io.cosmostaion.utils.FetchCallBack;
import wannabit.io.cosmostaion.utils.WLog;

public class BaseActivity extends AppCompatActivity implements IEnrichableActivity {
    private Injector injector;

    private BaseApplication mApplication;
    private BaseData mData;
    private WaitDialogFragment waitDialogFragment;

    public View rootView;
    protected String accountChainName = null;
    protected BaseChain baseChain;

    protected int mTaskCount;

    protected final CompositeDisposable compositeDisposable = new CompositeDisposable();

    // tmp
    protected AccountsInteractor accountsInteractor;
    protected SettingsInteractor settingsInteractor;
    protected BinanceInteractor binanceInteractor;
    protected OkexInteractor okexInteractor;
    protected GrpcInteractor grpcInteractor;
    protected StationInteractor stationInteractor;
    protected BalancesInteractor balancesInteractor;

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
        accountsInteractor = getAppInjector().getInstance(AccountsInteractor.class);
        settingsInteractor = getAppInjector().getInstance(SettingsInteractor.class);
        binanceInteractor = getAppInjector().getInstance(BinanceInteractor.class);
        okexInteractor = getAppInjector().getInstance(OkexInteractor.class);
        grpcInteractor = getAppInjector().getInstance(GrpcInteractor.class);
        stationInteractor = getAppInjector().getInstance(StationInteractor.class);
        balancesInteractor = getAppInjector().getInstance(BalancesInteractor.class);
        rootView = findViewById(android.R.id.content);
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

    public Account getAccount() {
        return accountsInteractor.getCurrentAccount().blockingGet();
    }

    public BaseChain getBaseChain() {
        final Account account = getAccount();
        if (baseChain == null || !account.baseChain.equals(accountChainName)) {
            baseChain = BaseChain.getChain(account.baseChain);
            accountChainName = account.baseChain;
        }
        return baseChain;
    }

    public BaseData getBaseDao() {
        if (mData == null)
            mData = getBaseApplication().getBaseDao();
        return mData;
    }

    public void showWaitDialog() {
        try {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("wait");
            if (fragment == null || !fragment.isAdded()) {
                if (waitDialogFragment == null) {
                    waitDialogFragment = WaitDialogFragment.Companion.newInstance();
                }
                showDialog(waitDialogFragment, "wait", false);
            }
        } catch (Exception ex) {
            WLog.e(ex.toString());
        }
    }

    public void hideWaitDialog() {
        if (waitDialogFragment != null) {
            try {
                waitDialogFragment.dismissAllowingStateLoss();
            } catch (Exception exception) {
                WLog.e(exception.getMessage());
                exception.printStackTrace();
            }
        }
        waitDialogFragment = null;
    }

    public void startMainActivity(int page) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("page", page);
        startActivity(intent);
    }

    public BigDecimal getBalance(String denom) {
        return getFullBalance(denom).getBalanceAmount();
    }

    public void startSendMainDenom() {
        final Account account = getAccount();
        if (account == null) return;
        if (!account.hasPrivateKey) {
            Dialog_WatchMode add = Dialog_WatchMode.newInstance();
            showDialog(add);
            return;
        }

        Intent intent = new Intent(getBaseContext(), SendActivity.class);
        final BaseChain chain = getBaseChain();
        final String mainDenom = chain.getMainDenom();
        BigDecimal mainAvailable = BigDecimal.ZERO;
        if (getBaseChain().isGRPC()) {
            mainAvailable = getBalance(getBaseChain().getMainDenom());
        } else {
            try {
                mainAvailable = getBalance(mainDenom);
            } catch (Exception exception) {
                WLog.e(exception.toString());
            }
        }
        BigDecimal feeAmount = getBaseChain().getGasFeeEstimateCalculator().calc(getBaseChain(), CONST_PW_TX_SIMPLE_SEND, 0);
        if (mainAvailable.compareTo(feeAmount) <= 0) {
            Toast.makeText(getBaseContext(), R.string.error_not_enough_budget, Toast.LENGTH_SHORT).show();
            return;
        }
        intent.putExtra("sendTokenDenom", getBaseChain().getMainDenom());
        startActivity(intent);

    }

    public void startHTLCSendActivity(String sendDenom) {
//        WLog.w("onStartHTLCSendActivity " + mBaseChain.getChain() + " " + sendDenom);
        if (getAccount() == null) return;
        if (!SUPPORT_BEP3_SWAP) {
            Toast.makeText(getBaseContext(), R.string.error_bep3_swap_temporary_disable, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!getAccount().hasPrivateKey) {
            Dialog_WatchMode add = Dialog_WatchMode.newInstance();
            showDialog(add);
            return;
        }

        boolean hasBalance = true;
        BigDecimal mainDenomAvailable = getBalance(getBaseChain().getMainDenom());
        if (getBaseChain().equals(BaseChain.BNB_MAIN.INSTANCE)) {
            if (mainDenomAvailable.compareTo(new BigDecimal(FEE_BNB_SEND)) <= 0) {
                hasBalance = false;
            }
        } else if (getBaseChain().equals(BaseChain.KAVA_MAIN.INSTANCE)) {
            BigDecimal mainAvailable = getBalance(getBaseChain().getMainDenom());
            BigDecimal feeAmount = getBaseChain().getGasFeeEstimateCalculator().calc(getBaseChain(), CONST_PW_TX_HTLS_REFUND, 0);
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

        if (getBaseChain().isGRPC()) {
            completable = updateGrpcAccount();
        } else if (getBaseChain() == BaseChain.BNB_MAIN.INSTANCE) {
            completable = updateBinanceAccount();
        } else if (getBaseChain() == BaseChain.OKEX_MAIN.INSTANCE) {
            completable = updateOkexAccount();
        } else {
            completable = Completable.error(new IllegalStateException());
        }

        Disposable disposable = completable
                .andThen(stationInteractor.updatePrices(getBaseChain()))
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

    @Nullable
    public Price getPrice(String denom) {
        Price price = null;
        try {
            price = stationInteractor.getPrice(baseChain, denom).blockingGet();
        } catch (Exception ignore) {
        }
        return price;
    }

    public WalletBalance getFullBalance(String denom) {
        WalletBalance result;
        final Account account = getAccount();
        try {
            result = balancesInteractor.getBalance(account.id, denom).blockingGet();
        } catch (Exception exception) {
            WLog.e(exception.toString());
            exception.printStackTrace();
            result = WalletBalance.Companion.create(0L, 0L, denom, "", "", "", System.currentTimeMillis());
        }
        return result;
    }

    @SuppressLint("CheckResult")
    public boolean hasBalance(String denom) {
        boolean result = false;
        final Account account = getAccount();
        try {
            balancesInteractor.getBalance(account.id, denom).blockingGet();
            result = true;
        } catch (Exception ignore) {
        }
        return result;
    }

    private Completable updateBinanceAccount() {
        return accountsInteractor.getCurrentAccount()
                .flatMapCompletable(account ->
                        binanceInteractor
                                .update(account)
                                .andThen(updateBalance(BaseChain.BNB_MAIN.INSTANCE, account))
                );
    }

    private Completable updateOkexAccount() {
        return accountsInteractor.getCurrentAccount()
                .flatMapCompletable(account -> okexInteractor
                        .update(account)
                        .andThen(updateBalance(BaseChain.OKEX_MAIN.INSTANCE, account))
                );
    }

    private Completable updateGrpcAccount() {
        return accountsInteractor.getCurrentAccount()
                .flatMapCompletable(account -> grpcInteractor
                        .update(account, getBaseChain())
                        .andThen(updateBalance(getBaseChain(), account))
                );
    }

    private Completable updateBalance(final BaseChain chain, final Account account) {
        return balancesInteractor
                .requestBalances(chain, account.address, account.id)
                .ignoreElement();
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
        if (getBaseChain().equals(BaseChain.COSMOS_MAIN.INSTANCE)) {
            query = query + "&currencyCode=atom";
        } else if (getBaseChain().equals(BaseChain.BNB_MAIN.INSTANCE)) {
            query = query + "&currencyCode=bnb";
        } else if (getBaseChain().equals(BaseChain.KAVA_MAIN.INSTANCE)) {
            query = query + "&currencyCode=kava";
        } else if (getBaseChain().equals(BaseChain.BAND_MAIN.INSTANCE)) {
            query = query + "&currencyCode=band";
        }
        query = query + "&walletAddress=" + getAccount().address + "&baseCurrencyCode=" + fiat;
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
