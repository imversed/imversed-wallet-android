package com.fulldive.wallet.presentation.main;

import static com.fulldive.wallet.models.BaseChain.KAVA_MAIN;
import static com.fulldive.wallet.models.BaseChain.OKEX_MAIN;
import static com.fulldive.wallet.models.BaseChain.SIF_MAIN;
import static wannabit.io.cosmostaion.base.BaseConstant.CONST_PW_PURPOSE;
import static wannabit.io.cosmostaion.base.BaseConstant.CONST_PW_SIMPLE_CHECK;
import static wannabit.io.cosmostaion.base.BaseConstant.CONST_PW_TX_CLAIM_INCENTIVE;
import static wannabit.io.cosmostaion.base.BaseConstant.CONST_PW_TX_SIF_CLAIM_INCENTIVE;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_HARD;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_KAVA;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_SWP;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.fulldive.wallet.models.BaseChain;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.math.BigDecimal;

import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.activities.PasswordCheckActivity;
import wannabit.io.cosmostaion.activities.WalletConnectActivity;
import wannabit.io.cosmostaion.activities.WalletSwitchActivity;
import wannabit.io.cosmostaion.activities.chains.kava.ClaimIncentiveActivity;
import wannabit.io.cosmostaion.activities.chains.sif.SifIncentiveActivity;
import wannabit.io.cosmostaion.appextensions.PopupManager;
import wannabit.io.cosmostaion.base.BaseActivity;
import wannabit.io.cosmostaion.base.BaseData;
import wannabit.io.cosmostaion.base.IBusyFetchListener;
import wannabit.io.cosmostaion.base.IRefreshTabListener;
import wannabit.io.cosmostaion.dao.Account;
import wannabit.io.cosmostaion.dialog.Dialog_WalletConnect;
import wannabit.io.cosmostaion.dialog.Dialog_WatchMode;
import wannabit.io.cosmostaion.model.kava.IncentiveReward;
import wannabit.io.cosmostaion.utils.FetchCallBack;
import wannabit.io.cosmostaion.utils.WDp;
import wannabit.io.cosmostaion.utils.WUtil;
import wannabit.io.cosmostaion.widget.FadePageTransformer;
import wannabit.io.cosmostaion.widget.LockedViewPager;
import wannabit.io.cosmostaion.widget.TintableImageView;

public class MainActivity extends BaseActivity implements FetchCallBack {

    private ImageView toolbarChainImageView;
    private TextView toolbarTitleTextView;
    private TextView toolbarChainTextView;

    public MainViewPageAdapter adapter;
    public FloatingActionButton floatingActionButton;

    private String lastAccountChainName = null;
    private long lastAccountCheckId = -1L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbarTitleTextView = findViewById(R.id.toolbarTitleTextView);
        toolbarChainImageView = findViewById(R.id.toolbarChainImageView);
        toolbarChainTextView = findViewById(R.id.toolbarChainTextView);
        floatingActionButton = findViewById(R.id.floatingActionButton);

        Toolbar toolbar = findViewById(R.id.toolbar);
        LockedViewPager viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayer = findViewById(R.id.bottom_tab);

        floatingActionButton.setOnClickListener(v -> startSendMainDenom());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        adapter = new MainViewPageAdapter(getSupportFragmentManager());
        viewPager.setPageTransformer(false, new FadePageTransformer());
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
        tabLayer.setupWithViewPager(viewPager);
        tabLayer.setTabRippleColor(null);

