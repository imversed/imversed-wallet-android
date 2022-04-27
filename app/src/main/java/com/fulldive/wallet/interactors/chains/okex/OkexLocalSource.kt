package com.fulldive.wallet.interactors.chains.okex

import com.fulldive.wallet.di.modules.DefaultLocalStorageModule
import com.fulldive.wallet.extensions.safeCompletable
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Completable
import wannabit.io.cosmostaion.base.BaseData
import wannabit.io.cosmostaion.model.type.Validator
import wannabit.io.cosmostaion.network.res.*
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
}
