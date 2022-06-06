package com.fulldive.wallet.models

import androidx.annotation.DrawableRes
import wannabit.io.cosmostaion.R

data class Token(
    val denom: String,
    val symbol: String,
    val name: String,
    val ticker: String = "",
    @DrawableRes
    val coinIconRes: Int = R.drawable.token_ic,
    val coinIcon: String = "",
    val coinColorRes: Int = R.color.colorWhite,
    val divideDecimal: Int = 6,
    val displayDecimal: Int = 6,
)

object Tokens {
    val ATOM = Token(
        denom = "uatom",
        ticker = "atom",
        name = "Cosmos Staking Coin",
        symbol = "ATOM",
        coinIconRes = R.drawable.atom_ic,
        coinColorRes = R.color.colorAtom
    )

    val IMV = Token(
        denom = "aimv",
        name = "Imversed Staking Coin",
        symbol = "IMV",
        coinIconRes = R.drawable.imversed_token_img,
        coinColorRes = R.color.colorImversed,
        divideDecimal = 18,
        displayDecimal = 18,
    )

    val IRIS = Token(
        denom = "uiris",
        ticker = "iris",
        name = "Iris Staking Coin",
        symbol = "IRIS",
        coinIconRes = R.drawable.iris_toket_img,
        coinColorRes = R.color.colorIris,
    )

    val IOV = Token(
        denom = "uiov",
        ticker = "iov",
        name = "Starname Staking Coin",
        symbol = "IOV",
        coinIconRes = R.drawable.iov_token_img,
        coinColorRes = R.color.colorIov,
    )

    val BNB = Token(
        denom = "BNB",
        ticker = "bnb",
        name = "Binance Chain Native Coin",
        symbol = "BNB",
        coinIconRes = R.drawable.bnb_token_img,
        divideDecimal = 0,
        displayDecimal = 8,
        coinColorRes = R.color.colorBnb,
    )

    val KAVA = Token(
        denom = "ukava",
        ticker = "kava",
        name = "Kava Staking Coin",
        symbol = "KAVA",
        coinIconRes = R.drawable.kava_token_img,
        coinColorRes = R.color.colorKava,
    )

    val BAND = Token(
        denom = "uband",
        ticker = "band",
        name = "Band Staking Coin",
        symbol = "BAND",
        coinIconRes = R.drawable.band_token_img,
        coinColorRes = R.color.colorBand,
    )

    val CTK = Token(
        denom = "uctk",
        ticker = "ctk",
        name = "Certik Staking Coin",
        symbol = "CTK",
        coinIconRes = R.drawable.certik_token_img,
        coinColorRes = R.color.colorCertik,
    )

    val SCRT = Token(
        denom = "uscrt",
        ticker = "scrt",
        name = "Secret Native Coin",
        symbol = "SCRT",
        coinIconRes = R.drawable.tokensecret,
        coinColorRes = R.color.colorSecret,
    )

    val AKT = Token(
        denom = "uakt",
        ticker = "akt",
        name = "Akash Staking Coin",
        symbol = "AKT",
        coinIconRes = R.drawable.akash_token_img,
        coinColorRes = R.color.colorAkash,
    )

    val OKT = Token(
        denom = "okt",
        ticker = "okb",
        name = "OKC Staking Coin",
        symbol = "OKT",
        coinIconRes = R.drawable.token_okx,
        coinColorRes = R.color.colorOK,
        divideDecimal = 0,
        displayDecimal = 18,
    )

    val XPRT = Token(
        denom = "uxprt",
        ticker = "xprt",
        name = "Persistence Staking Coin",
        symbol = "XPRT",
        coinIconRes = R.drawable.tokenpersistence,
        coinColorRes = R.color.colorPersis,
    )

    val DVPN = Token(
        denom = "udvpn",
        ticker = "dvpn",
        name = "Sentinel Native Coin",
        symbol = "DVPN",
        coinIconRes = R.drawable.tokensentinel,
        coinColorRes = R.color.colorSentinel,
    )

