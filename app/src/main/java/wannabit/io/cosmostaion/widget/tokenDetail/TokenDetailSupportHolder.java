package wannabit.io.cosmostaion.widget.tokenDetail;

import static com.fulldive.wallet.models.BaseChain.COSMOS_MAIN;
import static com.fulldive.wallet.models.BaseChain.CRYPTO_MAIN;
import static com.fulldive.wallet.models.BaseChain.IRIS_MAIN;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_HARD;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_SWP;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_USDX;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.fulldive.wallet.models.BaseChain;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import java.math.BigDecimal;

import irismod.nft.QueryOuterClass;
import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.base.BaseActivity;
import wannabit.io.cosmostaion.base.BaseData;
import wannabit.io.cosmostaion.dao.Assets;
import wannabit.io.cosmostaion.dao.Balance;
import wannabit.io.cosmostaion.dao.BnbToken;
import wannabit.io.cosmostaion.dao.Cw20Assets;
import wannabit.io.cosmostaion.dao.OkToken;
import wannabit.io.cosmostaion.utils.WDp;
import wannabit.io.cosmostaion.utils.WUtil;
import wannabit.io.cosmostaion.widget.BaseHolder;

public class TokenDetailSupportHolder extends BaseHolder {
    private final CardView mAmountView;
    private final TextView mTvTotal;
    private final TextView mTvAvailable;
    private final RelativeLayout mLockedLayout;
    private final TextView mTvLocked;
    private final RelativeLayout mFrozenLayout;
    private final TextView mTvFrozen;
    private final RelativeLayout mVestingLayout;
    private final TextView mTvVesting;

    // nft
    private final CardView mNftInfo;
    private final TextView mNftName;
    private final TextView mNftContent;
    private final TextView mNftDenomId;
    private final TextView mNftTokenId;
    private final TextView mNftIssuer;

    private final CardView mNftRawRoot;
    private final TextView mNftRawData;

    private int dpDecimal = 6;
    private BigDecimal mAvailableAmount = BigDecimal.ZERO;

    public TokenDetailSupportHolder(@NonNull View itemView) {
        super(itemView);
        mAmountView = itemView.findViewById(R.id.cardView);
        mTvTotal = itemView.findViewById(R.id.total_amount);
        mTvAvailable = itemView.findViewById(R.id.available_amount);
        mLockedLayout = itemView.findViewById(R.id.locked_layout);
        mTvLocked = itemView.findViewById(R.id.locked_amount);
        mFrozenLayout = itemView.findViewById(R.id.frozen_layout);
        mTvFrozen = itemView.findViewById(R.id.frozen_amount);
        mVestingLayout = itemView.findViewById(R.id.vesting_layout);
        mTvVesting = itemView.findViewById(R.id.vesrting_amount);

        mNftInfo = itemView.findViewById(R.id.nft_card_root);
        mNftName = itemView.findViewById(R.id.nft_name);
        mNftContent = itemView.findViewById(R.id.nft_content);
        mNftDenomId = itemView.findViewById(R.id.denom_id);
        mNftTokenId = itemView.findViewById(R.id.token_id);
        mNftIssuer = itemView.findViewById(R.id.issuer);

        mNftRawRoot = itemView.findViewById(R.id.nft_raw_card_root);
        mNftRawData = itemView.findViewById(R.id.nft_raw_data);
    }

