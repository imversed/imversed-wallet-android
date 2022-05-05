package com.fulldive.wallet.presentation.main.tokens;

import static com.fulldive.wallet.models.BaseChain.BNB_MAIN;
import static com.fulldive.wallet.models.BaseChain.COSMOS_MAIN;
import static com.fulldive.wallet.models.BaseChain.EMONEY_MAIN;
import static com.fulldive.wallet.models.BaseChain.GRABRIDGE_MAIN;
import static com.fulldive.wallet.models.BaseChain.INJ_MAIN;
import static com.fulldive.wallet.models.BaseChain.JUNO_MAIN;
import static com.fulldive.wallet.models.BaseChain.KAVA_MAIN;
import static com.fulldive.wallet.models.BaseChain.OKEX_MAIN;
import static com.fulldive.wallet.models.BaseChain.OSMOSIS_MAIN;
import static com.fulldive.wallet.models.BaseChain.SIF_MAIN;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_HARD;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_HTLC_KAVA_BNB;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_HTLC_KAVA_BTCB;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_HTLC_KAVA_BUSD;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_HTLC_KAVA_XRPB;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_ION;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_SWP;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_USDX;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

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
import wannabit.io.cosmostaion.dao.Cw20Assets;
import wannabit.io.cosmostaion.dao.IbcToken;
import wannabit.io.cosmostaion.utils.PriceProvider;
import wannabit.io.cosmostaion.utils.WDp;
import wannabit.io.cosmostaion.utils.WUtil;

public class MainTokensFragment extends BaseFragment implements IBusyFetchListener, IRefreshTabListener {

    public final static int SECTION_NATIVE = 0;
    public final static int SECTION_IBC_AUTHED_GRPC = 1;
    public final static int SECTION_OSMOSIS_POOL_GRPC = 2;
    public final static int SECTION_ETHER_GRPC = 3;
    public final static int SECTION_IBC_UNKNOWN_GRPC = 4;
    public final static int SECTION_GRAVICTY_DEX_GRPC = 5;
    public final static int SECTION_INJECTIVE_POOL_GRPC = 6;
    public final static int SECTION_KAVA_BEP2_GRPC = 7;
    public final static int SECTION_ETC = 8;
    public final static int SECTION_CW20_GRPC = 9;
    public final static int SECTION_UNKNOWN = 10;

    private CardView cardView;
    private ImageView keyStatusImageView;
    private TextView addressTextView;
    private TextView totalValueTextView;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayout emptyLayout;

    private TokensAdapter adapter;
    private RecyclerViewHeader recyclerViewHeader;

    private final ArrayList<WalletBalance> ibcAuthedItems = new ArrayList<>();
    private final ArrayList<WalletBalance> osmosisPoolItems = new ArrayList<>();
    private final ArrayList<WalletBalance> etherItems = new ArrayList<>();
    private final ArrayList<WalletBalance> ibcUnknownItems = new ArrayList<>();
    private final ArrayList<WalletBalance> GravityDexItems = new ArrayList<>();
    private final ArrayList<WalletBalance> injectivePoolItems = new ArrayList<>();
    private final ArrayList<WalletBalance> kavaBep2Items = new ArrayList<>();
    private final ArrayList<WalletBalance> nativeItems = new ArrayList<>();
    private final ArrayList<WalletBalance> etcItems = new ArrayList<>();
    private final ArrayList<WalletBalance> unknownItems = new ArrayList<>();
    private ArrayList<Cw20Assets> CW20Items = new ArrayList<>();

    private Account account;
    private BaseChain baseChain;
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
        cardView = rootView.findViewById(R.id.cardView);
        keyStatusImageView = rootView.findViewById(R.id.img_account);
        addressTextView = rootView.findViewById(R.id.wallet_address);
        totalValueTextView = rootView.findViewById(R.id.total_value);
        swipeRefreshLayout = rootView.findViewById(R.id.layer_refresher);
        recyclerView = rootView.findViewById(R.id.recycler);
        emptyLayout = rootView.findViewById(R.id.empty_token);

