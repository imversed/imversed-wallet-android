package com.fulldive.wallet.interactors.balances

import android.text.SpannableString
import com.fulldive.wallet.database.AppDatabase
import com.fulldive.wallet.di.modules.DefaultLocalStorageModule
import com.fulldive.wallet.extensions.safeCompletable
import com.fulldive.wallet.models.Currency
import com.fulldive.wallet.models.WalletBalance
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import wannabit.io.cosmostaion.base.BaseData
import wannabit.io.cosmostaion.dao.BnbToken
import wannabit.io.cosmostaion.dao.IbcToken
import wannabit.io.cosmostaion.dao.OkToken
import wannabit.io.cosmostaion.utils.PriceProvider
import wannabit.io.cosmostaion.utils.WDp
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ProvidedBy(DefaultLocalStorageModule::class)
class BalancesLocalSource @Inject constructor(
    private val appDatabase: AppDatabase,
    private val baseData: BaseData
) {
    private val balancesDao = appDatabase.balancesDao()

    fun getBalance(accountId: Long, denom: String): Single<WalletBalance> {
        return balancesDao.getBalance(accountId, denom)
    }

    fun observeBalances(accountId: Long): Observable<List<WalletBalance>> {
        return balancesDao.observeBalances(accountId)
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

    fun getAllExToken(denom: String): BigDecimal? {
        return baseData.getAllExToken(denom)
    }

    fun getIbcToken(denom: String): IbcToken? {
        return baseData.getIbcToken(denom)
    }

    fun getBnbToken(denom: String): BnbToken? {
        return baseData.getBnbToken(denom)
    }

    fun getOkToken(denom: String): OkToken? {
        return baseData.okToken(denom)
    }

    fun getAllMainAsset(denom: String): BigDecimal? {
        return baseData.getAllMainAsset(denom)
    }

    fun getTokenAmount(
        currency: Currency,
        balance: WalletBalance,
        priceProvider: PriceProvider,
        displayDivider: Int
    ): SpannableString {
        val amount = balance.getBalanceAmount()
            .add(getAllMainAsset(balance.denom)) //TODO: add vesting
        return WDp.dpUserCurrencyValue(
            baseData,
            currency,
            balance.denom,
            amount,
            displayDivider,
            priceProvider
        )
    }
}