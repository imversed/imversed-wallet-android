package com.fulldive.wallet.models.gas.providers

import wannabit.io.cosmostaion.base.BaseConstant
import java.math.BigDecimal

object ChihuahuaGasProvider : GasProvider(
    defaultGas = V1_GAS_AMOUNT_MID,
    simpleRewardGas = V1_GAS_REWARD_LOW,
    simpleRedelegateGas = V1_GAS_AMOUNT_HIGH,
    simpleReinvestGas = V1_GAS_AMOUNT_HIGH,
) {
    override fun getEstimateGasAmount(type: Int, index: Int): BigDecimal {
        return when (type) {
            BaseConstant.CONST_PW_TX_SIMPLE_REDELEGATE,
            BaseConstant.CONST_PW_TX_REINVEST,
            BaseConstant.CONST_PW_TX_SIMPLE_REWARD -> super.getEstimateGasAmount(type, index)
            else -> defaultGas
        }
    }
}