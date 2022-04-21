package com.fulldive.wallet.presentation.chains.switcher

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.fulldive.wallet.presentation.base.BaseMvpActivity
import com.joom.lightsaber.getInstance
import moxy.ktx.moxyPresenter
import wannabit.io.cosmostaion.databinding.ActivityWalletSwitchBinding

class WalletSwitchActivity : BaseMvpActivity<ActivityWalletSwitchBinding>(), WalletSwitchMoxyView {
    private var adapter: AccountListAdapter? = null

    private val presenter by moxyPresenter {
        appInjector.getInstance<WalletSwitchPresenter>()
    }

    override fun getViewBinding() = ActivityWalletSwitchBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding {
            closeButton.setOnClickListener {
                presenter.onCloseClicked()
            }

            recyclerView.layoutManager = LinearLayoutManager(
                recyclerView.context,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = AccountListAdapter(
                { chainName ->
                    presenter.onChainHeaderClicked(chainName)
                },
                { accountId ->
                    presenter.onAccountClicked(accountId)
                }
            )
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter
        }
    }


    override fun showItems(items: List<ChainsAccountItem>, scrollToIndex: Int) {
        adapter?.setItems(items)
        if (scrollToIndex >= 0) {
            binding?.recyclerView?.scrollToPosition(scrollToIndex)
        }
    }
}