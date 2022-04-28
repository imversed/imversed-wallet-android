package wannabit.io.cosmostaion.fragment;

import static com.fulldive.wallet.models.BaseChain.BNB_MAIN;
import static com.fulldive.wallet.models.BaseChain.COSMOS_MAIN;
import static com.fulldive.wallet.models.BaseChain.CRYPTO_MAIN;
import static com.fulldive.wallet.models.BaseChain.CUDOS_MAIN;
import static com.fulldive.wallet.models.BaseChain.EMONEY_MAIN;
import static com.fulldive.wallet.models.BaseChain.EVMOS_MAIN;
import static com.fulldive.wallet.models.BaseChain.FETCHAI_MAIN;
import static com.fulldive.wallet.models.BaseChain.GRABRIDGE_MAIN;
import static com.fulldive.wallet.models.BaseChain.INJ_MAIN;
import static com.fulldive.wallet.models.BaseChain.JUNO_MAIN;
import static com.fulldive.wallet.models.BaseChain.KAVA_MAIN;
import static com.fulldive.wallet.models.BaseChain.OKEX_MAIN;
import static com.fulldive.wallet.models.BaseChain.OSMOSIS_MAIN;
import static com.fulldive.wallet.models.BaseChain.PROVENANCE_MAIN;
import static com.fulldive.wallet.models.BaseChain.SECRET_MAIN;
import static com.fulldive.wallet.models.BaseChain.SIF_MAIN;
import static wannabit.io.cosmostaion.base.BaseConstant.ASSET_IMG_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.BINANCE_TOKEN_IMG_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.COSMOS_COIN_IMG_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.EMONEY_COIN_IMG_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.KAVA_COIN_IMG_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.OKEX_COIN_IMG_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_HARD;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_HTLC_KAVA_BNB;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_HTLC_KAVA_BTCB;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_HTLC_KAVA_BUSD;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_HTLC_KAVA_XRPB;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_ION;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_SWP;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_USDX;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fulldive.wallet.interactors.balances.BalancesInteractor;
import com.fulldive.wallet.interactors.settings.SettingsInteractor;
import com.fulldive.wallet.models.BaseChain;
import com.fulldive.wallet.models.Currency;
import com.fulldive.wallet.models.WalletBalance;
import com.fulldive.wallet.presentation.accounts.AccountShowDialogFragment;
import com.fulldive.wallet.presentation.main.MainActivity;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import tendermint.liquidity.v1beta1.Liquidity;
import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.activities.tokenDetail.BridgeTokenGrpcActivity;
import wannabit.io.cosmostaion.activities.tokenDetail.ContractTokenGrpcActivity;
import wannabit.io.cosmostaion.activities.tokenDetail.IBCTokenDetailActivity;
import wannabit.io.cosmostaion.activities.tokenDetail.NativeTokenDetailActivity;
import wannabit.io.cosmostaion.activities.tokenDetail.NativeTokenGrpcActivity;
import wannabit.io.cosmostaion.activities.tokenDetail.POOLTokenDetailActivity;
import wannabit.io.cosmostaion.activities.tokenDetail.StakingTokenDetailActivity;
import wannabit.io.cosmostaion.activities.tokenDetail.StakingTokenGrpcActivity;
import wannabit.io.cosmostaion.base.BaseFragment;
import wannabit.io.cosmostaion.base.IBusyFetchListener;
import wannabit.io.cosmostaion.base.IRefreshTabListener;
import wannabit.io.cosmostaion.dao.Account;
import wannabit.io.cosmostaion.dao.Assets;
import wannabit.io.cosmostaion.dao.BnbToken;
import wannabit.io.cosmostaion.dao.Cw20Assets;
import wannabit.io.cosmostaion.dao.IbcToken;
import wannabit.io.cosmostaion.dao.OkToken;
import wannabit.io.cosmostaion.utils.PriceProvider;
import wannabit.io.cosmostaion.utils.WDp;
import wannabit.io.cosmostaion.utils.WUtil;

public class MainTokensFragment extends BaseFragment implements IBusyFetchListener, IRefreshTabListener {

    public final static int SECTION_NATIVE_GRPC = 0;
    public final static int SECTION_IBC_AUTHED_GRPC = 1;
    public final static int SECTION_OSMOSIS_POOL_GRPC = 2;
    public final static int SECTION_ETHER_GRPC = 3;
    public final static int SECTION_IBC_UNKNOWN_GRPC = 4;
    public final static int SECTION_GRAVICTY_DEX_GRPC = 5;
    public final static int SECTION_INJECTIVE_POOL_GRPC = 6;
    public final static int SECTION_KAVA_BEP2_GRPC = 7;
    public final static int SECTION_ETC_GRPC = 8;
    public final static int SECTION_CW20_GRPC = 9;
    public final static int SECTION_UNKNOWN_GRPC = 10;

    public final static int SECTION_NATIVE = 20;
    public final static int SECTION_ETC = 21;
    public final static int SECTION_UNKNOWN = 22;

    private CardView mCardView;
    private ImageView itemKeyStatus;
    private TextView mWalletAddress;
    private TextView mTotalValue;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private LinearLayout mEmptyToken;

    private TokensAdapter mTokensAdapter;

    private RecyclerViewHeader mRecyclerViewHeader;

    private final ArrayList<WalletBalance> mIbcAuthedGrpc = new ArrayList<>();
    private final ArrayList<WalletBalance> mOsmosisPoolGrpc = new ArrayList<>();
    private final ArrayList<WalletBalance> mEtherGrpc = new ArrayList<>();
    private final ArrayList<WalletBalance> mIbcUnknownGrpc = new ArrayList<>();
    private final ArrayList<WalletBalance> mGravityDexGrpc = new ArrayList<>();
    private final ArrayList<WalletBalance> mInjectivePoolGrpc = new ArrayList<>();
    private final ArrayList<WalletBalance> mKavaBep2Grpc = new ArrayList<>();
    private ArrayList<Cw20Assets> mCW20Grpc = new ArrayList<>();

    private final ArrayList<WalletBalance> mNative = new ArrayList<>();
    private final ArrayList<WalletBalance> mEtc = new ArrayList<>();
    private final ArrayList<WalletBalance> mUnknown = new ArrayList<>();

    private Account mAccount;
    private BaseChain mBaseChain;
    private SettingsInteractor settingsInteractor;
    private BalancesInteractor balancesInteractor;

    private PriceProvider priceProvider;

    public static MainTokensFragment newInstance(Bundle bundle) {
        MainTokensFragment fragment = new MainTokensFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsInteractor = getAppInjector().getInstance(SettingsInteractor.class);
        balancesInteractor = getAppInjector().getInstance(BalancesInteractor.class);
        priceProvider = getMainActivity()::getPrice;

        setHasOptionsMenu(true);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_tokens, container, false);
        mCardView = rootView.findViewById(R.id.cardView);
        itemKeyStatus = rootView.findViewById(R.id.img_account);
        mWalletAddress = rootView.findViewById(R.id.wallet_address);
        mTotalValue = rootView.findViewById(R.id.total_value);
        mSwipeRefreshLayout = rootView.findViewById(R.id.layer_refresher);
        mRecyclerView = rootView.findViewById(R.id.recycler);
        mEmptyToken = rootView.findViewById(R.id.empty_token);

