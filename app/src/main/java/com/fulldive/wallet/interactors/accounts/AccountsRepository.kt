package com.fulldive.wallet.interactors.accounts

import com.fulldive.wallet.di.modules.DefaultRepositoryModule
import com.fulldive.wallet.models.BaseChain
import com.fulldive.wallet.models.WalletAccount
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Completable
import io.reactivex.Single
import wannabit.io.cosmostaion.dao.Account
import javax.inject.Inject

@ProvidedBy(DefaultRepositoryModule::class)
class AccountsRepository @Inject constructor(
    private val accountsLocalStorage: AccountsLocalStorage
) {

    fun getAccounts(): Single<List<Account>> {
        return accountsLocalStorage.getAccounts()
    }

    fun getAccount(accountId: Long): Single<Account> {
        return accountsLocalStorage.getAccount(accountId)
    }

    fun getWalletAccount(accountId: Long): Single<WalletAccount> {
        return accountsLocalStorage.getWalletAccount(accountId)
    }

    fun getAccount(chain: String, address: String): Single<Account> {
        return accountsLocalStorage.getAccount(chain, address)
    }

    fun getCurrentAccount(): Single<Account> {
        return accountsLocalStorage.getCurrentAccount()
    }

    fun getAccountsByAddress(address: String): Single<List<Account>> {
        return accountsLocalStorage.getAccountsByAddress(address)
    }

    fun getChainAccounts(chain: String): Single<List<Account>> {
        return accountsLocalStorage.getChainAccounts(chain)
    }

    fun getSortedChains(): Single<List<BaseChain>> {
        return accountsLocalStorage.getSortedChains()
    }

    fun selectAccount(id: Long): Completable {
        return accountsLocalStorage.selectAccount(id)
    }

    fun addAccount(account: WalletAccount): Single<Long> {
        return accountsLocalStorage.addAccount(account)
    }

    fun updateAccount(account: Account): Completable {
        return accountsLocalStorage.updateAccount(account)
    }

    fun updateAccount(account: WalletAccount): Completable {
        return accountsLocalStorage.updateAccount(account)
    }

    fun deleteAccount(accountId: Long): Completable {
        return accountsLocalStorage.deleteAccount(accountId)
    }

    fun getLastTotal(accountId: Long): String {
        return accountsLocalStorage.getLastTotal(accountId)
    }

    fun updateLastTotal(accountId: Long, amount: String) {
        accountsLocalStorage.updateLastTotal(accountId, amount)
    }
}