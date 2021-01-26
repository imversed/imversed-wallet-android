package wannabit.io.cosmostaion.widget;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.activities.MainActivity;
import wannabit.io.cosmostaion.base.BaseData;
import wannabit.io.cosmostaion.dialog.Dialog_Help_Msg;
import wannabit.io.cosmostaion.utils.WDp;
import wannabit.io.cosmostaion.utils.WLog;

import static wannabit.io.cosmostaion.base.BaseChain.COSMOS_TEST;
import static wannabit.io.cosmostaion.base.BaseChain.IRIS_MAIN;
import static wannabit.io.cosmostaion.base.BaseChain.IRIS_TEST;

public class WalletMintHolder extends WalletHolder {
    public CardView             mAprCard;
    public TextView             mInflation, mAPR;

    public WalletMintHolder(@NonNull View itemView) {
        super(itemView);
        mAprCard            = itemView.findViewById(R.id.apr_card);
        mInflation          = itemView.findViewById(R.id.mint_inflation);
        mAPR                = itemView.findViewById(R.id.mint_apr);
    }

    public void onBindHolder(@NotNull MainActivity mainActivity) {
        final BaseData baseData = mainActivity.getBaseDao();
        if (mainActivity.mBaseChain.equals(COSMOS_TEST)) {
            mInflation.setText(WDp.getPercentDp(baseData.mInflation_V1.multiply(new BigDecimal("100"))));
            mAPR.setText(WDp.getDpEstApr(mainActivity.getBaseDao(), mainActivity.mBaseChain));

        } else if (mainActivity.mBaseChain.equals(IRIS_TEST)) {
            mInflation.setText(WDp.getPercentDp(baseData.mParamMint_V1.getInflation().multiply(new BigDecimal("100"))));
            mAPR.setText(WDp.getDpEstApr(mainActivity.getBaseDao(), mainActivity.mBaseChain));

        } else if (mainActivity.mBaseChain.equals(IRIS_MAIN)) {
            mInflation.setText(WDp.getPercentDp(new BigDecimal("4")));
            mAPR.setText(WDp.getIrisYieldString(baseData.mIrisStakingPool, BigDecimal.ZERO));

        } else {
            mInflation.setText(WDp.getPercentDp(baseData.mInflation.multiply(new BigDecimal("100"))));
            mAPR.setText(WDp.getDpEstApr(mainActivity.getBaseDao(), mainActivity.mBaseChain));
        }

        mAprCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("title", mainActivity.getString(R.string.str_apr_help_title));
                bundle.putString("msg", mainActivity.getString(R.string.str_apr_help_msg));
                Dialog_Help_Msg dialog = Dialog_Help_Msg.newInstance(bundle);
                dialog.setCancelable(true);
                mainActivity.getSupportFragmentManager().beginTransaction().add(dialog, "dialog").commitNowAllowingStateLoss();
            }
        });
    }
}