        createTab(tabLayer, 0, R.drawable.wallet_ic, R.string.str_main_wallet);
        createTab(tabLayer, 1, R.drawable.tokens_ic, R.string.str_main_tokens);
        createTab(tabLayer, 2, R.drawable.ts_ic, R.string.str_main_history);
        createTab(tabLayer, 3, R.drawable.setting_ic, R.string.str_main_set);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }

            @Override
            public void onPageSelected(int position) {
                if (adapter != null && adapter.getCurrentFragment() != null) {
                    ((IRefreshTabListener) adapter.getCurrentFragment()).onRefreshTab();
                }
                if (position != 0) floatingActionButton.hide();
                else if (!floatingActionButton.isShown()) floatingActionButton.show();
            }
        });

        viewPager.setCurrentItem(getIntent().getIntExtra("page", 0), false);
        PopupManager.INSTANCE.onAppStarted(this);
    }

    private void createTab(TabLayout tabLayer, int index, @DrawableRes int iconResId, @StringRes int titleResId) {
        View tabView = LayoutInflater.from(this).inflate(R.layout.view_tab_item, null);
        TintableImageView tabItemIconView = tabView.findViewById(R.id.tabItemIcon);
        TextView tabItemTextView = tabView.findViewById(R.id.tabItemText);
        tabItemIconView.setImageResource(iconResId);
        tabItemTextView.setText(titleResId);
        tabLayer.getTabAt(index).setCustomView(tabView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAccountChanges();
        checkChainChanges();
    }

    public void checkAccountChanges() {
        final Account account = getAccount();

        if (lastAccountCheckId != account.id) {
            lastAccountCheckId = account.id;
            final BaseChain baseChain = getBaseChain();

            showWaitDialog();
            onFetchAllData();

            toolbarChainImageView.setImageResource(baseChain.getChainIcon());
            WDp.getChainHint(baseChain, toolbarChainTextView);
            toolbarChainTextView.setTextColor(WDp.getChainColor(this, baseChain));

            floatingActionButton.setImageTintList(ContextCompat.getColorStateList(this, baseChain.getFloatButtonColor()));
            floatingActionButton.setBackgroundTintList(ContextCompat.getColorStateList(this, baseChain.getFloatButtonBackground()));

            checkChainChanges();
        }

        toolbarTitleTextView.setText(account.getAccountTitle(this));
    }

    private void checkChainChanges() {
        final String chainName = getBaseChain().getChainName();
        if (!chainName.equals(lastAccountChainName)) {
            invalidateOptionsMenu();
            lastAccountChainName = chainName;
        }
    }

    public void onClickSwitchWallet() {
        startActivity(new Intent(this, WalletSwitchActivity.class));
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void onExplorerView() {
        String url = "";
        if (getBaseChain().equals(OKEX_MAIN.INSTANCE)) {
            url = WUtil.getExplorer(getBaseChain()) + "address/" + getAccount().address;
        } else {
            url = WUtil.getExplorer(getBaseChain()) + "account/" + getAccount().address;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public void onFetchAllData() {
        fetchAllData(this);
    }

    public void onStartBinanceWalletConnect(String wcUrl) {
        Intent intent = new Intent(this, PasswordCheckActivity.class);
        intent.putExtra(CONST_PW_PURPOSE, CONST_PW_SIMPLE_CHECK);
        intent.putExtra("wcUrl", wcUrl);
        startActivityForResult(intent, CONST_PW_SIMPLE_CHECK);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.fade_out);
    }

    @Override
    public void fetchFinished() {
        if (!isFinishing()) {
            hideWaitDialog();
            for (Fragment fragment : adapter.getFragments()) {
                if (fragment instanceof IRefreshTabListener) {
                    ((IRefreshTabListener) fragment).onRefreshTab();
                }
            }
        }
    }

    @Override
    public void fetchBusy() {
        if (!isFinishing()) {
            hideWaitDialog();
            final Fragment fragment = adapter.getCurrentFragment();
            if (fragment instanceof IBusyFetchListener) {
                ((IBusyFetchListener) fragment).onBusyFetch();
            }
        }
    }

    public void onClickIncentive() {
        if (!getAccount().hasPrivateKey) {
            Dialog_WatchMode add = Dialog_WatchMode.newInstance();
            showDialog(add);
            return;
        }
        final BaseData baseData = getBaseDao();
        if (getBaseChain().equals(KAVA_MAIN.INSTANCE)) {
            BigDecimal available = baseData.getAvailable(getBaseChain().getMainDenom());
            BigDecimal txFee = WUtil.getEstimateGasFeeAmount(this, getBaseChain(), CONST_PW_TX_CLAIM_INCENTIVE, 0);
            if (available.compareTo(txFee) <= 0) {
                Toast.makeText(this, R.string.error_not_enough_fee, Toast.LENGTH_SHORT).show();
                return;
            }
            final IncentiveReward incentiveReward = baseData.mIncentiveRewards;
            if (incentiveReward.getRewardSum(TOKEN_KAVA).equals(BigDecimal.ZERO) && incentiveReward.getRewardSum(TOKEN_HARD).equals(BigDecimal.ZERO) &&
                    incentiveReward.getRewardSum(TOKEN_SWP).equals(BigDecimal.ZERO)) {
                Toast.makeText(this, R.string.error_no_incentive_to_claim, Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(MainActivity.this, ClaimIncentiveActivity.class);
            startActivity(intent);

        } else if (getBaseChain().equals(SIF_MAIN.INSTANCE)) {
            BigDecimal available = baseData.getAvailable(getBaseChain().getMainDenom());
            BigDecimal txFee = WUtil.getEstimateGasFeeAmount(this, getBaseChain(), CONST_PW_TX_SIF_CLAIM_INCENTIVE, 0);
            if (available.compareTo(txFee) <= 0) {
                Toast.makeText(this, R.string.error_not_enough_fee, Toast.LENGTH_SHORT).show();
                return;
            }
            if (baseData.mSifLmIncentive == null || baseData.mSifLmIncentive.totalClaimableCommissionsAndClaimableRewards == 0) {
                Toast.makeText(this, R.string.error_no_incentive_to_claim, Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(MainActivity.this, SifIncentiveActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CONST_PW_SIMPLE_CHECK && resultCode == RESULT_OK && !TextUtils.isEmpty(data.getStringExtra("wcUrl"))) {
            Intent wcIntent = new Intent(this, WalletConnectActivity.class);
            wcIntent.putExtra("wcUrl", data.getStringExtra("wcUrl"));
            startActivity(wcIntent);

        } else {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null && result.getContents() != null && result.getContents().trim().contains("wallet-bridge.binance.org")) {
                Bundle bundle = new Bundle();
                bundle.putString("wcUrl", result.getContents().trim());
                Dialog_WalletConnect dialog = Dialog_WalletConnect.newInstance(bundle);
                showDialog(dialog);
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
