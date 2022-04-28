package wannabit.io.cosmostaion.base;

import static wannabit.io.cosmostaion.base.BaseConstant.IOV_MSG_TYPE_RENEW_ACCOUNT;
import static wannabit.io.cosmostaion.base.BaseConstant.IOV_MSG_TYPE_RENEW_DOMAIN;
import static wannabit.io.cosmostaion.base.BaseConstant.PRE_USER_EXPANDED_CHAINS;
import static wannabit.io.cosmostaion.base.BaseConstant.PRE_USER_HIDEN_CHAINS;
import static wannabit.io.cosmostaion.base.BaseConstant.PRE_USER_SORTED_CHAINS;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_OK;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.fulldive.wallet.models.BaseChain;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf2.Any;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cosmos.base.v1beta1.CoinOuterClass;
import cosmos.distribution.v1beta1.Distribution;
import cosmos.staking.v1beta1.Staking;
import cosmos.vesting.v1beta1.Vesting;
import desmos.profiles.v1beta1.ModelsProfile;
import kava.cdp.v1beta1.Genesis;
import kava.hard.v1beta1.Hard;
import kava.pricefeed.v1beta1.QueryOuterClass;
import kava.swap.v1beta1.Swap;
import osmosis.gamm.poolmodels.balancer.BalancerPool;
import tendermint.liquidity.v1beta1.Liquidity;
import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.crypto.EncResult;
import wannabit.io.cosmostaion.dao.Assets;
import wannabit.io.cosmostaion.dao.BnbTicker;
import wannabit.io.cosmostaion.dao.BnbToken;
import wannabit.io.cosmostaion.dao.ChainParam;
import wannabit.io.cosmostaion.dao.Cw20Assets;
import wannabit.io.cosmostaion.dao.IbcPath;
import wannabit.io.cosmostaion.dao.IbcToken;
import wannabit.io.cosmostaion.dao.OkToken;
import wannabit.io.cosmostaion.dao.Price;
import wannabit.io.cosmostaion.model.BondingInfo;
import wannabit.io.cosmostaion.model.GDexManager;
import wannabit.io.cosmostaion.model.NodeInfo;
import wannabit.io.cosmostaion.model.RewardInfo;
import wannabit.io.cosmostaion.model.SifIncentive;
import wannabit.io.cosmostaion.model.UnbondingInfo;
import wannabit.io.cosmostaion.model.kava.IncentiveParam;
import wannabit.io.cosmostaion.model.kava.IncentiveReward;
import wannabit.io.cosmostaion.model.type.Coin;
import wannabit.io.cosmostaion.model.type.Validator;
import wannabit.io.cosmostaion.network.res.ResBnbFee;
import wannabit.io.cosmostaion.network.res.ResOkStaking;
import wannabit.io.cosmostaion.network.res.ResOkTickersList;
import wannabit.io.cosmostaion.network.res.ResOkTokenList;
import wannabit.io.cosmostaion.network.res.ResOkUnbonding;
import wannabit.io.cosmostaion.utils.WDp;
import wannabit.io.cosmostaion.utils.WUtil;

public class BaseData {

    private final Context context;
    private SharedPreferences mSharedPreferences;
    public String mCopySalt;
    public EncResult mCopyEncResult;
    private final Gson gson = new Gson();

    public BaseData(Context context) {
        this.context = context.getApplicationContext();
        this.mSharedPreferences = getSharedPreferences();
    }

    private SharedPreferences getSharedPreferences() {
        if (mSharedPreferences == null)
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return mSharedPreferences;
    }

    public ChainParam.Params mChainParam;
    public List<IbcPath> mIbcPaths = new ArrayList<>();
    public List<IbcToken> mIbcTokens = new ArrayList<>();
    public List<Assets> mAssets = new ArrayList<>();
    public List<Cw20Assets> mCw20Assets = new ArrayList<>();

    public IbcToken getIbcToken(String denom) {
        String ibcHash = denom.replace("ibc/", "");
        for (IbcToken ibcToken : mIbcTokens) {
            if (ibcToken.hash.equals(ibcHash)) {
                return ibcToken;
            }
        }
        return null;
    }

    public IbcPath.Path getIbcPath(String channelId) {
        for (IbcPath ibcPath : mIbcPaths) {
            for (IbcPath.Path path : ibcPath.paths) {
                if (path.channel_id.equals(channelId)) {
                    return path;
                }
            }
        }
        return null;
    }

    public Assets getAsset(String denom) {
        for (Assets assets : mAssets) {
            if (assets.denom.equalsIgnoreCase(denom)) {
                return assets;
            }
        }
        return null;
    }

    public ArrayList<Cw20Assets> getCw20sGrpc(BaseChain baseChain) {
        ArrayList<Cw20Assets> result = new ArrayList<>();
        if (mCw20Assets.size() > 0) {
            for (Cw20Assets assets : mCw20Assets) {
                if (assets.chain.equalsIgnoreCase(WDp.getChainNameByBaseChain(baseChain)) && assets.getAmount() != null && assets.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                    result.add(assets);
                }
            }
        }
        return result;
    }

