package wannabit.io.cosmostaion.widget;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.fulldive.wallet.models.BaseChain;

import java.math.BigDecimal;

import sifnode.clp.v1.Querier;
import sifnode.clp.v1.Types;
import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.activities.chains.sif.SifDexListActivity;
import wannabit.io.cosmostaion.base.BaseData;
import wannabit.io.cosmostaion.utils.PriceProvider;
import wannabit.io.cosmostaion.utils.WDp;
import wannabit.io.cosmostaion.utils.WUtil;

public class SifPoolMyHolder extends BaseHolder {
    CardView itemRoot;
    ImageView itemExternalImg;
    TextView itemMyPoolType;
    TextView itemMyTotalDepositValue;
    TextView itemMyTotalDepositAmount0, itemMyTotalDepositSymbol0, itemMyTotalDepositAmount1, itemMyTotalDepositSymbol1;
    TextView itemMypoolDepositValue;
    TextView itemMyDepositAmount0, itemMyDepositSymbol0, itemMyDepositAmount1, itemMyDepositSymbol1;
    TextView itemMyAvailableAmount0, itemMyAvailableSymbol0, itemMyAvailableAmount1, itemMyAvailableSymbol1;

    public SifPoolMyHolder(@NonNull View itemView) {
        super(itemView);
        itemRoot = itemView.findViewById(R.id.cardView);
        itemExternalImg = itemView.findViewById(R.id.mypool_external_img);
        itemMyPoolType = itemView.findViewById(R.id.mypool_market_type);
        itemMyTotalDepositValue = itemView.findViewById(R.id.mypool_total_liquidity_value);
        itemMyTotalDepositAmount0 = itemView.findViewById(R.id.mypool_total_liquidity_amount1);
        itemMyTotalDepositSymbol0 = itemView.findViewById(R.id.mypool_total_liquidity_symbol1);
        itemMyTotalDepositAmount1 = itemView.findViewById(R.id.mypool_total_liquidity_amount2);
        itemMyTotalDepositSymbol1 = itemView.findViewById(R.id.mypool_total_liquidity_symbol2);

        itemMypoolDepositValue = itemView.findViewById(R.id.mypool_deposit_value);
        itemMyDepositAmount0 = itemView.findViewById(R.id.mypool_deposit_amout0);
        itemMyDepositSymbol0 = itemView.findViewById(R.id.mypool_deposit_symbol0);
        itemMyDepositAmount1 = itemView.findViewById(R.id.mypool_deposit_amout1);
        itemMyDepositSymbol1 = itemView.findViewById(R.id.mypool_deposit_symbol1);

        itemMyAvailableAmount0 = itemView.findViewById(R.id.my_available_amount0);
        itemMyAvailableSymbol0 = itemView.findViewById(R.id.my_available_symbol0);
        itemMyAvailableAmount1 = itemView.findViewById(R.id.my_available_amount1);
        itemMyAvailableSymbol1 = itemView.findViewById(R.id.my_available_symbol1);
    }

    @Override
    public void onBindSifMyEthPool(Context context, SifDexListActivity activity, BaseData baseData, Types.Pool myPool, Querier.LiquidityProviderRes myProvider) {
        int rowanDecimal = WDp.mainDisplayDecimal(BaseChain.SIF_MAIN.INSTANCE.getMainDenom());
        BigDecimal rowanAmount = new BigDecimal(myPool.getNativeAssetBalance());
        int externalDecimal = WUtil.getSifCoinDecimal(baseData, myPool.getExternalAsset().getSymbol());
        BigDecimal externalAmount = new BigDecimal(myPool.getExternalAssetBalance());
        String exteranlDenom = myPool.getExternalAsset().getSymbol();
        final PriceProvider priceProvider = activity::getPrice;
        BigDecimal poolValue = WUtil.getSifPoolValue(baseData, myPool, priceProvider);


        WUtil.DpSifTokenImg(baseData, itemExternalImg, exteranlDenom);
        itemMyPoolType.setText("ROWAN : " + WUtil.dpSifTokenName(baseData, exteranlDenom).toUpperCase());
        itemMyTotalDepositValue.setText(WDp.getDpRawDollor(context, poolValue, 2));

        WUtil.dpSifTokenName(context, baseData, itemMyTotalDepositSymbol0, BaseChain.SIF_MAIN.INSTANCE.getMainDenom());
        WUtil.dpSifTokenName(context, baseData, itemMyTotalDepositSymbol1, exteranlDenom);
        itemMyTotalDepositAmount0.setText(WDp.getDpAmount2(rowanAmount, rowanDecimal, 6));
        itemMyTotalDepositAmount1.setText(WDp.getDpAmount2(externalAmount, externalDecimal, 6));

        //dp my lp info
        if (myPool != null && myProvider != null) {
            BigDecimal myShareValue = WUtil.getSifMyShareValue(baseData, myPool, myProvider, priceProvider);
            itemMypoolDepositValue.setText(WDp.getDpRawDollor(context, myShareValue, 2));
            WUtil.dpSifTokenName(context, baseData, itemMyDepositSymbol0, BaseChain.SIF_MAIN.INSTANCE.getMainDenom());
            WUtil.dpSifTokenName(context, baseData, itemMyDepositSymbol1, exteranlDenom);
            itemMyDepositAmount0.setText(WDp.getDpAmount2(new BigDecimal(myProvider.getNativeAssetBalance()), rowanDecimal, 6));
            itemMyDepositAmount1.setText(WDp.getDpAmount2(new BigDecimal(myProvider.getExternalAssetBalance()), externalDecimal, 6));
        }

        //dp available
        BigDecimal availableRowan = activity.getBalance(BaseChain.SIF_MAIN.INSTANCE.getMainDenom());
        BigDecimal availableExternal = activity.getBalance(exteranlDenom);
        WUtil.dpSifTokenName(context, baseData, itemMyAvailableSymbol0, BaseChain.SIF_MAIN.INSTANCE.getMainDenom());
        WUtil.dpSifTokenName(context, baseData, itemMyAvailableSymbol1, exteranlDenom);
        itemMyAvailableAmount0.setText(WDp.getDpAmount2(availableRowan, rowanDecimal, 6));
        itemMyAvailableAmount1.setText(WDp.getDpAmount2(availableExternal, externalDecimal, 6));

        itemRoot.setOnClickListener(v -> activity.onClickMyPool(myPool, myProvider));
    }
}
