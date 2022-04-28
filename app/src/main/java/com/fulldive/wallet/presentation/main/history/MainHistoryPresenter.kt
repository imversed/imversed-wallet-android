package com.fulldive.wallet.presentation.main.history

import com.fulldive.wallet.di.modules.DefaultPresentersModule
import com.fulldive.wallet.extensions.withDefaults
import com.fulldive.wallet.interactors.HistoryInteractor
import com.fulldive.wallet.interactors.accounts.AccountsInteractor
import com.fulldive.wallet.interactors.balances.BalancesInteractor
import com.fulldive.wallet.interactors.settings.SettingsInteractor
import com.fulldive.wallet.models.BaseChain
import com.fulldive.wallet.presentation.base.BaseMoxyPresenter
import com.joom.lightsaber.ProvidedBy
import wannabit.io.cosmostaion.dao.Account
import wannabit.io.cosmostaion.utils.WDp
import javax.inject.Inject

@ProvidedBy(DefaultPresentersModule::class)
class MainHistoryPresenter @Inject constructor(
    private val accountsInteractor: AccountsInteractor,
    private val balancesInteractor: BalancesInteractor,
    private val settingsInteractor: SettingsInteractor,
    private val historyInteractor: HistoryInteractor
) : BaseMoxyPresenter<MainHistoryMoxyView>() {

    private var account: Account? = null

    override fun attachView(view: MainHistoryMoxyView) {
        super.attachView(view)
        viewState.setCurrency(settingsInteractor.getCurrency())
        requestAccount(false)
    }

    fun onAddressClicked() {
        account?.let(viewState::showAddressDialog)
    }

    fun onRefresh() {
        requestAccount(true)
    }

    private fun requestAccount(forced: Boolean) {
        accountsInteractor
            .getCurrentAccount()
            .withDefaults()
            .compositeSubscribe(onSuccess = { account ->
                val accountChanged = this.account?.id != account.id
                if (forced || accountChanged) {
                    this.account = account
                    val chain = BaseChain.getChain(account.baseChain)
                    if (chain != null) {
                        viewState.showAccount(account, chain)
                        requestHistory(account, chain, accountChanged)
                    }
                }
            })
    }

    private fun requestHistory(account: Account, chain: BaseChain, accountChanged: Boolean) {
        balancesInteractor
            .getBalances(account.id)
            .withDefaults()
            .compositeSubscribe(
                onSuccess = { balances ->
                    viewState.setBalances(balances)
                }
            )
        when (chain) {
            BaseChain.BNB_MAIN -> {
                historyInteractor
                    .getBinanceAccountTxHistory(
                        account.address,
                        WDp.threeMonthAgoTimeString(),
                        WDp.cTimeString()
                    )
                    .withDefaults()
                    .compositeSubscribe(
                        onSuccess = { items ->
                            viewState.showBinanceItems(items)
                        },
                        onError = object : OnErrorConsumer() {
                            override fun onError(error: Throwable) {
                                logError(error)
                                if (accountChanged) {
                                    viewState.showBinanceItems(emptyList())
                                } else {
                                    viewState.hideProgress()
                                }
                            }
                        }
                    )
            }
            BaseChain.OKEX_MAIN -> {
                historyInteractor
                    .getOkAccountTxHistory(account.address)
                    .withDefaults()
                    .compositeSubscribe(
                        onSuccess = { items ->
                            viewState.showOkItems(items)
                        },
                        onError = object : OnErrorConsumer() {
                            override fun onError(error: Throwable) {
                                logError(error)
                                if (accountChanged) {
                                    viewState.showOkItems(emptyList())
                                } else {
                                    viewState.hideProgress()
                                }
                            }
                        }
                    )
            }
            else -> {
                historyInteractor
                    .getAccountTxHistory(chain, account.address)
                    .withDefaults()
                    .compositeSubscribe(
                        onSuccess = { items ->
                            viewState.showItems(items)
                        },
                        onError = object : OnErrorConsumer() {
                            override fun onError(error: Throwable) {
                                logError(error)
                                if (accountChanged) {
                                    viewState.showItems(emptyList())
                                } else {
                                    viewState.hideProgress()
                                }
                            }
                        })
            }
        }
    }
}