package com.fulldive.wallet.presentation.main.tokens

import androidx.annotation.ColorRes
import com.fulldive.wallet.models.BaseChain
import com.fulldive.wallet.models.Currency
import com.fulldive.wallet.models.WalletBalance
import com.fulldive.wallet.presentation.base.BaseMoxyView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution
import wannabit.io.cosmostaion.dao.Account

interface MainTokensMoxyView : BaseMoxyView {

    @AddToEndSingle
    fun showAddress(address: String, @ColorRes background: Int, @ColorRes keyColor: Int)

    @AddToEndSingle
    fun showAccount(account: Account, chain: BaseChain)

    @AddToEndSingle
    fun setCurrency(currency: Currency)

    @AddToEndSingle
    fun setBalances(balances: List<WalletBalance>)

    @OneExecution
    fun showAddressDialog(account: Account)

    @OneExecution
    fun hideProgress()

    @AddToEndSingle
    fun showItems(items: List<TokenWrappedItem>)
}
