package com.fulldive.wallet.interactors.chains.binance

import com.fulldive.wallet.di.modules.DefaultInteractorsModule
import com.fulldive.wallet.extensions.concat
import com.fulldive.wallet.interactors.accounts.AccountsInteractor
import com.fulldive.wallet.interactors.chains.StationInteractor
import com.fulldive.wallet.models.BaseChain
import com.fulldive.wallet.rx.AppSchedulers
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Completable
import io.reactivex.Single
import wannabit.io.cosmostaion.dao.Account
import wannabit.io.cosmostaion.model.NodeInfo
import wannabit.io.cosmostaion.network.res.ResBnbAccountInfo
import wannabit.io.cosmostaion.utils.WLog
import javax.inject.Inject

@ProvidedBy(DefaultInteractorsModule::class)
class BinanceInteractor @Inject constructor(
    private val binanceRepository: BinanceRepository,
    private val stationInteractor: StationInteractor,
    private val accountsInteractor: AccountsInteractor
) {
    fun update(account: Account): Completable {
        return Completable
            .mergeArray(
                updateTokensList().subscribeOn(AppSchedulers.io()),
                updateTickersList().subscribeOn(AppSchedulers.io()),
                updateFees().subscribeOn(AppSchedulers.io())
            )
            .andThen(updateAccount(account))
            .andThen(updateNodeInfo())
            .flatMapCompletable { nodeInfo ->
                stationInteractor
                    .updateStationParams(
                        BaseChain.BNB_MAIN,
                        nodeInfo.network
                    )
                    .doOnError { error ->
                        WLog.e(error.message)
                        error.printStackTrace()
                    }
                    .onErrorComplete()
            }
    }

    fun updateTokensList(): Completable {
        return Single
            .zip(
                binanceRepository.requestTokens(DEFAULT_LIMIT).subscribeOn(AppSchedulers.io()),
                binanceRepository.requestMiniTokens(DEFAULT_LIMIT).subscribeOn(AppSchedulers.io()),
                ::concat
            )
            .flatMapCompletable(binanceRepository::setTokens)
    }

    fun updateTickersList(): Completable {
        return Single
            .zip(
                binanceRepository.requestTickers().subscribeOn(AppSchedulers.io()),
                binanceRepository.requestMiniTickers().subscribeOn(AppSchedulers.io()),
                ::concat
            )
            .flatMapCompletable(binanceRepository::setTickers)
    }

    fun updateFees(): Completable {
        return binanceRepository
            .requestFees()
            .flatMapCompletable(binanceRepository::setFees)
    }

    fun requestAccount(address: String): Single<ResBnbAccountInfo> {
        return binanceRepository.requestAccount(address)
    }

    fun updateAccount(account: Account): Completable {
        return binanceRepository
            .requestAccount(account.address)
            .flatMapCompletable { accountInfo ->
                accountsInteractor
                    .updateAccount(
                        account.id,
                        accountInfo.address,
                        accountInfo.sequence.toInt(),
                        accountInfo.account_number.toInt()
                    )
            }
            .doOnError { error ->
                WLog.e(error.message)
                error.printStackTrace()
            }
            .onErrorComplete()
    }

    fun updateNodeInfo(): Single<NodeInfo> {
        return binanceRepository
            .requestNodeInfo()
            .flatMap { nodeInfo ->
                binanceRepository
                    .setNodeInfo(nodeInfo)
                    .toSingleDefault(nodeInfo.node_info)
            }
    }


    companion object {
        private const val DEFAULT_LIMIT = 3000
    }
}