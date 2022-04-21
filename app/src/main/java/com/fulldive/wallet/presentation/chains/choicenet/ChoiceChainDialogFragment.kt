package com.fulldive.wallet.presentation.chains.choicenet

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fulldive.wallet.extensions.orFalse
import com.fulldive.wallet.extensions.unsafeLazy
import com.fulldive.wallet.interactors.ScreensInteractor
import com.fulldive.wallet.interactors.accounts.AccountsInteractor
import com.fulldive.wallet.models.BaseChain
import com.fulldive.wallet.presentation.accounts.AddAccountDialogFragment
import com.fulldive.wallet.presentation.base.BaseMvpDialogFragment
import com.joom.lightsaber.getInstance
import wannabit.io.cosmostaion.R
import wannabit.io.cosmostaion.databinding.DialogListBinding


class ChoiceChainDialogFragment : BaseMvpDialogFragment<DialogListBinding>() {
    private val isCheckLimit by unsafeLazy { arguments?.getBoolean(KEY_ADD, false).orFalse() }
    private val isAddNet by unsafeLazy { arguments?.getBoolean(KEY_ADD, false).orFalse() }
    private val requestCode by unsafeLazy { arguments?.getString(KEY_REQUEST_CODE).orEmpty() }
    private val chains: List<String> by unsafeLazy {
        arguments?.getStringArrayList(KEY_CHAINS).orEmpty()
    }

    private var resultSent = false
    private var adapter: ChainListAdapter? = null
    private lateinit var screensInteractor: ScreensInteractor

    override fun getViewBinding() = DialogListBinding.inflate(layoutInflater)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(0))
        screensInteractor = getInjector().getInstance()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDialogCreated(alertDialog: AlertDialog) {
        super.onDialogCreated(alertDialog)

        binding {
            val items = BaseChain.chains.filter { chain ->
                chain.isSupported && (chains.isEmpty() || chains.contains(chain.chainName))
            }

            val linearLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerView.layoutManager = linearLayout
            recyclerView.addItemDecoration(
                DividerItemDecoration(
                    recyclerView.context,
                    linearLayout.orientation
                )
            )
            recyclerView.setHasFixedSize(true)
            adapter = ChainListAdapter(items, ::onChainClicked)
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

    private fun onChainClicked(chain: BaseChain) {
        if (isCheckLimit && getInjector().getInstance<AccountsInteractor>()
                .getAccountsByChain(chain).size >= 5
        ) {
            showMessage(R.string.error_max_account_number)
        } else {
            sendResult(chain)
            if (isAddNet) {
                showDialog(
                    AddAccountDialogFragment.newInstance(
                        chain.chainName
                    )
                )
            }
            dialog?.dismiss()
        }
    }

    companion object {
        private const val KEY_CHECK_LIMIT = "KEY_CHECK_LIMIT"
        private const val KEY_ADD = "KEY_ADD"
        private const val KEY_REQUEST_CODE = "KEY_REQUEST_CODE"
        private const val KEY_CHAINS = "KEY_CHAINS"

        @JvmOverloads
        fun newInstance(
            isAdd: Boolean = true,
            requestCode: String = "",
            chains: List<String> = emptyList(),
            isCheckLimit: Boolean = false
        ) = ChoiceChainDialogFragment().apply {
            arguments = bundleOf(
                KEY_ADD to isAdd,
                KEY_CHECK_LIMIT to isCheckLimit,
                KEY_REQUEST_CODE to requestCode,
                KEY_CHAINS to ArrayList(chains)
            )
        }
    }
}