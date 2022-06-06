package wannabit.io.cosmostaion.utils;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_EMONEY_CHF;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_EMONEY_DKK;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_EMONEY_EUR;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_EMONEY_NOK;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_EMONEY_SEK;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_HARD;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_HTLC_KAVA_BNB;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_HTLC_KAVA_BUSD;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_HTLC_KAVA_XRPB;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_ION;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_SWP;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_USDX;
import static wannabit.io.cosmostaion.network.res.ResBnbSwapInfo.BNB_STATUS_COMPLETED;
import static wannabit.io.cosmostaion.network.res.ResBnbSwapInfo.BNB_STATUS_OPEN;
import static wannabit.io.cosmostaion.network.res.ResBnbSwapInfo.BNB_STATUS_REFUNDED;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.fulldive.wallet.extensions.ContextExtensionsKt;
import com.fulldive.wallet.interactors.secret.WalletUtils;
import com.fulldive.wallet.models.BaseChain;
import com.fulldive.wallet.models.Currency;
import com.fulldive.wallet.models.WalletBalance;
import com.fulldive.wallet.models.local.DenomMetadata;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cosmos.base.abci.v1beta1.Abci;
import cosmos.base.v1beta1.CoinOuterClass;
import cosmos.staking.v1beta1.Staking;
import cosmos.tx.v1beta1.ServiceOuterClass;
import cosmos.vesting.v1beta1.Vesting;
import kava.cdp.v1beta1.Genesis;
import kava.cdp.v1beta1.QueryOuterClass;
import osmosis.gamm.poolmodels.balancer.BalancerPool;
import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.base.BaseActivity;
import wannabit.io.cosmostaion.base.BaseConstant;
import wannabit.io.cosmostaion.base.BaseData;
import wannabit.io.cosmostaion.dao.Assets;
import wannabit.io.cosmostaion.dao.ChainParam;
import wannabit.io.cosmostaion.dao.Cw20Assets;
import wannabit.io.cosmostaion.dao.IbcToken;
import wannabit.io.cosmostaion.dao.OkTicker;
import wannabit.io.cosmostaion.dao.OkToken;
import wannabit.io.cosmostaion.dao.Price;
import wannabit.io.cosmostaion.model.type.Coin;
import wannabit.io.cosmostaion.network.res.ResBnbSwapInfo;
import wannabit.io.cosmostaion.network.res.ResNodeInfo;
import wannabit.io.cosmostaion.network.res.ResProposal;

public class WDp {
    //show display text with full input amount and to divide deciaml and to show point
    public static SpannableString getDpAmount2(BigDecimal input, int divideDecimal, int displayDecimal) {
        SpannableString result;
        BigDecimal amount = input.movePointLeft(divideDecimal).setScale(displayDecimal, BigDecimal.ROUND_DOWN);
        result = new SpannableString(getDecimalFormat(displayDecimal).format(amount));
        result.setSpan(new RelativeSizeSpan(0.8f), result.length() - displayDecimal, result.length(), SPAN_INCLUSIVE_INCLUSIVE);
        return result;
    }

    public static SpannableString getDpString(String input, int point) {
        SpannableString result;
        result = new SpannableString(input);
        result.setSpan(new RelativeSizeSpan(0.8f), result.length() - point, result.length(), SPAN_INCLUSIVE_INCLUSIVE);
        return result;
    }

    public static SpannableString getDpGasRate(String input) {
        SpannableString result;
        result = new SpannableString(input);
        result.setSpan(new RelativeSizeSpan(0.8f), 2, result.length(), SPAN_INCLUSIVE_INCLUSIVE);
        return result;
    }

    public static void showCoinDp(BaseData baseData, String denom, String amount, TextView denomTextView, TextView amountTextView, BaseChain chain) {
        showCoinDp(baseData, new Coin(denom, amount), denomTextView, amountTextView, chain);
    }

    public static void showCoinDp(BaseData baseData, Coin coin, TextView denomTextView, TextView amountTextView, BaseChain chain) {
        final Context context = denomTextView.getContext();
        int divideDecimal = chain.getDivideDecimal();
        int displayDecimal = chain.getDisplayDecimal();
        int denomColorRes = R.color.colorWhite;
        String denom = coin.denom.toUpperCase();

        if (coin.denom.equals(chain.getMainDenom())) {
            denomColorRes = chain.getChainColor();
            denom = context.getString(chain.getSymbolTitle());
        } else if (chain.isGRPC()) {
            final DenomMetadata denomMetadata = baseData.getDenomMetadata(chain.getChainName(), denom);
            if (denomMetadata != null) {
                int divider = denomMetadata.getDenomUnit(denom).getExpanent();
                divideDecimal = divider;
                displayDecimal = divider;
            }
        }

        if (chain.isGRPC() && coin.isIbc()) {
            IbcToken ibcToken = baseData.getIbcToken(coin.getIbcHash());
            if (ibcToken != null && ibcToken.auth) {
                divideDecimal = ibcToken.decimal;
                displayDecimal = ibcToken.decimal;

                denom = ibcToken.display_denom.toUpperCase();
            } else {
                denom = context.getString(R.string.str_unknown);
            }
            denomColorRes = R.color.colorWhite;

        } else if (chain.equals(BaseChain.IMVERSED_MAIN.INSTANCE) || chain.equals(BaseChain.IMVERSED_TEST.INSTANCE)) {
            int divider  = WUtil.getImvCoinDecimal(coin.denom);
            divideDecimal = divider;
            displayDecimal = divider;
        } else if (chain.equals(BaseChain.KAVA_MAIN.INSTANCE)) {
            int decimal = WUtil.getKavaCoinDecimal(coin);
            divideDecimal = decimal;
            displayDecimal = decimal;

            if (!coin.denom.equals(chain.getMainDenom())) {
                switch (coin.denom) {
                    case TOKEN_HARD:
                        denomColorRes = R.color.colorHard;
                        break;
                    case TOKEN_USDX:
                        denomColorRes = R.color.colorUsdx;
                        break;
                    case TOKEN_SWP:
                        denomColorRes = R.color.colorSwp;
                        break;
                }
            }

        } else if (chain.equals(BaseChain.SIF_MAIN.INSTANCE)) {
            int decimal = WUtil.getSifCoinDecimal(coin.denom);
            divideDecimal = decimal;
            displayDecimal = decimal;

            if (coin.denom.startsWith("c")) {
                denom = coin.denom.substring(1).toUpperCase();
            }

        } else if (chain.equals(BaseChain.OSMOSIS_MAIN.INSTANCE)) {
            if (!coin.denom.equals(chain.getMainDenom())) {
                if (coin.denom.equals(TOKEN_ION)) {
                    denom = context.getString(R.string.str_uion_c);
                    denomColorRes = R.color.colorIon;

                } else if (coin.osmosisAmm()) {
                    denom = coin.osmosisAmmDpDenom();
                    divideDecimal = 18;
                    displayDecimal = 18;
                }
            }

        } else if (chain.equals(BaseChain.GRABRIDGE_MAIN.INSTANCE)) {
            int decimal = WUtil.getGBridgeCoinDecimal(baseData, coin.denom);
            divideDecimal = decimal;
            displayDecimal = decimal;

            if (coin.denom.startsWith("gravity")) {
                final Assets assets = baseData.getAsset(coin.denom);
                if (assets != null) {
                    denom = assets.origin_symbol;
                }
            }
        } else if (chain.equals(BaseChain.INJ_MAIN.INSTANCE)) {
            if (coin.denom.startsWith("peggy")) {
                final Assets assets = baseData.getAsset(coin.denom);
                if (assets != null) {
                    denom = assets.origin_symbol;

                    divideDecimal = assets.decimal;
                    displayDecimal = assets.decimal;
                }
            }
        }

        denomTextView.setTextColor(ContextCompat.getColor(context, denomColorRes));
        denomTextView.setText(denom);
        amountTextView.setText(getDpAmount2(new BigDecimal(coin.amount), divideDecimal, displayDecimal));
    }

