package com.fulldive.wallet.models.gas

import com.fulldive.wallet.models.BaseChain
import wannabit.io.cosmostaion.base.BaseConstant
import java.math.BigDecimal
import java.math.RoundingMode

open class GasFeeEstimateCalculator(
    gasRate: String = "0.025",
    private val scale: Int = 0
) {

    private val gasRate = BigDecimal(gasRate)

    fun calc(chain: BaseChain, type: Int): BigDecimal {
        return calc(chain, type, 0)
    }

    open fun calc(chain: BaseChain, type: Int, index: Int): BigDecimal {
        val gasAmount = chain.gasProvider.getEstimateGasAmount(type, index)
        return gasRate.multiply(gasAmount).setScale(scale, RoundingMode.DOWN)
    }

    companion object {
        val DEFAULT = GasFeeEstimateCalculator()
        val ZERO = GasFeeEstimateCalculator(gasRate = "0.000")
        val ONE = GasFeeEstimateCalculator(gasRate = "1.000")
        val SIF = object : GasFeeEstimateCalculator() {
            private val value = BigDecimal("100000000000000000")
            override fun calc(chain: BaseChain, type: Int, index: Int): BigDecimal {
                return value //TODO: check, maybe it's wrong. and we have to use gasRate "0.50"
            }
        }
        val BNB = object : GasFeeEstimateCalculator() {
            private val value = BigDecimal("0.00007500")
            override fun calc(chain: BaseChain, type: Int, index: Int): BigDecimal {
                return value
            }
        }
        val KAVA = object : GasFeeEstimateCalculator(gasRate = "0.000") {
            override fun calc(chain: BaseChain, type: Int, index: Int): BigDecimal {
                return if (type == BaseConstant.CONST_PW_TX_HTLS_REFUND) {
                    BigDecimal("12500")
                } else {
                    super.calc(chain, type, index)
                }
            }
        }
    }
}