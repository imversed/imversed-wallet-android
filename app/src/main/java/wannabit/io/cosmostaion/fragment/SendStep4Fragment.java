package wannabit.io.cosmostaion.fragment;

import static com.fulldive.wallet.models.BaseChain.BNB_MAIN;
import static com.fulldive.wallet.models.BaseChain.OKEX_MAIN;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.fulldive.wallet.interactors.settings.SettingsInteractor;
import com.fulldive.wallet.models.BaseChain;
import com.fulldive.wallet.models.Currency;

import java.math.BigDecimal;

import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.activities.SendActivity;
import wannabit.io.cosmostaion.base.BaseFragment;
import wannabit.io.cosmostaion.base.IRefreshTabListener;
import wannabit.io.cosmostaion.utils.PriceProvider;
import wannabit.io.cosmostaion.utils.WDp;

public class SendStep4Fragment extends BaseFragment implements View.OnClickListener, IRefreshTabListener {

    private TextView mSendAmount;
    private TextView mFeeAmount;
    private RelativeLayout mTotalSpendLayer;
    private TextView mTotalSpendAmount, mTotalPrice;
    private TextView mCurrentBalance, mRemainingBalance, mRemainingPrice;
    private TextView mRecipientAddress, mRecipientStartName, mMemo;
    private Button mBeforeBtn, mConfirmBtn;
    private TextView mDenomSendAmount, mDenomFeeType, mDenomTotalSpend, mDenomCurrentAmount, mDenomRemainAmount;
    private int mDisplayDecimal = 6;        //bnb == 8
    private int mDivideDecimal = 6;         //bnb == 0
    private SettingsInteractor settingsInteractor;

    public static SendStep4Fragment newInstance(Bundle bundle) {
        SendStep4Fragment fragment = new SendStep4Fragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsInteractor = getAppInjector().getInstance(SettingsInteractor.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_send_step4, container, false);
        mSendAmount = rootView.findViewById(R.id.send_amount);
        mFeeAmount = rootView.findViewById(R.id.send_fees);
        mTotalSpendLayer = rootView.findViewById(R.id.spend_total_layer);
        mTotalSpendAmount = rootView.findViewById(R.id.spend_total);
        mTotalPrice = rootView.findViewById(R.id.spend_total_price);
        mCurrentBalance = rootView.findViewById(R.id.current_available_atom);
        mRemainingBalance = rootView.findViewById(R.id.remaining_available_atom);
        mRemainingPrice = rootView.findViewById(R.id.remaining_price);
        mRecipientAddress = rootView.findViewById(R.id.recipient_address);
        mRecipientStartName = rootView.findViewById(R.id.recipient_starname);
        mMemo = rootView.findViewById(R.id.memo);
        mBeforeBtn = rootView.findViewById(R.id.btn_before);
        mConfirmBtn = rootView.findViewById(R.id.confirmButton);
        mDenomSendAmount = rootView.findViewById(R.id.send_amount_title);
        mDenomFeeType = rootView.findViewById(R.id.send_fees_type);
        mDenomTotalSpend = rootView.findViewById(R.id.spend_total_type);
        mDenomCurrentAmount = rootView.findViewById(R.id.current_available_title);
        mDenomRemainAmount = rootView.findViewById(R.id.remaining_available_title);

        String baseChain = getSActivity().getAccount().baseChain;

        WDp.DpMainDenom(baseChain, mDenomFeeType);
        WDp.DpMainDenom(baseChain, mDenomTotalSpend);
        WDp.DpMainDenom(baseChain, mDenomSendAmount);
        WDp.DpMainDenom(baseChain, mDenomCurrentAmount);
        WDp.DpMainDenom(baseChain, mDenomRemainAmount);

        mBeforeBtn.setOnClickListener(this);
        mConfirmBtn.setOnClickListener(this);
        return rootView;
    }


