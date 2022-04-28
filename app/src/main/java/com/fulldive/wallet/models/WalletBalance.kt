package com.fulldive.wallet.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.fulldive.wallet.extensions.or
import com.fulldive.wallet.extensions.safe
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

@Entity(tableName = "WalletBalance")
data class WalletBalance(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val id: Long,
    @SerializedName("accountId")
    val accountId: Long,
    @SerializedName("denom")
    val denom: String,
    @SerializedName("balance")
    val balance: String,
    @SerializedName("frozen")
    val frozen: String,
    @SerializedName("locked")
    val locked: String,
    @SerializedName("fetchTime")
    val fetchTime: Long
) {

    @Ignore
    private var _balance: BigDecimal? = null

    @Ignore
    private var _locked: BigDecimal? = null

    @Ignore
    private var _frozen: BigDecimal? = null

    fun getBalanceAmount(): BigDecimal {
        return _balance.or {
            safe({ BigDecimal(balance) }, BigDecimal.ZERO).also {
                _balance = it
            }
        }
    }

    fun getLockedAmount(): BigDecimal {
        return _locked.or {
            safe({ BigDecimal(locked) }, BigDecimal.ZERO).also {
                _locked = it
            }
        }
    }

    fun getFrozenAmount(): BigDecimal {
        return _frozen.or {
            safe({ BigDecimal(frozen) }, BigDecimal.ZERO).also {
                _frozen = it
            }
        }
    }

    fun getTotalAmount(): BigDecimal? {
        return getBalanceAmount().add(getLockedAmount()).add(getFrozenAmount())
    }

    fun getDelegatableAmount(): BigDecimal {
        return getBalanceAmount().add(getLockedAmount())
    }

    fun isIbc(): Boolean {
        return denom.startsWith("ibc/")
    }

    fun getIbcHash(): String? {
        return if (!isIbc()) {
            null
        } else denom.replace("ibc/".toRegex(), "")
    }

    fun osmosisAmm(): Boolean {
        return denom.startsWith("gamm/pool/")
    }

    fun osmosisAmmDpDenom(): String {
        val split = denom.split("/".toRegex()).toTypedArray()
        return "GAMM-" + split[split.size - 1]
    }

    fun osmosisAmmPoolId(): Long {
        val id = denom.replace("gamm/pool/", "")
        return id.toLong()
    }

    fun injectivePoolId(): Long {
        val id = denom.replace("share", "")
        return id.toLong()
    }

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