    public void onBindNativeTokengRPC(BaseActivity baseActivity, BaseChain baseChain, BaseData baseData, String denom) {
        if (baseChain.equals(BaseChain.KAVA_MAIN.INSTANCE)) {
            dpDecimal = WUtil.getKavaCoinDecimal(baseData, denom);
            if (denom.equalsIgnoreCase(TOKEN_HARD)) {
                mAmountView.setCardBackgroundColor(ContextCompat.getColor(baseActivity, R.color.colorTransBghard));
            } else if (denom.equalsIgnoreCase(TOKEN_USDX)) {
                mAmountView.setCardBackgroundColor(ContextCompat.getColor(baseActivity, R.color.colorTransBgusdx));
            } else if (denom.equalsIgnoreCase(TOKEN_SWP)) {
                mAmountView.setCardBackgroundColor(ContextCompat.getColor(baseActivity, R.color.colorTransBgswp));
            }

            mAvailableAmount = baseActivity.getBalance(denom);

//            BigDecimal vestingAmount = baseData.getVesting(denom);
            BigDecimal vestingAmount = BigDecimal.ZERO; // TODO
            mTvTotal.setText(WDp.getDpAmount2(mAvailableAmount.add(vestingAmount), dpDecimal, dpDecimal));
            mTvAvailable.setText(WDp.getDpAmount2(mAvailableAmount, dpDecimal, dpDecimal));
            if (vestingAmount.compareTo(BigDecimal.ZERO) > 0) {
                mVestingLayout.setVisibility(View.VISIBLE);
                mTvVesting.setText(WDp.getDpAmount2(vestingAmount, dpDecimal, dpDecimal));
            }
        } else {
            dpDecimal = 6;
            mAvailableAmount = baseActivity.getBalance(denom);
            mTvTotal.setText(WDp.getDpAmount2(mAvailableAmount, dpDecimal, dpDecimal));
            mTvAvailable.setText(WDp.getDpAmount2(mAvailableAmount, dpDecimal, dpDecimal));
        }
    }

    public void onBindPoolToken(BaseActivity baseActivity, BaseChain baseChain, String denom) {
        if (baseChain.equals(COSMOS_MAIN.INSTANCE)) {
            dpDecimal = 6;
        } else {
            dpDecimal = 18;
        }
        final Balance balance = baseActivity.getFullBalance(denom);
        mAvailableAmount = balance.balance;
        mTvTotal.setText(WDp.getDpAmount2(mAvailableAmount, dpDecimal, dpDecimal));
        mTvAvailable.setText(WDp.getDpAmount2(mAvailableAmount, dpDecimal, dpDecimal));
    }

    public void onBindKavaToken(BaseActivity baseActivity, BaseData baseData, String denom) {
        dpDecimal = WUtil.getKavaCoinDecimal(baseData, denom);
        final Balance balance = baseActivity.getFullBalance(denom);
        mAvailableAmount = balance.balance;
        if (denom.equalsIgnoreCase(TOKEN_HARD)) {
            mAmountView.setCardBackgroundColor(ContextCompat.getColor(baseActivity, R.color.colorTransBghard));
        } else if (denom.equalsIgnoreCase(TOKEN_USDX)) {
            mAmountView.setCardBackgroundColor(ContextCompat.getColor(baseActivity, R.color.colorTransBgusdx));
        } else if (denom.equalsIgnoreCase(TOKEN_SWP)) {
            mAmountView.setCardBackgroundColor(ContextCompat.getColor(baseActivity, R.color.colorTransBgswp));
        }

        BigDecimal vestingAmount = balance.locked;
        mTvTotal.setText(WDp.getDpAmount2(mAvailableAmount.add(vestingAmount), dpDecimal, dpDecimal));
        mTvAvailable.setText(WDp.getDpAmount2(mAvailableAmount, dpDecimal, dpDecimal));
        if (vestingAmount.compareTo(BigDecimal.ZERO) > 0) {
            mVestingLayout.setVisibility(View.VISIBLE);
            mTvVesting.setText(WDp.getDpAmount2(vestingAmount, dpDecimal, dpDecimal));
        }

    }

    public void onBindBNBTokens(BaseActivity baseActivity, BaseData baseData, String denom) {
        BnbToken bnbToken = baseData.getBnbToken(denom);
        if (bnbToken != null) {
            mLockedLayout.setVisibility(View.VISIBLE);
            mFrozenLayout.setVisibility(View.VISIBLE);
        }
        final Balance balance = baseActivity.getFullBalance(denom);
        mAvailableAmount = balance.balance;
        final BigDecimal lockedAmount = balance.locked;
        final BigDecimal frozenAmount = balance.frozen;
        mTvTotal.setText(WDp.getDpAmount2(mAvailableAmount, 0, 8));
        mTvAvailable.setText(WDp.getDpAmount2(mAvailableAmount, 0, 8));
        mTvLocked.setText(WDp.getDpAmount2(lockedAmount, 0, 8));
        mTvFrozen.setText(WDp.getDpAmount2(frozenAmount, 0, 8));
    }

