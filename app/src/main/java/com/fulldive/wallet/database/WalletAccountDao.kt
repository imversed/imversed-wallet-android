package com.fulldive.wallet.database

import androidx.room.Dao
import androidx.room.Query
import com.fulldive.wallet.models.WalletAccount
import io.reactivex.Single

@Dao
interface WalletAccountDao : BaseDao<WalletAccount> {
    @Query("SELECT * FROM WalletAccount WHERE id = :accountId")
    fun getAccount(accountId: Long): Single<WalletAccount>

    @Query("SELECT * FROM WalletAccount WHERE chain = :chain and address = :address LIMIT 1")
    fun getAccount(address: String, chain: String): Single<WalletAccount>

    @Query("SELECT * FROM WalletAccount")
    fun getAccounts(): Single<List<WalletAccount>>

    @Query("SELECT * FROM WalletAccount WHERE address = :address")
    fun getAccounts(address: String): Single<List<WalletAccount>>

    @Query("SELECT * FROM WalletAccount WHERE chain = :chain")
    fun getChainAccounts(chain: String): Single<List<WalletAccount>>

    @Query("DELETE FROM WalletAccount WHERE id = :accountId")
    fun deleteAccount(accountId: Long)
}