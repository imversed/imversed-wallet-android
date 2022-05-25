package com.fulldive.wallet.models.gas.providers

object BandGasProvider : GasProvider(
    simpleSendGas = V1_GAS_AMOUNT_LOW,
    simpleDelegateGas = V1_GAS_AMOUNT_MID,
    simpleUndelegateGas = V1_GAS_AMOUNT_MID,
    simpleRedelegateGas = 240000,
    simpleReinvestGas = 220000,
    simpleChangeRewardAddressGas = V1_GAS_AMOUNT_LOW,
    voteGas = V1_GAS_AMOUNT_LOW,
    ibcTransferGas = V1_GAS_AMOUNT_LARGE,
    simpleRewardGas = V1_GAS_REWARD_HIGH
)