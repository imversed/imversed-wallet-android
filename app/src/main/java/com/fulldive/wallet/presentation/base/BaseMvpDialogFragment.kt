package com.fulldive.wallet.presentation.base

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.fulldive.wallet.di.IEnrichableActivity
import com.fulldive.wallet.di.IInjectorHolder
import com.fulldive.wallet.extensions.clearUi
import com.fulldive.wallet.extensions.toast
import com.joom.lightsaber.Injector
import moxy.MvpAppCompatDialogFragment

abstract class BaseMvpDialogFragment<VB : ViewBinding> : MvpAppCompatDialogFragment(),
    IInjectorHolder {

    private val appInjector: Injector
        get() = (activity as IEnrichableActivity).appInjector

    protected var binding: VB? = null

    override fun getInjector() = appInjector

    abstract fun getViewBinding(): VB

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = getViewBinding().also { binding = it }.root

        return AlertDialog.Builder(activity)
            .setView(view)
            .create()
            .also(::onDialogCreated)
    }

    override fun onDestroyView() {
        binding = null
        clearUi()
        super.onDestroyView()
    }

    protected fun binding(viewBinding: VB.() -> Unit) {
        binding?.apply { viewBinding() }
    }

    open fun onDialogCreated(alertDialog: AlertDialog) {
    }

    open fun showMessage(@StringRes resourceId: Int) {
        context?.toast(resourceId)
    }

    open fun showMessage(message: String) {
        context?.toast(message)
    }

    open fun showDialog(
        dialogFragment: DialogFragment,
        tag: String = "dialog",
        cancelable: Boolean = true
    ) {
        dialogFragment.isCancelable = cancelable
        parentFragmentManager
            .beginTransaction()
            .add(dialogFragment, tag)
            .commitNowAllowingStateLoss()
    }
}