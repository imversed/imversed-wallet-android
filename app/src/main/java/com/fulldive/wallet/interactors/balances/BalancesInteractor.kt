package com.fulldive.wallet.interactors.balances

import com.fulldive.wallet.di.modules.DefaultInteractorsModule
import com.fulldive.wallet.extensions.or
import com.fulldive.wallet.interactors.chains.binance.BinanceInteractor
import com.fulldive.wallet.interactors.chains.grpc.GrpcInteractor
import com.fulldive.wallet.interactors.chains.okex.OkexInteractor
import com.fulldive.wallet.models.BaseChain
import com.fulldive.wallet.models.WalletBalance
import com.fulldive.wallet.models.toBalance
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Completable
import io.reactivex.Single
import wannabit.io.cosmostaion.dao.Balance
import javax.inject.Inject

@ProvidedBy(DefaultInteractorsModule::class)
class BalancesInteractor @Inject constructor(
    private val balancesRepository: BalancesRepository,
    private val binanceInteractor: BinanceInteractor,
    private val okexInteractor: OkexInteractor,
    private val grpcInteractor: GrpcInteractor
) {

    fun getBalance(accountId: Long, denom: String): Single<Balance> {
        return balancesRepository.getBalance(accountId, denom).map { it.toBalance() }
    }

    fun getBalances(accountId: Long): Single<List<Balance>> {
        return balancesRepository.getBalances(accountId)
    }

    fun deleteBalances(accountId: Long): Completable {
        return balancesRepository.deleteBalances(accountId)
    }

    fun requestBalances(
        chain: BaseChain,
        address: String,
        accountId: Long = 0L
    ): Single<List<WalletBalance>> {
        return when (chain) {
            BaseChain.BNB_MAIN -> {
                binanceInteractor.requestAccount(address)
                    .map { accountInfo ->
                        accountInfo
                            .balances
                            ?.map { coin ->
                                WalletBalance.create(
                                    accountId = accountId,
                                    symbol = coin.symbol,
                                    balance = coin.free,
                                    locked = coin.locked,
                                    frozen = coin.frozen,
                                )
                            }
                            .or(emptyList())
                    }
            }
            BaseChain.OKEX_MAIN -> {
                okexInteractor
                    .requestAccountBalance(address)
                    .map { accountToken ->
                        accountToken
                            .data.currencies
                            ?.map { currency ->
                                WalletBalance.create(
                                    accountId = accountId,
                                    symbol = currency.symbol,
                                    balance = currency.available,
                                    locked = currency.locked,
                                )
                            }
                            .or(emptyList())
                    }
            }
            else -> {
                grpcInteractor.requestBalances(chain, address).map { coins ->
                    coins.map { coin ->
                        WalletBalance.create(
                            accountId = accountId,
                            symbol = coin.denom,
                            balance = coin.amount
                        )
                    }
                }
            }
        }
            .flatMap { balances ->
                if (accountId > 0L) {
                    balancesRepository.updateBalances(accountId, balances)
                } else {
                    Completable.complete()
                }
                    .toSingleDefault(balances)
            }
    }

    fun updateBalances(accountId: Long, balances: List<WalletBalance>): Completable {
        return balancesRepository.updateBalances(accountId, balances)
    }
}