    public Cw20Assets getCw20_gRPC(String contAddress) {
        if (mCw20Assets != null && mCw20Assets.size() > 0) {
            for (Cw20Assets assets : mCw20Assets) {
                if (assets.contract_address.equalsIgnoreCase(contAddress)) {
                    return assets;
                }
            }
        }
        return null;
    }

    public String getBaseDenom(String denom) {
        if (denom.startsWith("ibc/")) {
            IbcToken ibcToken = getIbcToken(denom.replaceAll("ibc/", ""));
            if (ibcToken != null && ibcToken.auth) {
                if (ibcToken.base_denom.startsWith("cw20:")) {
                    String cAddress = ibcToken.base_denom.replaceAll("cw20:", "");
                    for (Cw20Assets assets : mCw20Assets) {
                        if (assets.contract_address.equalsIgnoreCase(cAddress)) {
                            return assets.denom;
                        }
                    }
                } else {
                    return ibcToken.base_denom;
                }
            } else {
                return "UNKNOWN";
            }
        } else if (denom.startsWith("c")) {
            return denom.substring(1);
        }
        return denom;
    }

    public String getIbcRelayerImg(BaseChain baseChain, String channelId) {
        String url = "";
        if (getIbcPath(channelId).relayer_img != null) {
            url = getIbcPath(channelId).relayer_img;
        } else {
            url = WDp.getDefaultRelayerImg(baseChain);
        }
        return url;
    }

    public ArrayList<IbcPath> getIbcSendableRelayers() {
        ArrayList<IbcPath> result = new ArrayList<>();
        for (IbcPath ibcPath : mIbcPaths) {
            for (IbcPath.Path path : ibcPath.paths) {
                if (path.auth != null && path.auth) {
                    result.add(ibcPath);
                }
            }
        }
        Set<IbcPath> arr2 = new HashSet<>(result);
        ArrayList<IbcPath> resArr2 = new ArrayList<>(arr2);
        return resArr2;
    }

    public ArrayList<IbcPath> getIbcRollbackRelayer(String denom) {
        ArrayList<IbcPath> result = new ArrayList<>();
        IbcToken ibcToken = getIbcToken(denom.replaceAll("ibc/", ""));
        for (IbcPath ibcPath : mIbcPaths) {
            for (IbcPath.Path path : ibcPath.paths) {
                if (path.channel_id != null && path.channel_id.equalsIgnoreCase(ibcToken.channel_id)) {
                    result.add(ibcPath);
                }
            }
        }
        return result;
    }

    //COMMON DATA
    public NodeInfo mNodeInfo;
    public List<Validator> mAllValidators = new ArrayList<>();
    public List<Validator> mTopValidators = new ArrayList<>();
    public List<Validator> mOtherValidators = new ArrayList<>();
    public List<Validator> mMyValidators = new ArrayList<>();

    public List<Staking.Validator> mGRpcAllValidators = new ArrayList<>();
    public List<Staking.Validator> mGRpcTopValidators = new ArrayList<>();
    public List<Staking.Validator> mGRpcOtherValidators = new ArrayList<>();
    public List<Staking.Validator> mGRpcMyValidators = new ArrayList<>();


    public List<Validator> getMyValidators() {
        if (mMyValidators.isEmpty()) {
            final ArrayList<Validator> result = new ArrayList<>();
            if (mOkStaking != null && mOkStaking.validator_address != null) {
                for (String valAddr : mOkStaking.validator_address) {
                    for (Validator val : mAllValidators) {
                        if (val.operator_address.equals(valAddr)) {
                            result.add(val);
                        }
                    }
                }
            }

            if (!mMyDelegations.isEmpty() || !mMyUnbondings.isEmpty()) {
                for (Validator top : mAllValidators) {
                    boolean already = false;
                    for (BondingInfo bond : mMyDelegations) {
                        if (bond.validator_address.equals(top.operator_address)) {
                            already = true;
                            break;
                        }
                    }
                    for (UnbondingInfo unbond : mMyUnbondings) {
                        if (unbond.validator_address.equals(top.operator_address) && !already) {
                            already = true;
                            break;
                        }
                    }
                    if (already) result.add(top);
                }
            }

            mMyValidators = result;
            return result;
        } else {
            return mMyValidators;
        }
    }

    public ArrayList<BondingInfo> mMyDelegations = new ArrayList<>();
    public ArrayList<UnbondingInfo> mMyUnbondings = new ArrayList<>();
    public ArrayList<RewardInfo> mMyRewards = new ArrayList<>();

    //COMMON DATA FOR BINANCE
    public List<BnbToken> mBnbTokens = new ArrayList<>();
    public List<BnbTicker> mBnbTickers = new ArrayList<>();
    public List<ResBnbFee> mBnbFees = new ArrayList<>();

