package wannabit.io.cosmostaion.fragment.main;

import static com.fulldive.wallet.models.BaseChain.BNB_MAIN;
import static com.fulldive.wallet.models.BaseChain.DESMOS_MAIN;
import static com.fulldive.wallet.models.BaseChain.KAVA_MAIN;
import static com.fulldive.wallet.models.BaseChain.OKEX_MAIN;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.fulldive.wallet.models.WalletBalance;
import com.fulldive.wallet.presentation.accounts.AccountShowDialogFragment;
import com.fulldive.wallet.presentation.main.MainActivity;

import java.util.List;

import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.base.BaseFragment;
import wannabit.io.cosmostaion.base.IBusyFetchListener;
import wannabit.io.cosmostaion.base.IRefreshTabListener;
import wannabit.io.cosmostaion.dao.Account;
import wannabit.io.cosmostaion.utils.PriceProvider;
import wannabit.io.cosmostaion.utils.WDp;
import wannabit.io.cosmostaion.widget.BaseHolder;
import wannabit.io.cosmostaion.widget.mainWallet.WalletBinanceHolder;
import wannabit.io.cosmostaion.widget.mainWallet.WalletChainHolder;
import wannabit.io.cosmostaion.widget.mainWallet.WalletDesmosAppHolder;
import wannabit.io.cosmostaion.widget.mainWallet.WalletGuideHolder;
import wannabit.io.cosmostaion.widget.mainWallet.WalletKavaIncentiveHolder;
import wannabit.io.cosmostaion.widget.mainWallet.WalletMintHolder;
import wannabit.io.cosmostaion.widget.mainWallet.WalletOkexHolder;
import wannabit.io.cosmostaion.widget.mainWallet.WalletPriceHolder;

public class MainSendFragment extends BaseFragment implements IBusyFetchListener, IRefreshTabListener {
    private CardView mCardView;
    private ImageView itemKeyStatus;
    private TextView mWalletAddress;
    private TextView mTotalValue;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private MainWalletAdapter mMainWalletAdapter;

    private Account mAccount;
    private BaseChain mBaseChain;
    private SettingsInteractor settingsInteractor;
    private BalancesInteractor balancesInteractor;

    public static MainSendFragment newInstance(Bundle bundle) {
        MainSendFragment fragment = new MainSendFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsInteractor = getAppInjector().getInstance(SettingsInteractor.class);
        balancesInteractor = getAppInjector().getInstance(BalancesInteractor.class);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_send, container, false);
        mCardView = rootView.findViewById(R.id.cardView);
        itemKeyStatus = rootView.findViewById(R.id.img_account);
        mWalletAddress = rootView.findViewById(R.id.wallet_address);
        mTotalValue = rootView.findViewById(R.id.total_value);
        mSwipeRefreshLayout = rootView.findViewById(R.id.layer_refresher);
        mRecyclerView = rootView.findViewById(R.id.recycler);

