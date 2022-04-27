package com.fulldive.wallet.presentation.main.currency

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fulldive.wallet.extensions.safe
import com.fulldive.wallet.extensions.unsafeLazy
import com.fulldive.wallet.interactors.ScreensInteractor
import com.fulldive.wallet.interactors.settings.SettingsInteractor
import com.fulldive.wallet.models.Currency
import com.fulldive.wallet.presentation.base.BaseMvpDialogFragment
import com.joom.lightsaber.getInstance
import wannabit.io.cosmostaion.databinding.DialogListBinding

class CurrencyDialogFragment : BaseMvpDialogFragment<DialogListBinding>() {

    private val requestCode by unsafeLazy {
        arguments?.getString(KEY_REQUEST_CODE).orEmpty()
    }

    private var resultSent = false
    private var adapter: CurrencyListAdapter? = null
    private lateinit var screensInteractor: ScreensInteractor
    private lateinit var settingsInteractor: SettingsInteractor

    override fun getViewBinding() = DialogListBinding.inflate(layoutInflater)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(0))
        screensInteractor = getInjector().getInstance()
        settingsInteractor = getInjector().getInstance()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDialogCreated(alertDialog: AlertDialog) {
        super.onDialogCreated(alertDialog)
        binding {
            val linearLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerView.layoutManager = linearLayout
            recyclerView.addItemDecoration(
                DividerItemDecoration(
                    recyclerView.context,
                    linearLayout.orientation
                )
            )
            recyclerView.setHasFixedSize(true)
            adapter = CurrencyListAdapter(Currency.currencies, ::onItemClicked)
            recyclerView.adapter = adapter

        }
    }

    override fun onDestroy() {
        sendResult(Unit)
        super.onDestroy()
    }

    private fun sendResult(result: Any) {
        if (!resultSent) {
            resultSent = true
            screensInteractor.sendResult(requestCode, result)
        }
    }

    private fun onItemClicked(currency: Currency) {
        sendResult(currency.id)

        settingsInteractor.setCurrency(currency)

        // TODO: remove
        safe {
            val resultIntent = Intent()
            resultIntent.putExtra("currency", currency.id)
            targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, resultIntent)
        }
        dismiss()
    }

    companion object {
        private const val KEY_REQUEST_CODE = "KEY_REQUEST_CODE"

        @JvmStatic
        fun newInstance(
            requestCode: String = ""
        ) = CurrencyDialogFragment().apply {
            arguments = bundleOf(
                KEY_REQUEST_CODE to requestCode,
            )
        }
    }
}