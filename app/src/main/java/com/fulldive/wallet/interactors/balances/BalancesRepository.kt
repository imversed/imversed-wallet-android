package com.fulldive.wallet.interactors.balances

import com.fulldive.wallet.di.modules.DefaultRepositoryModule
import com.fulldive.wallet.models.WalletBalance
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

@ProvidedBy(DefaultRepositoryModule::class)
class BalancesRepository @Inject constructor(
    private val balancesLocalSource: BalancesLocalSource
) {

    fun getBalance(accountId: Long, denom: String): Single<WalletBalance> {
        return balancesLocalSource.getBalance(accountId, denom)
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
}