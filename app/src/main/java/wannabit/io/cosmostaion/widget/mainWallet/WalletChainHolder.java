package wannabit.io.cosmostaion.widget.mainWallet;

import static wannabit.io.cosmostaion.base.BaseConstant.CONST_PW_TX_PROFILE;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.fulldive.wallet.interactors.accounts.AccountsInteractor;
import com.fulldive.wallet.interactors.settings.SettingsInteractor;
import com.fulldive.wallet.models.BaseChain;
import com.fulldive.wallet.presentation.main.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

import desmos.profiles.v1beta1.ModelsProfile;
import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.activities.ValidatorListActivity;
import wannabit.io.cosmostaion.activities.VoteListActivity;
import wannabit.io.cosmostaion.activities.chains.cosmos.GravityListActivity;
import wannabit.io.cosmostaion.activities.chains.desmos.ProfileActivity;
import wannabit.io.cosmostaion.activities.chains.desmos.ProfileDetailActivity;
import wannabit.io.cosmostaion.activities.chains.kava.DAppsList5Activity;
import wannabit.io.cosmostaion.activities.chains.nft.NFTListActivity;
import wannabit.io.cosmostaion.activities.chains.osmosis.LabsListActivity;
import wannabit.io.cosmostaion.activities.chains.sif.SifDexListActivity;
import wannabit.io.cosmostaion.activities.chains.starname.StarNameListActivity;
import wannabit.io.cosmostaion.base.BaseData;
import wannabit.io.cosmostaion.dao.Account;
import wannabit.io.cosmostaion.dialog.Dialog_WatchMode;
import wannabit.io.cosmostaion.utils.WDp;
import wannabit.io.cosmostaion.utils.WUtil;
import wannabit.io.cosmostaion.widget.BaseHolder;

public class WalletChainHolder extends BaseHolder {
    public CardView mTvChainCard;
    public ImageView mTvChainIcon;
    public TextView mTvChainDenom;
    public TextView mTvChainTotal, mTvChainValue, mTvChainAvailable, mTvChainDelegated, mTvChainUnBonding, mTvChainRewards;
    public RelativeLayout mChainVestingLayer;
    public TextView mTvChainVesting;
    public RelativeLayout mBtnStake, mBtnVote, mBtnDex, mBtnWalletConnect;
    public TextView mBtnDexTitle;

    public WalletChainHolder(@NonNull View itemView) {
        super(itemView);
        mTvChainCard = itemView.findViewById(R.id.cardView);
        mTvChainIcon = itemView.findViewById(R.id.chain_icon);
        mTvChainDenom = itemView.findViewById(R.id.chain_denom);
        mTvChainTotal = itemView.findViewById(R.id.chain_amount);
        mTvChainValue = itemView.findViewById(R.id.chain_value);
        mTvChainAvailable = itemView.findViewById(R.id.chain_available);
        mTvChainDelegated = itemView.findViewById(R.id.chain_delegate);
        mTvChainUnBonding = itemView.findViewById(R.id.chain_unbonding);
        mTvChainRewards = itemView.findViewById(R.id.chain_reward);

        mChainVestingLayer = itemView.findViewById(R.id.chain_vesting_layer);
        mTvChainVesting = itemView.findViewById(R.id.chain_vesting);

        mBtnStake = itemView.findViewById(R.id.btn_delegate);
        mBtnVote = itemView.findViewById(R.id.btn_vote);
        mBtnDex = itemView.findViewById(R.id.btn_dex);
        mBtnDexTitle = itemView.findViewById(R.id.dex_title);
        mBtnWalletConnect = itemView.findViewById(R.id.btn_wallet_connect);
    }