    public static void showChainDp(Context context, BaseChain baseChain, CardView cardName, CardView cardBody, CardView cardRewardAddress) {
        int chainBackgroundRes = R.color.colorTransBg;

        if (!baseChain.isTestNet()) {
            chainBackgroundRes = baseChain.getChainBackground();
        }

        if (baseChain.equals(BaseChain.OKEX_MAIN.INSTANCE) || baseChain.equals(BaseChain.BNB_MAIN.INSTANCE) || baseChain.equals(BaseChain.FETCHAI_MAIN.INSTANCE)) {
            cardRewardAddress.setVisibility(View.GONE);
        } else {
            cardRewardAddress.setVisibility(View.VISIBLE);
        }

        int color = ContextExtensionsKt.getColorCompat(context, chainBackgroundRes);

        cardName.setCardBackgroundColor(color);
        cardBody.setCardBackgroundColor(color);
        cardRewardAddress.setCardBackgroundColor(color);
    }

    public static void getChainHint(BaseChain chain, TextView textView) {
        final Context context = textView.getContext();
        int titleRes = R.string.str_unknown;
        if (chain != null) {
            titleRes = chain.getChainTitle();
        }
        textView.setText(String.format("(%s)", context.getString(titleRes)));
    }

    public static boolean isValidChainAddress(BaseChain baseChain, String address) {
        return isValidChainAddress(baseChain, address, true);
    }

    public static boolean isValidChainAddress(BaseChain baseChain, String address, boolean checkValidBech32) {
        boolean result = false;
        if (baseChain != null) {
            if (address.startsWith("0x")) {
                result = WalletUtils.INSTANCE.isValidEthAddress(address) && baseChain.equals(BaseChain.OKEX_MAIN.INSTANCE);
            } else if (!checkValidBech32 || WalletUtils.INSTANCE.isValidBech32(address)) {
                result = address.startsWith(baseChain.getChainAddressPrefix());
            }
        }
        return result;
    }

    public static SpannableString getDpEstAprCommission(BaseData baseData, BaseChain chain, BigDecimal commission) {
        final ChainParam.Params param = baseData.mChainParam;
        BigDecimal apr = BigDecimal.ZERO;
        if (param != null) {
            apr = param.getApr(chain);
        }
        BigDecimal calCommission = BigDecimal.ONE.subtract(commission);
        BigDecimal aprCommission = apr.multiply(calCommission).movePointRight(2);
        return getPercentDp(aprCommission);
    }

    public static SpannableString getDailyReward(Context c, BaseData baseData, BigDecimal commission, BigDecimal delegated, BaseChain chain) {
        final ChainParam.Params param = baseData.mChainParam;
        BigDecimal apr = BigDecimal.ZERO;
        if (param != null) {
            if (BigDecimal.ZERO.compareTo(param.getRealApr(chain)) == 0) {
                apr = param.getApr(chain);
            } else {
                apr = param.getRealApr(chain);
            }
        }
        BigDecimal calCommission = BigDecimal.ONE.subtract(commission);
        BigDecimal aprCommission = apr.multiply(calCommission);
        BigDecimal dayReward = delegated.multiply(aprCommission).divide(new BigDecimal("365"), 0, RoundingMode.DOWN);
        return getDpAmount2(dayReward, chain.getDivideDecimal(), chain.getDisplayDecimal());
    }

    public static SpannableString getMonthlyReward(Context c, BaseData baseData, BigDecimal commission, BigDecimal delegated, BaseChain chain) {
        final ChainParam.Params param = baseData.mChainParam;
        BigDecimal apr = BigDecimal.ZERO;
        if (param != null) {
            if (BigDecimal.ZERO.compareTo(param.getRealApr(chain)) == 0) {
                apr = param.getApr(chain);
            } else {
                apr = param.getRealApr(chain);
            }
        }
        BigDecimal calCommission = BigDecimal.ONE.subtract(commission);
        BigDecimal aprCommission = apr.multiply(calCommission);
        BigDecimal dayReward = delegated.multiply(aprCommission).divide(new BigDecimal("12"), 0, RoundingMode.DOWN);
        return getDpAmount2(dayReward, chain.getDivideDecimal(), chain.getDisplayDecimal());
    }

    public static String getKavaBaseDenom(String denom) {
        if (denom.equalsIgnoreCase(BaseChain.KAVA_MAIN.INSTANCE.getMainDenom())) {
            return BaseChain.KAVA_MAIN.INSTANCE.getMainDenom();
        } else if (denom.equalsIgnoreCase(TOKEN_HARD)) {
            return TOKEN_HARD;
        } else if (denom.equalsIgnoreCase(TOKEN_USDX)) {
            return TOKEN_USDX;
        } else if (denom.equalsIgnoreCase(TOKEN_SWP)) {
            return TOKEN_SWP;
        } else if (denom.equalsIgnoreCase(TOKEN_HTLC_KAVA_BNB)) {
            return "bnb";
        } else if (denom.equalsIgnoreCase(TOKEN_HTLC_KAVA_XRPB) || denom.equalsIgnoreCase("xrbp")) {
            return "xrp";
        } else if (denom.equalsIgnoreCase(TOKEN_HTLC_KAVA_BUSD)) {
            return "busd";
        } else if (denom.contains("btc")) {
            return "btc";
        }
        return "";
    }

    public static BigDecimal kavaTokenDollorValue(BaseData baseData, String denom, BigDecimal amount) {
        int dpDecimal = WUtil.getKavaCoinDecimal(baseData, denom);
        Map<String, kava.pricefeed.v1beta1.QueryOuterClass.CurrentPriceResponse> prices = baseData.mKavaTokenPrice;
        if (denom.equals("hard") && prices.get("hard:usd") != null) {
            BigDecimal price = new BigDecimal(prices.get("hard:usd").getPrice());
            return amount.movePointLeft(dpDecimal).multiply(price);

        } else if (denom.contains("btc") && prices.get("btc:usd") != null) {
            BigDecimal price = new BigDecimal(prices.get("btc:usd").getPrice());
            return amount.movePointLeft(dpDecimal).multiply(price);

        } else if (denom.contains("bnb") && prices.get("bnb:usd") != null) {
            BigDecimal price = new BigDecimal(prices.get("bnb:usd").getPrice());
            return amount.movePointLeft(dpDecimal).multiply(price);

        } else if (denom.contains("xrp") && prices.get("xrp:usd") != null) {
            BigDecimal price = new BigDecimal(prices.get("xrp:usd").getPrice());
            return amount.movePointLeft(dpDecimal).multiply(price);

        } else if (denom.contains("usdx") && prices.get("usdx:usd") != null) {
            BigDecimal price = new BigDecimal(prices.get("usdx:usd").getPrice());
            return amount.movePointLeft(dpDecimal).multiply(price);

        } else if (denom.contains("busd") && prices.get("busd:usd") != null) {
            BigDecimal price = new BigDecimal(prices.get("busd:usd").getPrice());
            return amount.movePointLeft(dpDecimal).multiply(price);
        }
        return BigDecimal.ZERO;

    }

    public static String getKavaPriceFeedSymbol(BaseData baseData, String denom) {
        String result = "";
        if (denom != null) {
            if (denom.startsWith("ibc/")) {
                IbcToken ibcToken = baseData.getIbcToken(denom);
                result = ibcToken.display_denom + ":usd";
            } else if (denom.equalsIgnoreCase(BaseChain.KAVA_MAIN.INSTANCE.getMainDenom())) {
                result = "kava";
            } else if (denom.contains("btc")) {
                result = "btc";
            } else {
                result = denom;
            }

            result = result + ":usd";
        }
        return result;
    }

