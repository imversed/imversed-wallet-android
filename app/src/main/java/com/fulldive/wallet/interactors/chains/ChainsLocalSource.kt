package com.fulldive.wallet.interactors.chains

import com.fulldive.wallet.di.modules.DefaultLocalStorageModule
import com.fulldive.wallet.extensions.safeCompletable
import com.fulldive.wallet.extensions.safeSingle
import com.fulldive.wallet.extensions.toSingle
import com.fulldive.wallet.models.BaseChain
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Completable
import io.reactivex.Single
import wannabit.io.cosmostaion.base.BaseData
import javax.inject.Inject

@ProvidedBy(DefaultLocalStorageModule::class)
class ChainsLocalSource @Inject constructor(
    private val baseData: BaseData
) {
    fun getChains(): Single<List<BaseChain>> {
        return BaseChain.chains.toSingle()
    }

    fun getHiddenChains(): Single<List<String>> {
        return safeSingle {
            baseData.userHiddenChains
        }
    }

    fun getSortedChains(): Single<List<String>> {
        return safeSingle {
            baseData.userSortedChains
        }
    }

    fun getExpandedChains(): Single<List<String>> {
        return safeSingle {
            baseData.expandedChains
        }
    }

    fun setExpandedChains(items: List<String>): Completable {
        return safeCompletable {
            baseData.expandedChains = items
        }
    }
}