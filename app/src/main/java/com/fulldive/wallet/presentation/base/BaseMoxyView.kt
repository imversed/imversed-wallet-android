package com.fulldive.wallet.presentation.base

import androidx.annotation.StringRes
import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution
import wannabit.io.cosmostaion.base.BaseChain


interface BaseMoxyView : MvpView {
    @OneExecution
    fun showMessage(message: String)

    @OneExecution
    fun showMessage(@StringRes resourceId: Int)
}