    //COMMON DATA FOR OKEX
    public ResOkStaking mOkStaking;
    public ResOkUnbonding mOkUnbonding;
    public ResOkTokenList mOkTokenList;
    public ResOkTickersList mOkTickersList;
    public BigDecimal mOKBPrice = BigDecimal.ZERO;

    //INCENTIVE DATA FOR SIF
    public SifIncentive.User mSifLmIncentive;

    //GRPC for KAVA
    public Map<String, QueryOuterClass.CurrentPriceResponse> mKavaTokenPrice = new HashMap<>();
    public IncentiveParam mIncentiveParam5;
    public IncentiveReward mIncentiveRewards;
    public Swap.Params mSwapParams;
    public Genesis.Params mCdpParams;
    public Hard.Params mHardParams;
    public ArrayList<Coin> mModuleCoins = new ArrayList<>();
    public ArrayList<CoinOuterClass.Coin> mReserveCoins = new ArrayList<>();
    public ArrayList<kava.hard.v1beta1.QueryOuterClass.DepositResponse> mMyHardDeposits = new ArrayList<>();
    public ArrayList<kava.hard.v1beta1.QueryOuterClass.BorrowResponse> mMyHardBorrows = new ArrayList<>();

    public String getChainId() {
        if (mNodeInfo != null) {
            return mNodeInfo.network;
        }
        return "";
    }

    public BigDecimal delegatedSumAmount() {
        BigDecimal result = BigDecimal.ZERO;
        for (BondingInfo bondingInfo : mMyDelegations) {
            if (bondingInfo.balance != null) {
                result = result.add(bondingInfo.getBalance());
            }
        }
        return result;
    }

    public BigDecimal delegatedAmountByValidator(String opAddress) {
        BigDecimal result = BigDecimal.ZERO;
        for (BondingInfo bondingInfo : mMyDelegations) {
            if (bondingInfo.validator_address.equals(opAddress) && bondingInfo.balance != null) {
                result = result.add(bondingInfo.getBalance());
            }
        }
        return result;
    }

    public BigDecimal unbondingSumAmount() {
        BigDecimal result = BigDecimal.ZERO;
        for (UnbondingInfo unbondingInfo : mMyUnbondings) {
            if (unbondingInfo.entries != null) {
                for (UnbondingInfo.Entry entry : unbondingInfo.entries) {
                    result = result.add(new BigDecimal(entry.balance));
                }
            }
        }
        return result;
    }

    public BigDecimal unbondingAmountByValidator(String opAddress) {
        BigDecimal result = BigDecimal.ZERO;
        for (UnbondingInfo unbondingInfo : mMyUnbondings) {
            if (unbondingInfo.validator_address.equals(opAddress) && unbondingInfo.entries != null) {
                for (UnbondingInfo.Entry entry : unbondingInfo.entries) {
                    result = result.add(new BigDecimal(entry.balance));
                }
            }
        }
        return result;
    }

    public BigDecimal rewardAmount(String denom) {
        BigDecimal result = BigDecimal.ZERO;
        for (RewardInfo rewardInfo : mMyRewards) {
            if (rewardInfo.reward != null) {
                for (Coin coin : rewardInfo.reward) {
                    if (coin.denom.equals(denom)) {
                        result = result.add(new BigDecimal(coin.amount).setScale(0, RoundingMode.DOWN));
                    }
                }

            }
        }
        return result;
    }

    public BigDecimal rewardAmountByValidator(String denom, String opAddress) {
        BigDecimal result = BigDecimal.ZERO;
        for (RewardInfo rewardInfo : mMyRewards) {
            if (rewardInfo.validator_address.equals(opAddress) && rewardInfo.reward != null) {
                for (Coin coin : rewardInfo.reward) {
                    if (coin.denom.equals(denom)) {
                        result = result.add(new BigDecimal(coin.amount).setScale(0, RoundingMode.DOWN));
                    }
                }
            }
        }
        return result;
    }

    public OkToken okToken(String denom) {
        for (OkToken token : mOkTokenList.data) {
            if (token.symbol.equals(denom)) {
                return token;
            }
        }
        return null;
    }

    public BigDecimal okDepositAmount() {
        BigDecimal sum = BigDecimal.ZERO;
        if (mOkStaking != null && !TextUtils.isEmpty(mOkStaking.tokens)) {
            sum = new BigDecimal(mOkStaking.tokens);
        }
        return sum;
    }

    public BigDecimal okWithdrawAmount() {
        BigDecimal sum = BigDecimal.ZERO;
        if (mOkUnbonding != null && !TextUtils.isEmpty(mOkUnbonding.quantity)) {
            sum = new BigDecimal(mOkUnbonding.quantity);
        }
        return sum;
    }

    public BigDecimal getAllMainAssetOld(String denom) {
        return delegatedSumAmount().add(unbondingSumAmount()).add(rewardAmount(denom));
    }

