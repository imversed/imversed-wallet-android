package wannabit.io.cosmostaion.widget.tokenDetail;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.fulldive.wallet.models.BaseChain;
import com.fulldive.wallet.models.WalletBalance;
import com.fulldive.wallet.models.Token;

import java.math.BigDecimal;

import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.base.BaseActivity;
import wannabit.io.cosmostaion.base.BaseData;
import wannabit.io.cosmostaion.utils.WDp;
import wannabit.io.cosmostaion.widget.BaseHolder;

public class TokenStakingNewHolder extends BaseHolder {
    private final CardView mCardRoot;
    private final RelativeLayout mVestingLayer;

    private final TextView mTotalAmount;
    private final TextView mAvailableAmount;
    private final TextView mVestingAmount;
    private final TextView mDelegatedAmount;
    private final TextView mUnbondingAmount;
    private final TextView mRewardAmount;

    public TokenStakingNewHolder(@NonNull View itemView) {
        super(itemView);
        mCardRoot = itemView.findViewById(R.id.cardView);
        mTotalAmount = itemView.findViewById(R.id.total_amount);
        mAvailableAmount = itemView.findViewById(R.id.available_amount);
        mVestingAmount = itemView.findViewById(R.id.vesting_amount);
        mDelegatedAmount = itemView.findViewById(R.id.delegated_amount);
        mUnbondingAmount = itemView.findViewById(R.id.unbonding_amount);
        mRewardAmount = itemView.findViewById(R.id.reward_amount);

        mVestingLayer = itemView.findViewById(R.id.vesting_layer);
    }

    @Override
    public void onBindTokenHolder(BaseActivity baseActivity, BaseChain chain, BaseData baseData, String denom) {
        final Token mainToken = chain.getMainToken();
        final int stakingDivideDecimal = mainToken.getDivideDecimal();
        final int stakingDisplayDecimal = mainToken.getDisplayDecimal();
        final WalletBalance balance = baseActivity.getFullBalance(denom);
        final BigDecimal totalToken = balance.getBalanceAmount().add(baseData.getAllMainAsset(denom));  //TODO: add vesting
        mTotalAmount.setText(WDp.getDpAmount2(totalToken, stakingDivideDecimal, stakingDisplayDecimal));
        mAvailableAmount.setText(WDp.getDpAmount2(balance.getBalanceAmount(), stakingDivideDecimal, stakingDisplayDecimal));
        mDelegatedAmount.setText(WDp.getDpAmount2(baseData.getDelegationSum(), stakingDivideDecimal, stakingDisplayDecimal));
        mUnbondingAmount.setText(WDp.getDpAmount2(baseData.getUndelegationSum(), stakingDivideDecimal, stakingDisplayDecimal));
        mRewardAmount.setText(WDp.getDpAmount2(baseData.getRewardSum(denom), stakingDivideDecimal, stakingDisplayDecimal));

// TODO: Vesting
//        final BigDecimal vestingAmount = baseData.getVesting(denom);
//        if (vestingAmount.compareTo(BigDecimal.ZERO) > 0) {
//            mVestingLayer.setVisibility(View.VISIBLE);
//            mVestingAmount.setText(WDp.getDpAmount2(vestingAmount, stakingDivideDecimal, stakingDisplayDecimal));
//        }
        mCardRoot.setCardBackgroundColor(WDp.getChainBgColor(baseActivity, chain));
    }
}
