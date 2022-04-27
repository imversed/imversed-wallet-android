package com.fulldive.wallet.interactors.settings

import com.fulldive.wallet.di.modules.DefaultRepositoryModule
import com.fulldive.wallet.models.Currency
import com.joom.lightsaber.ProvidedBy
import javax.inject.Inject

@ProvidedBy(DefaultRepositoryModule::class)
class SettingsRepository @Inject constructor(
    private val settingsLocalSource: SettingsLocalSource
) {


    fun setCurrency(currency: Currency) {
        settingsLocalSource.setCurrency(currency)
    }

    fun getCurrency(): Currency {
        return settingsLocalSource.getCurrency()
    }
}