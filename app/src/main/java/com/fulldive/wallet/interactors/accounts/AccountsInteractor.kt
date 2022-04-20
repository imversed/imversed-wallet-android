package com.fulldive.wallet.interactors.accounts

import com.fulldive.wallet.di.modules.DefaultInteractorsModule
import com.fulldive.wallet.extensions.combine
import com.fulldive.wallet.extensions.safeSingle
import com.fulldive.wallet.extensions.singleCallable
import com.fulldive.wallet.interactors.secret.SecretInteractor
import com.fulldive.wallet.models.BaseChain
import com.fulldive.wallet.models.local.AccountSecrets
import com.fulldive.wallet.rx.AppSchedulers
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Completable
import io.reactivex.Single
import wannabit.io.cosmostaion.dao.Account
import java.util.*
import javax.inject.Inject

@ProvidedBy(DefaultInteractorsModule::class)
class AccountsInteractor @Inject constructor(
    private val accountsRepository: AccountsRepository,
    private val secretInteractor: SecretInteractor
) {

    fun getAccount(accountId: Long): Single<Account> {
        return accountsRepository.getAccount(accountId)
    }

    fun getAccount(chain: BaseChain, address: String): Single<Account> {
        return accountsRepository.getAccount(chain, address)
    }

    fun getSelectedAccount(): Single<Account> {
        return accountsRepository.getSelectedAccount()
    }

    fun getCurrentAccount(): Account {
        return accountsRepository.getCurrentAccount()
    }

    fun getAccounts(): Single<List<Account>> {
        return accountsRepository.getAccounts()
    }

    // TODO: migrate to rx
    fun getAccountsByChain(chain: BaseChain): List<Account> {
        return accountsRepository.getAccountsByChain(chain)
    }

    fun selectAccountForAddress(address: String): Completable {
        return accountsRepository
            .getAccountsByAddress(address)
            .flatMapCompletable { accounts ->
                if (accounts.isEmpty()) {
                    Completable.error(AccountsListEmptyException())
                } else {
                    accountsRepository.selectAccount(accounts[0].id)
                }
            }
    }


    fun selectAccount(accountId: Long): Completable {
        return accountsRepository.selectAccount(accountId)
    }

    fun selectAccount(): Single<Account> {
        return getAccounts()
            .flatMap { accounts ->
                if (accounts.isEmpty()) {
                    accountsRepository
                        .selectAccount(-1)
                        .andThen(Single.error(AccountsListEmptyException()))
                } else {
                    accountsRepository
                        .getSortedChains()
                        .flatMap { sortedChains ->
                            safeSingle {
                                var account: Account = accounts[0]
                                for (chain in sortedChains) {
                                    val foundAccount = accounts
                                        .find { chain.hasChainName(it.baseChain) }
                                    if (foundAccount != null) {
                                        account = foundAccount
                                        break
                                    }
                                }
                                account
                            }
                        }
                        .onErrorReturnItem(accounts[0])
                        .flatMap { account ->
                            accountsRepository
                                .selectAccount(account.id)
                                .toSingleDefault(account)
                        }
                }
            }
    }

    fun createAccount(chain: BaseChain, accountSecrets: AccountSecrets): Completable {
        return singleCallable { UUID.randomUUID().toString() }
            .flatMap { uuid ->
                secretInteractor.entropyFromMnemonic(uuid, accountSecrets.entropy)
                    .map { encryptData ->
                        Account(
                            uuid,
                            accountSecrets.address,
                            chain.chainName,
                            true,
                            encryptData.encDataString,
                            encryptData.ivDataString,
                            true,
                            accountSecrets.mnemonic.size,
                            System.currentTimeMillis(),
                            accountSecrets.path,
                            accountSecrets.customPath
                        )
                    }
            }
            .flatMap(accountsRepository::addAccount)
            .flatMapCompletable(accountsRepository::selectAccount)
            .andThen(showChain(chain.chainName))
    }

    fun createAccount(
        chain: BaseChain,
        address: String,
        entropy: String,
        mnemonicSize: Int,
        path: Int,
        customPath: Int
    ): Completable {
        return singleCallable { UUID.randomUUID().toString() }
            .flatMap { uuid ->
                secretInteractor.entropyFromMnemonic(uuid, entropy)
                    .map { encryptData ->
                        Account(
                            uuid,
                            address,
                            chain.chainName,
                            true,
                            encryptData.encDataString,
                            encryptData.ivDataString,
                            true,
                            mnemonicSize,
                            System.currentTimeMillis(),
                            path,
                            customPath
                        )
                    }
            }
            .flatMap(accountsRepository::addAccount)
            .flatMapCompletable(accountsRepository::selectAccount)
            .andThen(showChain(chain.chainName))
    }

    fun createAccount(
        chain: BaseChain,
        address: String,
        privateKey: String,
        customPath: Int
    ): Completable {
        return singleCallable { UUID.randomUUID().toString() }
            .flatMap { uuid ->
                secretInteractor.entropyFromPrivateKey(uuid, privateKey)
                    .map { encryptData ->
                        Account(
                            uuid,
                            address,
                            chain.chainName,
                            true,
                            encryptData.encDataString,
                            encryptData.ivDataString,
                            false,
                            -1,
                            System.currentTimeMillis(),
                            -1,
                            customPath
                        )
                    }
            }
            .flatMap(accountsRepository::addAccount)
            .flatMapCompletable(accountsRepository::selectAccount)
            .andThen(showChain(chain.chainName))
    }

    fun updateAccount(
        accountId: Long,
        uuid: String,
        entropy: String,
        chainName: String,
        address: String,
        hasPrivateKey: Boolean,
        fromMnemonic: Boolean,
        path: Int,
        mnemonicSize: Int,
        customPath: Int
    ): Completable {
        return secretInteractor.entropyFromMnemonic(uuid, entropy)
            .map { encryptData ->
                Account(
                    uuid,
                    address,
                    chainName,
                    hasPrivateKey,
                    encryptData.encDataString,
                    encryptData.ivDataString,
                    fromMnemonic,
                    mnemonicSize,
                    System.currentTimeMillis(),
                    path,
                    customPath
                ).apply {
                    id = accountId
                }
            }
            .flatMapCompletable(accountsRepository::updateAccount)
            .andThen(accountsRepository.selectAccount(accountId))
            .andThen(showChain(chainName))
    }

    fun updateAccount(
        accountId: Long,
        chainName: String,
        address: String,
        privateKey: String,
        customPath: Int
    ): Completable {
        return getAccount(accountId)
            .flatMap { account ->
                secretInteractor
                    .entropyFromPrivateKey(account.uuid, privateKey)
                    .map { encryptData ->
                        Account(
                            account.uuid,
                            address,
                            chainName,
                            true,
                            encryptData.encDataString,
                            encryptData.ivDataString,
                            false,
                            -1,
                            System.currentTimeMillis(),
                            -1,
                            customPath
                        ).apply {
                            id = accountId
                        }
                    }
            }
            .flatMapCompletable(accountsRepository::updateAccount)
            .andThen(accountsRepository.selectAccount(accountId))
            .andThen(showChain(chainName))
    }

    fun createWatchAccount(chain: BaseChain, address: String): Completable {
        return singleCallable {
            val uuid = UUID.randomUUID().toString()
            Account(
                uuid,
                address.lowercase(),
                chain.chainName,
                System.currentTimeMillis()
            )
        }
            .flatMap(accountsRepository::addAccount)
            .flatMapCompletable(accountsRepository::selectAccount)
            .andThen(showChain(chain.chainName))
    }

    private fun showChain(chain: String): Completable {
        return accountsRepository
            .getHiddenChains()
            .flatMapCompletable { items ->
                val index = items.indexOfFirst { it.chainName == chain }
                if (index >= 0) {
                    singleCallable {
                        items.toMutableList().apply {
                            removeAt(index)
                        }
                    }
                        .flatMapCompletable(accountsRepository::setHiddenChains)
                } else {
                    Completable.complete()
                }
            }
    }

    fun deleteAccount(accountId: Long): Completable {
        return Single.zip(
            getSelectedAccount(),
            getAccount(accountId),
            ::combine
        )
            .flatMapCompletable { (selectedAccount, account) ->
                deleteAccount(account)
                    .andThen(
                        if (selectedAccount.id.equals(account.id)) {
                            selectAccount().ignoreElement()
                        } else {
                            Completable.complete()
                        }
                    )
            }
    }

    fun checkExistsPassword(): Single<Boolean> {
        return accountsRepository.checkExistsPassword()
    }

    private fun deleteAccount(account: Account): Completable {
        return Completable.merge(
            listOf(
                secretInteractor.deleteMnemonicKey(account.uuid).onErrorComplete()
                    .subscribeOn(AppSchedulers.io()),
                secretInteractor.deletePrivateKey(account.uuid).onErrorComplete()
                    .subscribeOn(AppSchedulers.io()),
                accountsRepository.deleteAccount(account.id).onErrorComplete()
                    .subscribeOn(AppSchedulers.io())
            )
        )
    }
}
