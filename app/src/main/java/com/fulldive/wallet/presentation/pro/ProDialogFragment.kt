package com.fulldive.wallet.presentation.pro

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.fulldive.wallet.extensions.getColorCompat
import com.fulldive.wallet.interactors.accounts.AccountsInteractor
import com.fulldive.wallet.presentation.base.BaseMvpDialogFragment
import com.joom.lightsaber.getInstance
import moxy.ktx.moxyPresenter
import wannabit.io.cosmostaion.R
import wannabit.io.cosmostaion.databinding.DialogProBinding

class ProDialogFragment : BaseMvpDialogFragment<DialogProBinding>(), ProMoxyView {

    private val presenter by moxyPresenter {
        getInjector().getInstance<ProPresenter>()
    }

    override fun getViewBinding() = DialogProBinding.inflate(layoutInflater)

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
            buyButton.setOnClickListener {
                presenter.onBuyClicked(requireActivity())
            }
            restoreButton.setOnClickListener {
                presenter.onRestoreClicked()
            }
            closeButton.setOnClickListener {
                dismissAllowingStateLoss()
            }
        }
    }

    override fun setProState(isPro: Boolean, price: String) {
        binding {
            descriptionTextView.text = if (isPro) {
                getString(R.string.str_pro_active_message)
            } else {
                getString(R.string.str_pro_description, AccountsInteractor.FREE_ACCOUNTS_LIMIT)
            }
            buyButton.isVisible = !isPro
            restoreButton.isVisible = !isPro
            buyButton.text = if (price.isEmpty()) {
                getString(R.string.str_pro_buy)
            } else {
                getString(R.string.str_pro_buy_price, price)
            }
        }
    }

    companion object {
        fun newInstance() = ProDialogFragment()
    }
}
