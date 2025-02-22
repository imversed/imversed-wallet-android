package wannabit.io.cosmostaion.widget.tokenDetail;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.fulldive.wallet.models.BaseChain;

import java.math.BigDecimal;
import java.util.List;

import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.base.BaseActivity;
import wannabit.io.cosmostaion.base.BaseData;
import wannabit.io.cosmostaion.model.UnbondingInfo;
import wannabit.io.cosmostaion.utils.WDp;
import wannabit.io.cosmostaion.utils.WLog;
import wannabit.io.cosmostaion.utils.WUtil;
import wannabit.io.cosmostaion.widget.BaseHolder;

public class UnBondingHolder extends BaseHolder {
    private final CardView mUndelegateCard;
    private final TextView mUndelegateCnt;
    private final RelativeLayout mUndelegateLayer0;
    private final RelativeLayout mUndelegateLayer1;
    private final RelativeLayout mUndelegateLayer2;
    private final RelativeLayout mUndelegateLayer3;
    private final RelativeLayout mUndelegateLayer4;
    private final TextView mUndelegateTime0;
    private final TextView mUndelegateTime1;
    private final TextView mUndelegateTime2;
    private final TextView mUndelegateTime3;
    private final TextView mUndelegateTime4;
    private final TextView mUndelegateAmount0;
    private final TextView mUndelegateAmount1;
    private final TextView mUndelegateAmount2;
    private final TextView mUndelegateAmount3;
    private final TextView mUndelegateAmount4;
    private final TextView mUndelegateRemain0;
    private final TextView mUndelegateRemain1;
    private final TextView mUndelegateRemain2;
    private final TextView mUndelegateRemain3;
    private final TextView mUndelegateRemain4;

    public UnBondingHolder(@NonNull View itemView) {
        super(itemView);
        mUndelegateCard = itemView.findViewById(R.id.cardView);
        mUndelegateCnt = itemView.findViewById(R.id.undelegate_count);

        mUndelegateLayer0 = itemView.findViewById(R.id.undelegate_detail_layer0);
        mUndelegateLayer1 = itemView.findViewById(R.id.undelegate_detail_layer1);
        mUndelegateLayer2 = itemView.findViewById(R.id.undelegate_detail_layer2);
        mUndelegateLayer3 = itemView.findViewById(R.id.undelegate_detail_layer3);
        mUndelegateLayer4 = itemView.findViewById(R.id.undelegate_detail_layer4);

        mUndelegateTime0 = itemView.findViewById(R.id.undelegate_detail_time0);
        mUndelegateTime1 = itemView.findViewById(R.id.undelegate_detail_time1);
        mUndelegateTime2 = itemView.findViewById(R.id.undelegate_detail_time2);
        mUndelegateTime3 = itemView.findViewById(R.id.undelegate_detail_time3);
        mUndelegateTime4 = itemView.findViewById(R.id.undelegate_detail_time4);

        mUndelegateAmount0 = itemView.findViewById(R.id.undelegate_detail_amount0);
        mUndelegateAmount1 = itemView.findViewById(R.id.undelegate_detail_amount1);
        mUndelegateAmount2 = itemView.findViewById(R.id.undelegate_detail_amount2);
        mUndelegateAmount3 = itemView.findViewById(R.id.undelegate_detail_amount3);
        mUndelegateAmount4 = itemView.findViewById(R.id.undelegate_detail_amount4);

        mUndelegateRemain0 = itemView.findViewById(R.id.undelegate_detail_time0_remain);
        mUndelegateRemain1 = itemView.findViewById(R.id.undelegate_detail_time1_remain);
        mUndelegateRemain2 = itemView.findViewById(R.id.undelegate_detail_time2_remain);
        mUndelegateRemain3 = itemView.findViewById(R.id.undelegate_detail_time3_remain);
        mUndelegateRemain4 = itemView.findViewById(R.id.undelegate_detail_time4_remain);
    }

    @Override
    public void onBindTokenHolder(BaseActivity baseActivity, BaseChain chain, BaseData baseData, String denom) {
        mUndelegateCard.setCardBackgroundColor(WDp.getChainBgColor(baseActivity, chain));
        if (chain.isGRPC()) {
            onBindUnbondingGRPC(baseActivity, chain, baseData, denom);
        } else {
            onBindUnbonding(baseActivity, chain, baseData, denom);
        }
    }

