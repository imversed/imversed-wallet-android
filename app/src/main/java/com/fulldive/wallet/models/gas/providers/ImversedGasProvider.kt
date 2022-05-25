package com.fulldive.wallet.models.gas.providers

object ImversedGasProvider : GasProvider(
    simpleSendGas = V1_GAS_AMOUNT_MID,
    simpleDelegateGas = V1_GAS_AMOUNT_HIGH,
    simpleUndelegateGas = V1_GAS_AMOUNT_HIGH,
    simpleRedelegateGas = V1_GAS_AMOUNT_HIGH,
    simpleReinvestGas = V1_GAS_AMOUNT_HIGH,
    simpleChangeRewardAddressGas = V1_GAS_AMOUNT_MID,
    voteGas = V1_GAS_AMOUNT_MID,
    ibcTransferGas = V1_GAS_AMOUNT_HIGH,
    simpleRewardGas = V1_GAS_REWARD_HIGH
)