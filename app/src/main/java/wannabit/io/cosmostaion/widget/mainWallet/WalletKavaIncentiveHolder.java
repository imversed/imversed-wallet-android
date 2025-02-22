package wannabit.io.cosmostaion.widget.mainWallet;

import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_HARD;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_SWP;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fulldive.wallet.models.BaseChain;
import com.fulldive.wallet.presentation.main.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.base.BaseData;
import wannabit.io.cosmostaion.utils.WDp;
import wannabit.io.cosmostaion.widget.BaseHolder;

public class WalletKavaIncentiveHolder extends BaseHolder {

    private final TextView mKavaIncetive;
    private final TextView mHardIncetive;
    private final TextView mSwpIncetive;
    private final RelativeLayout mBtnIncentive;

    public WalletKavaIncentiveHolder(@NonNull View itemView) {
        super(itemView);
        mKavaIncetive = itemView.findViewById(R.id.kava_incentive);
        mHardIncetive = itemView.findViewById(R.id.hard_incentive);
        mSwpIncetive = itemView.findViewById(R.id.swp_incentive);
        mBtnIncentive = itemView.findViewById(R.id.btn_kava_incentive);
    }

    public void onBindHolder(@NotNull MainActivity mainActivity) {
        final BaseData baseData = mainActivity.getBaseDao();
        BigDecimal mKavaIncetiveAmount = BigDecimal.ZERO;
        BigDecimal mHardIncetiveAmount = BigDecimal.ZERO;
        BigDecimal mSwpIncetiveAmount = BigDecimal.ZERO;
        final String kavaDenom = BaseChain.KAVA_MAIN.INSTANCE.getMainDenom();

        if (baseData.mIncentiveRewards != null) {
            mKavaIncetiveAmount = baseData.mIncentiveRewards.getRewardSum(kavaDenom);
            mHardIncetiveAmount = baseData.mIncentiveRewards.getRewardSum(TOKEN_HARD);
            mSwpIncetiveAmount = baseData.mIncentiveRewards.getRewardSum(TOKEN_SWP);
        }

        mKavaIncetive.setText(WDp.getDpAmount2(mKavaIncetiveAmount, 6, 6));
        mHardIncetive.setText(WDp.getDpAmount2(mHardIncetiveAmount, 6, 6));
        mSwpIncetive.setText(WDp.getDpAmount2(mSwpIncetiveAmount, 6, 6));

        mBtnIncentive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onClickIncentive();
            }
        });
    }
}
