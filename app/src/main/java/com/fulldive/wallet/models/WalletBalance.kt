package com.fulldive.wallet.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import wannabit.io.cosmostaion.dao.Balance

@Entity(tableName = "WalletBalance")
data class WalletBalance(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val id: Long,
    @SerializedName("accountId")
    val accountId: Long,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("balance")
    val balance: String,
    @SerializedName("frozen")
    val frozen: String,
    @SerializedName("locked")
    val locked: String,
    @SerializedName("fetchTime")
    val fetchTime: Long
) {

    companion object {
        fun create(
            id: Long = 0L,
            accountId: Long,
            symbol: String,
            balance: String,
            frozen: String = "",
            locked: String = "",
            fetchTime: Long = System.currentTimeMillis()
        ): WalletBalance {
            return WalletBalance(
                id,
                accountId,
                symbol,
                balance,
                frozen,
                locked,
                fetchTime
            )
        }
    }
}

fun WalletBalance.toBalance(): Balance {
    return Balance(accountId, symbol, balance, fetchTime, frozen, locked)
}
