package com.fulldive.wallet.models

import wannabit.io.cosmostaion.R

open class TokensProvider {
    open fun getToken(denom: String?, mainToken: Token, subTokens: List<Token>): Token? {
        return when {
            denom == null -> null
            mainToken.denom.equals(denom, true) -> mainToken
            else -> subTokens.find { it.denom.equals(denom, true) }
        }
    }
}

object EMoneyTokenProvider : TokensProvider() {
    override fun getToken(denom: String?, mainToken: Token, subTokens: List<Token>): Token? {
        return when {
            denom == null -> null
            mainToken.denom.equals(denom, true) -> mainToken
            denom.startsWith("e", true) -> {
                val uppercaseDenom = denom.uppercase()
                Token(
                    denom = denom,
                    name = "${uppercaseDenom.substring(1)} on E-Money Network",
                    symbol = uppercaseDenom,
                    coinIcon = "https://raw.githubusercontent.com/cosmostation/cosmostation_token_resource/master/coin_image/emoney/$denom.png",
                    coinColorRes = R.color.colorWhite
                )
            }
            else -> subTokens.find { it.denom.equals(denom, true) }
        }
    }
}