    private void onBindUnbondingGRPC(Context c, BaseChain chain, BaseData baseData, String denom) {
        mUndelegateLayer1.setVisibility(View.GONE);
        mUndelegateLayer2.setVisibility(View.GONE);
        mUndelegateLayer3.setVisibility(View.GONE);
        mUndelegateLayer4.setVisibility(View.GONE);

        final int stakingDivideDecimal = chain.getDivideDecimal();
        final int stakingDisplayDecimal = chain.getDivideDecimal();

        final List<UnbondingInfo.DpEntry> unbondings = WUtil.onSortUnbondingsRecent_Grpc(baseData.mGrpcUndelegations);
        mUndelegateCnt.setText(String.valueOf(unbondings.size()));
        mUndelegateTime0.setText(WDp.getDpTime(c, Long.parseLong(unbondings.get(0).completion_time) * 1000));
        mUndelegateAmount0.setText(WDp.getDpAmount2(new BigDecimal(unbondings.get(0).balance), stakingDivideDecimal, stakingDisplayDecimal));
        mUndelegateRemain0.setText(WDp.getUnbondingTimeleft(Long.parseLong(unbondings.get(0).completion_time) * 1000));

        if (unbondings.size() > 1) {
            WLog.w("1 " + unbondings.get(1).completion_time);
            mUndelegateLayer1.setVisibility(View.VISIBLE);
            mUndelegateTime1.setText(WDp.getDpTime(c, Long.parseLong(unbondings.get(1).completion_time) * 1000));
            mUndelegateAmount1.setText(WDp.getDpAmount2(new BigDecimal(unbondings.get(1).balance), stakingDivideDecimal, stakingDisplayDecimal));
            mUndelegateRemain1.setText(WDp.getUnbondingTimeleft(Long.parseLong(unbondings.get(1).completion_time) * 1000));
        }
        if (unbondings.size() > 2) {
            mUndelegateLayer2.setVisibility(View.VISIBLE);
            mUndelegateTime2.setText(WDp.getDpTime(c, Long.parseLong(unbondings.get(2).completion_time) * 1000));
            mUndelegateAmount2.setText(WDp.getDpAmount2(new BigDecimal(unbondings.get(2).balance), stakingDivideDecimal, stakingDisplayDecimal));
            mUndelegateRemain2.setText(WDp.getUnbondingTimeleft(Long.parseLong(unbondings.get(2).completion_time) * 1000));
        }
        if (unbondings.size() > 3) {
            mUndelegateLayer3.setVisibility(View.VISIBLE);
            mUndelegateTime3.setText(WDp.getDpTime(c, Long.parseLong(unbondings.get(3).completion_time) * 1000));
            mUndelegateAmount3.setText(WDp.getDpAmount2(new BigDecimal(unbondings.get(3).balance), stakingDivideDecimal, stakingDisplayDecimal));
            mUndelegateRemain3.setText(WDp.getUnbondingTimeleft(Long.parseLong(unbondings.get(3).completion_time) * 1000));
        }
        if (unbondings.size() > 4) {
            mUndelegateLayer4.setVisibility(View.VISIBLE);
            mUndelegateTime4.setText(WDp.getDpTime(c, Long.parseLong(unbondings.get(4).completion_time) * 1000));
            mUndelegateAmount4.setText(WDp.getDpAmount2(new BigDecimal(unbondings.get(4).balance), stakingDivideDecimal, stakingDisplayDecimal));
            mUndelegateRemain4.setText(WDp.getUnbondingTimeleft(Long.parseLong(unbondings.get(4).completion_time) * 1000));
        }
    }

    private void onBindUnbonding(Context c, BaseChain chain, BaseData baseData, String denom) {
        mUndelegateLayer1.setVisibility(View.GONE);
        mUndelegateLayer2.setVisibility(View.GONE);
        mUndelegateLayer3.setVisibility(View.GONE);
        mUndelegateLayer4.setVisibility(View.GONE);

        final int stakingDivideDecimal = chain.getDivideDecimal();
        final int stakingDisplayDecimal = chain.getDivideDecimal();

        final List<UnbondingInfo.DpEntry> unbondings = WUtil.onSortUnbondingsRecent(c, baseData.mMyUnbondings);
        mUndelegateCnt.setText(String.valueOf(unbondings.size()));
        mUndelegateTime0.setText(WDp.getTimeformat(c, unbondings.get(0).completion_time));
        mUndelegateAmount0.setText(WDp.getDpAmount2(new BigDecimal(unbondings.get(0).balance), stakingDivideDecimal, stakingDisplayDecimal));
        mUndelegateRemain0.setText(WDp.getUnbondingTimeleft(WDp.dateToLong(c, unbondings.get(0).completion_time)));

        if (unbondings.size() > 1) {
            mUndelegateLayer1.setVisibility(View.VISIBLE);
            mUndelegateTime1.setText(WDp.getTimeformat(c, unbondings.get(1).completion_time));
            mUndelegateAmount1.setText(WDp.getDpAmount2(new BigDecimal(unbondings.get(1).balance), stakingDivideDecimal, stakingDisplayDecimal));
            mUndelegateRemain1.setText(WDp.getUnbondingTimeleft(WDp.dateToLong(c, unbondings.get(1).completion_time)));
        }
        if (unbondings.size() > 2) {
            mUndelegateLayer2.setVisibility(View.VISIBLE);
            mUndelegateTime2.setText(WDp.getTimeformat(c, unbondings.get(2).completion_time));
            mUndelegateAmount2.setText(WDp.getDpAmount2(new BigDecimal(unbondings.get(2).balance), stakingDivideDecimal, stakingDisplayDecimal));
            mUndelegateRemain2.setText(WDp.getUnbondingTimeleft(WDp.dateToLong(c, unbondings.get(2).completion_time)));
        }
        if (unbondings.size() > 3) {
            mUndelegateLayer3.setVisibility(View.VISIBLE);
            mUndelegateTime3.setText(WDp.getTimeformat(c, unbondings.get(3).completion_time));
            mUndelegateAmount3.setText(WDp.getDpAmount2(new BigDecimal(unbondings.get(3).balance), stakingDivideDecimal, stakingDisplayDecimal));
            mUndelegateRemain3.setText(WDp.getUnbondingTimeleft(WDp.dateToLong(c, unbondings.get(3).completion_time)));
        }
        if (unbondings.size() > 4) {
            mUndelegateLayer4.setVisibility(View.VISIBLE);
            mUndelegateTime4.setText(WDp.getTimeformat(c, unbondings.get(4).completion_time));
            mUndelegateAmount4.setText(WDp.getDpAmount2(new BigDecimal(unbondings.get(4).balance), stakingDivideDecimal, stakingDisplayDecimal));
            mUndelegateRemain4.setText(WDp.getUnbondingTimeleft(WDp.dateToLong(c, unbondings.get(4).completion_time)));
        }
    }
}