    public void onBindHolder(@NotNull MainActivity mainActivity) {
        final SettingsInteractor settingsInteractor = mainActivity.getAppInjector().getInstance(SettingsInteractor.class);
        final BaseData baseData = mainActivity.getBaseDao();
        final String denom = mainActivity.getBaseChain().getMainDenom();
        final int decimal = mainActivity.getBaseChain().getDivideDecimal();
        mTvChainCard.setCardBackgroundColor(WDp.getChainBgColor(mainActivity, mainActivity.getBaseChain()));
        WUtil.getWalletData(mainActivity.getBaseChain(), mTvChainIcon, mTvChainDenom);

        final BigDecimal availableAmount = baseData.getAvailable(denom);
        final BigDecimal vestingAmount = baseData.getVesting(denom);
        final BigDecimal delegateAmount = baseData.getDelegationSum();
        final BigDecimal unbondingAmount = baseData.getUndelegationSum();
        final BigDecimal rewardAmount = baseData.getRewardSum(denom);
        final BigDecimal totalAmount = baseData.getAllMainAsset(denom);

        mTvChainTotal.setText(WDp.getDpAmount2(totalAmount, decimal, 6));
        mTvChainAvailable.setText(WDp.getDpAmount2(availableAmount, decimal, 6));
        mTvChainVesting.setText(WDp.getDpAmount2(vestingAmount, decimal, 6));
        mTvChainDelegated.setText(WDp.getDpAmount2(delegateAmount, decimal, 6));
        mTvChainUnBonding.setText(WDp.getDpAmount2(unbondingAmount, decimal, 6));
        mTvChainRewards.setText(WDp.getDpAmount2(rewardAmount, decimal, 6));
        mTvChainValue.setText(WDp.dpUserCurrencyValue(baseData, settingsInteractor.getCurrency(), denom, totalAmount, decimal));

        if (!vestingAmount.equals(BigDecimal.ZERO)) {
            mChainVestingLayer.setVisibility(View.VISIBLE);
        } else {
            mChainVestingLayer.setVisibility(View.GONE);
        }

        mainActivity.getAppInjector().getInstance(AccountsInteractor.class).updateLastTotal(mainActivity.getAccount().id, totalAmount.toPlainString());

        mBtnStake.setOnClickListener(v -> {
            Intent validators = new Intent(mainActivity, ValidatorListActivity.class);
            mainActivity.startActivity(validators);
        });
        mBtnVote.setOnClickListener(v -> {
            Intent proposals = new Intent(mainActivity, VoteListActivity.class);
            mainActivity.startActivity(proposals);
        });

        // dex, nft, desmos profile setting
        WUtil.getDexTitle(mainActivity.getBaseChain(), mBtnDex, mBtnDexTitle);
        mBtnDex.setOnClickListener(v -> {
            if (mainActivity.getBaseChain().equals(BaseChain.DESMOS_MAIN.INSTANCE)) {
                onClickProfile(mainActivity.getBaseDao(), mainActivity.getBaseChain(), mainActivity.getAccount(), mainActivity);
            } else {
                mainActivity.startActivity(getDexIntent(mainActivity, mainActivity.getBaseChain()));
            }
        });

        if (mainActivity.getBaseChain().equals(BaseChain.COSMOS_MAIN.INSTANCE)) {
            mBtnWalletConnect.setVisibility(View.VISIBLE);
        } else {
            mBtnWalletConnect.setVisibility(View.GONE);
        }
        mBtnWalletConnect.setOnClickListener(v -> {
            Toast.makeText(mainActivity, R.string.error_prepare, Toast.LENGTH_SHORT).show();
        });
    }

    public void onClickProfile(BaseData baseDao, BaseChain baseChain, Account account, MainActivity activity) {
        if (baseDao.mGRpcNodeInfo != null && baseDao.mGRpcAccount != null && baseDao.mGRpcAccount.getTypeUrl().contains(ModelsProfile.Profile.getDescriptor().getFullName())) {
            Intent airdrop = new Intent(activity, ProfileDetailActivity.class);
            activity.startActivity(airdrop);
        } else if (account.hasPrivateKey) {
            BigDecimal available = baseDao.getAvailable(baseChain.getMainDenom());
            BigDecimal txFee = WUtil.getEstimateGasFeeAmount(activity, baseChain, CONST_PW_TX_PROFILE, 0);
            if (available.compareTo(txFee) <= 0) {
                Toast.makeText(activity, R.string.error_not_enough_fee, Toast.LENGTH_SHORT).show();
                return;
            }
            Intent profile = new Intent(activity, ProfileActivity.class);
            activity.startActivity(profile);
        } else {
            activity.showDialog(Dialog_WatchMode.newInstance());
        }
    }

    public static Intent getDexIntent(MainActivity mainActivity, BaseChain chain) {
        if (chain.equals(BaseChain.COSMOS_MAIN.INSTANCE)) {
            return new Intent(mainActivity, GravityListActivity.class);
        } else if (chain.equals(BaseChain.IRIS_MAIN.INSTANCE) || chain.equals(BaseChain.CRYPTO_MAIN.INSTANCE)) {
            return new Intent(mainActivity, NFTListActivity.class);
        } else if (chain.equals(BaseChain.IOV_MAIN.INSTANCE)) {
            return new Intent(mainActivity, StarNameListActivity.class);
        } else if (chain.equals(BaseChain.KAVA_MAIN.INSTANCE)) {
            return new Intent(mainActivity, DAppsList5Activity.class);
        } else if (chain.equals(BaseChain.SIF_MAIN.INSTANCE)) {
            return new Intent(mainActivity, SifDexListActivity.class);
        } else if (chain.equals(BaseChain.OSMOSIS_MAIN.INSTANCE)) {
            return new Intent(mainActivity, LabsListActivity.class);
        } else {
            return null;
        }
    }
}
