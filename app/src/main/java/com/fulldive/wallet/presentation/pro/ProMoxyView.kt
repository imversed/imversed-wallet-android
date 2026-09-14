package com.fulldive.wallet.presentation.pro

import com.fulldive.wallet.presentation.base.BaseMoxyView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface ProMoxyView : BaseMoxyView {

    @AddToEndSingle
    fun setProState(isPro: Boolean, price: String)
}