    public static BigDecimal getKavaPriceFeed(BaseData baseData, String denom) {
        String feedSymbol = getKavaPriceFeedSymbol(baseData, denom);
        if (baseData.mKavaTokenPrice.get(feedSymbol) == null) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(baseData.mKavaTokenPrice.get(feedSymbol).getPrice()).movePointLeft(18);
    }

    public static BigDecimal convertTokenToKava(BaseData baseData, WalletBalance balance, PriceProvider priceProvider) {
        String denom = balance.getDenom();
        BigDecimal tokenAmount = balance.getBalanceAmount(); // baseData.getAvailable(denom).add(baseData.getVesting(denom));
        BigDecimal totalTokenValue = kavaTokenDollorValue(baseData, denom, tokenAmount);
        return totalTokenValue.movePointRight(6).divide(perUsdValue(baseData, BaseChain.KAVA_MAIN.INSTANCE.getMainDenom(), priceProvider), 6, RoundingMode.DOWN);
    }

    public static BigDecimal okExTokenDollorValue(BaseData baseData, OkToken okToken, BigDecimal amount) {
        if (okToken != null && okToken.original_symbol != null) {
            if (okToken.original_symbol.equals("usdt") || okToken.original_symbol.equals("usdc") || okToken.original_symbol.equals("usdk")) {
                return amount;

            } else if (okToken.original_symbol.equals("okb") && baseData.mOKBPrice != null) {
                return amount.multiply(baseData.mOKBPrice);

            } else if (baseData.mOkTickersList != null) {
                //TODO display with ticker update!
                ArrayList<OkTicker> tickers = baseData.mOkTickersList.data;
                return BigDecimal.ZERO;
            }
        }
        return BigDecimal.ZERO;
    }

    public static BigDecimal convertTokenToOkt(WalletBalance balance, BaseData baseData, String denom, PriceProvider priceProvider) {
        OkToken okToken = baseData.okToken(denom);
        if (okToken != null) {
            BigDecimal tokenAmount = balance.getDelegatableAmount();
            BigDecimal totalTokenValue = okExTokenDollorValue(baseData, okToken, tokenAmount);
            return totalTokenValue.divide(perUsdValue(baseData, BaseChain.OKEX_MAIN.INSTANCE.getMainDenom(), priceProvider), 18, RoundingMode.DOWN);
        }
        return BigDecimal.ZERO;
    }

    public static BigDecimal convertTokenToOkt(BaseActivity baseActivity, BaseData baseData, String denom, PriceProvider priceProvider) {
        OkToken okToken = baseData.okToken(denom);
        if (okToken != null) {
            final WalletBalance balance = baseActivity.getFullBalance(denom);
            BigDecimal tokenAmount = balance.getDelegatableAmount();
            BigDecimal totalTokenValue = okExTokenDollorValue(baseData, okToken, tokenAmount);
            return totalTokenValue.divide(perUsdValue(baseData, BaseChain.OKEX_MAIN.INSTANCE.getMainDenom(), priceProvider), 18, RoundingMode.DOWN);
        }
        return BigDecimal.ZERO;
    }


    public static SpannableString getDpRawDollor(Context c, String price, int scale) {
        BigDecimal mPrice = new BigDecimal(price);
        SpannableString result;
        result = new SpannableString("$ " + getDecimalFormat(scale).format(mPrice));
        result.setSpan(new RelativeSizeSpan(0.8f), result.length() - scale, result.length(), SPAN_INCLUSIVE_INCLUSIVE);
        return result;
    }

    public static SpannableString getDpRawDollor(Context c, BigDecimal price, int scale) {
        SpannableString result;
        result = new SpannableString("$ " + getDecimalFormat(scale).format(price));
        result.setSpan(new RelativeSizeSpan(0.8f), result.length() - scale, result.length(), SPAN_INCLUSIVE_INCLUSIVE);
        return result;
    }

    public static SpannableString getPercentDp(BigDecimal input) {
        return getDpString(input.setScale(2, RoundingMode.DOWN).toPlainString() + "%", 3);
    }

    public static SpannableString getPercentDp(BigDecimal input, int scale) {
        return getDpString(input.setScale(scale, RoundingMode.DOWN).toPlainString() + "%", scale + 1);
    }


    //Token & Price

    public static BigDecimal valueChange(@Nullable Price price) {
        if (price != null) {
            return price.priceChange();
        }
        return BigDecimal.ZERO.setScale(2, RoundingMode.FLOOR);
    }

    public static SpannableString dpValueChange(@Nullable Price price) {
        return getDpString(valueChange(price).toPlainString() + "% (24h)", 9);
    }

    public static BigDecimal perUsdValue(BaseData baseData, String denom, PriceProvider priceProvider) {
        if (denom.contains("gamm/pool/")) {
            BalancerPool.Pool pool = baseData.getOsmosisPoolByDenom(denom);
            return WUtil.getOsmoLpTokenPerUsdPrice(baseData, pool, priceProvider);
        }
        if (denom.startsWith("pool") && denom.length() >= 68) {
            ChainParam.GdexStatus pool = baseData.getParamGravityPoolByDenom(denom);
            return WUtil.getParamGdexLpTokenPerUsdPrice(baseData, pool, priceProvider);
        }
        if (denom.equals(TOKEN_EMONEY_EUR) || denom.equals(TOKEN_EMONEY_CHF) || denom.equals(TOKEN_EMONEY_DKK) ||
                denom.equals(TOKEN_EMONEY_NOK) || denom.equals(TOKEN_EMONEY_SEK)) {
            final Price usdtPrice = priceProvider.getPrice("usdt");
            if (usdtPrice != null && usdtPrice.prices != null) {
                for (Price.Prices price : usdtPrice.prices) {
                    if (price.currency.equalsIgnoreCase(denom.substring(1))) {
                        return BigDecimal.ONE.divide(new BigDecimal(price.current_price), 18, RoundingMode.DOWN);
                    }
                }
            }
        }
        final Price denomPrice = priceProvider.getPrice(denom);
        if (denomPrice != null) {
            return denomPrice.currencyPrice("usd").setScale(18, RoundingMode.DOWN);
        }
        return BigDecimal.ZERO.setScale(18, RoundingMode.DOWN);
    }

    public static BigDecimal usdValue(BaseData baseData, String denom, BigDecimal amount, int divider, PriceProvider priceProvider) {
        return perUsdValue(baseData, denom, priceProvider).multiply(amount).movePointLeft(divider).setScale(3, RoundingMode.DOWN);
    }

    public static BigDecimal perUserCurrencyValue(BaseData baseData, Currency currency, String denom, PriceProvider priceProvider) {
        if (currency == Currency.USD.INSTANCE) {
            return perUsdValue(baseData, denom, priceProvider);
        }
        final Price usdtPrice = priceProvider.getPrice("usdt");
        if (usdtPrice != null) {
            final BigDecimal currentPrice = usdtPrice.currencyPrice(currency.getTitle().toLowerCase());
            return perUsdValue(baseData, denom, priceProvider).multiply(currentPrice).setScale(3, RoundingMode.DOWN);
        }
        return BigDecimal.ZERO.setScale(3, RoundingMode.DOWN);
    }

    public static SpannableString dpPerUserCurrencyValue(BaseData baseData, Currency currency, String denom, PriceProvider priceProvider) {
        BigDecimal totalValue = perUserCurrencyValue(baseData, currency, denom, priceProvider);
        final String formatted = currency.getSymbol() + " " + getDecimalFormat(3).format(totalValue);
        return dpCurrencyValue(formatted, 3);
    }

    public static BigDecimal userCurrencyValue(BaseData baseData, Currency currency, String denom, BigDecimal amount, int divider, PriceProvider priceProvider) {
        return perUserCurrencyValue(baseData, currency, denom, priceProvider).multiply(amount).movePointLeft(divider).setScale(3, RoundingMode.DOWN);
    }

