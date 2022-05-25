package com.fulldive.wallet.models.gas.providers

object CertikGasProvider : GasProvider(
    simpleSendGas = V1_GAS_AMOUNT_LOW,
    simpleDelegateGas = V1_GAS_AMOUNT_MID,
    simpleUndelegateGas = V1_GAS_AMOUNT_MID,
    simpleRedelegateGas = V1_GAS_AMOUNT_HIGH,
    simpleReinvestGas = V1_GAS_AMOUNT_HIGH,
    simpleChangeRewardAddressGas = V1_GAS_AMOUNT_LOW,
    voteGas = V1_GAS_AMOUNT_LOW,
    ibcTransferGas = V1_GAS_AMOUNT_LARGE,
    simpleRewardGas = V1_GAS_REWARD_HIGH
)