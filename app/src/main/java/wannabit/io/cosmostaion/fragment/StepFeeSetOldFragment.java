package wannabit.io.cosmostaion.fragment;

import static com.fulldive.wallet.models.BaseChain.BNB_MAIN;
import static com.fulldive.wallet.models.BaseChain.OKEX_MAIN;
import static wannabit.io.cosmostaion.base.BaseConstant.FEE_BNB_SEND;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.addisonelliott.segmentedbutton.SegmentedButtonGroup;
import com.fulldive.wallet.interactors.settings.SettingsInteractor;
import com.fulldive.wallet.models.BaseChain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.base.BaseBroadCastActivity;
import wannabit.io.cosmostaion.base.BaseFragment;
import wannabit.io.cosmostaion.base.IRefreshTabListener;
import wannabit.io.cosmostaion.model.type.Coin;
import wannabit.io.cosmostaion.model.type.Fee;
import wannabit.io.cosmostaion.utils.WDp;
import wannabit.io.cosmostaion.utils.WUtil;

public class StepFeeSetOldFragment extends BaseFragment implements View.OnClickListener, IRefreshTabListener {

    private CardView mFeeTotalCard;
    private TextView mFeeDenom, mFeeAmount, mFeeValue;

    private TextView mGasAmount, mGasRate, mGasFee;
    private SegmentedButtonGroup mButtonGroup;

    private LinearLayout mSpeedLayer;
    private ImageView mSpeedImg;
    private TextView mSpeedTxt;

    private LinearLayout mBottomControlCard;
    private Button mBtnBefore, mBtnNext;

    private int mSelectedGasPosition = 1;
    private BigDecimal mSelectedGasRate = BigDecimal.ZERO;
    private BigDecimal mEstimateGasAmount = BigDecimal.ZERO;
    private BigDecimal mFee = BigDecimal.ZERO;
    private SettingsInteractor settingsInteractor;

    public static StepFeeSetOldFragment newInstance(Bundle bundle) {
        StepFeeSetOldFragment fragment = new StepFeeSetOldFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_tx_step_fee_old, container, false);
        mFeeTotalCard = rootView.findViewById(R.id.card_fee_total);
        mFeeDenom = rootView.findViewById(R.id.fee_denom);
        mFeeAmount = rootView.findViewById(R.id.fee_amount);
        mFeeValue = rootView.findViewById(R.id.fee_value);

        mGasAmount = rootView.findViewById(R.id.gas_amount);
        mGasRate = rootView.findViewById(R.id.gas_rate);
        mGasFee = rootView.findViewById(R.id.gas_fee);
        mButtonGroup = rootView.findViewById(R.id.btns_segmented);

        mSpeedLayer = rootView.findViewById(R.id.speed_layer);
        mSpeedImg = rootView.findViewById(R.id.speed_img);
        mSpeedTxt = rootView.findViewById(R.id.speed_txt);

        mBottomControlCard = rootView.findViewById(R.id.bottom_control_layer);
        mBtnBefore = rootView.findViewById(R.id.btn_before);
        mBtnNext = rootView.findViewById(R.id.nextButton);

        final BaseChain baseChain = getSActivity().getBaseChain();

        WDp.DpMainDenom(baseChain, mFeeDenom);
        mFeeTotalCard.setCardBackgroundColor(WDp.getChainBgColor(getContext(), baseChain));
        mButtonGroup.setSelectedBackground(WDp.getChainColor(getContext(), baseChain));
        mButtonGroup.setRipple(WDp.getChainColor(getContext(), baseChain));

        if (baseChain.equals(OKEX_MAIN.INSTANCE)) {
            int myValidatorCnt = 0;
            if (getBaseDao().mOkStaking != null && getBaseDao().mOkStaking.validator_address != null) {
                myValidatorCnt = getBaseDao().mOkStaking.validator_address.size();
            }
            mEstimateGasAmount = WUtil.getEstimateGasAmount(getContext(), baseChain, getSActivity().mTxType, myValidatorCnt);
        } else {
            mEstimateGasAmount = WUtil.getEstimateGasAmount(getContext(), baseChain, getSActivity().mTxType, (getSActivity().mValidators.size()));
        }
        onUpdateView();

        mButtonGroup.setOnPositionChangedListener(position -> {
            mSelectedGasPosition = position;
            onUpdateView();
        });
        mBtnBefore.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onRefreshTab() {
        mFeeTotalCard.setVisibility(View.VISIBLE);
        mSpeedLayer.setVisibility(View.VISIBLE);
        mBottomControlCard.setVisibility(View.VISIBLE);
    }

    private void onCalculateFees() {
        final BaseChain baseChain = getSActivity().getBaseChain();
        mSelectedGasRate = WUtil.getGasRate(baseChain, mSelectedGasPosition);
        if (baseChain.equals(BNB_MAIN.INSTANCE)) {
            mFee = new BigDecimal(FEE_BNB_SEND);
        } else if (baseChain.equals(OKEX_MAIN.INSTANCE)) {
            mFee = mSelectedGasRate.multiply(mEstimateGasAmount).setScale(18, RoundingMode.UP);
        }
    }

    private void onUpdateView() {
        onCalculateFees();
        final BaseChain baseChain = getSActivity().getBaseChain();

        mFeeAmount.setText(WDp.getDpAmount2(mFee, baseChain.getDivideDecimal(), baseChain.getDisplayDecimal()));
        mFeeValue.setText(WDp.dpUserCurrencyValue(getBaseDao(), settingsInteractor.getCurrency(), baseChain.getMainDenom(), mFee, baseChain.getDivideDecimal()));

        mGasRate.setText(WDp.getDpGasRate(mSelectedGasRate.toPlainString()));
        mGasAmount.setText(mEstimateGasAmount.toPlainString());
        mGasFee.setText(mFee.toPlainString());

        mSpeedImg.setImageDrawable(getResources().getDrawable(R.drawable.rocket_img));
        mSpeedTxt.setText(getString(R.string.str_fee_speed_title_2));
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mBtnBefore)) {
            getSActivity().onBeforeStep();

        } else if (v.equals(mBtnNext)) {
            onSetFee();
            getSActivity().onNextStep();
        }
    }

    private void onSetFee() {
        Fee fee = new Fee();
        Coin gasCoin = new Coin();
        gasCoin.denom = getSActivity().getBaseChain().getMainDenom();
        gasCoin.amount = mFee.toPlainString();
        ArrayList<Coin> amount = new ArrayList<>();
        amount.add(gasCoin);
        fee.amount = amount;
        fee.gas = mEstimateGasAmount.toPlainString();
        getSActivity().mTxFee = fee;

    }

    private BaseBroadCastActivity getSActivity() {
        return (BaseBroadCastActivity) getBaseActivity();
    }
}