    public static SpannableString dpUserCurrencyValue(BaseData baseData, Currency currency, String denom, BigDecimal amount, int divider, PriceProvider priceProvider) {
        BigDecimal totalValue = userCurrencyValue(baseData, currency, denom, amount, divider, priceProvider);
        final String formatted = currency.getSymbol() + " " + getDecimalFormat(3).format(totalValue);
        return dpCurrencyValue(formatted, 3);
    }

    public static BigDecimal allAssetToUserCurrency(BaseChain baseChain, Currency currency, BaseData baseData, List<WalletBalance> balances, PriceProvider priceProvider) {
        BigDecimal totalValue = BigDecimal.ZERO;
        if (baseChain.isGRPC()) {
            for (WalletBalance balance : balances) {
                if (balance.getDenom().equals(baseChain.getMainDenom())) {
                    BigDecimal amount = balance.getBalanceAmount().add(baseData.getAllMainAsset(balance.getDenom()));   //TODO: add vesting
                    BigDecimal assetValue = userCurrencyValue(baseData, currency, balance.getDenom(), amount, baseChain.getDivideDecimal(), priceProvider);
                    totalValue = totalValue.add(assetValue);
                } else if (baseChain.equals(BaseChain.COSMOS_MAIN.INSTANCE) && balance.getDenom().startsWith("pool")) {
                    BigDecimal amount = balance.getBalanceAmount();
                    BigDecimal assetValue = userCurrencyValue(baseData, currency, balance.getDenom(), amount, 6, priceProvider);
                    totalValue = totalValue.add(assetValue);
                } else if (baseChain.equals(BaseChain.OSMOSIS_MAIN.INSTANCE) && balance.getDenom().equals(TOKEN_ION)) {
                    BigDecimal amount = balance.getBalanceAmount();
                    BigDecimal assetValue = userCurrencyValue(baseData, currency, balance.getDenom(), amount, 6, priceProvider);
                    totalValue = totalValue.add(assetValue);
                } else if (baseChain.equals(BaseChain.OSMOSIS_MAIN.INSTANCE) && balance.getDenom().contains("gamm/pool")) {
                    BigDecimal amount = balance.getBalanceAmount();
                    BigDecimal assetValue = userCurrencyValue(baseData, currency, balance.getDenom(), amount, 18, priceProvider);
                    totalValue = totalValue.add(assetValue);
                } else if (baseChain.equals(BaseChain.SIF_MAIN.INSTANCE) && balance.getDenom().startsWith("c")) {
                    BigDecimal amount = balance.getBalanceAmount();
                    int decimal = WUtil.getSifCoinDecimal(baseData, balance.getDenom());
                    BigDecimal assetValue = userCurrencyValue(baseData, currency, balance.getDenom().substring(1), amount, decimal, priceProvider);
                    totalValue = totalValue.add(assetValue);
                } else if (baseChain.equals(BaseChain.EMONEY_MAIN.INSTANCE) || balance.getDenom().startsWith("e")) {
                    BigDecimal available = balance.getBalanceAmount();
                    totalValue = totalValue.add(userCurrencyValue(baseData, currency, balance.getDenom(), available, 6, priceProvider));
                } else if (baseChain.equals(BaseChain.KAVA_MAIN.INSTANCE) && !balance.isIbc()) {
                    BigDecimal amount = balance.getBalanceAmount();
//                    amount = amount.add(baseData.getVesting(balance.getSymbol())); // TODO Vesting
                    String kavaDenom = WDp.getKavaBaseDenom(balance.getDenom());
                    int kavaDecimal = WUtil.getKavaCoinDecimal(baseData, balance.getDenom());
                    BigDecimal assetValue = userCurrencyValue(baseData, currency, kavaDenom, amount, kavaDecimal, priceProvider);
                    totalValue = totalValue.add(assetValue);
                } else if (baseChain.equals(BaseChain.GRABRIDGE_MAIN.INSTANCE) && balance.getDenom().startsWith("gravity")) {
                    Assets assets = baseData.getAsset(balance.getDenom());
                    BigDecimal available = balance.getBalanceAmount();
                    totalValue = totalValue.add(userCurrencyValue(baseData, currency, assets.origin_symbol, available, assets.decimal, priceProvider));
                } else if (baseChain.equals(BaseChain.INJ_MAIN.INSTANCE) && balance.getDenom().startsWith("peggy")) {
                    Assets assets = baseData.getAsset(balance.getDenom());
                    BigDecimal available = balance.getBalanceAmount();
                    totalValue = totalValue.add(userCurrencyValue(baseData, currency, assets.origin_symbol, available, assets.decimal, priceProvider));
                } else if (balance.isIbc()) {
                    BigDecimal amount = balance.getBalanceAmount();
                    IbcToken ibcToken = baseData.getIbcToken(balance.getDenom());
                    if (ibcToken != null && ibcToken.auth) {
                        BigDecimal assetValue = userCurrencyValue(baseData, currency, ibcToken.base_denom, amount, ibcToken.decimal, priceProvider);
                        totalValue = totalValue.add(assetValue);
                    }
                }
            }

            if (baseData.getCw20sGrpc(baseChain).size() > 0) {
                for (Cw20Assets assets : baseData.getCw20sGrpc(baseChain)) {
                    BigDecimal amount = assets.getAmount();
                    totalValue = totalValue.add(userCurrencyValue(baseData, currency, assets.denom, amount, assets.decimal, priceProvider));
                }
            }
        } else if (baseChain.equals(BaseChain.BNB_MAIN.INSTANCE)) {
            for (WalletBalance balance : balances) {
                if (balance.getDenom().equals(baseChain.getMainDenom())) {
                    BigDecimal amount = balance.getTotalAmount();
                    BigDecimal assetValue = userCurrencyValue(baseData, currency, baseChain.getMainDenom(), amount, baseChain.getDivideDecimal(), priceProvider);
                    totalValue = totalValue.add(assetValue);
                } else {
                    BigDecimal amount = balance.getTotalAmount();
                    BigDecimal convertAmount = WUtil.getBnbConvertAmount(baseData, balance.getDenom(), amount);
                    BigDecimal assetValue = userCurrencyValue(baseData, currency, baseChain.getMainDenom(), convertAmount, baseChain.getDivideDecimal(), priceProvider);
                    totalValue = totalValue.add(assetValue);
                }
            }

        } else if (baseChain.equals(BaseChain.OKEX_MAIN.INSTANCE)) {
            for (WalletBalance balance : balances) {
                if (balance.getDenom().equals(baseChain.getMainDenom())) {
                    BigDecimal amount = balance.getDelegatableAmount().add(baseData.getAllExToken(balance.getDenom()));
                    BigDecimal assetValue = userCurrencyValue(baseData, currency, baseChain.getMainDenom(), amount, baseChain.getDivideDecimal(), priceProvider);
                    totalValue = totalValue.add(assetValue);
                } else {
                    BigDecimal convertAmount = convertTokenToOkt(balance, baseData, balance.getDenom(), priceProvider);
                    BigDecimal assetValue = userCurrencyValue(baseData, currency, baseChain.getMainDenom(), convertAmount, baseChain.getDivideDecimal(), priceProvider);
                    totalValue = totalValue.add(assetValue);
                }
            }
        } else {
            for (WalletBalance balance : balances) {
                if (balance.getDenom().equals(baseChain.getMainDenom())) {
                    BigDecimal amount = balance.getDelegatableAmount().add(baseData.getAllMainAssetOld(balance.getDenom()));
                    BigDecimal assetValue = userCurrencyValue(baseData, currency, balance.getDenom(), amount, baseChain.getDivideDecimal(), priceProvider);
                    totalValue = totalValue.add(assetValue);
                }
            }

        }
        return totalValue;
    }


    public static SpannableString dpAllAssetValueUserCurrency(BaseChain baseChain, Currency currency, BaseData baseData, List<WalletBalance> balances, PriceProvider priceProvider) {
        BigDecimal totalValue = allAssetToUserCurrency(baseChain, currency, baseData, balances, priceProvider);
        final String formatted = currency.getSymbol() + " " + getDecimalFormat(3).format(totalValue);
        return dpCurrencyValue(formatted, 3);
    }

