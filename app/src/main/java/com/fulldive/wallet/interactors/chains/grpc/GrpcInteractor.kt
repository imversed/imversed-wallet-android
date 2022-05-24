package com.fulldive.wallet.interactors.chains.grpc

import com.fulldive.wallet.di.modules.DefaultInteractorsModule
import com.fulldive.wallet.extensions.*
import com.fulldive.wallet.interactors.chains.StationInteractor
import com.fulldive.wallet.models.BaseChain
import com.fulldive.wallet.rx.AppSchedulers
import com.joom.lightsaber.ProvidedBy
import cosmos.base.v1beta1.CoinOuterClass
import cosmos.staking.v1beta1.Staking
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.reactivex.Completable
import io.reactivex.Single
import tendermint.p2p.Types
import wannabit.io.cosmostaion.dao.Account
import wannabit.io.cosmostaion.utils.WLog
import javax.inject.Inject

@ProvidedBy(DefaultInteractorsModule::class)
class GrpcInteractor @Inject constructor(
    private val grpcRepository: GrpcRepository,
    private val stationInteractor: StationInteractor
) {
    fun update(account: Account, chain: BaseChain): Completable {
        return Completable
            .mergeArray(
                updateBondedValidators(chain).subscribeOn(AppSchedulers.io()),
                updateUnbondedValidators(chain).subscribeOn(AppSchedulers.io()),
                updateUnbondingValidators(chain).subscribeOn(AppSchedulers.io())
            )
            .andThen(
                Completable.mergeArray(
                    updateAccount(chain, account).subscribeOn(AppSchedulers.io()),
                    updateDelegations(chain, account).subscribeOn(AppSchedulers.io()),
                    updateUndelegations(chain, account).subscribeOn(AppSchedulers.io()),
                    updateRewards(chain, account).subscribeOn(AppSchedulers.io()),
                )
            )
            .andThen(updateAdditional(chain, account))
            .andThen(updateValidators(chain))
            .andThen(updateNodeInfo(chain))
            .flatMapCompletable { nodeInfo ->
                Completable
                    .mergeArray(
                        stationInteractor
                            .updateStationParams(
                                chain,
                                nodeInfo.network
                            )
                            .subscribeOn(AppSchedulers.io())
                            .doOnError { error ->
                                WLog.e(error.message)
                                error.printStackTrace()
                            }
                            .onErrorComplete(),
                        stationInteractor
                            .updateIbcPaths(
                                chain,
                                nodeInfo.network
                            )
                            .doOnError { error ->
                                WLog.e(error.message)
                                error.printStackTrace()
                            }
                            .onErrorComplete()
                            .subscribeOn(AppSchedulers.io()),
                        stationInteractor
                            .updateIbcTokens(
                                chain,
                                nodeInfo.network
                            )
                            .doOnError { error ->
                                WLog.e(error.message)
                                error.printStackTrace()
                            }
                            .onErrorComplete()
                            .subscribeOn(AppSchedulers.io()),
                        updateMintScanAssets(chain).subscribeOn(AppSchedulers.io())
                    )
            }
    }

    fun updateBondedValidators(chain: BaseChain): Completable {
        return grpcRepository.updateValidators(
            chain,
            Staking.BondStatus.BOND_STATUS_BONDED,
            DEFAULT_LIMIT
        )
    }

    fun updateUnbondedValidators(chain: BaseChain): Completable {
        return grpcRepository.updateValidators(
            chain,
            Staking.BondStatus.BOND_STATUS_UNBONDED,
            DEFAULT_LIMIT
        )
    }

    fun updateUnbondingValidators(chain: BaseChain): Completable {
        return grpcRepository.updateValidators(
            chain,
            Staking.BondStatus.BOND_STATUS_UNBONDING,
            DEFAULT_LIMIT
        )
    }

    fun updateAccount(chain: BaseChain, account: Account): Completable {
        return grpcRepository.updateAccount(chain, account.address)
            .onErrorComplete { error ->
                ((error as? StatusRuntimeException)?.status?.code == Status.Code.NOT_FOUND)
                    .apply {
                        ifTrue {
                            WLog.e(error.message)
                        }
                    }
            }
    }

    fun updateDelegations(chain: BaseChain, account: Account): Completable {
        return grpcRepository.updateDelegations(chain, account.address)
    }

    fun updateUndelegations(chain: BaseChain, account: Account): Completable {
        return grpcRepository.updateUndelegations(chain, account.address)
    }

    fun updateRewards(chain: BaseChain, account: Account): Completable {
        return grpcRepository.updateRewards(chain, account.address)
    }

    fun updateNodeInfo(chain: BaseChain): Single<Types.NodeInfo> {
        return grpcRepository
            .requestNodeInfo(chain)
            .flatMap { nodeInfo ->
                grpcRepository
                    .setNodeInfo(chain, nodeInfo)
                    .toSingleDefault(nodeInfo)
            }
    }

    fun requestBalances(chain: BaseChain, address: String): Single<List<CoinOuterClass.Coin>> {
        return grpcRepository.requestBalance(chain, address)
    }

    private fun updateValidators(chain: BaseChain): Completable {
        return Single
            .zip(
                grpcRepository.getTopValidators(),
                grpcRepository.getOtherValidators(),
                ::concat
            )
            .flatMap { validators ->
                grpcRepository
                    .setAllValidators(chain, validators)
                    .toSingleDefault(validators)
            }
            .flatMap { validators ->
                Single
                    .zip(
                        grpcRepository.getDelegations(),
                        grpcRepository.getUndelegations(),
                        ::combine
                    )
                    .flatMap { (delegations, undelegations) ->
                        singleCallable {
                            validators.mapNotNull { validator ->
                                validator.takeIf {
                                    delegations.any { it.delegation.validatorAddress == validator.operatorAddress }
                                            || undelegations.any { it.validatorAddress == validator.operatorAddress }
                                }
                            }
                        }
                    }
            }
            .flatMapCompletable { myValidators ->
                grpcRepository.setMyValidators(chain, myValidators)
            }
    }

    private fun updateMintScanAssets(chain: BaseChain): Completable {
        return grpcRepository.updateMintScanAssets(chain)
            .doOnError { error ->
                WLog.e(error.message)
                error.printStackTrace()
            }
            .onErrorComplete()
    }

    private fun updateMintScanCw20Assets(chain: BaseChain, account: Account): Completable {
        return grpcRepository
            .updateMintScanCw20Assets()
            .flatMapCompletable { cw20Assets ->
                cw20Assets.map { assets ->
                    assets
                        .takeIf {
                            chain.mintScanChainName.isNotEmpty() && assets.chain.equals(
                                chain.mintScanChainName,
                                ignoreCase = true
                            )
                        }
                        .letOr(
                            {
                                grpcRepository
                                    .requestCw20Balance(
                                        chain,
                                        account,
                                        assets.contract_address
                                    )
                                    .map { balance ->
                                        assets.amount = balance
                                        assets
                                    }
                            },
                            {
                                Single.just(assets)
                            }
                        )
                }
                    .let { Single.concat(it) }
                    .toList()
                    .flatMapCompletable(grpcRepository::setCw20Assets)
            }
    }

    private fun updateAdditional(chain: BaseChain, account: Account): Completable {
        return when (chain) {
            BaseChain.COSMOS_MAIN -> {
                grpcRepository.updateGravityDex(chain)
            }
            BaseChain.IOV_MAIN -> Completable.mergeArray(
                grpcRepository.updateStarNameFees(),
                grpcRepository.updateStarNameConfig()
            )
            BaseChain.OSMOSIS_MAIN -> {
                grpcRepository.updateOsmosisPools()
            }
            BaseChain.JUNO_MAIN -> {
                updateMintScanCw20Assets(chain, account)
            }
            BaseChain.KAVA_MAIN -> Completable.mergeArray(
                grpcRepository.updateKavaMarketPrice(),
                grpcRepository.updateKavaIncentiveParam(),
                grpcRepository.updateKavaIncentiveReward(account.address)
            )
            else -> Completable.complete()
        }
    }

    companion object {
        private const val DEFAULT_LIMIT = 300
    }
}