        cardView.setOnClickListener(v -> showAddressDialog());

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(rootView.getContext(), R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            onUpdateInfo();
            getMainActivity().onFetchAllData();
        });

        recyclerView.setOnTouchListener((View view, MotionEvent motionEvent) -> swipeRefreshLayout.isRefreshing());
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        adapter = new TokensAdapter();
        recyclerView.setAdapter(adapter);

        recyclerViewHeader = new RecyclerViewHeader(getMainActivity(), true, getSectionGrpcCall());
        recyclerView.addItemDecoration(recyclerViewHeader);

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
        account = getMainActivity().getAccount();
        baseChain = BaseChain.getChain(account.baseChain);
        recyclerViewHeader.baseChain = baseChain;
        adapter.baseChain = baseChain;
        adapter.currency = getCurrency();
        adapter.priceProvider = priceProvider;
        adapter.baseData = getBaseDao();
        adapter.onBalanceProvider = denom -> getMainActivity().getFullBalance(denom);
        adapter.itemsClickListeners = new TokensAdapter.OnItemsClickListeners() {
            @Override
            public void onStackingTokenClicked(String denom) {
                Intent intent = new Intent(getMainActivity(), StakingTokenGrpcActivity.class);
                intent.putExtra("denom", denom);
                startActivity(intent);
            }

            @Override
            public void onNativeTokenClicked(String denom) {
                Intent intent = new Intent(getMainActivity(), NativeTokenGrpcActivity.class);
                intent.putExtra("denom", denom);
                startActivity(intent);
            }

            @Override
            public void onIbcTokenClicked(String denom) {
                Intent intent = new Intent(getMainActivity(), IBCTokenDetailActivity.class);
                intent.putExtra("denom", denom);
                startActivity(intent);
            }

            @Override
            public void onOsmoPoolTokenClicked(String denom) {
                Intent intent = new Intent(getMainActivity(), POOLTokenDetailActivity.class);
                intent.putExtra("denom", denom);
                startActivity(intent);
            }

            @Override
            public void onErcTokenClicked(String denom) {
                Intent intent = new Intent(getMainActivity(), BridgeTokenGrpcActivity.class);
                intent.putExtra("denom", denom);
                startActivity(intent);
            }

            @Override
            public void onCW20TokenClicked(Cw20Assets cw20Asset) {
                Intent intent = new Intent(getMainActivity(), ContractTokenGrpcActivity.class);
                intent.putExtra("cw20Asset", cw20Asset);
                startActivity(intent);
            }

            @Override
            public void onNativeStackingTokenClicked() {
                Intent intent = new Intent(getMainActivity(), StakingTokenDetailActivity.class);
                startActivity(intent);
            }

            @Override
            public void onEtcTokenClicked(String denom) {
                Intent intent = new Intent(getMainActivity(), NativeTokenDetailActivity.class);
                intent.putExtra("denom", denom);
                startActivity(intent);
            }
        };

        cardView.setCardBackgroundColor(WDp.getChainBgColor(getMainActivity(), baseChain));
        if (account.hasPrivateKey) {
            keyStatusImageView.setColorFilter(WDp.getChainColor(getMainActivity(), baseChain), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            keyStatusImageView.setColorFilter(ContextCompat.getColor(getMainActivity(), R.color.colorGray0), android.graphics.PorterDuff.Mode.SRC_IN);
        }
        addressTextView.setText(account.address);
        totalValueTextView.setText(WDp.dpAllAssetValueUserCurrency(baseChain, getCurrency(), getBaseDao(), getBalances(), priceProvider));
    }

    private SectionCallback getSectionGrpcCall() {
        return new SectionCallback() {
            @Override
            public boolean isSection(BaseChain baseChain, int position) {
                if (baseChain.equals(OSMOSIS_MAIN.INSTANCE)) {
                    return position == 0 || position == nativeItems.size() || position == nativeItems.size() + ibcAuthedItems.size()
                            || position == nativeItems.size() + ibcAuthedItems.size() + osmosisPoolItems.size()
                            || position == nativeItems.size() + ibcAuthedItems.size() + osmosisPoolItems.size() + ibcUnknownItems.size();

                } else if (baseChain.equals(COSMOS_MAIN.INSTANCE)) {
                    return position == 0 || position == nativeItems.size() || position == nativeItems.size() + ibcAuthedItems.size()
                            || position == nativeItems.size() + ibcAuthedItems.size() + GravityDexItems.size()
                            || position == nativeItems.size() + ibcAuthedItems.size() + GravityDexItems.size() + ibcUnknownItems.size();

                } else if (baseChain.equals(SIF_MAIN.INSTANCE) || baseChain.equals(GRABRIDGE_MAIN.INSTANCE)) {
                    return position == 0 || position == nativeItems.size() || position == nativeItems.size() + ibcAuthedItems.size()
                            || position == nativeItems.size() + ibcAuthedItems.size() + etherItems.size()
                            || position == nativeItems.size() + ibcAuthedItems.size() + etherItems.size() + ibcUnknownItems.size();

                } else if (baseChain.equals(INJ_MAIN.INSTANCE)) {
                    return position == 0 || position == nativeItems.size() || position == nativeItems.size() + ibcAuthedItems.size()
                            || position == nativeItems.size() + ibcAuthedItems.size() + etherItems.size()
                            || position == nativeItems.size() + ibcAuthedItems.size() + etherItems.size() + injectivePoolItems.size()
                            || position == nativeItems.size() + ibcAuthedItems.size() + etherItems.size() + injectivePoolItems.size() + ibcUnknownItems.size();

                } else if (baseChain.equals(KAVA_MAIN.INSTANCE)) {
                    return position == 0 || position == nativeItems.size() || position == nativeItems.size() + ibcAuthedItems.size()
                            || position == nativeItems.size() + ibcAuthedItems.size() + kavaBep2Items.size()
                            || position == nativeItems.size() + ibcAuthedItems.size() + kavaBep2Items.size() + etcItems.size()
                            || position == nativeItems.size() + ibcAuthedItems.size() + kavaBep2Items.size() + etcItems.size() + ibcUnknownItems.size();

                } else if (baseChain.equals(JUNO_MAIN.INSTANCE)) {
                    return position == 0 || position == nativeItems.size() || position == nativeItems.size() + ibcAuthedItems.size()
                            || position == nativeItems.size() + ibcAuthedItems.size() + CW20Items.size()
                            || position == nativeItems.size() + ibcAuthedItems.size() + CW20Items.size() + ibcUnknownItems.size();

                } else if (baseChain.isGRPC()) {
                    return position == 0 || position == nativeItems.size() || position == nativeItems.size() + ibcAuthedItems.size()
                            || position == nativeItems.size() + ibcAuthedItems.size() + ibcUnknownItems.size();

                } else {
                    return position == 0 || position == nativeItems.size() || position == nativeItems.size() + etcItems.size();
                }
            }

            @Override
            public int getSectionHeader(BaseChain baseChain, int section) {
                switch (section) {
                    case SECTION_NATIVE:
                        return R.string.str_native_token_title;
                    case SECTION_IBC_AUTHED_GRPC:
                        return R.string.str_ibc_token_title;
                    case SECTION_IBC_UNKNOWN_GRPC:
                        return R.string.str_unknown_ibc_token_title;
                    case SECTION_UNKNOWN:
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
                    case SECTION_ETC:
                        if (baseChain.equals(OKEX_MAIN.INSTANCE)) {
                            return R.string.str_oec_kip10_title;
                        }
                        return R.string.str_etc_token_title;
                    case SECTION_CW20_GRPC:
                        return R.string.str_cw20_token_title;
                }

                return R.string.str_unknown_token_title;
            }

            @Override
            public int getSectionCount(int section) {
                switch (section) {
                    case SECTION_NATIVE:
                        return nativeItems.size();
                    case SECTION_IBC_AUTHED_GRPC:
                        return ibcAuthedItems.size();
                    case SECTION_IBC_UNKNOWN_GRPC:
                        return ibcUnknownItems.size();
                    case SECTION_UNKNOWN:
                        return unknownItems.size();
                    case SECTION_OSMOSIS_POOL_GRPC:
                        return osmosisPoolItems.size();
                    case SECTION_INJECTIVE_POOL_GRPC:
                        return injectivePoolItems.size();
                    case SECTION_ETHER_GRPC:
                        return etherItems.size();
                    case SECTION_GRAVICTY_DEX_GRPC:
                        return GravityDexItems.size();
                    case SECTION_KAVA_BEP2_GRPC:
                        return kavaBep2Items.size();
                    case SECTION_ETC:
                        return etcItems.size();
                    case SECTION_CW20_GRPC:
                        return CW20Items.size();
                    default:
                }
                return 0;
            }
        };
    }

    @Override
    public void onRefreshTab() {
        if (!isAdded()) return;
        swipeRefreshLayout.setRefreshing(false);
        recyclerView.getRecycledViewPool().clear();
        onUpdateInfo();
        onUpdateView();
    }

    @Override
    public void onBusyFetch() {
        if (!isAdded()) return;
        swipeRefreshLayout.setRefreshing(false);
    }

    private void onUpdateView() {
        final String mainDenom = baseChain.getMainDenom();
        CW20Items = getBaseDao().getCw20sGrpc(baseChain);
        ibcAuthedItems.clear();
        osmosisPoolItems.clear();
        etherItems.clear();
        ibcUnknownItems.clear();
        GravityDexItems.clear();
        injectivePoolItems.clear();
        kavaBep2Items.clear();
        nativeItems.clear();
        etcItems.clear();

        List<WalletBalance> balances = getBalances();

        for (WalletBalance balance : balances) {
            if (balance.getDenom().equalsIgnoreCase(mainDenom)) {
                nativeItems.add(balance);
            } else if (baseChain.equals(BNB_MAIN.INSTANCE)) {
                etcItems.add(balance);
            } else if (baseChain.equals(OKEX_MAIN.INSTANCE)) {
                etcItems.add(balance);
            } else if (balance.isIbc()) {
                final IbcToken ibcToken = getBaseDao().getIbcToken(balance.getIbcHash());
                if (ibcToken != null && ibcToken.auth) {
                    ibcAuthedItems.add(balance);
                } else {
                    ibcUnknownItems.add(balance);
                }
            } else if (baseChain.equals(OSMOSIS_MAIN.INSTANCE) && balance.osmosisAmm()) {
                osmosisPoolItems.add(balance);
            } else if (baseChain.equals(OSMOSIS_MAIN.INSTANCE) && balance.getDenom().equalsIgnoreCase(TOKEN_ION) ||
                    baseChain.equals(EMONEY_MAIN.INSTANCE) && balance.getDenom().startsWith("e")) {
                nativeItems.add(balance);
            } else if (baseChain.equals(SIF_MAIN.INSTANCE) && balance.getDenom().startsWith("c") ||
                    baseChain.equals(GRABRIDGE_MAIN.INSTANCE) && balance.getDenom().startsWith("gravity") ||
                    baseChain.equals(INJ_MAIN.INSTANCE) && balance.getDenom().startsWith("peggy")) {
                etherItems.add(balance);
            } else if (baseChain.equals(COSMOS_MAIN.INSTANCE) && balance.getDenom().startsWith("pool")) {
                GravityDexItems.add(balance);
            } else if (baseChain.equals(INJ_MAIN.INSTANCE) && balance.getDenom().startsWith("share")) {
                injectivePoolItems.add(balance);
            } else if (baseChain.equals(KAVA_MAIN.INSTANCE)) {
                if (balance.getDenom().equals(TOKEN_HARD) || balance.getDenom().equalsIgnoreCase(TOKEN_USDX) || balance.getDenom().equalsIgnoreCase(TOKEN_SWP)) {
                    nativeItems.add(balance);
                } else if (balance.getDenom().equalsIgnoreCase(TOKEN_HTLC_KAVA_BNB) || balance.getDenom().equalsIgnoreCase(TOKEN_HTLC_KAVA_BTCB) ||
                        balance.getDenom().equalsIgnoreCase(TOKEN_HTLC_KAVA_XRPB) || balance.getDenom().equalsIgnoreCase(TOKEN_HTLC_KAVA_BUSD)) {
                    kavaBep2Items.add(balance);
                } else if (balance.getDenom().equalsIgnoreCase("btch") || balance.getDenom().equalsIgnoreCase("hbtc")) {
                    etcItems.add(balance);
                }
            } else {
                unknownItems.add(balance);
            }
        }

        if (baseChain.isGRPC()) {
            WUtil.onSortingBalance(nativeItems, baseChain);
            WUtil.onSortingGravityPool(GravityDexItems, getBaseDao());
            WUtil.onSortingOsmosisPool(osmosisPoolItems);
            WUtil.onSortingInjectivePool(injectivePoolItems);

        } else if (baseChain.equals(BNB_MAIN.INSTANCE) || baseChain.equals(OKEX_MAIN.INSTANCE)) {
            WUtil.onSortingNativeCoins(etcItems, baseChain);
        } else {
            WUtil.onSortingNativeCoins(nativeItems, baseChain);
        }

        if (!balances.isEmpty()) {
            adapter.balances = balances;
            adapter.ibcAuthedItems = ibcAuthedItems;
            adapter.poolItems = osmosisPoolItems;
            adapter.etherItems = etherItems;
            adapter.ibcUnknownItems = ibcUnknownItems;
            adapter.GravityDexItems = GravityDexItems;
            adapter.injectivePoolItems = injectivePoolItems;
            adapter.kavaBep2Items = kavaBep2Items;
            adapter.nativeItems = nativeItems;
            adapter.etcItems = etcItems;
            adapter.unknownItems = unknownItems;
            adapter.CW20Items = CW20Items;
            adapter.notifyDataSetChanged();
            emptyLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            emptyLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    private List<WalletBalance> getBalances() {
        return balancesInteractor.getBalances(account.id).blockingGet();
    }


    private void showAddressDialog() {
        AccountShowDialogFragment show = AccountShowDialogFragment.Companion.newInstance(
                account.getAccountTitle(requireContext()),
                account.address
        );
        showDialog(show);
    }

    public MainActivity getMainActivity() {
        return (MainActivity) getBaseActivity();
    }

    private Currency getCurrency() {
        return settingsInteractor.getCurrency();
    }
}
