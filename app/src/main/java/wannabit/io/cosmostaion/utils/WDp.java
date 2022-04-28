package wannabit.io.cosmostaion.utils;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;
import static wannabit.io.cosmostaion.base.BaseConstant.AKASH_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.AKASH_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.ALTHEA_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.AXELAR_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.AXELAR_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.BAND_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.BAND_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.BITCANNA_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.BITCANNA_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.BITSONG_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.BITSONG_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.CERBERUS_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.CERBERUS_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.CERTIK_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.CERTIK_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.CHIHUAHUA_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.CHIHUAHUA_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.COMDEX_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.COMDEX_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.COSMOS_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.COSMOS_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.CRYPTO_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.CRYPTO_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.CUDOS_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.CUDOS_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.DESMOS_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.DESMOS_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.EMONEY_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.EMONEY_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.EVMOS_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.EVMOS_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.FETCHAI_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.FETCH_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.GRABRIDGE_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.GRAB_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.IMVERSED_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.IMVERSED_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.INJ_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.INJ_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.IOV_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.IRIS_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.IRIS_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.JUNO_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.JUNO_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.KAVA_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.KAVA_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.KI_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.KI_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.KONSTELL_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.KONSTELL_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.LUM_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.LUM_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.MEDI_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.MEDI_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.OKEX_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.OMNIFLIX_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.OMNIFLIX_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.OSMOSIS_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.OSMOSIS_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.PERSIS_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.PERSIS_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.PROVENANCE_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.PROVENANCE_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.REGEN_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.REGEN_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.RIZON_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.RIZON_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.SECRET_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.SECRET_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.SENTINEL_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.SENTINEL_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.SIFCHAIN_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.SIF_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.STARGAZE_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.STARGAZE_VAL_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.STARNAME_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_DVPN;
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
import static wannabit.io.cosmostaion.base.BaseConstant.UMEE_UNKNOWN_RELAYER;
import static wannabit.io.cosmostaion.base.BaseConstant.UMEE_VAL_URL;
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

