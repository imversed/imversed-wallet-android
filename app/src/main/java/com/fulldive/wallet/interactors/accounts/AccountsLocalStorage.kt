package com.fulldive.wallet.interactors.accounts

import android.content.Context
import com.fulldive.wallet.database.AppDatabase
import com.fulldive.wallet.di.modules.DefaultLocalStorageModule
import com.fulldive.wallet.extensions.or
import com.fulldive.wallet.extensions.safe
import com.fulldive.wallet.extensions.safeCompletable
import com.fulldive.wallet.extensions.safeSingle
import com.fulldive.wallet.models.BaseChain
import com.fulldive.wallet.models.WalletAccount
import com.fulldive.wallet.models.toAccount
import com.fulldive.wallet.models.toWalletAccount
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import wannabit.io.cosmostaion.appextensions.getPrivateSharedPreferences
import wannabit.io.cosmostaion.base.BaseData
import wannabit.io.cosmostaion.dao.Account
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ProvidedBy(DefaultLocalStorageModule::class)
class AccountsLocalStorage @Inject constructor(
    private val baseData: BaseData,
    context: Context,
    appDatabase: AppDatabase,
) {
    private val accountsDao = appDatabase.accountsDao()
    private val gson = Gson()

    private var userId: Long = -1L
    private var lastTotalMap: MutableMap<String, String>? = null
    private var sharedPreferences =
        context.getPrivateSharedPreferences(KEY_ACCOUNTS_PREFERENCES)

    private var currentAccount: Account? = null
        set(value) {
            field = value
            if (value != null) {
                accountSubject.onNext(value)
            }
        }
    private var accountSubject = BehaviorSubject.create<Account>()

    fun getAccount(accountId: Long): Single<Account> {
        return accountsDao.getAccount(accountId).map(WalletAccount::toAccount)
    }

    fun getWalletAccount(accountId: Long): Single<WalletAccount> {
        return accountsDao.getAccount(accountId)
    }

    fun getAccount(chain: String, address: String): Single<Account> {
        return accountsDao.getAccount(address, chain).map(WalletAccount::toAccount)
    }

    fun getAccountsByAddress(address: String): Single<List<Account>> {
        return accountsDao.getAccounts(address).map { it.map(WalletAccount::toAccount) }
    }

    fun getChainAccounts(chain: String): Single<List<Account>> {
        return accountsDao.getChainAccounts(chain).map { it.map(WalletAccount::toAccount) }
    }

    fun getAccounts(): Single<List<Account>> {
        return accountsDao.getAccounts().map { it.map(WalletAccount::toAccount) }
    }

    fun getCurrentAccount(): Single<Account> {
        return safeSingle {
            currentAccount
        }
            .onErrorResumeNext {
                getAccount(getLastUserId())
                    .onErrorResumeNext {
                        getAccounts().map { items -> items.getOrNull(0) }
                    }
                    .doOnSuccess { account ->
                        currentAccount = account
                    }
            }
    }

    fun observeCurrentAccount(): Observable<Account> {
        return accountSubject
    }

    fun getSortedChains(): Single<List<BaseChain>> {
        return safeSingle {
            baseData.dpSortedChains()
        }
    }

    fun selectAccount(id: Long): Completable {
        return getAccount(id)
            .flatMapCompletable { account ->
                safeCompletable {
                    currentAccount = account
                    setLastUser(id)
                }
            }
    }

    fun deleteAccount(accountId: Long): Completable {
        return safeCompletable {
            accountsDao.deleteAccount(accountId)
        }
    }

    fun addAccount(account: WalletAccount): Single<Long> {
        return getAccount(account.chain, account.address)
            .flatMap {
                Single.error<Long>(DuplicateAccountException())
            }
            .onErrorResumeNext {
                if (it is DuplicateAccountException) {
                    Single.error(it)
                } else {
                    safeSingle {
                        accountsDao.insert(account)
                    }
                }
            }
    }

    fun updateAccount(account: Account): Completable {
        return safeCompletable {
            accountsDao.update(account.toWalletAccount())
        }
    }

    fun updateAccount(account: WalletAccount): Completable {
        return safeCompletable {
            accountsDao.update(account)
        }
    }

    // Temporarily solution.
    private fun getLastTotalMap(): MutableMap<String, String> {
        return lastTotalMap.or {
            sharedPreferences
                .getString(KEY_LAST_TOTAL, null)
                ?.let { json ->
                    safe {
                        val type = object : TypeToken<Map<String, String>>() {}.type
                        val items: Map<String, String> = gson.fromJson(json, type)
                        items.toMutableMap()
                    }
                }
                .or(mutableMapOf())
                .also { lastTotalMap = it }
        }
    }

    fun getLastTotal(accountId: Long): String {
        return getLastTotalMap()["$accountId"].or("")
    }

    fun updateLastTotal(accountId: Long, amount: String) {
        val map = getLastTotalMap();
        map["$accountId"] = amount
        sharedPreferences.edit().putString(KEY_LAST_TOTAL, gson.toJson(map)).apply()
    }

    private fun setLastUser(userId: Long) {
        this.userId = userId
        sharedPreferences.edit().putLong(KEY_USER_ID, userId).apply()
    }

    private fun getLastUserId(): Long {
        if (userId == -1L) {
            userId = sharedPreferences.getLong(KEY_USER_ID, -1)
        }
        return userId
    }

    companion object {
        private const val KEY_ACCOUNTS_PREFERENCES = "accounts_preferences"
        private const val KEY_LAST_TOTAL = "KEY_LAST_TOTAL"
        private const val KEY_USER_ID = "KEY_USER_ID"
    }
}