package com.fulldive.wallet.presentation.chains.switcher

import com.fulldive.wallet.models.BaseChain
import wannabit.io.cosmostaion.dao.Account

data class ChainsAccountItem(
    val chain: BaseChain,
    val count: Int = 0,
    val account: Account? = null,
    val lastTotal: String = "",
    val selected: Boolean = false,
    val expanded: Boolean = false
)