package com.fulldive.wallet.presentation.main.history

import com.fulldive.wallet.di.modules.DefaultPresentersModule
import com.fulldive.wallet.extensions.withDefaults
import com.fulldive.wallet.interactors.HistoryInteractor
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
    private val balancesInteractor: BalancesInteractor,
    private val settingsInteractor: SettingsInteractor,
    private val historyInteractor: HistoryInteractor
) : BaseMoxyPresenter<MainHistoryMoxyView>() {
    private val historyErrorConsumer = object : OnErrorConsumer() {
        override fun onError(error: Throwable) {
            logError(error)
            viewState.hideProgress()
        }
    }

    override fun attachView(view: MainHistoryMoxyView) {
        super.attachView(view)
        viewState.setCurrency(settingsInteractor.getCurrency())
    }

    fun onFetchHistory(account: Account, chain: BaseChain) {
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
                        onError = historyErrorConsumer
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
                        onError = historyErrorConsumer
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
                        onError = historyErrorConsumer
                    )
            }
        }
    }
}