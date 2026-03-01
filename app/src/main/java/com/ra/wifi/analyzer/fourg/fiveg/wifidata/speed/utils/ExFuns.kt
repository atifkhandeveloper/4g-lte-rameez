package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.FragmentActivity

fun Activity.isOnline(): Boolean {
    return try {
        val cm = getSystemService(FragmentActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.activeNetworkInfo != null && cm.activeNetworkInfo?.isConnected == true
    } catch (e: Exception) {
        false
    }
}
fun Context.openUrlInBrowser(url: String) {
    try {
        val intent = CustomTabsIntent.Builder()
            .build()
        intent.launchUrl(this, Uri.parse(url))
    } catch (e: Exception) {
        e.message?.let {

        }
    }
}
