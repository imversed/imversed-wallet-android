package com.fulldive.wallet.presentation.main.settings

import com.fulldive.wallet.di.modules.DefaultPresentersModule
import com.fulldive.wallet.extensions.withDefaults
import com.fulldive.wallet.interactors.settings.SettingsInteractor
import com.fulldive.wallet.presentation.base.BaseMoxyPresenter
import com.joom.lightsaber.ProvidedBy
import javax.inject.Inject

@ProvidedBy(DefaultPresentersModule::class)
class MainSettingsPresenter @Inject constructor(
    private val settingsInteractor: SettingsInteractor,
) : BaseMoxyPresenter<MainSettingsMoxyView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        settingsInteractor
            .observeCurrency()
            .withDefaults()
            .compositeSubscribe(
                onNext = viewState::setCurrency
            )

        settingsInteractor
            .observeAppLockEnabled()
            .withDefaults()
            .compositeSubscribe(
                onNext = viewState::setAppLockEnabled
            )
    }
}