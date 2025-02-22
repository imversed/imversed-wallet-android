package wannabit.io.cosmostaion.fragment.chains.cosmos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.math.BigDecimal;

import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.activities.chains.cosmos.GravitySwapActivity;
import wannabit.io.cosmostaion.base.BaseFragment;
import wannabit.io.cosmostaion.base.IRefreshTabListener;
import wannabit.io.cosmostaion.utils.WDp;
import wannabit.io.cosmostaion.utils.WUtil;

public class GDexSwapStep3Fragment extends BaseFragment implements View.OnClickListener, IRefreshTabListener {

    private TextView mFeeAmount;
    private TextView mFeeAmountSymbol;
    private TextView mSwapFee;
    private TextView mSwapInAmount, mSwapInAmountSymbol;
    private TextView mSwapOutAmount, mSwapOutAmountSymbol;
    private RelativeLayout mSlippageLayer;
    private TextView mMemo;
    private int mDpDecimal = 6, mInputCoinDecimal = 6, mOutputCoinDecimal = 6;

    private Button mBeforeBtn, mConfirmBtn;

    public static GDexSwapStep3Fragment newInstance(Bundle bundle) {
        GDexSwapStep3Fragment fragment = new GDexSwapStep3Fragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_swap_step3, container, false);
        mFeeAmount = rootView.findViewById(R.id.swap_fee_amount);
        mFeeAmountSymbol = rootView.findViewById(R.id.swap_fee_amount_symbol);
        mSwapFee = rootView.findViewById(R.id.swap_fee);
        mSwapInAmount = rootView.findViewById(R.id.swap_in_amount);
        mSwapInAmountSymbol = rootView.findViewById(R.id.swap_in_amount_symbol);
        mSwapOutAmount = rootView.findViewById(R.id.swap_out_amount);
        mSwapOutAmountSymbol = rootView.findViewById(R.id.swap_out_amount_symbol);
        mSlippageLayer = rootView.findViewById(R.id.slippage_layer);
        mMemo = rootView.findViewById(R.id.memo);
        mBeforeBtn = rootView.findViewById(R.id.btn_before);
        mConfirmBtn = rootView.findViewById(R.id.confirmButton);

        WDp.DpMainDenom(getSActivity().getAccount().baseChain, mFeeAmountSymbol);

        mBeforeBtn.setOnClickListener(this);
        mConfirmBtn.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onRefreshTab() {
        mSlippageLayer.setVisibility(View.GONE);
        mDpDecimal = getSActivity().getBaseChain().getDivideDecimal();
        mInputCoinDecimal = WUtil.getCosmosCoinDecimal(getBaseDao(), getSActivity().mInputDenom);
        mOutputCoinDecimal = WUtil.getCosmosCoinDecimal(getBaseDao(), getSActivity().mOutputDenom);
        BigDecimal feeAmount = new BigDecimal(getSActivity().mTxFee.amount.get(0).amount);

        mFeeAmount.setText(WDp.getDpAmount2(feeAmount, mDpDecimal, mDpDecimal));
        mSwapFee.setText(WDp.getPercentDp(new BigDecimal(getBaseDao().mParams.getSwapFeeRate()).movePointLeft(16)));
        WDp.showCoinDp(getBaseDao(), getSActivity().mSwapInCoin, mSwapInAmountSymbol, mSwapInAmount, getSActivity().getBaseChain());
        WDp.showCoinDp(getBaseDao(), getSActivity().mSwapOutCoin, mSwapOutAmountSymbol, mSwapOutAmount, getSActivity().getBaseChain());

        mMemo.setText(getSActivity().mTxMemo);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mBeforeBtn)) {
            getSActivity().onBeforeStep();

        } else if (v.equals(mConfirmBtn)) {
            getSActivity().onStartSwap();
        }
    }

    private GravitySwapActivity getSActivity() {
        return (GravitySwapActivity) getBaseActivity();
    }
}
