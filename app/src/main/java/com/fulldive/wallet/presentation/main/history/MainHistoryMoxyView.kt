package com.fulldive.wallet.presentation.main.history

import com.fulldive.wallet.models.BaseChain
import com.fulldive.wallet.models.Currency
import com.fulldive.wallet.models.WalletBalance
import com.fulldive.wallet.presentation.base.BaseMoxyView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution
import wannabit.io.cosmostaion.dao.Account
import wannabit.io.cosmostaion.model.type.BnbHistory
import wannabit.io.cosmostaion.network.res.ResApiNewTxListCustom
import wannabit.io.cosmostaion.network.res.ResOkHistoryHit

interface MainHistoryMoxyView : BaseMoxyView {

    @AddToEndSingle
    fun showAccount(account: Account, chain: BaseChain)

    @AddToEndSingle
    fun showBinanceItems(items: List<BnbHistory>)

    @AddToEndSingle
    fun showOkItems(items: List<ResOkHistoryHit>)

    @AddToEndSingle
    fun showItems(items: List<ResApiNewTxListCustom>)

    @AddToEndSingle
    fun setBalances(balances: List<WalletBalance>)

    @AddToEndSingle
    fun setCurrency(currency: Currency)

    @OneExecution
    fun showAddressDialog(account: Account)

    @OneExecution
    fun hideProgress()
}
