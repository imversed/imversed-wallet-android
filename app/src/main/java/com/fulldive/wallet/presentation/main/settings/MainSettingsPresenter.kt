package com.fulldive.wallet.presentation.main.settings

import com.fulldive.wallet.di.modules.DefaultPresentersModule
import com.fulldive.wallet.extensions.withDefaults
import com.fulldive.wallet.interactors.accounts.AccountsInteractor
import com.fulldive.wallet.interactors.billing.BillingInteractor
import com.fulldive.wallet.interactors.settings.SettingsInteractor
import com.fulldive.wallet.presentation.base.BaseMoxyPresenter
import com.joom.lightsaber.ProvidedBy
import javax.inject.Inject

@ProvidedBy(DefaultPresentersModule::class)
class MainSettingsPresenter @Inject constructor(
    private val settingsInteractor: SettingsInteractor,
    private val accountsInteractor: AccountsInteractor,
    private val billingInteractor: BillingInteractor,
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

        billingInteractor
            .observeIsPro()
            .withDefaults()
            .compositeSubscribe(
                onNext = viewState::setProEnabled
            )

        billingInteractor.refresh()
    }

    fun onAddWalletClicked() {
        accountsInteractor
            .canAddAccount()
            .withDefaults()
            .compositeSubscribe(
                onSuccess = { canAdd ->
                    if (canAdd) {
                        viewState.showAddWalletDialog()
                    } else {
                        viewState.showProDialog()
                    }
                }
            )
    }

    fun onProClicked() {
        viewState.showProDialog()
    }
}
