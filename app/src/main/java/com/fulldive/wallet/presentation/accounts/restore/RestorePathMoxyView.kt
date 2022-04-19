package com.fulldive.wallet.presentation.accounts.restore

import com.fulldive.wallet.presentation.base.BaseMoxyView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

interface RestorePathMoxyView : BaseMoxyView {

    @AddToEndSingle
    fun showItems(items: List<WalletItem>)

    @OneExecution
    fun showWaitDialog()

    @OneExecution
    fun hideWaitDialog()

    @OneExecution
    fun showMainActivity()

    @OneExecution
    fun finish()
}