    public BnbToken getBnbToken(String denom) {
        for (BnbToken token : mBnbTokens) {
            if (token.symbol.equals(denom)) {
                return token;
            }
        }
        return null;
    }

    public BigDecimal getAllExToken(String denom) {
        BigDecimal result = BigDecimal.ZERO;
        if (denom.equals(TOKEN_OK)) {
            result = okDepositAmount().add(okWithdrawAmount());
        }
        return result;
    }

    //gRPC
    public tendermint.p2p.Types.NodeInfo mGRpcNodeInfo;
    public Any mGRpcAccount;

    public List<Staking.DelegationResponse> mGrpcDelegations = new ArrayList<>();
    public List<Staking.UnbondingDelegation> mGrpcUndelegations = new ArrayList<>();
    public List<Distribution.DelegationDelegatorReward> mGrpcRewards = new ArrayList<>();

    //COMMON DATA FOR STARNAME
    public starnamed.x.configuration.v1beta1.Types.Fees mGrpcStarNameFee;
    public starnamed.x.configuration.v1beta1.Types.Config mGrpcStarNameConfig;

    //Osmosis pool list
    public List<BalancerPool.Pool> mGrpcOsmosisPool = new ArrayList<>();

    //Gravity pool list
    public List<Liquidity.Pool> mGrpcGravityPools = new ArrayList<>();
    public Liquidity.Params mParams;

    //Gravity GDex Manager
    public ArrayList<GDexManager> mGDexManager = new ArrayList<>();
    //Gravity GDex Denom Supply
    public ArrayList<Coin> mGDexPoolTokens = new ArrayList<>();

    //gRPC funcs
    public String getChainIdGrpc() {
        if (mGRpcNodeInfo != null) {
            return mGRpcNodeInfo.getNetwork();
        }
        return "";
    }

    public ArrayList<Vesting.Period> onParseRemainVestingsByDenom(String denom) {
        ArrayList<Vesting.Period> result = new ArrayList<>();
        if (mGRpcAccount != null && mGRpcAccount.getTypeUrl().contains(ModelsProfile.Profile.getDescriptor().getFullName())) {
            try {
                ModelsProfile.Profile profile = ModelsProfile.Profile.parseFrom(mGRpcAccount.getValue());
                if (profile.getAccount().getTypeUrl().contains(Vesting.PeriodicVestingAccount.getDescriptor().getFullName())) {
                    Vesting.PeriodicVestingAccount vestingAccount = Vesting.PeriodicVestingAccount.parseFrom(profile.getAccount().getValue());
                    return WDp.onParsePeriodicRemainVestingsByDenom(vestingAccount, denom);

                } else if (profile.getAccount().getTypeUrl().contains(Vesting.ContinuousVestingAccount.getDescriptor().getFullName())) {
                    Vesting.ContinuousVestingAccount vestingAccount = Vesting.ContinuousVestingAccount.parseFrom(profile.getAccount().getValue());
                    long cTime = Calendar.getInstance().getTime().getTime();
                    long vestingEnd = vestingAccount.getBaseVestingAccount().getEndTime() * 1000;
                    if (cTime < vestingEnd) {
                        for (CoinOuterClass.Coin vesting : vestingAccount.getBaseVestingAccount().getOriginalVestingList()) {
                            if (vesting.getDenom().equals(denom)) {
                                result.add(Vesting.Period.newBuilder().setLength(vestingEnd).addAllAmount(vestingAccount.getBaseVestingAccount().getOriginalVestingList()).build());
                            }
                        }
                    }
                }
            } catch (InvalidProtocolBufferException e) {
            }

        } else {
            if (mGRpcAccount != null && mGRpcAccount.getTypeUrl().contains(Vesting.PeriodicVestingAccount.getDescriptor().getFullName())) {
                try {
                    Vesting.PeriodicVestingAccount vestingAccount = Vesting.PeriodicVestingAccount.parseFrom(mGRpcAccount.getValue());
                    return WDp.onParsePeriodicRemainVestingsByDenom(vestingAccount, denom);
                } catch (InvalidProtocolBufferException e) {
                }

            } else if (mGRpcAccount != null && mGRpcAccount.getTypeUrl().contains(Vesting.ContinuousVestingAccount.getDescriptor().getFullName())) {
                try {
                    Vesting.ContinuousVestingAccount vestingAccount = Vesting.ContinuousVestingAccount.parseFrom(mGRpcAccount.getValue());
                    long cTime = Calendar.getInstance().getTime().getTime();
                    long vestingEnd = vestingAccount.getBaseVestingAccount().getEndTime() * 1000;
                    if (cTime < vestingEnd) {
                        for (CoinOuterClass.Coin vesting : vestingAccount.getBaseVestingAccount().getOriginalVestingList()) {
                            if (vesting.getDenom().equals(denom)) {
                                result.add(Vesting.Period.newBuilder().setLength(vestingEnd).addAllAmount(vestingAccount.getBaseVestingAccount().getOriginalVestingList()).build());
                            }
                        }
                    }
                } catch (InvalidProtocolBufferException e) {
                }
            }
        }
        return result;
    }

