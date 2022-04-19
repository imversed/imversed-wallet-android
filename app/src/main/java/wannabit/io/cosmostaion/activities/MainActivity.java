package wannabit.io.cosmostaion.activities;

import static com.fulldive.wallet.models.BaseChain.KAVA_MAIN;
import static com.fulldive.wallet.models.BaseChain.OKEX_MAIN;
import static com.fulldive.wallet.models.BaseChain.SIF_MAIN;
import static wannabit.io.cosmostaion.base.BaseConstant.CONST_PW_PURPOSE;
import static wannabit.io.cosmostaion.base.BaseConstant.CONST_PW_SIMPLE_CHECK;
import static wannabit.io.cosmostaion.base.BaseConstant.CONST_PW_TX_CLAIM_INCENTIVE;
import static wannabit.io.cosmostaion.base.BaseConstant.CONST_PW_TX_PROFILE;
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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.fulldive.wallet.presentation.main.history.MainHistoryFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import desmos.profiles.v1beta1.ModelsProfile;
import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.activities.chains.desmos.ProfileActivity;
import wannabit.io.cosmostaion.activities.chains.desmos.ProfileDetailActivity;
import wannabit.io.cosmostaion.activities.chains.kava.ClaimIncentiveActivity;
import wannabit.io.cosmostaion.activities.chains.sif.SifIncentiveActivity;
import wannabit.io.cosmostaion.appextensions.PopupManager;
import wannabit.io.cosmostaion.base.BaseActivity;
import com.fulldive.wallet.models.BaseChain;
import wannabit.io.cosmostaion.base.BaseData;
import wannabit.io.cosmostaion.base.IBusyFetchListener;
import wannabit.io.cosmostaion.base.IRefreshTabListener;
import wannabit.io.cosmostaion.dao.Account;
import wannabit.io.cosmostaion.dialog.Dialog_WalletConnect;
import wannabit.io.cosmostaion.dialog.Dialog_WatchMode;
import wannabit.io.cosmostaion.fragment.MainSendFragment;
import wannabit.io.cosmostaion.fragment.MainSettingFragment;
import wannabit.io.cosmostaion.fragment.MainTokensFragment;
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

    private BaseChain mSelectedChain;

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
                if (adapter != null && adapter.currentFragment != null) {
                    ((IRefreshTabListener) adapter.currentFragment).onRefreshTab();
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
        onAccountSwitched();
        onChainSelect(baseChain);
    }

    public void onAccountSwitched() {
        boolean needFetch = false;
        Account supportedAccount = getBaseDao().getAccount(getBaseDao().getLastUser());

        if (account == null) {
            needFetch = true;
        } else if (!supportedAccount.id.toString().equals(getBaseDao().getLastUser())) {
            getBaseDao().setLastUser(supportedAccount.id);
            needFetch = true;
        } else if (!getBaseDao().getLastUser().equals(account.id.toString())) {
            needFetch = true;
        }

        account = getBaseDao().getAccount(getBaseDao().getLastUser());
        baseChain = BaseChain.getChain(account.baseChain);
        if (needFetch) {
            showWaitDialog();
            onFetchAllData();

            toolbarChainImageView.setImageResource(baseChain.getChainIcon());
            WDp.getChainHint(baseChain, toolbarChainTextView);
            toolbarChainTextView.setTextColor(WDp.getChainColor(this, baseChain));

            floatingActionButton.setImageTintList(ContextCompat.getColorStateList(this, baseChain.getFloatButtonColor()));
            floatingActionButton.setBackgroundTintList(ContextCompat.getColorStateList(this, baseChain.getFloatButtonBackground()));

            mSelectedChain = baseChain;
            onChainSelect(mSelectedChain);
        }

        toolbarTitleTextView.setText(account.getAccountTitle(this));
    }

    private void onChainSelect(BaseChain baseChain) {
        invalidateOptionsMenu();
        mSelectedChain = baseChain;
        getBaseDao().setLastChain(mSelectedChain.getChainName());
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
        if (baseChain.equals(OKEX_MAIN.INSTANCE)) {
            url = WUtil.getExplorer(baseChain) + "address/" + account.address;
        } else {
            url = WUtil.getExplorer(baseChain) + "account/" + account.address;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public void onFetchAllData() {
        fetchAllData(this);
    }

    public void onClickProfile() {
        if (getBaseDao().mGRpcNodeInfo != null && getBaseDao().mGRpcAccount != null) {
            if (getBaseDao().mGRpcAccount.getTypeUrl().contains(ModelsProfile.Profile.getDescriptor().getFullName())) {
                Intent airdrop = new Intent(this, ProfileDetailActivity.class);
                startActivity(airdrop);

            } else {
                if (!account.hasPrivateKey) {
                    Dialog_WatchMode dialog = Dialog_WatchMode.newInstance();
                    showDialog(dialog);
                    return;
                }
                BigDecimal available = getBaseDao().getAvailable(baseChain.getMainDenom());
                BigDecimal txFee = WUtil.getEstimateGasFeeAmount(this, baseChain, CONST_PW_TX_PROFILE, 0);
                if (available.compareTo(txFee) <= 0) {
                    Toast.makeText(this, R.string.error_not_enough_fee, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent profile = new Intent(this, ProfileActivity.class);
                startActivity(profile);
            }
        } else {
            if (!account.hasPrivateKey) {
                Dialog_WatchMode add = Dialog_WatchMode.newInstance();
                showDialog(add);
                return;
            }
            BigDecimal available = getBaseDao().getAvailable(baseChain.getMainDenom());
            BigDecimal txFee = WUtil.getEstimateGasFeeAmount(this, baseChain, CONST_PW_TX_PROFILE, 0);
            if (available.compareTo(txFee) <= 0) {
                Toast.makeText(this, R.string.error_not_enough_fee, Toast.LENGTH_SHORT).show();
                return;
            }
            Intent profile = new Intent(this, ProfileActivity.class);
            startActivity(profile);
        }
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
            Fragment fragment = adapter.currentFragment;
            if (fragment instanceof IBusyFetchListener) {
                ((IBusyFetchListener) adapter.currentFragment).onBusyFetch();
            }
        }
    }

    public void onClickIncentive() {
        if (!account.hasPrivateKey) {
            Dialog_WatchMode add = Dialog_WatchMode.newInstance();
            showDialog(add);
            return;
        }
        final BaseData baseData = getBaseDao();
        if (baseChain.equals(KAVA_MAIN.INSTANCE)) {
            BigDecimal available = baseData.getAvailable(baseChain.getMainDenom());
            BigDecimal txFee = WUtil.getEstimateGasFeeAmount(this, baseChain, CONST_PW_TX_CLAIM_INCENTIVE, 0);
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

        } else if (baseChain.equals(SIF_MAIN.INSTANCE)) {
            BigDecimal available = baseData.getAvailable(baseChain.getMainDenom());
            BigDecimal txFee = WUtil.getEstimateGasFeeAmount(this, baseChain, CONST_PW_TX_SIF_CLAIM_INCENTIVE, 0);
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

    private static class MainViewPageAdapter extends FragmentPagerAdapter {

        private final ArrayList<Fragment> fragments = new ArrayList<>();
        private Fragment currentFragment;

        public MainViewPageAdapter(FragmentManager fm) {
            super(fm);
            fragments.add(MainSendFragment.newInstance(null));
            fragments.add(MainTokensFragment.newInstance(null));
            fragments.add(MainHistoryFragment.Companion.newInstance());
            fragments.add(MainSettingFragment.newInstance(null));
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            if (getCurrentFragment() != object) {
                currentFragment = ((Fragment) object);
            }
            super.setPrimaryItem(container, position, object);
        }

        public Fragment getCurrentFragment() {
            return currentFragment;
        }

        public List<Fragment> getFragments() {
            return fragments;
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
