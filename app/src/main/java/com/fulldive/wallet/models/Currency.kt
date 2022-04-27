package com.fulldive.wallet.models

import com.fulldive.wallet.extensions.unsafeLazy

sealed class Currency(
    val id: Int,
    val title: String,
    val symbol: String
) {
    object USD : Currency(0, "USD", "$")
    object EUR : Currency(1, "EUR", "€")
    object KRW : Currency(2, "KRW", "₩")
    object JPY : Currency(3, "JPY", "¥")
    object CNY : Currency(4, "CNY", "¥")
    object RUB : Currency(5, "RUB", "₽")
    object GBP : Currency(6, "GBP", "£")
    object INR : Currency(7, "INR", "₹")
    object BRL : Currency(8, "BRL", "R$")
    object IDR : Currency(9, "IDR", "Rp")
    object DKK : Currency(10, "DKK", "Kr")
    object NOK : Currency(11, "NOK", "Kr")
    object SEK : Currency(12, "SEK", "Kr")
    object CHF : Currency(13, "CHF", "sFr")
    object AUD : Currency(14, "AUD", "AU$")
    object CAD : Currency(15, "CAD", "$")
    object MYR : Currency(16, "MYR", "RM")

    companion object {
        val currencies by unsafeLazy {
            Currency::class
                .sealedSubclasses
                .mapNotNull { it.objectInstance }
        }

        fun getCurrency(id: Int): Currency? {
            return currencies.find { currency ->
                currency.id == id
            }
        }

        fun getDefault() = USD
    }
}