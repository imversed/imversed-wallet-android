package com.fulldive.wallet.models.local

import com.fulldive.wallet.models.WalletBalance
import wannabit.io.cosmostaion.dao.Account

class AccountWithBalances(
    val account: Account,
    val balances: List<WalletBalance>
)
