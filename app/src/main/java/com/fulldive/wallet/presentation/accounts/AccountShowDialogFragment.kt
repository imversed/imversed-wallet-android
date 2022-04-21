package com.fulldive.wallet.presentation.accounts

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import com.fulldive.wallet.extensions.unsafeLazy
import com.fulldive.wallet.presentation.base.BaseMvpDialogFragment
import com.joom.lightsaber.getInstance
import moxy.ktx.moxyPresenter
import wannabit.io.cosmostaion.R
import wannabit.io.cosmostaion.databinding.DialogAccountShowBinding

class AccountShowDialogFragment : BaseMvpDialogFragment<DialogAccountShowBinding>(),
    AccountShowMoxyView {

    private val title by unsafeLazy {
        arguments?.getString(KEY_TITLE)
            ?: throw IllegalStateException("argument title can't be null")
    }

    private val address by unsafeLazy {
        arguments?.getString(KEY_ADDRESS)
            ?: throw IllegalStateException("argument address can't be null")
    }

    private val presenter by moxyPresenter {
        getInjector().getInstance<AccountShowPresenter>()
            .also {
                it.address = address
            }
    }

    override fun getViewBinding() = DialogAccountShowBinding.inflate(layoutInflater)

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
            walletName.text = title
            walletAddressTv.text = address
            shareButton.setOnClickListener {
                onShareType()
                dismiss()
            }
            copyButton.setOnClickListener {
                presenter.onCopyButtonClicked(it.context.applicationContext)
            }
        }
    }

    override fun showQRCode(bitmap: Bitmap) {
        dialog
            ?.findViewById<ImageView>(R.id.wallet_address_qr)
            ?.setImageBitmap(bitmap)
    }

    private fun onShareType() {
        val fragment = ShareAccountDialogFragment
            .newInstance(address)
        showDialog(fragment)
    }

    companion object {
        private const val KEY_TITLE = "title"
        private const val KEY_ADDRESS = "address"

        fun newInstance(title: String, address: String): AccountShowDialogFragment {
            return AccountShowDialogFragment()
                .apply {
                    arguments = bundleOf(
                        KEY_TITLE to title,
                        KEY_ADDRESS to address
                    )
                }
        }
    }
}