package com.fulldive.wallet.models

import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import com.fulldive.wallet.extensions.unsafeLazy
import wannabit.io.cosmostaion.utils.WDp
import java.math.BigDecimal
import java.text.DecimalFormat

sealed class Currency(
    val id: Int,
    val title: String,
    val symbol: String,
    val divider: Int = 3
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

    private val decimalFormat: DecimalFormat by unsafeLazy {
        WDp.getDecimalFormat(divider)
    }

    fun format(value: BigDecimal, fractionalPartSize: Float = 0.8f): SpannableString {
        return SpannableString("$symbol ${decimalFormat.format(value)}")
            .apply {
                setSpan(
                    RelativeSizeSpan(fractionalPartSize),
                    length - divider,
                    length,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE
                )
            }
    }

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