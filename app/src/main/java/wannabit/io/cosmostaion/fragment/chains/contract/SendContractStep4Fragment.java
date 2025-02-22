package wannabit.io.cosmostaion.fragment.chains.contract;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.math.BigDecimal;

import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.activities.chains.contract.SendContractActivity;
import wannabit.io.cosmostaion.base.BaseFragment;
import wannabit.io.cosmostaion.base.IRefreshTabListener;
import wannabit.io.cosmostaion.dao.Cw20Assets;
import wannabit.io.cosmostaion.utils.WDp;

public class SendContractStep4Fragment extends BaseFragment implements View.OnClickListener, IRefreshTabListener {

    private TextView mFeeAmount, mFeeDenom;
    private TextView mSendAmount, mSendDenom;
    private TextView mRecipientAddress, mMemo;
    private Button mBeforeBtn, mConfirmBtn;

    private Cw20Assets mCw20Assets;
    private int mDpdecimal = 6;
    private int mCw20Decimal = 6;

    public static SendContractStep4Fragment newInstance(Bundle bundle) {
        SendContractStep4Fragment fragment = new SendContractStep4Fragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_send_contract_step4, container, false);
        mFeeAmount = rootView.findViewById(R.id.send_fees);
        mFeeDenom = rootView.findViewById(R.id.send_fees_type);
        mSendAmount = rootView.findViewById(R.id.send_amount);
        mSendDenom = rootView.findViewById(R.id.send_amount_title);
        mRecipientAddress = rootView.findViewById(R.id.recipient_address);
        mMemo = rootView.findViewById(R.id.memo);
        mBeforeBtn = rootView.findViewById(R.id.btn_before);
        mConfirmBtn = rootView.findViewById(R.id.confirmButton);

        WDp.DpMainDenom(getSActivity().getAccount().baseChain, mFeeDenom);

        mBeforeBtn.setOnClickListener(this);
        mConfirmBtn.setOnClickListener(this);
        return rootView;
    }


    @Override
    public void onRefreshTab() {
        BigDecimal toSendAmount = new BigDecimal(getSActivity().mAmounts.get(0).amount);
        BigDecimal feeAmount = new BigDecimal(getSActivity().mTxFee.amount.get(0).amount);

        mDpdecimal = getSActivity().getBaseChain().getDivideDecimal();
        mFeeAmount.setText(WDp.getDpAmount2(feeAmount, mDpdecimal, mDpdecimal));

        mCw20Assets = getBaseDao().getCw20_gRPC(getSActivity().mContractAddress);
        if (mCw20Assets != null) {
            mCw20Decimal = mCw20Assets.decimal;
            mSendAmount.setText(WDp.getDpAmount2(toSendAmount, mCw20Decimal, mCw20Decimal));
            mSendDenom.setText(mCw20Assets.denom.toUpperCase());
        }

        mRecipientAddress.setText(getSActivity().mToAddress);
        mMemo.setText(getSActivity().mTxMemo);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mBeforeBtn)) {
            getSActivity().onBeforeStep();
        } else if (v.equals(mConfirmBtn)) {
            getSActivity().onStartSendContract();
        }
    }

    private SendContractActivity getSActivity() {
        return (SendContractActivity) getBaseActivity();
    }
}
