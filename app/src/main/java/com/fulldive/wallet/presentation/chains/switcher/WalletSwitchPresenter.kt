package com.fulldive.wallet.presentation.chains.switcher

import com.fulldive.wallet.di.modules.DefaultPresentersModule
import com.fulldive.wallet.extensions.combine
import com.fulldive.wallet.extensions.withDefaults
import com.fulldive.wallet.interactors.accounts.AccountsInteractor
import com.fulldive.wallet.interactors.chains.ChainsInteractor
import com.fulldive.wallet.models.BaseChain
import com.fulldive.wallet.presentation.base.BaseMoxyPresenter
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Single
import wannabit.io.cosmostaion.dao.Account
import javax.inject.Inject
import kotlin.math.max

@ProvidedBy(DefaultPresentersModule::class)
class WalletSwitchPresenter @Inject constructor(
    private val accountsInteractor: AccountsInteractor,
    private val chainsInteractor: ChainsInteractor
) : BaseMoxyPresenter<WalletSwitchMoxyView>() {
    private var chains: List<BaseChain> = emptyList()
    private var accounts: List<Account> = emptyList()
    private var expandedChains = mutableSetOf<String>()
    private var account: Account? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        account = accountsInteractor.getCurrentAccount().blockingGet()

        Single.zip(
            chainsInteractor.getSortedChains(),
            chainsInteractor.getExpandedChains(),
            accountsInteractor.getAccounts(),
            ::combine
        )
            .withDefaults()
            .compositeSubscribe(
                onSuccess = { (chains, expanded, accounts) ->
                    this.chains = chains
                    this.accounts = accounts
                    this.expandedChains.clear()
                    this.expandedChains.addAll(expanded)
                    account?.baseChain?.let(this.expandedChains::add)
                    updateItems(true)
                }
            )
    }

    fun onChainHeaderClicked(chainName: String) {
        if (!expandedChains.remove(chainName)) {
            expandedChains.add(chainName)
        }
        updateItems(false)
        chainsInteractor
            .setExpandedChains(expandedChains.toList())
            .withDefaults()
            .compositeSubscribe()
    }

    fun onAccountClicked(accountId: Long) {
        accountsInteractor
            .selectAccount(accountId)
            .withDefaults()
            .compositeSubscribe(
                onSuccess = {
                    viewState.finish()
                }
            )
    }

    fun onCloseClicked() {
        viewState.finish()
    }

    private fun updateItems(scrollToSelected: Boolean) {
        val currentAccount = this.account
        val items = mutableListOf<ChainsAccountItem>()
        var scrollToIndex = -1

        for (chain in chains) {
            val chainAccounts = accounts.filter { it.baseChain == chain.chainName }
            if (chainAccounts.isNotEmpty()) {
                val expanded = expandedChains.contains(chain.chainName)
                items.add(
                    ChainsAccountItem(
                        chain,
                        count = chainAccounts.size,
                        expanded = expanded
                    )
                )
                if (expanded)
                    chainAccounts.mapTo(items) { account ->
                        ChainsAccountItem(
                            chain,
                            account = account,
                            selected = account.id == currentAccount?.id,
                            lastTotal = accountsInteractor.getLastTotal(account.id)
                        )
                    }
            }
        }
        if (scrollToSelected && currentAccount != null) {
            val index = items.indexOfFirst { it.account?.id == currentAccount.id }
            if (index > 0) {
                scrollToIndex = max(0, index - 2)
            }
        }
        viewState.showItems(items, scrollToIndex)
    }
}