    public BigDecimal getDelegationSum() {
        BigDecimal sum = BigDecimal.ZERO;
        for (Staking.DelegationResponse delegation : mGrpcDelegations) {
            sum = sum.add(new BigDecimal(delegation.getBalance().getAmount()));
        }
        return sum;
    }

    public BigDecimal getDelegation(String valOpAddress) {
        BigDecimal result = BigDecimal.ZERO;
        for (Staking.DelegationResponse delegation : mGrpcDelegations) {
            if (delegation.getDelegation().getValidatorAddress().equals(valOpAddress)) {
                result = new BigDecimal(delegation.getBalance().getAmount());
            }
        }
        return result;
    }

    public Staking.DelegationResponse getDelegationInfo(String valOpAddress) {
        for (Staking.DelegationResponse delegation : mGrpcDelegations) {
            if (delegation.getDelegation().getValidatorAddress().equals(valOpAddress)) {
                return delegation;
            }
        }
        return null;
    }

    public BigDecimal getUndelegationSum() {
        BigDecimal sum = BigDecimal.ZERO;
        for (Staking.UnbondingDelegation undelegation : mGrpcUndelegations) {
            sum = sum.add(getAllUnbondingBalance(undelegation));
        }
        return sum;
    }

    public BigDecimal getUndelegation(String valOpAddress) {
        BigDecimal result = BigDecimal.ZERO;
        for (Staking.UnbondingDelegation undelegation : mGrpcUndelegations) {
            if (undelegation.getValidatorAddress().equals(valOpAddress)) {
                result = getAllUnbondingBalance(undelegation);
            }
        }
        return result;
    }

    public Staking.UnbondingDelegation getUndelegationInfo(String valOpAddress) {
        for (Staking.UnbondingDelegation undelegation : mGrpcUndelegations) {
            if (undelegation.getValidatorAddress().equals(valOpAddress)) {
                return undelegation;
            }
        }
        return null;
    }

    public BigDecimal getAllUnbondingBalance(Staking.UnbondingDelegation undelegation) {
        BigDecimal result = BigDecimal.ZERO;
        if (undelegation != null && undelegation.getEntriesList().size() > 0) {
            for (Staking.UnbondingDelegationEntry entry : undelegation.getEntriesList()) {
                result = result.add(new BigDecimal(entry.getBalance()));
            }
        }
        return result;
    }

    public BigDecimal getRewardSum(String denom) {
        BigDecimal sum = BigDecimal.ZERO;
        for (Distribution.DelegationDelegatorReward reward : mGrpcRewards) {
            sum = sum.add(WUtil.decCoinAmount(reward.getRewardList(), denom));
        }
        return sum;
    }

    public BigDecimal getReward(String denom, String valOpAddress) {
        BigDecimal result = BigDecimal.ZERO;
        for (Distribution.DelegationDelegatorReward reward : mGrpcRewards) {
            if (reward.getValidatorAddress().equals(valOpAddress)) {
                result = WUtil.decCoinAmount(reward.getRewardList(), denom);
            }
        }
        return result;
    }

    public BigDecimal getAllMainAsset(String denom) {
        return getDelegationSum().add(getUndelegationSum()).add(getRewardSum(denom));
    }

    public Staking.Validator getValidatorInfo(String valOpAddress) {
        for (Staking.Validator val : mGRpcAllValidators) {
            if (val.getOperatorAddress().equals(valOpAddress)) {
                return val;
            }
        }
        return null;
    }

    // for kava funcs
    public Genesis.CollateralParam getCollateralParamByType(String type) {
        Genesis.CollateralParam result = null;
        if (mCdpParams != null && mCdpParams.getCollateralParamsList().size() > 0) {
            for (Genesis.CollateralParam collateralParam : mCdpParams.getCollateralParamsList()) {
                if (collateralParam.getType().equalsIgnoreCase(type)) {
                    return collateralParam;
                }
            }
        }
        return result;
    }

    public BigDecimal getKavaOraclePrice(String market_id) {
        BigDecimal price = BigDecimal.ZERO;
        if (mKavaTokenPrice != null && mKavaTokenPrice.get(market_id).getPrice() != null) {
            price = new BigDecimal(mKavaTokenPrice.get(market_id).getPrice()).movePointLeft(18);
        }
        return price;
    }

