package wannabit.io.cosmostaion.activities.tokenDetail;

import static com.fulldive.wallet.models.BaseChain.COSMOS_MAIN;
import static com.fulldive.wallet.models.BaseChain.INJ_MAIN;
import static com.fulldive.wallet.models.BaseChain.OSMOSIS_MAIN;

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

import com.fulldive.wallet.models.Currency;
import com.fulldive.wallet.presentation.accounts.AccountShowDialogFragment;

import java.math.BigDecimal;

import tendermint.liquidity.v1beta1.Liquidity;
import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.base.BaseActivity;
import wannabit.io.cosmostaion.dao.Price;
import wannabit.io.cosmostaion.utils.PriceProvider;
import wannabit.io.cosmostaion.utils.WDp;
import wannabit.io.cosmostaion.utils.WUtil;
import wannabit.io.cosmostaion.widget.tokenDetail.TokenDetailSupportHolder;

public class POOLTokenDetailActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private ImageView mToolbarSymbolImg;
    private TextView mToolbarSymbol;
    private TextView mItemPerPrice;
    private ImageView mItemUpDownImg;
    private TextView mItemUpDownPrice;

    private CardView mBtnAddressPopup;
    private ImageView mKeyState;
    private TextView mAddress;
    private TextView mTotalValue;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private RelativeLayout mBtnIbcSend;
    private RelativeLayout mBtnSend;

    private POOlTokenAdapter mAdapter;
    private String mPoolDenom;

    private int mDivideDecimal = 18;
    private int mDisplayDecimal = 18;
    private BigDecimal mTotalAmount = BigDecimal.ZERO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_detail);

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
        mBtnSend = findViewById(R.id.btn_send);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPoolDenom = getIntent().getStringExtra("denom");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new POOlTokenAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onUpdateView();
            }
        });

        onUpdateView();
        mBtnAddressPopup.setOnClickListener(this);
        mBtnIbcSend.setOnClickListener(this);
        mBtnSend.setOnClickListener(this);
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
        final Currency currency = settingsInteractor.getCurrency();
        final PriceProvider priceProvider = this::getPrice;
        if (getBaseChain().equals(OSMOSIS_MAIN.INSTANCE)) {
            WUtil.DpOsmosisTokenImg(getBaseDao(), mToolbarSymbolImg, mPoolDenom);
            String[] split = mPoolDenom.split("/");
            mToolbarSymbol.setText("GAMM-" + split[split.length - 1]);
            mToolbarSymbol.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));

            mDivideDecimal = 18;
            mDisplayDecimal = 18;
            mTotalAmount = getBalance(mPoolDenom);
            mTotalValue.setText(WDp.dpUserCurrencyValue(getBaseDao(), currency, mPoolDenom, mTotalAmount, mDivideDecimal, priceProvider));

            mBtnIbcSend.setVisibility(View.VISIBLE);

        } else if (getBaseChain().equals(COSMOS_MAIN.INSTANCE)) {
            WUtil.DpCosmosTokenImg(getBaseDao(), mToolbarSymbolImg, mPoolDenom);
            Liquidity.Pool poolInfo = getBaseDao().getGravityPoolByDenom(mPoolDenom);
            if (poolInfo != null) {
                mToolbarSymbol.setText("GDEX-" + poolInfo.getId());
                mToolbarSymbol.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            }

            mDivideDecimal = 6;
            mDisplayDecimal = 6;
            mTotalAmount = getBalance(mPoolDenom);
            mTotalValue.setText(WDp.dpUserCurrencyValue(getBaseDao(), currency, mPoolDenom, mTotalAmount, mDivideDecimal, priceProvider));

            mBtnIbcSend.setVisibility(View.VISIBLE);

        } else if (getBaseChain().equals(INJ_MAIN.INSTANCE)) {
            mToolbarSymbolImg.setImageResource(R.drawable.token_ic);
            mToolbarSymbol.setText("SHARE" + mPoolDenom.substring(5));
            mToolbarSymbol.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));

            mDivideDecimal = 18;
            mDisplayDecimal = 18;
            mTotalAmount = getBalance(mPoolDenom);
            mTotalValue.setText(WDp.dpUserCurrencyValue(getBaseDao(), currency, mPoolDenom, mTotalAmount, mDivideDecimal, priceProvider));

            mBtnIbcSend.setVisibility(View.VISIBLE);
        }

        mItemPerPrice.setText(WDp.dpPerUserCurrencyValue(getBaseDao(), currency, mPoolDenom, priceProvider));
        final Price price = getPrice(mPoolDenom);
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

        mBtnAddressPopup.setCardBackgroundColor(WDp.getChainBgColor(POOLTokenDetailActivity.this, getBaseChain()));
        mAddress.setText(getAccount().address);
        mKeyState.setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.colorGray0), android.graphics.PorterDuff.Mode.SRC_IN);
        if (getAccount().hasPrivateKey) {
            mKeyState.setColorFilter(WDp.getChainColor(getBaseContext(), getBaseChain()), android.graphics.PorterDuff.Mode.SRC_IN);
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

        } else if (v.equals(mBtnSend)) {
            Toast.makeText(POOLTokenDetailActivity.this, getString(R.string.error_prepare), Toast.LENGTH_SHORT).show();
            return;
//            Intent intent = new Intent(getBaseContext(), SendActivity.class);
//            BigDecimal mainAvailable = getBalance(mBaseChain.getMainDenom());
//            BigDecimal feeAmount = WUtil.getEstimateGasFeeAmount(mBaseChain, CONST_PW_TX_SIMPLE_SEND, 0);
//            if (mainAvailable.compareTo(feeAmount) < 0) {
//                Toast.makeText(getBaseContext(), R.string.error_not_enough_fee, Toast.LENGTH_SHORT).show();
//                return;
//            }
//            intent.putExtra("sendTokenDenom", mPoolDenom);
//            startActivity(intent);
        } else if (v.equals(mBtnIbcSend)) {
            Toast.makeText(POOLTokenDetailActivity.this, getString(R.string.error_prepare), Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private class POOlTokenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int TYPE_UNKNOWN = -1;
        private static final int TYPE_POOL_TOKEN = 0;

        private static final int TYPE_HISTORY = 100;

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            if (viewType == TYPE_UNKNOWN) {

            } else if (viewType == TYPE_POOL_TOKEN) {
                return new TokenDetailSupportHolder(getLayoutInflater().inflate(R.layout.item_amount_detail, viewGroup, false));
            }

//            } else if (viewType == TYPE_HISTORY) {
//                return new HistoryHolder(getLayoutInflater().inflate(R.layout.item_history, viewGroup, false));
//            }
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            if (getItemViewType(position) == TYPE_POOL_TOKEN) {
                TokenDetailSupportHolder holder = (TokenDetailSupportHolder) viewHolder;
                holder.onBindPoolToken(POOLTokenDetailActivity.this, getBaseChain(), mPoolDenom);
            }
//
//            } else if (getItemViewType(position) == TYPE_HISTORY) {
//
//            } else if (getItemViewType(position) == TYPE_UNKNOWN) {
        }

        @Override
        public int getItemCount() {
            return 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) return TYPE_POOL_TOKEN;
            else return TYPE_HISTORY;
        }
    }
}
