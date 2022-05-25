package com.fulldive.wallet.models.gas.providers

import wannabit.io.cosmostaion.base.BaseConstant
import java.math.BigDecimal

object OsmosisGasProvider : GasProvider(
    simpleSendGas = V1_GAS_AMOUNT_LOW,
    simpleDelegateGas = V1_GAS_AMOUNT_MID,
    simpleUndelegateGas = V1_GAS_AMOUNT_MID,
    simpleRedelegateGas = V1_GAS_AMOUNT_HIGH,
    simpleReinvestGas = V1_GAS_AMOUNT_TOO_HIGH,
    simpleChangeRewardAddressGas = V1_GAS_AMOUNT_LOW,
    voteGas = V1_GAS_AMOUNT_LOW,
    ibcTransferGas = V1_GAS_AMOUNT_LARGE,
    simpleRewardGas = V1_GAS_REWARD_LOW
) {

    private val osmosisSwapGas: BigDecimal = BigDecimal(V1_GAS_AMOUNT_LARGE)
    private val osmosisPoolGas: BigDecimal = BigDecimal(1500000)
    private val osmosisEarningGas: BigDecimal = BigDecimal(1500000)
    private val osmosisUnbondingGas: BigDecimal = BigDecimal(1500000)
    private val osmosisUnlockGas: BigDecimal = BigDecimal(1500000)

    override fun getEstimateGasAmount(type: Int, index: Int): BigDecimal {
        return when (type) {
            BaseConstant.CONST_PW_TX_OSMOSIS_SWAP -> osmosisSwapGas
            BaseConstant.CONST_PW_TX_OSMOSIS_JOIN_POOL, BaseConstant.CONST_PW_TX_OSMOSIS_EXIT_POOL -> osmosisPoolGas
            BaseConstant.CONST_PW_TX_OSMOSIS_EARNING -> osmosisEarningGas
            BaseConstant.CONST_PW_TX_OSMOSIS_BEGIN_UNBONDING -> osmosisUnbondingGas
            BaseConstant.CONST_PW_TX_OSMOSIS_UNLOCK -> osmosisUnlockGas
            else -> super.getEstimateGasAmount(type, index)
        }
    }
}