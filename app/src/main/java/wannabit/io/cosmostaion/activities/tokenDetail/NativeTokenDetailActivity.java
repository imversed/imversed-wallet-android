package wannabit.io.cosmostaion.activities.tokenDetail;

import static com.fulldive.wallet.models.BaseChain.BNB_MAIN;
import static com.fulldive.wallet.models.BaseChain.KAVA_MAIN;
import static com.fulldive.wallet.models.BaseChain.OKEX_MAIN;
import static wannabit.io.cosmostaion.base.BaseConstant.BINANCE_TOKEN_IMG_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.CONST_PW_TX_SIMPLE_SEND;
import static wannabit.io.cosmostaion.base.BaseConstant.OKEX_COIN_IMG_URL;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fulldive.wallet.models.BaseChain;
import com.fulldive.wallet.models.Currency;
import com.fulldive.wallet.models.WalletBalance;
import com.fulldive.wallet.presentation.accounts.AccountShowDialogFragment;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;

import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.activities.SendActivity;
import wannabit.io.cosmostaion.base.BaseActivity;
import wannabit.io.cosmostaion.dao.BnbToken;
import wannabit.io.cosmostaion.dao.OkToken;
import wannabit.io.cosmostaion.dao.Price;
import wannabit.io.cosmostaion.dialog.Dialog_WatchMode;
import wannabit.io.cosmostaion.utils.PriceProvider;
import wannabit.io.cosmostaion.utils.WDp;
import wannabit.io.cosmostaion.utils.WUtil;
import wannabit.io.cosmostaion.widget.tokenDetail.TokenDetailSupportHolder;
import wannabit.io.cosmostaion.widget.tokenDetail.VestingHolder;

