package wannabit.io.cosmostaion.base

import android.text.TextUtils
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.fulldive.wallet.extensions.or
import com.fulldive.wallet.extensions.safe
import wannabit.io.cosmostaion.R
import wannabit.io.cosmostaion.utils.WKey
import java.math.BigDecimal

enum class BaseChain constructor(
    names: String, // names: String - format "{main chain name}, aliases"
    val chainAddressPrefix: String,
    @DrawableRes val chainIcon: Int,
    @StringRes val chainTitle: Int,
    @StringRes val chainAlterTitle: Int,
    val mainDenom: String,
    val fullNameCoin: String,
    val ticker: String = "",
    @ColorRes val denomColor: Int,
    @DrawableRes val coinIcon: Int,
    @DrawableRes val mnemonicBackground: Int,
    @StringRes val symbolTitle: Int,
    @ColorRes val chainBackground: Int,
    @ColorRes val chainTabColor: Int,
    @ColorRes val floatButtonColor: Int = R.color.colorWhite,
    @ColorRes floatButtonBackground: Int = 0,
    val historyApiUrl: String = "",
    grpcApiUrl: String = "",
    blockTimeString: String = "",
    val pathConfig: String = "{\"default\":\"118\"}",
    val isSupported: Boolean = true,
    val isTestNet: Boolean = false,
    val isGRPC: Boolean = true
) {
    // chain_id is checked on-chain. no need update chain version  21.03.20
    COSMOS_MAIN(
        names = "cosmoshub-mainnet,cosmoshub-1,cosmoshub-2,cosmoshub-3,cosmoshub-4",
        chainAddressPrefix = "cosmos1",
        chainIcon = R.drawable.cosmos_wh_main,
        chainTitle = R.string.str_cosmos_hub,
        chainAlterTitle = R.string.str_cosmos,
        mainDenom = "uatom",
        fullNameCoin = "Cosmos Staking Coin",
        ticker = "atom",
        denomColor = R.color.colorAtom,
        coinIcon = R.drawable.atom_ic,
        mnemonicBackground = R.drawable.box_round_atom,
        symbolTitle = R.string.str_atom_c,
        chainBackground = R.color.colorTransBgCosmos,
        chainTabColor = R.color.color_tab_myvalidator,
        blockTimeString = "7.6597",
        historyApiUrl = "https://api.cosmostation.io/",
        grpcApiUrl = "lcd-cosmos-app-and.cosmostation.io"
    ),
    IMVERSED_MAIN(
        names = "imversed-canary",
        chainAddressPrefix = "imv1",
        chainIcon = R.drawable.imversed,
        chainTitle = R.string.str_imversed_canary_net,
        chainAlterTitle = R.string.str_imversed_canary,
        mainDenom = "nimv",
        fullNameCoin = "Imversed Staking Coin",
        denomColor = R.color.colorImversed,
        coinIcon = R.drawable.imversed_token_img,
        mnemonicBackground = R.drawable.box_round_imversed,
        symbolTitle = R.string.str_imversed_c,
        chainBackground = R.color.colorTransBgImversed,
        chainTabColor = R.color.color_tab_myvalidator_imversed,
        grpcApiUrl = "qc.imversed.com"
    ),
    IRIS_MAIN(
        names = "irishub-mainnet,irishub,irishub-1",
        chainAddressPrefix = "iaa1",
        chainIcon = R.drawable.iris_wh,
        chainTitle = R.string.str_iris_net,
        chainAlterTitle = R.string.str_iris,
        mainDenom = "uiris",
        fullNameCoin = "Iris Staking Coin",
        ticker = "iris",
        denomColor = R.color.colorIris,
        coinIcon = R.drawable.iris_toket_img,
        mnemonicBackground = R.drawable.box_round_iris,
        symbolTitle = R.string.str_iris_c,
        chainBackground = R.color.colorTransBgIris,
        chainTabColor = R.color.color_tab_myvalidator_iris,
        blockTimeString = "6.7884",
        historyApiUrl = "https://api-iris.cosmostation.io/",
        grpcApiUrl = "lcd-iris-app.cosmostation.io"
    ),
    IOV_MAIN(
        names = "iov-mainnet,iov-mainnet-2",
        chainAddressPrefix = "star1",
        chainIcon = R.drawable.chain_starname,
        chainTitle = R.string.str_iov_net,
        chainAlterTitle = R.string.str_iov,
        mainDenom = "uiov",
        fullNameCoin = "Starname Staking Coin",
        ticker = "iov",
        denomColor = R.color.colorIov,
        coinIcon = R.drawable.iov_token_img,
        mnemonicBackground = R.drawable.box_round_iov,
        symbolTitle = R.string.str_iov_c,
        chainBackground = R.color.colorTransBgStarname,
        chainTabColor = R.color.color_tab_myvalidator_iov,
        blockTimeString = "6.0124",
        historyApiUrl = "https://api-iov.cosmostation.io/",
        grpcApiUrl = "lcd-iov-app.cosmostation.io",
        pathConfig = "{\"default\":\"234\"}"
    ),
    BNB_MAIN(
        names = "binance-mainnet,Binance-Chain-Tigris",
        chainAddressPrefix = "bnb1",
        chainIcon = R.drawable.binance_ch_img,
        chainTitle = R.string.str_binance_net,
        chainAlterTitle = R.string.str_binance,
        mainDenom = "BNB",
        fullNameCoin = "Binance Chain Native Coin",
        ticker = "bnb",
        denomColor = R.color.colorBnb,
        coinIcon = R.drawable.bnb_token_img,
        mnemonicBackground = R.drawable.box_round_bnb,
        symbolTitle = R.string.str_bnb_c,
        chainBackground = R.color.colorTransBgBinance,
        chainTabColor = R.color.color_tab_myvalidator,
        blockTimeString = "0.4124",
        pathConfig = "{\"default\":\"714\"}",
        isGRPC = false
    ),
    KAVA_MAIN(
        names = "kava-mainnet,kava-1,kava-2,kava-3,kava-4,kava-5,kava-6",
        chainAddressPrefix = "kava1",
        chainIcon = R.drawable.kava_img,
        chainTitle = R.string.str_kava_net,
        chainAlterTitle = R.string.str_kava,
        mainDenom = "ukava",
        fullNameCoin = "Kava Staking Coin",
        ticker = "kava",
        denomColor = R.color.colorKava,
        coinIcon = R.drawable.kava_token_img,
        mnemonicBackground = R.drawable.box_round_kava,
        symbolTitle = R.string.str_kava_c,
        chainBackground = R.color.colorTransBgKava,
        chainTabColor = R.color.color_tab_myvalidator_kava,
        blockTimeString = "6.7262",
        historyApiUrl = "https://api-kava.cosmostation.io/",
        grpcApiUrl = "lcd-kava-app.cosmostation.io",
        pathConfig = "{\"default\":\"459\", \"0\":\"118\"}"
    ),
    BAND_MAIN(
        names = "band-mainnet,band-wenchang-mainnet,band-guanyu-mainnet",
        chainAddressPrefix = "band1",
        chainIcon = R.drawable.band_chain_img,
        chainTitle = R.string.str_band_chain,
        chainAlterTitle = R.string.str_band,
        mainDenom = "uband",
        fullNameCoin = "Band Staking Coin",
        ticker = "band",
        denomColor = R.color.colorBand,
        coinIcon = R.drawable.band_token_img,
        mnemonicBackground = R.drawable.box_round_band,
        symbolTitle = R.string.str_band_c,
        chainBackground = R.color.colorTransBgBand,
        chainTabColor = R.color.color_tab_myvalidator_band,
        blockTimeString = "3.0236",
        historyApiUrl = "https://api-band.cosmostation.io/",
        grpcApiUrl = "lcd-band-app.cosmostation.io",
        pathConfig = "{\"default\":\"494\"}"
    ),
    CERTIK_MAIN(
        names = "shentu-mainnet,shentu-1,shentu-2",
        chainAddressPrefix = "certik1",
        chainIcon = R.drawable.certik_chain_img,
        chainTitle = R.string.str_certik_chain,
        chainAlterTitle = R.string.str_certik_main,
        mainDenom = "uctk",
        fullNameCoin = "Certik Staking Coin",
        ticker = "ctk",
        denomColor = R.color.colorCertik,
        coinIcon = R.drawable.certik_token_img,
        mnemonicBackground = R.drawable.box_round_certik,
        symbolTitle = R.string.str_ctk_c,
        chainBackground = R.color.colorTransBgCertik,
        chainTabColor = R.color.color_tab_myvalidator_certik,
        blockTimeString = "5.9740",
        historyApiUrl = "https://api-certik.cosmostation.io/",
        grpcApiUrl = "lcd-certik-app.cosmostation.io"
    ),
    SECRET_MAIN(
        names = "secret-mainnet,secret-2",
        chainAddressPrefix = "secret1",
        chainIcon = R.drawable.chainsecret,
        chainTitle = R.string.str_secret_chain,
        chainAlterTitle = R.string.str_secret_main,
        mainDenom = "uscrt",
        fullNameCoin = "Secret Native Coin",
        ticker = "scrt",
        denomColor = R.color.colorSecret,
        coinIcon = R.drawable.tokensecret,
        mnemonicBackground = R.drawable.box_round_secret,
        symbolTitle = R.string.str_scrt_c,
        chainBackground = R.color.colorTransBgSecret,
        chainTabColor = R.color.color_tab_myvalidator_secret,
        floatButtonBackground = R.color.colorSecret2,
        blockTimeString = "6.0408",
        historyApiUrl = "https://api-secret.cosmostation.io/",
        grpcApiUrl = "lcd-secret.cosmostation.io",
        pathConfig = "{\"default\":\"529\",\"0\":\"118\"}"
    ),
    AKASH_MAIN(
        names = "akashnet-mainnet, akashnet-1,akashnet-2",
        chainAddressPrefix = "akash1",
        chainIcon = R.drawable.akash_chain_img,
        chainTitle = R.string.str_akash_chain,
        chainAlterTitle = R.string.str_akash_main,
        mainDenom = "uakt",
        fullNameCoin = "Akash Staking Coin",
        ticker = "akt",
        denomColor = R.color.colorAkash,
        coinIcon = R.drawable.akash_token_img,
        mnemonicBackground = R.drawable.box_round_akash,
        symbolTitle = R.string.str_akt_c,
        chainBackground = R.color.colorTransBgAkash,
        chainTabColor = R.color.color_tab_myvalidator_akash,
        blockTimeString = "6.4526",
        historyApiUrl = "https://api-akash.cosmostation.io/",
        grpcApiUrl = "lcd-akash-app.cosmostation.io"
    ),
    OKEX_MAIN(
        names = "okexchain-mainnet,okexchain-66",
        chainAddressPrefix = "0x",
        chainIcon = R.drawable.chain_okx,
        chainTitle = R.string.str_ok_net,
        chainAlterTitle = R.string.str_okex_main,
        mainDenom = "okt",
        fullNameCoin = "OEC Staking Coin",
        ticker = "okb",
        denomColor = R.color.colorOK,
        coinIcon = R.drawable.token_okx,
        mnemonicBackground = R.drawable.box_round_okex,
        symbolTitle = R.string.str_ok_c,
        chainBackground = R.color.colorTransBgOkex,
        chainTabColor = R.color.color_tab_myvalidator_ok,
        blockTimeString = "4.0286",
        pathConfig = "{\"default\":\"60\",\"0\":\"996\",\"1\":\"996\"}",
        isGRPC = false
    ),
    PERSIS_MAIN(
        names = "persistence-mainnet",
        chainAddressPrefix = "persistence1",
        chainIcon = R.drawable.chainpersistence,
        chainTitle = R.string.str_persis_net,
        chainAlterTitle = R.string.str_persis_main,
        mainDenom = "uxprt",
        fullNameCoin = "Persistence Staking Coin",
        ticker = "xprt",
        denomColor = R.color.colorPersis,
        coinIcon = R.drawable.tokenpersistence,
        mnemonicBackground = R.drawable.box_round_persis,
        symbolTitle = R.string.str_xprt_c,
        chainBackground = R.color.colorTransBgPersis,
        chainTabColor = R.color.color_tab_myvalidator_persis,
        floatButtonColor = R.color.colorPersis,
        floatButtonBackground = R.color.colorBlack,
        blockTimeString = "5.7982",
        historyApiUrl = "https://api-persistence.cosmostation.io/",
        grpcApiUrl = "lcd-persistence-app.cosmostation.io",
        pathConfig = "{\"default\":\"750\"}"
    ),
    SENTINEL_MAIN(
        names = "sentinel-mainnet",
        chainAddressPrefix = "sent1",
        chainIcon = R.drawable.chainsentinel,
        chainTitle = R.string.str_sentinel_net,
        chainAlterTitle = R.string.str_sentinel_main,
        mainDenom = "udvpn",
        fullNameCoin = "Sentinel Native Coin",
        ticker = "dvpn",
        denomColor = R.color.colorSentinel,
        coinIcon = R.drawable.tokensentinel,
        mnemonicBackground = R.drawable.box_round_sentinel,
        symbolTitle = R.string.str_dvpn_c,
        chainBackground = R.color.colorTransBgSentinel,
        chainTabColor = R.color.color_tab_myvalidator_sentinel,
        floatButtonBackground = R.color.colorSentinel3,
        blockTimeString = "6.3113",
        historyApiUrl = "https://api-sentinel.cosmostation.io/",
        grpcApiUrl = "lcd-sentinel-app.cosmostation.io"
    ),
    FETCHAI_MAIN(
        names = "fetchai-mainnet",
        chainAddressPrefix = "fetch1",
        chainIcon = R.drawable.chainfetchai,
        chainTitle = R.string.str_fetch_net,
        chainAlterTitle = R.string.str_fetch_main,
        mainDenom = "afet",
        fullNameCoin = "Fetch,ai Staking Coin",
        ticker = "",  // "fet"
        denomColor = R.color.colorFetch,
        coinIcon = R.drawable.tokenfetchai,
        mnemonicBackground = R.drawable.box_round_fetch,
        symbolTitle = R.string.str_fet_c,
        chainBackground = R.color.colorTransBgFetch,
        chainTabColor = R.color.color_tab_myvalidator_fetch,
        blockTimeString = "6.0678",
        historyApiUrl = "https://api-fetchai.cosmostation.io/",
        grpcApiUrl = "lcd-fetchai-app.cosmostation.io",
        pathConfig = "{\"default\":\"60:0\",\"0\":\"118\",\"1\":\"60\",\"2\":\"60:h0\"}"
    ),
    CRYPTO_MAIN(
        names = "crytoorg-mainnet",
        chainAddressPrefix = "cro1",
        chainIcon = R.drawable.chaincrypto,
        chainTitle = R.string.str_crypto_net,
        chainAlterTitle = R.string.str_crypto_main,
        mainDenom = "basecro",
        fullNameCoin = "Cronos",
        ticker = "cro",
        denomColor = R.color.colorCryto,
        coinIcon = R.drawable.tokencrypto,
        mnemonicBackground = R.drawable.box_round_cryto,
        symbolTitle = R.string.str_cro_c,
        chainBackground = R.color.colorTransBgCryto,
        chainTabColor = R.color.color_tab_myvalidator_cryto,
        floatButtonBackground = R.color.colorCryto2,
        blockTimeString = "6.1939",
        historyApiUrl = "https://api-cryptocom.cosmostation.io/",
        grpcApiUrl = "lcd-cryptocom-app.cosmostation.io",
        pathConfig = "{\"default\":\"394\"}"
    ),
    SIF_MAIN(
        names = "sif-mainnet",
        chainAddressPrefix = "sif1",
        chainIcon = R.drawable.chainsifchain,
        chainTitle = R.string.str_sif_net,
        chainAlterTitle = R.string.str_sif_main,
        mainDenom = "rowan",
        fullNameCoin = "Sif Staking Coin",
        ticker = "rowan",
        denomColor = R.color.colorSif,
        coinIcon = R.drawable.tokensifchain,
        mnemonicBackground = R.drawable.box_round_sif,
        symbolTitle = R.string.str_sif_c,
        chainBackground = R.color.colorTransBgSif,
        chainTabColor = R.color.color_tab_myvalidator_sif,
        blockTimeString = "5.7246",
        historyApiUrl = "https://api-sifchain.cosmostation.io/",
        grpcApiUrl = "lcd-sifchain-app.cosmostation.io"
    ),
    KI_MAIN(
        names = "ki-mainnet",
        chainAddressPrefix = "ki1",
        chainIcon = R.drawable.chain_kifoundation,
        chainTitle = R.string.str_ki_net,
        chainAlterTitle = R.string.str_ki_main,
        mainDenom = "uxki",
        fullNameCoin = "KiChain Staking Coin",
        ticker = "",  // "ki"
        denomColor = R.color.colorKi,
        coinIcon = R.drawable.token_kifoundation,
        mnemonicBackground = R.drawable.box_round_ki,
        symbolTitle = R.string.str_ki_c,
        chainBackground = R.color.colorTransBgKi,
        chainTabColor = R.color.color_tab_myvalidator_ki,
        blockTimeString = "5.7571",
        historyApiUrl = "https://api-kichain.cosmostation.io/",
        grpcApiUrl = "lcd-kichain-app.cosmostation.io"
    ),
    OSMOSIS_MAIN(
        names = "osmosis-mainnet",
        chainAddressPrefix = "osmo1",
        chainIcon = R.drawable.chain_osmosis,
        chainTitle = R.string.str_osmosis_net,
        chainAlterTitle = R.string.str_osmosis_main,
        mainDenom = "uosmo",
        fullNameCoin = "Osmosis Staking Coin",
        ticker = "osmo",
        denomColor = R.color.colorOsmosis,
        coinIcon = R.drawable.token_osmosis,
        mnemonicBackground = R.drawable.box_round_osmosis,
        symbolTitle = R.string.str_osmosis_c,
        chainBackground = R.color.colorTransBgOsmosis,
        chainTabColor = R.color.color_tab_myvalidator_osmosis,
        blockTimeString = "6.5324",
        historyApiUrl = "https://api-osmosis.cosmostation.io/",
        grpcApiUrl = "lcd-osmosis-app-and.cosmostation.io"
    ),
    MEDI_MAIN(
        names = "medibloc-mainnet",
        chainAddressPrefix = "panacea1",
        chainIcon = R.drawable.chainmedibloc,
        chainTitle = R.string.str_medi_net,
        chainAlterTitle = R.string.str_medi_main,
        mainDenom = "umed",
        fullNameCoin = "Medibloc Staking Coin",
        ticker = "",  // "med"
        denomColor = R.color.colorMedi,
        coinIcon = R.drawable.tokenmedibloc,
        mnemonicBackground = R.drawable.box_round_medi,
        symbolTitle = R.string.str_medi_c,
        chainBackground = R.color.colorTransBgMedi,
        chainTabColor = R.color.color_tab_myvalidator_med,
        floatButtonColor = R.color.colorMedi,
        floatButtonBackground = R.color.colorWhite,
        blockTimeString = "5.7849",
        historyApiUrl = "https://api-medibloc.cosmostation.io/",
        grpcApiUrl = "lcd-medibloc-app.cosmostation.io",
        pathConfig = "{\"default\":\"371\"}"
    ),
    EMONEY_MAIN(
        names = "emoney-mainnet",
        chainAddressPrefix = "emoney1",
        chainIcon = R.drawable.chain_emoney,
        chainTitle = R.string.str_emoney_net,
        chainAlterTitle = R.string.str_emoney_main,
        mainDenom = "ungm",
        fullNameCoin = "E-Money Staking Coin",
        ticker = "",  // "ngm"
        denomColor = R.color.colorEmoney,
        coinIcon = R.drawable.token_emoney,
        mnemonicBackground = R.drawable.box_round_emoney,
        symbolTitle = R.string.str_ngm_c,
        chainBackground = R.color.colorTransBgEmoney,
        chainTabColor = R.color.color_tab_myvalidator_emoney,
        blockTimeString = "24.8486",
        historyApiUrl = "https://api-emoney.cosmostation.io/",
        grpcApiUrl = "lcd-emoney-app.cosmostation.io"
    ),
    RIZON_MAIN(
        names = "rizon-mainnet",
        chainAddressPrefix = "rizon1",
        chainIcon = R.drawable.chain_rizon,
        chainTitle = R.string.str_rizon_net,
        chainAlterTitle = R.string.str_rizon_main,
        mainDenom = "uatolo",
        fullNameCoin = "Rizon Staking Coin",
        ticker = "atolo",
        denomColor = R.color.colorRizon,
        coinIcon = R.drawable.token_rizon,
        mnemonicBackground = R.drawable.box_round_rizon,
        symbolTitle = R.string.str_rizon_c,
        chainBackground = R.color.colorTransBgRizon,
        chainTabColor = R.color.color_tab_myvalidator_rizon,
        blockTimeString = "5.8850",
        historyApiUrl = "https://api-rizon.cosmostation.io/",
        grpcApiUrl = "lcd-rizon-app.cosmostation.io"
    ),
    JUNO_MAIN(
        names = "juno-mainnet",
        chainAddressPrefix = "juno1",
        chainIcon = R.drawable.chain_juno,
        chainTitle = R.string.str_juno_net,
        chainAlterTitle = R.string.str_juno_main,
        mainDenom = "ujuno",
        fullNameCoin = "Juno Staking Coin",
        ticker = "",  // "ujuno"
        denomColor = R.color.colorJuno,
        coinIcon = R.drawable.token_juno,
        mnemonicBackground = R.drawable.box_round_juno,
        symbolTitle = R.string.str_juno_c,
        chainBackground = R.color.colorTransBgJuno,
        chainTabColor = R.color.color_tab_myvalidator_juno,
        blockTimeString = "6.3104",
        historyApiUrl = "https://api-juno.cosmostation.io/",
        grpcApiUrl = "lcd-juno-app.cosmostation.io"
    ),
    REGEN_MAIN(
        names = "regen-mainnet",
        chainAddressPrefix = "regen1",
        chainIcon = R.drawable.chain_regen,
        chainTitle = R.string.str_regen_net,
        chainAlterTitle = R.string.str_regen_main,
        mainDenom = "uregen",
        fullNameCoin = "Regen Staking Coin",
        ticker = "regen",
        denomColor = R.color.colorRegen,
        coinIcon = R.drawable.token_regen,
        mnemonicBackground = R.drawable.box_round_regen,
        symbolTitle = R.string.str_regen_c,
        chainBackground = R.color.colorTransBgRegen,
        chainTabColor = R.color.color_tab_myvalidator_regen,
        blockTimeString = "6.2491",
        historyApiUrl = "https://api-regen.cosmostation.io/",
        grpcApiUrl = "lcd-regen-app.cosmostation.io"
    ),
    BITCANNA_MAIN(
        names = "bitcanna-mainnet",
        chainAddressPrefix = "bcna1",
        chainIcon = R.drawable.chain_bitcanna,
        chainTitle = R.string.str_bitcanna_net,
        chainAlterTitle = R.string.str_bitcanna_main,
        mainDenom = "ubcna",
        fullNameCoin = "Bitcanna Staking Coin",
        ticker = "",  // "bcna"
        denomColor = R.color.colorBitcanna,
        coinIcon = R.drawable.token_bitcanna,
        mnemonicBackground = R.drawable.box_round_bitcanna,
        symbolTitle = R.string.str_bitcanna_c,
        chainBackground = R.color.colorTransBgBitcanna,
        chainTabColor = R.color.color_tab_myvalidator_bitcanna,
        blockTimeString = "6.0256",
        historyApiUrl = "https://api-bitcanna.cosmostation.io/",
        grpcApiUrl = "lcd-bitcanna-app.cosmostation.io"
    ),
    ALTHEA_MAIN(
        names = "althea-mainnet",
        chainAddressPrefix = "althea1",
        chainIcon = R.drawable.chain_althea,
        chainTitle = R.string.str_althea_net,
        chainAlterTitle = R.string.str_althea_main,
        mainDenom = "ualtg",
        fullNameCoin = "Althea Stacking Coin",
        denomColor = R.color.colorAlthea,
        coinIcon = R.drawable.token_althea,
        mnemonicBackground = R.drawable.box_round_althea,
        symbolTitle = R.string.str_althea_c,
        chainBackground = R.color.colorTransBgAlthea,
        chainTabColor = R.color.color_tab_myvalidator_althea,
        historyApiUrl = "https://api-althea.cosmostation.io/",
        grpcApiUrl = "lcd-althea-app.cosmostation.io"
    ),
    STARGAZE_MAIN(
        names = "stargaze-mainnet",
        chainAddressPrefix = "stars1",
        chainIcon = R.drawable.chain_stargaze,
        chainTitle = R.string.str_stargaze_net,
        chainAlterTitle = R.string.str_stargaze_main,
        mainDenom = "ustars",
        fullNameCoin = "Stargaze Staking Coin",
        ticker = "",  // "stars"
        denomColor = R.color.colorStargaze,
        coinIcon = R.drawable.token_stargaze,
        mnemonicBackground = R.drawable.box_round_stargaze,
        symbolTitle = R.string.str_stargaze_c,
        chainBackground = R.color.colorTransBgStargaze,
        chainTabColor = R.color.color_tab_myvalidator_stargaze,
        blockTimeString = "5.8129",
        historyApiUrl = "https://api-stargaze.cosmostation.io/",
        grpcApiUrl = "lcd-stargaze-app.cosmostation.io"
    ),
    GRABRIDGE_MAIN(
        names = "GravityBridge-mainnet",
        chainAddressPrefix = "gravity1",
        chainIcon = R.drawable.chain_gravitybridge,
        chainTitle = R.string.str_grabridge_net,
        chainAlterTitle = R.string.str_grabridge_main,
        mainDenom = "ugraviton",
        fullNameCoin = "G-Bridge Staking Coin",
        denomColor = R.color.colorGraBridge,
        coinIcon = R.drawable.token_gravitybridge,
        mnemonicBackground = R.drawable.box_round_grabridge,
        symbolTitle = R.string.str_grabridge_c,
        chainBackground = R.color.colorTransBgGraBridge,
        chainTabColor = R.color.color_tab_myvalidator_grabridge,
        blockTimeString = "6.4500",
        historyApiUrl = "https://api-gravity-bridge.cosmostation.io/",
        grpcApiUrl = "lcd-gravity-bridge-app.cosmostation.io"
    ),
    COMDEX_MAIN(
        names = "comdex-mainnet",
        chainAddressPrefix = "comdex1",
        chainIcon = R.drawable.chain_comdex,
        chainTitle = R.string.str_comdex_net,
        chainAlterTitle = R.string.str_comdex_main,
        mainDenom = "ucmdx",
        fullNameCoin = "Comdex Staking Coin",
        denomColor = R.color.colorComdex,
        coinIcon = R.drawable.token_comdex,
        mnemonicBackground = R.drawable.box_round_comdex,
        symbolTitle = R.string.str_comdex_c,
        chainBackground = R.color.colorTransBgComdex,
        chainTabColor = R.color.color_tab_myvalidator_comdex,
        floatButtonColor = R.color.colorComdex,
        floatButtonBackground = R.color.colorTransBgComdex,
        blockTimeString = "6.1746",
        historyApiUrl = "https://api-comdex.cosmostation.io/",
        grpcApiUrl = "lcd-comdex-app.cosmostation.io"
    ),
    INJ_MAIN(
        names = "injective-mainnet",
        chainAddressPrefix = "inj1",
        chainIcon = R.drawable.chain_injective,
        chainTitle = R.string.str_inj_net,
        chainAlterTitle = R.string.str_inj_main,
        mainDenom = "inj",
        fullNameCoin = "Injective Staking Coin",
        denomColor = R.color.colorInj,
        coinIcon = R.drawable.token_injective,
        mnemonicBackground = R.drawable.box_round_inj,
        symbolTitle = R.string.str_inj_c,
        chainBackground = R.color.colorTransBgInj,
        chainTabColor = R.color.color_tab_myvalidator_inj,
        floatButtonColor = R.color.colorBlack,
        blockTimeString = "2.4865",
        historyApiUrl = "https://api-inj.cosmostation.io/",
        grpcApiUrl = "lcd-inj-app.cosmostation.io",
        pathConfig = "{\"default\":\"60\"}"
    ),
    BITSONG_MAIN(
        names = "bitsong-mainnet",
        chainAddressPrefix = "bitsong1",
        chainIcon = R.drawable.chain_bitsong,
        chainTitle = R.string.str_bitsong_net,
        chainAlterTitle = R.string.str_bitsong_main,
        mainDenom = "ubtsg",
        fullNameCoin = "Bitsong Staking Coin",
        denomColor = R.color.colorBitsong,
        coinIcon = R.drawable.token_bitsong,
        mnemonicBackground = R.drawable.box_round_bitsong,
        symbolTitle = R.string.str_bitsong_c,
        chainBackground = R.color.colorTransBgBitsong,
        chainTabColor = R.color.color_tab_myvalidator_bitsong,
        blockTimeString = "5.9040",
        historyApiUrl = "https://api-bitsong.cosmostation.io/",
        grpcApiUrl = "lcd-bitsong-app.cosmostation.io",
        pathConfig = "{\"default\":\"639\"}"
    ),
    DESMOS_MAIN(
        names = "desmos-mainnet",
        chainAddressPrefix = "desmos1",
        chainIcon = R.drawable.chain_desmos,
        chainTitle = R.string.str_desmos_net,
        chainAlterTitle = R.string.str_desmos_main,
        mainDenom = "udsm",
        fullNameCoin = "Desmos Staking Coin",
        denomColor = R.color.colorDesmos,
        coinIcon = R.drawable.token_desmos,
        mnemonicBackground = R.drawable.box_round_desmos,
        symbolTitle = R.string.str_desmos_c,
        chainBackground = R.color.colorTransBgDesmos,
        chainTabColor = R.color.color_tab_myvalidator_desmos,
        blockTimeString = "6.1605",
        historyApiUrl = "https://api-desmos.cosmostation.io/",
        grpcApiUrl = "lcd-desmos-app.cosmostation.io",
        pathConfig = "{\"default\":\"852\"}"
    ),
    LUM_MAIN(
        names = "lum-mainnet",
        chainAddressPrefix = "lum1",
        chainIcon = R.drawable.chain_lumnetwork,
        chainTitle = R.string.str_lum_net,
        chainAlterTitle = R.string.str_lum_main,
        mainDenom = "ulum",
        fullNameCoin = "Lum Staking Coin",
        denomColor = R.color.colorLum,
        coinIcon = R.drawable.token_lum,
        mnemonicBackground = R.drawable.box_round_lum,
        symbolTitle = R.string.str_lum_c,
        chainBackground = R.color.colorTransBgLum,
        chainTabColor = R.color.color_tab_myvalidator_lum,
        blockTimeString = "5.7210",
        historyApiUrl = "https://api-lum.cosmostation.io/",
        grpcApiUrl = "lcd-lum-app.cosmostation.io",
        pathConfig = "{\"default\":\"880\",\"0\":\"118\"}"
    ),
    CHIHUAHUA_MAIN(
        names = "chihuahua-mainnet",
        chainAddressPrefix = "chihuahua1",
        chainIcon = R.drawable.chain_chihuahua,
        chainTitle = R.string.str_chihuahua_net,
        chainAlterTitle = R.string.str_chihuahua_main,
        mainDenom = "uhuahua",
        fullNameCoin = "Chihuahua Staking Coin",
        denomColor = R.color.colorChihuahua,
        coinIcon = R.drawable.token_huahua,
        mnemonicBackground = R.drawable.box_round_chihuahua,
        symbolTitle = R.string.str_chihuahua_c,
        chainBackground = R.color.colorTransBgChihuahua,
        chainTabColor = R.color.color_tab_myvalidator_chihuahua,
        blockTimeString = "5.8172",
        historyApiUrl = "https://api-chihuahua.cosmostation.io/",
        grpcApiUrl = "lcd-chihuahua-app.cosmostation.io"
    ),
    AXELAR_MAIN(
        names = "axelar-mainnet",
        chainAddressPrefix = "axelar1",
        chainIcon = R.drawable.chain_axelar,
        chainTitle = R.string.str_axelar_net,
        chainAlterTitle = R.string.str_axelar_main,
        mainDenom = "uaxl",
        fullNameCoin = "Axelar Staking Coin",
        denomColor = R.color.colorAxelar,
        coinIcon = R.drawable.token_axelar,
        mnemonicBackground = R.drawable.box_round_axelar,
        symbolTitle = R.string.str_axl_c,
        chainBackground = R.color.colorTransBgAxelar,
        chainTabColor = R.color.color_tab_myvalidator_axelar,
        floatButtonColor = R.color.colorBlack,
        blockTimeString = "5.5596",
        historyApiUrl = "https://api-axelar.cosmostation.io/",
        grpcApiUrl = "lcd-axelar-app.cosmostation.io"
    ),
    KONSTELL_MAIN(
        names = "konstellation-mainnet",
        chainAddressPrefix = "darc1",
        chainIcon = R.drawable.chain_konstellation,
        chainTitle = R.string.str_konstellation_net,
        chainAlterTitle = R.string.str_konstellation_main,
        mainDenom = "udarc",
        fullNameCoin = "Konstellation Staking Coin",
        denomColor = R.color.colorKonstellation,
        coinIcon = R.drawable.token_konstellation,
        mnemonicBackground = R.drawable.box_round_konstellattion,
        symbolTitle = R.string.str_konstellation_c,
        chainBackground = R.color.colorTransBgKonstellation,
        chainTabColor = R.color.color_tab_myvalidator_konstellation,
        floatButtonColor = R.color.colorKonstellation,
        floatButtonBackground = R.color.colorKonstellation3,
        blockTimeString = "5.376",
        historyApiUrl = "https://api-konstellation.cosmostation.io/",
        grpcApiUrl = "lcd-konstellation-app.cosmostation.io"
    ),
    UMEE_MAIN(
        names = "umee-mainnet",
        chainAddressPrefix = "umee1",
        chainIcon = R.drawable.chain_umee,
        chainTitle = R.string.str_umee_net,
        chainAlterTitle = R.string.str_umee_main,
        mainDenom = "uumee",
        fullNameCoin = "Umee Staking Coin",
        denomColor = R.color.colorUmee,
        coinIcon = R.drawable.token_umee,
        mnemonicBackground = R.drawable.box_round_umee,
        symbolTitle = R.string.str_umee_c,
        chainBackground = R.color.colorTransBgUmee,
        chainTabColor = R.color.color_tab_myvalidator_umee,
        blockTimeString = "5.658",
        historyApiUrl = "https://api-umee.cosmostation.io/",
        grpcApiUrl = "lcd-umee-app.cosmostation.io"
    ),
    EVMOS_MAIN(
        names = "evmos-mainnet",
        chainAddressPrefix = "evmos1",
        chainIcon = R.drawable.chain_evmos,
        chainTitle = R.string.str_evmos_net,
        chainAlterTitle = R.string.str_evmos_main,
        mainDenom = "aevmos",
        fullNameCoin = "Evmos Staking Coin",
        denomColor = R.color.colorEvmos,
        coinIcon = R.drawable.token_evmos,
        mnemonicBackground = R.drawable.box_round_evmos,
        symbolTitle = R.string.str_evmos_c,
        chainBackground = R.color.colorTransBgEvmos,
        chainTabColor = R.color.color_tab_myvalidator_evmos,
        floatButtonColor = R.color.colorEvmos,
        floatButtonBackground = R.color.colorBlack,
        blockTimeString = "5.824",
        historyApiUrl = "https://api-evmos.cosmostation.io/",
        grpcApiUrl = "lcd-evmos-app.cosmostation.io",
        pathConfig = "{\"default\":\"60\"}"
    ),
    CUDOS_MAIN(
        names = "cudos-mainnet",
        chainAddressPrefix = "cudos1",
        chainIcon = R.drawable.chain_cudos,
        chainTitle = R.string.str_cudos_net,
        chainAlterTitle = R.string.str_cudos_main,
        mainDenom = "acudos",
        fullNameCoin = "Cudos Staking Coin",
        denomColor = R.color.colorCudos,
        coinIcon = R.drawable.token_cudos,
        mnemonicBackground = R.drawable.box_round_cudos,
        symbolTitle = R.string.str_cudos_c,
        chainBackground = R.color.colorTransBgCudos,
        chainTabColor = R.color.color_tab_myvalidator_cudos,
        historyApiUrl = "https://api-cudos-testnet.cosmostation.io/",
        grpcApiUrl = "lcd-cudos-testnet.cosmostation.io",
        isSupported = false,
        isTestNet = true
    ),
    PROVENANCE_MAIN(
        names = "provenance-mainnet",
        chainAddressPrefix = "pb1",
        chainIcon = R.drawable.chain_provenance,
        chainTitle = R.string.str_provenance_net,
        chainAlterTitle = R.string.str_provenance_main,
        mainDenom = "nhash",
        fullNameCoin = "Provenance Staking Coin",
        denomColor = R.color.colorProvenance,
        coinIcon = R.drawable.token_hash,
        mnemonicBackground = R.drawable.box_round_provenance,
        symbolTitle = R.string.str_provenance_c,
        chainBackground = R.color.colorTransBgProvenance,
        chainTabColor = R.color.color_tab_myvalidator_provenance,
        blockTimeString = "6.3061",
        historyApiUrl = "https://api-provenance.cosmostation.io/",
        grpcApiUrl = "lcd-provenance-app.cosmostation.io",
        pathConfig = "{\"default\":\"505\"}"
    ),
    CERBERUS_MAIN(
        names = "cerberus-mainnet",
        chainAddressPrefix = "cerberus1",
        chainIcon = R.drawable.chain_cerberus,
        chainTitle = R.string.str_cerberus_net,
        chainAlterTitle = R.string.str_cerberus_main,
        mainDenom = "ucrbrus",
        fullNameCoin = "Cerberus Staking Coin",
        denomColor = R.color.colorCerberus,
        coinIcon = R.drawable.token_cerberus,
        mnemonicBackground = R.drawable.box_round_cerberus,
        symbolTitle = R.string.str_cerberus_c,
        chainBackground = R.color.colorTransBgCerberus,
        chainTabColor = R.color.color_tab_myvalidator_cerberus,
        blockTimeString = "5.9666",
        historyApiUrl = "https://api-cerberus.cosmostation.io/",
        grpcApiUrl = "lcd-cerberus-app.cosmostation.io"
    ),
    OMNIFLIX_MAIN(
        names = "omniflix-mainnet",
        chainAddressPrefix = "omniflix1",
        chainIcon = R.drawable.chain_omniflix,
        chainTitle = R.string.str_omniflix_net,
        chainAlterTitle = R.string.str_omniflix_main,
        mainDenom = "uflix",
        fullNameCoin = "Omniflix Staking Coin",
        denomColor = R.color.colorOmniflix,
        coinIcon = R.drawable.token_omniflix,
        mnemonicBackground = R.drawable.box_round_omniflix,
        symbolTitle = R.string.str_omniflix_c,
        chainBackground = R.color.colorTransBgOmniflix,
        chainTabColor = R.color.color_tab_myvalidator_omniflix,
        blockTimeString = "5.7970",
        historyApiUrl = "https://api-omniflix.cosmostation.io/",
        grpcApiUrl = "lcd-omniflix-app.cosmostation.io"
    ),
    COSMOS_TEST(
        names = "cosmos-testnet,stargate-final",
        chainAddressPrefix = "cosmos1",
        chainIcon = R.drawable.chain_test_cosmos,
        chainTitle = R.string.str_cosmos_test_net,
        chainAlterTitle = R.string.str_cosmos_test,
        mainDenom = "uatom",
        fullNameCoin = "Stargate Staking Coin",
        denomColor = R.color.colorAtom,
        coinIcon = R.drawable.atom_ic,
        mnemonicBackground = R.drawable.box_round_atom,
        symbolTitle = R.string.str_muon_c,
        chainBackground = R.color.colorTransBgCosmos,
        chainTabColor = R.color.color_tab_myvalidator,
        blockTimeString = "7.6597",
        historyApiUrl = "https://api-office.cosmostation.io/stargate-final/",
        grpcApiUrl = "lcd-office.cosmostation.io:10300",
        isSupported = false,
        isTestNet = true
    ),
    IRIS_TEST(
        names = "iris-testnet,bifrost-2",
        chainAddressPrefix = "iaa1",
        chainIcon = R.drawable.chain_test_iris,
        chainTitle = R.string.str_iris_test_net,
        chainAlterTitle = R.string.str_iris_test,
        mainDenom = "uiris",
        fullNameCoin = "Bifrost Staking Coin",
        denomColor = R.color.colorIris,
        coinIcon = R.drawable.iris_toket_img,
        mnemonicBackground = R.drawable.box_round_darkgray,
        symbolTitle = R.string.str_bif_c,
        chainBackground = R.color.colorTransBgIris,
        chainTabColor = R.color.color_tab_myvalidator_iris,
        blockTimeString = "6.7884",
        historyApiUrl = "https://api-office.cosmostation.io/bifrost/",
        grpcApiUrl = "lcd-office.cosmostation.io:9095",
        isSupported = false,
        isTestNet = true
    ),
    OK_TEST(
        names = "okexchain-testnet",
        chainAddressPrefix = "0x",
        chainIcon = R.drawable.chain_okx,
        chainTitle = R.string.str_ok_test_net,
        chainAlterTitle = R.string.str_okex_test,
        mainDenom = "okt",
        fullNameCoin = "OEC Staking Coin",
        denomColor = R.color.colorOK,
        coinIcon = R.drawable.token_okx,
        mnemonicBackground = R.drawable.box_round_okex,
        symbolTitle = R.string.str_ok_c,
        chainBackground = R.color.colorTransBgOkex,
        chainTabColor = R.color.color_tab_myvalidator_ok,
        isSupported = false,
        isTestNet = true
    ),
    RIZON_TEST(
        names = "rizon-testnet2",
        chainAddressPrefix = "rizon1",
        chainIcon = R.drawable.chain_rizon,
        chainTitle = R.string.str_rizon_test_net,
        chainAlterTitle = R.string.str_rizon_test,
        mainDenom = "uatolo",
        fullNameCoin = "Rizon Staking Coin",
        denomColor = R.color.colorRizon,
        coinIcon = R.drawable.token_rizon,
        mnemonicBackground = R.drawable.box_round_rizon,
        symbolTitle = R.string.str_rizon_c,
        chainBackground = R.color.colorTransBgRizon,
        chainTabColor = R.color.color_tab_myvalidator_rizon,
        historyApiUrl = "https://api-rizon-testnet.cosmostation.io/",
        isSupported = false,
        isTestNet = true
    ),
    ALTHEA_TEST(
        names = "althea-testnet",
        chainAddressPrefix = "althea1",
        chainIcon = R.drawable.chain_althea,
        chainTitle = R.string.str_althea_test_net,
        chainAlterTitle = R.string.str_althea_test,
        mainDenom = "ualtg",
        fullNameCoin = "Althea Staking Coin",
        denomColor = R.color.colorAlthea,
        coinIcon = R.drawable.token_althea,
        mnemonicBackground = R.drawable.box_round_darkgray,
        symbolTitle = R.string.str_althea_c,
        chainBackground = R.color.colorTransBgAlthea,
        chainTabColor = R.color.color_tab_myvalidator_althea,
        historyApiUrl = "https://api-office.cosmostation.io/althea-testnet2v1/",
        grpcApiUrl = "lcd-office.cosmostation.io:20100",
        isSupported = false,
        isTestNet = true
    );

    private val aliases: Array<String>

    val chain: String
    val blockTime: BigDecimal

    val grpcApiUrl: String
    val grpcApiPort: Int

    @ColorRes
    val floatButtonBackground: Int

    init {
        val chunks = names.split(",".toRegex()).toTypedArray()
        chain = chunks[0]
        aliases = if (chunks.size > 1) chunks.copyOfRange(1, chunks.size) else emptyArray()
        val grpcApiChunks = grpcApiUrl.split(":".toRegex()).toTypedArray()
        this.grpcApiUrl = grpcApiChunks[0]
        grpcApiPort = if (grpcApiChunks.size > 1) grpcApiChunks[1].toInt() else 9090
        this.blockTime = safe {
            if (!TextUtils.isEmpty(blockTimeString)) {
                BigDecimal(blockTimeString)
            } else {
                null
            }
        }.or(BigDecimal.ZERO)

        this.floatButtonBackground = if (floatButtonBackground == 0) {
            denomColor
        } else {
            floatButtonBackground
        }
    }

    fun hasChainName(chainName: String): Boolean {
        return this.chain == chainName || aliases.any { it == chainName }
    }

    companion object {
        @JvmStatic
        fun getChain(chainName: String): BaseChain? {
            return values().find { chain ->
                chain.hasChainName(chainName)
            }
        }

        @JvmStatic
        fun getChainByDenom(denom: String): BaseChain? {
            return values().find { chain ->
                chain.mainDenom == denom
            }
        }

        @JvmStatic
        fun getChainsByAddress(address: String): List<BaseChain> {
            return when {
                address.startsWith("0x") && WKey.isValidEthAddress(address) -> {
                    listOf(OKEX_MAIN)
                }
                WKey.isValidBech32(address) -> {
                    return values().filter { chain ->
                        address.startsWith(chain.chainAddressPrefix)
                    }
                }
                else -> {
                    emptyList()
                }
            }
        }

        @JvmStatic
        fun isSupported(chainName: String): Boolean {
            return getChain(chainName)?.isSupported == true
        }

        @JvmStatic
        fun getHtlcSendable(fromChain: BaseChain): List<BaseChain> {
            return when (fromChain) {
                KAVA_MAIN -> listOf(BNB_MAIN)
                BNB_MAIN -> listOf(KAVA_MAIN)
                else -> emptyList()
            }
        }

        @JvmStatic
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