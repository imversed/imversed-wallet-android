package com.fulldive.wallet.interactors.balances

import android.text.SpannableString
import com.fulldive.wallet.di.modules.DefaultRepositoryModule
import com.fulldive.wallet.models.Currency
import com.fulldive.wallet.models.WalletBalance
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import wannabit.io.cosmostaion.dao.BnbToken
import wannabit.io.cosmostaion.dao.IbcToken
import wannabit.io.cosmostaion.dao.OkToken
import wannabit.io.cosmostaion.utils.PriceProvider
import java.math.BigDecimal
import javax.inject.Inject

@ProvidedBy(DefaultRepositoryModule::class)
class BalancesRepository @Inject constructor(
    private val balancesLocalSource: BalancesLocalSource
) {

    fun getBalance(accountId: Long, denom: String): Single<WalletBalance> {
        return balancesLocalSource.getBalance(accountId, denom)
    }

    fun observeBalances(accountId: Long): Observable<List<WalletBalance>> {
        return balancesLocalSource.observeBalances(accountId)
    }

    fun getBalances(accountId: Long): Single<List<WalletBalance>> {
        return balancesLocalSource.getBalances(accountId)
    }

    fun deleteBalances(accountId: Long): Completable {
        return balancesLocalSource.deleteBalances(accountId)
    }

    fun updateBalances(accountId: Long, balances: List<WalletBalance>): Completable {
        return balancesLocalSource.updateBalances(accountId, balances)
    }

    fun getAllExToken(denom: String): BigDecimal? {
        return balancesLocalSource.getAllExToken(denom)
    }

    fun getIbcToken(denom: String): IbcToken? {
        return balancesLocalSource.getIbcToken(denom)
    }

    fun getBnbToken(denom: String): BnbToken? {
        return balancesLocalSource.getBnbToken(denom)
    }

    fun getOkToken(denom: String): OkToken? {
        return balancesLocalSource.getOkToken(denom)
    }

    fun getAllMainAsset(denom: String): BigDecimal? {
        return balancesLocalSource.getAllMainAsset(denom)
    }

    fun getTokenAmount(
        currency: Currency,
        balance: WalletBalance,
        priceProvider: PriceProvider,
        displayDivider: Int
    ): SpannableString {
        return balancesLocalSource.getTokenAmount(currency, balance, priceProvider, displayDivider)
    }
}