    val FET = Token(
        denom = "afet",
        ticker = "",  // "fet"
        name = "Fetch,ai Staking Coin",
        symbol = "FET",
        coinIconRes = R.drawable.tokenfetchai,
        coinColorRes = R.color.colorFetch,
        divideDecimal = 18,
        displayDecimal = 18,
    )

    val CRO = Token(
        denom = "basecro",
        ticker = "cro",
        name = "Cronos",
        symbol = "CRO",
        coinIconRes = R.drawable.tokencrypto,
        coinColorRes = R.color.colorCryto,
        divideDecimal = 8,
        displayDecimal = 8,
    )

    val ROWAN = Token(
        denom = "rowan",
        ticker = "rowan",
        name = "Sif Staking Coin",
        symbol = "ROWAN",
        coinIconRes = R.drawable.tokensifchain,
        coinColorRes = R.color.colorSif,
        divideDecimal = 18,
        displayDecimal = 18,
    )

    val XKI = Token(
        denom = "uxki",
        ticker = "",  // "ki"
        name = "KiChain Staking Coin",
        symbol = "XKI",
        coinIconRes = R.drawable.token_kifoundation,
        coinColorRes = R.color.colorKi,
    )

    val OSMO = Token(
        denom = "uosmo",
        ticker = "osmo",
        name = "Osmosis Staking Coin",
        symbol = "OSMO",
        coinIconRes = R.drawable.token_osmosis,
        coinColorRes = R.color.colorOsmosis,
    )

    val MED = Token(
        denom = "umed",
        ticker = "",  // "med"
        name = "Medibloc Staking Coin",
        symbol = "MED",
        coinIconRes = R.drawable.tokenmedibloc,
        coinColorRes = R.color.colorMedi,
    )

    val NGM = Token(
        denom = "ungm",
        ticker = "",  // "ngm"
        name = "E-Money Staking Coin",
        symbol = "NGM",
        coinIconRes = R.drawable.token_emoney,
        coinColorRes = R.color.colorEmoney,
    )

    val ATOLO = Token(
        denom = "uatolo",
        ticker = "atolo",
        name = "Rizon Staking Coin",
        symbol = "ATOLO",
        coinIconRes = R.drawable.token_rizon,
        coinColorRes = R.color.colorRizon,
    )

    val JUNO = Token(
        denom = "ujuno",
        ticker = "",  // "ujuno"
        name = "Juno Staking Coin",
        symbol = "JUNO",
        coinIconRes = R.drawable.token_juno,
        coinColorRes = R.color.colorJuno,
    )

    val REGEN = Token(
        denom = "uregen",
        ticker = "regen",
        name = "Regen Staking Coin",
        symbol = "REGEN",
        coinIconRes = R.drawable.token_regen,
        coinColorRes = R.color.colorRegen,
    )

    val BCNA = Token(
        denom = "ubcna",
        ticker = "",  // "bcna"
        name = "Bitcanna Staking Coin",
        symbol = "BCNA",
        coinIconRes = R.drawable.token_bitcanna,
        coinColorRes = R.color.colorBitcanna,
    )

    val ALTG = Token(
        denom = "ualtg",
        name = "Althea Stacking Coin",
        symbol = "ALTG",
        coinIconRes = R.drawable.token_althea,
        coinColorRes = R.color.colorAlthea,
    )

    val STARS = Token(
        denom = "ustars",
        ticker = "",  // "stars"
        name = "Stargaze Staking Coin",
        symbol = "STARS",
        coinIconRes = R.drawable.token_stargaze,
        coinColorRes = R.color.colorStargaze,
    )

    val GRAVITON = Token(
        denom = "ugraviton",
        name = "G-Bridge Staking Coin",
        symbol = "GRAVITON",
        coinIconRes = R.drawable.token_gravitybridge,
        coinColorRes = R.color.colorGraBridge,
    )

