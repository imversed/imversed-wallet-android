package com.fulldive.wallet.presentation.main.history

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.fulldive.wallet.models.BaseChain
import com.fulldive.wallet.models.Currency
import com.fulldive.wallet.models.WalletBalance
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
import wannabit.io.cosmostaion.utils.PriceProvider
import wannabit.io.cosmostaion.utils.WDp

class MainHistoryFragment : BaseMvpFragment<FragmentMainHistoryBinding>(),
    MainHistoryMoxyView,
    IRefreshTabListener {
    private var balances: List<WalletBalance> = emptyList()
    private var currency: Currency = Currency.getDefault()

    private var historyAdapter: HistoryAdapter? = null

    private val mainActivity: MainActivity?
        get() = getBaseActivity() as? MainActivity

    private val presenter by moxyPresenter {
        getInjector().getInstance<MainHistoryPresenter>()
    }

    override fun getViewBinding() = FragmentMainHistoryBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyAdapter = HistoryAdapter(mainActivity)
        binding {
            cardView.setOnClickListener {
                presenter.onAddressClicked()
            }
            layerRefresher.setColorSchemeColors(
                ContextCompat.getColor(
                    layerRefresher.context,
                    R.color.colorPrimary
                )
            )
            layerRefresher.setOnRefreshListener { presenter.onRefresh() }
            recycler.layoutManager = LinearLayoutManager(
                recycler.context,
                LinearLayoutManager.VERTICAL,
                false
            )
            recycler.setHasFixedSize(true)
            recycler.adapter = historyAdapter
            recycler.addItemDecoration(HistoryViewHeader(mainActivity))

        }
    }

    override fun showAccount(account: Account, chain: BaseChain) {
        binding {
            cardView.setCardBackgroundColor(WDp.getChainBgColor(cardView.context, chain))
            val chainColor = if (account.hasPrivateKey) {
                WDp.getChainColor(imgAccount.context, chain)
            } else {
                ContextCompat.getColor(imgAccount.context, R.color.colorGray0)
            }

            imgAccount.setColorFilter(chainColor, PorterDuff.Mode.SRC_IN)

            walletAddress.text = account.address
            val priceProvider = PriceProvider { denom -> mainActivity?.getPrice(denom) }

            totalValue.text = WDp.dpAllAssetValueUserCurrency(
                chain,
                currency,
                mainActivity!!.baseDao,
                balances,
                priceProvider
            )
        }
    }

    override fun setBalances(balances: List<WalletBalance>) {
        this.balances = balances
    }

    override fun onRefreshTab() {
        if (!isAdded) return
        presenter.onRefresh()
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

    override fun setCurrency(currency: Currency) {
        this.currency = currency
    }

    override fun showAddressDialog(account: Account) {
        AccountShowDialogFragment
            .newInstance(
                account.getAccountTitle(requireContext()),
                account.address
            )
            .let(::showDialog)
    }

    companion object {
        fun newInstance() = MainHistoryFragment()
    }
}