    public void onBindOKTokens(BaseActivity baseActivity, BaseData baseData, String denom) {
        final OkToken okToken = baseData.okToken(denom);
        if (okToken != null) {
            mLockedLayout.setVisibility(View.VISIBLE);
        }
        final Balance balance = baseActivity.getFullBalance(denom);
        mAvailableAmount = balance.balance;
        final BigDecimal lockedAmount = balance.locked;
        final BigDecimal totalAmount = mAvailableAmount.add(lockedAmount);

        mTvTotal.setText(WDp.getDpAmount2(totalAmount, 0, 18));
        mTvAvailable.setText(WDp.getDpAmount2(mAvailableAmount, 0, 18));
        mTvLocked.setText(WDp.getDpAmount2(lockedAmount, 0, 18));
    }

    public void onBindBridgeToken(BaseActivity activity, BaseData baseData, String denom) {
        final Assets assets = baseData.getAsset(denom);
        if (assets != null) {
            mAvailableAmount = activity.getBalance(assets.denom);
            mTvTotal.setText(WDp.getDpAmount2(mAvailableAmount, assets.decimal, assets.decimal));
            mTvAvailable.setText(WDp.getDpAmount2(mAvailableAmount, assets.decimal, assets.decimal));
        }
    }

    public void onBindNftInfo(Context c, BaseChain baseChain, QueryOuterClass.QueryNFTResponse irisResponse, chainmain.nft.v1.Nft.BaseNFT myCryptoNftInfo, String denomId, String tokenId) {
        if (baseChain.equals(IRIS_MAIN.INSTANCE) && irisResponse != null) {
            mNftInfo.setCardBackgroundColor(ContextCompat.getColor(c, R.color.colorTransBgIris));
            mNftName.setText(irisResponse.getNft().getName());
            mNftContent.setText(WUtil.getNftDescription(irisResponse.getNft().getData()));
            mNftIssuer.setText(WUtil.getNftIssuer(irisResponse.getNft().getData()));

        } else if (baseChain.equals(CRYPTO_MAIN.INSTANCE) && myCryptoNftInfo != null) {
            mNftInfo.setCardBackgroundColor(ContextCompat.getColor(c, R.color.colorTransBgCryto));
            mNftName.setText(myCryptoNftInfo.getName());
            mNftContent.setText(WUtil.getNftDescription(myCryptoNftInfo.getData()));
            mNftIssuer.setText(myCryptoNftInfo.getOwner());
        }
        mNftDenomId.setText(denomId);
        mNftTokenId.setText(tokenId);
    }

    public void onBindNftRawData(Context c, BaseChain baseChain, QueryOuterClass.QueryNFTResponse irisResponse, chainmain.nft.v1.Nft.BaseNFT myCryptoNftInfo) {
        if (baseChain.equals(IRIS_MAIN.INSTANCE) && irisResponse != null) {
            mNftRawRoot.setCardBackgroundColor(ContextCompat.getColor(c, R.color.colorTransBgIris));
            if (irisResponse.getNft().getData().isEmpty()) {
                mNftRawData.setText("");
            } else {
                mNftRawData.setText(new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(irisResponse.getNft().getData())));
            }
        } else if (baseChain.equals(CRYPTO_MAIN.INSTANCE) && myCryptoNftInfo != null) {
            mNftRawRoot.setCardBackgroundColor(ContextCompat.getColor(c, R.color.colorTransBgCryto));
            if (myCryptoNftInfo.getData().isEmpty()) {
                mNftRawData.setText("");
            } else {
                mNftRawData.setText(new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(myCryptoNftInfo.getData())));
            }
        }
    }

    public void onBindCw20Token(Context c, BaseChain baseChain, BaseData baseData, Cw20Assets asset) {
        if (asset != null) {
            mAvailableAmount = asset.getAmount();
            mTvTotal.setText(WDp.getDpAmount2(mAvailableAmount, asset.decimal, asset.decimal));
            mTvAvailable.setText(WDp.getDpAmount2(mAvailableAmount, asset.decimal, asset.decimal));
        }
    }
}
