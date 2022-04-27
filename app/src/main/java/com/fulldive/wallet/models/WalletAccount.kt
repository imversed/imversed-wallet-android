package com.fulldive.wallet.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import wannabit.io.cosmostaion.dao.Account
import java.util.*

@Entity(tableName = "WalletAccount")
data class WalletAccount(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val id: Long,
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("nickName")
    val nickName: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("chain")
    val chain: String,
    @SerializedName("hasPrivateKey")
    val hasPrivateKey: Boolean,
    @SerializedName("resource")
    val resource: String,
    @SerializedName("spec")
    val spec: String,
    @SerializedName("fromMnemonic")
    val fromMnemonic: Boolean,
    @SerializedName("path")
    val path: Int,
    @SerializedName("customPath")
    val customPath: Int,
    @SerializedName("sequenceNumber")
    val sequenceNumber: Int?,
    @SerializedName("accountNumber")
    val accountNumber: Int?,
    @SerializedName("msize")
    val msize: Int,
    @SerializedName("importTime")
    val importTime: Long,
) {

    companion object {
        fun create(
            address: String,
            chain: String,
            id: Long = 0L,
            uuid: String = UUID.randomUUID().toString(),
            nickName: String = "",
            hasPrivateKey: Boolean = false,
            resource: String = "",
            spec: String = "",
            fromMnemonic: Boolean = false,
            path: Int = 0,
            customPath: Int = 0,
            sequenceNumber: Int? = null,
            accountNumber: Int? = null,
            msize: Int = 0,
            importTime: Long = System.currentTimeMillis()
        ): WalletAccount {
            return WalletAccount(
                id,
                uuid,
                nickName,
                address,
                chain,
                hasPrivateKey,
                resource,
                spec,
                fromMnemonic,
                path,
                customPath,
                sequenceNumber,
                accountNumber,
                msize,
                importTime
            )
        }
    }
}

fun WalletAccount.toAccount(): Account {
    return Account(
        id,
        uuid,
        nickName,
        address,
        chain,
        hasPrivateKey,
        resource,
        spec,
        fromMnemonic,
        path,
        sequenceNumber ?: 0,
        accountNumber ?: 0,
        msize,
        importTime,
        customPath
    )
}

fun Account.toWalletAccount(): WalletAccount {
    return WalletAccount(
        id,
        uuid,
        nickName,
        address,
        baseChain,
        hasPrivateKey,
        resource,
        spec,
        fromMnemonic,
        path,
        customPath,
        sequenceNumber,
        accountNumber,
        msize,
        importTime
    )
}