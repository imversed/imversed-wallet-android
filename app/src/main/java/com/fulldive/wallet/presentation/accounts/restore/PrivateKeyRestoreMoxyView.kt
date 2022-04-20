package com.fulldive.wallet.presentation.accounts.restore

import androidx.fragment.app.DialogFragment
import com.fulldive.wallet.presentation.base.BaseMoxyView
import moxy.viewstate.strategy.alias.OneExecution

interface PrivateKeyRestoreMoxyView : BaseMoxyView {

    @OneExecution
    fun setPrivateKey(text: String)

    @OneExecution
    fun requestCreatePassword()

    @OneExecution
    fun requestCheckPassword()

    @OneExecution
    fun showWaitDialog()

    @OneExecution
    fun hideWaitDialog()

    @OneExecution
    fun showDialog(
        dialogFragment: DialogFragment,
        tag: String,
        cancelable: Boolean
    )

    @OneExecution
    fun showMainActivity()

    @OneExecution
    fun finish()
}
