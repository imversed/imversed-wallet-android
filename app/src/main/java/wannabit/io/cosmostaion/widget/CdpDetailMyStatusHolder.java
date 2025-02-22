package wannabit.io.cosmostaion.widget;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

import kava.cdp.v1beta1.Genesis;
import kava.cdp.v1beta1.QueryOuterClass;
import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.activities.chains.kava.CdpDetail5Activity;
import wannabit.io.cosmostaion.base.BaseData;
import wannabit.io.cosmostaion.dialog.Dialog_Help_Msg;
import wannabit.io.cosmostaion.utils.WDp;
import wannabit.io.cosmostaion.utils.WUtil;

public class CdpDetailMyStatusHolder extends BaseHolder {
    private final ImageView mMyCollateralImg;
    private final TextView mMyCollateralDenom;
    private final LinearLayout mMySelfDepositLayer;
    private final TextView mMySelfDepositAmount;
    private final LinearLayout mMyTotalDepositLayer;
    private final TextView mMyTotalDepositAmount;
    private final LinearLayout mMyWithdrawableLayer;
    private final TextView mMyWithdrawableAmountTitle;
    private final TextView mMyWithdrawableAmount;
    private final TextView mMySelfDepositValue;
    private final TextView mMyTotalDepositValue;
    private final TextView mMyWithdrawableValue;
    private final RelativeLayout mMyBtnDeposit;
    private final RelativeLayout mMyBtnWithdraw;
    private final TextView mMyBtnDepositTxt;
    private final TextView mMyBtnWithdrawTxt;
    private final ImageView mMyPrincipalImg;
    private final TextView mMyPrincipalDenom;
    private final LinearLayout mMyLoadnedLayer;
    private final TextView mMyLoadnedAmount;
    private final TextView mMyLoadedValue;
    private final LinearLayout mMyCdpFeeLayer;
    private final TextView mMyCdpFeeAmount;
    private final TextView mMyCdpFeeValue;
    private final LinearLayout mMyLoadableLayer;
    private final TextView mMyLoadableAmount;
    private final TextView mMyLoadableValue;
    private final RelativeLayout mMyBtnDrawdebt;
    private final RelativeLayout mMyBtnRepay;

    public CdpDetailMyStatusHolder(@NonNull View itemView) {
        super(itemView);
        mMyCollateralImg = itemView.findViewById(R.id.collateral_icon);
        mMyCollateralDenom = itemView.findViewById(R.id.collateral_denom);
        mMySelfDepositLayer = itemView.findViewById(R.id.self_deposited_amount_layer);
        mMySelfDepositAmount = itemView.findViewById(R.id.self_deposited_amount);
        mMyTotalDepositLayer = itemView.findViewById(R.id.total_deposited_amount_layer);
        mMyTotalDepositAmount = itemView.findViewById(R.id.total_deposited_amount);
        mMyWithdrawableLayer = itemView.findViewById(R.id.expected_withdrawable_amount_layer);
        mMyWithdrawableAmountTitle = itemView.findViewById(R.id.expected_withdrawable_amount_title);
        mMyWithdrawableAmount = itemView.findViewById(R.id.expected_withdrawable_amount);
        mMySelfDepositValue = itemView.findViewById(R.id.self_deposited_value);
        mMyTotalDepositValue = itemView.findViewById(R.id.total_deposited_value);
        mMyWithdrawableValue = itemView.findViewById(R.id.expected_withdrawable_value);
        mMyBtnDeposit = itemView.findViewById(R.id.btn_deposit);
        mMyBtnDepositTxt = itemView.findViewById(R.id.btn_deposit_txt);
        mMyBtnWithdraw = itemView.findViewById(R.id.btn_withdraw);
        mMyBtnWithdrawTxt = itemView.findViewById(R.id.btn_withdraw_txt);
        mMyPrincipalImg = itemView.findViewById(R.id.principal_icon);
        mMyPrincipalDenom = itemView.findViewById(R.id.principal_denom);
        mMyLoadnedLayer = itemView.findViewById(R.id.loaned_amount_layer);
        mMyLoadnedAmount = itemView.findViewById(R.id.loaned_amount);
        mMyLoadedValue = itemView.findViewById(R.id.loaned_value);
        mMyCdpFeeLayer = itemView.findViewById(R.id.cdp_fee_amount_layer);
        mMyCdpFeeAmount = itemView.findViewById(R.id.cdp_fee_amount);
        mMyCdpFeeValue = itemView.findViewById(R.id.fee_value);
        mMyLoadableLayer = itemView.findViewById(R.id.expected_loanable_amount_layer);
        mMyLoadableAmount = itemView.findViewById(R.id.expected_loanable_amount);
        mMyLoadableValue = itemView.findViewById(R.id.loanable_value);
        mMyBtnDrawdebt = itemView.findViewById(R.id.btn_drawdebt);
        mMyBtnRepay = itemView.findViewById(R.id.btn_repay);
    }

