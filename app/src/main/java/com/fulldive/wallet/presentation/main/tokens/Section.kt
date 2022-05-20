package com.fulldive.wallet.presentation.main.tokens

import androidx.annotation.StringRes
import wannabit.io.cosmostaion.R

sealed class Section(
    val id: Int,
    @StringRes val titleResId: Int
) {
    object SECTION_UNKNOWN : Section(id = -1, R.string.str_unknown_token_title)
    object SECTION_NATIVE : Section(id = 0, R.string.str_native_token_title)
    object SECTION_IBC_AUTHED_GRPC : Section(id = 1, R.string.str_ibc_token_title)
    object SECTION_IBC_UNKNOWN_GRPC : Section(id = 4, R.string.str_unknown_ibc_token_title)
    object SECTION_OSMOSIS_POOL_GRPC : Section(id = 2, R.string.str_pool_coin_title)
    object SECTION_ETHER_GRPC : Section(id = 3, R.string.str_sif_ether_token_title)
    object SECTION_GRAVICTY_DEX_GRPC : Section(id = 5, R.string.str_gravity_dex_token_title)
    object SECTION_INJECTIVE_POOL_GRPC : Section(id = 6, R.string.str_pool_coin_title)
    object SECTION_KAVA_BEP2_GRPC : Section(id = 7, R.string.str_kava_bep2_token_title)
    object SECTION_ETC : Section(id = 8, R.string.str_etc_token_title)
    object SECTION_OKEX_ETC : Section(id = 9, R.string.str_oec_kip10_title)
    object SECTION_CW20_GRPC : Section(id = 10, R.string.str_cw20_token_title)
}
