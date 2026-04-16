package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui


import com.android.billingclient.api.ProductDetails

data class PlanUiModel(
    val id: String,
    val title: String,
    val price: String,
    val hasFreeTrial: Boolean,
    val isBestValue: Boolean,
    val product: ProductDetails
)