    public static SpannableString dpCurrencyValue(String input, int dpPoint) {
        return getDpString(input, dpPoint);
    }

    public static SpannableString getSelfBondRate(String total, String self) {
        BigDecimal result = new BigDecimal(self).multiply(new BigDecimal("100")).divide(new BigDecimal(total), 2, RoundingMode.DOWN);
        return getPercentDp(result);
    }

    public static SpannableString getCommissionRate(String rate) {
        BigDecimal result = new BigDecimal(rate).multiply(new BigDecimal("100")).setScale(2, RoundingMode.DOWN);
        return getPercentDp(result);
    }

    public static SpannableString getDpCommissionGrpcRate(@NotNull Staking.Validator validator) {
        BigDecimal result = getCommissionGrpcRate(validator)
                .movePointRight(2).setScale(2, RoundingMode.DOWN);
        return getPercentDp(result);
    }

    public static BigDecimal getCommissionGrpcRate(@NotNull Staking.Validator validator) {
        return new BigDecimal(validator.getCommission().getCommissionRates().getRate()).movePointLeft(18);
    }

    public static DecimalFormat getDecimalFormat(int cnt) {
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat decimalformat = (DecimalFormat) formatter;
        decimalformat.setRoundingMode(RoundingMode.DOWN);

        StringBuilder stringBuilder = new StringBuilder("###,###,###,###,###,###,###,##0");
        if (cnt > 0) {
            if (cnt <= 18) {
                stringBuilder.append('.');
                for (int i = 0; i < cnt; i++) {
                    stringBuilder.append('0');
                }
            } else {
                stringBuilder.append(".000000");
            }
        }
        decimalformat.applyLocalizedPattern(stringBuilder.toString());
        return decimalformat;
    }