        mCardView.setOnClickListener(v -> showAddressDialog());

        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(rootView.getContext(), R.color.colorPrimary));
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            onUpdateInfo();
            getMainActivity().onFetchAllData();
        });

        mRecyclerView.setOnTouchListener((View view, MotionEvent motionEvent) -> mSwipeRefreshLayout.isRefreshing());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        mTokensAdapter = new TokensAdapter();
        mRecyclerView.setAdapter(mTokensAdapter);

        mRecyclerViewHeader = new RecyclerViewHeader(getMainActivity(), true, getSectionGrpcCall());
        mRecyclerView.addItemDecoration(mRecyclerViewHeader);

        onUpdateInfo();
        return rootView;
    }

    @Override
    public void onDestroy() {
        priceProvider = null;
        super.onDestroy();
    }

    private void onUpdateInfo() {
        if (getMainActivity() == null || getMainActivity().getAccount() == null) return;
        mAccount = getMainActivity().getAccount();
        mBaseChain = BaseChain.getChain(mAccount.baseChain);

        mCardView.setCardBackgroundColor(WDp.getChainBgColor(getMainActivity(), mBaseChain));
        if (mAccount.hasPrivateKey) {
            itemKeyStatus.setColorFilter(WDp.getChainColor(getMainActivity(), mBaseChain), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            itemKeyStatus.setColorFilter(ContextCompat.getColor(getMainActivity(), R.color.colorGray0), android.graphics.PorterDuff.Mode.SRC_IN);
        }
        mWalletAddress.setText(mAccount.address);
        mTotalValue.setText(WDp.dpAllAssetValueUserCurrency(mBaseChain, getCurrency(), getBaseDao(), getBalances(), priceProvider));
    }

    private SectionCallback getSectionGrpcCall() {
        return new SectionCallback() {
            // 헤더 구분 true / false
            @Override
            public boolean isSection(BaseChain baseChain, int position) {
                if (baseChain.equals(OSMOSIS_MAIN.INSTANCE)) {
                    return position == 0 || position == mNative.size() || position == mNative.size() + mIbcAuthedGrpc.size()
                            || position == mNative.size() + mIbcAuthedGrpc.size() + mOsmosisPoolGrpc.size()
                            || position == mNative.size() + mIbcAuthedGrpc.size() + mOsmosisPoolGrpc.size() + mIbcUnknownGrpc.size();

                } else if (baseChain.equals(COSMOS_MAIN.INSTANCE)) {
                    return position == 0 || position == mNative.size() || position == mNative.size() + mIbcAuthedGrpc.size()
                            || position == mNative.size() + mIbcAuthedGrpc.size() + mGravityDexGrpc.size()
                            || position == mNative.size() + mIbcAuthedGrpc.size() + mGravityDexGrpc.size() + mIbcUnknownGrpc.size();

                } else if (baseChain.equals(SIF_MAIN.INSTANCE) || baseChain.equals(GRABRIDGE_MAIN.INSTANCE)) {
                    return position == 0 || position == mNative.size() || position == mNative.size() + mIbcAuthedGrpc.size()
                            || position == mNative.size() + mIbcAuthedGrpc.size() + mEtherGrpc.size()
                            || position == mNative.size() + mIbcAuthedGrpc.size() + mEtherGrpc.size() + mIbcUnknownGrpc.size();

                } else if (baseChain.equals(INJ_MAIN.INSTANCE)) {
                    return position == 0 || position == mNative.size() || position == mNative.size() + mIbcAuthedGrpc.size()
                            || position == mNative.size() + mIbcAuthedGrpc.size() + mEtherGrpc.size()
                            || position == mNative.size() + mIbcAuthedGrpc.size() + mEtherGrpc.size() + mInjectivePoolGrpc.size()
                            || position == mNative.size() + mIbcAuthedGrpc.size() + mEtherGrpc.size() + mInjectivePoolGrpc.size() + mIbcUnknownGrpc.size();

                } else if (baseChain.equals(KAVA_MAIN.INSTANCE)) {
                    return position == 0 || position == mNative.size() || position == mNative.size() + mIbcAuthedGrpc.size()
                            || position == mNative.size() + mIbcAuthedGrpc.size() + mKavaBep2Grpc.size()
                            || position == mNative.size() + mIbcAuthedGrpc.size() + mKavaBep2Grpc.size() + mEtc.size()
                            || position == mNative.size() + mIbcAuthedGrpc.size() + mKavaBep2Grpc.size() + mEtc.size() + mIbcUnknownGrpc.size();

                } else if (baseChain.equals(JUNO_MAIN.INSTANCE)) {
                    return position == 0 || position == mNative.size() || position == mNative.size() + mIbcAuthedGrpc.size()
                            || position == mNative.size() + mIbcAuthedGrpc.size() + mCW20Grpc.size()
                            || position == mNative.size() + mIbcAuthedGrpc.size() + mCW20Grpc.size() + mIbcUnknownGrpc.size();

                } else if (baseChain.isGRPC()) {
                    return position == 0 || position == mNative.size() || position == mNative.size() + mIbcAuthedGrpc.size()
                            || position == mNative.size() + mIbcAuthedGrpc.size() + mIbcUnknownGrpc.size();

                } else {
                    return position == 0 || position == mNative.size() || position == mNative.size() + mEtc.size();
                }
            }

            @Override
            public int getSectionHeader(BaseChain baseChain, int section) {
                switch (section) {
                    case SECTION_NATIVE_GRPC:
                    case SECTION_NATIVE:
                        return R.string.str_native_token_title;
                    case SECTION_IBC_AUTHED_GRPC:
                        return R.string.str_ibc_token_title;
                    case SECTION_IBC_UNKNOWN_GRPC:
                        return R.string.str_unknown_ibc_token_title;
                    case SECTION_UNKNOWN_GRPC:
                        return R.string.str_unknown_token_title;
                    case SECTION_OSMOSIS_POOL_GRPC:
                    case SECTION_INJECTIVE_POOL_GRPC:
                        return R.string.str_pool_coin_title;
                    case SECTION_ETHER_GRPC:
                        return R.string.str_sif_ether_token_title;
                    case SECTION_GRAVICTY_DEX_GRPC:
                        return R.string.str_gravity_dex_token_title;
                    case SECTION_KAVA_BEP2_GRPC:
                        return R.string.str_kava_bep2_token_title;
                    case SECTION_ETC_GRPC:
                        return R.string.str_etc_token_title;
                    case SECTION_CW20_GRPC:
                        return R.string.str_cw20_token_title;
                    case SECTION_ETC:
                        if (baseChain.equals(OKEX_MAIN.INSTANCE)) {
                            return R.string.str_oec_kip10_title;
                        }
                        return R.string.str_etc_token_title;
                }

                return R.string.str_unknown_token_title;
            }
        };
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        getMainActivity().getMenuInflater().inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_accounts:
                getMainActivity().onClickSwitchWallet();
                break;
            case R.id.menu_explorer:
                getMainActivity().onExplorerView();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefreshTab() {
        if (!isAdded()) return;
        mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView.getRecycledViewPool().clear();
        onUpdateInfo();
        onUpdateView();
    }

    @Override
    public void onBusyFetch() {
        if (!isAdded()) return;
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void onUpdateView() {
        final String mainDenom = getMainActivity().getBaseChain().getMainDenom();
        mCW20Grpc = getBaseDao().getCw20sGrpc(getMainActivity().getBaseChain());
        mIbcAuthedGrpc.clear();
        mOsmosisPoolGrpc.clear();
        mEtherGrpc.clear();
        mIbcUnknownGrpc.clear();
        mGravityDexGrpc.clear();
        mInjectivePoolGrpc.clear();
        mKavaBep2Grpc.clear();

        mNative.clear();
        mEtc.clear();

        final BaseChain chain = getMainActivity().getBaseChain();
        List<WalletBalance> balances = getBalances();

        for (WalletBalance balance : balances) {
            if (balance.getDenom().equalsIgnoreCase(mainDenom)) {
                mNative.add(balance);
            } else if (chain.equals(BNB_MAIN.INSTANCE)) {
                mEtc.add(balance);
            } else if (chain.equals(OKEX_MAIN.INSTANCE)) {
                mEtc.add(balance);
            } else if (balance.isIbc()) {
                final IbcToken ibcToken = getBaseDao().getIbcToken(balance.getIbcHash());
                if (ibcToken != null && ibcToken.auth) {
                    mIbcAuthedGrpc.add(balance);
                } else {
                    mIbcUnknownGrpc.add(balance);
                }
            } else if (chain.equals(OSMOSIS_MAIN.INSTANCE) && balance.osmosisAmm()) {
                mOsmosisPoolGrpc.add(balance);
            } else if (chain.equals(OSMOSIS_MAIN.INSTANCE) && balance.getDenom().equalsIgnoreCase(TOKEN_ION) ||
                    chain.equals(EMONEY_MAIN.INSTANCE) && balance.getDenom().startsWith("e")) {
                mNative.add(balance);
            } else if (chain.equals(SIF_MAIN.INSTANCE) && balance.getDenom().startsWith("c") ||
                    chain.equals(GRABRIDGE_MAIN.INSTANCE) && balance.getDenom().startsWith("gravity") ||
                    chain.equals(INJ_MAIN.INSTANCE) && balance.getDenom().startsWith("peggy")) {
                mEtherGrpc.add(balance);
            } else if (chain.equals(COSMOS_MAIN.INSTANCE) && balance.getDenom().startsWith("pool")) {
                mGravityDexGrpc.add(balance);
            } else if (chain.equals(INJ_MAIN.INSTANCE) && balance.getDenom().startsWith("share")) {
                mInjectivePoolGrpc.add(balance);
            } else if (chain.equals(KAVA_MAIN.INSTANCE)) {
                if (balance.getDenom().equals(TOKEN_HARD) || balance.getDenom().equalsIgnoreCase(TOKEN_USDX) || balance.getDenom().equalsIgnoreCase(TOKEN_SWP)) {
                    mNative.add(balance);
                } else if (balance.getDenom().equalsIgnoreCase(TOKEN_HTLC_KAVA_BNB) || balance.getDenom().equalsIgnoreCase(TOKEN_HTLC_KAVA_BTCB) ||
                        balance.getDenom().equalsIgnoreCase(TOKEN_HTLC_KAVA_XRPB) || balance.getDenom().equalsIgnoreCase(TOKEN_HTLC_KAVA_BUSD)) {
                    mKavaBep2Grpc.add(balance);
                } else if (balance.getDenom().equalsIgnoreCase("btch") || balance.getDenom().equalsIgnoreCase("hbtc")) {
                    mEtc.add(balance);
                }
            } else {
                mUnknown.add(balance);
            }
        }

        if (chain.isGRPC()) {
            WUtil.onSortingBalance(mNative, chain);
            WUtil.onSortingGravityPool(mGravityDexGrpc, getBaseDao());
            WUtil.onSortingOsmosisPool(mOsmosisPoolGrpc);
            WUtil.onSortingInjectivePool(mInjectivePoolGrpc);

        } else if (chain.equals(BNB_MAIN.INSTANCE) || chain.equals(OKEX_MAIN.INSTANCE)) {
            WUtil.onSortingNativeCoins(mEtc, chain);
        } else {
            WUtil.onSortingNativeCoins(mNative, chain);
        }

        if (!balances.isEmpty()) {
            mTokensAdapter.balances = balances;
            mTokensAdapter.notifyDataSetChanged();
            mEmptyToken.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mEmptyToken.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    private List<WalletBalance> getBalances() {
        return balancesInteractor.getBalances(mAccount.id).blockingGet();
    }

    private class TokensAdapter extends RecyclerView.Adapter<TokensAdapter.AssetHolder> {

        public List<WalletBalance> balances = new ArrayList<>();

        @NonNull
        @Override
        public AssetHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View rowView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_token, viewGroup, false);
            AssetHolder viewHolder = new AssetHolder(rowView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull AssetHolder viewHolder, int position) {
            final BaseChain chain = getMainActivity().getBaseChain();
            if (chain.equals(OSMOSIS_MAIN.INSTANCE)) {
                if (getItemViewType(position) == SECTION_NATIVE_GRPC) {
                    onNativeGrpcItem(viewHolder, position);
                } else if (getItemViewType(position) == SECTION_IBC_AUTHED_GRPC) {
                    onBindIbcAuthToken(viewHolder, position - mNative.size());
                } else if (getItemViewType(position) == SECTION_OSMOSIS_POOL_GRPC) {
                    onBindOsmoPoolToken(viewHolder, position - mNative.size() - mIbcAuthedGrpc.size());
                } else if (getItemViewType(position) == SECTION_IBC_UNKNOWN_GRPC) {
                    onBindIbcUnknownToken(viewHolder, position - mNative.size() - mIbcAuthedGrpc.size() - mOsmosisPoolGrpc.size());
                } else if (getItemViewType(position) == SECTION_UNKNOWN_GRPC) {
                    onBindUnKnownToken(viewHolder, position - mNative.size() - mIbcAuthedGrpc.size() - mOsmosisPoolGrpc.size() - mIbcUnknownGrpc.size());
                }

            } else if (chain.equals(SIF_MAIN.INSTANCE) || chain.equals(GRABRIDGE_MAIN.INSTANCE)) {
                if (getItemViewType(position) == SECTION_NATIVE_GRPC) {
                    onNativeGrpcItem(viewHolder, position);
                } else if (getItemViewType(position) == SECTION_IBC_AUTHED_GRPC) {
                    onBindIbcAuthToken(viewHolder, position - mNative.size());
                } else if (getItemViewType(position) == SECTION_ETHER_GRPC) {
                    onBindErcToken(viewHolder, position - mNative.size() - mIbcAuthedGrpc.size());
                } else if (getItemViewType(position) == SECTION_IBC_UNKNOWN_GRPC) {
                    onBindIbcUnknownToken(viewHolder, position - mNative.size() - mIbcAuthedGrpc.size() - mEtherGrpc.size());
                } else if (getItemViewType(position) == SECTION_UNKNOWN_GRPC) {
                    onBindUnKnownToken(viewHolder, position - mNative.size() - mIbcAuthedGrpc.size() - mEtherGrpc.size() - mIbcUnknownGrpc.size());
                }

            } else if (chain.equals(COSMOS_MAIN.INSTANCE)) {
                if (getItemViewType(position) == SECTION_NATIVE_GRPC) {
                    onNativeGrpcItem(viewHolder, position);
                } else if (getItemViewType(position) == SECTION_IBC_AUTHED_GRPC) {
                    onBindIbcAuthToken(viewHolder, position - mNative.size());
                } else if (getItemViewType(position) == SECTION_GRAVICTY_DEX_GRPC) {
                    onBindGravityDexToken(viewHolder, position - mNative.size() - mIbcAuthedGrpc.size());
                } else if (getItemViewType(position) == SECTION_IBC_UNKNOWN_GRPC) {
                    onBindIbcUnknownToken(viewHolder, position - mNative.size() - mIbcAuthedGrpc.size() - mGravityDexGrpc.size());
                } else if (getItemViewType(position) == SECTION_UNKNOWN_GRPC) {
                    onBindUnKnownToken(viewHolder, position - mNative.size() - mIbcAuthedGrpc.size() - mGravityDexGrpc.size() - mIbcUnknownGrpc.size());
                }

            } else if (chain.equals(INJ_MAIN.INSTANCE)) {
                if (getItemViewType(position) == SECTION_NATIVE_GRPC) {
                    onNativeGrpcItem(viewHolder, position);
                } else if (getItemViewType(position) == SECTION_IBC_AUTHED_GRPC) {
                    onBindIbcAuthToken(viewHolder, position - mNative.size());
                } else if (getItemViewType(position) == SECTION_ETHER_GRPC) {
                    onBindErcToken(viewHolder, position - mNative.size() - mIbcAuthedGrpc.size());
                } else if (getItemViewType(position) == SECTION_INJECTIVE_POOL_GRPC) {
                    onBindInjectivePoolToken(viewHolder, position - mNative.size() - mIbcAuthedGrpc.size() - mEtherGrpc.size());
                } else if (getItemViewType(position) == SECTION_IBC_UNKNOWN_GRPC) {
                    onBindIbcUnknownToken(viewHolder, position - mNative.size() - mIbcAuthedGrpc.size() - mEtherGrpc.size() - mInjectivePoolGrpc.size());
                } else if (getItemViewType(position) == SECTION_UNKNOWN_GRPC) {
                    onBindUnKnownToken(viewHolder, position - mNative.size() - mIbcAuthedGrpc.size() - mEtherGrpc.size() - mInjectivePoolGrpc.size() - mIbcUnknownGrpc.size());
                }

            } else if (chain.equals(KAVA_MAIN.INSTANCE)) {
                if (getItemViewType(position) == SECTION_NATIVE_GRPC) {
                    onNativeGrpcItem(viewHolder, position);
                } else if (getItemViewType(position) == SECTION_IBC_AUTHED_GRPC) {
                    onBindIbcAuthToken(viewHolder, position - mNative.size());
                } else if (getItemViewType(position) == SECTION_KAVA_BEP2_GRPC) {
                    onBindKavaBep2Token(viewHolder, position - mNative.size() - mIbcAuthedGrpc.size());
                } else if (getItemViewType(position) == SECTION_ETC_GRPC) {
                    onBindEtcGrpcToken(viewHolder, position - mNative.size() - mIbcAuthedGrpc.size() - mKavaBep2Grpc.size());
                } else if (getItemViewType(position) == SECTION_IBC_UNKNOWN_GRPC) {
                    onBindIbcUnknownToken(viewHolder, position - mNative.size() - mIbcAuthedGrpc.size() - mKavaBep2Grpc.size() - mEtc.size());
                } else if (getItemViewType(position) == SECTION_UNKNOWN_GRPC) {
                    onBindUnKnownToken(viewHolder, position - mNative.size() - mIbcAuthedGrpc.size() - mKavaBep2Grpc.size() - mEtc.size() - mIbcUnknownGrpc.size());
                }

            } else if (chain.equals(JUNO_MAIN.INSTANCE)) {
                if (getItemViewType(position) == SECTION_NATIVE_GRPC) {
                    onNativeGrpcItem(viewHolder, position);
                } else if (getItemViewType(position) == SECTION_IBC_AUTHED_GRPC) {
                    onBindIbcAuthToken(viewHolder, position - mNative.size());
                } else if (getItemViewType(position) == SECTION_CW20_GRPC) {
                    onBindCw20GrpcToken(viewHolder, position - mNative.size() - mIbcAuthedGrpc.size());
                } else if (getItemViewType(position) == SECTION_IBC_UNKNOWN_GRPC) {
                    onBindIbcUnknownToken(viewHolder, position - mNative.size() - mIbcAuthedGrpc.size() - mCW20Grpc.size());
                } else if (getItemViewType(position) == SECTION_UNKNOWN_GRPC) {
                    onBindUnKnownToken(viewHolder, position - mNative.size() - mIbcAuthedGrpc.size() - mCW20Grpc.size() - mIbcUnknownGrpc.size());
                }

            } else if (chain.isGRPC()) {
                if (getItemViewType(position) == SECTION_NATIVE_GRPC) {
                    onNativeGrpcItem(viewHolder, position);
                } else if (getItemViewType(position) == SECTION_IBC_AUTHED_GRPC) {
                    onBindIbcAuthToken(viewHolder, position - mNative.size());
                } else if (getItemViewType(position) == SECTION_IBC_UNKNOWN_GRPC) {
                    onBindIbcUnknownToken(viewHolder, position - mNative.size() - mIbcAuthedGrpc.size());
                } else if (getItemViewType(position) == SECTION_UNKNOWN_GRPC) {
                    onBindUnKnownToken(viewHolder, position - mNative.size() - mIbcAuthedGrpc.size() - mIbcUnknownGrpc.size());
                }

            } else if (chain.equals(OKEX_MAIN.INSTANCE)) {
                if (getItemViewType(position) == SECTION_NATIVE) {
                    onBindNativeItem(viewHolder, position);
                } else if (getItemViewType(position) == SECTION_ETC) {
                    onBindEtcToken(viewHolder, position - mNative.size());
                } else if (getItemViewType(position) == SECTION_UNKNOWN) {
                    onBindUnKnownCoin(viewHolder, position - mNative.size() - mEtc.size());
                }
            } else if (chain.equals(BNB_MAIN.INSTANCE)) {
                if (getItemViewType(position) == SECTION_NATIVE) {
                    onBindNativeItem(viewHolder, position);
                } else if (getItemViewType(position) == SECTION_ETC) {
                    onBindEtcToken(viewHolder, position - mNative.size());
                } else if (getItemViewType(position) == SECTION_UNKNOWN) {
                    onBindUnKnownCoin(viewHolder, position - mNative.size() - mEtc.size());
                }
            } else {
                if (getItemViewType(position) == SECTION_NATIVE) {
                    onBindNativeItem(viewHolder, position);
                } else if (getItemViewType(position) == SECTION_UNKNOWN) {
                    onBindUnKnownCoin(viewHolder, position - mNative.size());
                }
            }
        }

        @Override
        public int getItemCount() {
            if (getMainActivity().getBaseChain().equals(JUNO_MAIN.INSTANCE)) {
                return balances.size() + mCW20Grpc.size();
            } else {
                return balances.size();
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (getMainActivity().getBaseChain().equals(OSMOSIS_MAIN.INSTANCE)) {
                if (position < mNative.size()) {
                    return SECTION_NATIVE_GRPC;
                } else if (position < mNative.size() + mIbcAuthedGrpc.size()) {
                    return SECTION_IBC_AUTHED_GRPC;
                } else if (position < mNative.size() + mIbcAuthedGrpc.size() + mOsmosisPoolGrpc.size()) {
                    return SECTION_OSMOSIS_POOL_GRPC;
                } else if (position < mNative.size() + mIbcAuthedGrpc.size() + mOsmosisPoolGrpc.size() + mIbcUnknownGrpc.size()) {
                    return SECTION_IBC_UNKNOWN_GRPC;
                } else if (position < mNative.size() + mIbcAuthedGrpc.size() + mOsmosisPoolGrpc.size() + mIbcUnknownGrpc.size() + mUnknown.size()) {
                    return SECTION_UNKNOWN_GRPC;
                }

            } else if (getMainActivity().getBaseChain().equals(SIF_MAIN.INSTANCE) || getMainActivity().getBaseChain().equals(GRABRIDGE_MAIN.INSTANCE)) {
                if (position < mNative.size()) {
                    return SECTION_NATIVE_GRPC;
                } else if (position < mNative.size() + mIbcAuthedGrpc.size()) {
                    return SECTION_IBC_AUTHED_GRPC;
                } else if (position < mNative.size() + mIbcAuthedGrpc.size() + mEtherGrpc.size()) {
                    return SECTION_ETHER_GRPC;
                } else if (position < mNative.size() + mIbcAuthedGrpc.size() + mEtherGrpc.size() + mIbcUnknownGrpc.size()) {
                    return SECTION_IBC_UNKNOWN_GRPC;
                } else if (position < mNative.size() + mIbcAuthedGrpc.size() + mEtherGrpc.size() + mIbcUnknownGrpc.size() + mUnknown.size()) {
                    return SECTION_UNKNOWN_GRPC;
                }

            } else if (getMainActivity().getBaseChain().equals(COSMOS_MAIN.INSTANCE)) {
                if (position < mNative.size()) {
                    return SECTION_NATIVE_GRPC;
                } else if (position < mNative.size() + mIbcAuthedGrpc.size()) {
                    return SECTION_IBC_AUTHED_GRPC;
                } else if (position < mNative.size() + mIbcAuthedGrpc.size() + mGravityDexGrpc.size()) {
                    return SECTION_GRAVICTY_DEX_GRPC;
                } else if (position < mNative.size() + mIbcAuthedGrpc.size() + mGravityDexGrpc.size() + mIbcUnknownGrpc.size()) {
                    return SECTION_IBC_UNKNOWN_GRPC;
                } else if (position < mNative.size() + mIbcAuthedGrpc.size() + mGravityDexGrpc.size() + mIbcUnknownGrpc.size() + mUnknown.size()) {
                    return SECTION_UNKNOWN_GRPC;
                }

            } else if (getMainActivity().getBaseChain().equals(INJ_MAIN.INSTANCE)) {
                if (position < mNative.size()) {
                    return SECTION_NATIVE_GRPC;
                } else if (position < mNative.size() + mIbcAuthedGrpc.size()) {
                    return SECTION_IBC_AUTHED_GRPC;
                } else if (position < mNative.size() + mIbcAuthedGrpc.size() + mEtherGrpc.size()) {
                    return SECTION_ETHER_GRPC;
                } else if (position < mNative.size() + mIbcAuthedGrpc.size() + mEtherGrpc.size() + mInjectivePoolGrpc.size()) {
                    return SECTION_INJECTIVE_POOL_GRPC;
                } else if (position < mNative.size() + mIbcAuthedGrpc.size() + mEtherGrpc.size() + mInjectivePoolGrpc.size() + mIbcUnknownGrpc.size()) {
                    return SECTION_IBC_UNKNOWN_GRPC;
                } else if (position < mNative.size() + mIbcAuthedGrpc.size() + mEtherGrpc.size() + mInjectivePoolGrpc.size() + mIbcUnknownGrpc.size() + mUnknown.size()) {
                    return SECTION_UNKNOWN_GRPC;
                }

            } else if (getMainActivity().getBaseChain().equals(KAVA_MAIN.INSTANCE)) {
                if (position < mNative.size()) {
                    return SECTION_NATIVE_GRPC;
                } else if (position < mNative.size() + mIbcAuthedGrpc.size()) {
                    return SECTION_IBC_AUTHED_GRPC;
                } else if (position < mNative.size() + mIbcAuthedGrpc.size() + mKavaBep2Grpc.size()) {
                    return SECTION_KAVA_BEP2_GRPC;
                } else if (position < mNative.size() + mIbcAuthedGrpc.size() + mKavaBep2Grpc.size() + mEtc.size()) {
                    return SECTION_ETC_GRPC;
                } else if (position < mNative.size() + mIbcAuthedGrpc.size() + mKavaBep2Grpc.size() + mEtc.size() + mIbcUnknownGrpc.size()) {
                    return SECTION_IBC_UNKNOWN_GRPC;
                } else if (position < mNative.size() + mIbcAuthedGrpc.size() + mKavaBep2Grpc.size() + mEtc.size() + mIbcUnknownGrpc.size() + mUnknown.size()) {
                    return SECTION_UNKNOWN_GRPC;
                }

            } else if (getMainActivity().getBaseChain().equals(JUNO_MAIN.INSTANCE)) {
                if (position < mNative.size()) {
                    return SECTION_NATIVE_GRPC;
                } else if (position < mNative.size() + mIbcAuthedGrpc.size()) {
                    return SECTION_IBC_AUTHED_GRPC;
                } else if (position < mNative.size() + mIbcAuthedGrpc.size() + mCW20Grpc.size()) {
                    return SECTION_CW20_GRPC;
                } else if (position < mNative.size() + mIbcAuthedGrpc.size() + mCW20Grpc.size() + mIbcUnknownGrpc.size()) {
                    return SECTION_IBC_UNKNOWN_GRPC;
                } else if (position < mNative.size() + mIbcAuthedGrpc.size() + mCW20Grpc.size() + mIbcUnknownGrpc.size() + mUnknown.size()) {
                    return SECTION_UNKNOWN_GRPC;
                }

            } else if (getMainActivity().getBaseChain().isGRPC()) {
                if (position < mNative.size()) {
                    return SECTION_NATIVE_GRPC;
                } else if (position < mNative.size() + mIbcAuthedGrpc.size()) {
                    return SECTION_IBC_AUTHED_GRPC;
                } else if (position < mNative.size() + mIbcAuthedGrpc.size() + mIbcUnknownGrpc.size()) {
                    return SECTION_IBC_UNKNOWN_GRPC;
                } else if (position < mNative.size() + mIbcAuthedGrpc.size() + mIbcUnknownGrpc.size() + mUnknown.size()) {
                    return SECTION_UNKNOWN_GRPC;
                }
            } else if (getMainActivity().getBaseChain().equals(OKEX_MAIN.INSTANCE)) {
                if (mNative != null) {
                    if (position < mNative.size()) {
                        return SECTION_NATIVE;
                    } else if (position < mNative.size() + mEtc.size()) {
                        return SECTION_ETC;
                    } else if (position < mNative.size() + mEtc.size() + mUnknown.size()) {
                        return SECTION_UNKNOWN;
                    }
                } else {
                    if (position < mEtc.size()) {
                        return SECTION_ETC;
                    } else if (position < mEtc.size() + mUnknown.size()) {
                        return SECTION_UNKNOWN;
                    }
                }

            } else if (getMainActivity().getBaseChain().equals(BNB_MAIN.INSTANCE)) {
                if (position < mNative.size()) {
                    return SECTION_NATIVE;
                } else if (position < mNative.size() + mEtc.size()) {
                    return SECTION_ETC;
                } else if (position < mNative.size() + mEtc.size() + mUnknown.size()) {
                    return SECTION_UNKNOWN;
                }
            } else {
                return SECTION_NATIVE;
            }
            return 0;
        }

        public class AssetHolder extends RecyclerView.ViewHolder {
            private final CardView itemRoot;
            private final ImageView itemImg;
            private final TextView itemSymbol;
            private final TextView itemInnerSymbol;
            private final TextView itemFullName;
            private final TextView itemBalance;
            private final TextView itemValue;

            public AssetHolder(View v) {
                super(v);
                itemRoot = itemView.findViewById(R.id.token_card);
                itemImg = itemView.findViewById(R.id.token_img);
                itemSymbol = itemView.findViewById(R.id.token_symbol);
                itemInnerSymbol = itemView.findViewById(R.id.token_inner_symbol);
                itemFullName = itemView.findViewById(R.id.token_fullname);
                itemBalance = itemView.findViewById(R.id.token_balance);
                itemValue = itemView.findViewById(R.id.token_value);
            }
        }
    }

    //with Native gRPC
    private void onNativeGrpcItem(TokensAdapter.AssetHolder holder, final int position) {
        final WalletBalance balance = mNative.get(position);
        final BaseChain chain = BaseChain.Companion.getChainByDenom(balance.getDenom());

        Picasso.get().cancelRequest(holder.itemImg);

        if (chain != null) {
            holder.itemSymbol.setText(getString(chain.getSymbolTitle()));
            holder.itemSymbol.setTextColor(ContextCompat.getColor(requireContext(), chain.getChainColor()));
            holder.itemFullName.setText(chain.getFullNameCoin());
            if (balance.getDenom().equals(SECRET_MAIN.INSTANCE.getMainDenom())) {
                holder.itemInnerSymbol.setText("(" + balance.getDenom() + ")");
            } else {
                holder.itemInnerSymbol.setText("");
            }
            holder.itemImg.setImageDrawable(ContextCompat.getDrawable(requireContext(), chain.getCoinIcon()));
        } else if (balance.getDenom().equals(TOKEN_ION)) {
            holder.itemSymbol.setText(getString(R.string.str_uion_c));
            holder.itemSymbol.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorIon));
            holder.itemInnerSymbol.setText("");
            holder.itemFullName.setText("Ion Coin");
            holder.itemImg.setImageDrawable(getResources().getDrawable(R.drawable.token_ion));
        } else if (balance.getDenom().equals(TOKEN_HARD)) {
            Picasso.get().load(KAVA_COIN_IMG_URL + balance.getDenom() + ".png").fit().placeholder(R.drawable.token_ic).error(R.drawable.token_ic).into(holder.itemImg);
            holder.itemSymbol.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorHard));
            holder.itemSymbol.setText(balance.getDenom().toUpperCase());
            holder.itemInnerSymbol.setText("");
            holder.itemFullName.setText("HardPool Gov. Coin");
        } else if (balance.getDenom().equals(TOKEN_USDX)) {
            Picasso.get().load(KAVA_COIN_IMG_URL + balance.getDenom() + ".png").fit().placeholder(R.drawable.token_ic).error(R.drawable.token_ic).into(holder.itemImg);
            holder.itemSymbol.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorUsdx));
            holder.itemSymbol.setText(balance.getDenom().toUpperCase());
            holder.itemInnerSymbol.setText("");
            holder.itemFullName.setText("USD Stable Asset");
        } else if (balance.getDenom().equals(TOKEN_SWP)) {
            Picasso.get().load(KAVA_COIN_IMG_URL + balance.getDenom() + ".png").fit().placeholder(R.drawable.token_ic).error(R.drawable.token_ic).into(holder.itemImg);
            holder.itemSymbol.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorSwp));
            holder.itemSymbol.setText(balance.getDenom().toUpperCase());
            holder.itemInnerSymbol.setText("");
            holder.itemFullName.setText("Kava Swap Coin");
        } else if (balance.getDenom().startsWith("e")) {
            holder.itemSymbol.setText(balance.getDenom().toUpperCase());
            holder.itemSymbol.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite));
            holder.itemInnerSymbol.setText("");
            holder.itemFullName.setText(balance.getDenom().substring(1).toUpperCase() + " on E-Money Network");
            Picasso.get().load(EMONEY_COIN_IMG_URL + balance.getDenom() + ".png").fit().placeholder(R.drawable.token_ic).error(R.drawable.token_ic).into(holder.itemImg);
        }

        BigDecimal amount = balance.getBalanceAmount().add(getBaseDao().getAllMainAsset(balance.getDenom()));  //TODO: add vesting
        int divideDecimal = 6;
        int displayDecimal = 6;
        int divider = 6;

        if (balance.getDenom().equals(TOKEN_ION) || balance.getDenom().startsWith("e")) {
            amount = getMainActivity().getBalance(balance.getDenom());
        } else if (balance.getDenom().equals(FETCHAI_MAIN.INSTANCE.getMainDenom()) || balance.getDenom().equals(INJ_MAIN.INSTANCE.getMainDenom()) || balance.getDenom().equals(SIF_MAIN.INSTANCE.getMainDenom())) {
            divideDecimal = 18;
            divider = 18;
        } else if (balance.getDenom().equals(EVMOS_MAIN.INSTANCE.getMainDenom()) || balance.getDenom().equals(CUDOS_MAIN.INSTANCE.getMainDenom())) {
            divideDecimal = 18;
        } else if (balance.getDenom().equals(PROVENANCE_MAIN.INSTANCE.getMainDenom())) {
            divideDecimal = 9;
        } else if (balance.getDenom().equals(CRYPTO_MAIN.INSTANCE.getMainDenom())) {
            divideDecimal = 8;
            divider = 8;
        } else if (balance.getDenom().equals(TOKEN_HARD) || balance.getDenom().equals(TOKEN_USDX) || balance.getDenom().equals(TOKEN_SWP)) {
            amount = balance.getBalanceAmount(); // .add(getBaseDao().getVesting(balance.getSymbol()));
            divideDecimal = WUtil.getKavaCoinDecimal(getBaseDao(), balance.getDenom());
        }

        holder.itemBalance.setText(WDp.getDpAmount2(amount, divideDecimal, displayDecimal));
        holder.itemValue.setText(WDp.dpUserCurrencyValue(getBaseDao(), getCurrency(), balance.getDenom(), amount, divider, priceProvider));

        holder.itemRoot.setOnClickListener(v -> {
            if (mNative.get(position).getDenom().equalsIgnoreCase(getMainActivity().getBaseChain().getMainDenom())) {
                Intent intent = new Intent(getMainActivity(), StakingTokenGrpcActivity.class);
                intent.putExtra("denom", balance.getDenom());
                startActivity(intent);
            } else {
                Intent intent = new Intent(getMainActivity(), NativeTokenGrpcActivity.class);
                intent.putExtra("denom", balance.getDenom());
                startActivity(intent);
            }
        });
    }

    //with Authed IBC gRPC
    private void onBindIbcAuthToken(TokensAdapter.AssetHolder holder, int position) {
        final WalletBalance balance = mIbcAuthedGrpc.get(position);
        final IbcToken ibcToken = getBaseDao().getIbcToken(balance.getDenom());
        holder.itemSymbol.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite));
        holder.itemFullName.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        holder.itemInnerSymbol.setText("");
        if (ibcToken == null) {
            holder.itemSymbol.setText(R.string.str_unknown);
            holder.itemFullName.setText("");
            holder.itemImg.setImageDrawable(getResources().getDrawable(R.drawable.token_default_ibc));
            holder.itemBalance.setText(WDp.getDpAmount2(balance.getBalanceAmount(), 6, 6));
            holder.itemValue.setText(WDp.dpUserCurrencyValue(getBaseDao(), getCurrency(), balance.getDenom(), BigDecimal.ZERO, 6, priceProvider));
        } else {
            holder.itemSymbol.setText(ibcToken.display_denom.toUpperCase());
            holder.itemFullName.setText(ibcToken.channel_id);
            holder.itemBalance.setText(WDp.getDpAmount2(balance.getBalanceAmount(), ibcToken.decimal, 6));
            holder.itemValue.setText(WDp.dpUserCurrencyValue(getBaseDao(), getCurrency(), getBaseDao().getBaseDenom(balance.getDenom()), balance.getBalanceAmount(), ibcToken.decimal, priceProvider));
            try {
                Picasso.get().load(ibcToken.moniker).fit().placeholder(R.drawable.token_default_ibc).error(R.drawable.token_default_ibc).into(holder.itemImg);
            } catch (Exception e) {
            }
        }

        holder.itemRoot.setOnClickListener(v -> {
            Intent intent = new Intent(getMainActivity(), IBCTokenDetailActivity.class);
            intent.putExtra("denom", balance.getDenom());
            startActivity(intent);
        });
    }

    //with Unknown IBC gRPC
    private void onBindIbcUnknownToken(TokensAdapter.AssetHolder holder, int position) {
        final WalletBalance balance = mIbcUnknownGrpc.get(position);
        final IbcToken ibcToken = getBaseDao().getIbcToken(balance.getDenom());
        holder.itemInnerSymbol.setText("");
        holder.itemSymbol.setText(R.string.str_unknown);
        if (ibcToken == null) {
            holder.itemFullName.setText("");
            holder.itemImg.setImageResource(R.drawable.token_default_ibc);
            holder.itemBalance.setText(WDp.getDpAmount2(balance.getBalanceAmount(), 6, 6));
            holder.itemValue.setText(WDp.dpUserCurrencyValue(getBaseDao(), getCurrency(), balance.getDenom(), BigDecimal.ZERO, 6, priceProvider));
        } else {
            holder.itemFullName.setText(ibcToken.channel_id);
            holder.itemImg.setImageResource(R.drawable.token_default_ibc);
            holder.itemBalance.setText(WDp.getDpAmount2(balance.getBalanceAmount(), 6, 6));
            holder.itemValue.setText(WDp.dpUserCurrencyValue(getBaseDao(), getCurrency(), balance.getDenom(), balance.getBalanceAmount(), 6, priceProvider));
        }

        holder.itemRoot.setOnClickListener(v -> {
            Intent intent = new Intent(getMainActivity(), IBCTokenDetailActivity.class);
            intent.putExtra("denom", balance.getDenom());
            startActivity(intent);
        });
    }

    //with Osmosis Pool gRPC
    private void onBindOsmoPoolToken(TokensAdapter.AssetHolder holder, int position) {
        final WalletBalance balance = mOsmosisPoolGrpc.get(position);
        holder.itemSymbol.setText(balance.osmosisAmmDpDenom());
        holder.itemSymbol.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite));
        holder.itemInnerSymbol.setText("");
        holder.itemFullName.setText(balance.getDenom());
        holder.itemImg.setImageDrawable(getResources().getDrawable(R.drawable.token_pool));
        holder.itemBalance.setText(WDp.getDpAmount2(balance.getBalanceAmount(), 18, 6));
        holder.itemValue.setText(WDp.dpUserCurrencyValue(getBaseDao(), getCurrency(), balance.getDenom(), balance.getBalanceAmount(), 18, priceProvider));

        holder.itemRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getMainActivity(), POOLTokenDetailActivity.class);
                intent.putExtra("denom", balance.getDenom());
                startActivity(intent);
            }
        });
    }

    //with Cosmos Gravity Dex gRPC
    private void onBindGravityDexToken(TokensAdapter.AssetHolder holder, int position) {
        final WalletBalance balance = mGravityDexGrpc.get(position);
        Picasso.get().load(COSMOS_COIN_IMG_URL + "gravitydex.png").fit().placeholder(R.drawable.token_ic).error(R.drawable.token_ic).into(holder.itemImg);
        holder.itemBalance.setText(WDp.getDpAmount2(balance.getBalanceAmount(), 6, 6));
        holder.itemValue.setText(WDp.dpUserCurrencyValue(getBaseDao(), getCurrency(), balance.getDenom(), balance.getBalanceAmount(), 6, priceProvider));
        holder.itemInnerSymbol.setText("");
        Liquidity.Pool poolInfo = getBaseDao().getGravityPoolByDenom(balance.getDenom());
        if (poolInfo != null) {
            holder.itemSymbol.setText("GDEX-" + poolInfo.getId());
            holder.itemSymbol.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite));
            holder.itemFullName.setText("pool/" + poolInfo.getId());
        }

        holder.itemRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getMainActivity(), POOLTokenDetailActivity.class);
                intent.putExtra("denom", balance.getDenom());
                startActivity(intent);
            }
        });
    }

    //with Injective Pool gRPC
    private void onBindInjectivePoolToken(TokensAdapter.AssetHolder holder, int position) {
        final WalletBalance balance = mInjectivePoolGrpc.get(position);
        holder.itemSymbol.setText("SHARE" + balance.getDenom().substring(5));
        holder.itemSymbol.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite));
        holder.itemInnerSymbol.setText("");
        holder.itemFullName.setText("Pool Asset");
        holder.itemImg.setImageDrawable(getResources().getDrawable(R.drawable.token_ic));
        holder.itemBalance.setText(WDp.getDpAmount2(balance.getBalanceAmount(), 18, 6));
        holder.itemValue.setText(WDp.dpUserCurrencyValue(getBaseDao(), getCurrency(), balance.getDenom(), BigDecimal.ZERO, 6, priceProvider));

        holder.itemRoot.setOnClickListener(v -> {
            Intent intent = new Intent(getMainActivity(), POOLTokenDetailActivity.class);
            intent.putExtra("denom", balance.getDenom());
            startActivity(intent);
        });
    }

    //with Erc gRPC
    private void onBindErcToken(TokensAdapter.AssetHolder holder, int position) {
        final WalletBalance balance = mEtherGrpc.get(position);
        final Assets assets = getBaseDao().getAsset(balance.getDenom());
        if (assets != null) {
            holder.itemSymbol.setText(assets.origin_symbol);
            holder.itemSymbol.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite));
            holder.itemInnerSymbol.setText("");
            holder.itemFullName.setText(assets.display_symbol);
            Picasso.get().load(ASSET_IMG_URL + assets.logo).fit().placeholder(R.drawable.token_ic).error(R.drawable.token_ic).into(holder.itemImg);

            BigDecimal totalAmount = getMainActivity().getBalance(assets.denom);
            holder.itemBalance.setText(WDp.getDpAmount2(totalAmount, assets.decimal, 6));
            holder.itemValue.setText(WDp.dpUserCurrencyValue(getBaseDao(), getCurrency(), assets.origin_symbol, totalAmount, assets.decimal, priceProvider));

            holder.itemRoot.setOnClickListener(v -> {
                Intent intent = new Intent(getMainActivity(), BridgeTokenGrpcActivity.class);
                intent.putExtra("denom", assets.denom);
                startActivity(intent);
            });
        }
    }

    //bind kava bep2 tokens with gRPC
    private void onBindKavaBep2Token(TokensAdapter.AssetHolder holder, int position) {
        final WalletBalance balance = mKavaBep2Grpc.get(position);
        Picasso.get().load(KAVA_COIN_IMG_URL + balance.getDenom() + ".png").fit().placeholder(R.drawable.token_ic).error(R.drawable.token_ic).into(holder.itemImg);
        holder.itemSymbol.setText(balance.getDenom().toUpperCase());
        holder.itemSymbol.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite));
        holder.itemInnerSymbol.setText("");
        holder.itemFullName.setText(balance.getDenom().toUpperCase() + " on Kava Chain");

        BigDecimal totalAmount = getMainActivity().getBalance(balance.getDenom());
        String baseDenom = WDp.getKavaBaseDenom(balance.getDenom());
        int bep2decimal = WUtil.getKavaCoinDecimal(getBaseDao(), balance.getDenom());
        holder.itemBalance.setText(WDp.getDpAmount2(totalAmount, bep2decimal, 6));
        holder.itemValue.setText(WDp.dpUserCurrencyValue(getBaseDao(), getCurrency(), baseDenom, totalAmount, bep2decimal, priceProvider));

        holder.itemRoot.setOnClickListener(v -> {
            Intent intent = new Intent(getMainActivity(), NativeTokenGrpcActivity.class);
            intent.putExtra("denom", balance.getDenom());
            startActivity(intent);
        });
    }

    //bind kava etc tokens with gRPC
    private void onBindEtcGrpcToken(TokensAdapter.AssetHolder holder, int position) {
        final WalletBalance balance = mEtc.get(position);
        Picasso.get().load(KAVA_COIN_IMG_URL + "hbtc.png").fit().placeholder(R.drawable.token_ic).error(R.drawable.token_ic).into(holder.itemImg);
        holder.itemSymbol.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite));
        holder.itemSymbol.setText(balance.getDenom().toUpperCase());
        holder.itemInnerSymbol.setText("(" + balance.getDenom() + ")");
        holder.itemFullName.setText(balance.getDenom().toUpperCase() + " on Kava Chain");

        BigDecimal tokenTotalAmount = balance.getBalanceAmount(); //getBalance(balance.getSymbol()).add(getBaseDao().getVesting(balance.getSymbol()));
        BigDecimal convertedKavaAmount = WDp.convertTokenToKava(getBaseDao(), balance, priceProvider);
        holder.itemBalance.setText(WDp.getDpAmount2(tokenTotalAmount, WUtil.getKavaCoinDecimal(getBaseDao(), balance.getDenom()), 6));
        holder.itemValue.setText(WDp.dpUserCurrencyValue(getBaseDao(), getCurrency(), KAVA_MAIN.INSTANCE.getMainDenom(), convertedKavaAmount, 6, priceProvider));
    }

    //bind cw20 tokens with gRPC
    private void onBindCw20GrpcToken(TokensAdapter.AssetHolder holder, int position) {
        final Cw20Assets cw20Asset = mCW20Grpc.get(position);
        Picasso.get().load(cw20Asset.logo).fit().placeholder(R.drawable.token_ic).error(R.drawable.token_ic).into(holder.itemImg);
        holder.itemSymbol.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite));
        holder.itemSymbol.setText(cw20Asset.denom.toUpperCase());
        holder.itemInnerSymbol.setText("");
        holder.itemFullName.setText(cw20Asset.contract_address);

        int decimal = cw20Asset.decimal;
        holder.itemBalance.setText(WDp.getDpAmount2(cw20Asset.getAmount(), decimal, 6));
        holder.itemValue.setText(WDp.dpUserCurrencyValue(getBaseDao(), getCurrency(), cw20Asset.denom, cw20Asset.getAmount(), decimal, priceProvider));

        holder.itemRoot.setOnClickListener(v -> {
            Intent intent = new Intent(getMainActivity(), ContractTokenGrpcActivity.class);
            intent.putExtra("cw20Asset", cw20Asset);
            startActivity(intent);
        });
    }

    //with Unknown Token gRPC
    private void onBindUnKnownToken(TokensAdapter.AssetHolder holder, int position) {
        final WalletBalance balance = mUnknown.get(position);
        holder.itemSymbol.setText(R.string.str_unknown);
        holder.itemSymbol.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite));
        holder.itemInnerSymbol.setText("");
        holder.itemFullName.setText("");
        holder.itemImg.setImageDrawable(getResources().getDrawable(R.drawable.token_ic));
        holder.itemBalance.setText(WDp.getDpAmount2(balance.getBalanceAmount(), 6, 6));
        holder.itemValue.setText(WDp.dpUserCurrencyValue(getBaseDao(), getCurrency(), balance.getDenom(), BigDecimal.ZERO, 6, priceProvider));
    }


    //with native tokens
    private void onBindNativeItem(TokensAdapter.AssetHolder holder, int position) {
        final WalletBalance balance = mNative.get(position);
        if (getMainActivity().getBaseChain().equals(BNB_MAIN.INSTANCE)) {
            final String denom = balance.getDenom();

            final BigDecimal amount = balance.getTotalAmount();
            final BnbToken bnbToken = getBaseDao().getBnbToken(denom);
            if (bnbToken != null) {
                holder.itemSymbol.setText(bnbToken.original_symbol.toUpperCase());
                holder.itemInnerSymbol.setText("(" + bnbToken.symbol + ")");
                holder.itemFullName.setText(BNB_MAIN.INSTANCE.getFullNameCoin());
                holder.itemImg.setImageDrawable(ContextCompat.getDrawable(requireContext(), BNB_MAIN.INSTANCE.getCoinIcon()));
                holder.itemSymbol.setTextColor(WDp.getChainColor(getContext(), BNB_MAIN.INSTANCE));
                holder.itemBalance.setText(WDp.getDpAmount2(amount, 0, 6));
                holder.itemValue.setText(WDp.dpUserCurrencyValue(getBaseDao(), getCurrency(), BNB_MAIN.INSTANCE.getMainDenom(), amount, 0, priceProvider));
            }
            holder.itemRoot.setOnClickListener(v -> {
                Intent intent = new Intent(getMainActivity(), StakingTokenDetailActivity.class);
                startActivity(intent);
            });

        } else if (getMainActivity().getBaseChain().equals(OKEX_MAIN.INSTANCE)) {
            final OkToken okToken = getBaseDao().okToken(balance.getDenom());
            holder.itemSymbol.setText(okToken.original_symbol.toUpperCase());
            holder.itemInnerSymbol.setText("(" + okToken.symbol + ")");
            holder.itemFullName.setText(OKEX_MAIN.INSTANCE.getFullNameCoin());
            if (balance.getDenom().equals(OKEX_MAIN.INSTANCE.getMainDenom())) {
                holder.itemSymbol.setTextColor(WDp.getChainColor(getContext(), getMainActivity().getBaseChain()));
                holder.itemImg.setImageDrawable(ContextCompat.getDrawable(requireContext(), OKEX_MAIN.INSTANCE.getCoinIcon()));

                BigDecimal totalAmount = balance.getDelegatableAmount().add(getBaseDao().getAllExToken(balance.getDenom()));
                holder.itemBalance.setText(WDp.getDpAmount2(totalAmount, 0, 6));
                holder.itemValue.setText(WDp.dpUserCurrencyValue(getBaseDao(), getCurrency(), balance.getDenom(), totalAmount, 0, priceProvider));
            }
            holder.itemRoot.setOnClickListener(v -> {
                Intent intent = new Intent(getMainActivity(), StakingTokenDetailActivity.class);
                startActivity(intent);
            });
        }
    }

    //with Etc tokens (binance, okex)
    private void onBindEtcToken(TokensAdapter.AssetHolder holder, int position) {
        final WalletBalance balance = mEtc.get(position);
        final String denom = balance.getDenom();
        BaseChain baseChain = getMainActivity().getBaseChain();
        if (OKEX_MAIN.INSTANCE.equals(baseChain)) {
            final OkToken okToken = getBaseDao().okToken(denom);
            if (okToken != null) {
                holder.itemSymbol.setText(okToken.original_symbol.toUpperCase());
                holder.itemInnerSymbol.setText("(" + okToken.symbol + ")");
                holder.itemFullName.setText(okToken.description);
                holder.itemSymbol.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite));
                Picasso.get().load(OKEX_COIN_IMG_URL + okToken.original_symbol + ".png").placeholder(R.drawable.token_ic).error(R.drawable.token_ic).fit().into(holder.itemImg);
            }

            BigDecimal totalAmount = balance.getDelegatableAmount().add(getBaseDao().getAllExToken(denom));
            BigDecimal convertAmount = WDp.convertTokenToOkt(getBaseActivity().getFullBalance(denom), getBaseDao(), denom, priceProvider);
            holder.itemBalance.setText(WDp.getDpAmount2(totalAmount, 0, 6));
            holder.itemValue.setText(WDp.dpUserCurrencyValue(getBaseDao(), getCurrency(), OKEX_MAIN.INSTANCE.getMainDenom(), convertAmount, 0, priceProvider));
            holder.itemRoot.setOnClickListener(v -> {
                Intent intent = new Intent(getMainActivity(), NativeTokenDetailActivity.class);
                intent.putExtra("denom", denom);
                startActivity(intent);
            });
        } else if (BNB_MAIN.INSTANCE.equals(baseChain)) {
            final BigDecimal amount = balance.getTotalAmount();
            final BnbToken bnbToken = getBaseDao().getBnbToken(denom);

            holder.itemSymbol.setText(bnbToken.original_symbol.toUpperCase());
            holder.itemInnerSymbol.setText("(" + bnbToken.symbol + ")");
            holder.itemFullName.setText(bnbToken.name);
            Picasso.get().load(BINANCE_TOKEN_IMG_URL + bnbToken.original_symbol + ".png").fit().placeholder(R.drawable.token_ic).error(R.drawable.token_ic).into(holder.itemImg);
            holder.itemSymbol.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite));
            holder.itemBalance.setText(WDp.getDpAmount2(amount, 0, 6));

            final BigDecimal convertAmount = WUtil.getBnbConvertAmount(getBaseDao(), denom, amount);
            holder.itemValue.setText(WDp.dpUserCurrencyValue(getBaseDao(), getCurrency(), BNB_MAIN.INSTANCE.getMainDenom(), convertAmount, 0, priceProvider));
            holder.itemRoot.setOnClickListener(v -> {
                Intent intent = new Intent(getMainActivity(), NativeTokenDetailActivity.class);
                intent.putExtra("denom", denom);
                startActivity(intent);
            });
        }
    }

    //with Unknown coin oec, bnb
    private void onBindUnKnownCoin(TokensAdapter.AssetHolder holder, int position) {
        final WalletBalance balance = mUnknown.get(position);
        holder.itemSymbol.setText(R.string.str_unknown);
        holder.itemSymbol.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite));
        holder.itemInnerSymbol.setText("");
        holder.itemFullName.setText("");
        holder.itemImg.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.token_ic));
        holder.itemBalance.setText(WDp.getDpAmount2(balance.getBalanceAmount(), 6, 6));
        holder.itemValue.setText(WDp.dpUserCurrencyValue(getBaseDao(), getCurrency(), balance.getDenom(), BigDecimal.ZERO, 6, priceProvider));
    }

    private void showAddressDialog() {
        AccountShowDialogFragment show = AccountShowDialogFragment.Companion.newInstance(
                mAccount.getAccountTitle(requireContext()),
                mAccount.address
        );
        showDialog(show);
    }

    public MainActivity getMainActivity() {
        return (MainActivity) getBaseActivity();
    }

    private Currency getCurrency() {
        return settingsInteractor.getCurrency();
    }

    // Section Header
    public class RecyclerViewHeader extends RecyclerView.ItemDecoration {
        private final int topPadding;

        private final boolean sticky;
        private final SectionCallback sectionCallback;

        private View headerView;
        private TextView mHeaderTitle;
        private TextView mItemCnt;

        public RecyclerViewHeader(Context context, boolean sticky, @NonNull SectionCallback sectionCallback) {
            this.sticky = sticky;
            this.sectionCallback = sectionCallback;

            topPadding = context.getResources().getDimensionPixelSize(R.dimen.space_32);
        }

        @Override
        public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.onDrawOver(c, parent, state);

            if (headerView == null) {
                headerView = inflateHeaderView(parent);
                mHeaderTitle = headerView.findViewById(R.id.header_title);
                mItemCnt = headerView.findViewById(R.id.recycler_cnt);
                fixLayoutSize(headerView, parent);
            }

            int previousHeader = -1;
            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                final int position = parent.getChildAdapterPosition(child);
                if (position == RecyclerView.NO_POSITION) {
                    return;
                }

                // section 구분
                int section = parent.getAdapter().getItemViewType(position);
                int title = sectionCallback.getSectionHeader(mBaseChain, section);

                switch (section) {
                    case SECTION_NATIVE_GRPC:
                        mItemCnt.setText("" + mNative.size());
                        break;
                    case SECTION_IBC_AUTHED_GRPC:
                        mItemCnt.setText("" + mIbcAuthedGrpc.size());
                        break;
                    case SECTION_IBC_UNKNOWN_GRPC:
                        mItemCnt.setText("" + mIbcUnknownGrpc.size());
                        break;
                    case SECTION_UNKNOWN_GRPC:
                        mItemCnt.setText("" + mUnknown.size());
                        break;

                    // osmosis pool token
                    case SECTION_OSMOSIS_POOL_GRPC:
                        mItemCnt.setText("" + mOsmosisPoolGrpc.size());
                        break;

                    // injective pool token
                    case SECTION_INJECTIVE_POOL_GRPC:
                        mItemCnt.setText("" + mInjectivePoolGrpc.size());
                        break;

                    // ether bridge token
                    case SECTION_ETHER_GRPC:
                        mItemCnt.setText("" + mEtherGrpc.size());
                        break;

                    // cosmos gravity dex token
                    case SECTION_GRAVICTY_DEX_GRPC:
                        mItemCnt.setText("" + mGravityDexGrpc.size());
                        break;

                    // kava token
                    case SECTION_KAVA_BEP2_GRPC:
                        mItemCnt.setText("" + mKavaBep2Grpc.size());
                        break;
                    case SECTION_ETC_GRPC:
                        mItemCnt.setText("" + mEtc.size());
                        break;

                    // cw20 token
                    case SECTION_CW20_GRPC:
                        mItemCnt.setText("" + mCW20Grpc.size());
                        break;
                    case SECTION_NATIVE:
                        mItemCnt.setText("" + mNative.size());
                        break;
                    case SECTION_ETC:
                        mItemCnt.setText("" + mEtc.size());
                        break;
                    case SECTION_UNKNOWN:
                        mItemCnt.setText("" + mUnknown.size());
                        break;
                    default:
                        break;
                }
                mHeaderTitle.setText(title);
                if (previousHeader != title || sectionCallback.isSection(getMainActivity().getBaseChain(), position)) {
                    drawHeader(c, child, headerView);
                    previousHeader = title;
                }
            }
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            int position = parent.getChildAdapterPosition(view);
            if (sectionCallback.isSection(getMainActivity().getBaseChain(), position)) {
                outRect.top = topPadding;
            }
        }

        private void drawHeader(Canvas c, View child, View headerView) {
            c.save();
            if (sticky) {
                c.translate(0, Math.max(0, child.getTop() - headerView.getHeight()));
            } else {
                c.translate(0, child.getTop() - headerView.getHeight());
            }
            headerView.draw(c);
            c.restore();
        }

        private View inflateHeaderView(RecyclerView parent) {
            return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sticky_header, parent, false);
        }

        private void fixLayoutSize(View view, ViewGroup parent) {
            int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(),
                    View.MeasureSpec.EXACTLY);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(),
                    View.MeasureSpec.UNSPECIFIED);

            int childWidth = ViewGroup.getChildMeasureSpec(widthSpec,
                    parent.getPaddingLeft() + parent.getPaddingRight(),
                    view.getLayoutParams().width);
            int childHeight = ViewGroup.getChildMeasureSpec(heightSpec,
                    parent.getPaddingTop() + parent.getPaddingBottom(),
                    view.getLayoutParams().height);

            view.measure(childWidth, childHeight);

            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        }
    }

    public interface SectionCallback {
        boolean isSection(BaseChain baseChain, int position);

        int getSectionHeader(BaseChain baseChain, int section);
    }
}