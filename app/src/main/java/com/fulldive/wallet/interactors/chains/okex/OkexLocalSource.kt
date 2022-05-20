package com.fulldive.wallet.interactors.chains.okex

import android.text.SpannableString
import com.fulldive.wallet.di.modules.DefaultLocalStorageModule
import com.fulldive.wallet.extensions.safeCompletable
import com.fulldive.wallet.models.BaseChain
import com.fulldive.wallet.models.Currency
import com.fulldive.wallet.models.WalletBalance
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Completable
import wannabit.io.cosmostaion.base.BaseData
import wannabit.io.cosmostaion.dao.OkToken
import wannabit.io.cosmostaion.model.type.Validator
import wannabit.io.cosmostaion.network.res.*
import wannabit.io.cosmostaion.utils.PriceProvider
import wannabit.io.cosmostaion.utils.WDp
import java.math.BigDecimal
import javax.inject.Inject

@ProvidedBy(DefaultLocalStorageModule::class)
class OkexLocalSource @Inject constructor(
    private val baseData: BaseData
) {

    fun setValidators(items: List<Validator>): Completable {
        return safeCompletable {
            baseData.mAllValidators = items

            baseData.mTopValidators = items.filter { it.status == Validator.BONDED }
            baseData.mOtherValidators = items.filter { it.status != Validator.BONDED }
            baseData.mMyValidators.clear() // TODO: remove
        }
    }

    fun setTickers(tickers: ResOkTickersList): Completable {
        return safeCompletable {
            baseData.mOkTickersList = tickers
        }
    }

    fun setTokens(tokens: ResOkTokenList): Completable {
        return safeCompletable {
            baseData.mOkTokenList = tokens
        }
    }

    fun setUnbonding(unbonding: ResOkUnbonding): Completable {
        return safeCompletable {
            baseData.mOkUnbonding = unbonding
        }
    }

    fun setStakingInfo(stakingInfo: ResOkStaking): Completable {
        return safeCompletable {
            baseData.mOkStaking = stakingInfo
            baseData.mMyValidators.clear() // TODO: remove
        }
    }

    fun setNodeInfo(nodeInfo: ResNodeInfo): Completable {
        return safeCompletable {
            baseData.mNodeInfo = nodeInfo.node_info
        }
    }

    fun getOkAmount(
        currency: Currency,
        balance: WalletBalance,
        priceProvider: PriceProvider
    ): SpannableString {
        val convertAmount: BigDecimal = WDp.convertTokenToOkt(
            balance,
            baseData,
            balance.denom,
            priceProvider
        )
        return WDp.dpUserCurrencyValue(
            baseData,
            currency,
            BaseChain.OKEX_MAIN.mainDenom,
            convertAmount,
            0,
            priceProvider
        )
    }
}
