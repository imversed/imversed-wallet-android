package com.fulldive.wallet.interactors.chains.grpc

import com.fulldive.wallet.di.modules.DefaultRepositoryModule
import com.fulldive.wallet.extensions.safe
import com.fulldive.wallet.extensions.safeSingle
import com.fulldive.wallet.models.BaseChain
import com.joom.lightsaber.ProvidedBy
import cosmos.staking.v1beta1.Staking
import io.reactivex.Completable
import io.reactivex.Single
import tendermint.p2p.Types
import wannabit.io.cosmostaion.dao.Account
import wannabit.io.cosmostaion.dao.Balance
import wannabit.io.cosmostaion.dao.Cw20Assets
import wannabit.io.cosmostaion.model.type.Coin
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject

@ProvidedBy(DefaultRepositoryModule::class)
class GrpcRepository @Inject constructor(
    private val grpcLocalSource: GrpcLocalSource,
    private val grpcRemoteSource: GrpcRemoteSource
) {

    fun updateValidators(
        chain: BaseChain,
        status: Staking.BondStatus,
        limit: Int
    ): Completable {
        return grpcRemoteSource
            .requestValidators(chain, status, limit)
            .flatMapCompletable { items ->
                grpcLocalSource.setValidators(chain, status, items)
            }
    }

    fun requestNodeInfo(chain: BaseChain): Single<Types.NodeInfo> {
        return grpcRemoteSource.requestNodeInfo(chain)
    }

    fun setNodeInfo(chain: BaseChain, nodeInfo: Types.NodeInfo): Completable {
        return grpcLocalSource.setNodeInfo(chain, nodeInfo)
    }

    fun updateAccount(chain: BaseChain, address: String): Completable {
        return grpcRemoteSource
            .requestAccount(chain, address)
            .flatMapCompletable { account ->
                grpcLocalSource.setAccount(chain, account)
            }
    }

    fun updateDelegations(chain: BaseChain, address: String): Completable {
        return grpcRemoteSource
            .requestDelegations(chain, address)
            .flatMapCompletable { items ->
                grpcLocalSource.setDelegations(chain, items)
            }
    }

    fun updateUndelegations(chain: BaseChain, address: String): Completable {
        return grpcRemoteSource
            .requestUndelegations(chain, address)
            .flatMapCompletable { items ->
                grpcLocalSource.setUndelegations(chain, items)
            }
    }

    fun updateRewards(chain: BaseChain, address: String): Completable {
        return grpcRemoteSource
            .requestRewards(chain, address)
            .flatMapCompletable { items ->
                grpcLocalSource.setRewards(chain, items)
            }
    }

    fun updateBalances(chain: BaseChain, account: Account): Completable {
        return grpcRemoteSource
            .requestBalance(chain, account.address)
            .map { balances ->
                var items = balances
                    .mapNotNull { coin ->
                        safe {
                            coin
                                .takeIf {
                                    it.denom.equals(chain.mainDenom, true) || it.amount.toInt() > 0
                                }
                                ?.let { Coin(it.denom, it.amount) }
                        }
                    }
                if (!items.any { it.denom == chain.mainDenom }) {
                    items = listOf(Coin(chain.mainDenom, "0")) + items
                }
                items
            }
            .flatMap { balances ->
                grpcLocalSource
                    .setBalances(chain, balances)
                    .toSingleDefault(balances)
            }
            .map { balances ->
                balances.map { coin ->
                    Balance(
                        account.id,
                        coin.denom,
                        coin.amount,
                        Calendar.getInstance().time.time,
                        "0",
                        "0"
                    )
                }
            }
            .flatMapCompletable { snapBalances ->
                grpcLocalSource.updateBalances(account.id, snapBalances)
            }
    }

    fun getBalances(chain: BaseChain, address: String): Single<List<Balance>> {
        return grpcRemoteSource
            .requestBalance(chain, address)
            .flatMap { coins ->
                safeSingle {
                    coins.map { coin ->
                        Balance().apply {
                            symbol = coin.denom
                            balance = BigDecimal(coin.amount)
                        }
                    }
                }
            }
    }

    fun getTopValidators(): Single<List<Staking.Validator>> {
        return grpcLocalSource.getTopValidators()
    }

    fun getOtherValidators(): Single<List<Staking.Validator>> {
        return grpcLocalSource.getOtherValidators()
    }

    fun setAllValidators(chain: BaseChain, validators: List<Staking.Validator>): Completable {
        return grpcLocalSource.setAllValidators(chain, validators)
    }

    fun getDelegations(): Single<List<Staking.DelegationResponse>> {
        return grpcLocalSource.getDelegations()
    }

    fun getUndelegations(): Single<List<Staking.UnbondingDelegation>> {
        return grpcLocalSource.getUndelegations()
    }

    fun setMyValidators(chain: BaseChain, validators: List<Staking.Validator>): Completable {
        return grpcLocalSource.setMyValidators(chain, validators)
    }

    fun updateMintScanAssets(chain: BaseChain): Completable {
        return grpcRemoteSource
            .requestMintScanAssets(chain)
            .flatMapCompletable { items ->
                grpcLocalSource.setMintScanAssets(chain, items)
            }
    }

    fun updateMintScanCw20Assets(): Single<List<Cw20Assets>> {
        return grpcRemoteSource
            .requestMintScanCw20Assets()
            .flatMap { items ->
                setCw20Assets(items)
                    .toSingleDefault(items)
            }
    }

    fun setCw20Assets(items: List<Cw20Assets>): Completable {
        return grpcLocalSource.setCw20Assets(items)
    }

    fun requestCw20Balance(
        chain: BaseChain,
        account: Account,
        contractAddress: String
    ): Single<String> {
        return grpcRemoteSource.requestCw20Balance(chain, account.address, contractAddress)
    }

    fun updateGravityDex(chain: BaseChain, limit: Int = 1000): Completable {
        return grpcRemoteSource.requestGravityDex(chain, limit)
            .flatMapCompletable { items ->
                grpcLocalSource.setGravityPools(chain, items)
            }
    }

    fun updateStarNameFees(): Completable {
        return grpcRemoteSource
            .requestStarNameFees()
            .flatMapCompletable(grpcLocalSource::setStarNameFees)
    }

    fun updateStarNameConfig(): Completable {
        return grpcRemoteSource
            .requestStarNameConfig()
            .flatMapCompletable(grpcLocalSource::setStarNameConfig)
    }

    fun updateOsmosisPools(limit: Int = 1000): Completable {
        return grpcRemoteSource
            .requestOsmosisPools(limit)
            .flatMapCompletable(grpcLocalSource::setOsmosisPools)
    }

    fun updateKavaMarketPrice(): Completable {
        return grpcRemoteSource
            .requestKavaMarketPrice()
            .flatMapCompletable(grpcLocalSource::setKavaMarketPrice)
    }

    fun updateKavaIncentiveParam(): Completable {
        return grpcRemoteSource
            .requestKavaIncentiveParam()
            .flatMapCompletable(grpcLocalSource::setKavaIncentiveParam)
    }

    fun updateKavaIncentiveReward(address: String): Completable {
        return grpcRemoteSource
            .requestKavaIncentiveReward(address)
            .flatMapCompletable(grpcLocalSource::setKavaIncentiveReward)
    }
}
