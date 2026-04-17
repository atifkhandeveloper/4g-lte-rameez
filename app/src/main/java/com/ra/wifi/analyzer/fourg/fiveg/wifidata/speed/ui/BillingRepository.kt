package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.android.billingclient.api.*
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.core.PremiumManager

object BillingRepository : PurchasesUpdatedListener {

    private lateinit var billingClient: BillingClient
    private lateinit var appContext: Context

    private var isConnected = false

    private var cachedPlans = emptyList<PlanUiModel>()
    private var premiumCallback: (() -> Unit)? = null
    private var plansCallback: (() -> Unit)? = null

    // ---------------- INIT ----------------
    fun init(
        context: Context,
        onReady: (() -> Unit)? = null,
        onPremiumUnlocked: (() -> Unit)? = null
    ) {
        appContext = context.applicationContext
        premiumCallback = onPremiumUnlocked
        plansCallback = onReady

        billingClient = BillingClient.newBuilder(appContext)
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
                    Log.d("BILLING", "Connected")

                    loadAllProducts()
                    restorePurchases(appContext)
                }
            }

            override fun onBillingServiceDisconnected() {
                isConnected = false
            }
        })
    }

    // ---------------- LOAD PRODUCTS ----------------
    private fun loadAllProducts() {

        val subs = listOf(
            product("weekly_fourg", BillingClient.ProductType.SUBS),
            product("monthly_subscription", BillingClient.ProductType.SUBS)
        )

        val inApp = listOf(
            product("lifetime", BillingClient.ProductType.INAPP)
        )

        queryProducts(subs) { subList ->
            queryProducts(inApp) { inAppList ->

                cachedPlans = (subList + inAppList)

                plansCallback?.invoke()
            }
        }
    }

    private fun queryProducts(
        list: List<QueryProductDetailsParams.Product>,
        onResult: (List<PlanUiModel>) -> Unit
    ) {

        billingClient.queryProductDetailsAsync(
            QueryProductDetailsParams.newBuilder()
                .setProductList(list)
                .build()
        ) { result, data ->

            if (result.responseCode != BillingClient.BillingResponseCode.OK) {
                onResult(emptyList())
                return@queryProductDetailsAsync
            }

            val mapped = data.productDetailsList.mapNotNull { it.toPlan() }
            onResult(mapped)
        }
    }

    private fun product(id: String, type: String): QueryProductDetailsParams.Product {
        return QueryProductDetailsParams.Product.newBuilder()
            .setProductId(id)
            .setProductType(type)
            .build()
    }

    // ---------------- MAP PRODUCT ----------------
    private fun ProductDetails.toPlan(): PlanUiModel {

        val title = when (productId) {
            "weekly_fourg" -> "Weekly Plan"
            "monthly_subscription" -> "Monthly Plan"
            "lifetime" -> "Lifetime Plan"
            else -> productId
        }

        val price = when (productType) {
            BillingClient.ProductType.INAPP ->
                oneTimePurchaseOfferDetails?.formattedPrice ?: "Free"

            else ->
                subscriptionOfferDetails?.firstOrNull()
                    ?.pricingPhases
                    ?.pricingPhaseList
                    ?.lastOrNull()
                    ?.formattedPrice ?: "Free"
        }

        return PlanUiModel(
            id = productId,
            title = title,
            price = price,
            hasFreeTrial = false,
            isBestValue = productId == "monthly_subscription",
            product = this
        )
    }

    // ---------------- PURCHASE ----------------
    fun launchPurchase(activity: Activity, plan: PlanUiModel) {

        val product = plan.product

        val params = if (product.productType == BillingClient.ProductType.SUBS) {

            val offer = product.subscriptionOfferDetails?.firstOrNull() ?: return

            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(product)
                .setOfferToken(offer.offerToken)
                .build()

        } else {

            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(product)
                .build()
        }

        billingClient.launchBillingFlow(
            activity,
            BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(listOf(params))
                .build()
        )
    }

    // ---------------- CALLBACK ----------------
    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {

        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            purchases.forEach { handlePurchase(it) }
        }
    }

    private fun handlePurchase(purchase: Purchase) {

        if (purchase.purchaseState != Purchase.PurchaseState.PURCHASED) return

        if (!purchase.isAcknowledged) {

            billingClient.acknowledgePurchase(
                AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
            ) { result ->

                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    unlockPremium()
                }
            }

        } else {
            unlockPremium()
        }
    }

    // ---------------- UNLOCK PREMIUM (FIXED) ----------------
    private fun unlockPremium() {

        // 🔥 SAVE PREMIUM PROPERLY
        PremiumManager.setPremium(appContext, true)

        Log.d("BILLING", "PREMIUM UNLOCKED")

        // 🔥 notify UI only (NO restart)
        premiumCallback?.invoke()
    }

    // ---------------- RESTORE ----------------
    fun restorePurchases(context: Context) {

        if (!::billingClient.isInitialized) return

        listOf(
            BillingClient.ProductType.SUBS,
            BillingClient.ProductType.INAPP
        ).forEach { type ->

            billingClient.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder()
                    .setProductType(type)
                    .build()
            ) { _, purchases ->

                val hasPremium = purchases.any {
                    it.purchaseState == Purchase.PurchaseState.PURCHASED
                }

                if (hasPremium) {
                    unlockPremium()
                }
            }
        }
    }

    // ---------------- CACHE ----------------
    fun getCachedPlans(): List<PlanUiModel> = cachedPlans

    // ---------------- CLEAN ----------------
    fun destroy() {
        if (::billingClient.isInitialized) {
            billingClient.endConnection()
        }
    }
}