package com.fulldive.wallet.models.gas

import java.math.BigDecimal

class GasRateProvider {
    private val tinyGasRate: BigDecimal
    private val lowGasRate: BigDecimal
    private val averageGasRate: BigDecimal

    constructor(value: String) {
        tinyGasRate = BigDecimal(value)
        lowGasRate = BigDecimal(value)
        averageGasRate = BigDecimal(value)
    }

    constructor(tiny: String, low: String, average: String) {
        tinyGasRate = BigDecimal(tiny)
        lowGasRate = BigDecimal(low)
        averageGasRate = BigDecimal(average)
    }

    fun get(index: Int): BigDecimal {
        return when (index) {
            0 -> tinyGasRate
            1 -> lowGasRate
            else -> averageGasRate
        }
    }

    companion object {
        val DEFAULT = GasRateProvider("0.00025", "0.0025", "0.025")
        val ZERO = GasRateProvider("0.000")
    }
}