    @Override
    public void onBindCdpDetailMyStatus(CdpDetail5Activity context, BaseData baseData, QueryOuterClass.CDPResponse myCdp, String collateralType, BigDecimal selfDepositAmount) {
        final Genesis.CollateralParam collateralParam = baseData.getCollateralParamByType(collateralType);
        final String cDenom = collateralParam.getDenom();
        final String pDenom = collateralParam.getDebtLimit().getDenom();
        final BigDecimal currentPrice = baseData.getKavaOraclePrice(collateralParam.getLiquidationMarketId());

        mMySelfDepositAmount.setText(WDp.getDpAmount2(selfDepositAmount, WUtil.getKavaCoinDecimal(baseData, cDenom), WUtil.getKavaCoinDecimal(baseData, cDenom)));
        BigDecimal selfDepositValue = selfDepositAmount.movePointLeft(WUtil.getKavaCoinDecimal(baseData, cDenom)).multiply(currentPrice).setScale(2, RoundingMode.DOWN);
        mMySelfDepositValue.setText(WDp.getDpRawDollor(context, selfDepositValue, 2));

        mMyTotalDepositAmount.setText(WDp.getDpAmount2(new BigDecimal(myCdp.getCollateral().getAmount()), WUtil.getKavaCoinDecimal(baseData, cDenom), WUtil.getKavaCoinDecimal(baseData, cDenom)));
        BigDecimal totalDepositValue = new BigDecimal(myCdp.getCollateral().getAmount()).movePointLeft(WUtil.getKavaCoinDecimal(baseData, cDenom)).multiply(currentPrice).setScale(2, RoundingMode.DOWN);
        mMyTotalDepositValue.setText(WDp.getDpRawDollor(context, totalDepositValue, 2));

        mMyWithdrawableAmountTitle.setText(context.getString(R.string.str_expected_withdrawable_amount) + " " + WUtil.getKavaTokenName(baseData, cDenom));
        BigDecimal maxWithdrawableAmount = WUtil.getWithdrawableAmount(context, baseData, myCdp, collateralParam, currentPrice, selfDepositAmount);
        BigDecimal maxWithdrawableValue = maxWithdrawableAmount.movePointLeft(WUtil.getKavaCoinDecimal(baseData, cDenom)).multiply(currentPrice);
        mMyWithdrawableAmount.setText(WDp.getDpAmount2(maxWithdrawableAmount, WUtil.getKavaCoinDecimal(baseData, cDenom), WUtil.getKavaCoinDecimal(baseData, cDenom)));
        mMyWithdrawableValue.setText(WDp.getDpRawDollor(context, maxWithdrawableValue, 2));

        final BigDecimal debtValue = new BigDecimal(myCdp.getPrincipal().getAmount());
        mMyLoadnedAmount.setText(WDp.getDpAmount2(debtValue, WUtil.getKavaCoinDecimal(baseData, pDenom), WUtil.getKavaCoinDecimal(baseData, pDenom)));
        mMyLoadedValue.setText(WDp.getDpRawDollor(context, debtValue.movePointLeft(WUtil.getKavaCoinDecimal(baseData, pDenom)), 2));

        final BigDecimal totalFeeValue = WUtil.getEstimatedTotalFee(context, myCdp, collateralParam);
        mMyCdpFeeAmount.setText(WDp.getDpAmount2(totalFeeValue, WUtil.getKavaCoinDecimal(baseData, pDenom), WUtil.getKavaCoinDecimal(baseData, pDenom)));
        mMyCdpFeeValue.setText(WDp.getDpRawDollor(context, totalFeeValue.movePointLeft(WUtil.getKavaCoinDecimal(baseData, pDenom)), 2));

        final BigDecimal moreDebtAmount = WUtil.getMoreLoanableAmount(context, myCdp, collateralParam);
        mMyLoadableAmount.setText(WDp.getDpAmount2(moreDebtAmount, WUtil.getKavaCoinDecimal(baseData, pDenom), WUtil.getKavaCoinDecimal(baseData, pDenom)));
        mMyLoadableValue.setText(WDp.getDpRawDollor(context, moreDebtAmount.movePointLeft(WUtil.getKavaCoinDecimal(baseData, pDenom)), 2));

        mMyBtnDepositTxt.setText(String.format(context.getString(R.string.str_btn_text_deposit), WUtil.getKavaTokenName(baseData, cDenom)));
        mMyBtnWithdrawTxt.setText(String.format(context.getString(R.string.str_btn_text_withdraw), WUtil.getKavaTokenName(baseData, cDenom)));

        mMyPrincipalDenom.setText(WUtil.getKavaTokenName(baseData, pDenom));
        mMyCollateralDenom.setText(WUtil.getKavaTokenName(baseData, collateralParam.getDenom()));

        WUtil.DpKavaTokenImg(baseData, mMyCollateralImg, cDenom);
        WUtil.DpKavaTokenImg(baseData, mMyPrincipalImg, pDenom);
        WUtil.dpKavaTokenName(context, baseData, mMyCollateralDenom, cDenom);
        WUtil.dpKavaTokenName(context, baseData, mMyPrincipalDenom, pDenom);

        mMySelfDepositLayer.setOnClickListener(v -> {
            onShowHelpPopup(context, context.getString(R.string.str_help_self_deposited_collateral_t),
                    String.format(context.getString(R.string.str_help_self_deposited_collateral_), WUtil.getKavaTokenName(baseData, collateralParam.getDenom())));
        });
        mMyTotalDepositLayer.setOnClickListener(v -> {
            onShowHelpPopup(context, context.getString(R.string.str_help_total_deposited_collateral_t),
                    String.format(context.getString(R.string.str_help_total_deposited_collateral), WUtil.getKavaTokenName(baseData, collateralParam.getDenom())));
        });
        mMyWithdrawableLayer.setOnClickListener(v -> {
            onShowHelpPopup(context, context.getString(R.string.str_expected_withdrawable_amount) + " " + WUtil.getKavaTokenName(baseData, collateralParam.getDenom()),
                    context.getString(R.string.str_help_withdrawable));
        });
        mMyLoadnedLayer.setOnClickListener(v -> onShowHelpPopup(context, context.getString(R.string.str_help_loaned_amount_t), context.getString(R.string.str_help_loaned_amount)));
        mMyCdpFeeLayer.setOnClickListener(v -> onShowHelpPopup(context, context.getString(R.string.str_help_total_fee_t), context.getString(R.string.str_help_total_fee)));
        mMyLoadableLayer.setOnClickListener(v -> onShowHelpPopup(context, context.getString(R.string.str_help_remaining_loan_capacity_t), context.getString(R.string.str_help_remaining_loan_capacity)));

        mMyBtnDeposit.setOnClickListener(v -> context.onCheckStartDepositCdp());
        mMyBtnWithdraw.setOnClickListener(v -> context.onCheckStartWithdrawCdp());
        mMyBtnDrawdebt.setOnClickListener(v -> context.onCheckStartDrawDebtCdp());
        mMyBtnRepay.setOnClickListener(v -> context.onCheckStartRepayCdp());
    }

    private void onShowHelpPopup(CdpDetail5Activity context, String title, String msg) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("msg", msg);
        Dialog_Help_Msg dialog = Dialog_Help_Msg.newInstance(bundle);
        context.showDialog(dialog);
    }
}
