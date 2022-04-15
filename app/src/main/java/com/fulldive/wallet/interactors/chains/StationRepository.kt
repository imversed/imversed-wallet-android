package com.fulldive.wallet.interactors.chains

import com.fulldive.wallet.di.modules.DefaultRepositoryModule
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Completable
import wannabit.io.cosmostaion.base.BaseChain
import javax.inject.Inject

@ProvidedBy(DefaultRepositoryModule::class)
class StationRepository @Inject constructor(
    private val stationLocalSource: StationLocalSource,
    private val stationRemoteSource: StationRemoteSource
) {
    fun updateChainParams(chain: BaseChain, network: String): Completable {
        return stationRemoteSource
            .requestChainParams(chain, network)
            .flatMapCompletable { chainParam ->
                stationLocalSource.setChainParams(chain, chainParam)
            }
    }

    fun updateIbcPaths(chain: BaseChain, network: String): Completable {
        return stationRemoteSource.requestIbcPaths(chain, network)
            .flatMapCompletable { items ->
                stationLocalSource.setIbcPaths(chain, items)
            }
    }

    fun updateIbcTokens(chain: BaseChain, network: String): Completable {
        return stationRemoteSource.requestIbcTokens(chain, network)
            .flatMapCompletable { items ->
                stationLocalSource.setIbcTokens(chain, items)
            }
    }

    fun updatePrices(chain: BaseChain): Completable {
        return stationRemoteSource.updatePrices(chain)
            .flatMapCompletable { items ->
                stationLocalSource.setPrices(chain, items)
            }
    }
}