        mCardView.setOnClickListener(v -> showAddressDialog());

        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(rootView.getContext(), R.color.colorPrimary));
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            getMainActivity().onFetchAllData();
            onUpdateView();
            mMainWalletAdapter.notifyDataSetChanged();
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if (getMainActivity().floatingActionButton.isShown()) {
                        getMainActivity().floatingActionButton.hide();
                    }
                } else if (dy < 0) {
                    if (!getMainActivity().floatingActionButton.isShown()) {
                        getMainActivity().floatingActionButton.show();
                    }
                }
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        mMainWalletAdapter = new MainWalletAdapter();
        mRecyclerView.setAdapter(mMainWalletAdapter);

        onUpdateView();
        return rootView;
    }

    @Override
    public void onRefreshTab() {
        if (!isAdded()) return;
        mSwipeRefreshLayout.setRefreshing(false);
        onUpdateView();
    }

    @Override
    public void onBusyFetch() {
        if (!isAdded()) return;
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void onUpdateView() {
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
        final PriceProvider priceProvider = getMainActivity()::getPrice;
        mTotalValue.setText(WDp.dpAllAssetValueUserCurrency(mBaseChain, settingsInteractor.getCurrency(), getBaseDao(), getBalances(), priceProvider));
        mMainWalletAdapter.notifyDataSetChanged();
    }

    private List<WalletBalance> getBalances() {
        return balancesInteractor.getBalances(mAccount.id).blockingGet();
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

    private class MainWalletAdapter extends RecyclerView.Adapter<BaseHolder> {
        private static final int TYPE_WALLET = 0;
        private static final int TYPE_BINANCE = 1;
        private static final int TYPE_OKEX = 2;

        private static final int TYPE_KAVA_INCENTIVE = 40;
        private static final int TYPE_DESMOS_APP = 50;
        private static final int TYPE_PRICE = 80;
        private static final int TYPE_MINT = 81;
        private static final int TYPE_GIUDE = 82;

        @NonNull
        @Override
        public BaseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            if (viewType == TYPE_OKEX) {
                return new WalletOkexHolder(getLayoutInflater().inflate(R.layout.item_wallet_okex, viewGroup, false));

            } else if (viewType == TYPE_BINANCE) {
                return new WalletBinanceHolder(getLayoutInflater().inflate(R.layout.item_wallet_binance, viewGroup, false));

            } else if (viewType == TYPE_WALLET) {
                return new WalletChainHolder(getLayoutInflater().inflate(R.layout.item_wallet_chain, viewGroup, false));
            } else if (viewType == TYPE_PRICE) {
                return new WalletPriceHolder(getLayoutInflater().inflate(R.layout.item_wallet_price, viewGroup, false));

            } else if (viewType == TYPE_MINT) {
                return new WalletMintHolder(getLayoutInflater().inflate(R.layout.item_wallet_mint, viewGroup, false));

            } else if (viewType == TYPE_GIUDE) {
                return new WalletGuideHolder(getLayoutInflater().inflate(R.layout.item_wallet_guide, viewGroup, false));

            } else if (viewType == TYPE_KAVA_INCENTIVE) {
                return new WalletKavaIncentiveHolder(getLayoutInflater().inflate(R.layout.item_wallet_kavaincentive, viewGroup, false));

            } else if (viewType == TYPE_DESMOS_APP) {
                return new WalletDesmosAppHolder(getLayoutInflater().inflate(R.layout.item_wallet_desmos_app, viewGroup, false));

            }
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull BaseHolder viewHolder, int position) {
            final MainActivity mainActivity = getMainActivity();
            viewHolder.onBindHolder(mainActivity);
        }

        @Override
        public int getItemCount() {
            if (getMainActivity().getBaseChain() == null) {
                return 0;
            }
            if (getMainActivity().getBaseChain().equals(KAVA_MAIN.INSTANCE) || getMainActivity().getBaseChain().equals(DESMOS_MAIN.INSTANCE)) {
                return 5;
            } else if (getMainActivity().getBaseChain().isGRPC()) {
                return 4;
            } else {
                return 3;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (getMainActivity().getBaseChain().equals(KAVA_MAIN.INSTANCE)) {
                if (position == 0) {
                    return TYPE_WALLET;
                } else if (position == 1) {
                    return TYPE_KAVA_INCENTIVE;
                } else if (position == 2) {
                    return TYPE_PRICE;
                } else if (position == 3) {
                    return TYPE_MINT;
                } else if (position == 4) {
                    return TYPE_GIUDE;
                }

            } else if (getMainActivity().getBaseChain().equals(DESMOS_MAIN.INSTANCE)) {
                if (position == 0) {
                    return TYPE_WALLET;
                } else if (position == 1) {
                    return TYPE_PRICE;
                } else if (position == 2) {
                    return TYPE_MINT;
                } else if (position == 3) {
                    return TYPE_DESMOS_APP;
                } else if (position == 4) {
                    return TYPE_GIUDE;
                }

            } else if (getMainActivity().getBaseChain().isGRPC()) {
                if (position == 0) {
                    return TYPE_WALLET;
                } else if (position == 1) {
                    return TYPE_PRICE;
                } else if (position == 2) {
                    return TYPE_MINT;
                } else if (position == 3) {
                    return TYPE_GIUDE;
                }

            } else if (getMainActivity().getBaseChain().equals(BNB_MAIN.INSTANCE) || getMainActivity().getBaseChain().equals(OKEX_MAIN.INSTANCE)) {
                if (position == 0) {
                    if (getMainActivity().getBaseChain().equals(BNB_MAIN.INSTANCE)) {
                        return TYPE_BINANCE;
                    } else if (getMainActivity().getBaseChain().equals(OKEX_MAIN.INSTANCE)) {
                        return TYPE_OKEX;
                    }
                } else if (position == 1) {
                    return TYPE_PRICE;
                } else if (position == 2) {
                    return TYPE_GIUDE;
                }

            }
            return 0;
        }
    }
}