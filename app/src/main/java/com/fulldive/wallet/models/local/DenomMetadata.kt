package com.fulldive.wallet.models.local

import cosmos.bank.v1beta1.Bank

data class DenomUnit(
    val denom: String,
    val aliases: List<String>,
    val expanent: Int
) {
    companion object {
        fun from(value: Bank.DenomUnit): DenomUnit {
            return DenomUnit(
                value.denom,
                value.aliasesList,
                value.exponent
            )
        }
    }
}

data class DenomMetadata(
    val base: String,
    val symbol: String,
    val display: String,
    val name: String,
    val uri: String,
    val description: String,
    val denomUnits: List<DenomUnit>
) {
    fun getDenomUnit(denom: String): DenomUnit? {
        return denomUnits.firstOrNull { it.aliases.contains(denom) }
    }

    companion object {
        fun from(value: Bank.Metadata): DenomMetadata {
            return DenomMetadata(
                value.base,
                value.symbol,
                value.display,
                value.name,
                value.uri,
                value.description,
                value.denomUnitsList.map(DenomUnit::from)
            )
        }
    }
}