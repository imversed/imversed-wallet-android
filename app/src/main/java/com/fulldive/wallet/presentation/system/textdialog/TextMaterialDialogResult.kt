package com.fulldive.wallet.presentation.system.textdialog

sealed class TextMaterialDialogResult(val checkBoxValue: Boolean?) {
    class PositiveResult(checkBoxValue: Boolean? = null) : TextMaterialDialogResult(checkBoxValue)
    class NegativeResult(checkBoxValue: Boolean? = null) : TextMaterialDialogResult(checkBoxValue)
    class NeutralResult(checkBoxValue: Boolean? = null) : TextMaterialDialogResult(checkBoxValue)
    object DismissResult : TextMaterialDialogResult(false)
}
