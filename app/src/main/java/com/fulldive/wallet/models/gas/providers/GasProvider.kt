package com.fulldive.wallet.models.gas.providers

import wannabit.io.cosmostaion.base.BaseConstant
import java.math.BigDecimal

open class GasProvider(
    defaultGas: Int = 0,
    simpleSendGas: Int = V1_GAS_AMOUNT_LOW,
    simpleDelegateGas: Int = V1_GAS_AMOUNT_MID,
    simpleUndelegateGas: Int = V1_GAS_AMOUNT_MID,
    simpleRedelegateGas: Int = V1_GAS_AMOUNT_HIGH,
    simpleReinvestGas: Int = V1_GAS_AMOUNT_HIGH,
    simpleChangeRewardAddressGas: Int = V1_GAS_AMOUNT_LOW,
    voteGas: Int = V1_GAS_AMOUNT_LOW,
    ibcTransferGas: Int = V1_GAS_AMOUNT_LARGE,
    simpleRewardGas: List<Int> = V1_GAS_REWARD_LOW,
    gdexSwapGas: Int = V1_GAS_AMOUNT_MID,
    gdexDepositGas: Int = V1_GAS_AMOUNT_HIGH,
    gdexWithdrawGas: Int = V1_GAS_AMOUNT_HIGH,
    mintNftGas: Int = V1_GAS_AMOUNT_HIGH,
    sendNftGas: Int = V1_GAS_AMOUNT_MID,
    txProfileGas: Int = V1_GAS_AMOUNT_TOO_HIGH,
    txLinkAccountGas: Int = V1_GAS_AMOUNT_TOO_HIGH,
    txExecuteContractGas: Int = V1_GAS_AMOUNT_LARGE
) {

    val defaultGas: BigDecimal = BigDecimal(defaultGas)

    private val simpleSendGas: BigDecimal = BigDecimal(simpleSendGas)
    private val simpleDelegateGas: BigDecimal = BigDecimal(simpleDelegateGas)
    private val simpleUndelegateGas: BigDecimal = BigDecimal(simpleUndelegateGas)
    private val simpleRedelegateGas: BigDecimal = BigDecimal(simpleRedelegateGas)
    private val simpleReinvestGas: BigDecimal = BigDecimal(simpleReinvestGas)
    private val simpleChangeRewardAddressGas: BigDecimal = BigDecimal(simpleChangeRewardAddressGas)
    private val simpleRewardGas: List<BigDecimal> = simpleRewardGas.map { BigDecimal(it) }

    private val voteGas: BigDecimal = BigDecimal(voteGas)
    private val ibcTransferGas: BigDecimal = BigDecimal(ibcTransferGas)

    private val gdexSwapGas: BigDecimal = BigDecimal(gdexSwapGas)
    private val gdexDepositGas: BigDecimal = BigDecimal(gdexDepositGas)
    private val gdexWithdrawGas: BigDecimal = BigDecimal(gdexWithdrawGas)

    private val mintNftGas: BigDecimal = BigDecimal(mintNftGas)
    private val sendNftGas: BigDecimal = BigDecimal(sendNftGas)

    private val txProfileGas: BigDecimal = BigDecimal(txProfileGas)
    private val txLinkAccountGas: BigDecimal = BigDecimal(txLinkAccountGas)

    private val txExecuteContractGas: BigDecimal = BigDecimal(txExecuteContractGas)

    open fun getEstimateGasAmount(type: Int, index: Int): BigDecimal {
        return when (type) {
            BaseConstant.CONST_PW_TX_SIMPLE_SEND -> simpleSendGas
            BaseConstant.CONST_PW_TX_SIMPLE_DELEGATE -> simpleDelegateGas
            BaseConstant.CONST_PW_TX_SIMPLE_UNDELEGATE -> simpleUndelegateGas
            BaseConstant.CONST_PW_TX_SIMPLE_REDELEGATE -> simpleRedelegateGas
            BaseConstant.CONST_PW_TX_REINVEST -> simpleReinvestGas
            BaseConstant.CONST_PW_TX_SIMPLE_REWARD -> simpleRewardGas[index]
            BaseConstant.CONST_PW_TX_SIMPLE_CHANGE_REWARD_ADDRESS -> simpleChangeRewardAddressGas
            BaseConstant.CONST_PW_TX_VOTE -> voteGas
            BaseConstant.CONST_PW_TX_IBC_TRANSFER -> ibcTransferGas
            BaseConstant.CONST_PW_TX_GDEX_SWAP -> gdexSwapGas
            BaseConstant.CONST_PW_TX_GDEX_DEPOSIT -> gdexDepositGas
            BaseConstant.CONST_PW_TX_GDEX_WITHDRAW -> gdexWithdrawGas
            BaseConstant.CONST_PW_TX_MINT_NFT -> mintNftGas
            BaseConstant.CONST_PW_TX_SEND_NFT -> sendNftGas
            BaseConstant.CONST_PW_TX_PROFILE -> txProfileGas
            BaseConstant.CONST_PW_TX_LINK_ACCOUNT -> txLinkAccountGas
            BaseConstant.CONST_PW_TX_EXECUTE_CONTRACT -> txExecuteContractGas
            else -> defaultGas
        }
    }

    companion object {
        const val V1_GAS_AMOUNT_LOW = 100000
        const val V1_GAS_AMOUNT_MID = 200000
        const val V1_GAS_AMOUNT_HIGH = 300000
        const val V1_GAS_AMOUNT_TOO_HIGH = 350000
        const val V1_GAS_AMOUNT_LARGE = 500000
        val V1_GAS_REWARD_LOW = listOf(    // gas_multi_reward
            150000,
            220000,
            280000,
            320000,
            380000,
            440000,
            500000,
            560000,
            620000,
            680000,
            740000,
            820000,
            900000,
            980000,
            1020000,
            1080000
        )
        val V1_GAS_REWARD_HIGH = listOf(    // gas_multi_reward_kava
            300000,
            380000,
            460000,
            540000,
            620000,
            700000,
            800000,
            880000,
            960000,
            1040000,
            1120000,
            1200000,
            1300000,
            1380000,
            1460000,
            1540000
        )
    }
}