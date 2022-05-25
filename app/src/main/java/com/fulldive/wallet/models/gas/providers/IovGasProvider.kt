package com.fulldive.wallet.models.gas.providers

import wannabit.io.cosmostaion.base.BaseConstant
import java.math.BigDecimal

object IovGasProvider : GasProvider(
    simpleSendGas = V1_GAS_AMOUNT_LOW,
    simpleDelegateGas = V1_GAS_AMOUNT_MID,
    simpleUndelegateGas = V1_GAS_AMOUNT_MID,
    simpleRedelegateGas = V1_GAS_AMOUNT_HIGH,
    simpleReinvestGas = V1_GAS_AMOUNT_HIGH,
    simpleChangeRewardAddressGas = V1_GAS_AMOUNT_LOW,
    voteGas = V1_GAS_AMOUNT_LOW,
    ibcTransferGas = V1_GAS_AMOUNT_LARGE,
    simpleRewardGas = V1_GAS_REWARD_HIGH
) {

    private val registerGas: BigDecimal = BigDecimal(V1_GAS_AMOUNT_HIGH)
    private val deleteGas: BigDecimal = BigDecimal(150000)
    private val renewGas: BigDecimal = BigDecimal(V1_GAS_AMOUNT_HIGH)
    private val replaceStarnameGas: BigDecimal = BigDecimal(V1_GAS_AMOUNT_HIGH)

    override fun getEstimateGasAmount(type: Int, index: Int): BigDecimal {
        return when (type) {
            BaseConstant.CONST_PW_TX_REGISTER_DOMAIN, BaseConstant.CONST_PW_TX_REGISTER_ACCOUNT -> registerGas
            BaseConstant.CONST_PW_TX_DELETE_DOMAIN, BaseConstant.CONST_PW_TX_DELETE_ACCOUNT -> deleteGas
            BaseConstant.CONST_PW_TX_RENEW_DOMAIN, BaseConstant.CONST_PW_TX_RENEW_ACCOUNT -> renewGas
            BaseConstant.CONST_PW_TX_REPLACE_STARNAME -> replaceStarnameGas
            else -> super.getEstimateGasAmount(type, index)
        }
    }
}