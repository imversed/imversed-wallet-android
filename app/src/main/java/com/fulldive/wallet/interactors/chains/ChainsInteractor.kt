package com.fulldive.wallet.interactors.chains

import com.fulldive.wallet.di.modules.DefaultInteractorsModule
import com.fulldive.wallet.models.BaseChain
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

@ProvidedBy(DefaultInteractorsModule::class)
class ChainsInteractor @Inject constructor(
    private val chainsRepository: ChainsRepository
) {
    fun getExpandedChains(): Single<List<String>> {
        return chainsRepository.getExpandedChains()
    }

    fun getSortedChains(): Single<List<BaseChain>> {
        return Single.zip(
            chainsRepository.getChains(),
            chainsRepository.getHiddenChains(),
            chainsRepository.getSortedChains()
        )
        { chains, hiddenChains, sortedChains ->
            chains
                .filter { chain ->
                    chain.isSupported && !hiddenChains.contains(chain.chainName)
                }
                .sortedWith { left, right ->
                    var result = right.sortValue.compareTo(left.sortValue)
                    if (result == 0) {
                        result = sortedChains.indexOf(right.chainName)
                            .compareTo(
                                sortedChains.indexOf(left.chainName)
                            )
                    }
                    result
                }
        }
    }

    fun setExpandedChains(items: List<String>): Completable {
        return chainsRepository.setExpandedChains(items)
    }
}