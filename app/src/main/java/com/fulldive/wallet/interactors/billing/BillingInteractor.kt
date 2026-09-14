package com.fulldive.wallet.interactors.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.fulldive.wallet.di.modules.DefaultInteractorsModule
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import wannabit.io.cosmostaion.appextensions.getPrivateSharedPreferences
import wannabit.io.cosmostaion.utils.WLog
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Google Play billing for the one-time "Imversed PRO" product.
 *
 * PRO removes the free wallets limit, see [com.fulldive.wallet.interactors.accounts.AccountsInteractor].
 * The purchase state is cached in preferences so the limit stays lifted while Play is unreachable.
 */
@Singleton
@ProvidedBy(DefaultInteractorsModule::class)
class BillingInteractor @Inject constructor(
    context: Context
) : PurchasesUpdatedListener, BillingClientStateListener {

    private val preferences = context.getPrivateSharedPreferences(KEY_BILLING_PREFERENCES)

    private val isProSubject = BehaviorSubject.createDefault(
        preferences.getBoolean(KEY_IS_PRO, false)
    )
    private val priceSubject = BehaviorSubject.createDefault("")

    private var productDetails: ProductDetails? = null

    private val billingClient by lazy {
        BillingClient.newBuilder(context)
            .setListener(this)
            .enablePendingPurchases(
                PendingPurchasesParams.newBuilder()
                    .enableOneTimeProducts()
                    .build()
            )
            .build()
    }

    fun isPro() = isProSubject.value == true

    fun observeIsPro(): Observable<Boolean> = isProSubject.distinctUntilChanged()

    /** Localized price of the PRO product, empty until Play answers. */
    fun observePrice(): Observable<String> = priceSubject.distinctUntilChanged()

    /** Connects to Play (if needed) and refreshes both the price and the ownership state. */
    fun refresh() {
        if (billingClient.connectionState == BillingClient.ConnectionState.CONNECTED) {
            queryProductDetails()
            queryPurchases()
        } else if (billingClient.connectionState != BillingClient.ConnectionState.CONNECTING) {
            billingClient.startConnection(this)
        }
    }

    /**
     * Starts the purchase flow. Returns `false` when the product isn't loaded yet -
     * the caller should tell the user that the store is unavailable.
     */
    fun purchase(activity: Activity): Boolean {
        val details = productDetails
        if (details == null) {
            refresh()
            return false
        }
        val productParams = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(details)
            .apply {
                // Products built on the purchase-options model are launched by offer token;
                // legacy ones carry no token and must be launched without it.
                offerDetails(details)?.offerToken
                    ?.takeIf(String::isNotEmpty)
                    ?.let(::setOfferToken)
            }
            .build()
        val params = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(productParams))
            .build()
        val result = billingClient.launchBillingFlow(activity, params)
        return result.responseCode == BillingClient.BillingResponseCode.OK
    }

    override fun onBillingSetupFinished(billingResult: BillingResult) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            queryProductDetails()
            queryPurchases()
        } else {
            WLog.w("Billing setup failed: ${billingResult.debugMessage}")
        }
    }

    override fun onBillingServiceDisconnected() {
        // Reconnected lazily on the next refresh() call.
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            handlePurchases(purchases)
        }
    }

    private fun queryProductDetails() {
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(PRODUCT_ID)
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
                )
            )
            .build()
        billingClient.queryProductDetailsAsync(params) { billingResult, result ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val details = result.productDetailsList.firstOrNull { it.productId == PRODUCT_ID }
                productDetails = details
                priceSubject.onNext(
                    details?.let(::offerDetails)?.formattedPrice.orEmpty()
                )
            } else {
                WLog.w("Billing product details failed: ${billingResult.debugMessage}")
            }
        }
    }

    private fun queryPurchases() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()
        billingClient.queryPurchasesAsync(params) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                // Authoritative answer: a refunded purchase revokes PRO as well.
                setPro(purchases.any(::isProPurchase))
                handlePurchases(purchases)
            } else {
                WLog.w("Billing query purchases failed: ${billingResult.debugMessage}")
            }
        }
    }

    private fun handlePurchases(purchases: List<Purchase>) {
        purchases
            .filter(::isProPurchase)
            .forEach { purchase ->
                setPro(true)
                if (!purchase.isAcknowledged) {
                    acknowledge(purchase)
                }
            }
    }

    private fun acknowledge(purchase: Purchase) {
        val params = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        billingClient.acknowledgePurchase(params) { billingResult ->
            if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
                WLog.w("Billing acknowledge failed: ${billingResult.debugMessage}")
            }
        }
    }

    /**
     * The purchase-options model exposes offers through the list, older products only through the
     * single legacy field - which stays null unless a purchase option is marked backwards compatible.
     */
    private fun offerDetails(details: ProductDetails): ProductDetails.OneTimePurchaseOfferDetails? {
        return details.oneTimePurchaseOfferDetailsList?.firstOrNull()
            ?: details.oneTimePurchaseOfferDetails
    }

    private fun isProPurchase(purchase: Purchase): Boolean {
        return purchase.products.contains(PRODUCT_ID)
                && purchase.purchaseState == Purchase.PurchaseState.PURCHASED
    }

    private fun setPro(isPro: Boolean) {
        if (isProSubject.value != isPro) {
            preferences.edit().putBoolean(KEY_IS_PRO, isPro).apply()
            isProSubject.onNext(isPro)
        }
    }

    companion object {
        const val PRODUCT_ID = "imversed_pro"

        private const val KEY_BILLING_PREFERENCES = "billing_preferences"
        private const val KEY_IS_PRO = "isPro"
    }
}
