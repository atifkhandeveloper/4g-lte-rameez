package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.core

import android.content.Context

object PremiumManager {

    private const val PREF = "premium_prefs"
    private const val KEY = "is_premium"

    // ✅ Save premium state
    fun setPremium(context: Context, value: Boolean) {
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY, value)
            .apply()
    }

    // ✅ Check premium state
    fun isPremium(context: Context): Boolean {
        return context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .getBoolean(KEY, false)
    }

    // ✅ Optional: quick helper for ads
    fun shouldShowAds(context: Context): Boolean {
        return !isPremium(context)
    }

    // ✅ Optional: reset (for testing / logout)
    fun reset(context: Context) {
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }
}