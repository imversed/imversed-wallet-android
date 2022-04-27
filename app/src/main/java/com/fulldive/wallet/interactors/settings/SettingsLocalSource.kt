package com.fulldive.wallet.interactors.settings

import android.content.Context
import com.fulldive.wallet.di.modules.DefaultLocalStorageModule
import com.fulldive.wallet.extensions.or
import com.fulldive.wallet.extensions.safeCompletable
import com.fulldive.wallet.models.Currency
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Completable
import wannabit.io.cosmostaion.appextensions.getPrivateSharedPreferences
import wannabit.io.cosmostaion.crypto.CryptoHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ProvidedBy(DefaultLocalStorageModule::class)
class SettingsLocalSource @Inject constructor(
    context: Context
) {
    private var sharedPreferences = context.getPrivateSharedPreferences(KEY_SETTINGS)
    private var currency: Currency? = null

    fun setCurrency(currency: Currency) {
        this.currency = currency
        sharedPreferences.edit().putInt(KEY_CURRENCY, currency.id).apply()
    }

    fun getCurrency(): Currency {
        return currency.or {
            sharedPreferences
                .getInt(KEY_CURRENCY, -1)
                .let(Currency::getCurrency)
                .or { Currency.getDefault() }
                .also { currency = it }
        }
    }

    companion object {
        private const val KEY_SETTINGS = "settings"
        private const val KEY_CURRENCY = "currency"
    }
}