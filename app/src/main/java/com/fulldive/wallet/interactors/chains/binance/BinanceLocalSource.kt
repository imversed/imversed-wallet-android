package com.fulldive.wallet.interactors.chains.binance

import android.text.SpannableString
import android.util.Log
import com.fulldive.wallet.di.modules.DefaultLocalStorageModule
import com.fulldive.wallet.extensions.safeCompletable
import com.fulldive.wallet.models.BaseChain
import com.fulldive.wallet.models.Currency
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Completable
import wannabit.io.cosmostaion.base.BaseData
import wannabit.io.cosmostaion.dao.BnbTicker
import wannabit.io.cosmostaion.dao.BnbToken
import wannabit.io.cosmostaion.network.res.ResBnbFee
import wannabit.io.cosmostaion.network.res.ResNodeInfo
import wannabit.io.cosmostaion.utils.PriceProvider
import wannabit.io.cosmostaion.utils.WDp
import wannabit.io.cosmostaion.utils.WUtil
import java.math.BigDecimal
import javax.inject.Inject

@ProvidedBy(DefaultLocalStorageModule::class)
class BinanceLocalSource @Inject constructor(
    private val baseData: BaseData
) {
    fun setTokens(items: List<BnbToken>): Completable {
        return safeCompletable {
            baseData.mBnbTokens = items
        }
    }

    fun setTickers(items: List<BnbTicker>): Completable {
        return safeCompletable {
            baseData.mBnbTickers = items
        }
    }

    fun setFees(items: List<ResBnbFee>): Completable {
        return safeCompletable {
            baseData.mBnbFees = items
        }
    }

    fun setNodeInfo(nodeInfo: ResNodeInfo): Completable {
        return safeCompletable {
            baseData.mNodeInfo = nodeInfo.node_info
        }
    }

    fun getBnbAmount(currency: Currency, denom: String, amount: BigDecimal, priceProvider: PriceProvider): SpannableString {
        val convertAmount = WUtil.getBnbConvertAmount(baseData, denom, amount)
        Log.d("fftf", "convertAmount: $convertAmount")
        return WDp.dpUserCurrencyValue(
            baseData,
            currency,
            BaseChain.BNB_MAIN.mainDenom,
            convertAmount,
            0,
            priceProvider
        )
    }
}