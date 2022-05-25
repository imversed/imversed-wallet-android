package com.fulldive.wallet.models.gas.providers

import wannabit.io.cosmostaion.base.BaseConstant
import java.math.BigDecimal

object KavaGasProvider : GasProvider(
    simpleSendGas = 400000,
    simpleDelegateGas = 800000,
    simpleUndelegateGas = 800000,
    simpleRedelegateGas = 800000,
    simpleReinvestGas = 800000,
    simpleChangeRewardAddressGas = V1_GAS_AMOUNT_LOW,
    voteGas = V1_GAS_AMOUNT_HIGH,
    ibcTransferGas = V1_GAS_AMOUNT_LARGE,
    simpleRewardGas = V1_GAS_REWARD_HIGH
) {

    private val kavaClaimGas: BigDecimal = BigDecimal(2000000)
    private val kavaCdpGas: BigDecimal = BigDecimal(2000000)
    private val kavaHardGas: BigDecimal = BigDecimal(800000)
    private val kavaBep3Gas: BigDecimal = BigDecimal(500000)
    private val kavaSwapGas: BigDecimal = BigDecimal(800000)
    private val kavaPoolGas: BigDecimal = BigDecimal(800000)

    override fun getEstimateGasAmount(type: Int, index: Int): BigDecimal {
        return when (type) {
            BaseConstant.CONST_PW_TX_CLAIM_INCENTIVE,
            BaseConstant.CONST_PW_TX_CLAIM_HARVEST_REWARD -> kavaClaimGas
            BaseConstant.CONST_PW_TX_CREATE_CDP,
            BaseConstant.CONST_PW_TX_DEPOSIT_CDP,
            BaseConstant.CONST_PW_TX_WITHDRAW_CDP,
            BaseConstant.CONST_PW_TX_DRAW_DEBT_CDP,
            BaseConstant.CONST_PW_TX_REPAY_CDP -> kavaCdpGas
            BaseConstant.CONST_PW_TX_DEPOSIT_HARD,
            BaseConstant.CONST_PW_TX_WITHDRAW_HARD,
            BaseConstant.CONST_PW_TX_BORROW_HARD,
            BaseConstant.CONST_PW_TX_REPAY_HARD -> kavaHardGas
            BaseConstant.CONST_PW_TX_HTLS_REFUND -> kavaBep3Gas
            BaseConstant.CONST_PW_TX_KAVA_SWAP -> kavaSwapGas
            BaseConstant.CONST_PW_TX_KAVA_JOIN_POOL,
            BaseConstant.CONST_PW_TX_KAVA_EXIT_POOL -> kavaPoolGas
            else -> super.getEstimateGasAmount(type, index)
        }
    }
}