import com.fulldive.wallet.interactors.secret.WalletUtils;
import com.fulldive.wallet.models.BaseChain;
import com.fulldive.wallet.models.Currency;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

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
import wannabit.io.cosmostaion.dao.Balance;
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

    public static void showCoinDp(Context c, BaseData baseData, Coin coin, TextView denomTv, TextView amountTv, BaseChain chain) {
        if (chain.isGRPC() && coin.isIbc()) {
            IbcToken ibcToken = baseData.getIbcToken(coin.getIbcHash());
            if (ibcToken != null && ibcToken.auth) {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(ibcToken.display_denom.toUpperCase());
                amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), ibcToken.decimal, ibcToken.decimal));

            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(R.string.str_unknown);
                amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));
            }

        } else if (chain.equals(BaseChain.COSMOS_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
                amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));
                denomTv.setText(coin.denom.toUpperCase());
            }

        } else if (chain.equals(BaseChain.IMVERSED_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));

        } else if (chain.equals(BaseChain.IRIS_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));

        } else if (chain.equals(BaseChain.KAVA_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else if (coin.denom.equals(TOKEN_HARD)) {
                denomTv.setText(coin.denom.toUpperCase());
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorHard));
            } else if (coin.denom.equals(TOKEN_USDX)) {
                denomTv.setText(coin.denom.toUpperCase());
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorUsdx));
            } else if (coin.denom.equals(TOKEN_SWP)) {
                denomTv.setText(coin.denom.toUpperCase());
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorSwp));
            } else {
                denomTv.setText(coin.denom.toUpperCase());
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), WUtil.getKavaCoinDecimal(coin), WUtil.getKavaCoinDecimal(coin)));

        } else if (chain.equals(BaseChain.IOV_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setText(coin.denom.toUpperCase());

            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));

        } else if (chain.equals(BaseChain.BNB_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 8, 8));

        } else if (chain.equals(BaseChain.BAND_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));


        } else if (chain.equals(BaseChain.OKEX_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 0, 18));

        } else if (chain.equals(BaseChain.CERTIK_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));


        } else if (chain.equals(BaseChain.SECRET_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));


        } else if (chain.equals(BaseChain.AKASH_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));

        } else if (chain.equals(BaseChain.PERSIS_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));

        } else if (chain.equals(BaseChain.SENTINEL_MAIN.INSTANCE)) {
            if (coin.denom.equals(TOKEN_DVPN)) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));

        } else if (chain.equals(BaseChain.FETCHAI_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 18, 18));

        } else if (chain.equals(BaseChain.CRYPTO_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 8, 8));

        } else if (chain.equals(BaseChain.SIF_MAIN.INSTANCE)) {
            int decimal = WUtil.getSifCoinDecimal(coin.denom);
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else if (coin.denom.startsWith("c")) {
                denomTv.setText(coin.denom.substring(1).toUpperCase());
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
            } else {
                denomTv.setText(coin.denom.toUpperCase());
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), decimal, decimal));

        } else if (chain.equals(BaseChain.KI_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));

        } else if (chain.equals(BaseChain.OSMOSIS_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
                amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));

            } else if (coin.denom.equals(TOKEN_ION)) {
                denomTv.setText("ION");
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorIon));
                amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));

            } else if (coin.osmosisAmm()) {
                denomTv.setText(coin.osmosisAmmDpDenom());
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 18, 18));

            } else {
                denomTv.setText(coin.denom.toUpperCase());
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));
            }

        } else if (chain.equals(BaseChain.MEDI_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));

        } else if (chain.equals(BaseChain.EMONEY_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setText(coin.denom.toUpperCase());
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));

        } else if (chain.equals(BaseChain.RIZON_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));

        } else if (chain.equals(BaseChain.JUNO_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));

        } else if (chain.equals(BaseChain.REGEN_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));

        } else if (chain.equals(BaseChain.BITCANNA_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));

        } else if (chain.equals(BaseChain.ALTHEA_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));

        } else if (chain.equals(BaseChain.STARGAZE_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));

        } else if (chain.equals(BaseChain.GRABRIDGE_MAIN.INSTANCE)) {
            int decimal = WUtil.getGBridgeCoinDecimal(baseData, coin.denom);
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else if (coin.denom.startsWith("gravity")) {
                final Assets assets = baseData.getAsset(coin.denom);
                if (assets != null) {
                    denomTv.setText(assets.origin_symbol);
                    denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                }
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), decimal, decimal));

        } else if (chain.equals(BaseChain.COMDEX_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));

        } else if (chain.equals(BaseChain.INJ_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
                amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 18, 18));
            } else if (coin.denom.startsWith("peggy")) {
                final Assets assets = baseData.getAsset(coin.denom);
                if (assets != null) {
                    denomTv.setText(assets.origin_symbol);
                    denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                    amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), assets.decimal, assets.decimal));
                }
            } else {
                denomTv.setText(coin.denom.toUpperCase());
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 18, 18));
            }

        } else if (chain.equals(BaseChain.BITSONG_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));

        } else if (chain.equals(BaseChain.DESMOS_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));

        } else if (chain.equals(BaseChain.LUM_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));

        } else if (chain.equals(BaseChain.CHIHUAHUA_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));

        } else if (chain.equals(BaseChain.AXELAR_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));

        } else if (chain.equals(BaseChain.KONSTELL_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));

        } else if (chain.equals(BaseChain.UMEE_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));

        } else if (chain.equals(BaseChain.EVMOS_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 18, 18));

        } else if (chain.equals(BaseChain.CUDOS_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 18, 18));

        } else if (chain.equals(BaseChain.PROVENANCE_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 9, 9));

        } else if (chain.equals(BaseChain.CERBERUS_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));

        } else if (chain.equals(BaseChain.OMNIFLIX_MAIN.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));

        } else if (chain.equals(BaseChain.COSMOS_TEST.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));

        } else if (chain.equals(BaseChain.IRIS_TEST.INSTANCE)) {
            if (coin.denom.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(coin.denom.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(coin.amount), 6, 6));

        }
    }

    public static void showCoinDp(Context c, BaseData baseData, String symbol, String amount, TextView denomTv, TextView amountTv, BaseChain chain) {
        if (chain.isGRPC() && symbol.startsWith("ibc")) {
            IbcToken ibcToken = baseData.getIbcToken(symbol.replaceAll("ibc/", ""));
            if (ibcToken != null && ibcToken.auth) {
                denomTv.setText(ibcToken.display_denom.toUpperCase());
                amountTv.setText(getDpAmount2(new BigDecimal(amount), ibcToken.decimal, ibcToken.decimal));

            } else {
                denomTv.setText(R.string.str_unknown);
                amountTv.setText(getDpAmount2(new BigDecimal(amount), chain.getDivideDecimal(), chain.getDisplayDecimal()));
            }
            denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));

        } else if (chain.equals(BaseChain.COSMOS_MAIN.INSTANCE)) {
            if (symbol.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(symbol.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(amount), chain.getDivideDecimal(), chain.getDisplayDecimal()));

        } else if (chain.equals(BaseChain.KAVA_MAIN.INSTANCE)) {
            if (symbol.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else if (symbol.equals(TOKEN_HARD)) {
                denomTv.setText(symbol.toUpperCase());
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorHard));
            } else if (symbol.equals(TOKEN_USDX)) {
                denomTv.setText(symbol.toUpperCase());
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorUsdx));
            } else if (symbol.equals(TOKEN_SWP)) {
                denomTv.setText(symbol.toUpperCase());
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorSwp));
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(symbol.toUpperCase());
            }
            if (amountTv != null) {
                amountTv.setText(getDpAmount2(new BigDecimal(amount), WUtil.getKavaCoinDecimal(baseData, symbol), WUtil.getKavaCoinDecimal(baseData, symbol)));
            }

        } else if (chain.equals(BaseChain.BAND_MAIN.INSTANCE)) {
            DpMainDenom(chain.getChainName(), denomTv);
            amountTv.setText(getDpAmount2(new BigDecimal(amount), chain.getDivideDecimal(), chain.getDisplayDecimal()));

        } else if (chain.equals(BaseChain.SIF_MAIN.INSTANCE)) {
            int decimal = WUtil.getSifCoinDecimal(symbol);
            if (symbol.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else if (symbol.startsWith("c")) {
                denomTv.setText(symbol.substring(1).toUpperCase());
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(symbol.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(amount), decimal, decimal));
        } else if (chain.equals(BaseChain.OSMOSIS_MAIN.INSTANCE)) {
            if (symbol.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
                amountTv.setText(getDpAmount2(new BigDecimal(amount), chain.getDivideDecimal(), chain.getDisplayDecimal()));

            } else if (symbol.equals(TOKEN_ION)) {
                denomTv.setText("ION");
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorIon));
                amountTv.setText(getDpAmount2(new BigDecimal(amount), chain.getDivideDecimal(), chain.getDisplayDecimal()));

            } else if (symbol.startsWith("gamm/pool/")) {
                String[] value = symbol.split("/");
                denomTv.setText("GAMM-" + value[value.length - 1]);
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                amountTv.setText(getDpAmount2(new BigDecimal(amount), 18, 18));

            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(symbol.toUpperCase());
                amountTv.setText(getDpAmount2(new BigDecimal(amount), chain.getDivideDecimal(), chain.getDisplayDecimal()));
            }
        } else if (chain.equals(BaseChain.GRABRIDGE_MAIN.INSTANCE)) {
            int decimal = WUtil.getGBridgeCoinDecimal(baseData, symbol);
            if (symbol.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else if (symbol.startsWith("gravity")) {
                final Assets assets = baseData.getAsset(symbol);
                if (assets != null) {
                    denomTv.setText(assets.origin_symbol);
                    denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                }
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(symbol.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(amount), decimal, decimal));
        } else if (chain.equals(BaseChain.INJ_MAIN.INSTANCE)) {
            if (symbol.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
                amountTv.setText(getDpAmount2(new BigDecimal(amount), chain.getDivideDecimal(), chain.getDisplayDecimal()));
            } else if (symbol.startsWith("peggy")) {
                final Assets assets = baseData.getAsset(symbol);
                if (assets != null) {
                    denomTv.setText(assets.origin_symbol);
                    denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                    amountTv.setText(getDpAmount2(new BigDecimal(amount), assets.decimal, assets.decimal));
                }
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(symbol.toUpperCase());
                amountTv.setText(getDpAmount2(new BigDecimal(amount), chain.getDivideDecimal(), chain.getDisplayDecimal()));
            }

        } else {
            if (symbol.equals(chain.getMainDenom())) {
                DpMainDenom(chain.getChainName(), denomTv);
            } else {
                denomTv.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                denomTv.setText(symbol.toUpperCase());
            }
            amountTv.setText(getDpAmount2(new BigDecimal(amount), chain.getDivideDecimal(), chain.getDisplayDecimal()));

        }
    }

    public static void showChainDp(Context c, BaseChain baseChain, CardView cardName, CardView cardBody, CardView cardRewardAddress) {
        if (baseChain.equals(BaseChain.OKEX_MAIN.INSTANCE) || baseChain.equals(BaseChain.KAVA_MAIN.INSTANCE) || baseChain.equals(BaseChain.BNB_MAIN.INSTANCE) || baseChain.equals(BaseChain.FETCHAI_MAIN.INSTANCE)) {
            cardRewardAddress.setVisibility(View.GONE);
        } else {
            cardRewardAddress.setVisibility(View.VISIBLE);
        }
        cardName.setCardBackgroundColor(WDp.getChainBgColor(c, baseChain));
        cardBody.setCardBackgroundColor(WDp.getChainBgColor(c, baseChain));
        cardRewardAddress.setCardBackgroundColor(WDp.getChainBgColor(c, baseChain));

        if (baseChain.equals(BaseChain.COSMOS_TEST.INSTANCE)) {
            cardName.setCardBackgroundColor(c.getColor(R.color.colorTransBg));
            cardBody.setCardBackgroundColor(c.getColor(R.color.colorTransBg));
            cardRewardAddress.setCardBackgroundColor(c.getColor(R.color.colorTransBg));
            cardRewardAddress.setVisibility(View.VISIBLE);

        } else if (baseChain.equals(BaseChain.IRIS_TEST.INSTANCE)) {
            cardName.setCardBackgroundColor(c.getColor(R.color.colorTransBg));
            cardBody.setCardBackgroundColor(c.getColor(R.color.colorTransBg));
            cardRewardAddress.setCardBackgroundColor(c.getColor(R.color.colorTransBg));
            cardRewardAddress.setVisibility(View.VISIBLE);

        }
    }

    public static void getChainHint(BaseChain chain, TextView textView) {
        final Context context = textView.getContext();
        int titleRes = R.string.str_unknown;
        if (chain != null) {
            titleRes = chain.getChainTitle();
        }
        textView.setText(String.format("(%s)", context.getString(titleRes)));
    }

    public static BaseChain getChainTypeByChainId(String chainId) {
        if (chainId != null) {
            if (chainId.contains("cosmoshub-")) {
                return BaseChain.COSMOS_MAIN.INSTANCE;
            } else if (chainId.contains("irishub-")) {
                return BaseChain.IRIS_MAIN.INSTANCE;
            } else if (chainId.contains("iov-")) {
                return BaseChain.IOV_MAIN.INSTANCE;
            } else if (chainId.contains("akashnet-")) {
                return BaseChain.AKASH_MAIN.INSTANCE;
            } else if (chainId.contains("sentinelhub-")) {
                return BaseChain.SENTINEL_MAIN.INSTANCE;
            } else if (chainId.contains("core-")) {
                return BaseChain.PERSIS_MAIN.INSTANCE;
            } else if (chainId.contains("sifchain-")) {
                return BaseChain.SIF_MAIN.INSTANCE;
            } else if (chainId.contains("osmosis-")) {
                return BaseChain.OSMOSIS_MAIN.INSTANCE;
            } else if (chainId.contains("crypto-org-")) {
                return BaseChain.CRYPTO_MAIN.INSTANCE;
            } else if (chainId.contains("laozi-mainnet")) {
                return BaseChain.BAND_MAIN.INSTANCE;
            } else if (chainId.contains("shentu-")) {
                return BaseChain.CERTIK_MAIN.INSTANCE;
            } else if (chainId.contains("panacea-")) {
                return BaseChain.MEDI_MAIN.INSTANCE;
            } else if (chainId.contains("emoney-")) {
                return BaseChain.EMONEY_MAIN.INSTANCE;
            } else if (chainId.contains("juno-")) {
                return BaseChain.JUNO_MAIN.INSTANCE;
            } else if (chainId.contains("regen-")) {
                return BaseChain.REGEN_MAIN.INSTANCE;
            } else if (chainId.contains("bitcanna-")) {
                return BaseChain.BITCANNA_MAIN.INSTANCE;
            } else if (chainId.contains("stargaze-")) {
                return BaseChain.STARGAZE_MAIN.INSTANCE;
            } else if (chainId.contains("fetchhub--")) {
                return BaseChain.FETCHAI_MAIN.INSTANCE;
            } else if (chainId.contains("kichain-")) {
                return BaseChain.KI_MAIN.INSTANCE;
            } else if (chainId.contains("secret-")) {
                return BaseChain.SECRET_MAIN.INSTANCE;
            } else if (chainId.contains("titan-")) {
                return BaseChain.RIZON_MAIN.INSTANCE;
            } else if (chainId.contains("comdex-")) {
                return BaseChain.COMDEX_MAIN.INSTANCE;
            } else if (chainId.contains("bitsong-")) {
                return BaseChain.BITSONG_MAIN.INSTANCE;
            } else if (chainId.contains("injective-")) {
                return BaseChain.INJ_MAIN.INSTANCE;
            } else if (chainId.contains("desmos-")) {
                return BaseChain.DESMOS_MAIN.INSTANCE;
            } else if (chainId.contains("gravity-bridge-")) {
                return BaseChain.GRABRIDGE_MAIN.INSTANCE;
            } else if (chainId.contains("lum-network-")) {
                return BaseChain.LUM_MAIN.INSTANCE;
            } else if (chainId.contains("chihuahua-")) {
                return BaseChain.CHIHUAHUA_MAIN.INSTANCE;
            } else if (chainId.contains("kava-")) {
                return BaseChain.KAVA_MAIN.INSTANCE;
            } else if (chainId.contains("axelar-")) {
                return BaseChain.AXELAR_MAIN.INSTANCE;
            } else if (chainId.contains("darchub")) {
                return BaseChain.KONSTELL_MAIN.INSTANCE;
            } else if (chainId.contains("umee-")) {
                return BaseChain.UMEE_MAIN.INSTANCE;
            } else if (chainId.contains("evmos")) {
                return BaseChain.EVMOS_MAIN.INSTANCE;
            } else if (chainId.contains("cudos-")) {
                return BaseChain.CUDOS_MAIN.INSTANCE;
            } else if (chainId.contains("pio-mainnet-")) {
                return BaseChain.PROVENANCE_MAIN.INSTANCE;
            } else if (chainId.contains("cerberus-")) {
                return BaseChain.CERBERUS_MAIN.INSTANCE;
            } else if (chainId.contains("omniflixhub-")) {
                return BaseChain.OMNIFLIX_MAIN.INSTANCE;
            }
        }
        return null;
    }

    public static String getChainNameByBaseChain(BaseChain baseChain) {
        if (baseChain != null) {
            if (baseChain.equals(BaseChain.COSMOS_MAIN.INSTANCE)) {
                return "cosmos";
            } else if (baseChain.equals(BaseChain.IMVERSED_MAIN.INSTANCE)) {
                return "imversed-canary";
            } else if (baseChain.equals(BaseChain.IRIS_MAIN.INSTANCE)) {
                return "iris";
            } else if (baseChain.equals(BaseChain.BNB_MAIN.INSTANCE)) {
                return "bnb";
            } else if (baseChain.equals(BaseChain.OKEX_MAIN.INSTANCE)) {
                return "okex";
            } else if (baseChain.equals(BaseChain.KAVA_MAIN.INSTANCE)) {
                return "kava";
            } else if (baseChain.equals(BaseChain.BAND_MAIN.INSTANCE)) {
                return "band";
            } else if (baseChain.equals(BaseChain.PERSIS_MAIN.INSTANCE)) {
                return "persistence";
            } else if (baseChain.equals(BaseChain.IOV_MAIN.INSTANCE)) {
                return "starname";
            } else if (baseChain.equals(BaseChain.CERTIK_MAIN.INSTANCE)) {
                return "certik";
            } else if (baseChain.equals(BaseChain.AKASH_MAIN.INSTANCE)) {
                return "akash";
            } else if (baseChain.equals(BaseChain.SENTINEL_MAIN.INSTANCE)) {
                return "sentinel";
            } else if (baseChain.equals(BaseChain.FETCHAI_MAIN.INSTANCE)) {
                return "fetchai";
            } else if (baseChain.equals(BaseChain.CRYPTO_MAIN.INSTANCE)) {
                return "cryptoorg";
            } else if (baseChain.equals(BaseChain.SIF_MAIN.INSTANCE)) {
                return "sifchain";
            } else if (baseChain.equals(BaseChain.RIZON_MAIN.INSTANCE)) {
                return "rizon";
            } else if (baseChain.equals(BaseChain.KI_MAIN.INSTANCE)) {
                return "kichain";
            } else if (baseChain.equals(BaseChain.OSMOSIS_MAIN.INSTANCE)) {
                return "osmosis";
            } else if (baseChain.equals(BaseChain.MEDI_MAIN.INSTANCE)) {
                return "medibloc";
            } else if (baseChain.equals(BaseChain.EMONEY_MAIN.INSTANCE)) {
                return "emoney";
            } else if (baseChain.equals(BaseChain.REGEN_MAIN.INSTANCE)) {
                return "regen";
            } else if (baseChain.equals(BaseChain.JUNO_MAIN.INSTANCE)) {
                return "juno";
            } else if (baseChain.equals(BaseChain.BITCANNA_MAIN.INSTANCE)) {
                return "bitcanna";
            } else if (baseChain.equals(BaseChain.STARGAZE_MAIN.INSTANCE)) {
                return "stargaze";
            } else if (baseChain.equals(BaseChain.COMDEX_MAIN.INSTANCE)) {
                return "comdex";
            } else if (baseChain.equals(BaseChain.SECRET_MAIN.INSTANCE)) {
                return "secret";
            } else if (baseChain.equals(BaseChain.BITSONG_MAIN.INSTANCE)) {
                return "bitsong";
            } else if (baseChain.equals(BaseChain.ALTHEA_MAIN.INSTANCE)) {
                return "althea";
            } else if (baseChain.equals(BaseChain.INJ_MAIN.INSTANCE)) {
                return "injective";
            } else if (baseChain.equals(BaseChain.DESMOS_MAIN.INSTANCE)) {
                return "desmos";
            } else if (baseChain.equals(BaseChain.GRABRIDGE_MAIN.INSTANCE)) {
                return "gravity-bridge";
            } else if (baseChain.equals(BaseChain.LUM_MAIN.INSTANCE)) {
                return "lum";
            } else if (baseChain.equals(BaseChain.CHIHUAHUA_MAIN.INSTANCE)) {
                return "chihuahua";
            } else if (baseChain.equals(BaseChain.AXELAR_MAIN.INSTANCE)) {
                return "axelar";
            } else if (baseChain.equals(BaseChain.KONSTELL_MAIN.INSTANCE)) {
                return "konstellation";
            } else if (baseChain.equals(BaseChain.UMEE_MAIN.INSTANCE)) {
                return "umee";
            } else if (baseChain.equals(BaseChain.EVMOS_MAIN.INSTANCE)) {
                return "evmos";
            } else if (baseChain.equals(BaseChain.CUDOS_MAIN.INSTANCE)) {
                return "cudos";
            } else if (baseChain.equals(BaseChain.PROVENANCE_MAIN.INSTANCE)) {
                return "provenance";
            } else if (baseChain.equals(BaseChain.CERBERUS_MAIN.INSTANCE)) {
                return "cerberus";
            } else if (baseChain.equals(BaseChain.OMNIFLIX_MAIN.INSTANCE)) {
                return "omniflix";
            } else if (baseChain.equals(BaseChain.COSMOS_TEST.INSTANCE)) {
                return "cosmos-testnet";
            }
        }
        return null;
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

    public static String getDefaultRelayerImg(BaseChain chain) {
        if (chain != null) {
            if (chain.equals(BaseChain.AKASH_MAIN.INSTANCE)) {
                return AKASH_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.BAND_MAIN.INSTANCE)) {
                return BAND_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.CERTIK_MAIN.INSTANCE)) {
                return CERTIK_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.COSMOS_MAIN.INSTANCE)) {
                return COSMOS_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.CRYPTO_MAIN.INSTANCE)) {
                return CRYPTO_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.EMONEY_MAIN.INSTANCE)) {
                return EMONEY_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.FETCHAI_MAIN.INSTANCE)) {
                return FETCHAI_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.IMVERSED_MAIN.INSTANCE)) {
                return IMVERSED_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.IRIS_MAIN.INSTANCE)) {
                return IRIS_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.JUNO_MAIN.INSTANCE)) {
                return JUNO_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.OSMOSIS_MAIN.INSTANCE)) {
                return OSMOSIS_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.PERSIS_MAIN.INSTANCE)) {
                return PERSIS_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.REGEN_MAIN.INSTANCE)) {
                return REGEN_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.SENTINEL_MAIN.INSTANCE)) {
                return SENTINEL_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.SIF_MAIN.INSTANCE)) {
                return SIFCHAIN_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.IOV_MAIN.INSTANCE)) {
                return STARNAME_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.KI_MAIN.INSTANCE)) {
                return KI_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.BITCANNA_MAIN.INSTANCE)) {
                return BITCANNA_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.RIZON_MAIN.INSTANCE)) {
                return RIZON_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.MEDI_MAIN.INSTANCE)) {
                return MEDI_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.STARGAZE_MAIN.INSTANCE)) {
                return STARGAZE_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.COMDEX_MAIN.INSTANCE)) {
                return COMDEX_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.SECRET_MAIN.INSTANCE)) {
                return SECRET_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.INJ_MAIN.INSTANCE)) {
                return INJ_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.BITSONG_MAIN.INSTANCE)) {
                return BITSONG_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.DESMOS_MAIN.INSTANCE)) {
                return DESMOS_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.GRABRIDGE_MAIN.INSTANCE)) {
                return GRAB_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.LUM_MAIN.INSTANCE)) {
                return LUM_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.CHIHUAHUA_MAIN.INSTANCE)) {
                return CHIHUAHUA_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.KAVA_MAIN.INSTANCE)) {
                return KAVA_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.AXELAR_MAIN.INSTANCE)) {
                return AXELAR_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.KONSTELL_MAIN.INSTANCE)) {
                return KONSTELL_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.UMEE_MAIN.INSTANCE)) {
                return UMEE_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.EVMOS_MAIN.INSTANCE)) {
                return EVMOS_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.CUDOS_MAIN.INSTANCE)) {
                return CUDOS_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.PROVENANCE_MAIN.INSTANCE)) {
                return PROVENANCE_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.CERBERUS_MAIN.INSTANCE)) {
                return CERBERUS_UNKNOWN_RELAYER;
            } else if (chain.equals(BaseChain.OMNIFLIX_MAIN.INSTANCE)) {
                return OMNIFLIX_UNKNOWN_RELAYER;
            }
        }
        return null;
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

    public static String getKavaPriceFeedSymbol(String denom) {
        if (denom.equalsIgnoreCase(BaseChain.KAVA_MAIN.INSTANCE.getMainDenom())) {
            return "kava:usd";
        } else if (denom.equalsIgnoreCase(TOKEN_HARD)) {
            return "hard:usd";
        } else if (denom.equalsIgnoreCase(TOKEN_USDX)) {
            return "usdx:usd";
        } else if (denom.equalsIgnoreCase(TOKEN_SWP)) {
            return "swp:usd";
        } else if (denom.equalsIgnoreCase(TOKEN_HTLC_KAVA_BNB)) {
            return "bnb:usd";
        } else if (denom.equalsIgnoreCase(TOKEN_HTLC_KAVA_XRPB)) {
            return "xrp:usd";
        } else if (denom.equalsIgnoreCase(TOKEN_HTLC_KAVA_BUSD)) {
            return "busd:usd";
        } else if (denom.contains("btc")) {
            return "btc:usd";
        }
        return "";
    }

    public static BigDecimal getKavaPriceFeed(BaseData baseData, String denom) {
        String feedSymbol = getKavaPriceFeedSymbol(denom);
        if (baseData.mKavaTokenPrice.get(feedSymbol) == null) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(baseData.mKavaTokenPrice.get(feedSymbol).getPrice()).movePointLeft(18);
    }

    public static BigDecimal convertTokenToKava(BaseData baseData, Balance balance) {
        String denom = balance.symbol;
        BigDecimal tokenAmount = balance.balance; // baseData.getAvailable(denom).add(baseData.getVesting(denom));
        BigDecimal totalTokenValue = kavaTokenDollorValue(baseData, denom, tokenAmount);
        return totalTokenValue.movePointRight(6).divide(perUsdValue(baseData, BaseChain.KAVA_MAIN.INSTANCE.getMainDenom()), 6, RoundingMode.DOWN);
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

    public static BigDecimal convertTokenToOkt(Balance balance, BaseData baseData, String denom) {
        OkToken okToken = baseData.okToken(denom);
        if (okToken != null) {
            BigDecimal tokenAmount = balance.getDelegatableAmount();
            BigDecimal totalTokenValue = okExTokenDollorValue(baseData, okToken, tokenAmount);
            return totalTokenValue.divide(perUsdValue(baseData, BaseChain.OKEX_MAIN.INSTANCE.getMainDenom()), 18, RoundingMode.DOWN);
        }
        return BigDecimal.ZERO;
    }

    public static BigDecimal convertTokenToOkt(BaseActivity baseActivity, BaseData baseData, String denom) {
        OkToken okToken = baseData.okToken(denom);
        if (okToken != null) {
            final Balance balance = baseActivity.getFullBalance(denom);
            BigDecimal tokenAmount = balance.getDelegatableAmount();
            BigDecimal totalTokenValue = okExTokenDollorValue(baseData, okToken, tokenAmount);
            return totalTokenValue.divide(perUsdValue(baseData, BaseChain.OKEX_MAIN.INSTANCE.getMainDenom()), 18, RoundingMode.DOWN);
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

    public static BigDecimal valueChange(BaseData baseData, String denom) {
        if (baseData.getPrice(denom) != null) {
            return baseData.getPrice(denom).priceChange();
        }
        return BigDecimal.ZERO.setScale(2, RoundingMode.FLOOR);
    }

    public static SpannableString dpValueChange(BaseData baseData, String denom) {
        return getDpString(valueChange(baseData, denom).toPlainString() + "% (24h)", 9);
    }

    public static BigDecimal perUsdValue(BaseData baseData, String denom) {
        if (denom.contains("gamm/pool/")) {
            BalancerPool.Pool pool = baseData.getOsmosisPoolByDenom(denom);
            return WUtil.getOsmoLpTokenPerUsdPrice(baseData, pool);
        }
        if (denom.startsWith("pool") && denom.length() >= 68) {
            ChainParam.GdexStatus pool = baseData.getParamGravityPoolByDenom(denom);
            return WUtil.getParamGdexLpTokenPerUsdPrice(baseData, pool);
        }
        if (denom.equals(TOKEN_EMONEY_EUR) || denom.equals(TOKEN_EMONEY_CHF) || denom.equals(TOKEN_EMONEY_DKK) ||
                denom.equals(TOKEN_EMONEY_NOK) || denom.equals(TOKEN_EMONEY_SEK)) {
            final Price usdtPrice = baseData.getPrice("usdt");
            if (usdtPrice != null && usdtPrice.prices != null) {
                for (Price.Prices price : usdtPrice.prices) {
                    if (price.currency.equalsIgnoreCase(denom.substring(1))) {
                        return BigDecimal.ONE.divide(new BigDecimal(price.current_price), 18, RoundingMode.DOWN);
                    }
                }
            }
        }
        if (baseData.getPrice(denom) != null) {
            return baseData.getPrice(denom).currencyPrice("usd").setScale(18, RoundingMode.DOWN);
        }
        return BigDecimal.ZERO.setScale(18, RoundingMode.DOWN);
    }

    public static BigDecimal usdValue(BaseData baseData, String denom, BigDecimal amount, int divider) {
        return perUsdValue(baseData, denom).multiply(amount).movePointLeft(divider).setScale(3, RoundingMode.DOWN);
    }

    public static BigDecimal perUserCurrencyValue(BaseData baseData, Currency currency, String denom) {
        if (currency == Currency.USD.INSTANCE) {
            return perUsdValue(baseData, denom);
        }
        final Price usdtPrice = baseData.getPrice("usdt");
        if (usdtPrice != null) {
            final BigDecimal currentPrice = usdtPrice.currencyPrice(currency.getTitle().toLowerCase());
            return perUsdValue(baseData, denom).multiply(currentPrice).setScale(3, RoundingMode.DOWN);
        }
        return BigDecimal.ZERO.setScale(3, RoundingMode.DOWN);
    }

    public static SpannableString dpPerUserCurrencyValue(BaseData baseData, Currency currency, String denom) {
        BigDecimal totalValue = perUserCurrencyValue(baseData, currency, denom);
        final String formatted = currency.getSymbol() + " " + getDecimalFormat(3).format(totalValue);
        return dpCurrencyValue(formatted, 3);
    }

    public static BigDecimal userCurrencyValue(BaseData baseData, Currency currency, String denom, BigDecimal amount, int divider) {
        return perUserCurrencyValue(baseData, currency, denom).multiply(amount).movePointLeft(divider).setScale(3, RoundingMode.DOWN);
    }

    public static SpannableString dpUserCurrencyValue(BaseData baseData, Currency currency, String denom, BigDecimal amount, int divider) {
        BigDecimal totalValue = userCurrencyValue(baseData, currency, denom, amount, divider);
        final String formatted = currency.getSymbol() + " " + getDecimalFormat(3).format(totalValue);
        return dpCurrencyValue(formatted, 3);
    }

    public static BigDecimal allAssetToUserCurrency(BaseChain baseChain, Currency currency, BaseData baseData, List<Balance> balances) {
        BigDecimal totalValue = BigDecimal.ZERO;
        if (baseChain.isGRPC()) {
            for (Balance balance : balances) {
                if (balance.symbol.equals(baseChain.getMainDenom())) {
                    BigDecimal amount = balance.balance.add(baseData.getAllMainAsset(balance.symbol));   //TODO: add vesting
                    BigDecimal assetValue = userCurrencyValue(baseData, currency, balance.symbol, amount, baseChain.getDivideDecimal());
                    totalValue = totalValue.add(assetValue);
                } else if (baseChain.equals(BaseChain.COSMOS_MAIN.INSTANCE) && balance.symbol.startsWith("pool")) {
                    BigDecimal amount = balance.balance;
                    BigDecimal assetValue = userCurrencyValue(baseData, currency, balance.symbol, amount, 6);
                    totalValue = totalValue.add(assetValue);
                } else if (baseChain.equals(BaseChain.OSMOSIS_MAIN.INSTANCE) && balance.symbol.equals(TOKEN_ION)) {
                    BigDecimal amount = balance.balance;
                    BigDecimal assetValue = userCurrencyValue(baseData, currency, balance.symbol, amount, 6);
                    totalValue = totalValue.add(assetValue);
                } else if (baseChain.equals(BaseChain.OSMOSIS_MAIN.INSTANCE) && balance.symbol.contains("gamm/pool")) {
                    BigDecimal amount = balance.balance;
                    BigDecimal assetValue = userCurrencyValue(baseData, currency, balance.symbol, amount, 18);
                    totalValue = totalValue.add(assetValue);
                } else if (baseChain.equals(BaseChain.SIF_MAIN.INSTANCE) && balance.symbol.startsWith("c")) {
                    BigDecimal amount = balance.balance;
                    int decimal = WUtil.getSifCoinDecimal(baseData, balance.symbol);
                    BigDecimal assetValue = userCurrencyValue(baseData, currency, balance.symbol.substring(1), amount, decimal);
                    totalValue = totalValue.add(assetValue);
                } else if (baseChain.equals(BaseChain.EMONEY_MAIN.INSTANCE) || balance.symbol.startsWith("e")) {
                    BigDecimal available = balance.balance;
                    totalValue = totalValue.add(userCurrencyValue(baseData, currency, balance.symbol, available, 6));
                } else if (baseChain.equals(BaseChain.KAVA_MAIN.INSTANCE) && !balance.isIbc()) {
                    BigDecimal amount = balance.balance;
//                    amount = amount.add(baseData.getVesting(balance.symbol)); // TODO Vesting
                    String kavaDenom = WDp.getKavaBaseDenom(balance.symbol);
                    int kavaDecimal = WUtil.getKavaCoinDecimal(baseData, balance.symbol);
                    BigDecimal assetValue = userCurrencyValue(baseData, currency, kavaDenom, amount, kavaDecimal);
                    totalValue = totalValue.add(assetValue);
                } else if (baseChain.equals(BaseChain.GRABRIDGE_MAIN.INSTANCE) && balance.symbol.startsWith("gravity")) {
                    Assets assets = baseData.getAsset(balance.symbol);
                    BigDecimal available = balance.balance;
                    totalValue = totalValue.add(userCurrencyValue(baseData, currency, assets.origin_symbol, available, assets.decimal));
                } else if (baseChain.equals(BaseChain.INJ_MAIN.INSTANCE) && balance.symbol.startsWith("peggy")) {
                    Assets assets = baseData.getAsset(balance.symbol);
                    BigDecimal available = balance.balance;
                    totalValue = totalValue.add(userCurrencyValue(baseData, currency, assets.origin_symbol, available, assets.decimal));
                } else if (balance.isIbc()) {
                    BigDecimal amount = balance.balance;
                    IbcToken ibcToken = baseData.getIbcToken(balance.symbol);
                    if (ibcToken != null && ibcToken.auth) {
                        BigDecimal assetValue = userCurrencyValue(baseData, currency, ibcToken.base_denom, amount, ibcToken.decimal);
                        totalValue = totalValue.add(assetValue);
                    }
                }
            }

            if (baseData.getCw20sGrpc(baseChain).size() > 0) {
                for (Cw20Assets assets : baseData.getCw20sGrpc(baseChain)) {
                    BigDecimal amount = assets.getAmount();
                    totalValue = totalValue.add(userCurrencyValue(baseData, currency, assets.denom, amount, assets.decimal));
                }
            }
        } else if (baseChain.equals(BaseChain.BNB_MAIN.INSTANCE)) {
            for (Balance balance : balances) {
                if (balance.symbol.equals(baseChain.getMainDenom())) {
                    BigDecimal amount = balance.getTotalAmount();
                    BigDecimal assetValue = userCurrencyValue(baseData, currency, baseChain.getMainDenom(), amount, baseChain.getDivideDecimal());
                    totalValue = totalValue.add(assetValue);
                } else {
                    BigDecimal amount = balance.getTotalAmount();
                    BigDecimal convertAmount = WUtil.getBnbConvertAmount(baseData, balance.symbol, amount);
                    BigDecimal assetValue = userCurrencyValue(baseData, currency, baseChain.getMainDenom(), convertAmount, baseChain.getDivideDecimal());
                    totalValue = totalValue.add(assetValue);
                }
            }

        } else if (baseChain.equals(BaseChain.OKEX_MAIN.INSTANCE)) {
            for (Balance balance : balances) {
                if (balance.symbol.equals(baseChain.getMainDenom())) {
                    BigDecimal amount = balance.getDelegatableAmount().add(baseData.getAllExToken(balance.symbol));
                    BigDecimal assetValue = userCurrencyValue(baseData, currency, baseChain.getMainDenom(), amount, baseChain.getDivideDecimal());
                    totalValue = totalValue.add(assetValue);
                } else {
                    BigDecimal convertAmount = convertTokenToOkt(balance, baseData, balance.symbol);
                    BigDecimal assetValue = userCurrencyValue(baseData, currency, baseChain.getMainDenom(), convertAmount, baseChain.getDivideDecimal());
                    totalValue = totalValue.add(assetValue);
                }
            }
        } else {
            for (Balance balance : balances) {
                if (balance.symbol.equals(baseChain.getMainDenom())) {
                    BigDecimal amount = balance.getDelegatableAmount().add(baseData.getAllMainAssetOld(balance.symbol));
                    BigDecimal assetValue = userCurrencyValue(baseData, currency, balance.symbol, amount, baseChain.getDivideDecimal());
                    totalValue = totalValue.add(assetValue);
                }
            }

        }
        return totalValue;
    }


    public static SpannableString dpAllAssetValueUserCurrency(BaseChain baseChain, Currency currency, BaseData baseData, List<Balance> balances) {
        BigDecimal totalValue = allAssetToUserCurrency(baseChain, currency, baseData, balances);
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

    public static String getUnbondingTimeleft(Context c, long finishTime) {
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

    public static String getMonikerImgUrl(BaseChain basechain, String opAddress) {
        if (basechain.equals(BaseChain.COSMOS_MAIN.INSTANCE) || basechain.equals(BaseChain.COSMOS_TEST.INSTANCE)) {
            return COSMOS_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.IMVERSED_MAIN.INSTANCE)) {
            return IMVERSED_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.IRIS_MAIN.INSTANCE) || basechain.equals(BaseChain.IRIS_TEST.INSTANCE)) {
            return IRIS_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.AKASH_MAIN.INSTANCE)) {
            return AKASH_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.SENTINEL_MAIN.INSTANCE)) {
            return SENTINEL_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.PERSIS_MAIN.INSTANCE)) {
            return PERSIS_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.CRYPTO_MAIN.INSTANCE)) {
            return CRYPTO_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.OSMOSIS_MAIN.INSTANCE)) {
            return OSMOSIS_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.IOV_MAIN.INSTANCE)) {
            return IOV_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.SIF_MAIN.INSTANCE)) {
            return SIF_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.CERTIK_MAIN.INSTANCE)) {
            return CERTIK_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.MEDI_MAIN.INSTANCE)) {
            return MEDI_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.EMONEY_MAIN.INSTANCE)) {
            return EMONEY_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.FETCHAI_MAIN.INSTANCE)) {
            return FETCH_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.BAND_MAIN.INSTANCE)) {
            return BAND_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.RIZON_MAIN.INSTANCE)) {
            return RIZON_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.JUNO_MAIN.INSTANCE)) {
            return JUNO_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.REGEN_MAIN.INSTANCE)) {
            return REGEN_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.BITCANNA_MAIN.INSTANCE)) {
            return BITCANNA_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.ALTHEA_MAIN.INSTANCE) || basechain.equals(BaseChain.ALTHEA_TEST.INSTANCE)) {
            return ALTHEA_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.STARGAZE_MAIN.INSTANCE)) {
            return STARGAZE_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.GRABRIDGE_MAIN.INSTANCE)) {
            return GRABRIDGE_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.COMDEX_MAIN.INSTANCE)) {
            return COMDEX_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.INJ_MAIN.INSTANCE)) {
            return INJ_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.BITSONG_MAIN.INSTANCE)) {
            return BITSONG_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.DESMOS_MAIN.INSTANCE)) {
            return DESMOS_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.SECRET_MAIN.INSTANCE)) {
            return SECRET_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.KI_MAIN.INSTANCE)) {
            return KI_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.LUM_MAIN.INSTANCE)) {
            return LUM_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.CHIHUAHUA_MAIN.INSTANCE)) {
            return CHIHUAHUA_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.UMEE_MAIN.INSTANCE)) {
            return UMEE_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.AXELAR_MAIN.INSTANCE)) {
            return AXELAR_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.KAVA_MAIN.INSTANCE)) {
            return KAVA_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.KONSTELL_MAIN.INSTANCE)) {
            return KONSTELL_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.EVMOS_MAIN.INSTANCE)) {
            return EVMOS_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.CUDOS_MAIN.INSTANCE)) {
            return CUDOS_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.PROVENANCE_MAIN.INSTANCE)) {
            return PROVENANCE_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.CERBERUS_MAIN.INSTANCE)) {
            return CERBERUS_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.OMNIFLIX_MAIN.INSTANCE)) {
            return OMNIFLIX_VAL_URL + opAddress + ".png";
        } else if (basechain.equals(BaseChain.OKEX_MAIN.INSTANCE)) {
            return OKEX_VAL_URL + opAddress + ".png";
        }
        return "";
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

    public static BigDecimal onParseAutoReward(ServiceOuterClass.GetTxResponse response, String Addr, int position) {
        BigDecimal result = BigDecimal.ZERO;
        if (response.getTxResponse().getLogsCount() > 0 && response.getTxResponse().getLogs(position) != null) {
            for (Abci.StringEvent event : response.getTxResponse().getLogs(position).getEventsList()) {
                if (event.getType().equals("transfer")) {
                    for (int i = 0; i < event.getAttributesList().size(); i++) {
                        if (event.getAttributes(i).getKey().equals("recipient") && event.getAttributes(i).getValue().equals(Addr)) {
                            for (int j = i; j < event.getAttributesList().size(); j++) {
                                if (event.getAttributes(j).getKey().equals("amount") && event.getAttributes(j).getValue() != null) {
                                    String temp = event.getAttributes(j).getValue().replaceAll("[^0-9]", "");
                                    result = result.add(new BigDecimal(temp));
                                    break;
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