    // for starname funcs
    public BigDecimal getStarNameRegisterDomainFee(String domain, String type) {
        BigDecimal feeAmount = BigDecimal.ZERO;
        if (mGrpcStarNameFee == null) {
            return feeAmount;
        }
        if (TextUtils.isEmpty(domain) || domain.length() <= 3) {
            return feeAmount;
        } else if (domain.length() == 4) {
            feeAmount = new BigDecimal(mGrpcStarNameFee.getRegisterDomain4()).divide(new BigDecimal(mGrpcStarNameFee.getFeeCoinPrice()), 0, RoundingMode.DOWN);
        } else if (domain.length() == 5) {
            feeAmount = new BigDecimal(mGrpcStarNameFee.getRegisterDomain5()).divide(new BigDecimal(mGrpcStarNameFee.getFeeCoinPrice()), 0, RoundingMode.DOWN);
        } else {
            feeAmount = new BigDecimal(mGrpcStarNameFee.getRegisterDomainDefault()).divide(new BigDecimal(mGrpcStarNameFee.getFeeCoinPrice()), 0, RoundingMode.DOWN);
        }
        if (type.equals("open")) {
            feeAmount = feeAmount.multiply(new BigDecimal(mGrpcStarNameFee.getRegisterOpenDomainMultiplier()).movePointLeft(18));
        }
        return feeAmount;
    }

    public BigDecimal getStarNameRegisterAccountFee(String type) {
        BigDecimal feeAmount = BigDecimal.ZERO;
        if (mGrpcStarNameFee == null) {
            return feeAmount;
        }
        if (type.equals("open")) {
            return new BigDecimal(mGrpcStarNameFee.getRegisterAccountOpen()).divide(new BigDecimal(mGrpcStarNameFee.getFeeCoinPrice()), 0, RoundingMode.DOWN);
        } else {
            return new BigDecimal(mGrpcStarNameFee.getRegisterAccountClosed()).divide(new BigDecimal(mGrpcStarNameFee.getFeeCoinPrice()), 0, RoundingMode.DOWN);
        }
    }

    public BigDecimal getStarNameRenewDomainFee(String domain, String type) {
        BigDecimal feeAmount = BigDecimal.ZERO;
        if (mGrpcStarNameFee == null) {
            return feeAmount;
        }
        if (type == "open") {
            return new BigDecimal(mGrpcStarNameFee.getRenewDomainOpen()).divide(new BigDecimal(mGrpcStarNameFee.getFeeCoinPrice()), 0, RoundingMode.DOWN);
        } else {
            BigDecimal registerFee = getStarNameRegisterDomainFee(domain, type);
            BigDecimal addtionalFee = new BigDecimal(mGrpcStarNameFee.getRegisterAccountClosed()).divide(new BigDecimal(mGrpcStarNameFee.getFeeCoinPrice()), 0, RoundingMode.DOWN);
            return registerFee.add(addtionalFee);
        }
    }

    public BigDecimal getStarNameRenewAccountFee(String type) {
        BigDecimal feeAmount = BigDecimal.ZERO;
        if (mGrpcStarNameFee == null) {
            return feeAmount;
        }
        if (type.equals("open")) {
            return new BigDecimal(mGrpcStarNameFee.getRegisterAccountOpen()).divide(new BigDecimal(mGrpcStarNameFee.getFeeCoinPrice()), 0, RoundingMode.DOWN);
        } else {
            return new BigDecimal(mGrpcStarNameFee.getRegisterAccountClosed()).divide(new BigDecimal(mGrpcStarNameFee.getFeeCoinPrice()), 0, RoundingMode.DOWN);
        }
    }

    public BigDecimal getReplaceFee() {
        BigDecimal feeAmount = BigDecimal.ZERO;
        if (mGrpcStarNameFee == null) {
            return feeAmount;
        }
        return new BigDecimal(mGrpcStarNameFee.getReplaceAccountResources()).divide(new BigDecimal(mGrpcStarNameFee.getFeeCoinPrice()), 0, RoundingMode.DOWN);
    }

    public long getStarNameRegisterDomainExpireTime() {
        if (mGrpcStarNameConfig == null) {
            return 0;
        }
        return Calendar.getInstance().getTimeInMillis() + mGrpcStarNameConfig.getDomainRenewalPeriod().getSeconds() * 1000;
    }

    public long getRenewExpireTime(String type, long currentExpire) {
        if (mGrpcStarNameConfig == null) {
            return 0;
        }
        if (type.equals(IOV_MSG_TYPE_RENEW_DOMAIN)) {
            return currentExpire + mGrpcStarNameConfig.getDomainRenewalPeriod().getSeconds() * 1000;
        } else if (type.equals(IOV_MSG_TYPE_RENEW_ACCOUNT)) {
            return currentExpire + mGrpcStarNameConfig.getAccountRenewalPeriod().getSeconds() * 1000;
        }
        return 0;
    }

    public Liquidity.Pool getGravityPoolByDenom(String denom) {
        for (Liquidity.Pool pool : mGrpcGravityPools) {
            if (pool.getPoolCoinDenom().equals(denom)) {
                return pool;
            }
        }
        return null;
    }

