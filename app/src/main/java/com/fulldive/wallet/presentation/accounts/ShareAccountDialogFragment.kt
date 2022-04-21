package com.fulldive.wallet.presentation.accounts

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.fulldive.wallet.extensions.unsafeLazy
import com.fulldive.wallet.presentation.base.BaseMvpDialogFragment
import com.joom.lightsaber.getInstance
import moxy.ktx.moxyPresenter
import wannabit.io.cosmostaion.databinding.DialogShareTypeBinding

class ShareAccountDialogFragment : BaseMvpDialogFragment<DialogShareTypeBinding>(),
    ShareAccountMoxyView {

    private val address by unsafeLazy {
        arguments?.getString(KEY_ADDRESS)
            ?: throw IllegalStateException("argument address can't be null")
    }

    private val presenter by moxyPresenter {
        getInjector().getInstance<ShareAccountPresenter>()
            .also {
                it.address = address
            }
    }

    override fun getViewBinding() = DialogShareTypeBinding.inflate(layoutInflater)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(0))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDialogCreated(alertDialog: AlertDialog) {
        super.onDialogCreated(alertDialog)

        binding {
            shareQRCodeButton.setOnClickListener {
                presenter.onShareQRClicked(it.context.applicationContext)
            }
            shareTextButton.setOnClickListener {
                presenter.onShareTextClicked(it.context.applicationContext)
                dismiss()
            }
        }
    }

    companion object {
        private const val KEY_ADDRESS = "address"
        fun newInstance(address: String): ShareAccountDialogFragment {
            return ShareAccountDialogFragment().apply {
                arguments = bundleOf(
                    KEY_ADDRESS to address
                )
            }
        }
    }
}