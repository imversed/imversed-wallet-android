package com.fulldive.wallet.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import wannabit.io.cosmostaion.R

class Guide(
    @StringRes val guideTitle: Int,
    @StringRes val guideMessage: Int,
    @DrawableRes val guideIcon: Int,
    @StringRes val buttonText1: Int,
    @StringRes val buttonText2: Int,
    val buttonLink1: String,
    val buttonLink2: String
) {

    companion object {
        fun create(
            @StringRes guideTitle: Int,
            @StringRes guideMessage: Int,
            @DrawableRes guideIcon: Int,
            @StringRes buttonText1: Int = R.string.str_home,
            @StringRes buttonText2: Int = R.string.str_blog,
            buttonLink1: String,
            buttonLink2: String
        ) = Guide(
            guideTitle, guideMessage, guideIcon, buttonText1, buttonText2, buttonLink1, buttonLink2
        )
    }
}