    public ChainParam.GdexStatus getParamGravityPoolByDenom(String denom) {
        for (ChainParam.GdexStatus gdexStatus : mChainParam.getmGdexList()) {
            if (gdexStatus.pool_token.equals(denom)) {
                return gdexStatus;
            }
        }
        return null;
    }


    public BalancerPool.Pool getOsmosisPoolByDenom(String denom) {
        for (BalancerPool.Pool pool : mGrpcOsmosisPool) {
            if (pool.getTotalShares().getDenom().equals(denom)) {
                return pool;
            }
        }
        return null;
    }


    public void setValSorting(int sort) {
        getSharedPreferences().edit().putInt(BaseConstant.PRE_VALIDATOR_SORTING, sort).commit();
    }

    public int getValSorting() {
        return getSharedPreferences().getInt(BaseConstant.PRE_VALIDATOR_SORTING, 1);
    }

    public void setMyValSorting(int sort) {
        getSharedPreferences().edit().putInt(BaseConstant.PRE_MY_VALIDATOR_SORTING, sort).commit();
    }

    public int getMyValSorting() {
        return getSharedPreferences().getInt(BaseConstant.PRE_MY_VALIDATOR_SORTING, 1);
    }

    public boolean getUsingAppLock() {
        return getSharedPreferences().getBoolean(BaseConstant.PRE_USING_APP_LOCK, false);
    }

    public void setUsingAppLock(boolean using) {
        getSharedPreferences().edit().putBoolean(BaseConstant.PRE_USING_APP_LOCK, using).commit();
    }

    public boolean getUsingFingerPrint() {
        return getSharedPreferences().getBoolean(BaseConstant.PRE_USING_FINGERPRINT, false);
    }

    public void setUsingFingerPrint(boolean using) {
        getSharedPreferences().edit().putBoolean(BaseConstant.PRE_USING_FINGERPRINT, using).commit();
    }

    public int getAppLockTriggerTime() {
        return getSharedPreferences().getInt(BaseConstant.PRE_APP_LOCK_TIME, 0);
    }

    public void setAppLockTriggerTime(int trigger) {
        getSharedPreferences().edit().putInt(BaseConstant.PRE_APP_LOCK_TIME, trigger).commit();
    }

    public long getAppLockLeaveTime() {
        return getSharedPreferences().getLong(BaseConstant.PRE_APP_LOCK_LEAVE_TIME, 0);
    }

    public void setAppLockLeaveTime() {
        getSharedPreferences().edit().putLong(BaseConstant.PRE_APP_LOCK_LEAVE_TIME, System.currentTimeMillis()).commit();
    }

    public int getAppLockLeaveTimeString() {
        switch (getAppLockTriggerTime()) {
            case 1:
                return R.string.str_applock_time_10sec;
            case 2:
                return R.string.str_applock_time_30sec;
            case 3:
                return R.string.str_applock_time_60sec;
            default:
                return R.string.str_applock_time_immediately;
        }
    }

    public void setUserHiddenChains(List<String> chains) {
        if (!chains.isEmpty()) {
            final String json = gson.toJson(chains);
            getSharedPreferences().edit().putString(PRE_USER_HIDEN_CHAINS, json).commit();
        } else {
            getSharedPreferences().edit().putString(PRE_USER_HIDEN_CHAINS, null).commit();
        }
    }

    public void setUserHiddenBaseChains(List<BaseChain> hidedChains) {
        final ArrayList<String> items = new ArrayList<>();
        for (BaseChain baseChain : hidedChains) {
            items.add(baseChain.getChainName());
        }
        setUserHiddenChains(items);
    }

