package com.fulldive.wallet.interactors.chains.okex

import com.fulldive.wallet.di.modules.DefaultInteractorsModule
import com.fulldive.wallet.interactors.accounts.AccountsInteractor
import com.fulldive.wallet.interactors.chains.StationInteractor
import com.fulldive.wallet.models.BaseChain
import com.fulldive.wallet.rx.AppSchedulers
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Completable
import io.reactivex.Single
import wannabit.io.cosmostaion.base.BaseConstant
import wannabit.io.cosmostaion.dao.Account
import wannabit.io.cosmostaion.dao.Balance
import wannabit.io.cosmostaion.model.NodeInfo
import javax.inject.Inject

@ProvidedBy(DefaultInteractorsModule::class)
class OkexInteractor @Inject constructor(
    private val okexRepository: OkexRepository,
    private val stationInteractor: StationInteractor,
    private val accountsInteractor: AccountsInteractor
) {
    fun update(account: Account, chain: BaseChain): Completable {
        return Completable
            .mergeArray(
                updateValidatorsList().subscribeOn(AppSchedulers.io()),
                updateTickersList().subscribeOn(AppSchedulers.io())
                    .onErrorComplete(),  //XXX: remove
                updateTokensList().subscribeOn(AppSchedulers.io())
            )
            .andThen(
                Completable.mergeArray(
                    updateAccountBalance(account).subscribeOn(AppSchedulers.io()),
                    updateUnbonding(account).subscribeOn(AppSchedulers.io()),
                    updateStakingInfo(account).subscribeOn(AppSchedulers.io())
                )
            )
            .andThen(updateNodeInfo())
            .flatMapCompletable { nodeInfo ->
                stationInteractor
                    .updateStationParams(
                        chain,
                        nodeInfo.network
                    )
            }

    }

    fun updateValidatorsList(): Completable {
        return okexRepository.updateValidatorsList()
    }

    fun updateTickersList(): Completable {
        return okexRepository.updateTickersList()
    }

    fun updateTokensList(): Completable {
        return okexRepository.updateTokensList()
    }

    fun updateAccountBalance(account: Account): Completable {
        return okexRepository.updateAccountBalance(account)
    }

    fun updateUnbonding(account: Account): Completable {
        return okexRepository.updateUnbonding(account)
    }

    fun updateStakingInfo(account: Account): Completable {
        return okexRepository.updateStakingInfo(account)
    }

    fun updateAccount(account: Account): Completable {
        return okexRepository
            .requestAccount(account.address)
            .flatMapCompletable { accountInfo ->
                if (accountInfo.type == BaseConstant.COSMOS_AUTH_TYPE_OKEX_ACCOUNT) {
                    accountsInteractor.updateAccount(
                        account.id,
                        address = accountInfo.value.eth_address,
                        sequenceNumber = accountInfo.value.sequence.toInt(),
                        accountNumber = accountInfo.value.account_number.toInt()
                    )
                } else {
                    Completable.complete()
                }
            }
    }

    fun getBalances(address: String): Single<List<Balance>> {
        return okexRepository
            .requestAccountBalance(address)
    }

    fun updateNodeInfo(): Single<NodeInfo> {
        return okexRepository
            .requestNodeInfo()
            .flatMap { nodeInfo ->
                okexRepository
                    .setNodeInfo(nodeInfo)
                    .toSingleDefault(nodeInfo.node_info)
            }
    }
}