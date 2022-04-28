package com.fulldive.wallet.database

import androidx.room.Dao
import androidx.room.Query
import com.fulldive.wallet.models.WalletBalance
import io.reactivex.Single

@Dao
interface WalletBalanceDao : BaseDao<WalletBalance> {

    @Query("SELECT * FROM WalletBalance WHERE accountId = :accountId and denom = :denom")
    fun getBalance(accountId: Long, denom: String): Single<WalletBalance>

    @Query("SELECT * FROM WalletBalance WHERE accountId = :accountId")
    fun getBalances(accountId: Long): Single<List<WalletBalance>>

    @Query("DELETE FROM WalletBalance WHERE accountId = :accountId")
    fun deleteBalances(accountId: Long)
}