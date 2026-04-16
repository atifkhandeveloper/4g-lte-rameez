package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.billing

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.*
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui.PlanUiModel

object BillingRepository : PurchasesUpdatedListener {

    private lateinit var billingClient: BillingClient

    private var isConnected = false
    private var cachedPlans: List<PlanUiModel> = emptyList()

    private var premiumCallback: (() -> Unit)? = null

    fun init(
        context: Context,
        onReady: (() -> Unit)? = null,
        onPremiumUnlocked: (() -> Unit)? = null
    ) {
        premiumCallback = onPremiumUnlocked

        if (::billingClient.isInitialized && isConnected) {
            onReady?.invoke()
            return
        }

        billingClient = BillingClient.newBuilder(context.applicationContext)
            .enablePendingPurchases(
                PendingPurchasesParams.newBuilder()
                    .enableOneTimeProducts()
                    .build()
            )
            .setListener(this)
            .build()

        billingClient.startConnection(object : BillingClientStateListener {

            override fun onBillingSetupFinished(result: BillingResult) {
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    isConnected = true
                    preloadPlans(onReady)
                }
            }

            override fun onBillingServiceDisconnected() {
                isConnected = false
            }
        })
    }

    private fun preloadPlans(onReady: (() -> Unit)?) {

        val subs = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("weekly_fourg")
                .setProductType(BillingClient.ProductType.SUBS)
                .build(),

            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("monthly_subscription")
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        )

        billingClient.queryProductDetailsAsync(
            QueryProductDetailsParams.newBuilder()
                .setProductList(subs)
                .build()
        ) { _, subsResult ->

            val subPlans = subsResult.productDetailsList.mapNotNull { it.toPlan() }

            val inApp = listOf(
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId("lifetime")
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build()
            )

            billingClient.queryProductDetailsAsync(
                QueryProductDetailsParams.newBuilder()
                    .setProductList(inApp)
                    .build()
            ) { _, inAppResult ->

                val inAppPlans = inAppResult.productDetailsList.mapNotNull { it.toPlan() }

                cachedPlans = subPlans + inAppPlans

                onReady?.invoke()
            }
        }
    }

    fun getCachedPlans(): List<PlanUiModel> = cachedPlans

    fun launchPurchase(activity: Activity, plan: PlanUiModel) {

        val params = if (plan.product.productType == BillingClient.ProductType.SUBS) {

            val offer = plan.product.subscriptionOfferDetails?.firstOrNull()
                ?: return

            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(plan.product)
                .setOfferToken(offer.offerToken)
                .build()

        } else {

            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(plan.product)
                .build()
        }

        billingClient.launchBillingFlow(
            activity,
            BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(listOf(params))
                .build()
        )
    }

    private fun ProductDetails.toPlan(): PlanUiModel? {

        val title = when (productId) {
            "weekly_fourg" -> "Weekly Plan"
            "monthly_subscription" -> "Monthly Plan"
            "lifetime" -> "Lifetime Plan"
            else -> productId
        }

        return if (productType == BillingClient.ProductType.INAPP) {
            PlanUiModel(
                id = productId,
                title = title,
                price = oneTimePurchaseOfferDetails?.formattedPrice ?: "N/A",
                hasFreeTrial = false,
                isBestValue = false,
                product = this
            )
        } else {
            val offer = subscriptionOfferDetails?.firstOrNull() ?: return null
            val phases = offer.pricingPhases.pricingPhaseList
            val recurring = phases.lastOrNull { it.priceAmountMicros > 0 }

            PlanUiModel(
                id = productId,
                title = title,
                price = recurring?.formattedPrice ?: "N/A",
                hasFreeTrial = phases.any { it.priceAmountMicros == 0L },
                isBestValue = productId == "monthly_subscription",
                product = this
            )
        }
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            premiumCallback?.invoke()
        }
    }
}