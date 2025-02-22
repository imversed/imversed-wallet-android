package com.fulldive.wallet.interactors.chains

import com.fulldive.wallet.di.modules.DefaultInteractorsModule
import com.fulldive.wallet.models.BaseChain
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Completable
import io.reactivex.Single
import wannabit.io.cosmostaion.dao.Price
import javax.inject.Inject

@ProvidedBy(DefaultInteractorsModule::class)
class StationInteractor @Inject constructor(
    private val stationRepository: StationRepository
) {

    fun updateStationParams(baseChain: BaseChain, network: String): Completable {
        return stationRepository.updateChainParams(baseChain, network)
    }

    fun updateIbcPaths(chain: BaseChain, network: String): Completable {
        return stationRepository.updateIbcPaths(chain, network)
    }

    fun updateIbcTokens(chain: BaseChain, network: String): Completable {
        return stationRepository.updateIbcTokens(chain, network)
    }

    fun updatePrices(chain: BaseChain): Completable {
        return stationRepository.updatePrices(chain)
    }

    fun getPrice(baseChain: BaseChain, denom: String): Single<Price> {
        return stationRepository.getPrice(baseChain, denom)
    }

}