public class NativeTokenDetailActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private ImageView mToolbarSymbolImg;
    private TextView mToolbarSymbol;
    private TextView mItemPerPrice;
    private ImageView mItemUpDownImg;
    private TextView mItemUpDownPrice;

    private CardView mBtnAddressPopup;
    private ImageView mKeyState;
    private TextView mTotalValue;
    private TextView mAddress;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private RelativeLayout mBtnIbcSend;
    private RelativeLayout mBtnBep3Send;
    private RelativeLayout mBtnSend;

    private NativeTokenAdapter mAdapter;
    private String mDenom;

    private final Boolean mHasVesting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_detail_native);

        mToolbar = findViewById(R.id.toolbar);
        mToolbarSymbolImg = findViewById(R.id.toolbar_symbol_img);
        mToolbarSymbol = findViewById(R.id.toolbar_symbol);
        mItemPerPrice = findViewById(R.id.per_price);
        mItemUpDownImg = findViewById(R.id.ic_price_updown);
        mItemUpDownPrice = findViewById(R.id.dash_price_updown_tx);

        mBtnAddressPopup = findViewById(R.id.cardView);
        mKeyState = findViewById(R.id.img_account);
        mAddress = findViewById(R.id.account_Address);
        mTotalValue = findViewById(R.id.total_value);
        mSwipeRefreshLayout = findViewById(R.id.layer_refresher);
        mRecyclerView = findViewById(R.id.recycler);
        mBtnIbcSend = findViewById(R.id.btn_ibc_send);
        mBtnBep3Send = findViewById(R.id.btn_bep3_send);
        mBtnSend = findViewById(R.id.btn_send);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDenom = getIntent().getStringExtra("denom");

        if (getBaseChain().equals(BNB_MAIN.INSTANCE)) {
            if (WUtil.isBep3Coin(mDenom)) {
                mBtnBep3Send.setVisibility(View.VISIBLE);
            }
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new NativeTokenAdapter();
        mRecyclerView.setAdapter(mAdapter);

        //prepare for token history
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onUpdateView();
            }
        });

        onUpdateView();
        mBtnAddressPopup.setOnClickListener(this);
        mBtnSend.setOnClickListener(this);
        mBtnIbcSend.setOnClickListener(this);
        mBtnBep3Send.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onUpdateView() {
        final BaseChain chain = getBaseChain();
        final String mainDenom = chain.getMainDenom();
        final Currency currency = settingsInteractor.getCurrency();
        final PriceProvider priceProvider = this::getPrice;

        mBtnAddressPopup.setCardBackgroundColor(WDp.getChainBgColor(NativeTokenDetailActivity.this, chain));

        if (chain.equals(OKEX_MAIN.INSTANCE)) {
            final OkToken okToken = getBaseDao().okToken(mDenom);
            Picasso.get().load(OKEX_COIN_IMG_URL + okToken.original_symbol + ".png").placeholder(R.drawable.token_ic).error(R.drawable.token_ic).fit().into(mToolbarSymbolImg);
            mToolbarSymbol.setText(okToken.original_symbol.toUpperCase());
            mToolbarSymbol.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));

            BigDecimal convertedOktAmount = WDp.convertTokenToOkt(this, getBaseDao(), mDenom, priceProvider);
            mTotalValue.setText(WDp.dpUserCurrencyValue(getBaseDao(), currency, mainDenom, convertedOktAmount, 0, priceProvider));

            if (okToken.original_symbol.equalsIgnoreCase("okb")) {
                mItemPerPrice.setText(WDp.dpPerUserCurrencyValue(getBaseDao(), currency, "okb", priceProvider));

                final Price price = getPrice("okb");
                mItemUpDownPrice.setText(WDp.dpValueChange(price));
                final BigDecimal lastUpDown = WDp.valueChange(price);
                if (lastUpDown.compareTo(BigDecimal.ZERO) > 0) {
                    mItemUpDownImg.setVisibility(View.VISIBLE);
                    mItemUpDownImg.setImageResource(R.drawable.ic_price_up);
                } else if (lastUpDown.compareTo(BigDecimal.ZERO) < 0) {
                    mItemUpDownImg.setVisibility(View.VISIBLE);
                    mItemUpDownImg.setImageResource(R.drawable.ic_price_down);
                } else {
                    mItemUpDownImg.setVisibility(View.INVISIBLE);
                }
            } else {
                mItemPerPrice.setText("");
                mItemUpDownPrice.setText("");
                mItemUpDownImg.setVisibility(View.INVISIBLE);
            }

        } else if (chain.equals(BNB_MAIN.INSTANCE)) {
            final WalletBalance balance = getFullBalance(mDenom);
            final BigDecimal amount = balance.getTotalAmount();
            final BnbToken bnbToken = getBaseDao().getBnbToken(mDenom);
            Picasso.get().load(BINANCE_TOKEN_IMG_URL + bnbToken.original_symbol + ".png").placeholder(R.drawable.token_ic).error(R.drawable.token_ic).fit().into(mToolbarSymbolImg);
            mToolbarSymbol.setText(bnbToken.original_symbol.toUpperCase());
            mToolbarSymbol.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));

            BigDecimal convertedBnbAmount = WUtil.getBnbConvertAmount(getBaseDao(), mDenom, amount);
            mTotalValue.setText(WDp.dpUserCurrencyValue(getBaseDao(), currency, mainDenom, convertedBnbAmount, 0, priceProvider));

            mItemPerPrice.setText(WUtil.dpBnbTokenUserCurrencyPrice(getBaseDao(), currency, mDenom, priceProvider));
            mItemUpDownPrice.setText("");
            mItemUpDownImg.setVisibility(View.INVISIBLE);
        }

        mAddress.setText(getAccount().address);
        mKeyState.setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.colorGray0), android.graphics.PorterDuff.Mode.SRC_IN);
        if (getAccount().hasPrivateKey) {
            mKeyState.setColorFilter(WDp.getChainColor(getBaseContext(), chain), android.graphics.PorterDuff.Mode.SRC_IN);
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onClick(View v) {
        if (v.equals(mBtnAddressPopup)) {
            AccountShowDialogFragment show = AccountShowDialogFragment.Companion.newInstance(
                    getAccount().getAccountTitle(this),
                    getAccount().address
            );
            showDialog(show);

        } else if (v.equals(mBtnIbcSend)) {
            Toast.makeText(getBaseContext(), R.string.error_prepare, Toast.LENGTH_SHORT).show();
            return;

        } else if (v.equals(mBtnBep3Send)) {
            startHTLCSendActivity(mDenom);

        } else if (v.equals(mBtnSend)) {
            if (!getAccount().hasPrivateKey) {
                Dialog_WatchMode add = Dialog_WatchMode.newInstance();
                showDialog(add);
                return;
            }
            Intent intent = new Intent(getBaseContext(), SendActivity.class);
            BigDecimal mainAvailable = getBalance(getBaseChain().getMainDenom());
            BigDecimal feeAmount = getBaseChain().getGasFeeEstimateCalculator().calc(getBaseChain(), CONST_PW_TX_SIMPLE_SEND);
            if (mainAvailable.compareTo(feeAmount) < 0) {
                Toast.makeText(getBaseContext(), R.string.error_not_enough_fee, Toast.LENGTH_SHORT).show();
                return;
            }
            intent.putExtra("sendTokenDenom", mDenom);
            startActivity(intent);
        }

    }

    private class NativeTokenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int TYPE_UNKNOWN = -1;
        private static final int TYPE_NATIVE = 0;

        private static final int TYPE_VESTING = 99;
        private static final int TYPE_HISTORY = 100;

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            if (viewType == TYPE_UNKNOWN) {

            } else if (viewType == TYPE_NATIVE) {
                return new TokenDetailSupportHolder(getLayoutInflater().inflate(R.layout.item_amount_detail, viewGroup, false));

            } else if (viewType == TYPE_VESTING) {
                return new VestingHolder(getLayoutInflater().inflate(R.layout.layout_vesting_schedule, viewGroup, false));
            }

//            } else if (viewType == TYPE_HISTORY) {
//                return new HistoryHolder(getLayoutInflater().inflate(R.layout.item_history, viewGroup, false));
//            }
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            if (getItemViewType(position) == TYPE_NATIVE) {
                TokenDetailSupportHolder holder = (TokenDetailSupportHolder) viewHolder;
                if (getBaseChain().equals(KAVA_MAIN.INSTANCE)) {
                    holder.onBindKavaToken(NativeTokenDetailActivity.this, getBaseDao(), mDenom);
                } else if (getBaseChain().equals(BNB_MAIN.INSTANCE)) {
                    holder.onBindBNBTokens(NativeTokenDetailActivity.this, getBaseDao(), mDenom);
                } else if (getBaseChain().equals(OKEX_MAIN.INSTANCE)) {
                    holder.onBindOKTokens(NativeTokenDetailActivity.this, getBaseDao(), mDenom);
                }

            } else if (getItemViewType(position) == TYPE_VESTING) {
                VestingHolder holder = (VestingHolder) viewHolder;
                holder.onBindTokenHolder(NativeTokenDetailActivity.this, getBaseChain(), getBaseDao(), mDenom);
//
//            } else if (getItemViewType(position) == TYPE_HISTORY) {
//
//            } else if (getItemViewType(position) == TYPE_UNKNOWN) {
            }

        }

        @Override
        public int getItemCount() {
            if (mHasVesting) {
                return 2;
            }
            return 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (getBaseChain().equals(BNB_MAIN.INSTANCE)) {
                if (position == 0) return TYPE_NATIVE;
                else return TYPE_HISTORY;

            } else if (getBaseChain().equals(OKEX_MAIN.INSTANCE)) {
                if (position == 0) return TYPE_NATIVE;
                else return TYPE_HISTORY;
            }
            return TYPE_UNKNOWN;
        }
    }
}
