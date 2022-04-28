package com.fulldive.wallet.interactors.balances

import com.fulldive.wallet.database.AppDatabase
import com.fulldive.wallet.di.modules.DefaultLocalStorageModule
import com.fulldive.wallet.extensions.safeCompletable
import com.fulldive.wallet.models.WalletBalance
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ProvidedBy(DefaultLocalStorageModule::class)
class BalancesLocalSource @Inject constructor(
    private val appDatabase: AppDatabase
) {
    private val balancesDao = appDatabase.balancesDao()

    fun getBalance(accountId: Long, denom: String): Single<WalletBalance> {
        return balancesDao.getBalance(accountId, denom)
    }

    fun getBalances(accountId: Long): Single<List<WalletBalance>> {
        return balancesDao.getBalances(accountId)
    }

    fun deleteBalances(accountId: Long): Completable {
        return safeCompletable {
            balancesDao.deleteBalances(accountId)
        }
    }

    fun updateBalances(accountId: Long, balances: List<WalletBalance>): Completable {
        return safeCompletable {
            appDatabase.runInTransaction {
                balancesDao.deleteBalances(accountId)
                balancesDao.insertAll(balances)
            }
        }
    }
}