    public List<String> getUserHiddenChains() {
        String json = getSharedPreferences().getString(PRE_USER_HIDEN_CHAINS, null);
        List<String> result = new ArrayList<>();
        if (json != null) {
            try {
                result = gson.fromJson(json, new TypeToken<List<Coin>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return result;
    }

    public void setUserSortedBaseChains(List<BaseChain> chains) {
        final ArrayList<String> items = new ArrayList<>();
        for (BaseChain baseChain : chains) {
            items.add(baseChain.getChainName());
        }
        setUserSortedChains(items);
    }

    public void setUserSortedChains(List<String> items) {
        if (!items.isEmpty()) {
            final String json = gson.toJson(items);
            getSharedPreferences().edit().putString(PRE_USER_SORTED_CHAINS, json).apply();
        } else {
            getSharedPreferences().edit().putString(PRE_USER_SORTED_CHAINS, null).apply();
        }
    }

    public List<String> getUserSortedChains() {
        String json = getSharedPreferences().getString(PRE_USER_SORTED_CHAINS, null);
        List<String> result = new ArrayList<>();
        if (json != null) {
            try {
                result = gson.fromJson(json, new TypeToken<List<String>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public List<BaseChain> userHideChains() {
        ArrayList<BaseChain> result = new ArrayList<>();
        ArrayList<BaseChain> mAllChains = new ArrayList<>();
        List<String> hiddenChains = getUserHiddenChains();
        for (BaseChain baseChain : BaseChain.Companion.getChains()) {
            if (baseChain.isSupported() && !baseChain.equals(BaseChain.COSMOS_MAIN.INSTANCE)) {
                mAllChains.add(baseChain);
            }
        }
        for (BaseChain baseChain : mAllChains) {
            if (hiddenChains.contains(baseChain.getChainName())) {
                result.add(baseChain);
            }
        }

        return result;
    }

    public List<BaseChain> userDisplayChains() {
        List<BaseChain> result = new ArrayList<>();
        List<BaseChain> mAllChains = new ArrayList<>();
        List<BaseChain> hiddenChains = userHideChains();
        for (BaseChain baseChain : BaseChain.Companion.getChains()) {
            if (baseChain.isSupported() && !baseChain.equals(BaseChain.COSMOS_MAIN.INSTANCE)) {
                mAllChains.add(baseChain);
            }
        }
        for (BaseChain baseChain : mAllChains) {
            if (!hiddenChains.contains(baseChain)) {
                result.add(baseChain);
            }
        }
        ArrayList<BaseChain> sorted = new ArrayList<>();
        for (String chainName : getUserSortedChains()) {
            if (result.contains(BaseChain.getChain(chainName))) {   // TODO optimize
                sorted.add(BaseChain.getChain(chainName));
            }
        }
        for (BaseChain baseChain : result) {
            if (!sorted.contains(baseChain)) {
                sorted.add(baseChain);
            }
        }
        return sorted;
    }

    public List<BaseChain> userSortedChains() {
        List<BaseChain> result = new ArrayList<>();
        List<BaseChain> rawDpChains = userDisplayChains();
        List<String> orderedChains = getUserHiddenChains();
        for (BaseChain chain : rawDpChains) {
            if (!orderedChains.contains(chain.getChainName())) {
                result.add(chain);
            }
        }
        return result;
    }

    public ArrayList<BaseChain> dpSortedChains() {
        ArrayList<BaseChain> result = new ArrayList<>();
        result.add(BaseChain.COSMOS_MAIN.INSTANCE);
        List<BaseChain> rawDpChains = userDisplayChains();
        List<String> orderedChains = getUserHiddenChains();
        for (BaseChain chain : rawDpChains) {
            if (!orderedChains.contains(chain.getChainName())) {
                result.add(chain);
            }
        }
        return result;
    }

    public void setExpandedChains(List<String> items) {
        final String json = gson.toJson(items);
        if (!items.isEmpty()) {
            getSharedPreferences().edit().putString(PRE_USER_EXPANDED_CHAINS, json).apply();
        } else {
            getSharedPreferences().edit().putString(PRE_USER_EXPANDED_CHAINS, null).apply();
        }
    }

    public List<String> getExpandedChains() {
        String json = getSharedPreferences().getString(PRE_USER_EXPANDED_CHAINS, null);
        ArrayList<String> result = new ArrayList<>();
        if (json != null) {
            try {
                result = gson.fromJson(json, new TypeToken<List<String>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public void clear() {
        mIbcPaths = new ArrayList<>();
        mIbcTokens = new ArrayList<>();
        mChainParam = null;
        mAssets = new ArrayList<>();
        mCw20Assets = new ArrayList<>();

        mSifLmIncentive = null;

        mNodeInfo = null;
        mAllValidators = new ArrayList<>();
        mMyValidators = new ArrayList<>();
        mTopValidators = new ArrayList<>();
        mOtherValidators = new ArrayList<>();

        mMyDelegations = new ArrayList<>();
        mMyUnbondings = new ArrayList<>();
        mMyRewards = new ArrayList<>();

        //kava GRPC
        mIncentiveParam5 = null;
        mIncentiveRewards = null;
        mMyHardDeposits = new ArrayList<>();
        mMyHardBorrows = new ArrayList<>();
        mModuleCoins = new ArrayList<>();
        mReserveCoins = new ArrayList<>();


        //binance
        mBnbTokens = new ArrayList<>();
        mBnbTickers = new ArrayList<>();
        mBnbFees = new ArrayList<>();

        //gRPC
        mGRpcNodeInfo = null;
        mGRpcAccount = null;
        mGRpcTopValidators = new ArrayList<>();
        mGRpcOtherValidators = new ArrayList<>();
        mGRpcAllValidators = new ArrayList<>();
        mGRpcMyValidators = new ArrayList<>();

        mGrpcDelegations = new ArrayList<>();
        mGrpcUndelegations = new ArrayList<>();
        mGrpcRewards = new ArrayList<>();

        mGrpcStarNameFee = null;
        mGrpcStarNameConfig = null;

        mGrpcGravityPools = new ArrayList<>();

        //okex
        mOkStaking = null;
        mOkUnbonding = null;
        mOkTokenList = null;
        mOkTickersList = null;
    }
}
