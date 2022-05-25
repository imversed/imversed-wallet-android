package com.fulldive.wallet.models.gas.providers

object SecretGasProvider : GasProvider(
    simpleSendGas = 80000,
    simpleDelegateGas = V1_GAS_AMOUNT_MID,
    simpleUndelegateGas = V1_GAS_AMOUNT_MID,
    simpleRedelegateGas = V1_GAS_AMOUNT_HIGH,
    simpleReinvestGas = V1_GAS_AMOUNT_TOO_HIGH,
    simpleChangeRewardAddressGas = 80000,
    voteGas = V1_GAS_AMOUNT_LOW,
    ibcTransferGas = 80000,
    simpleRewardGas = V1_GAS_REWARD_HIGH
)