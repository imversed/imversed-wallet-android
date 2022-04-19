package com.fulldive.wallet.interactors.chains

import android.content.Context
import com.fulldive.wallet.di.modules.DefaultLocalStorageModule
import com.fulldive.wallet.extensions.safeSingle
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Single
import com.fulldive.wallet.models.BaseChain
import wannabit.io.cosmostaion.dao.ChainParam
import wannabit.io.cosmostaion.dao.IbcPath
import wannabit.io.cosmostaion.dao.IbcToken
import wannabit.io.cosmostaion.dao.Price
import wannabit.io.cosmostaion.network.ApiClient
import javax.inject.Inject

@ProvidedBy(DefaultLocalStorageModule::class)
class StationRemoteSource @Inject constructor(
    private val context: Context
) {

    fun requestChainParams(chain: BaseChain, network: String): Single<ChainParam> {
        return safeSingle {
            ApiClient.getStationApi(context, chain)
                .getParam(network).execute()
                .body()
        }
    }

    fun requestIbcPaths(chain: BaseChain, network: String): Single<List<IbcPath>> {
        return safeSingle {
            ApiClient.getStationApi(context, chain)
                .getIbcPaths(network).execute()
                .body()!!.sendable
        }
    }

    fun requestIbcTokens(chain: BaseChain, network: String): Single<List<IbcToken>> {
        return safeSingle {
            ApiClient.getStationApi(context, chain)
                .getIbcTokens(network).execute()
                .body()!!.ibc_tokens
        }
    }

    fun updatePrices(chain: BaseChain): Single<List<Price>> {
        return safeSingle {
            ApiClient.getStationApi(context, chain)
                .price.execute().body()
        }
    }
}