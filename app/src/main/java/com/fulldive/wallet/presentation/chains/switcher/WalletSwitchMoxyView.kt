package com.fulldive.wallet.presentation.chains.switcher

import com.fulldive.wallet.presentation.base.BaseMoxyView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

interface WalletSwitchMoxyView : BaseMoxyView {

    @AddToEndSingle
    fun showItems(items: List<ChainsAccountItem>, scrollToIndex: Int)

    @OneExecution
    fun finish()
}
