package com.fulldive.wallet.presentation.accounts

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.fulldive.wallet.extensions.getColorCompat
import com.fulldive.wallet.extensions.unsafeLazy
import com.fulldive.wallet.presentation.base.BaseMvpDialogFragment
import com.joom.lightsaber.getInstance
import moxy.ktx.moxyPresenter
import wannabit.io.cosmostaion.R
import wannabit.io.cosmostaion.databinding.DialogAddAccountBinding

class AddAccountDialogFragment : BaseMvpDialogFragment<DialogAddAccountBinding>(),
    AddAccountMoxyView {
    private val chain by unsafeLazy {
        arguments?.getString(KEY_CHAIN).orEmpty()
    }

    private val presenter by moxyPresenter {
        getInjector().getInstance<AddAccountPresenter>()
            .also {
                it.chain = chain
            }
    }

    override fun getViewBinding() = DialogAddAccountBinding.inflate(layoutInflater)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(requireContext().getColorCompat(R.color.colorBlack)))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDialogCreated(alertDialog: AlertDialog) {
        super.onDialogCreated(alertDialog)

        binding {
            btnImportKey.setOnClickListener {
                presenter.onImportKeyClicked(requireActivity())
            }
            btnImportMnemonic.setOnClickListener {
                presenter.onImportMnemonicClicked(requireActivity())
            }
            btnWatchAddress.setOnClickListener {
                presenter.onWatchAddressClicked(requireActivity())
            }
            btnCreate.setOnClickListener {
                presenter.onCreateClicked(requireActivity())
            }
        }
    }

    companion object {
        private const val KEY_CHAIN = "chain"

        fun newInstance(chain: String): AddAccountDialogFragment {
            return AddAccountDialogFragment().apply {
                arguments = bundleOf(
                    KEY_CHAIN to chain
                )
            }
        }
    }
}