    val CMDX = Token(
        denom = "ucmdx",
        name = "Comdex Staking Coin",
        symbol = "CMDX",
        coinIconRes = R.drawable.token_comdex,
        coinColorRes = R.color.colorComdex,
    )

    val INJ = Token(
        denom = "inj",
        name = "Injective Staking Coin",
        symbol = "INJ",
        coinIconRes = R.drawable.token_injective,
        coinColorRes = R.color.colorInj,
        divideDecimal = 18,
        displayDecimal = 18,
    )

    val BTSG = Token(
        denom = "ubtsg",
        name = "Bitsong Staking Coin",
        symbol = "BTSG",
        coinIconRes = R.drawable.token_bitsong,
        coinColorRes = R.color.colorBitsong,
    )

    val DSM = Token(
        denom = "udsm",
        name = "Desmos Staking Coin",
        symbol = "DSM",
        coinIconRes = R.drawable.token_desmos,
        coinColorRes = R.color.colorDesmos,
    )

    val LUM = Token(
        denom = "ulum",
        name = "Lum Staking Coin",
        symbol = "LUM",
        coinIconRes = R.drawable.token_lum,
        coinColorRes = R.color.colorLum,
    )

    val HUAHUA = Token(
        denom = "uhuahua",
        name = "Chihuahua Staking Coin",
        symbol = "HUAHUA",
        coinIconRes = R.drawable.token_huahua,
        coinColorRes = R.color.colorChihuahua,
    )

    val AXL = Token(
        denom = "uaxl",
        name = "Axelar Staking Coin",
        symbol = "AXL",
        coinIconRes = R.drawable.token_axelar,
        coinColorRes = R.color.colorAxelar,
    )

    val DARC = Token(
        denom = "udarc",
        name = "Konstellation Staking Coin",
        symbol = "DARC",
        coinIconRes = R.drawable.token_konstellation,
        coinColorRes = R.color.colorKonstellation,
    )

    val UMEE = Token(
        denom = "uumee",
        name = "Umee Staking Coin",
        symbol = "UMEE",
        coinIconRes = R.drawable.token_umee,
        coinColorRes = R.color.colorUmee,
    )

    val EVMOS = Token(
        denom = "aevmos",
        name = "Evmos Staking Coin",
        symbol = "EVMOS",
        coinIconRes = R.drawable.token_evmos,
        coinColorRes = R.color.colorEvmos,
        divideDecimal = 18,
        displayDecimal = 18,
    )

    val CUDOS = Token(
        denom = "acudos",
        name = "Cudos Staking Coin",
        symbol = "CUDOS",
        coinIconRes = R.drawable.token_cudos,
        coinColorRes = R.color.colorCudos,
        divideDecimal = 18,
        displayDecimal = 18,
    )

    val HASH = Token(
        denom = "nhash",
        name = "Provenance Staking Coin",
        symbol = "HASH",
        coinIconRes = R.drawable.token_hash,
        coinColorRes = R.color.colorProvenance,
        divideDecimal = 9,
        displayDecimal = 9,
    )

    val CRBRUS = Token(
        denom = "ucrbrus",
        name = "Cerberus Staking Coin",
        symbol = "CRBRUS",
        coinIconRes = R.drawable.token_cerberus,
        coinColorRes = R.color.colorCerberus,
    )

    val FLIX = Token(
        denom = "uflix",
        name = "Omniflix Staking Coin",
        symbol = "FLIX",
        coinIconRes = R.drawable.token_omniflix,
        coinColorRes = R.color.colorOmniflix,
    )

    // TEST TOKENS
    val MUON = Token(
        denom = "uatom",
        name = "Stargate Staking Coin",
        symbol = "MUON",
        coinIconRes = R.drawable.atom_ic,
        coinColorRes = R.color.colorAtom,
    )

    val BIF = Token(
        denom = "uiris",
        name = "Bifrost Staking Coin",
        symbol = "BIF",
        coinIconRes = R.drawable.iris_toket_img,
        coinColorRes = R.color.colorIris,
    )
}