package com.fulldive.wallet.models.gas.providers

import wannabit.io.cosmostaion.base.BaseConstant
import java.math.BigDecimal

object AlterGasProvider : GasProvider(
    simpleSendGas = V1_GAS_AMOUNT_LOW,
    simpleDelegateGas = V1_GAS_AMOUNT_MID,
    simpleUndelegateGas = V1_GAS_AMOUNT_MID,
    simpleRedelegateGas = V1_GAS_AMOUNT_HIGH,
    simpleReinvestGas = V1_GAS_AMOUNT_TOO_HIGH,
    simpleChangeRewardAddressGas = V1_GAS_AMOUNT_LOW,
    voteGas = V1_GAS_AMOUNT_LOW,
    ibcTransferGas = V1_GAS_AMOUNT_LOW,
    simpleRewardGas = V1_GAS_REWARD_LOW
) {

    private val sifGas: BigDecimal = BigDecimal(250000)

    override fun getEstimateGasAmount(type: Int, index: Int): BigDecimal {
        return when (type) {
            BaseConstant.CONST_PW_TX_SIF_CLAIM_INCENTIVE,
            BaseConstant.CONST_PW_TX_SIF_SWAP,
            BaseConstant.CONST_PW_TX_SIF_JOIN_POOL,
            BaseConstant.CONST_PW_TX_SIF_EXIT_POOL -> sifGas
            else -> super.getEstimateGasAmount(type, index)
        }
    }
}