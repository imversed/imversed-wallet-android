package wannabit.io.cosmostaion.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.fulldive.wallet.models.BaseChain;

import java.math.BigDecimal;

import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.activities.HtlcRefundActivity;
import wannabit.io.cosmostaion.base.BaseFragment;
import wannabit.io.cosmostaion.base.IRefreshTabListener;
import wannabit.io.cosmostaion.model.type.Coin;
import wannabit.io.cosmostaion.utils.WDp;

public class HtlcRefundStep3Fragment extends BaseFragment implements View.OnClickListener, IRefreshTabListener {

    private Button mBtnBack, mBtnConfirm;
    private TextView mFeeAmount, mFeeDenom;
    private TextView mSwapId, mRefundAddress, mRefundAmount, mRefundAmountDenom;

    public static HtlcRefundStep3Fragment newInstance(Bundle bundle) {
        HtlcRefundStep3Fragment fragment = new HtlcRefundStep3Fragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_htlc_refund_3, container, false);
        mBtnBack = rootView.findViewById(R.id.btn_before);
        mBtnConfirm = rootView.findViewById(R.id.confirmButton);
        mFeeAmount = rootView.findViewById(R.id.fee_amount);
        mFeeDenom = rootView.findViewById(R.id.fee_denom);
        mSwapId = rootView.findViewById(R.id.refund_swap_id);
        mRefundAddress = rootView.findViewById(R.id.refund_address);
        mRefundAmount = rootView.findViewById(R.id.refund_amount);
        mRefundAmountDenom = rootView.findViewById(R.id.refund_amount_denom);
        mBtnBack.setOnClickListener(this);
        mBtnConfirm.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onRefreshTab() {
        BigDecimal feeAmount = new BigDecimal(getSActivity().mTxFee.amount.get(0).amount);
        WDp.DpMainDenom(getSActivity().getBaseChain().getChainName(), mFeeDenom);
        if (getSActivity().getBaseChain().equals(BaseChain.BNB_MAIN.INSTANCE)) {
            mFeeAmount.setText(WDp.getDpAmount2(feeAmount, 0, 8));
            mSwapId.setText(getSActivity().mSwapId);
            mRefundAddress.setText(getSActivity().mResBnbSwapInfo.fromAddr);
            Coin coin = getSActivity().mResBnbSwapInfo.getSendCoin();
            WDp.showCoinDp(getBaseDao(), coin, mRefundAmountDenom, mRefundAmount, getSActivity().getBaseChain());

        } else if (getSActivity().getBaseChain().equals(BaseChain.KAVA_MAIN.INSTANCE)) {
            mFeeAmount.setText(WDp.getDpAmount2(feeAmount, 6, 6));
            mSwapId.setText(getSActivity().mSwapId);
            mRefundAddress.setText(getSActivity().mResKavaSwapInfo.result.sender);
            Coin coin = getSActivity().mResKavaSwapInfo.result.amount.get(0);
            WDp.showCoinDp(getBaseDao(), coin, mRefundAmountDenom, mRefundAmount, getSActivity().getBaseChain());
        }


    }

    @Override
    public void onClick(View v) {
        if (v.equals(mBtnBack)) {
            getSActivity().onBeforeStep();
        } else if (v.equals(mBtnConfirm)) {
            getSActivity().onStartHtlcRefund();
        }
    }


    private HtlcRefundActivity getSActivity() {
        return (HtlcRefundActivity) getBaseActivity();
    }
}
