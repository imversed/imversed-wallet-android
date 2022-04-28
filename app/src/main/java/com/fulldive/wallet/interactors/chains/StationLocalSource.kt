package com.fulldive.wallet.interactors.chains

import com.fulldive.wallet.di.modules.DefaultLocalStorageModule
import com.fulldive.wallet.extensions.completeCallable
import com.fulldive.wallet.extensions.safeCompletable
import com.fulldive.wallet.extensions.safeSingle
import com.fulldive.wallet.models.BaseChain
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Completable
import io.reactivex.Single
import wannabit.io.cosmostaion.base.BaseData
import wannabit.io.cosmostaion.dao.ChainParam
import wannabit.io.cosmostaion.dao.IbcPath
import wannabit.io.cosmostaion.dao.IbcToken
import wannabit.io.cosmostaion.dao.Price
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ProvidedBy(DefaultLocalStorageModule::class)
class StationLocalSource @Inject constructor(
    private val baseData: BaseData
) {
    private var pricesMap = mutableMapOf<String, List<Price>>()

    fun setChainParams(chain: BaseChain, chainParams: ChainParam): Completable {
        return safeCompletable {
            baseData.mChainParam = chainParams.params
        }
    }

    fun setIbcPaths(chain: BaseChain, items: List<IbcPath>): Completable {
        return completeCallable {
            baseData.mIbcPaths = items
        }
    }

    fun setIbcTokens(chain: BaseChain, items: List<IbcToken>): Completable {
        return completeCallable {
            baseData.mIbcTokens = items
        }
    }

    fun setPrices(chain: BaseChain, items: List<Price>): Completable {
        return completeCallable {
            pricesMap[chain.chainName] = items
        }
    }

    fun getPrice(chain: BaseChain, denom: String): Single<Price> {
        return safeSingle {
            pricesMap[chain.chainName]
                .orEmpty()
                .find { denom.equals(it.denom, true) }
        }
    }
}