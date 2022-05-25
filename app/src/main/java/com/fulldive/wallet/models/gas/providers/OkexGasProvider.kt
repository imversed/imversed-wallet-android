package com.fulldive.wallet.models.gas.providers

import wannabit.io.cosmostaion.base.BaseConstant
import java.math.BigDecimal

object OkexGasProvider : GasProvider(
    simpleSendGas = V1_GAS_AMOUNT_MID
) {

    private val okStakeMuxGas: BigDecimal = BigDecimal(20000)
    private val okVoteMuxGas: BigDecimal = BigDecimal(50000)
    private val okStakeGas: BigDecimal = BigDecimal(200000)
    private val okVoteGas: BigDecimal = BigDecimal(200000)

    override fun getEstimateGasAmount(type: Int, index: Int): BigDecimal {
        return when (type) {
            BaseConstant.CONST_PW_TX_SIMPLE_SEND -> super.getEstimateGasAmount(type, index)
            BaseConstant.CONST_PW_TX_OK_DEPOSIT, BaseConstant.CONST_PW_TX_OK_WITHDRAW -> okStakeMuxGas
                .multiply(BigDecimal.valueOf(index.toLong()))
                .add(okStakeGas)
            BaseConstant.CONST_PW_TX_OK_DIRECT_VOTE -> okVoteMuxGas
                .multiply(BigDecimal.valueOf(index.toLong()))
                .add(okVoteGas)
            else -> defaultGas
        }
    }
}