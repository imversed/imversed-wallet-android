package com.fulldive.wallet.presentation.pro

import android.app.Activity
import com.fulldive.wallet.di.modules.DefaultPresentersModule
import com.fulldive.wallet.extensions.withDefaults
import com.fulldive.wallet.interactors.billing.BillingInteractor
import com.fulldive.wallet.presentation.base.BaseMoxyPresenter
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import wannabit.io.cosmostaion.R
import javax.inject.Inject

@ProvidedBy(DefaultPresentersModule::class)
class ProPresenter @Inject constructor(
    private val billingInteractor: BillingInteractor
) : BaseMoxyPresenter<ProMoxyView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        Observable
            .combineLatest(
                billingInteractor.observeIsPro(),
                billingInteractor.observePrice(),
                BiFunction { isPro: Boolean, price: String -> isPro to price }
            )
            .withDefaults()
            .compositeSubscribe(
                onNext = { (isPro, price) -> viewState.setProState(isPro, price) }
            )

        billingInteractor.refresh()
    }

    fun onBuyClicked(activity: Activity) {
        if (!billingInteractor.purchase(activity)) {
            viewState.showMessage(R.string.error_pro_unavailable)
        }
    }

    fun onRestoreClicked() {
        billingInteractor.refresh()
    }
}
