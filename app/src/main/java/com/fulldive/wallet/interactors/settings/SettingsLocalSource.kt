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
    private val currencySubject by lazy {
        BehaviorSubject.create<Currency>().apply {
            onNext(getCurrency())
        }
    }
    private var appLockEnabled: Boolean? = null
    private val appLockEnabledSubject by lazy {
        BehaviorSubject.create<Boolean>().apply {
            onNext(getAppLockEnabled())
        }
    }
    private var fingerprintEnabled: Boolean? = null
    private var appLockInterval: Int? = null
    private var lastActivityTime: Long? = null

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

    fun getAppLockEnabled(): Boolean {
        return appLockEnabled.or {
            sharedPreferences
                .getBoolean(KEY_APPLOCK_ENABLED, false)
                .or(false)
                .also { appLockEnabled = it }
        }
    }

    fun observeAppLockEnabled(): Observable<Boolean> {
        return appLockEnabledSubject
    }

    fun setAppLockEnabled(enabled: Boolean) {
        this.appLockEnabled = enabled
        this.appLockEnabledSubject.onNext(enabled)
        sharedPreferences.edit().putBoolean(KEY_APPLOCK_ENABLED, enabled).apply()
    }

    fun getFingerprintEnabled(): Boolean {
        return fingerprintEnabled.or {
            sharedPreferences
                .getBoolean(KEY_FINGERPRINT_ENABLED, false)
                .or(false)
                .also { fingerprintEnabled = it }
        }
    }

    fun setFingerprintEnabled(enabled: Boolean) {
        this.fingerprintEnabled = enabled
        sharedPreferences.edit().putBoolean(KEY_FINGERPRINT_ENABLED, enabled).apply()
    }

    fun getAppLockInterval(): Int {
        return appLockInterval.or {
            sharedPreferences
                .getInt(KEY_APPLOCK_INTERVAL, 0)
                .or(0)
                .also { appLockInterval = it }
        }
    }

    fun setAppLockInterval(interval: Int) {
        this.appLockInterval = interval
        sharedPreferences.edit().putInt(KEY_APPLOCK_INTERVAL, interval).apply()
    }

    fun getLastActivityTime(): Long {
        return lastActivityTime.or {
            sharedPreferences
                .getLong(KEY_LAST_ACTIVITY_TIME, 0L)
                .or(0L)
                .also { lastActivityTime = it }
        }
    }

    fun setLastActivityTime(value: Long) {
        this.lastActivityTime = value
        sharedPreferences.edit().putLong(KEY_LAST_ACTIVITY_TIME, value).apply()
    }

    companion object {
        private const val KEY_SETTINGS = "settings"
        private const val KEY_CURRENCY = "currency"
        private const val KEY_APPLOCK_ENABLED = "appLockEnabled"
        private const val KEY_FINGERPRINT_ENABLED = "fingerprintEnabled"
        private const val KEY_APPLOCK_INTERVAL = "appLockInterval"
        private const val KEY_LAST_ACTIVITY_TIME = "lastActivityTime"
    }
}