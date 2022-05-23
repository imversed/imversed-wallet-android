package com.fulldive.wallet.presentation.main.settings

import com.fulldive.wallet.models.Currency
import com.fulldive.wallet.presentation.base.BaseMoxyView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface MainSettingsMoxyView : BaseMoxyView {

    @AddToEndSingle
    fun setCurrency(currency: Currency)

    @AddToEndSingle
    fun setAppLockEnabled(enabled: Boolean)
}
