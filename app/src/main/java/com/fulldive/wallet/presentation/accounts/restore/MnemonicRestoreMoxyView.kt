package com.fulldive.wallet.presentation.accounts.restore

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.fulldive.wallet.presentation.base.BaseMoxyView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution
import com.fulldive.wallet.models.BaseChain

interface MnemonicRestoreMoxyView : BaseMoxyView {

    @AddToEndSingle
    fun setDictionary(words: List<String>)

    @AddToEndSingle
    fun showChain(chain: BaseChain)

    @OneExecution
    fun updateField(index: Int, text: String, requestFocus: Boolean)

    @OneExecution
    fun updateFields(items: Array<String>, focusedFieldIndex: Int)

    @AddToEndSingle
    fun showWordsCount(count: Int, colorResId: Int)

    @OneExecution
    fun checkPassword()

    @OneExecution
    fun requestSetPassword()

    @OneExecution
    fun showDialog(
        dialogFragment: DialogFragment,
        tag: String,
        cancelable: Boolean
    )

    @OneExecution
    fun showRestorePathActivity(extras: Bundle)

    @OneExecution
    fun finish()
}
