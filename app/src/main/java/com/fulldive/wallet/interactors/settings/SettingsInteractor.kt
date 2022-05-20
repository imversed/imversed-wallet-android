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
}