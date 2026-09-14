package com.fulldive.wallet.interactors.accounts

class DuplicateAccountException : Exception()

class AccountsListEmptyException : Exception()

/** The free wallets limit is reached, Imversed PRO is required to add more. */
class AccountsLimitException : Exception()
