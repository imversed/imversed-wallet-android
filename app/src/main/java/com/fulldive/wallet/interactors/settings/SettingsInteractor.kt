package com.fulldive.wallet.interactors.settings

import com.fulldive.wallet.di.modules.DefaultInteractorsModule
import com.fulldive.wallet.models.Currency
import com.joom.lightsaber.ProvidedBy
import javax.inject.Inject

@ProvidedBy(DefaultInteractorsModule::class)
class SettingsInteractor @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    fun getCurrency(): Currency {
        return settingsRepository.getCurrency()
    }

    fun setCurrency(currency: Currency) {
        settingsRepository.setCurrency(currency)
    }
}