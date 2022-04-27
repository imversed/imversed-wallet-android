package com.fulldive.wallet.interactors.chains

import com.fulldive.wallet.di.modules.DefaultRepositoryModule
import com.fulldive.wallet.models.BaseChain
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

@ProvidedBy(DefaultRepositoryModule::class)
class ChainsRepository @Inject constructor(
    private val chainsLocalSource: ChainsLocalSource
) {
    fun getChains(): Single<List<BaseChain>> {
        return chainsLocalSource.getChains()
    }

    fun getSortedChains(): Single<List<String>> {
        return chainsLocalSource.getSortedChains()
    }

    fun getExpandedChains(): Single<List<String>> {
        return chainsLocalSource.getExpandedChains()
    }

    fun setExpandedChains(items: List<String>): Completable {
        return chainsLocalSource.setExpandedChains(items)
    }

    fun getHiddenChains(): Single<List<String>> {
        return chainsLocalSource.getHiddenChains()
    }

    fun setHiddenChains(items: List<String>): Completable {
        return chainsLocalSource.setHiddenChains(items)
    }
}
