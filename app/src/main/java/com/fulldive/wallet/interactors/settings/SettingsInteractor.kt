package com.fulldive.wallet.interactors.settings

import com.fulldive.wallet.di.modules.DefaultInteractorsModule
import com.fulldive.wallet.models.Currency
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Observable
import javax.inject.Inject

@ProvidedBy(DefaultInteractorsModule::class)
class SettingsInteractor @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    fun getCurrency(): Currency {
        return settingsRepository.getCurrency()
    }

    fun observeCurrency(): Observable<Currency> {
        return settingsRepository.observeCurrency()
    }

    fun setCurrency(currency: Currency) {
        settingsRepository.setCurrency(currency)
    }

    fun getAppLockEnabled(): Boolean {
        return settingsRepository.getAppLockEnabled()
    }

    fun observeAppLockEnabled(): Observable<Boolean> {
        return settingsRepository.observeAppLockEnabled()
    }

    fun setAppLockEnabled(enabled: Boolean) {
        settingsRepository.setAppLockEnabled(enabled)
    }

    fun getFingerprintEnabled(): Boolean {
        return settingsRepository.getFingerprintEnabled()
    }

    fun setFingerprintEnabled(enabled: Boolean) {
        settingsRepository.setFingerprintEnabled(enabled)
    }

    fun getAppLockInterval(): Int {
        return settingsRepository.getAppLockInterval()
    }

    fun setAppLockInterval(interval: Int) {
        settingsRepository.setAppLockInterval(interval)
    }

    fun getLastActivityTime(): Long {
        return settingsRepository.getLastActivityTime()
    }

    fun updateLastActivityTime() {
        settingsRepository.setLastActivityTime(System.currentTimeMillis())
    }
}