    @Override
    public void onRefreshTab() {
        final Currency currency = settingsInteractor.getCurrency();
        final BaseChain baseChain = getSActivity().getBaseChain();
        BigDecimal toSendAmount = new BigDecimal(getSActivity().mAmounts.get(0).amount);
        BigDecimal feeAmount = new BigDecimal(getSActivity().mTxFee.amount.get(0).amount);
        final String mainDenom = baseChain.getMainDenom();
        final String toSendDenom = getSActivity().mDenom;
        final PriceProvider priceProvider = getSActivity()::getPrice;

        mDivideDecimal = baseChain.getDivideDecimal();
        mDisplayDecimal = baseChain.getDisplayDecimal();

        if (baseChain.isGRPC()) {
            mFeeAmount.setText(WDp.getDpAmount2(feeAmount, mDivideDecimal, mDisplayDecimal));

            if (toSendDenom.equals(mainDenom)) {
                mSendAmount.setText(WDp.getDpAmount2(toSendAmount, mDivideDecimal, mDisplayDecimal));
                mTotalSpendAmount.setText(WDp.getDpAmount2(feeAmount.add(toSendAmount), mDivideDecimal, mDisplayDecimal));
                mTotalPrice.setText(WDp.dpUserCurrencyValue(getBaseDao(), currency, mainDenom, feeAmount.add(toSendAmount), mDivideDecimal, priceProvider));

                BigDecimal currentAvai = getSActivity().getBalance(toSendDenom);
                mCurrentBalance.setText(WDp.getDpAmount2(currentAvai, mDivideDecimal, mDisplayDecimal));
                BigDecimal subtract = currentAvai.subtract(toSendAmount).subtract(feeAmount);
                mRemainingBalance.setText(WDp.getDpAmount2(subtract, mDivideDecimal, mDisplayDecimal));
                mRemainingPrice.setText(WDp.dpUserCurrencyValue(getBaseDao(), currency, mainDenom, subtract, mDivideDecimal, priceProvider));

            } else {
                // not staking denom send
                int textColor = ContextCompat.getColor(requireContext(), R.color.colorWhite);
                mDenomSendAmount.setTextColor(textColor);
                mDenomCurrentAmount.setTextColor(textColor);
                mDenomRemainAmount.setTextColor(textColor);
                mTotalSpendLayer.setVisibility(View.GONE);
                mTotalPrice.setVisibility(View.GONE);
                mRemainingPrice.setVisibility(View.GONE);

                BigDecimal currentAvai = getSActivity().getBalance(toSendDenom);
                WDp.showCoinDp(getContext(), getBaseDao(), toSendDenom, toSendAmount.toPlainString(), mDenomSendAmount, mSendAmount, baseChain);
                WDp.showCoinDp(getContext(), getBaseDao(), toSendDenom, currentAvai.toPlainString(), mDenomCurrentAmount, mCurrentBalance, baseChain);
                WDp.showCoinDp(getContext(), getBaseDao(), toSendDenom, currentAvai.subtract(toSendAmount).toPlainString(), mDenomRemainAmount, mRemainingBalance, baseChain);
            }

        } else if (baseChain.equals(BNB_MAIN.INSTANCE)) {
            mDenomSendAmount.setText(getSActivity().mBnbToken.original_symbol.toUpperCase());
            mDenomCurrentAmount.setText(getSActivity().mBnbToken.original_symbol.toUpperCase());
            mDenomRemainAmount.setText(getSActivity().mBnbToken.original_symbol.toUpperCase());

            mSendAmount.setText(WDp.getDpAmount2(toSendAmount, mDivideDecimal, mDisplayDecimal));
            mFeeAmount.setText(WDp.getDpAmount2(feeAmount, mDivideDecimal, mDisplayDecimal));

            final String mainDenomBNB = BNB_MAIN.INSTANCE.getMainDenom();
            if (getSActivity().mBnbToken.symbol.equals(mainDenomBNB)) {
                int textColor = ContextCompat.getColor(requireContext(), R.color.colorBnb);
                mDenomSendAmount.setTextColor(textColor);
                mDenomCurrentAmount.setTextColor(textColor);
                mDenomRemainAmount.setTextColor(textColor);

                mTotalSpendAmount.setText(WDp.getDpAmount2(feeAmount.add(toSendAmount), mDivideDecimal, mDisplayDecimal));
                mTotalPrice.setText(WDp.dpUserCurrencyValue(getBaseDao(), currency, mainDenomBNB, feeAmount.add(toSendAmount), 0, priceProvider));

                BigDecimal currentAvai = getSActivity().getAccount().getTokenBalance(mainDenomBNB);
                mCurrentBalance.setText(WDp.getDpAmount2(currentAvai, mDivideDecimal, mDisplayDecimal));
                BigDecimal subtract = currentAvai.subtract(toSendAmount).subtract(feeAmount);
                mRemainingBalance.setText(WDp.getDpAmount2(subtract, mDivideDecimal, mDisplayDecimal));
                mRemainingPrice.setText(WDp.dpUserCurrencyValue(getBaseDao(), currency, mainDenomBNB, subtract, 0, priceProvider));
            } else {
                int textColor = ContextCompat.getColor(requireContext(), R.color.colorWhite);
                mDenomSendAmount.setTextColor(textColor);
                mDenomCurrentAmount.setTextColor(textColor);
                mDenomRemainAmount.setTextColor(textColor);
                mTotalSpendLayer.setVisibility(View.GONE);
                mTotalPrice.setVisibility(View.GONE);
                mRemainingPrice.setVisibility(View.GONE);

                BigDecimal currentAvai = getSActivity().getAccount().getTokenBalance(getSActivity().mBnbToken.symbol);
                mCurrentBalance.setText(WDp.getDpAmount2(currentAvai, mDivideDecimal, mDisplayDecimal));
                mRemainingBalance.setText(WDp.getDpAmount2(currentAvai.subtract(toSendAmount), mDivideDecimal, mDisplayDecimal));
            }


        } else if (baseChain.equals(OKEX_MAIN.INSTANCE)) {

            mSendAmount.setText(WDp.getDpAmount2(toSendAmount, mDivideDecimal, mDisplayDecimal));
            mFeeAmount.setText(WDp.getDpAmount2(feeAmount, mDivideDecimal, mDisplayDecimal));

            mDenomSendAmount.setText(toSendDenom.toUpperCase());
            mDenomCurrentAmount.setText(toSendDenom.toUpperCase());
            mDenomRemainAmount.setText(toSendDenom.toUpperCase());

            if (toSendDenom.equals(mainDenom)) {
                int textColor = ContextCompat.getColor(requireContext(), R.color.colorOK);
                mDenomSendAmount.setTextColor(textColor);
                mDenomCurrentAmount.setTextColor(textColor);
                mDenomRemainAmount.setTextColor(textColor);
                mTotalSpendAmount.setText(WDp.getDpAmount2(feeAmount.add(toSendAmount), mDivideDecimal, mDisplayDecimal));
                mTotalPrice.setText(WDp.dpUserCurrencyValue(getBaseDao(), currency, OKEX_MAIN.INSTANCE.getMainDenom(), feeAmount.add(toSendAmount), mDivideDecimal, priceProvider));

                BigDecimal currentAvai = getSActivity().getBalance(toSendDenom);
                mCurrentBalance.setText(WDp.getDpAmount2(currentAvai, mDivideDecimal, mDisplayDecimal));
                BigDecimal subtract = currentAvai.subtract(toSendAmount).subtract(feeAmount);
                mRemainingBalance.setText(WDp.getDpAmount2(subtract, mDivideDecimal, mDisplayDecimal));
                mRemainingPrice.setText(WDp.dpUserCurrencyValue(getBaseDao(), currency, OKEX_MAIN.INSTANCE.getMainDenom(), subtract, mDivideDecimal, priceProvider));

            } else {
                int textColor = ContextCompat.getColor(requireContext(), R.color.colorWhite);
                mDenomSendAmount.setTextColor(textColor);
                mDenomCurrentAmount.setTextColor(textColor);
                mDenomRemainAmount.setTextColor(textColor);
                mTotalSpendLayer.setVisibility(View.GONE);
                mTotalPrice.setVisibility(View.GONE);
                mRemainingPrice.setVisibility(View.GONE);

                BigDecimal currentAvai = getSActivity().getBalance(toSendDenom);
                mCurrentBalance.setText(WDp.getDpAmount2(currentAvai, mDivideDecimal, mDisplayDecimal));
                mRemainingBalance.setText(WDp.getDpAmount2(currentAvai.subtract(toSendAmount), mDivideDecimal, mDisplayDecimal));

            }

        } else {
            mSendAmount.setText(WDp.getDpAmount2(toSendAmount, mDivideDecimal, mDisplayDecimal));
            mFeeAmount.setText(WDp.getDpAmount2(feeAmount, mDivideDecimal, mDisplayDecimal));

            mSendAmount.setText(WDp.getDpAmount2(toSendAmount, mDivideDecimal, mDisplayDecimal));
            mFeeAmount.setText(WDp.getDpAmount2(feeAmount, mDivideDecimal, mDisplayDecimal));
            mTotalSpendAmount.setText(WDp.getDpAmount2(feeAmount.add(toSendAmount), mDivideDecimal, mDisplayDecimal));
            mTotalPrice.setText(WDp.dpUserCurrencyValue(getBaseDao(), currency, toSendDenom, feeAmount.add(toSendAmount), mDivideDecimal, priceProvider));

            BigDecimal currentAvai = getSActivity().getBalance(toSendDenom);
            BigDecimal subtract = currentAvai.subtract(toSendAmount).subtract(feeAmount);

            mCurrentBalance.setText(WDp.getDpAmount2(currentAvai, mDivideDecimal, mDisplayDecimal));
            mRemainingBalance.setText(WDp.getDpAmount2(subtract, mDivideDecimal, mDisplayDecimal));
            mRemainingPrice.setText(WDp.dpUserCurrencyValue(getBaseDao(), currency, toSendDenom, subtract, mDivideDecimal, priceProvider));
        }

        mRecipientAddress.setText(getSActivity().mToAddress);
        mRecipientStartName.setVisibility(View.GONE);
        mMemo.setText(getSActivity().mTxMemo);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mBeforeBtn)) {
            getSActivity().onBeforeStep();
        } else if (v.equals(mConfirmBtn)) {
            getSActivity().onStartSend();
        }
    }

    private SendActivity getSActivity() {
        return (SendActivity) getBaseActivity();
    }
}
