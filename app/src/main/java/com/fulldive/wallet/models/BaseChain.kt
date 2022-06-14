package com.fulldive.wallet.models

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.fulldive.wallet.components.path.*
import com.fulldive.wallet.extensions.unsafeLazy
import com.fulldive.wallet.interactors.secret.WalletUtils
import com.fulldive.wallet.models.gas.GasFeeEstimateCalculator
import com.fulldive.wallet.models.gas.GasRateProvider
import com.fulldive.wallet.models.gas.providers.*
import com.fulldive.wallet.models.local.ApiHost
import wannabit.io.cosmostaion.R
import wannabit.io.cosmostaion.base.BaseConstant
import java.math.BigDecimal

sealed class BaseChain constructor(
    val chainName: String,
    val aliases: Array<String> = emptyArray(),
    val chainAddressPrefix: String,
    val mintScanChainName: String = "",
    val ibcChainId: String = "",
    @DrawableRes val chainIcon: Int,
    @StringRes val chainTitle: Int,
    @StringRes val chainAlterTitle: Int,
    val mainToken: Token,
    @ColorRes val chainColor: Int,
    @DrawableRes val mnemonicBackground: Int,
    @ColorRes val chainBackground: Int,
    @ColorRes val chainTabColor: Int,
    @ColorRes val floatButtonColor: Int = R.color.colorWhite,
    @ColorRes val floatButtonBackground: Int = chainColor,
    val historyApiUrl: String = "",
    val grpcApiHost: ApiHost = ApiHost.EMPTY,
    val explorerUrl: String,
    val coingeckoUrl: String,
    val guide: Guide? = null,
    val relayerImageTag: String = "$mintScanChainName/relay-$mintScanChainName-unknown",
    val monikerImageTag: String = mintScanChainName,
    val blockTime: BigDecimal = BigDecimal.ZERO,
    val pathProvider: IPathProvider = PathProvider.DEFAULT,
    val gasProvider: GasProvider = GasProvider(),
    val gasRateProvider: GasRateProvider = GasRateProvider.ZERO,
    val gasFeeEstimateCalculator: GasFeeEstimateCalculator = GasFeeEstimateCalculator.DEFAULT,
    val isSupported: Boolean = true,
    val isTestNet: Boolean = false,
    val isGRPC: Boolean = true,
    val sortValue: Int = 0,
) {

    object COSMOS_MAIN : BaseChain(
        chainName = "cosmoshub-mainnet",
        aliases = arrayOf("cosmoshub-1", "cosmoshub-2", "cosmoshub-3", "cosmoshub-4"),
        chainAddressPrefix = "cosmos",
        mintScanChainName = "cosmos",
        ibcChainId = "cosmoshub-",
        chainIcon = R.drawable.cosmos_wh_main,
        chainTitle = R.string.str_cosmos_hub,
        chainAlterTitle = R.string.str_cosmos,
        mainToken = Tokens.ATOM,
        chainColor = R.color.colorAtom,
        mnemonicBackground = R.drawable.box_round_atom,
        chainBackground = R.color.colorTransBgCosmos,
        chainTabColor = R.color.color_tab_myvalidator,
        coingeckoUrl = "https://www.coingecko.com/en/coins/cosmos",
        explorerUrl = "https://www.mintscan.io/cosmos/",
        blockTime = BigDecimal("7.6597"),
        historyApiUrl = "https://api.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-cosmos-app-and.cosmostation.io"),
        gasRateProvider = GasRateProvider.DEFAULT,
        sortValue = 20,
        guide = Guide.create(
            guideIcon = R.drawable.guide_img,
            guideTitle = R.string.str_front_guide_title,
            guideMessage = R.string.str_front_guide_msg,
            buttonText1 = R.string.str_guide,
            buttonText2 = R.string.str_faq,
            buttonLink1 = "https://cosmos.network/",
            buttonLink2 = "https://guide.cosmostation.io/app_wallet_en.html"
        ),
        monikerImageTag = "cosmoshub"
    )

    object IMVERSED_MAIN : BaseChain(
        chainName = "imversed-canary",
        chainAddressPrefix = "imv",
        chainIcon = R.drawable.imversed,
        chainTitle = R.string.str_imversed_canary_net,
        chainAlterTitle = R.string.str_imversed_canary,
        mainToken = Tokens.IMV,
        chainColor = R.color.colorImversed,
        mnemonicBackground = R.drawable.box_round_imversed,
        chainBackground = R.color.colorTransBgImversed,
        chainTabColor = R.color.color_tab_myvalidator_imversed,
        grpcApiHost = ApiHost.from("qc.imversed.com"),
        coingeckoUrl = "https://www.coingecko.com/en/coins/imversed",
        explorerUrl = "https://tex-c.imversed.com/",
        pathProvider = PathProvider(60),
        gasProvider = ImversedGasProvider,
        gasRateProvider = GasRateProvider.DEFAULT,
        sortValue = 10,
        guide = Guide.create(
            guideIcon = R.drawable.infoicon_imversed,
            guideTitle = R.string.str_front_guide_title_imversed,
            guideMessage = R.string.str_front_guide_msg_imversed,
            buttonLink1 = "https://imversed.com",
            buttonLink2 = "https://imversed.com"
        )
    )

    object IRIS_MAIN : BaseChain(
        chainName = "irishub-mainnet",
        aliases = arrayOf("irishub", "irishub-1"),
        chainAddressPrefix = "iaa",
        mintScanChainName = "iris",
        ibcChainId = "irishub-",
        chainIcon = R.drawable.iris_wh,
        chainTitle = R.string.str_iris_net,
        chainAlterTitle = R.string.str_iris,
        mainToken = Tokens.IRIS,
        chainColor = R.color.colorIris,
        mnemonicBackground = R.drawable.box_round_iris,
        chainBackground = R.color.colorTransBgIris,
        chainTabColor = R.color.color_tab_myvalidator_iris,
        coingeckoUrl = "https://www.coingecko.com/en/coins/irisnet",
        explorerUrl = "https://www.mintscan.io/iris/",
        blockTime = BigDecimal("6.7884"),
        historyApiUrl = "https://api-iris.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-iris-app.cosmostation.io"),
        gasRateProvider = GasRateProvider("0.002", "0.02", "0.2"),
        gasFeeEstimateCalculator = GasFeeEstimateCalculator(gasRate = "0.2"),
        guide = Guide.create(
            guideIcon = R.drawable.irisnet_img,
            guideTitle = R.string.str_front_guide_title_iris,
            guideMessage = R.string.str_front_guide_msg_iris,
            buttonLink1 = "https://www.irisnet.org/",
            buttonLink2 = "https://medium.com/irisnet-blog"
        ),
        monikerImageTag = "irishub"
    )

    object IOV_MAIN : BaseChain(
        chainName = "iov-mainnet",
        aliases = arrayOf("iov-mainnet-2"),
        chainAddressPrefix = "star",
        mintScanChainName = "starname",
        ibcChainId = "iov-",
        chainIcon = R.drawable.chain_starname,
        chainTitle = R.string.str_iov_net,
        chainAlterTitle = R.string.str_iov,
        mainToken = Tokens.IOV,
        chainColor = R.color.colorIov,
        mnemonicBackground = R.drawable.box_round_iov,
        chainBackground = R.color.colorTransBgStarname,
        chainTabColor = R.color.color_tab_myvalidator_iov,
        coingeckoUrl = "https://www.coingecko.com/en/coins/starname",
        explorerUrl = "https://www.mintscan.io/starname/",
        blockTime = BigDecimal("6.0124"),
        historyApiUrl = "https://api-iov.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-iov-app.cosmostation.io"),
        pathProvider = PathProvider(234),
        gasProvider = IovGasProvider,
        gasRateProvider = GasRateProvider("0.10", "1.00", "1.00"),
        gasFeeEstimateCalculator = GasFeeEstimateCalculator.ONE,
        guide = Guide.create(
            guideIcon = R.drawable.iov_img,
            guideTitle = R.string.str_front_guide_title_iov,
            guideMessage = R.string.str_front_guide_msg_iov,
            buttonLink1 = "https://www.starname.me/",
            buttonLink2 = "https://medium.com/iov-internet-of-values"
        ),
        monikerImageTag = "iov"
    )

    object BNB_MAIN : BaseChain(
        chainName = "binance-mainnet",
        aliases = arrayOf("Binance-Chain-Tigris"),
        chainAddressPrefix = "bnb",
        mintScanChainName = "binance",
        chainIcon = R.drawable.binance_ch_img,
        chainTitle = R.string.str_binance_net,
        chainAlterTitle = R.string.str_binance,
        mainToken = Tokens.BNB,
        chainColor = R.color.colorBnb,
        mnemonicBackground = R.drawable.box_round_bnb,
        chainBackground = R.color.colorTransBgBinance,
        chainTabColor = R.color.color_tab_myvalidator,
        coingeckoUrl = "https://www.coingecko.com/en/coins/binancecoin",
        explorerUrl = "https://binance.mintscan.io/",
        blockTime = BigDecimal("0.4124"),
        pathProvider = PathProvider(714),
        isGRPC = false,
        gasRateProvider = GasRateProvider.ZERO,
        gasFeeEstimateCalculator = GasFeeEstimateCalculator.BNB,
        guide = Guide.create(
            guideIcon = R.drawable.binance_img,
            guideTitle = R.string.str_front_guide_title_binance,
            guideMessage = R.string.str_front_guide_msg_bnb,
            buttonText1 = R.string.str_faq_bnb,
            buttonText2 = R.string.str_guide_bnb,
            buttonLink1 = "https://www.binance.org",
            buttonLink2 = "https://medium.com/@binance"
        )
    )

    object KAVA_MAIN : BaseChain(
        chainName = "kava-mainnet",
        aliases = arrayOf("kava-1", "kava-2", "kava-3", "kava-4", "kava-5", "kava-6"),
        chainAddressPrefix = "kava",
        mintScanChainName = "kava",
        ibcChainId = "kava-",
        chainIcon = R.drawable.kava_img,
        chainTitle = R.string.str_kava_net,
        chainAlterTitle = R.string.str_kava,
        mainToken = Tokens.KAVA,
        chainColor = R.color.colorKava,
        mnemonicBackground = R.drawable.box_round_kava,
        chainBackground = R.color.colorTransBgKava,
        chainTabColor = R.color.color_tab_myvalidator_kava,
        coingeckoUrl = "https://www.coingecko.com/en/coins/kava",
        explorerUrl = "https://www.mintscan.io/kava/",
        blockTime = BigDecimal("6.7262"),
        historyApiUrl = "https://api-kava.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-kava-app.cosmostation.io"),
        pathProvider = MultiPathProvider(459, mapOf(0 to 118)),
        gasProvider = KavaGasProvider,
        gasRateProvider = GasRateProvider("0.01", "0.0025", "0.025"),
        gasFeeEstimateCalculator = GasFeeEstimateCalculator.KAVA,
        guide = Guide.create(
            guideIcon = R.drawable.kavamain_img,
            guideTitle = R.string.str_front_guide_title_kava,
            guideMessage = R.string.str_front_guide_msg_kava,
            buttonLink1 = "https://www.kava.io/registration/",
            buttonLink2 = "https://medium.com/kava-labs"
        )
    )

    object BAND_MAIN : BaseChain(
        chainName = "band-mainnet",
        aliases = arrayOf("band-wenchang-mainnet", "band-guanyu-mainnet"),
        chainAddressPrefix = "band",
        mintScanChainName = "band",
        monikerImageTag = "bandprotocol",
        ibcChainId = "laozi-mainnet",
        chainIcon = R.drawable.band_chain_img,
        chainTitle = R.string.str_band_chain,
        chainAlterTitle = R.string.str_band,
        mainToken = Tokens.BAND,
        chainColor = R.color.colorBand,
        mnemonicBackground = R.drawable.box_round_band,
        chainBackground = R.color.colorTransBgBand,
        chainTabColor = R.color.color_tab_myvalidator_band,
        coingeckoUrl = "https://www.coingecko.com/en/coins/band-protocol",
        explorerUrl = "https://www.mintscan.io/band/",
        blockTime = BigDecimal("3.0236"),
        historyApiUrl = "https://api-band.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-band-app.cosmostation.io"),
        pathProvider = PathProvider(494),
        gasProvider = BandGasProvider,
        gasRateProvider = GasRateProvider("0.000", "0.0025", "0.025"),
        gasFeeEstimateCalculator = GasFeeEstimateCalculator.ZERO,
        guide = Guide.create(
            guideIcon = R.drawable.infoicon_bandprotocol,
            guideTitle = R.string.str_front_guide_title_band,
            guideMessage = R.string.str_front_guide_msg_band,
            buttonLink1 = "https://bandprotocol.com/",
            buttonLink2 = "https://medium.com/bandprotocol"
        )
    )

    object CERTIK_MAIN : BaseChain(
        chainName = "shentu-mainnet",
        aliases = arrayOf("shentu-1", "shentu-2"),
        chainAddressPrefix = "certik",
        mintScanChainName = "certik",
        ibcChainId = "shentu-",
        chainIcon = R.drawable.certik_chain_img,
        chainTitle = R.string.str_certik_chain,
        chainAlterTitle = R.string.str_certik_main,
        mainToken = Tokens.CTK,
        chainColor = R.color.colorCertik,
        mnemonicBackground = R.drawable.box_round_certik,
        chainBackground = R.color.colorTransBgCertik,
        chainTabColor = R.color.color_tab_myvalidator_certik,
        coingeckoUrl = "https://www.coingecko.com/en/coins/certik",
        explorerUrl = "https://www.mintscan.io/certik/",
        blockTime = BigDecimal("5.9740"),
        historyApiUrl = "https://api-certik.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-certik-app.cosmostation.io"),
        gasProvider = CertikGasProvider,
        gasRateProvider = GasRateProvider("0.05"),
        gasFeeEstimateCalculator = GasFeeEstimateCalculator(gasRate = "0.05"),
        guide = Guide.create(
            guideIcon = R.drawable.certik_img,
            guideTitle = R.string.str_front_guide_title_certik,
            guideMessage = R.string.str_front_guide_msg_certik,
            buttonLink1 = "https://www.certik.foundation/",
            buttonLink2 = "https://www.certik.foundation/blog"
        )
    )

    object SECRET_MAIN : BaseChain(
        chainName = "secret-mainnet",
        aliases = arrayOf("secret-2"),
        chainAddressPrefix = "secret",
        mintScanChainName = "secret",
        ibcChainId = "secret-",
        chainIcon = R.drawable.chainsecret,
        chainTitle = R.string.str_secret_chain,
        chainAlterTitle = R.string.str_secret_main,
        mainToken = Tokens.SCRT,
        chainColor = R.color.colorSecret,
        mnemonicBackground = R.drawable.box_round_secret,
        chainBackground = R.color.colorTransBgSecret,
        chainTabColor = R.color.color_tab_myvalidator_secret,
        floatButtonBackground = R.color.colorSecret2,
        coingeckoUrl = "https://www.coingecko.com/en/coins/secret",
        explorerUrl = "https://www.mintscan.io/secret/",
        blockTime = BigDecimal("6.0408"),
        historyApiUrl = "https://api-secret.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-secret.cosmostation.io"),
        pathProvider = MultiPathProvider(529, mapOf(0 to 118)),
        gasProvider = SecretGasProvider,
        gasRateProvider = GasRateProvider("0.25"),
        gasFeeEstimateCalculator = GasFeeEstimateCalculator(gasRate = "0.25"),
        guide = Guide.create(
            guideIcon = R.drawable.secret_img,
            guideTitle = R.string.str_front_guide_title_secret,
            guideMessage = R.string.str_front_guide_msg_secret,
            buttonLink1 = "https://scrt.network",
            buttonLink2 = "https://blog.scrt.network"
        )
    )

    object AKASH_MAIN : BaseChain(
        chainName = "akashnet-mainnet",
        aliases = arrayOf("akashnet-1", "akashnet-2"),
        chainAddressPrefix = "akash",
        mintScanChainName = "akash",
        ibcChainId = "akashnet-",
        chainIcon = R.drawable.akash_chain_img,
        chainTitle = R.string.str_akash_chain,
        chainAlterTitle = R.string.str_akash_main,
        mainToken = Tokens.AKT,
        chainColor = R.color.colorAkash,
        mnemonicBackground = R.drawable.box_round_akash,
        chainBackground = R.color.colorTransBgAkash,
        chainTabColor = R.color.color_tab_myvalidator_akash,
        coingeckoUrl = "https://www.coingecko.com/en/coins/akash-network",
        explorerUrl = "https://www.mintscan.io/akash/",
        blockTime = BigDecimal("6.4526"),
        historyApiUrl = "https://api-akash.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-akash-app.cosmostation.io"),
        gasRateProvider = GasRateProvider.DEFAULT,
        guide = Guide.create(
            guideIcon = R.drawable.akash_img,
            guideTitle = R.string.str_front_guide_title_akash,
            guideMessage = R.string.str_front_guide_msg_akash,
            buttonLink1 = "https://akash.network/",
            buttonLink2 = "https://akash.network/"
        )
    )

    object OKEX_MAIN : BaseChain(
        chainName = "okexchain-mainnet",
        aliases = arrayOf("okexchain-66"),
        chainAddressPrefix = "0x",
        mintScanChainName = "okex",
        chainIcon = R.drawable.chain_okx,
        chainTitle = R.string.str_ok_net,
        chainAlterTitle = R.string.str_okex_main,
        mainToken = Tokens.OKT,
        chainColor = R.color.colorOK,
        mnemonicBackground = R.drawable.box_round_okex,
        chainBackground = R.color.colorTransBgOkex,
        chainTabColor = R.color.color_tab_myvalidator_ok,
        coingeckoUrl = "https://www.coingecko.com/en/coins/okexchain",
        explorerUrl = "https://www.oklink.com/okexchain/",
        blockTime = BigDecimal("4.0286"),
        gasRateProvider = GasRateProvider("0.0000000001"),
        pathProvider = HintedMultiPathProvider(
            60,
            mapOf(0 to 996, 1 to 996),
            "Ethereum Type",
            mapOf(0 to "Tendermint Type", 1 to "Ethermint Type")
        ),
        gasProvider = OkexGasProvider,
        gasFeeEstimateCalculator = GasFeeEstimateCalculator(gasRate = "0.0000000001", scale = 18),
        guide = Guide.create(
            guideIcon = R.drawable.infoicon_okx,
            guideTitle = R.string.str_front_guide_title_ok,
            guideMessage = R.string.str_front_guide_msg_ok,
            buttonText1 = R.string.str_faq_ok,
            buttonText2 = R.string.str_guide_ok,
            buttonLink1 = "https://www.okex.com/",
            buttonLink2 = "https://www.okex.com/community"
        ),
        isGRPC = false
    )

    object PERSIS_MAIN : BaseChain(
        chainName = "persistence-mainnet",
        chainAddressPrefix = "persistence",
        mintScanChainName = "persistence",
        ibcChainId = "core-",
        chainIcon = R.drawable.chainpersistence,
        chainTitle = R.string.str_persis_net,
        chainAlterTitle = R.string.str_persis_main,
        mainToken = Tokens.XPRT,
        chainColor = R.color.colorPersis,
        mnemonicBackground = R.drawable.box_round_persis,
        chainBackground = R.color.colorTransBgPersis,
        chainTabColor = R.color.color_tab_myvalidator_persis,
        floatButtonColor = R.color.colorPersis,
        floatButtonBackground = R.color.colorBlack,
        coingeckoUrl = "https://www.coingecko.com/en/coins/persistence",
        explorerUrl = "https://www.mintscan.io/persistence/",
        blockTime = BigDecimal("5.7982"),
        historyApiUrl = "https://api-persistence.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-persistence-app.cosmostation.io"),
        pathProvider = PathProvider(750),
        gasRateProvider = GasRateProvider.ZERO,
        gasFeeEstimateCalculator = GasFeeEstimateCalculator.ZERO,
        guide = Guide.create(
            guideIcon = R.drawable.persistence_img,
            guideTitle = R.string.str_front_guide_title_persis,
            guideMessage = R.string.str_front_guide_msg_persis,
            buttonLink1 = "https://persistence.one/",
            buttonLink2 = "https://medium.com/persistence-blog"
        )
    )

    object SENTINEL_MAIN : BaseChain(
        chainName = "sentinel-mainnet",
        chainAddressPrefix = "sent",
        mintScanChainName = "sentinel",
        ibcChainId = "sentinelhub-",
        chainIcon = R.drawable.chainsentinel,
        chainTitle = R.string.str_sentinel_net,
        chainAlterTitle = R.string.str_sentinel_main,
        mainToken = Tokens.DVPN,
        chainColor = R.color.colorSentinel,
        mnemonicBackground = R.drawable.box_round_sentinel,
        chainBackground = R.color.colorTransBgSentinel,
        chainTabColor = R.color.color_tab_myvalidator_sentinel,
        floatButtonBackground = R.color.colorSentinel3,
        coingeckoUrl = "https://www.coingecko.com/en/coins/sentinel",
        explorerUrl = "https://www.mintscan.io/sentinel/",
        blockTime = BigDecimal("6.3113"),
        historyApiUrl = "https://api-sentinel.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-sentinel-app.cosmostation.io"),
        gasProvider = AlterGasProvider,
        gasRateProvider = GasRateProvider("0.01", "0.1", "0.1"),
        gasFeeEstimateCalculator = GasFeeEstimateCalculator(gasRate = "0.1"),
        guide = Guide.create(
            guideIcon = R.drawable.sentinel_img,
            guideTitle = R.string.str_front_guide_title_sentinel,
            guideMessage = R.string.str_front_guide_msg_sentinel,
            buttonLink1 = "https://sentinel.co/",
            buttonLink2 = "https://medium.com/sentinel"
        )
    )

    object FETCHAI_MAIN : BaseChain(
        chainName = "fetchai-mainnet",
        chainAddressPrefix = "fetch",
        mintScanChainName = "fetchai",
        ibcChainId = "fetchhub--",
        chainIcon = R.drawable.chainfetchai,
        chainTitle = R.string.str_fetch_net,
        chainAlterTitle = R.string.str_fetch_main,
        mainToken = Tokens.FET,
        chainColor = R.color.colorFetch,
        mnemonicBackground = R.drawable.box_round_fetch,
        chainBackground = R.color.colorTransBgFetch,
        chainTabColor = R.color.color_tab_myvalidator_fetch,
        coingeckoUrl = "https://www.coingecko.com/en/coins/fetch-ai",
        explorerUrl = "https://www.mintscan.io/fetchai/",
        blockTime = BigDecimal("6.0678"),
        historyApiUrl = "https://api-fetchai.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-fetchai-app.cosmostation.io"),
        pathProvider = FetchaiPathProvider,
        gasProvider = AlterGasProvider,
        gasRateProvider = GasRateProvider.ZERO,
        gasFeeEstimateCalculator = GasFeeEstimateCalculator.ZERO,
        guide = Guide.create(
            guideIcon = R.drawable.fetchai_img,
            guideTitle = R.string.str_front_guide_title_fetch,
            guideMessage = R.string.str_front_guide_msg_fetch,
            buttonLink1 = "https://fetch.ai/",
            buttonLink2 = "https://fetch.ai/blog/"
        )
    )

    object CRYPTO_MAIN : BaseChain(
        chainName = "crytoorg-mainnet",
        chainAddressPrefix = "cro",
        mintScanChainName = "cryptoorg",
        ibcChainId = "crypto-org-",
        chainIcon = R.drawable.chaincrypto,
        chainTitle = R.string.str_crypto_net,
        chainAlterTitle = R.string.str_crypto_main,
        mainToken = Tokens.CRO,
        chainColor = R.color.colorCryto,
        mnemonicBackground = R.drawable.box_round_cryto,
        chainBackground = R.color.colorTransBgCryto,
        chainTabColor = R.color.color_tab_myvalidator_cryto,
        floatButtonBackground = R.color.colorCryto2,
        coingeckoUrl = "https://www.coingecko.com/en/coins/crypto-com-chain",
        explorerUrl = "https://www.mintscan.io/crypto-org/",
        blockTime = BigDecimal("6.1939"),
        historyApiUrl = "https://api-cryptocom.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-cryptocom-app.cosmostation.io"),
        pathProvider = PathProvider(394),
        gasRateProvider = GasRateProvider("0.025", "0.05", "0.075"),
        guide = Guide.create(
            guideIcon = R.drawable.cryptochain_img,
            guideTitle = R.string.str_front_guide_title_crypto,
            guideMessage = R.string.str_front_guide_msg_crypto,
            buttonLink1 = "https://crypto.org/",
            buttonLink2 = "https://crypto.org/community/"
        ),
        monikerImageTag = "cryto"
    )

    object SIF_MAIN : BaseChain(
        chainName = "sif-mainnet",
        chainAddressPrefix = "sif",
        mintScanChainName = "sifchain",
        ibcChainId = "sifchain-",
        chainIcon = R.drawable.chainsifchain,
        chainTitle = R.string.str_sif_net,
        chainAlterTitle = R.string.str_sif_main,
        mainToken = Tokens.ROWAN,
        chainColor = R.color.colorSif,
        mnemonicBackground = R.drawable.box_round_sif,
        chainBackground = R.color.colorTransBgSif,
        chainTabColor = R.color.color_tab_myvalidator_sif,
        coingeckoUrl = "https://www.coingecko.com/en/coins/sifchain",
        explorerUrl = "https://www.mintscan.io/sifchain/",
        blockTime = BigDecimal("5.7246"),
        historyApiUrl = "https://api-sifchain.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-sifchain-app.cosmostation.io"),
        gasProvider = AlterGasProvider,
        gasRateProvider = GasRateProvider("0.50"),
        gasFeeEstimateCalculator = GasFeeEstimateCalculator.SIF,
        guide = Guide.create(
            guideIcon = R.drawable.sifchain_img,
            guideTitle = R.string.str_front_guide_title_sif,
            guideMessage = R.string.str_front_guide_msg_sif,
            buttonLink1 = "https://sifchain.finance/",
            buttonLink2 = "https://medium.com/sifchain-finance"
        ),
        monikerImageTag = "sif"
    )

    object KI_MAIN : BaseChain(
        chainName = "ki-mainnet",
        chainAddressPrefix = "ki",
        mintScanChainName = "kichain",
        ibcChainId = "kichain-",
        chainIcon = R.drawable.chain_kifoundation,
        chainTitle = R.string.str_ki_net,
        chainAlterTitle = R.string.str_ki_main,
        mainToken = Tokens.XKI,
        chainColor = R.color.colorKi,
        mnemonicBackground = R.drawable.box_round_ki,
        chainBackground = R.color.colorTransBgKi,
        chainTabColor = R.color.color_tab_myvalidator_ki,
        coingeckoUrl = "https://www.coingecko.com/en/coins/ki",
        explorerUrl = "https://www.mintscan.io/ki-chain/",
        blockTime = BigDecimal("5.7571"),
        historyApiUrl = "https://api-kichain.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-kichain-app.cosmostation.io"),
        gasProvider = AlterGasProvider,
        gasRateProvider = GasRateProvider("0.025"),
        guide = Guide.create(
            guideIcon = R.drawable.kifoundation_img,
            guideTitle = R.string.str_front_guide_title_ki,
            guideMessage = R.string.str_front_guide_msg_ki,
            buttonLink1 = "https://foundation.ki/en",
            buttonLink2 = "https://medium.com/ki-foundation"
        ),
        monikerImageTag = "ki"
    )

    object OSMOSIS_MAIN : BaseChain(
        chainName = "osmosis-mainnet",
        chainAddressPrefix = "osmo",
        mintScanChainName = "osmosis",
        ibcChainId = "osmosis-",
        chainIcon = R.drawable.chain_osmosis,
        chainTitle = R.string.str_osmosis_net,
        chainAlterTitle = R.string.str_osmosis_main,
        mainToken = Tokens.OSMO,
        chainColor = R.color.colorOsmosis,
        mnemonicBackground = R.drawable.box_round_osmosis,
        chainBackground = R.color.colorTransBgOsmosis,
        chainTabColor = R.color.color_tab_myvalidator_osmosis,
        coingeckoUrl = "https://www.coingecko.com/en/coins/osmosis",
        explorerUrl = "https://www.mintscan.io/osmosis/",
        blockTime = BigDecimal("6.5324"),
        historyApiUrl = "https://api-osmosis.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-osmosis-app-and.cosmostation.io"),
        gasProvider = OsmosisGasProvider,
        gasRateProvider = GasRateProvider("0.000", "0.0025", "0.025"),
        gasFeeEstimateCalculator = GasFeeEstimateCalculator.ZERO,
        guide = Guide.create(
            guideIcon = R.drawable.infoicon_osmosis,
            guideTitle = R.string.str_front_guide_title_osmosis,
            guideMessage = R.string.str_front_guide_msg_osmosis,
            buttonLink1 = "https://osmosis.zone/",
            buttonLink2 = "https://medium.com/osmosis"
        )
    )

    object MEDI_MAIN : BaseChain(
        chainName = "medibloc-mainnet",
        chainAddressPrefix = "panacea",
        mintScanChainName = "medibloc",
        ibcChainId = "panacea-",
        chainIcon = R.drawable.chainmedibloc,
        chainTitle = R.string.str_medi_net,
        chainAlterTitle = R.string.str_medi_main,
        mainToken = Tokens.MED,
        chainColor = R.color.colorMedi,
        mnemonicBackground = R.drawable.box_round_medi,
        chainBackground = R.color.colorTransBgMedi,
        chainTabColor = R.color.color_tab_myvalidator_med,
        floatButtonColor = R.color.colorMedi,
        floatButtonBackground = R.color.colorWhite,
        coingeckoUrl = "https://www.coingecko.com/en/coins/medibloc",
        explorerUrl = "https://www.mintscan.io/medibloc/",
        blockTime = BigDecimal("5.7849"),
        historyApiUrl = "https://api-medibloc.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-medibloc-app.cosmostation.io"),
        pathProvider = PathProvider(371),
        gasProvider = AlterGasProvider,
        gasRateProvider = GasRateProvider("5.00"),
        gasFeeEstimateCalculator = GasFeeEstimateCalculator(gasRate = "5.00"),
        guide = Guide.create(
            guideIcon = R.drawable.medibloc_img,
            guideTitle = R.string.str_front_guide_title_medi,
            guideMessage = R.string.str_front_guide_msg_medi,
            buttonLink1 = "https://medibloc.com/en/",
            buttonLink2 = "https://medium.com/medibloc"
        )
    )

    object EMONEY_MAIN : BaseChain(
        chainName = "emoney-mainnet",
        chainAddressPrefix = "emoney",
        mintScanChainName = "emoney",
        ibcChainId = "emoney-",
        chainIcon = R.drawable.chain_emoney,
        chainTitle = R.string.str_emoney_net,
        chainAlterTitle = R.string.str_emoney_main,
        mainToken = Tokens.NGM,
        chainColor = R.color.colorEmoney,
        mnemonicBackground = R.drawable.box_round_emoney,
        chainBackground = R.color.colorTransBgEmoney,
        chainTabColor = R.color.color_tab_myvalidator_emoney,
        coingeckoUrl = "https://www.coingecko.com/en/coins/e-money",
        explorerUrl = "https://www.mintscan.io/emoney/",
        blockTime = BigDecimal("24.8486"),
        historyApiUrl = "https://api-emoney.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-emoney-app.cosmostation.io"),
        gasRateProvider = GasRateProvider("0.10", "0.30", "1.00"),
        gasFeeEstimateCalculator = GasFeeEstimateCalculator.ONE,
        guide = Guide.create(
            guideIcon = R.drawable.infoicon_emoney,
            guideTitle = R.string.str_front_guide_title_emoney,
            guideMessage = R.string.str_front_guide_msg_emoney,
            buttonLink1 = "https://www.e-money.com/",
            buttonLink2 = "https://emoneytokenstandard.org/"
        )
    )

    object RIZON_MAIN : BaseChain(
        chainName = "rizon-mainnet",
        chainAddressPrefix = "rizon",
        mintScanChainName = "rizon",
        ibcChainId = "titan-",
        chainIcon = R.drawable.chain_rizon,
        chainTitle = R.string.str_rizon_net,
        chainAlterTitle = R.string.str_rizon_main,
        mainToken = Tokens.ATOLO,
        chainColor = R.color.colorRizon,
        mnemonicBackground = R.drawable.box_round_rizon,
        chainBackground = R.color.colorTransBgRizon,
        chainTabColor = R.color.color_tab_myvalidator_rizon,
        coingeckoUrl = "https://www.coingecko.com/en/coins/rizon",
        explorerUrl = "https://www.mintscan.io/rizon/",
        blockTime = BigDecimal("5.8850"),
        historyApiUrl = "https://api-rizon.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-rizon-app.cosmostation.io"),
        gasRateProvider = GasRateProvider.DEFAULT,
        guide = Guide.create(
            guideIcon = R.drawable.infoicon_rizon,
            guideTitle = R.string.str_front_guide_title_rizon,
            guideMessage = R.string.str_front_guide_msg_rizon,
            buttonLink1 = "https://www.hdactech.com/en/index.do",
            buttonLink2 = "https://medium.com/hdac"
        )
    )

    object JUNO_MAIN : BaseChain(
        chainName = "juno-mainnet",
        chainAddressPrefix = "juno",
        mintScanChainName = "juno",
        ibcChainId = "juno-",
        chainIcon = R.drawable.chain_juno,
        chainTitle = R.string.str_juno_net,
        chainAlterTitle = R.string.str_juno_main,
        mainToken = Tokens.JUNO,
        chainColor = R.color.colorJuno,
        mnemonicBackground = R.drawable.box_round_juno,
        chainBackground = R.color.colorTransBgJuno,
        chainTabColor = R.color.color_tab_myvalidator_juno,
        coingeckoUrl = "https://www.coingecko.com/en/coins/juno-network",
        explorerUrl = "https://www.mintscan.io/juno/",
        blockTime = BigDecimal("6.3104"),
        historyApiUrl = "https://api-juno.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-juno-app.cosmostation.io"),
        gasRateProvider = GasRateProvider("0.0025", "0.005", "0.025"),
        guide = Guide.create(
            guideIcon = R.drawable.infoicon_juno,
            guideTitle = R.string.str_front_guide_title_juno,
            guideMessage = R.string.str_front_guide_msg_juno,
            buttonLink1 = "https://junochain.com/",
            buttonLink2 = "https://medium.com/@JunoNetwork"
        )
    )

    object REGEN_MAIN : BaseChain(
        chainName = "regen-mainnet",
        chainAddressPrefix = "regen",
        mintScanChainName = "regen",
        ibcChainId = "regen-",
        chainIcon = R.drawable.chain_regen,
        chainTitle = R.string.str_regen_net,
        chainAlterTitle = R.string.str_regen_main,
        mainToken = Tokens.REGEN,
        chainColor = R.color.colorRegen,
        mnemonicBackground = R.drawable.box_round_regen,
        chainBackground = R.color.colorTransBgRegen,
        chainTabColor = R.color.color_tab_myvalidator_regen,
        coingeckoUrl = "https://www.coingecko.com/en/coins/regen",
        explorerUrl = "https://www.mintscan.io/regen/",
        blockTime = BigDecimal("6.2491"),
        historyApiUrl = "https://api-regen.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-regen-app.cosmostation.io"),
        gasRateProvider = GasRateProvider.DEFAULT,
        guide = Guide.create(
            guideIcon = R.drawable.infoicon_regen,
            guideTitle = R.string.str_front_guide_title_regen,
            guideMessage = R.string.str_front_guide_msg_regen,
            buttonLink1 = "https://www.regen.network/",
            buttonLink2 = "https://medium.com/regen-network"
        )
    )

    object BITCANNA_MAIN : BaseChain(
        chainName = "bitcanna-mainnet",
        chainAddressPrefix = "bcna",
        mintScanChainName = "bitcanna",
        ibcChainId = "bitcanna-",
        chainIcon = R.drawable.chain_bitcanna,
        chainTitle = R.string.str_bitcanna_net,
        chainAlterTitle = R.string.str_bitcanna_main,
        mainToken = Tokens.BCNA,
        chainColor = R.color.colorBitcanna,
        mnemonicBackground = R.drawable.box_round_bitcanna,
        chainBackground = R.color.colorTransBgBitcanna,
        chainTabColor = R.color.color_tab_myvalidator_bitcanna,
        coingeckoUrl = "https://www.coingecko.com/en/coins/bitcanna",
        explorerUrl = "https://www.mintscan.io/bitcanna/",
        blockTime = BigDecimal("6.0256"),
        historyApiUrl = "https://api-bitcanna.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-bitcanna-app.cosmostation.io"),
        gasRateProvider = GasRateProvider("0.025"),
        guide = Guide.create(
            guideIcon = R.drawable.infoicon_bitcanna,
            guideTitle = R.string.str_front_guide_title_bitcanna,
            guideMessage = R.string.str_front_guide_msg_bitcanna,
            buttonLink1 = "https://www.bitcanna.io/",
            buttonLink2 = "https://medium.com/@BitCannaGlobal"
        )
    )

    object ALTHEA_MAIN : BaseChain(
        chainName = "althea-mainnet",
        chainAddressPrefix = "althea",
        mintScanChainName = "althea",
        chainIcon = R.drawable.chain_althea,
        chainTitle = R.string.str_althea_net,
        chainAlterTitle = R.string.str_althea_main,
        mainToken = Tokens.ALTG,
        chainColor = R.color.colorAlthea,
        mnemonicBackground = R.drawable.box_round_althea,
        chainBackground = R.color.colorTransBgAlthea,
        chainTabColor = R.color.color_tab_myvalidator_althea,
        historyApiUrl = "https://api-althea.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-althea-app.cosmostation.io"),
        coingeckoUrl = "",
        explorerUrl = "https://www.mintscan.io/althea/",
        guide = Guide.create(
            guideIcon = R.drawable.infoicon_althea,
            guideTitle = R.string.str_front_guide_title_althea,
            guideMessage = R.string.str_front_guide_msg_althea,
            buttonLink1 = "https://www.althea.net/",
            buttonLink2 = "https://blog.althea.net/"
        )
    )

    object STARGAZE_MAIN : BaseChain(
        chainName = "stargaze-mainnet",
        chainAddressPrefix = "stars",
        mintScanChainName = "stargaze",
        ibcChainId = "stargaze-",
        chainIcon = R.drawable.chain_stargaze,
        chainTitle = R.string.str_stargaze_net,
        chainAlterTitle = R.string.str_stargaze_main,
        mainToken = Tokens.STARS,
        chainColor = R.color.colorStargaze,
        mnemonicBackground = R.drawable.box_round_stargaze,
        chainBackground = R.color.colorTransBgStargaze,
        chainTabColor = R.color.color_tab_myvalidator_stargaze,
        coingeckoUrl = "https://www.coingecko.com/en/coins/stargaze",
        explorerUrl = "https://www.mintscan.io/stargaze/",
        blockTime = BigDecimal("5.8129"),
        historyApiUrl = "https://api-stargaze.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-stargaze-app.cosmostation.io"),
        gasRateProvider = GasRateProvider.ZERO,
        gasFeeEstimateCalculator = GasFeeEstimateCalculator.ZERO,
        guide = Guide.create(
            guideIcon = R.drawable.infoicon_stargaze,
            guideTitle = R.string.str_front_guide_title_stargaze,
            guideMessage = R.string.str_front_guide_msg_stargaze,
            buttonLink1 = "https://stargaze.zone/",
            buttonLink2 = "https://medium.com/stargaze-protocol"
        )
    )

    object GRABRIDGE_MAIN : BaseChain(
        chainName = "GravityBridge-mainnet",
        chainAddressPrefix = "gravity",
        mintScanChainName = "gravity-bridge",
        relayerImageTag = "gravity-bridge/relay-gravitybridge-unknown",
        ibcChainId = "gravity-bridge-",
        chainIcon = R.drawable.chain_gravitybridge,
        chainTitle = R.string.str_grabridge_net,
        chainAlterTitle = R.string.str_grabridge_main,
        mainToken = Tokens.GRAVITON,
        chainColor = R.color.colorGraBridge,
        mnemonicBackground = R.drawable.box_round_grabridge,
        chainBackground = R.color.colorTransBgGraBridge,
        chainTabColor = R.color.color_tab_myvalidator_grabridge,
        coingeckoUrl = "https://www.coingecko.com/en/coins/graviton",
        explorerUrl = "https://www.mintscan.io/gravity-bridge/",
        blockTime = BigDecimal("6.4500"),
        historyApiUrl = "https://api-gravity-bridge.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-gravity-bridge-app.cosmostation.io"),
        gasRateProvider = GasRateProvider.ZERO,
        gasFeeEstimateCalculator = GasFeeEstimateCalculator.ZERO,
        guide = Guide.create(
            guideIcon = R.drawable.infoicon_gravitybridge,
            guideTitle = R.string.str_front_guide_title_grabridge,
            guideMessage = R.string.str_front_guide_msg_grabridge,
            buttonLink1 = "https://www.gravitybridge.net/",
            buttonLink2 = "https://www.gravitybridge.net/blog"
        )
    )

    object COMDEX_MAIN : BaseChain(
        chainName = "comdex-mainnet",
        chainAddressPrefix = "comdex",
        mintScanChainName = "comdex",
        ibcChainId = "comdex-",
        chainIcon = R.drawable.chain_comdex,
        chainTitle = R.string.str_comdex_net,
        chainAlterTitle = R.string.str_comdex_main,
        mainToken = Tokens.CMDX,
        chainColor = R.color.colorComdex,
        mnemonicBackground = R.drawable.box_round_comdex,
        chainBackground = R.color.colorTransBgComdex,
        chainTabColor = R.color.color_tab_myvalidator_comdex,
        floatButtonColor = R.color.colorComdex,
        floatButtonBackground = R.color.colorTransBgComdex,
        coingeckoUrl = "https://www.coingecko.com/en/coins/comdex",
        explorerUrl = "https://www.mintscan.io/comdex/",
        blockTime = BigDecimal("6.1746"),
        historyApiUrl = "https://api-comdex.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-comdex-app.cosmostation.io"),
        gasRateProvider = GasRateProvider("0.025"),
        guide = Guide.create(
            guideIcon = R.drawable.infoicon_comdex,
            guideTitle = R.string.str_front_guide_title_comdex,
            guideMessage = R.string.str_front_guide_msg_comdex,
            buttonLink1 = "https://comdex.one/home",
            buttonLink2 = "https://blog.comdex.one"
        )
    )

    object INJ_MAIN : BaseChain(
        chainName = "injective-mainnet",
        chainAddressPrefix = "inj",
        mintScanChainName = "injective",
        ibcChainId = "injective-",
        chainIcon = R.drawable.chain_injective,
        chainTitle = R.string.str_inj_net,
        chainAlterTitle = R.string.str_inj_main,
        mainToken = Tokens.INJ,
        chainColor = R.color.colorInj,
        mnemonicBackground = R.drawable.box_round_inj,
        chainBackground = R.color.colorTransBgInj,
        chainTabColor = R.color.color_tab_myvalidator_inj,
        floatButtonColor = R.color.colorBlack,
        coingeckoUrl = "https://www.coingecko.com/en/coins/injective-protocol",
        explorerUrl = "https://www.mintscan.io/injective/",
        blockTime = BigDecimal("2.4865"),
        historyApiUrl = "https://api-inj.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-inj-app.cosmostation.io"),
        pathProvider = PathProvider(60),
        gasProvider = InjGasProvider,
        gasRateProvider = GasRateProvider("500000000.00"),
        gasFeeEstimateCalculator = GasFeeEstimateCalculator(gasRate = "500000000.00"),
        guide = Guide.create(
            guideIcon = R.drawable.infoicon_injective,
            guideTitle = R.string.str_front_guide_title_inj,
            guideMessage = R.string.str_front_guide_msg_inj,
            buttonLink1 = "https://injectiveprotocol.com/",
            buttonLink2 = "https://blog.injectiveprotocol.com/"
        )
    )

    object BITSONG_MAIN : BaseChain(
        chainName = "bitsong-mainnet",
        chainAddressPrefix = "bitsong",
        mintScanChainName = "bitsong",
        ibcChainId = "bitsong-",
        chainIcon = R.drawable.chain_bitsong,
        chainTitle = R.string.str_bitsong_net,
        chainAlterTitle = R.string.str_bitsong_main,
        mainToken = Tokens.BTSG,
        chainColor = R.color.colorBitsong,
        mnemonicBackground = R.drawable.box_round_bitsong,
        chainBackground = R.color.colorTransBgBitsong,
        chainTabColor = R.color.color_tab_myvalidator_bitsong,
        coingeckoUrl = "https://www.coingecko.com/en/coins/bitsong",
        explorerUrl = "https://www.mintscan.io/bitsong/",
        blockTime = BigDecimal("5.9040"),
        historyApiUrl = "https://api-bitsong.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-bitsong-app.cosmostation.io"),
        pathProvider = PathProvider(639),
        gasRateProvider = GasRateProvider("0.025"),
        guide = Guide.create(
            guideIcon = R.drawable.infoicon_bitsong,
            guideTitle = R.string.str_front_guide_title_bitsong,
            guideMessage = R.string.str_front_guide_msg_bitsong,
            buttonLink1 = "https://explorebitsong.com/",
            buttonLink2 = "https://bitsongofficial.medium.com/"
        )
    )

    object DESMOS_MAIN : BaseChain(
        chainName = "desmos-mainnet",
        chainAddressPrefix = "desmos",
        mintScanChainName = "desmos",
        ibcChainId = "desmos-",
        chainIcon = R.drawable.chain_desmos,
        chainTitle = R.string.str_desmos_net,
        chainAlterTitle = R.string.str_desmos_main,
        mainToken = Tokens.DSM,
        chainColor = R.color.colorDesmos,
        mnemonicBackground = R.drawable.box_round_desmos,
        chainBackground = R.color.colorTransBgDesmos,
        chainTabColor = R.color.color_tab_myvalidator_desmos,
        coingeckoUrl = "https://www.coingecko.com/en/coins/desmos",
        explorerUrl = "https://www.mintscan.io/desmos/",
        blockTime = BigDecimal("6.1605"),
        historyApiUrl = "https://api-desmos.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-desmos-app.cosmostation.io"),
        pathProvider = PathProvider(852),
        gasRateProvider = GasRateProvider("0.001", "0.010", "0.025"),
        guide = Guide.create(
            guideIcon = R.drawable.infoicon_desmos,
            guideTitle = R.string.str_front_guide_title_desmos,
            guideMessage = R.string.str_front_guide_msg_desmos,
            buttonLink1 = "https://www.desmos.network/",
            buttonLink2 = "https://medium.com/desmosnetwork"
        )
    )

    object LUM_MAIN : BaseChain(
        chainName = "lum-mainnet",
        chainAddressPrefix = "lum",
        mintScanChainName = "lum",
        relayerImageTag = "lum-network/relay-lum-unknown",
        ibcChainId = "lum-network-",
        chainIcon = R.drawable.chain_lumnetwork,
        chainTitle = R.string.str_lum_net,
        chainAlterTitle = R.string.str_lum_main,
        mainToken = Tokens.LUM,
        chainColor = R.color.colorLum,
        mnemonicBackground = R.drawable.box_round_lum,
        chainBackground = R.color.colorTransBgLum,
        chainTabColor = R.color.color_tab_myvalidator_lum,
        coingeckoUrl = "https://www.coingecko.com/en/coins/lum-network",
        explorerUrl = "https://www.mintscan.io/lum/",
        blockTime = BigDecimal("5.7210"),
        historyApiUrl = "https://api-lum.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-lum-app.cosmostation.io"),
        pathProvider = MultiPathProvider(880, mapOf(0 to 118)),
        gasRateProvider = GasRateProvider("0.001"),
        gasFeeEstimateCalculator = GasFeeEstimateCalculator(gasRate = "0.001"),
        guide = Guide.create(
            guideIcon = R.drawable.infoicon_lumnetwork,
            guideTitle = R.string.str_front_guide_title_lum,
            guideMessage = R.string.str_front_guide_msg_lum,
            buttonLink1 = "https://lum.network/",
            buttonLink2 = "https://medium.com/lum-network"
        ),
        monikerImageTag = "lum-network"
    )

    object CHIHUAHUA_MAIN : BaseChain(
        chainName = "chihuahua-mainnet",
        chainAddressPrefix = "chihuahua",
        mintScanChainName = "chihuahua",
        ibcChainId = "chihuahua-",
        chainIcon = R.drawable.chain_chihuahua,
        chainTitle = R.string.str_chihuahua_net,
        chainAlterTitle = R.string.str_chihuahua_main,
        mainToken = Tokens.HUAHUA,
        chainColor = R.color.colorChihuahua,
        mnemonicBackground = R.drawable.box_round_chihuahua,
        chainBackground = R.color.colorTransBgChihuahua,
        chainTabColor = R.color.color_tab_myvalidator_chihuahua,
        coingeckoUrl = "https://www.coingecko.com/en/coins/chihuahua-chain",
        explorerUrl = "https://www.mintscan.io/chihuahua/",
        blockTime = BigDecimal("5.8172"),
        historyApiUrl = "https://api-chihuahua.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-chihuahua-app.cosmostation.io"),
        gasProvider = ChihuahuaGasProvider,
        gasRateProvider = GasRateProvider("0.00035", "0.0035", "0.035"),
        gasFeeEstimateCalculator = GasFeeEstimateCalculator(gasRate = "0.035"),
        guide = Guide.create(
            guideIcon = R.drawable.infoicon_chihuahua,
            guideTitle = R.string.str_front_guide_title_chihuahua,
            guideMessage = R.string.str_front_guide_msg_chihuahua,
            buttonLink1 = "https://chi.huahua.wtf/",
            buttonLink2 = "https://chi.huahua.wtf"
        )
    )

    object AXELAR_MAIN : BaseChain(
        chainName = "axelar-mainnet",
        chainAddressPrefix = "axelar",
        mintScanChainName = "axelar",
        ibcChainId = "axelar-",
        chainIcon = R.drawable.chain_axelar,
        chainTitle = R.string.str_axelar_net,
        chainAlterTitle = R.string.str_axelar_main,
        mainToken = Tokens.AXL,
        chainColor = R.color.colorAxelar,
        mnemonicBackground = R.drawable.box_round_axelar,
        chainBackground = R.color.colorTransBgAxelar,
        chainTabColor = R.color.color_tab_myvalidator_axelar,
        floatButtonColor = R.color.colorBlack,
        coingeckoUrl = "",
        explorerUrl = "https://www.mintscan.io/axelar/",
        blockTime = BigDecimal("5.5596"),
        historyApiUrl = "https://api-axelar.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-axelar-app.cosmostation.io"),
        gasRateProvider = GasRateProvider("0.05"),
        gasFeeEstimateCalculator = GasFeeEstimateCalculator(gasRate = "0.05"),
        guide = Guide.create(
            guideIcon = R.drawable.infoicon_axelar,
            guideTitle = R.string.str_front_guide_title_axelar,
            guideMessage = R.string.str_front_guide_msg_axelar,
            buttonLink1 = "https://axelar.network/",
            buttonLink2 = "https://axelar.network/blog"
        )
    )

    object KONSTELL_MAIN : BaseChain(
        chainName = "konstellation-mainnet",
        chainAddressPrefix = "darc",
        mintScanChainName = "konstellation",
        ibcChainId = "darchub",
        chainIcon = R.drawable.chain_konstellation,
        chainTitle = R.string.str_konstellation_net,
        chainAlterTitle = R.string.str_konstellation_main,
        mainToken = Tokens.DARC,
        chainColor = R.color.colorKonstellation,
        mnemonicBackground = R.drawable.box_round_konstellattion,
        chainBackground = R.color.colorTransBgKonstellation,
        chainTabColor = R.color.color_tab_myvalidator_konstellation,
        floatButtonColor = R.color.colorKonstellation,
        floatButtonBackground = R.color.colorKonstellation3,
        coingeckoUrl = "https://www.coingecko.com/en/coins/konstellation",
        explorerUrl = "https://www.mintscan.io/konstellation/",
        blockTime = BigDecimal("5.376"),
        historyApiUrl = "https://api-konstellation.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-konstellation-app.cosmostation.io"),
        gasRateProvider = GasRateProvider("0.0001", "0.001", "0.01"),
        gasFeeEstimateCalculator = GasFeeEstimateCalculator(gasRate = "0.01"),
        guide = Guide.create(
            guideIcon = R.drawable.infoicon_konstellation,
            guideTitle = R.string.str_front_guide_title_konstellation,
            guideMessage = R.string.str_front_guide_msg_konstellation,
            buttonLink1 = "https://konstellation.tech/",
            buttonLink2 = "https://konstellation.medium.com/"
        )
    )

    object UMEE_MAIN : BaseChain(
        chainName = "umee-mainnet",
        chainAddressPrefix = "umee",
        mintScanChainName = "umee",
        ibcChainId = "umee-",
        chainIcon = R.drawable.chain_umee,
        chainTitle = R.string.str_umee_net,
        chainAlterTitle = R.string.str_umee_main,
        mainToken = Tokens.UMEE,
        chainColor = R.color.colorUmee,
        mnemonicBackground = R.drawable.box_round_umee,
        chainBackground = R.color.colorTransBgUmee,
        chainTabColor = R.color.color_tab_myvalidator_umee,
        coingeckoUrl = "https://www.coingecko.com/en/coins/umee",
        explorerUrl = "https://www.mintscan.io/umee/",
        blockTime = BigDecimal("5.658"),
        historyApiUrl = "https://api-umee.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-umee-app.cosmostation.io"),
        gasRateProvider = GasRateProvider("0.000", "0.001", "0.005"),
        gasFeeEstimateCalculator = GasFeeEstimateCalculator.ZERO,
        guide = Guide.create(
            guideIcon = R.drawable.infoicon_umee,
            guideTitle = R.string.str_front_guide_title_umee,
            guideMessage = R.string.str_front_guide_msg_umee,
            buttonLink1 = "https://umee.cc/",
            buttonLink2 = "https://medium.com/umeeblog"
        )
    )

    object EVMOS_MAIN : BaseChain(
        chainName = "evmos-mainnet",
        chainAddressPrefix = "evmos",
        mintScanChainName = "evmos",
        ibcChainId = "evmos",
        chainIcon = R.drawable.chain_evmos,
        chainTitle = R.string.str_evmos_net,
        chainAlterTitle = R.string.str_evmos_main,
        mainToken = Tokens.EVMOS,
        chainColor = R.color.colorEvmos,
        mnemonicBackground = R.drawable.box_round_evmos,
        chainBackground = R.color.colorTransBgEvmos,
        chainTabColor = R.color.color_tab_myvalidator_evmos,
        floatButtonColor = R.color.colorEvmos,
        floatButtonBackground = R.color.colorBlack,
        coingeckoUrl = "https://www.coingecko.com/en/coins/evmos",
        explorerUrl = "https://www.mintscan.io/evmos/",
        blockTime = BigDecimal("5.824"),
        historyApiUrl = "https://api-evmos.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-evmos-app.cosmostation.io"),
        pathProvider = PathProvider(60),
        gasRateProvider = GasRateProvider.ZERO,
        gasFeeEstimateCalculator = GasFeeEstimateCalculator.ZERO,
        guide = Guide.create(
            guideIcon = R.drawable.infoicon_evmos,
            guideTitle = R.string.str_front_guide_title_evmos,
            guideMessage = R.string.str_front_guide_msg_evmos,
            buttonLink1 = "https://evmos.org/",
            buttonLink2 = "https://evmos.blog/"
        )
    )

    object CUDOS_MAIN : BaseChain(
        chainName = "cudos-mainnet",
        chainAddressPrefix = "cudos",
        mintScanChainName = "cudos",
        ibcChainId = "cudos-",
        chainIcon = R.drawable.chain_cudos,
        chainTitle = R.string.str_cudos_net,
        chainAlterTitle = R.string.str_cudos_main,
        mainToken = Tokens.CUDOS,
        chainColor = R.color.colorCudos,
        mnemonicBackground = R.drawable.box_round_cudos,
        chainBackground = R.color.colorTransBgCudos,
        chainTabColor = R.color.color_tab_myvalidator_cudos,
        historyApiUrl = "https://api-cudos-testnet.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-cudos-testnet.cosmostation.io"),
        coingeckoUrl = "https://www.coingecko.com/en/coins/cudos",
        explorerUrl = "https://www.mintscan.io/cudos/",
        gasRateProvider = GasRateProvider.ZERO,
        gasFeeEstimateCalculator = GasFeeEstimateCalculator.ZERO,
        guide = Guide.create(
            guideIcon = R.drawable.infoicon_cudos,
            guideTitle = R.string.str_front_guide_title_cudos,
            guideMessage = R.string.str_front_guide_msg_cudos,
            buttonLink1 = "https://www.cudos.org/",
            buttonLink2 = "https://www.cudos.org/blog/"
        ),
        isSupported = false,
        isTestNet = true
    )

    object PROVENANCE_MAIN : BaseChain(
        chainName = "provenance-mainnet",
        chainAddressPrefix = "pb",
        mintScanChainName = "provenance",
        ibcChainId = "pio-mainnet-",
        chainIcon = R.drawable.chain_provenance,
        chainTitle = R.string.str_provenance_net,
        chainAlterTitle = R.string.str_provenance_main,
        mainToken = Tokens.HASH,
        chainColor = R.color.colorProvenance,
        mnemonicBackground = R.drawable.box_round_provenance,
        chainBackground = R.color.colorTransBgProvenance,
        chainTabColor = R.color.color_tab_myvalidator_provenance,
        coingeckoUrl = "https://www.coingecko.com/en/coins/provenance-blockchain",
        explorerUrl = "https://www.mintscan.io/provenance/",
        blockTime = BigDecimal("6.3061"),
        historyApiUrl = "https://api-provenance.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-provenance-app.cosmostation.io"),
        pathProvider = PathProvider(505),
        gasRateProvider = GasRateProvider("2000.00"),
        gasFeeEstimateCalculator = GasFeeEstimateCalculator(gasRate = "2000.00"),
        guide = Guide.create(
            guideIcon = R.drawable.infoicon_provenance,
            guideTitle = R.string.str_front_guide_title_provenance,
            guideMessage = R.string.str_front_guide_msg_provenance,
            buttonLink1 = "https://www.provenance.io/",
            buttonLink2 = "https://www.provenance.io/blog/"
        )
    )

    object CERBERUS_MAIN : BaseChain(
        chainName = "cerberus-mainnet",
        chainAddressPrefix = "cerberus",
        mintScanChainName = "cerberus",
        ibcChainId = "cerberus-",
        chainIcon = R.drawable.chain_cerberus,
        chainTitle = R.string.str_cerberus_net,
        chainAlterTitle = R.string.str_cerberus_main,
        mainToken = Tokens.CRBRUS,
        chainColor = R.color.colorCerberus,
        mnemonicBackground = R.drawable.box_round_cerberus,
        chainBackground = R.color.colorTransBgCerberus,
        chainTabColor = R.color.color_tab_myvalidator_cerberus,
        coingeckoUrl = "https://www.coingecko.com/en/coins/cerberus",
        explorerUrl = "https://www.mintscan.io/cerberus/",
        blockTime = BigDecimal("5.9666"),
        historyApiUrl = "https://api-cerberus.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-cerberus-app.cosmostation.io"),
        gasRateProvider = GasRateProvider.ZERO,
        gasFeeEstimateCalculator = GasFeeEstimateCalculator.ZERO,
        guide = Guide.create(
            guideIcon = R.drawable.infoicon_cerberus,
            guideTitle = R.string.str_front_guide_title_cerberus,
            guideMessage = R.string.str_front_guide_msg_cerberus,
            buttonLink1 = "https://cerberus.zone/",
            buttonLink2 = "https://medium.com/@cerberus_zone"
        )
    )

    object OMNIFLIX_MAIN : BaseChain(
        chainName = "omniflix-mainnet",
        chainAddressPrefix = "omniflix",
        mintScanChainName = "omniflix",
        ibcChainId = "omniflixhub-",
        chainIcon = R.drawable.chain_omniflix,
        chainTitle = R.string.str_omniflix_net,
        chainAlterTitle = R.string.str_omniflix_main,
        mainToken = Tokens.FLIX,
        chainColor = R.color.colorOmniflix,
        mnemonicBackground = R.drawable.box_round_omniflix,
        chainBackground = R.color.colorTransBgOmniflix,
        chainTabColor = R.color.color_tab_myvalidator_omniflix,
        coingeckoUrl = "",
        explorerUrl = "https://www.mintscan.io/omniflix/",
        blockTime = BigDecimal("5.7970"),
        historyApiUrl = "https://api-omniflix.cosmostation.io/",
        grpcApiHost = ApiHost.from("lcd-omniflix-app.cosmostation.io"),
        gasRateProvider = GasRateProvider("0.001"),
        gasFeeEstimateCalculator = GasFeeEstimateCalculator(gasRate = "0.001"),
        guide = Guide.create(
            guideIcon = R.drawable.infoicon_omniflix,
            guideTitle = R.string.str_front_guide_title_omniflix,
            guideMessage = R.string.str_front_guide_msg_omniflix,
            buttonLink1 = "https://omniflix.network/",
            buttonLink2 = "https://blog.omniflix.network/"
        )
    )

    object COSMOS_TEST : BaseChain(
        chainName = "cosmos-testnet,",
        aliases = arrayOf("stargate-final"),
        chainAddressPrefix = "cosmos",
        mintScanChainName = "cosmos-testnet",
        chainIcon = R.drawable.chain_test_cosmos,
        chainTitle = R.string.str_cosmos_test_net,
        chainAlterTitle = R.string.str_cosmos_test,
        mainToken = Tokens.MUON,
        chainColor = R.color.colorAtom,
        mnemonicBackground = R.drawable.box_round_atom,
        chainBackground = R.color.colorTransBgCosmos,
        chainTabColor = R.color.color_tab_myvalidator,
        coingeckoUrl = "",
        explorerUrl = "https://testnet.mintscan.io/cosmos/",
        blockTime = BigDecimal("7.6597"),
        historyApiUrl = "https://api-office.cosmostation.io/stargate-final/",
        grpcApiHost = ApiHost.from("lcd-office.cosmostation.io:10300"),
        isSupported = false,
        isTestNet = true,
        gasRateProvider = GasRateProvider.DEFAULT,
        guide = Guide.create(
            guideIcon = R.drawable.guide_img,
            guideTitle = R.string.str_front_guide_title,
            guideMessage = R.string.str_front_guide_msg,
            buttonText1 = R.string.str_guide,
            buttonText2 = R.string.str_faq,
            buttonLink1 = "https://cosmos.network/",
            buttonLink2 = "https://guide.cosmostation.io/app_wallet_en.html"
        )
    )

    object IMVERSED_TEST : BaseChain(
        chainName = "imversed-testnet-1",
        chainAddressPrefix = "imv",
        chainIcon = R.drawable.imversed,
        chainTitle = R.string.str_imversed_test_net,
        chainAlterTitle = R.string.str_imversed_test,
        mainToken = Tokens.IMV,
        chainColor = R.color.colorImversed,
        mnemonicBackground = R.drawable.box_round_imversed,
        chainBackground = R.color.colorTransBgImversed,
        chainTabColor = R.color.color_tab_myvalidator_imversed,
        grpcApiHost = ApiHost.from("qt.imversed.com"),
        pathProvider = PathProvider(60),
        gasProvider = ImversedGasProvider,
        gasRateProvider = GasRateProvider.DEFAULT,
        coingeckoUrl = "https://www.coingecko.com/en/coins/imversed",
        explorerUrl = "https://tex-t.imversed.com/",
        guide = Guide.create(
            guideIcon = R.drawable.infoicon_imversed,
            guideTitle = R.string.str_front_guide_title_imversed,
            guideMessage = R.string.str_front_guide_msg_imversed,
            buttonLink1 = "https://imversed.com",
            buttonLink2 = "https://imversed.com"
        ),
        isTestNet = true
    )

    object IRIS_TEST : BaseChain(
        chainName = "iris-testnet",
        aliases = arrayOf("bifrost-2"),
        chainAddressPrefix = "iaa",
        chainIcon = R.drawable.chain_test_iris,
        chainTitle = R.string.str_iris_test_net,
        chainAlterTitle = R.string.str_iris_test,
        mainToken = Tokens.BIF,
        chainColor = R.color.colorIris,
        mnemonicBackground = R.drawable.box_round_darkgray,
        chainBackground = R.color.colorTransBgIris,
        chainTabColor = R.color.color_tab_myvalidator_iris,
        coingeckoUrl = "",
        explorerUrl = "https://testnet.mintscan.io/iris/",
        blockTime = BigDecimal("6.7884"),
        historyApiUrl = "https://api-office.cosmostation.io/bifrost/",
        grpcApiHost = ApiHost.from("lcd-office.cosmostation.io:9095"),
        gasRateProvider = GasRateProvider("0.002", "0.02", "0.2"),
        gasFeeEstimateCalculator = GasFeeEstimateCalculator(gasRate = "0.2"),
        guide = Guide.create(
            guideIcon = R.drawable.irisnet_img,
            guideTitle = R.string.str_front_guide_title_iris,
            guideMessage = R.string.str_front_guide_msg_iris,
            buttonLink1 = "https://www.irisnet.org/",
            buttonLink2 = "https://medium.com/irisnet-blog"
        ),
        isSupported = false,
        isTestNet = true
    )

    object OK_TEST : BaseChain(
        chainName = "okexchain-testnet",
        chainAddressPrefix = "0x",
        chainIcon = R.drawable.chain_okx,
        chainTitle = R.string.str_ok_test_net,
        chainAlterTitle = R.string.str_okex_test,
        mainToken = Tokens.OKT,
        chainColor = R.color.colorOK,
        mnemonicBackground = R.drawable.box_round_okex,
        chainBackground = R.color.colorTransBgOkex,
        chainTabColor = R.color.color_tab_myvalidator_ok,
        coingeckoUrl = "",
        explorerUrl = "https://www.oklink.com/oec/",
        isSupported = false,
        isTestNet = true
    )

    object RIZON_TEST : BaseChain(
        chainName = "rizon-testnet2",
        chainAddressPrefix = "rizon",
        chainIcon = R.drawable.chain_rizon,
        chainTitle = R.string.str_rizon_test_net,
        chainAlterTitle = R.string.str_rizon_test,
        mainToken = Tokens.ATOLO,
        chainColor = R.color.colorRizon,
        mnemonicBackground = R.drawable.box_round_rizon,
        chainBackground = R.color.colorTransBgRizon,
        chainTabColor = R.color.color_tab_myvalidator_rizon,
        historyApiUrl = "https://api-rizon-testnet.cosmostation.io/",
        coingeckoUrl = "",
        explorerUrl = "https://testnet.mintscan.io/rizon/",
        isSupported = false,
        isTestNet = true
    )

    object ALTHEA_TEST : BaseChain(
        chainName = "althea-testnet",
        chainAddressPrefix = "althea",
        chainIcon = R.drawable.chain_althea,
        chainTitle = R.string.str_althea_test_net,
        chainAlterTitle = R.string.str_althea_test,
        mainToken = Tokens.ALTG,
        chainColor = R.color.colorAlthea,
        mnemonicBackground = R.drawable.box_round_darkgray,
        chainBackground = R.color.colorTransBgAlthea,
        chainTabColor = R.color.color_tab_myvalidator_althea,
        historyApiUrl = "https://api-office.cosmostation.io/althea-testnet2v1/",
        grpcApiHost = ApiHost.from("lcd-office.cosmostation.io:20100"),
        coingeckoUrl = "",
        explorerUrl = "",
        isSupported = false,
        isTestNet = true
    )

    val mainDenom: String get() = mainToken.denom   // XXX
    val divideDecimal: Int get() = mainToken.divideDecimal  // XXX

    fun hasChainName(chainName: String): Boolean {
        return this.chainName == chainName || aliases.any { it == chainName }
    }

    fun getMonikerImageLink(address: String): String {
        return "https://raw.githubusercontent.com/cosmostation/cosmostation_token_resource/master/moniker/$monikerImageTag/$address.png"
    }

    fun getRelayerImageLink(): String {
        return "https://raw.githubusercontent.com/cosmostation/cosmostation_token_resource/master/relayer/$relayerImageTag.png"
    }

    companion object {

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

        @JvmStatic
        fun getChainByIbcChainId(chainId: String): BaseChain? {
            return chains.find { chain ->
                chain.ibcChainId.isNotEmpty() && chainId.contains(chain.ibcChainId)
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