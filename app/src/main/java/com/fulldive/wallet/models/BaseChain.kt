package com.fulldive.wallet.models

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.fulldive.wallet.components.path.*
import com.fulldive.wallet.extensions.unsafeLazy
import com.fulldive.wallet.interactors.secret.WalletUtils
import com.fulldive.wallet.models.local.ApiHost
import wannabit.io.cosmostaion.R
import wannabit.io.cosmostaion.base.BaseConstant
import java.math.BigDecimal

sealed class BaseChain constructor(
    val chainName: String,
    val aliases: Array<String> = emptyArray(),
    val chainAddressPrefix: String,
    @DrawableRes val chainIcon: Int,
    @StringRes val chainTitle: Int,
    @StringRes val chainAlterTitle: Int,
    val mainDenom: String,
    val fullNameCoin: String,
    val divideDecimal: Int = DEFAULT_DIVIDE_DECIMAL,
    val displayDecimal: Int = DEFAULT_DISPLAY_DECIMAL,
    val ticker: String = "",
    @ColorRes val chainColor: Int,
    @DrawableRes val coinIcon: Int,
    @DrawableRes val mnemonicBackground: Int,
    @StringRes val symbolTitle: Int,
    @ColorRes val chainBackground: Int,
    @ColorRes val chainTabColor: Int,
    @ColorRes val floatButtonColor: Int = R.color.colorWhite,
    @ColorRes val floatButtonBackground: Int = chainColor,
    val historyApiUrl: String = "",
    val grpcApiHost: ApiHost = ApiHost.EMPTY,
    val blockTime: BigDecimal = BigDecimal.ZERO,
    val pathProvider: IPathProvider = PathProvider.DEFAULT,
    val isSupported: Boolean = true,
    val isTestNet: Boolean = false,
    val isGRPC: Boolean = true,
    val sortValue: Int = 0,
) {

    object COSMOS_MAIN : BaseChain(
        chainName = "cosmoshub-mainnet",
        aliases = arrayOf("cosmoshub-1", "cosmoshub-2", "cosmoshub-3", "cosmoshub-4"),
        chainAddressPrefix = "cosmos",
        chainIcon = R.drawable.cosmos_wh_main,
        chainTitle = R.string.str_cosmos_hub,
        chainAlterTitle = R.string.str_cosmos,
        mainDenom = "uatom",
        fullNameCoin = "Cosmos Staking Coin",
        ticker = "atom",
        chainColor = R.color.colorAtom,
        coinIcon = R.drawable.atom_ic,
        mnemonicBackground = R.drawable.box_round_atom,
        symbolTitle = R.string.str_atom_c,
        chainBackground = R.color.colorTransBgCosmos,
        chainTabColor = R.color.color_tab_myvalidator,
        blockTime = BigDecimal("7.6597"),
        historyApiUrl = "https://api.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-cosmos-app-and.cosmostation.io"),
        sortValue = 20
    )

    object IMVERSED_MAIN : BaseChain(
        chainName = "imversed-canary",
        chainAddressPrefix = "imv",
        chainIcon = R.drawable.imversed,
        chainTitle = R.string.str_imversed_canary_net,
        chainAlterTitle = R.string.str_imversed_canary,
        mainDenom = "aimv",
        divideDecimal = 18,
        displayDecimal = 18,
        fullNameCoin = "Imversed Staking Coin",
        chainColor = R.color.colorImversed,
        coinIcon = R.drawable.imversed_token_img,
        mnemonicBackground = R.drawable.box_round_imversed,
        symbolTitle = R.string.str_imversed_c,
        chainBackground = R.color.colorTransBgImversed,
        chainTabColor = R.color.color_tab_myvalidator_imversed,
        grpcApiHost = ApiHost.from("qc.imversed.com"),
        pathProvider = PathProvider(60),
        sortValue = 10
    )

    object IRIS_MAIN : BaseChain(
        chainName = "irishub-mainnet",
        aliases = arrayOf("irishub", "irishub-1"),
        chainAddressPrefix = "iaa",
        chainIcon = R.drawable.iris_wh,
        chainTitle = R.string.str_iris_net,
        chainAlterTitle = R.string.str_iris,
        mainDenom = "uiris",
        fullNameCoin = "Iris Staking Coin",
        ticker = "iris",
        chainColor = R.color.colorIris,
        coinIcon = R.drawable.iris_toket_img,
        mnemonicBackground = R.drawable.box_round_iris,
        symbolTitle = R.string.str_iris_c,
        chainBackground = R.color.colorTransBgIris,
        chainTabColor = R.color.color_tab_myvalidator_iris,
        blockTime = BigDecimal("6.7884"),
        historyApiUrl = "https://api-iris.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-iris-app.cosmostation.io")
    )

    object IOV_MAIN : BaseChain(
        chainName = "iov-mainnet",
        aliases = arrayOf("iov-mainnet-2"),
        chainAddressPrefix = "star",
        chainIcon = R.drawable.chain_starname,
        chainTitle = R.string.str_iov_net,
        chainAlterTitle = R.string.str_iov,
        mainDenom = "uiov",
        fullNameCoin = "Starname Staking Coin",
        ticker = "iov",
        chainColor = R.color.colorIov,
        coinIcon = R.drawable.iov_token_img,
        mnemonicBackground = R.drawable.box_round_iov,
        symbolTitle = R.string.str_iov_c,
        chainBackground = R.color.colorTransBgStarname,
        chainTabColor = R.color.color_tab_myvalidator_iov,
        blockTime = BigDecimal("6.0124"),
        historyApiUrl = "https://api-iov.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-iov-app.cosmostation.io"),
        pathProvider = PathProvider(234)
    )

    object BNB_MAIN : BaseChain(
        chainName = "binance-mainnet",
        aliases = arrayOf("Binance-Chain-Tigris"),
        chainAddressPrefix = "bnb",
        chainIcon = R.drawable.binance_ch_img,
        chainTitle = R.string.str_binance_net,
        chainAlterTitle = R.string.str_binance,
        mainDenom = "BNB",
        fullNameCoin = "Binance Chain Native Coin",
        divideDecimal = 0,
        displayDecimal = 8,
        ticker = "bnb",
        chainColor = R.color.colorBnb,
        coinIcon = R.drawable.bnb_token_img,
        mnemonicBackground = R.drawable.box_round_bnb,
        symbolTitle = R.string.str_bnb_c,
        chainBackground = R.color.colorTransBgBinance,
        chainTabColor = R.color.color_tab_myvalidator,
        blockTime = BigDecimal("0.4124"),
        pathProvider = PathProvider(714),
        isGRPC = false
    )

    object KAVA_MAIN : BaseChain(
        chainName = "kava-mainnet",
        aliases = arrayOf("kava-1", "kava-2", "kava-3", "kava-4", "kava-5", "kava-6"),
        chainAddressPrefix = "kava",
        chainIcon = R.drawable.kava_img,
        chainTitle = R.string.str_kava_net,
        chainAlterTitle = R.string.str_kava,
        mainDenom = "ukava",
        fullNameCoin = "Kava Staking Coin",
        ticker = "kava",
        chainColor = R.color.colorKava,
        coinIcon = R.drawable.kava_token_img,
        mnemonicBackground = R.drawable.box_round_kava,
        symbolTitle = R.string.str_kava_c,
        chainBackground = R.color.colorTransBgKava,
        chainTabColor = R.color.color_tab_myvalidator_kava,
        blockTime = BigDecimal("6.7262"),
        historyApiUrl = "https://api-kava.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-kava-app.cosmostation.io"),
        pathProvider = MultiPathProvider(459, mapOf(0 to 118))
    )

    object BAND_MAIN : BaseChain(
        chainName = "band-mainnet",
        aliases = arrayOf("band-wenchang-mainnet", "band-guanyu-mainnet"),
        chainAddressPrefix = "band",
        chainIcon = R.drawable.band_chain_img,
        chainTitle = R.string.str_band_chain,
        chainAlterTitle = R.string.str_band,
        mainDenom = "uband",
        fullNameCoin = "Band Staking Coin",
        ticker = "band",
        chainColor = R.color.colorBand,
        coinIcon = R.drawable.band_token_img,
        mnemonicBackground = R.drawable.box_round_band,
        symbolTitle = R.string.str_band_c,
        chainBackground = R.color.colorTransBgBand,
        chainTabColor = R.color.color_tab_myvalidator_band,
        blockTime = BigDecimal("3.0236"),
        historyApiUrl = "https://api-band.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-band-app.cosmostation.io"),
        pathProvider = PathProvider(494)
    )

    object CERTIK_MAIN : BaseChain(
        chainName = "shentu-mainnet",
        aliases = arrayOf("shentu-1", "shentu-2"),
        chainAddressPrefix = "certik",
        chainIcon = R.drawable.certik_chain_img,
        chainTitle = R.string.str_certik_chain,
        chainAlterTitle = R.string.str_certik_main,
        mainDenom = "uctk",
        fullNameCoin = "Certik Staking Coin",
        ticker = "ctk",
        chainColor = R.color.colorCertik,
        coinIcon = R.drawable.certik_token_img,
        mnemonicBackground = R.drawable.box_round_certik,
        symbolTitle = R.string.str_ctk_c,
        chainBackground = R.color.colorTransBgCertik,
        chainTabColor = R.color.color_tab_myvalidator_certik,
        blockTime = BigDecimal("5.9740"),
        historyApiUrl = "https://api-certik.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-certik-app.cosmostation.io")
    )

    object SECRET_MAIN : BaseChain(
        chainName = "secret-mainnet",
        aliases = arrayOf("secret-2"),
        chainAddressPrefix = "secret",
        chainIcon = R.drawable.chainsecret,
        chainTitle = R.string.str_secret_chain,
        chainAlterTitle = R.string.str_secret_main,
        mainDenom = "uscrt",
        fullNameCoin = "Secret Native Coin",
        ticker = "scrt",
        chainColor = R.color.colorSecret,
        coinIcon = R.drawable.tokensecret,
        mnemonicBackground = R.drawable.box_round_secret,
        symbolTitle = R.string.str_scrt_c,
        chainBackground = R.color.colorTransBgSecret,
        chainTabColor = R.color.color_tab_myvalidator_secret,
        floatButtonBackground = R.color.colorSecret2,
        blockTime = BigDecimal("6.0408"),
        historyApiUrl = "https://api-secret.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-secret.cosmostation.io"),
        pathProvider = MultiPathProvider(529, mapOf(0 to 118))
    )

    object AKASH_MAIN : BaseChain(
        chainName = "akashnet-mainnet",
        aliases = arrayOf("akashnet-1", "akashnet-2"),
        chainAddressPrefix = "akash",
        chainIcon = R.drawable.akash_chain_img,
        chainTitle = R.string.str_akash_chain,
        chainAlterTitle = R.string.str_akash_main,
        mainDenom = "uakt",
        fullNameCoin = "Akash Staking Coin",
        ticker = "akt",
        chainColor = R.color.colorAkash,
        coinIcon = R.drawable.akash_token_img,
        mnemonicBackground = R.drawable.box_round_akash,
        symbolTitle = R.string.str_akt_c,
        chainBackground = R.color.colorTransBgAkash,
        chainTabColor = R.color.color_tab_myvalidator_akash,
        blockTime = BigDecimal("6.4526"),
        historyApiUrl = "https://api-akash.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-akash-app.cosmostation.io")
    )

    object OKEX_MAIN : BaseChain(
        chainName = "okexchain-mainnet",
        aliases = arrayOf("okexchain-66"),
        chainAddressPrefix = "0x",
        chainIcon = R.drawable.chain_okx,
        chainTitle = R.string.str_ok_net,
        chainAlterTitle = R.string.str_okex_main,
        mainDenom = "okt",
        fullNameCoin = "OKC Staking Coin",
        divideDecimal = 0,
        displayDecimal = 18,
        ticker = "okb",
        chainColor = R.color.colorOK,
        coinIcon = R.drawable.token_okx,
        mnemonicBackground = R.drawable.box_round_okex,
        symbolTitle = R.string.str_ok_c,
        chainBackground = R.color.colorTransBgOkex,
        chainTabColor = R.color.color_tab_myvalidator_ok,
        blockTime = BigDecimal("4.0286"),
        pathProvider = HintedMultiPathProvider(
            60,
            mapOf(0 to 996, 1 to 996),
            "Ethereum Type",
            mapOf(0 to "Tendermint Type", 1 to "Ethermint Type")
        ),
        isGRPC = false
    )

    object PERSIS_MAIN : BaseChain(
        chainName = "persistence-mainnet",
        chainAddressPrefix = "persistence",
        chainIcon = R.drawable.chainpersistence,
        chainTitle = R.string.str_persis_net,
        chainAlterTitle = R.string.str_persis_main,
        mainDenom = "uxprt",
        fullNameCoin = "Persistence Staking Coin",
        ticker = "xprt",
        chainColor = R.color.colorPersis,
        coinIcon = R.drawable.tokenpersistence,
        mnemonicBackground = R.drawable.box_round_persis,
        symbolTitle = R.string.str_xprt_c,
        chainBackground = R.color.colorTransBgPersis,
        chainTabColor = R.color.color_tab_myvalidator_persis,
        floatButtonColor = R.color.colorPersis,
        floatButtonBackground = R.color.colorBlack,
        blockTime = BigDecimal("5.7982"),
        historyApiUrl = "https://api-persistence.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-persistence-app.cosmostation.io"),
        pathProvider = PathProvider(750)
    )

    object SENTINEL_MAIN : BaseChain(
        chainName = "sentinel-mainnet",
        chainAddressPrefix = "sent",
        chainIcon = R.drawable.chainsentinel,
        chainTitle = R.string.str_sentinel_net,
        chainAlterTitle = R.string.str_sentinel_main,
        mainDenom = "udvpn",
        fullNameCoin = "Sentinel Native Coin",
        ticker = "dvpn",
        chainColor = R.color.colorSentinel,
        coinIcon = R.drawable.tokensentinel,
        mnemonicBackground = R.drawable.box_round_sentinel,
        symbolTitle = R.string.str_dvpn_c,
        chainBackground = R.color.colorTransBgSentinel,
        chainTabColor = R.color.color_tab_myvalidator_sentinel,
        floatButtonBackground = R.color.colorSentinel3,
        blockTime = BigDecimal("6.3113"),
        historyApiUrl = "https://api-sentinel.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-sentinel-app.cosmostation.io")
    )

    object FETCHAI_MAIN : BaseChain(
        chainName = "fetchai-mainnet",
        chainAddressPrefix = "fetch",
        chainIcon = R.drawable.chainfetchai,
        chainTitle = R.string.str_fetch_net,
        chainAlterTitle = R.string.str_fetch_main,
        mainDenom = "afet",
        fullNameCoin = "Fetch,ai Staking Coin",
        divideDecimal = 18,
        displayDecimal = 18,
        ticker = "",  // "fet"
        chainColor = R.color.colorFetch,
        coinIcon = R.drawable.tokenfetchai,
        mnemonicBackground = R.drawable.box_round_fetch,
        symbolTitle = R.string.str_fet_c,
        chainBackground = R.color.colorTransBgFetch,
        chainTabColor = R.color.color_tab_myvalidator_fetch,
        blockTime = BigDecimal("6.0678"),
        historyApiUrl = "https://api-fetchai.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-fetchai-app.cosmostation.io"),
        pathProvider = FetchaiPathProvider
    )

    object CRYPTO_MAIN : BaseChain(
        chainName = "crytoorg-mainnet",
        chainAddressPrefix = "cro",
        chainIcon = R.drawable.chaincrypto,
        chainTitle = R.string.str_crypto_net,
        chainAlterTitle = R.string.str_crypto_main,
        mainDenom = "basecro",
        fullNameCoin = "Cronos",
        divideDecimal = 8,
        displayDecimal = 8,
        ticker = "cro",
        chainColor = R.color.colorCryto,
        coinIcon = R.drawable.tokencrypto,
        mnemonicBackground = R.drawable.box_round_cryto,
        symbolTitle = R.string.str_cro_c,
        chainBackground = R.color.colorTransBgCryto,
        chainTabColor = R.color.color_tab_myvalidator_cryto,
        floatButtonBackground = R.color.colorCryto2,
        blockTime = BigDecimal("6.1939"),
        historyApiUrl = "https://api-cryptocom.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-cryptocom-app.cosmostation.io"),
        pathProvider = PathProvider(394)
    )

    object SIF_MAIN : BaseChain(
        chainName = "sif-mainnet",
        chainAddressPrefix = "sif",
        chainIcon = R.drawable.chainsifchain,
        chainTitle = R.string.str_sif_net,
        chainAlterTitle = R.string.str_sif_main,
        mainDenom = "rowan",
        fullNameCoin = "Sif Staking Coin",
        divideDecimal = 18,
        displayDecimal = 18,
        ticker = "rowan",
        chainColor = R.color.colorSif,
        coinIcon = R.drawable.tokensifchain,
        mnemonicBackground = R.drawable.box_round_sif,
        symbolTitle = R.string.str_sif_c,
        chainBackground = R.color.colorTransBgSif,
        chainTabColor = R.color.color_tab_myvalidator_sif,
        blockTime = BigDecimal("5.7246"),
        historyApiUrl = "https://api-sifchain.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-sifchain-app.cosmostation.io")
    )

    object KI_MAIN : BaseChain(
        chainName = "ki-mainnet",
        chainAddressPrefix = "ki",
        chainIcon = R.drawable.chain_kifoundation,
        chainTitle = R.string.str_ki_net,
        chainAlterTitle = R.string.str_ki_main,
        mainDenom = "uxki",
        fullNameCoin = "KiChain Staking Coin",
        ticker = "",  // "ki"
        chainColor = R.color.colorKi,
        coinIcon = R.drawable.token_kifoundation,
        mnemonicBackground = R.drawable.box_round_ki,
        symbolTitle = R.string.str_ki_c,
        chainBackground = R.color.colorTransBgKi,
        chainTabColor = R.color.color_tab_myvalidator_ki,
        blockTime = BigDecimal("5.7571"),
        historyApiUrl = "https://api-kichain.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-kichain-app.cosmostation.io")
    )

    object OSMOSIS_MAIN : BaseChain(
        chainName = "osmosis-mainnet",
        chainAddressPrefix = "osmo",
        chainIcon = R.drawable.chain_osmosis,
        chainTitle = R.string.str_osmosis_net,
        chainAlterTitle = R.string.str_osmosis_main,
        mainDenom = "uosmo",
        fullNameCoin = "Osmosis Staking Coin",
        ticker = "osmo",
        chainColor = R.color.colorOsmosis,
        coinIcon = R.drawable.token_osmosis,
        mnemonicBackground = R.drawable.box_round_osmosis,
        symbolTitle = R.string.str_osmosis_c,
        chainBackground = R.color.colorTransBgOsmosis,
        chainTabColor = R.color.color_tab_myvalidator_osmosis,
        blockTime = BigDecimal("6.5324"),
        historyApiUrl = "https://api-osmosis.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-osmosis-app-and.cosmostation.io")
    )

    object MEDI_MAIN : BaseChain(
        chainName = "medibloc-mainnet",
        chainAddressPrefix = "panacea",
        chainIcon = R.drawable.chainmedibloc,
        chainTitle = R.string.str_medi_net,
        chainAlterTitle = R.string.str_medi_main,
        mainDenom = "umed",
        fullNameCoin = "Medibloc Staking Coin",
        ticker = "",  // "med"
        chainColor = R.color.colorMedi,
        coinIcon = R.drawable.tokenmedibloc,
        mnemonicBackground = R.drawable.box_round_medi,
        symbolTitle = R.string.str_medi_c,
        chainBackground = R.color.colorTransBgMedi,
        chainTabColor = R.color.color_tab_myvalidator_med,
        floatButtonColor = R.color.colorMedi,
        floatButtonBackground = R.color.colorWhite,
        blockTime = BigDecimal("5.7849"),
        historyApiUrl = "https://api-medibloc.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-medibloc-app.cosmostation.io"),
        pathProvider = PathProvider(371)
    )

    object EMONEY_MAIN : BaseChain(
        chainName = "emoney-mainnet",
        chainAddressPrefix = "emoney",
        chainIcon = R.drawable.chain_emoney,
        chainTitle = R.string.str_emoney_net,
        chainAlterTitle = R.string.str_emoney_main,
        mainDenom = "ungm",
        fullNameCoin = "E-Money Staking Coin",
        ticker = "",  // "ngm"
        chainColor = R.color.colorEmoney,
        coinIcon = R.drawable.token_emoney,
        mnemonicBackground = R.drawable.box_round_emoney,
        symbolTitle = R.string.str_ngm_c,
        chainBackground = R.color.colorTransBgEmoney,
        chainTabColor = R.color.color_tab_myvalidator_emoney,
        blockTime = BigDecimal("24.8486"),
        historyApiUrl = "https://api-emoney.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-emoney-app.cosmostation.io")
    )

    object RIZON_MAIN : BaseChain(
        chainName = "rizon-mainnet",
        chainAddressPrefix = "rizon",
        chainIcon = R.drawable.chain_rizon,
        chainTitle = R.string.str_rizon_net,
        chainAlterTitle = R.string.str_rizon_main,
        mainDenom = "uatolo",
        fullNameCoin = "Rizon Staking Coin",
        ticker = "atolo",
        chainColor = R.color.colorRizon,
        coinIcon = R.drawable.token_rizon,
        mnemonicBackground = R.drawable.box_round_rizon,
        symbolTitle = R.string.str_rizon_c,
        chainBackground = R.color.colorTransBgRizon,
        chainTabColor = R.color.color_tab_myvalidator_rizon,
        blockTime = BigDecimal("5.8850"),
        historyApiUrl = "https://api-rizon.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-rizon-app.cosmostation.io")
    )

    object JUNO_MAIN : BaseChain(
        chainName = "juno-mainnet",
        chainAddressPrefix = "juno",
        chainIcon = R.drawable.chain_juno,
        chainTitle = R.string.str_juno_net,
        chainAlterTitle = R.string.str_juno_main,
        mainDenom = "ujuno",
        fullNameCoin = "Juno Staking Coin",
        ticker = "",  // "ujuno"
        chainColor = R.color.colorJuno,
        coinIcon = R.drawable.token_juno,
        mnemonicBackground = R.drawable.box_round_juno,
        symbolTitle = R.string.str_juno_c,
        chainBackground = R.color.colorTransBgJuno,
        chainTabColor = R.color.color_tab_myvalidator_juno,
        blockTime = BigDecimal("6.3104"),
        historyApiUrl = "https://api-juno.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-juno-app.cosmostation.io")
    )

    object REGEN_MAIN : BaseChain(
        chainName = "regen-mainnet",
        chainAddressPrefix = "regen",
        chainIcon = R.drawable.chain_regen,
        chainTitle = R.string.str_regen_net,
        chainAlterTitle = R.string.str_regen_main,
        mainDenom = "uregen",
        fullNameCoin = "Regen Staking Coin",
        ticker = "regen",
        chainColor = R.color.colorRegen,
        coinIcon = R.drawable.token_regen,
        mnemonicBackground = R.drawable.box_round_regen,
        symbolTitle = R.string.str_regen_c,
        chainBackground = R.color.colorTransBgRegen,
        chainTabColor = R.color.color_tab_myvalidator_regen,
        blockTime = BigDecimal("6.2491"),
        historyApiUrl = "https://api-regen.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-regen-app.cosmostation.io")
    )

    object BITCANNA_MAIN : BaseChain(
        chainName = "bitcanna-mainnet",
        chainAddressPrefix = "bcna",
        chainIcon = R.drawable.chain_bitcanna,
        chainTitle = R.string.str_bitcanna_net,
        chainAlterTitle = R.string.str_bitcanna_main,
        mainDenom = "ubcna",
        fullNameCoin = "Bitcanna Staking Coin",
        ticker = "",  // "bcna"
        chainColor = R.color.colorBitcanna,
        coinIcon = R.drawable.token_bitcanna,
        mnemonicBackground = R.drawable.box_round_bitcanna,
        symbolTitle = R.string.str_bitcanna_c,
        chainBackground = R.color.colorTransBgBitcanna,
        chainTabColor = R.color.color_tab_myvalidator_bitcanna,
        blockTime = BigDecimal("6.0256"),
        historyApiUrl = "https://api-bitcanna.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-bitcanna-app.cosmostation.io")
    )

    object ALTHEA_MAIN : BaseChain(
        chainName = "althea-mainnet",
        chainAddressPrefix = "althea",
        chainIcon = R.drawable.chain_althea,
        chainTitle = R.string.str_althea_net,
        chainAlterTitle = R.string.str_althea_main,
        mainDenom = "ualtg",
        fullNameCoin = "Althea Stacking Coin",
        chainColor = R.color.colorAlthea,
        coinIcon = R.drawable.token_althea,
        mnemonicBackground = R.drawable.box_round_althea,
        symbolTitle = R.string.str_althea_c,
        chainBackground = R.color.colorTransBgAlthea,
        chainTabColor = R.color.color_tab_myvalidator_althea,
        historyApiUrl = "https://api-althea.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-althea-app.cosmostation.io")
    )

    object STARGAZE_MAIN : BaseChain(
        chainName = "stargaze-mainnet",
        chainAddressPrefix = "stars",
        chainIcon = R.drawable.chain_stargaze,
        chainTitle = R.string.str_stargaze_net,
        chainAlterTitle = R.string.str_stargaze_main,
        mainDenom = "ustars",
        fullNameCoin = "Stargaze Staking Coin",
        ticker = "",  // "stars"
        chainColor = R.color.colorStargaze,
        coinIcon = R.drawable.token_stargaze,
        mnemonicBackground = R.drawable.box_round_stargaze,
        symbolTitle = R.string.str_stargaze_c,
        chainBackground = R.color.colorTransBgStargaze,
        chainTabColor = R.color.color_tab_myvalidator_stargaze,
        blockTime = BigDecimal("5.8129"),
        historyApiUrl = "https://api-stargaze.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-stargaze-app.cosmostation.io")
    )

    object GRABRIDGE_MAIN : BaseChain(
        chainName = "GravityBridge-mainnet",
        chainAddressPrefix = "gravity",
        chainIcon = R.drawable.chain_gravitybridge,
        chainTitle = R.string.str_grabridge_net,
        chainAlterTitle = R.string.str_grabridge_main,
        mainDenom = "ugraviton",
        fullNameCoin = "G-Bridge Staking Coin",
        chainColor = R.color.colorGraBridge,
        coinIcon = R.drawable.token_gravitybridge,
        mnemonicBackground = R.drawable.box_round_grabridge,
        symbolTitle = R.string.str_grabridge_c,
        chainBackground = R.color.colorTransBgGraBridge,
        chainTabColor = R.color.color_tab_myvalidator_grabridge,
        blockTime = BigDecimal("6.4500"),
        historyApiUrl = "https://api-gravity-bridge.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-gravity-bridge-app.cosmostation.io")
    )

    object COMDEX_MAIN : BaseChain(
        chainName = "comdex-mainnet",
        chainAddressPrefix = "comdex",
        chainIcon = R.drawable.chain_comdex,
        chainTitle = R.string.str_comdex_net,
        chainAlterTitle = R.string.str_comdex_main,
        mainDenom = "ucmdx",
        fullNameCoin = "Comdex Staking Coin",
        chainColor = R.color.colorComdex,
        coinIcon = R.drawable.token_comdex,
        mnemonicBackground = R.drawable.box_round_comdex,
        symbolTitle = R.string.str_comdex_c,
        chainBackground = R.color.colorTransBgComdex,
        chainTabColor = R.color.color_tab_myvalidator_comdex,
        floatButtonColor = R.color.colorComdex,
        floatButtonBackground = R.color.colorTransBgComdex,
        blockTime = BigDecimal("6.1746"),
        historyApiUrl = "https://api-comdex.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-comdex-app.cosmostation.io")
    )

    object INJ_MAIN : BaseChain(
        chainName = "injective-mainnet",
        chainAddressPrefix = "inj",
        chainIcon = R.drawable.chain_injective,
        chainTitle = R.string.str_inj_net,
        chainAlterTitle = R.string.str_inj_main,
        mainDenom = "inj",
        fullNameCoin = "Injective Staking Coin",
        divideDecimal = 18,
        displayDecimal = 18,
        chainColor = R.color.colorInj,
        coinIcon = R.drawable.token_injective,
        mnemonicBackground = R.drawable.box_round_inj,
        symbolTitle = R.string.str_inj_c,
        chainBackground = R.color.colorTransBgInj,
        chainTabColor = R.color.color_tab_myvalidator_inj,
        floatButtonColor = R.color.colorBlack,
        blockTime = BigDecimal("2.4865"),
        historyApiUrl = "https://api-inj.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-inj-app.cosmostation.io"),
        pathProvider = PathProvider(60)
    )

    object BITSONG_MAIN : BaseChain(
        chainName = "bitsong-mainnet",
        chainAddressPrefix = "bitsong",
        chainIcon = R.drawable.chain_bitsong,
        chainTitle = R.string.str_bitsong_net,
        chainAlterTitle = R.string.str_bitsong_main,
        mainDenom = "ubtsg",
        fullNameCoin = "Bitsong Staking Coin",
        chainColor = R.color.colorBitsong,
        coinIcon = R.drawable.token_bitsong,
        mnemonicBackground = R.drawable.box_round_bitsong,
        symbolTitle = R.string.str_bitsong_c,
        chainBackground = R.color.colorTransBgBitsong,
        chainTabColor = R.color.color_tab_myvalidator_bitsong,
        blockTime = BigDecimal("5.9040"),
        historyApiUrl = "https://api-bitsong.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-bitsong-app.cosmostation.io"),
        pathProvider = PathProvider(639)
    )

    object DESMOS_MAIN : BaseChain(
        chainName = "desmos-mainnet",
        chainAddressPrefix = "desmos",
        chainIcon = R.drawable.chain_desmos,
        chainTitle = R.string.str_desmos_net,
        chainAlterTitle = R.string.str_desmos_main,
        mainDenom = "udsm",
        fullNameCoin = "Desmos Staking Coin",
        chainColor = R.color.colorDesmos,
        coinIcon = R.drawable.token_desmos,
        mnemonicBackground = R.drawable.box_round_desmos,
        symbolTitle = R.string.str_desmos_c,
        chainBackground = R.color.colorTransBgDesmos,
        chainTabColor = R.color.color_tab_myvalidator_desmos,
        blockTime = BigDecimal("6.1605"),
        historyApiUrl = "https://api-desmos.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-desmos-app.cosmostation.io"),
        pathProvider = PathProvider(852)
    )

    object LUM_MAIN : BaseChain(
        chainName = "lum-mainnet",
        chainAddressPrefix = "lum",
        chainIcon = R.drawable.chain_lumnetwork,
        chainTitle = R.string.str_lum_net,
        chainAlterTitle = R.string.str_lum_main,
        mainDenom = "ulum",
        fullNameCoin = "Lum Staking Coin",
        chainColor = R.color.colorLum,
        coinIcon = R.drawable.token_lum,
        mnemonicBackground = R.drawable.box_round_lum,
        symbolTitle = R.string.str_lum_c,
        chainBackground = R.color.colorTransBgLum,
        chainTabColor = R.color.color_tab_myvalidator_lum,
        blockTime = BigDecimal("5.7210"),
        historyApiUrl = "https://api-lum.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-lum-app.cosmostation.io"),
        pathProvider = MultiPathProvider(880, mapOf(0 to 118))
    )

    object CHIHUAHUA_MAIN : BaseChain(
        chainName = "chihuahua-mainnet",
        chainAddressPrefix = "chihuahua",
        chainIcon = R.drawable.chain_chihuahua,
        chainTitle = R.string.str_chihuahua_net,
        chainAlterTitle = R.string.str_chihuahua_main,
        mainDenom = "uhuahua",
        fullNameCoin = "Chihuahua Staking Coin",
        chainColor = R.color.colorChihuahua,
        coinIcon = R.drawable.token_huahua,
        mnemonicBackground = R.drawable.box_round_chihuahua,
        symbolTitle = R.string.str_chihuahua_c,
        chainBackground = R.color.colorTransBgChihuahua,
        chainTabColor = R.color.color_tab_myvalidator_chihuahua,
        blockTime = BigDecimal("5.8172"),
        historyApiUrl = "https://api-chihuahua.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-chihuahua-app.cosmostation.io")
    )

    object AXELAR_MAIN : BaseChain(
        chainName = "axelar-mainnet",
        chainAddressPrefix = "axelar",
        chainIcon = R.drawable.chain_axelar,
        chainTitle = R.string.str_axelar_net,
        chainAlterTitle = R.string.str_axelar_main,
        mainDenom = "uaxl",
        fullNameCoin = "Axelar Staking Coin",
        chainColor = R.color.colorAxelar,
        coinIcon = R.drawable.token_axelar,
        mnemonicBackground = R.drawable.box_round_axelar,
        symbolTitle = R.string.str_axl_c,
        chainBackground = R.color.colorTransBgAxelar,
        chainTabColor = R.color.color_tab_myvalidator_axelar,
        floatButtonColor = R.color.colorBlack,
        blockTime = BigDecimal("5.5596"),
        historyApiUrl = "https://api-axelar.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-axelar-app.cosmostation.io")
    )

    object KONSTELL_MAIN : BaseChain(
        chainName = "konstellation-mainnet",
        chainAddressPrefix = "darc",
        chainIcon = R.drawable.chain_konstellation,
        chainTitle = R.string.str_konstellation_net,
        chainAlterTitle = R.string.str_konstellation_main,
        mainDenom = "udarc",
        fullNameCoin = "Konstellation Staking Coin",
        chainColor = R.color.colorKonstellation,
        coinIcon = R.drawable.token_konstellation,
        mnemonicBackground = R.drawable.box_round_konstellattion,
        symbolTitle = R.string.str_konstellation_c,
        chainBackground = R.color.colorTransBgKonstellation,
        chainTabColor = R.color.color_tab_myvalidator_konstellation,
        floatButtonColor = R.color.colorKonstellation,
        floatButtonBackground = R.color.colorKonstellation3,
        blockTime = BigDecimal("5.376"),
        historyApiUrl = "https://api-konstellation.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-konstellation-app.cosmostation.io")
    )

    object UMEE_MAIN : BaseChain(
        chainName = "umee-mainnet",
        chainAddressPrefix = "umee",
        chainIcon = R.drawable.chain_umee,
        chainTitle = R.string.str_umee_net,
        chainAlterTitle = R.string.str_umee_main,
        mainDenom = "uumee",
        fullNameCoin = "Umee Staking Coin",
        chainColor = R.color.colorUmee,
        coinIcon = R.drawable.token_umee,
        mnemonicBackground = R.drawable.box_round_umee,
        symbolTitle = R.string.str_umee_c,
        chainBackground = R.color.colorTransBgUmee,
        chainTabColor = R.color.color_tab_myvalidator_umee,
        blockTime = BigDecimal("5.658"),
        historyApiUrl = "https://api-umee.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-umee-app.cosmostation.io")
    )

    object EVMOS_MAIN : BaseChain(
        chainName = "evmos-mainnet",
        chainAddressPrefix = "evmos",
        chainIcon = R.drawable.chain_evmos,
        chainTitle = R.string.str_evmos_net,
        chainAlterTitle = R.string.str_evmos_main,
        mainDenom = "aevmos",
        fullNameCoin = "Evmos Staking Coin",
        divideDecimal = 18,
        displayDecimal = 18,
        chainColor = R.color.colorEvmos,
        coinIcon = R.drawable.token_evmos,
        mnemonicBackground = R.drawable.box_round_evmos,
        symbolTitle = R.string.str_evmos_c,
        chainBackground = R.color.colorTransBgEvmos,
        chainTabColor = R.color.color_tab_myvalidator_evmos,
        floatButtonColor = R.color.colorEvmos,
        floatButtonBackground = R.color.colorBlack,
        blockTime = BigDecimal("5.824"),
        historyApiUrl = "https://api-evmos.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-evmos-app.cosmostation.io"),
        pathProvider = PathProvider(60)
    )

    object CUDOS_MAIN : BaseChain(
        chainName = "cudos-mainnet",
        chainAddressPrefix = "cudos",
        chainIcon = R.drawable.chain_cudos,
        chainTitle = R.string.str_cudos_net,
        chainAlterTitle = R.string.str_cudos_main,
        mainDenom = "acudos",
        fullNameCoin = "Cudos Staking Coin",
        divideDecimal = 18,
        displayDecimal = 18,
        chainColor = R.color.colorCudos,
        coinIcon = R.drawable.token_cudos,
        mnemonicBackground = R.drawable.box_round_cudos,
        symbolTitle = R.string.str_cudos_c,
        chainBackground = R.color.colorTransBgCudos,
        chainTabColor = R.color.color_tab_myvalidator_cudos,
        historyApiUrl = "https://api-cudos-testnet.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-cudos-testnet.cosmostation.io"),
        isSupported = false,
        isTestNet = true
    )

    object PROVENANCE_MAIN : BaseChain(
        chainName = "provenance-mainnet",
        chainAddressPrefix = "pb",
        chainIcon = R.drawable.chain_provenance,
        chainTitle = R.string.str_provenance_net,
        chainAlterTitle = R.string.str_provenance_main,
        mainDenom = "nhash",
        fullNameCoin = "Provenance Staking Coin",
        divideDecimal = 9,
        displayDecimal = 9,
        chainColor = R.color.colorProvenance,
        coinIcon = R.drawable.token_hash,
        mnemonicBackground = R.drawable.box_round_provenance,
        symbolTitle = R.string.str_provenance_c,
        chainBackground = R.color.colorTransBgProvenance,
        chainTabColor = R.color.color_tab_myvalidator_provenance,
        blockTime = BigDecimal("6.3061"),
        historyApiUrl = "https://api-provenance.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-provenance-app.cosmostation.io"),
        pathProvider = PathProvider(505)
    )

    object CERBERUS_MAIN : BaseChain(
        chainName = "cerberus-mainnet",
        chainAddressPrefix = "cerberus",
        chainIcon = R.drawable.chain_cerberus,
        chainTitle = R.string.str_cerberus_net,
        chainAlterTitle = R.string.str_cerberus_main,
        mainDenom = "ucrbrus",
        fullNameCoin = "Cerberus Staking Coin",
        chainColor = R.color.colorCerberus,
        coinIcon = R.drawable.token_cerberus,
        mnemonicBackground = R.drawable.box_round_cerberus,
        symbolTitle = R.string.str_cerberus_c,
        chainBackground = R.color.colorTransBgCerberus,
        chainTabColor = R.color.color_tab_myvalidator_cerberus,
        blockTime = BigDecimal("5.9666"),
        historyApiUrl = "https://api-cerberus.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-cerberus-app.cosmostation.io")
    )

    object OMNIFLIX_MAIN : BaseChain(
        chainName = "omniflix-mainnet",
        chainAddressPrefix = "omniflix",
        chainIcon = R.drawable.chain_omniflix,
        chainTitle = R.string.str_omniflix_net,
        chainAlterTitle = R.string.str_omniflix_main,
        mainDenom = "uflix",
        fullNameCoin = "Omniflix Staking Coin",
        chainColor = R.color.colorOmniflix,
        coinIcon = R.drawable.token_omniflix,
        mnemonicBackground = R.drawable.box_round_omniflix,
        symbolTitle = R.string.str_omniflix_c,
        chainBackground = R.color.colorTransBgOmniflix,
        chainTabColor = R.color.color_tab_myvalidator_omniflix,
        blockTime = BigDecimal("5.7970"),
        historyApiUrl = "https://api-omniflix.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-omniflix-app.cosmostation.io")
    )

    object COSMOS_TEST : BaseChain(
        chainName = "cosmos-testnet,",
        aliases = arrayOf("stargate-final"),
        chainAddressPrefix = "cosmos",
        chainIcon = R.drawable.chain_test_cosmos,
        chainTitle = R.string.str_cosmos_test_net,
        chainAlterTitle = R.string.str_cosmos_test,
        mainDenom = "uatom",
        fullNameCoin = "Stargate Staking Coin",
        chainColor = R.color.colorAtom,
        coinIcon = R.drawable.atom_ic,
        mnemonicBackground = R.drawable.box_round_atom,
        symbolTitle = R.string.str_muon_c,
        chainBackground = R.color.colorTransBgCosmos,
        chainTabColor = R.color.color_tab_myvalidator,
        blockTime = BigDecimal("7.6597"),
        historyApiUrl = "https://api-office.cosmostation.io/stargate-final/",
        grpcApiHost = ApiHost.from("lcd-office.cosmostation.io:10300"),
        isSupported = false,
        isTestNet = true
    )

    object IRIS_TEST : BaseChain(
        chainName = "iris-testnet",
        aliases = arrayOf("bifrost-2"),
        chainAddressPrefix = "iaa",
        chainIcon = R.drawable.chain_test_iris,
        chainTitle = R.string.str_iris_test_net,
        chainAlterTitle = R.string.str_iris_test,
        mainDenom = "uiris",
        fullNameCoin = "Bifrost Staking Coin",
        chainColor = R.color.colorIris,
        coinIcon = R.drawable.iris_toket_img,
        mnemonicBackground = R.drawable.box_round_darkgray,
        symbolTitle = R.string.str_bif_c,
        chainBackground = R.color.colorTransBgIris,
        chainTabColor = R.color.color_tab_myvalidator_iris,
        blockTime = BigDecimal("6.7884"),
        historyApiUrl = "https://api-office.cosmostation.io/bifrost/",
        grpcApiHost = ApiHost.from("lcd-office.cosmostation.io:9095"),
        isSupported = false,
        isTestNet = true
    )

    object OK_TEST : BaseChain(
        chainName = "okexchain-testnet",
        chainAddressPrefix = "0x",
        chainIcon = R.drawable.chain_okx,
        chainTitle = R.string.str_ok_test_net,
        chainAlterTitle = R.string.str_okex_test,
        mainDenom = "okt",
        fullNameCoin = "OKC Staking Coin",
        chainColor = R.color.colorOK,
        coinIcon = R.drawable.token_okx,
        mnemonicBackground = R.drawable.box_round_okex,
        symbolTitle = R.string.str_ok_c,
        chainBackground = R.color.colorTransBgOkex,
        chainTabColor = R.color.color_tab_myvalidator_ok,
        isSupported = false,
        isTestNet = true
    )

    object RIZON_TEST : BaseChain(
        chainName = "rizon-testnet2",
        chainAddressPrefix = "rizon",
        chainIcon = R.drawable.chain_rizon,
        chainTitle = R.string.str_rizon_test_net,
        chainAlterTitle = R.string.str_rizon_test,
        mainDenom = "uatolo",
        fullNameCoin = "Rizon Staking Coin",
        chainColor = R.color.colorRizon,
        coinIcon = R.drawable.token_rizon,
        mnemonicBackground = R.drawable.box_round_rizon,
        symbolTitle = R.string.str_rizon_c,
        chainBackground = R.color.colorTransBgRizon,
        chainTabColor = R.color.color_tab_myvalidator_rizon,
        historyApiUrl = "https://api-rizon-testnet.cosmostation.io/",
        isSupported = false,
        isTestNet = true
    )

    object ALTHEA_TEST : BaseChain(
        chainName = "althea-testnet",
        chainAddressPrefix = "althea",
        chainIcon = R.drawable.chain_althea,
        chainTitle = R.string.str_althea_test_net,
        chainAlterTitle = R.string.str_althea_test,
        mainDenom = "ualtg",
        fullNameCoin = "Althea Staking Coin",
        chainColor = R.color.colorAlthea,
        coinIcon = R.drawable.token_althea,
        mnemonicBackground = R.drawable.box_round_darkgray,
        symbolTitle = R.string.str_althea_c,
        chainBackground = R.color.colorTransBgAlthea,
        chainTabColor = R.color.color_tab_myvalidator_althea,
        historyApiUrl = "https://api-office.cosmostation.io/althea-testnet2v1/",
        grpcApiHost = ApiHost.from("lcd-office.cosmostation.io:20100"),
        isSupported = false,
        isTestNet = true
    )

    fun hasChainName(chainName: String): Boolean {
        return this.chainName == chainName || aliases.any { it == chainName }
    }

    companion object {
        const val DEFAULT_DIVIDE_DECIMAL = 6
        const val DEFAULT_DISPLAY_DECIMAL = 6

        val chains by unsafeLazy {
            BaseChain::class
                .sealedSubclasses
                .mapNotNull { it.objectInstance }
        }

        @JvmStatic
        fun getChain(chainName: String): BaseChain? {
            return chains.find { chain ->
                chain.hasChainName(chainName)
            }
        }

        fun getChainByDenom(denom: String): BaseChain? {
            return chains.find { chain ->
                chain.mainDenom == denom
            }
        }

        fun getChainsByAddress(address: String): List<BaseChain> {
            return when {
                address.startsWith("0x") && WalletUtils.isValidEthAddress(address) -> {
                    listOf(OKEX_MAIN)
                }
                WalletUtils.isValidBech32(address) -> {
                    return chains.filter { chain ->
                        address.startsWith(chain.chainAddressPrefix)
                    }
                }
                else -> {
                    emptyList()
                }
            }
        }

        fun isSupported(chainName: String): Boolean {
            return getChain(chainName)?.isSupported == true
        }

        fun getHtlcSendable(fromChain: BaseChain): List<BaseChain> {
            return when (fromChain) {
                KAVA_MAIN -> listOf(BNB_MAIN)
                BNB_MAIN -> listOf(KAVA_MAIN)
                else -> emptyList()
            }
        }

        fun getHtlcSwappableCoin(fromChain: BaseChain): List<String> {
            return when (fromChain) {
                BNB_MAIN -> listOf(
                    BaseConstant.TOKEN_HTLC_BINANCE_BNB,
                    BaseConstant.TOKEN_HTLC_BINANCE_BTCB,
                    BaseConstant.TOKEN_HTLC_BINANCE_XRPB,
                    BaseConstant.TOKEN_HTLC_BINANCE_BUSD
                )
                KAVA_MAIN ->
                    listOf(
                        BaseConstant.TOKEN_HTLC_KAVA_BNB,
                        BaseConstant.TOKEN_HTLC_KAVA_BTCB,
                        BaseConstant.TOKEN_HTLC_KAVA_XRPB,
                        BaseConstant.TOKEN_HTLC_KAVA_BUSD
                    )
                else -> emptyList()
            }
        }
    }
}