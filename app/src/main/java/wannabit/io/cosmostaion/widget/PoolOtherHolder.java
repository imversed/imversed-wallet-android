package wannabit.io.cosmostaion.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.fulldive.wallet.models.BaseChain;

import java.math.BigDecimal;
import java.math.RoundingMode;

import cosmos.base.v1beta1.CoinOuterClass;
import kava.swap.v1beta1.QueryOuterClass;
import osmosis.gamm.poolmodels.balancer.BalancerPool;
import tendermint.liquidity.v1beta1.Liquidity;
import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.activities.chains.cosmos.GravityListActivity;
import wannabit.io.cosmostaion.activities.chains.kava.DAppsList5Activity;
import wannabit.io.cosmostaion.activities.chains.osmosis.LabsListActivity;
import wannabit.io.cosmostaion.base.BaseActivity;
import wannabit.io.cosmostaion.base.BaseData;
import wannabit.io.cosmostaion.model.type.Coin;
import wannabit.io.cosmostaion.utils.PriceProvider;
import wannabit.io.cosmostaion.utils.WDp;
import wannabit.io.cosmostaion.utils.WUtil;

public class PoolOtherHolder extends BaseHolder {
    CardView itemRoot;
    TextView itemPoolType;
    TextView itemTotalDepositValue;
    TextView itemTotalDepositAmount0, itemTotalDepositSymbol0, itemTotalDepositAmount1, itemTotalDepositSymbol1;
    TextView itemMyAvailableAmount0, itemMyAvailableSymbol0, itemMyAvailableAmount1, itemMyAvailableSymbol1;

    public PoolOtherHolder(@NonNull View itemView) {
        super(itemView);
        itemRoot = itemView.findViewById(R.id.card_pool_all);
        itemPoolType = itemView.findViewById(R.id.pool_market_type);
        itemTotalDepositValue = itemView.findViewById(R.id.pool_total_liquidity_value);
        itemTotalDepositAmount0 = itemView.findViewById(R.id.pool_total_liquidity_amount1);
        itemTotalDepositSymbol0 = itemView.findViewById(R.id.pool_total_liquidity_symbol1);
        itemTotalDepositAmount1 = itemView.findViewById(R.id.pool_total_liquidity_amount2);
        itemTotalDepositSymbol1 = itemView.findViewById(R.id.pool_total_liquidity_symbol2);

        itemMyAvailableAmount0 = itemView.findViewById(R.id.mypool_amount1);
        itemMyAvailableSymbol0 = itemView.findViewById(R.id.mypool_symbol1);
        itemMyAvailableAmount1 = itemView.findViewById(R.id.mypool_amount2);
        itemMyAvailableSymbol1 = itemView.findViewById(R.id.mypool_symbol2);
    }

    @Override
    public void onBindOsmoOtherPool(Context context, BaseActivity activity, BaseData baseData, BalancerPool.Pool otherPool) {
        Coin coin0 = new Coin(otherPool.getPoolAssets(0).getToken().getDenom(), otherPool.getPoolAssets(0).getToken().getAmount());
        Coin coin1 = new Coin(otherPool.getPoolAssets(1).getToken().getDenom(), otherPool.getPoolAssets(1).getToken().getAmount());

        final PriceProvider priceProvider = activity::getPrice;
        itemPoolType.setText("#" + otherPool.getId() + " " + WUtil.dpOsmosisTokenName(baseData, coin0.denom) + "/" + WUtil.dpOsmosisTokenName(baseData, coin1.denom));

        itemTotalDepositValue.setText("" + WDp.usdValue(baseData, baseData.getBaseDenom(coin0.denom), new BigDecimal(coin0.amount), WUtil.getOsmosisCoinDecimal(baseData, coin0.denom), priceProvider));

        BigDecimal coin0Value = WDp.usdValue(baseData, baseData.getBaseDenom(coin0.denom), new BigDecimal(coin0.amount), WUtil.getOsmosisCoinDecimal(baseData, coin0.denom), priceProvider);
        BigDecimal coin1Value = WDp.usdValue(baseData, baseData.getBaseDenom(coin1.denom), new BigDecimal(coin1.amount), WUtil.getOsmosisCoinDecimal(baseData, coin1.denom), priceProvider);
        BigDecimal PoolValue = coin0Value.add(coin1Value);
        itemTotalDepositValue.setText(WDp.getDpRawDollor(context, PoolValue, 2));

        WDp.showCoinDp(baseData, coin0, itemTotalDepositSymbol0, itemTotalDepositAmount0, BaseChain.OSMOSIS_MAIN.INSTANCE);
        WDp.showCoinDp(baseData, coin1, itemTotalDepositSymbol1, itemTotalDepositAmount1, BaseChain.OSMOSIS_MAIN.INSTANCE);

        BigDecimal availableCoin0 = activity.getBalance(coin0.denom);
        Coin Coin0 = new Coin(otherPool.getPoolAssets(0).getToken().getDenom(), availableCoin0.toPlainString());
        BigDecimal availableCoin1 = activity.getBalance(coin1.denom);
        Coin Coin1 = new Coin(otherPool.getPoolAssets(1).getToken().getDenom(), availableCoin1.toPlainString());

        WDp.showCoinDp(baseData, Coin0, itemMyAvailableSymbol0, itemMyAvailableAmount0, BaseChain.OSMOSIS_MAIN.INSTANCE);
        WDp.showCoinDp(baseData, Coin1, itemMyAvailableSymbol1, itemMyAvailableAmount1, BaseChain.OSMOSIS_MAIN.INSTANCE);

        itemRoot.setOnClickListener(v -> ((LabsListActivity) activity).onCheckStartJoinPool(otherPool.getId()));
    }

