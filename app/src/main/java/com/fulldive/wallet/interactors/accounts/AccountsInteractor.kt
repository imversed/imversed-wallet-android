package com.fulldive.wallet.interactors.accounts

import com.fulldive.wallet.di.modules.DefaultInteractorsModule
import com.fulldive.wallet.extensions.combine
import com.fulldive.wallet.extensions.safeSingle
import com.fulldive.wallet.extensions.singleCallable
import com.fulldive.wallet.interactors.chains.ChainsInteractor
import com.fulldive.wallet.interactors.secret.SecretInteractor
import com.fulldive.wallet.models.BaseChain
import com.fulldive.wallet.models.WalletAccount
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
    private val secretInteractor: SecretInteractor,
    private val chainsInteractor: ChainsInteractor
) {

    fun getWalletAccount(accountId: Long): Single<WalletAccount> {
        return accountsRepository.getWalletAccount(accountId)
    }

    fun getAccount(accountId: Long): Single<Account> {
        return accountsRepository.getAccount(accountId)
    }

    fun getAccount(chain: BaseChain, address: String): Single<Account> {
        return accountsRepository.getAccount(chain.chainName, address)
    }

    fun getCurrentAccount(): Single<Account> {
        return accountsRepository.getCurrentAccount()
    }

    fun getAccounts(): Single<List<Account>> {
        return accountsRepository.getAccounts()
    }

    // TODO: migrate to rx
    fun getChainAccounts(chain: BaseChain): List<Account> {
        return accountsRepository.getChainAccounts(chain.chainName).blockingGet()
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

    fun createAccount(chain: BaseChain, address: String): Completable {
        return singleCallable {
            WalletAccount.create(
                address = address.lowercase(),
                chain = chain.chainName
            )
        }
            .flatMap(accountsRepository::addAccount)
            .flatMapCompletable(accountsRepository::selectAccount)
            .andThen(chainsInteractor.showChain(chain.chainName))
    }

    fun createAccount(chain: BaseChain, accountSecrets: AccountSecrets): Completable {
        return singleCallable { UUID.randomUUID().toString() }
            .flatMap { uuid ->
                secretInteractor.entropyFromMnemonic(uuid, accountSecrets.entropy)
                    .map { encryptData ->
                        WalletAccount.create(
                            uuid = uuid,
                            address = accountSecrets.address,
                            chain = chain.chainName,
                            hasPrivateKey = true,
                            resource = encryptData.encDataString,
                            spec = encryptData.ivDataString,
                            fromMnemonic = true,
                            msize = accountSecrets.mnemonic.size,
                            path = accountSecrets.path,
                            customPath = accountSecrets.customPath
                        )
                    }
            }
            .flatMap(accountsRepository::addAccount)
            .flatMapCompletable(accountsRepository::selectAccount)
            .andThen(chainsInteractor.showChain(chain.chainName))
    }

    fun createAccount(
        chain: String,
        address: String,
        entropy: String,
        path: Int,
        customPath: Int,
        mnemonicSize: Int
    ): Completable {
        return singleCallable { UUID.randomUUID().toString() }
            .flatMap { uuid ->
                secretInteractor.entropyFromMnemonic(uuid, entropy)
                    .map { encryptData ->
                        WalletAccount.create(
                            uuid = uuid,
                            address = address,
                            chain = chain,
                            hasPrivateKey = true,
                            resource = encryptData.encDataString,
                            spec = encryptData.ivDataString,
                            fromMnemonic = true,
                            msize = mnemonicSize,
                            path = path,
                            customPath = customPath
                        )
                    }
            }
            .flatMap(accountsRepository::addAccount)
            .flatMapCompletable(accountsRepository::selectAccount)
            .andThen(chainsInteractor.showChain(chain))
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
                        WalletAccount.create(
                            uuid = uuid,
                            address = address,
                            chain = chain.chainName,
                            hasPrivateKey = true,
                            resource = encryptData.encDataString,
                            spec = encryptData.ivDataString,
                            fromMnemonic = false,
                            customPath = customPath
                        )
                    }
            }
            .flatMap(accountsRepository::addAccount)
            .flatMapCompletable(accountsRepository::selectAccount)
            .andThen(chainsInteractor.showChain(chain.chainName))
    }

    fun updateAccount(
        accountId: Long,
        address: String,
        entropy: String,
        path: Int,
        customPath: Int,
        mnemonicSize: Int
    ): Completable {
        return getWalletAccount(accountId)
            .flatMap { account ->
                secretInteractor
                    .entropyFromMnemonic(account.uuid, entropy)
                    .map { encResults ->
                        account.copy(
                            address = address,
                            resource = encResults.encDataString,
                            spec = encResults.ivDataString,
                            hasPrivateKey = true,
                            fromMnemonic = true,
                            path = path,
                            customPath = customPath,
                            msize = mnemonicSize
                        )
                    }
            }
            .flatMapCompletable(accountsRepository::updateAccount)
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
            .andThen(chainsInteractor.showChain(chainName))
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
            .andThen(chainsInteractor.showChain(chainName))
    }

    fun updateAccount(
        accountId: Long,
        address: String,
        sequenceNumber: Int,
        accountNumber: Int
    ): Completable {
        return getAccount(accountId)
            .map { account ->
                account.also {
                    it.address = address
                    it.sequenceNumber = sequenceNumber
                    it.accountNumber = accountNumber
                }
            }
            .flatMapCompletable(accountsRepository::updateAccount)
    }

    fun updateAccountNickName(accountId: Long, nickName: String): Completable {
        return getAccount(accountId)
            .map { account ->
                account.nickName = nickName
                account
            }
            .flatMapCompletable(accountsRepository::updateAccount)
    }

    fun deleteAccount(accountId: Long): Completable {
        return Single.zip(
            getCurrentAccount(),
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

    fun getLastTotal(accountId: Long): String {
        return accountsRepository.getLastTotal(accountId)
    }

    fun updateLastTotal(accountId: Long, amount: String) {
        accountsRepository.updateLastTotal(accountId, amount)
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
