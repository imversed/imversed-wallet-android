package com.fulldive.wallet.presentation.security.key

import androidx.fragment.app.DialogFragment
import com.fulldive.wallet.models.BaseChain
import com.fulldive.wallet.presentation.base.BaseMoxyView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

interface ShowPrivateKeyMoxyView : BaseMoxyView {

    @AddToEndSingle
    fun showPrivateKey(key: String)

    @AddToEndSingle
    fun showChain(chain: BaseChain)

    @OneExecution
    fun showDialog(
        dialogFragment: DialogFragment,
        tag: String,
        cancelable: Boolean
    )

    @OneExecution
    fun showMainActivity(tabIndex: Int)

    @OneExecution
    fun finish()
}
