package com.fulldive.wallet.interactors.balances

import android.text.SpannableString
import com.fulldive.wallet.di.modules.DefaultInteractorsModule
import com.fulldive.wallet.extensions.or
import com.fulldive.wallet.interactors.chains.StationInteractor
import com.fulldive.wallet.interactors.chains.binance.BinanceInteractor
import com.fulldive.wallet.interactors.chains.grpc.GrpcInteractor
import com.fulldive.wallet.interactors.chains.okex.OkexInteractor
import com.fulldive.wallet.models.BaseChain
import com.fulldive.wallet.models.Currency
import com.fulldive.wallet.models.WalletBalance
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import wannabit.io.cosmostaion.dao.BnbToken
import wannabit.io.cosmostaion.dao.OkToken
import wannabit.io.cosmostaion.utils.PriceProvider
import java.math.BigDecimal
import javax.inject.Inject

@ProvidedBy(DefaultInteractorsModule::class)
class BalancesInteractor @Inject constructor(
    private val balancesRepository: BalancesRepository,
    private val binanceInteractor: BinanceInteractor,
    private val okexInteractor: OkexInteractor,
    private val grpcInteractor: GrpcInteractor,
    private val stationInteractor: StationInteractor
) {

    fun getBalance(accountId: Long, denom: String): Single<WalletBalance> {
        return balancesRepository.getBalance(accountId, denom)
    }

    fun observeBalances(accountId: Long): Observable<List<WalletBalance>> {
        return balancesRepository.observeBalances(accountId)
    }

    fun getBalances(accountId: Long): Single<List<WalletBalance>> {
        return balancesRepository.getBalances(accountId)
//    TODO: add filter for zero balances. and add zero balance for main denom if it doesn't exists. vesting
//    .map { balances ->
//        var items = balances
//            .mapNotNull { coin ->
//                safe {
//                    coin
//                        .takeIf {
//                            it.denom.equals(chain.mainDenom, true) || it.amount.toInt() > 0
//                        }
//                        ?.let { Coin(it.denom, it.amount) }
//                }
//            }
//        if (!items.any { it.denom == chain.mainDenom }) {
//            items = listOf(Coin(chain.mainDenom, "0")) + items
//        }
//        items
//    }
//            .flatMapCompletable(Function<List<Balance?>, CompletableSource> { balances: List<Balance?>? ->
//                Completable.fromCallable {
//                    if (getBaseDao().mGRpcAccount != null && !getBaseDao().mGRpcAccount.getTypeUrl()
//                            .contains(Auth.BaseAccount.getDescriptor().fullName)
//                    ) {
//                        WUtil.onParseVestingAccount(getBaseDao(), getBaseChain(), balances)
//                    }
//                    true
//                }
//            })

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
                    coins.mapNotNull { coin ->
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

    fun getAllExToken(denom: String): BigDecimal? {
        return balancesRepository.getAllExToken(denom)
    }

    fun getBnbToken(denom: String): BnbToken? {
        return balancesRepository.getBnbToken(denom)
    }

    fun getOkToken(denom: String): OkToken? {
        return balancesRepository.getOkToken(denom)
    }

    fun getIbcTokenDecimal(denom: String): Int {
        return balancesRepository
            .getIbcToken(denom.replace("ibc/", ""))
            ?.takeIf { it.auth }?.decimal.or(6)
    }

    fun getAllMainAsset(denom: String): BigDecimal? {
        return balancesRepository.getAllMainAsset(denom)
    }

    fun getKavaCoinDecimal(denom: String): Int {
        return when (denom.lowercase()) {
            "xrpb",
            "xrbp",
            "btc",
            "bnb",
            "btcb",
            "hbtc",
            "busd"
            -> 8
            else -> {
                if (denom.lowercase().startsWith("ibc/")) {
                    getIbcTokenDecimal(denom)
                } else 6
            }
        }
    }

    fun getTokenAmount(
        chain: BaseChain,
        currency: Currency,
        balance: WalletBalance
    ): SpannableString {
        val priceProvider = PriceProvider { priceDenom: String ->
            stationInteractor.getPrice(chain, priceDenom).blockingGet()
        }

        return balancesRepository.getTokenAmount(
            currency,
            balance,
            priceProvider,
            chain.displayDecimal
        )
    }
}