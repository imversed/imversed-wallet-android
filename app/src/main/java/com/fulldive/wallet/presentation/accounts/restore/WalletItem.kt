package com.fulldive.wallet.presentation.accounts.restore

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import wannabit.io.cosmostaion.R
import java.math.BigDecimal

data class WalletItem(
    val address: String,
    val path: Int,
    val mnemonicPath: String,
    @StringRes
    val symbolTitle: Int,
    @ColorRes
    val color: Int,
    @ColorRes
    val chainBackground: Int,
    val state: WalletState,
    val amount: BigDecimal,
    val divideDecimal: Int,
    val displayDecimal: Int,
    val accountId: Long = 0L,
    val accountUuid: String = ""
)


sealed class WalletState(
    @StringRes
    val stateText: Int,
    @ColorRes
    val stateColor: Int
) {
    object ReadyState : WalletState(R.string.str_ready, R.color.colorWhite)
    object ImportedState : WalletState(R.string.str_imported, R.color.colorGray1)
    object OverrideState : WalletState(R.string.str_override, R.color.colorWhite)
}