    @Override
    public void onBindKavaOtherPool(Context context, BaseActivity activity, BaseData baseData, QueryOuterClass.PoolResponse otherPool) {
        CoinOuterClass.Coin coin0 = otherPool.getCoins(0);
        CoinOuterClass.Coin coin1 = otherPool.getCoins(1);
        int coin0Decimal = WUtil.getKavaCoinDecimal(baseData, coin0.getDenom());
        int coin1Decimal = WUtil.getKavaCoinDecimal(baseData, coin1.getDenom());
        BigDecimal coin0price = WDp.getKavaPriceFeed(baseData, coin0.getDenom());
        BigDecimal coin1price = WDp.getKavaPriceFeed(baseData, coin1.getDenom());
        BigDecimal coin0value = new BigDecimal(coin0.getAmount()).multiply(coin0price).movePointLeft(coin0Decimal).setScale(2, RoundingMode.DOWN);
        BigDecimal coin1value = new BigDecimal(coin1.getAmount()).multiply(coin1price).movePointLeft(coin1Decimal).setScale(2, RoundingMode.DOWN);

        itemPoolType.setText(WUtil.getKavaTokenName(baseData, coin0.getDenom()) + " : " + WUtil.getKavaTokenName(baseData, coin1.getDenom()));

        // Total deposit
        BigDecimal poolValue = coin0value.add(coin1value);
        itemTotalDepositValue.setText(WDp.getDpRawDollor(context, poolValue, 2));

        WUtil.dpKavaTokenName(context, baseData, itemTotalDepositSymbol0, coin0.getDenom());
        WUtil.dpKavaTokenName(context, baseData, itemTotalDepositSymbol1, coin1.getDenom());
        itemTotalDepositAmount0.setText(WDp.getDpAmount2(new BigDecimal(coin0.getAmount()), coin0Decimal, 6));
        itemTotalDepositAmount1.setText(WDp.getDpAmount2(new BigDecimal(coin1.getAmount()), coin1Decimal, 6));

        // available
        BigDecimal availableCoin0 = activity.getBalance(coin0.getDenom());
        BigDecimal availableCoin1 = activity.getBalance(coin1.getDenom());

        WUtil.dpKavaTokenName(context, baseData, itemMyAvailableSymbol0, coin0.getDenom());
        WUtil.dpKavaTokenName(context, baseData, itemMyAvailableSymbol1, coin1.getDenom());
        itemMyAvailableAmount0.setText(WDp.getDpAmount2(availableCoin0, coin0Decimal, 6));
        itemMyAvailableAmount1.setText(WDp.getDpAmount2(availableCoin1, coin1Decimal, 6));

        itemRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DAppsList5Activity) activity).onCheckStartJoinPool(otherPool);
            }
        });
    }

    @Override
    public void onBindGDexOtherPool(Context context, GravityListActivity activity, BaseData baseData, Liquidity.Pool otherPool) {
        String coin0Denom = otherPool.getReserveCoinDenoms(0);
        String coin1Denom = otherPool.getReserveCoinDenoms(1);
        BigDecimal coin0Amount = activity.getLpAmount(otherPool.getReserveAccountAddress(), coin0Denom);
        BigDecimal coin1Amount = activity.getLpAmount(otherPool.getReserveAccountAddress(), coin1Denom);
        int coin0Decimal = WUtil.getCosmosCoinDecimal(baseData, coin0Denom);
        int coin1Decimal = WUtil.getCosmosCoinDecimal(baseData, coin1Denom);

        itemPoolType.setText("#" + otherPool.getId() + " " + WUtil.dpCosmosTokenName(baseData, coin0Denom) + " : " + WUtil.dpCosmosTokenName(baseData, coin1Denom));

        // Total deposit
        BigDecimal PoolValue = activity.getGdexPoolValue(otherPool);
        itemTotalDepositValue.setText(WDp.getDpRawDollor(context, PoolValue, 2));

        WUtil.dpCosmosTokenName(context, baseData, itemTotalDepositSymbol0, coin0Denom);
        WUtil.dpCosmosTokenName(context, baseData, itemTotalDepositSymbol1, coin1Denom);
        itemTotalDepositAmount0.setText(WDp.getDpAmount2(coin0Amount, coin0Decimal, 6));
        itemTotalDepositAmount1.setText(WDp.getDpAmount2(coin1Amount, coin1Decimal, 6));

        BigDecimal availableCoin0 = activity.getBalance(coin0Denom);
        Coin Coin0 = new Coin(coin0Denom, availableCoin0.toPlainString());
        BigDecimal availableCoin1 = activity.getBalance(coin1Denom);
        Coin Coin1 = new Coin(coin1Denom, availableCoin1.toPlainString());

        WDp.showCoinDp(baseData, Coin0, itemMyAvailableSymbol0, itemMyAvailableAmount0, BaseChain.COSMOS_MAIN.INSTANCE);
        WDp.showCoinDp(baseData, Coin1, itemMyAvailableSymbol1, itemMyAvailableAmount1, BaseChain.COSMOS_MAIN.INSTANCE);

        itemRoot.setOnClickListener(v -> (activity).onCheckStartDepositPool(otherPool.getId()));
    }
}