    public static String getDpTime(Context c, long time) {
        String result = "??";
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            SimpleDateFormat simpleFormat = new SimpleDateFormat(c.getString(R.string.str_dp_time_format1));
            result = simpleFormat.format(calendar.getTimeInMillis());
        } catch (Exception e) {
        }
        return result;
    }

    public static String getUnbondTime(Context c, BaseData baseData, BaseChain baseChain) {
        String result = "??";
        try {
            if (baseData != null && baseData.mChainParam != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, baseData.mChainParam.getUnbonding(baseChain));
                SimpleDateFormat unbondFormat = new SimpleDateFormat(c.getString(R.string.str_dp_time_format2));
                result = unbondFormat.format(calendar.getTimeInMillis());
            }
        } catch (Exception e) {
        }
        return result + "   " + "(" + baseData.mChainParam.getUnbonding(baseChain) + c.getString(R.string.str_unbonding_days_after);
    }

    public static String getUnbondingTimeleft(long finishTime) {
        String result = "??";
        try {
            long now = Calendar.getInstance().getTimeInMillis();
            long left = finishTime - now;

            if (left >= BaseConstant.CONSTANT_D) {
                result = "(" + (left / BaseConstant.CONSTANT_D) + " days remaining)";
            } else if (left >= BaseConstant.CONSTANT_H) {
                result = "(" + (left / BaseConstant.CONSTANT_H) + " hours remaining)";
            } else if (left >= BaseConstant.CONSTANT_M) {
                result = "(" + (left / BaseConstant.CONSTANT_M) + " minutes remaining)";
            } else {
                return "Soon";
            }

        } catch (Exception e) {
        }

        return result;
    }

    public static long dateToLong(Context c, String rawValue) {
        long result = 0;
        try {
            SimpleDateFormat blockDateFormat = new SimpleDateFormat(c.getString(R.string.str_block_time_format));
            blockDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            result = blockDateFormat.parse(rawValue).getTime();
        } catch (Exception e) {
        }
        return result;
    }

    public static long dateToLong2(Context c, String rawValue) {
        long result = 0;
        try {
            SimpleDateFormat blockDateFormat = new SimpleDateFormat(c.getString(R.string.str_tx_time_format));
            blockDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            result = blockDateFormat.parse(rawValue).getTime();
        } catch (Exception e) {
        }
        return result;
    }

    public static String getTimeformat(Context c, String rawValue) {
        String result = "??";
        try {
            SimpleDateFormat blockDateFormat = new SimpleDateFormat(c.getString(R.string.str_block_time_format));
            SimpleDateFormat myFormat = new SimpleDateFormat(c.getString(R.string.str_dp_time_format1));
            blockDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            result = myFormat.format(blockDateFormat.parse(rawValue));
        } catch (Exception e) {
        }

        return result;
    }

    public static String getTimeTxformat(Context c, String rawValue) {
        String result = "??";
        try {
            SimpleDateFormat blockDateFormat = new SimpleDateFormat(c.getString(R.string.str_tx_time_format));
            SimpleDateFormat myFormat = new SimpleDateFormat(c.getString(R.string.str_dp_time_format1));
            blockDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            result = myFormat.format(blockDateFormat.parse(rawValue));
        } catch (Exception e) {
        }

        return result;
    }

    public static String getTimeVoteformat(Context c, String rawValue) {
        String result = "??";
        try {
            SimpleDateFormat blockDateFormat = new SimpleDateFormat(c.getString(R.string.str_vote_time_format));
            SimpleDateFormat myFormat = new SimpleDateFormat(c.getString(R.string.str_dp_time_format1));
            blockDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            result = myFormat.format(blockDateFormat.parse(rawValue));
        } catch (Exception e) {
        }

        return result;
    }

    public static String getTimeGap(Context c, String rawValue) {
        String result = "";
        try {
            SimpleDateFormat blockDateFormat = new SimpleDateFormat(c.getString(R.string.str_block_time_format));
            blockDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date blockTime = blockDateFormat.parse(rawValue);
            Date nowTime = Calendar.getInstance().getTime();

            long difference = nowTime.getTime() - blockTime.getTime();

            long differenceSeconds = difference / 1000 % 60;
            long differenceMinutes = difference / (60 * 1000) % 60;
            long differenceHours = difference / (60 * 60 * 1000) % 24;
            long differenceDays = difference / (24 * 60 * 60 * 1000);

            if (differenceDays > 1) {
                result = "" + differenceDays + " " + c.getString(R.string.str_day);
            } else if (differenceDays == 1) {
                result = "" + differenceDays + c.getString(R.string.str_d) + " " + differenceHours + c.getString(R.string.str_h);
            } else {
                if (differenceHours > 0) {
                    result = "" + differenceHours + c.getString(R.string.str_h) + " " + differenceMinutes + c.getString(R.string.str_m);
                } else {
                    if (differenceMinutes > 0) {
                        result = "" + differenceMinutes + c.getString(R.string.str_m) + " " + differenceSeconds + c.getString(R.string.str_s);
                    } else {
                        result = differenceSeconds + c.getString(R.string.str_s);
                    }
                }

            }

        } catch (Exception e) {
        }

        return "(" + result + " " + c.getString(R.string.str_ago) + ")";
    }

    public static String getTimeTxGap(Context c, String rawValue) {
        String result = "";
        try {
            SimpleDateFormat blockDateFormat = new SimpleDateFormat(c.getString(R.string.str_tx_time_format));
            blockDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date blockTime = blockDateFormat.parse(rawValue);
            Date nowTime = Calendar.getInstance().getTime();

            long difference = nowTime.getTime() - blockTime.getTime();

            long differenceSeconds = difference / 1000 % 60;
            long differenceMinutes = difference / (60 * 1000) % 60;
            long differenceHours = difference / (60 * 60 * 1000) % 24;
            long differenceDays = difference / (24 * 60 * 60 * 1000);

            if (differenceDays > 1) {
                result = "" + differenceDays + " " + c.getString(R.string.str_day);
            } else if (differenceDays == 1) {
                result = "" + differenceDays + c.getString(R.string.str_d) + " " + differenceHours + c.getString(R.string.str_h);
            } else {
                if (differenceHours > 0) {
                    result = "" + differenceHours + c.getString(R.string.str_h) + " " + differenceMinutes + c.getString(R.string.str_m);
                } else {
                    if (differenceMinutes > 0) {
                        result = "" + differenceMinutes + c.getString(R.string.str_m) + " " + differenceSeconds + c.getString(R.string.str_s);
                    } else {
                        result = differenceSeconds + c.getString(R.string.str_s);
                    }
                }

            }

        } catch (Exception e) {
        }

        return "(" + result + " " + c.getString(R.string.str_ago) + ")";
    }

    public static String getTimeTxGap(Context c, long rawValue) {
        String result = "";
        try {
            Date blockTime = new Date(rawValue);
            Date nowTime = Calendar.getInstance().getTime();

            long difference = nowTime.getTime() - blockTime.getTime();

            long differenceSeconds = difference / 1000 % 60;
            long differenceMinutes = difference / (60 * 1000) % 60;
            long differenceHours = difference / (60 * 60 * 1000) % 24;
            long differenceDays = difference / (24 * 60 * 60 * 1000);

            if (differenceDays > 1) {
                result = "" + differenceDays + " " + c.getString(R.string.str_day);
            } else if (differenceDays == 1) {
                result = "" + differenceDays + c.getString(R.string.str_d) + " " + differenceHours + c.getString(R.string.str_h);
            } else {
                if (differenceHours > 0) {
                    result = "" + differenceHours + c.getString(R.string.str_h) + " " + differenceMinutes + c.getString(R.string.str_m);
                } else {
                    if (differenceMinutes > 0) {
                        result = "" + differenceMinutes + c.getString(R.string.str_m) + " " + differenceSeconds + c.getString(R.string.str_s);
                    } else {
                        result = differenceSeconds + c.getString(R.string.str_s);
                    }
                }
            }

        } catch (Exception e) {
        }

        return "(" + result + " " + c.getString(R.string.str_ago) + ")";
    }


    public static String cTimeString() {
        Calendar c = Calendar.getInstance();
        return "" + c.getTimeInMillis();
    }

    public static String threeMonthAgoTimeString() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -3);
        return "" + c.getTimeInMillis();
    }

    public static int getChainColor(Context c, BaseChain chain) {
        int colorResId = R.color.colorGray0;
        if (chain != null) {
            colorResId = chain.getChainColor();
        }
        return ContextCompat.getColor(c, colorResId);
    }

    public static ColorStateList getChainTintColor(Context c, BaseChain chain) {
        int colorResId = R.color.colorTransBg;
        if (chain != null) {
            colorResId = chain.getChainColor();
        }
        return ContextCompat.getColorStateList(c, colorResId);
    }

    public static int getChainBgColor(Context c, BaseChain chain) {
        int colorResId = chain != null ? chain.getChainBackground() : R.color.colorTransBg;
        return ContextCompat.getColor(c, colorResId);
    }

    public static ColorStateList getTabColor(Context c, BaseChain chain) {
        int colorResId = chain != null ? chain.getChainTabColor() : R.color.color_tab_myvalidator;
        return ContextCompat.getColorStateList(c, colorResId);
    }

    public static void DpMainDenom(BaseChain chain, TextView textview) {
        DpMainDenom(chain.getChainName(), textview);
    }

    public static void DpMainDenom(String chainName, TextView textView) {
        BaseChain chain = BaseChain.getChain(chainName);
        if (chain != null) {
            textView.setTextColor(ContextCompat.getColor(textView.getContext(), chain.getChainColor()));
            textView.setText(textView.getContext().getString(chain.getSymbolTitle()));
        }
    }

    public static int tokenDivideDecimal(BaseData baseData, BaseChain baseChain, String denom) {
        String mainDenom = baseChain.getMainDenom();
        if (baseChain.isGRPC()) {
            if (denom.equalsIgnoreCase(mainDenom)) {
                return baseChain.getDivideDecimal();
            }
            if (denom.startsWith("ibc/")) {
                return WUtil.getIbcDecimal(baseData, denom);
            }
            if (baseChain.equals(BaseChain.COSMOS_MAIN.INSTANCE)) {
                return WUtil.getCosmosCoinDecimal(baseData, denom);
            } else if (baseChain.equals(BaseChain.OSMOSIS_MAIN.INSTANCE)) {
                return WUtil.getOsmosisCoinDecimal(baseData, denom);
            } else if (baseChain.equals(BaseChain.SIF_MAIN.INSTANCE)) {
                return WUtil.getSifCoinDecimal(baseData, denom);
            } else if (baseChain.equals(BaseChain.GRABRIDGE_MAIN.INSTANCE)) {
                return WUtil.getGBridgeCoinDecimal(baseData, denom);
            } else if (baseChain.equals(BaseChain.INJ_MAIN.INSTANCE)) {
                return WUtil.getInjCoinDecimal(baseData, denom);
            }
        } else {
            return 6;
        }
        return 6;
    }

    public static int mainDivideDecimal(String denom) {
        BaseChain chain = BaseChain.Companion.getChainByDenom(denom);
        if (chain != null) {
            return chain.getDivideDecimal();
        } else {
            return 6;
        }
    }

    public static int mainDisplayDecimal(String denom) {
        BaseChain chain = BaseChain.Companion.getChainByDenom(denom);
        if (chain != null) {
            return chain.getDisplayDecimal();
        } else {
            return 6;
        }
    }

    public static int getDpRiskColor(Context c, BigDecimal riskRate) {
        if (riskRate.longValue() <= 50) {
            return ContextCompat.getColor(c, R.color.colorCdpSafe);
        } else if (riskRate.longValue() < 80) {
            return ContextCompat.getColor(c, R.color.colorCdpStable);
        } else {
            return ContextCompat.getColor(c, R.color.colorCdpDanger);
        }
    }

    public static void DpRiskRate(Context c, BigDecimal riskRate, TextView textView, ImageView imageview) {
        textView.setText(WDp.getDpAmount2(riskRate, 0, 2));
        if (riskRate.floatValue() <= 50) {
            textView.setTextColor(ContextCompat.getColor(c, R.color.colorCdpSafe));
            if (imageview != null) {
                imageview.setImageResource(R.drawable.img_safe);
            }

        } else if (riskRate.floatValue() < 80) {
            textView.setTextColor(ContextCompat.getColor(c, R.color.colorCdpStable));
            if (imageview != null) {
                imageview.setImageResource(R.drawable.img_stable);
            }

        } else {
            textView.setTextColor(ContextCompat.getColor(c, R.color.colorCdpDanger));
            if (imageview != null) {
                imageview.setImageResource(R.drawable.img_danger);

            }
        }

    }

    public static void DpRiskButton(Context c, BigDecimal riskRate, Button button) {
        if (riskRate.longValue() <= 50) {
            button.setBackgroundResource(R.drawable.btn_score_safe_fill);
            button.setTextColor(ContextCompat.getColor(c, R.color.colorBlack));
            button.setTypeface(null, Typeface.BOLD);
            button.setText(String.format(Locale.ENGLISH, "%s %s", c.getString(R.string.str_safe), riskRate.toPlainString()));

        } else if (riskRate.longValue() < 80) {
            button.setBackgroundResource(R.drawable.btn_score_stable_fill);
            button.setTextColor(ContextCompat.getColor(c, R.color.colorBlack));
            button.setTypeface(null, Typeface.BOLD);
            button.setText(String.format(Locale.ENGLISH, "%s %s", c.getString(R.string.str_stable), riskRate.toPlainString()));

        } else {
            button.setBackgroundResource(R.drawable.btn_score_danger_fill);
            button.setTextColor(ContextCompat.getColor(c, R.color.colorBlack));
            button.setTypeface(null, Typeface.BOLD);
            button.setText(String.format(Locale.ENGLISH, "%s %s", c.getString(R.string.str_danger), riskRate.toPlainString()));
        }
    }

    public static void DpRiskButton2(Context c, BigDecimal riskRate, Button button) {
        if (riskRate.longValue() <= 50) {
            button.setBackgroundResource(R.drawable.btn_score_safe_fill);
            button.setTextColor(ContextCompat.getColor(c, R.color.colorBlack));
            button.setTypeface(null, Typeface.BOLD);
            button.setText(R.string.str_safe);

        } else if (riskRate.longValue() < 80) {
            button.setBackgroundResource(R.drawable.btn_score_stable_fill);
            button.setTextColor(ContextCompat.getColor(c, R.color.colorBlack));
            button.setTypeface(null, Typeface.BOLD);
            button.setText(R.string.str_stable);

        } else {
            button.setBackgroundResource(R.drawable.btn_score_danger_fill);
            button.setTextColor(ContextCompat.getColor(c, R.color.colorBlack));
            button.setTypeface(null, Typeface.BOLD);
            button.setText(R.string.str_danger);
        }
    }

    public static void DpRiskRate2(Context c, BigDecimal riskRate, TextView text, TextView rate, LinearLayout layer) {
        rate.setText(WDp.getDpAmount2(riskRate, 0, 2));
        if (riskRate.longValue() <= 50) {
            rate.setText(R.string.str_safe);
            layer.setBackgroundResource(R.drawable.btn_score_safe_fill);

        } else if (riskRate.longValue() < 80) {
            text.setText(R.string.str_stable);
            layer.setBackgroundResource(R.drawable.btn_score_stable_fill);

        } else {
            text.setText(R.string.str_danger);
            layer.setBackgroundResource(R.drawable.btn_score_danger_fill);
        }
    }

    public static void DpRiskRate3(Context c, BigDecimal riskRate, TextView score, TextView rate) {
        score.setText(WDp.getDpAmount2(riskRate, 0, 2));
        if (riskRate.longValue() <= 50) {
            rate.setText(R.string.str_safe);
            rate.setTextColor(ContextCompat.getColor(c, R.color.colorCdpSafe));
            score.setTextColor(ContextCompat.getColor(c, R.color.colorCdpSafe));

        } else if (riskRate.longValue() < 80) {
            rate.setText(R.string.str_stable);
            rate.setTextColor(ContextCompat.getColor(c, R.color.colorCdpStable));
            score.setTextColor(ContextCompat.getColor(c, R.color.colorCdpStable));

        } else {
            rate.setText(R.string.str_danger);
            rate.setTextColor(ContextCompat.getColor(c, R.color.colorCdpDanger));
            score.setTextColor(ContextCompat.getColor(c, R.color.colorCdpDanger));
        }
    }

    public static BigDecimal getCdpGrpcHiddenFee(Context c, BigDecimal outstandingDebt, Genesis.CollateralParam paramCdp, QueryOuterClass.CDPResponse myCdp) {
        BigDecimal result = BigDecimal.ZERO;
        try {
            long now = Calendar.getInstance().getTimeInMillis();
            SimpleDateFormat blockDateFormat = new SimpleDateFormat(c.getString(R.string.str_block_time_format));
            blockDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            long start = blockDateFormat.parse(myCdp.getFeesUpdated().toString()).getTime();
            Long gap = (now - start) / 1000;
            //TODO 냥냥하게 패딩
            gap = gap + 30;

            Double double1 = Double.parseDouble(paramCdp.getStabilityFee());
            Double double2 = gap.doubleValue();

            Double pow = Math.pow(double1, double2);
            result = outstandingDebt.multiply(new BigDecimal(pow.toString())).setScale(0, RoundingMode.UP).subtract(outstandingDebt);
            return result;
        } catch (Exception e) {
            WLog.w("e " + e.getMessage());
        }
        return result;
    }

    // HTLC using
    public static String getDpChainName(Context context, BaseChain chain) {
        return context.getString(chain.getChainTitle());
    }

    // HTLC using
    public static void onDpChain(Context c, BaseChain chain, ImageView imgView, TextView txtView) {
        if (imgView != null) {
            imgView.setImageResource(chain.getChainIcon());
        }
        txtView.setText(c.getString(chain.getChainTitle()));
    }

    public static void onDpSwapChain(Context c, BaseChain chain, ImageView imgView, TextView txtView) {
        if (imgView != null) {
            imgView.setImageResource(chain.getChainIcon());
        }
        if (chain.equals(BaseChain.BNB_MAIN.INSTANCE)) {
            txtView.setText(c.getString(R.string.str_binance));
        } else if (chain.equals(BaseChain.KAVA_MAIN.INSTANCE)) {
            txtView.setText(c.getString(R.string.str_kava));
        }
    }

    public static String getBnbHtlcStatus(Context c, ResBnbSwapInfo resBnbSwapInfo, ResNodeInfo resNodeInfo) {
        if (resBnbSwapInfo == null || resNodeInfo == null) {
            return "-";
        }
        if (resBnbSwapInfo.status == BNB_STATUS_REFUNDED) {
            return c.getString(R.string.str_bep3_status_refunded);

        } else if (resBnbSwapInfo.status == BNB_STATUS_COMPLETED) {
            return c.getString(R.string.str_bep3_status_completed);

        } else if (resBnbSwapInfo.status == BNB_STATUS_OPEN && resBnbSwapInfo.expireHeight < resNodeInfo.getCHeight()) {
            return c.getString(R.string.str_bep3_status_expired);

        }
        return c.getString(R.string.str_bep3_status_open);

    }

    public static ArrayList<Coin> getCoins(Object amount) {
        ArrayList<Coin> result = new ArrayList<>();
        try {
            Coin temp = new Gson().fromJson(new Gson().toJson(amount), Coin.class);
            result.add(temp);

        } catch (Exception e) {
        }

        try {
            result = new Gson().fromJson(new Gson().toJson(amount), new TypeToken<List<Coin>>() {
            }.getType());
        } catch (Exception e) {
        }
        return result;
    }

    public static BigDecimal geTallySum(ResProposal proposal) {
        return new BigDecimal(proposal.voteMeta.yes_amount).add(new BigDecimal(proposal.voteMeta.no_amount)).add(new BigDecimal(proposal.voteMeta.no_with_veto_amount)).add(new BigDecimal(proposal.voteMeta.abstain_amount));
    }

    public static BigDecimal getYesPer(ResProposal proposal) {
        if (geTallySum(proposal).equals(BigDecimal.ZERO) || (new BigDecimal(proposal.voteMeta.yes_amount).longValue() == 0)) {
            return BigDecimal.ZERO.setScale(2);
        }
        return new BigDecimal(proposal.voteMeta.yes_amount).movePointRight(2).divide(geTallySum(proposal), 2, RoundingMode.HALF_UP);
    }

    public static BigDecimal getNoPer(ResProposal proposal) {
        if (geTallySum(proposal).equals(BigDecimal.ZERO) || (new BigDecimal(proposal.voteMeta.no_amount).longValue() == 0)) {
            return BigDecimal.ZERO.setScale(2);
        }
        return new BigDecimal(proposal.voteMeta.no_amount).movePointRight(2).divide(geTallySum(proposal), 2, RoundingMode.HALF_UP);
    }

    public static BigDecimal getAbstainPer(ResProposal proposal) {
        if (geTallySum(proposal).equals(BigDecimal.ZERO) || (new BigDecimal(proposal.voteMeta.abstain_amount).longValue() == 0)) {
            return BigDecimal.ZERO.setScale(2);
        }
        return new BigDecimal(proposal.voteMeta.abstain_amount).movePointRight(2).divide(geTallySum(proposal), 2, RoundingMode.HALF_UP);
    }

    public static BigDecimal getVetoPer(ResProposal proposal) {
        if (geTallySum(proposal).equals(BigDecimal.ZERO) || (new BigDecimal(proposal.voteMeta.no_with_veto_amount).longValue() == 0)) {
            return BigDecimal.ZERO.setScale(2);
        }
        return new BigDecimal(proposal.voteMeta.no_with_veto_amount).movePointRight(2).divide(geTallySum(proposal), 2, RoundingMode.HALF_UP);
    }

    public static BigDecimal getTurnout(BaseChain baseCahin, BaseData baseData, ResProposal proposal) {
        BigDecimal result = BigDecimal.ZERO;
        if (baseData != null && baseData.mChainParam != null) {
            if (geTallySum(proposal).equals(BigDecimal.ZERO)) {
                return BigDecimal.ZERO.setScale(2);

            } else {
                BigDecimal bonded = baseData.mChainParam.getBondedAmount(baseCahin);
                return geTallySum(proposal).movePointRight(2).divide(bonded, 2, RoundingMode.HALF_UP);
            }
        }
        return result;
    }

    public static BigDecimal onParseFee(ServiceOuterClass.GetTxResponse response) {
        BigDecimal result = BigDecimal.ZERO;
        if (response.getTx().getAuthInfo().getFee().getAmountCount() > 0) {
            return new BigDecimal(response.getTx().getAuthInfo().getFee().getAmount(0).getAmount());
        }
        return result;
    }

    public static ArrayList<Coin> onParseAutoReward(ServiceOuterClass.GetTxResponse response, String Addr, int position) {
        ArrayList<Coin> result = new ArrayList<>();
        if (response.getTxResponse().getLogsCount() > 0 && response.getTxResponse().getLogs(position) != null) {
            for (Abci.StringEvent event : response.getTxResponse().getLogs(position).getEventsList()) {
                if (event.getType().equals("transfer")) {
                    for (int i = 0; i < event.getAttributesList().size(); i++) {
                        if (event.getAttributes(i).getKey().equals("recipient") && event.getAttributes(i).getValue().equals(Addr)) {
                            for (int j = i; j < event.getAttributesList().size(); j++) {
                                if (event.getAttributes(j).getKey().equals("amount") && event.getAttributes(j).getValue() != null) {
                                    String rawValue = event.getAttributes(j).getValue();
                                    for (String rawCoin : rawValue.split(",")) {
                                        Pattern p = Pattern.compile("([0-9])+");
                                        Matcher m = p.matcher(rawCoin);
                                        if (m.find()) {
                                            String amount = m.group();
                                            String denom = rawCoin.substring(m.end());
                                            result.add(new Coin(denom, amount));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public static BigDecimal onParseStakeReward(BaseChain baseChain, ServiceOuterClass.GetTxResponse response, String valAddr, int position) {
        BigDecimal result = BigDecimal.ZERO;
        if (response.getTxResponse().getLogsCount() > 0 && response.getTxResponse().getLogs(position) != null) {
            for (Abci.StringEvent event : response.getTxResponse().getLogs(position).getEventsList()) {
                if (event.getType().equals("withdraw_rewards")) {
                    for (int i = 0; i < event.getAttributesList().size(); i++) {
                        if (event.getAttributes(i).getKey().equals("validator") && event.getAttributes(i).getValue().equals(valAddr)) {
                            String rawValue = event.getAttributes(i - 1).getValue();
                            if (rawValue != null) {
                                for (String rawCoin : rawValue.split(",")) {
                                    if (rawCoin.contains(baseChain.getMainDenom())) {
                                        result = result.add(new BigDecimal(rawCoin.replaceAll("[^0-9]", "")));
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public static ArrayList<Coin> onParseCommission(ServiceOuterClass.GetTxResponse response, int position) {
        ArrayList<Coin> result = new ArrayList<>();
        if (response.getTxResponse().getLogsCount() > 0 && response.getTxResponse().getLogs(position) != null) {
            for (Abci.StringEvent event : response.getTxResponse().getLogs(position).getEventsList()) {
                if (event.getType().equals("withdraw_commission")) {
                    for (int i = 0; i < event.getAttributesList().size(); i++) {
                        if (event.getAttributes(i).getKey().equals("amount")) {
                            String rawValue = event.getAttributes(i).getValue();
                            for (String rawCoin : rawValue.split(",")) {
                                Pattern p = Pattern.compile("([0-9])+");
                                Matcher m = p.matcher(rawCoin);
                                if (m.find()) {
                                    String amount = m.group();
                                    String denom = rawCoin.substring(m.end());
                                    result.add(new Coin(denom, amount));
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public static ArrayList<Coin> onParseKavaIncentiveGrpc(ServiceOuterClass.GetTxResponse tx, int position) {
        ArrayList<Coin> result = new ArrayList<>();
        if (tx.getTxResponse().getLogsList() != null && tx.getTxResponse().getLogsCount() > position && tx.getTxResponse().getLogs(position) != null) {
            for (Abci.StringEvent event : tx.getTxResponse().getLogs(position).getEventsList()) {
                if (event.getType().equalsIgnoreCase("claim_reward")) {
                    for (int i = 0; i < event.getAttributesList().size(); i++) {
                        if (event.getAttributes(i).getKey().equalsIgnoreCase("claim_amount")) {
                            String rawValue = event.getAttributes(i).getValue();
                            for (String rawCoin : rawValue.split(",")) {
                                Pattern p = Pattern.compile("([0-9])+");
                                Matcher m = p.matcher(rawCoin);
                                if (m.find()) {
                                    String amount = m.group();
                                    String denom = rawCoin.substring(m.end());
                                    result.add(new Coin(denom, amount));
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public static long onParsePeriodicUnLockTime(Vesting.PeriodicVestingAccount vestingAccount, int position) {
        long result = vestingAccount.getStartTime();
        for (int i = 0; i <= position; i++) {
            result = result + vestingAccount.getVestingPeriods(i).getLength();
        }
        return result * 1000;
    }

    public static ArrayList<Vesting.Period> onParsePeriodicRemainVestings(Vesting.PeriodicVestingAccount vestingAccount) {
        ArrayList<Vesting.Period> result = new ArrayList<>();
        long cTime = Calendar.getInstance().getTime().getTime();
        for (int i = 0; i < vestingAccount.getVestingPeriodsCount(); i++) {
            long unlockTime = onParsePeriodicUnLockTime(vestingAccount, i);
            if (cTime < unlockTime) {
                result.add(Vesting.Period.newBuilder().setLength(unlockTime).addAllAmount(vestingAccount.getVestingPeriods(i).getAmountList()).build());
            }
        }
        return result;
    }

    public static ArrayList<Vesting.Period> onParsePeriodicRemainVestingsByDenom(Vesting.PeriodicVestingAccount vestingAccount, String denom) {
        ArrayList<Vesting.Period> result = new ArrayList<>();
        for (Vesting.Period vp : onParsePeriodicRemainVestings(vestingAccount)) {
            for (CoinOuterClass.Coin coin : vp.getAmountList()) {
                if (coin.getDenom().equals(denom)) {
                    result.add(vp);
                }
            }
        }
        return result;
    }

    public static BigDecimal onParsePeriodicRemainVestingsAmountByDenom(Vesting.PeriodicVestingAccount vestingAccount, String denom) {
        BigDecimal result = BigDecimal.ZERO;
        ArrayList<Vesting.Period> vps = onParsePeriodicRemainVestingsByDenom(vestingAccount, denom);
        for (Vesting.Period vp : vps) {
            for (CoinOuterClass.Coin coin : vp.getAmountList()) {
                if (coin.getDenom().equals(denom)) {
                    result = result.add(new BigDecimal(coin.getAmount()));
                }
            }
        }
        return result;
    }

    public static BigDecimal getAmountVp(Vesting.Period vp, String denom) {
        BigDecimal result = BigDecimal.ZERO;
        for (CoinOuterClass.Coin coin : vp.getAmountList()) {
            if (coin.getDenom().equals(denom)) {
                return new BigDecimal(coin.getAmount());
            }
        }
        return result;
    }
}
