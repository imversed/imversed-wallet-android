package com.fulldive.wallet.interactors.accounts

import com.fulldive.wallet.di.modules.DefaultLocalStorageModule
import com.fulldive.wallet.extensions.or
import com.fulldive.wallet.extensions.safeCompletable
import com.fulldive.wallet.extensions.safeSingle
import com.fulldive.wallet.models.BaseChain
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Completable
import io.reactivex.Single
import wannabit.io.cosmostaion.base.BaseData
import wannabit.io.cosmostaion.dao.Account
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ProvidedBy(DefaultLocalStorageModule::class)
class AccountsLocalStorage @Inject constructor(
    private val baseData: BaseData
) {
    private var currentAccount: Account? = null

    fun getAccount(accountId: Long): Single<Account> {
        return safeSingle {
            baseData.getAccount("$accountId")
        }
    }

    fun getAccount(chain: BaseChain, address: String): Single<Account> {
        return safeSingle {
            baseData.getAccount(address, chain.chainName)
        }
    }

    fun getAccountsByAddress(address: String): Single<List<Account>> {
        return safeSingle {
            baseData.getAccountsByAddress(address)
        }
    }

    fun getAccountsByChain(chain: BaseChain): List<Account> {
        return baseData.getAccountsByChain(chain)
    }

    fun getAccounts(): Single<List<Account>> {
        return safeSingle {
            baseData.accounts
        }
    }

    fun getSelectedAccount(): Single<Account> {
        return safeSingle {
            getCurrentAccount()
        }
    }

    fun getSortedChains(): Single<List<BaseChain>> {
        return safeSingle {
            baseData.dpSortedChains()
        }
    }

    fun selectAccount(id: Long): Completable {
        return safeCompletable {
            currentAccount = baseData.getAccount("$id")
            baseData.setLastUser(id)
        }
    }

    fun deleteAccount(accountId: Long): Completable {
        return safeCompletable {
            baseData.onDeleteBalance("$accountId")
            baseData.onDeleteAccount("$accountId")
        }
    }

    fun addAccount(account: Account): Single<Long> {
        return safeSingle {
            baseData.insertAccount(account)
        }
    }

    fun updateAccount(account: Account): Completable {
        return safeCompletable {
            baseData.overrideAccount(account)
        }
    }

    fun getHiddenChains(): Single<List<BaseChain>> {
        return safeSingle {
            baseData.userHideChains()
        }
    }

    fun setHiddenChains(items: List<BaseChain>): Completable {
        return safeCompletable {
            baseData.setUserHidenChains(items)
        }
    }

    fun getCurrentAccount(): Account {
        return currentAccount
            .or {
                baseData
                    .getAccount("${baseData.lastUserId}")
                    .also {
                        currentAccount = it
                    }
            }
    }
}