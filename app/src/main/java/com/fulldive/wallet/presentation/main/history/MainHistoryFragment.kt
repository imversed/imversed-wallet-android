package com.fulldive.wallet.presentation.main.history

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.fulldive.wallet.interactors.settings.SettingsInteractor
import com.fulldive.wallet.models.BaseChain
import com.fulldive.wallet.presentation.accounts.AccountShowDialogFragment
import com.fulldive.wallet.presentation.base.BaseMvpFragment
import com.fulldive.wallet.presentation.main.MainActivity
import com.joom.lightsaber.getInstance
import moxy.ktx.moxyPresenter
import wannabit.io.cosmostaion.R
import wannabit.io.cosmostaion.base.IRefreshTabListener
import wannabit.io.cosmostaion.dao.Account
import wannabit.io.cosmostaion.databinding.FragmentMainHistoryBinding
import wannabit.io.cosmostaion.model.type.BnbHistory
import wannabit.io.cosmostaion.network.res.ResApiNewTxListCustom
import wannabit.io.cosmostaion.network.res.ResOkHistoryHit
import wannabit.io.cosmostaion.utils.WDp

class MainHistoryFragment : BaseMvpFragment<FragmentMainHistoryBinding>(),
    MainHistoryMoxyView,
    IRefreshTabListener {
    private var historyAdapter: HistoryAdapter? = null
    private val account: Account?
        get() {
            return mainActivity?.account
        }
    private val chain: BaseChain?
        get() {
            return account
                ?.let { account -> BaseChain.getChain(account.baseChain) }
        }

    private val mainActivity: MainActivity?
        get() = getBaseActivity() as? MainActivity

    private lateinit var settingsInteractor: SettingsInteractor

    private val presenter by moxyPresenter {
        getInjector().getInstance<MainHistoryPresenter>()
    }

    override fun getViewBinding() = FragmentMainHistoryBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsInteractor = getInjector().getInstance()
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyAdapter = HistoryAdapter(mainActivity)
        binding {
            cardView.setOnClickListener { showAddressDialog() }
            layerRefresher.setColorSchemeColors(
                ContextCompat.getColor(
                    layerRefresher.context,
                    R.color.colorPrimary
                )
            )
            layerRefresher.setOnRefreshListener { onFetchHistory() }
            recycler.layoutManager = LinearLayoutManager(
                recycler.context,
                LinearLayoutManager.VERTICAL,
                false
            )
            recycler.setHasFixedSize(true)
            recycler.adapter = historyAdapter
            recycler.addItemDecoration(HistoryViewHeader(mainActivity))

        }
        onUpdateView()
    }

    private fun onUpdateView() {
        val account = account ?: return
        val chain = chain ?: return
        binding {
            cardView.setCardBackgroundColor(WDp.getChainBgColor(cardView.context, chain))
            val chainColor = if (account.hasPrivateKey) {
                WDp.getChainColor(imgAccount.context, chain)
            } else {
                ContextCompat.getColor(imgAccount.context, R.color.colorGray0)
            }

            imgAccount.setColorFilter(chainColor, PorterDuff.Mode.SRC_IN)

            walletAddress.text = account.address
            totalValue.text = WDp.dpAllAssetValueUserCurrency(
                chain,
                settingsInteractor.getCurrency(),
                mainActivity!!.baseDao
            )

        }
    }

    override fun onRefreshTab() {
        if (!isAdded) return
        onUpdateView()
        onFetchHistory()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        activity?.menuInflater?.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_accounts -> mainActivity?.onClickSwitchWallet()
            R.id.menu_explorer -> mainActivity?.onExplorerView()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showBinanceItems(items: List<BnbHistory>) {
        binding {
            if (items.isEmpty()) {
                emptyHistory.visibility = View.VISIBLE
                recycler.visibility = View.GONE
            } else {
                emptyHistory.visibility = View.GONE
                recycler.visibility = View.VISIBLE
                historyAdapter?.setBnbHistory(items)
            }
            layerRefresher.isRefreshing = false
        }
    }

    override fun showOkItems(items: List<ResOkHistoryHit>) {
        binding {
            if (items.isEmpty()) {
                emptyHistory.visibility = View.VISIBLE
                recycler.visibility = View.GONE
            } else {
                emptyHistory.visibility = View.GONE
                recycler.visibility = View.VISIBLE
                historyAdapter?.setOkHistory(items)
            }
            layerRefresher.isRefreshing = false
        }
    }

    override fun showItems(items: List<ResApiNewTxListCustom>) {
        binding {
            if (items.isEmpty()) {
                emptyHistory.visibility = View.VISIBLE
                recycler.visibility = View.GONE
            } else {
                emptyHistory.visibility = View.GONE
                recycler.visibility = View.VISIBLE
                historyAdapter?.setItems(items)
            }
            layerRefresher.isRefreshing = false
        }
    }

    override fun hideProgress() {
        binding {
            layerRefresher.isRefreshing = false
        }
    }

    private fun showAddressDialog() {
        val account = account ?: return
        AccountShowDialogFragment
            .newInstance(
                account.getAccountTitle(requireContext()),
                account.address
            )
            .let(::showDialog)
    }

    private fun onFetchHistory() {
        val account = account ?: return
        val chain = chain ?: return
        binding?.textNotyet?.visibility = View.GONE

        presenter.onFetchHistory(account, chain)
    }

    companion object {
        fun newInstance() = MainHistoryFragment()
    }
}