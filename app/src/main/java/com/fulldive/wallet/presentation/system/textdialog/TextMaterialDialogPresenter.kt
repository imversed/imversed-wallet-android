package com.fulldive.wallet.presentation.system.textdialog

import com.fulldive.wallet.di.modules.DefaultPresentersModule
import com.fulldive.wallet.interactors.ScreensInteractor
import com.fulldive.wallet.presentation.base.BaseMoxyPresenter
import com.fulldive.wallet.presentation.base.BaseMoxyView
import com.joom.lightsaber.ProvidedBy
import javax.inject.Inject

@ProvidedBy(DefaultPresentersModule::class)
class TextMaterialDialogPresenter @Inject constructor(
    private val screensInteractor: ScreensInteractor
) : BaseMoxyPresenter<BaseMoxyView>() {
    var resultCode = ""
    private var resultSent = false
    private var checkBoxValue: Boolean? = null

    override fun onDestroy() {
        if (!resultSent) {
            resultSent = true
            screensInteractor.sendResult(resultCode, TextMaterialDialogResult.DismissResult)
        }
        super.onDestroy()
    }

    fun onPositive() {
        resultSent = true
        screensInteractor.sendResult(
            resultCode,
            TextMaterialDialogResult.PositiveResult(checkBoxValue)
        )
    }

    fun onNegative() {
        resultSent = true
        screensInteractor.sendResult(
            resultCode,
            TextMaterialDialogResult.NegativeResult(checkBoxValue)
        )
    }

    fun onNeutral() {
        resultSent = true
        screensInteractor.sendResult(
            resultCode,
            TextMaterialDialogResult.NeutralResult(checkBoxValue)
        )
    }

    fun onCheckBoxChanged(value: Boolean) {
        checkBoxValue = value
    }
}
