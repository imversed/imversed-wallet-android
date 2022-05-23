package com.fulldive.wallet.interactors.settings

import com.fulldive.wallet.di.modules.DefaultRepositoryModule
import com.fulldive.wallet.models.Currency
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Observable
import javax.inject.Inject

@ProvidedBy(DefaultRepositoryModule::class)
class SettingsRepository @Inject constructor(
    private val settingsLocalSource: SettingsLocalSource
) {

    fun setCurrency(currency: Currency) {
        settingsLocalSource.setCurrency(currency)
    }

    fun observeCurrency(): Observable<Currency> {
        return settingsLocalSource.observeCurrency()
    }

    fun getCurrency(): Currency {
        return settingsLocalSource.getCurrency()
    }

    fun getAppLockEnabled(): Boolean {
        return settingsLocalSource.getAppLockEnabled()
    }

    fun observeAppLockEnabled(): Observable<Boolean> {
        return settingsLocalSource.observeAppLockEnabled()
    }

    fun setAppLockEnabled(enabled: Boolean) {
        settingsLocalSource.setAppLockEnabled(enabled)
    }

    fun getFingerprintEnabled(): Boolean {
        return settingsLocalSource.getFingerprintEnabled()
    }

    fun setFingerprintEnabled(enabled: Boolean) {
        settingsLocalSource.setFingerprintEnabled(enabled)
    }

    fun getAppLockInterval(): Int {
        return settingsLocalSource.getAppLockInterval()
    }

    fun setAppLockInterval(interval: Int) {
        settingsLocalSource.setAppLockInterval(interval)
    }

    fun getLastActivityTime(): Long {
        return settingsLocalSource.getLastActivityTime()
    }

    fun setLastActivityTime(value: Long) {
        settingsLocalSource.setLastActivityTime(value)
    }
}