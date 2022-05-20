package com.fulldive.wallet.presentation.main.tokens

import android.text.SpannableString
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class TokenWrappedItem(
    val id: String,
    val section: Section,
    @DrawableRes val iconResId: Int,
    val iconUrl: String,
    val title: String,
    @StringRes val titleRes: Int,
    @ColorRes val titleColorRes: Int,
    val hint: String,
    val description: String,
    val balance: SpannableString,
    val value: SpannableString
)