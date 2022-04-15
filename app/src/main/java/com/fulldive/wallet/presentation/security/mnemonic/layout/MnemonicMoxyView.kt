package com.fulldive.wallet.presentation.security.mnemonic.layout

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.fulldive.wallet.presentation.base.BaseMoxyView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface MnemonicMoxyView : BaseMoxyView {
    @AddToEndSingle
    fun setColors(@ColorRes backgroundResId: Int, @DrawableRes wordsBackgroundResId: Int)

    @AddToEndSingle
    fun showMnemonicWords(mnemonicWords: List<String>)
}
