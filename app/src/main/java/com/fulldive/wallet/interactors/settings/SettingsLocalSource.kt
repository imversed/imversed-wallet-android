package com.fulldive.wallet.interactors.settings

import android.content.Context
import com.fulldive.wallet.di.modules.DefaultLocalStorageModule
import com.fulldive.wallet.extensions.or
import com.fulldive.wallet.models.Currency
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import wannabit.io.cosmostaion.appextensions.getPrivateSharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ProvidedBy(DefaultLocalStorageModule::class)
class SettingsLocalSource @Inject constructor(
    context: Context
) {
    private var sharedPreferences = context.getPrivateSharedPreferences(KEY_SETTINGS)
    private var currency: Currency? = null
    private var currencySubject = BehaviorSubject.create<Currency>()

    fun setCurrency(currency: Currency) {
        this.currency = currency
        currencySubject.onNext(currency)
        sharedPreferences.edit().putInt(KEY_CURRENCY, currency.id).apply()
    }

    fun observeCurrency(): Observable<Currency> {
        return currencySubject
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