package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.app

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.akexorcist.localizationactivity.ui.LocalizationApplication
import com.google.android.gms.ads.*
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.BuildConfig
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.R
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.billing.BillingRepository
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.core.PremiumManager
import java.util.*

class AppDelegate : LocalizationApplication(),
    Application.ActivityLifecycleCallbacks,
    DefaultLifecycleObserver {

    private lateinit var prefs: SharedPreferences
    private var currentActivity: Activity? = null
    private var remoteConfig: FirebaseRemoteConfig? = null

    private var appOpenAdManager: AppOpenAdManager? = null

    private var lastShownTime = 0L
    private val COOLDOWN = 30_000L

    override fun onCreate() {
        super<LocalizationApplication>.onCreate()

        BillingRepository.init(this)
        FirebaseApp.initializeApp(this)

        remoteConfig = FirebaseRemoteConfig.getInstance()

        prefs = getSharedPreferences("Myrehgffs", Context.MODE_PRIVATE)

        AppCompatDelegate.setDefaultNightMode(
            prefs.getInt("MODE", AppCompatDelegate.MODE_NIGHT_NO)
        )

        registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        initRemoteConfig()

        if (PremiumManager.shouldShowAds(this)) {
            appOpenAdManager = AppOpenAdManager()
        }
    }

    // ================= SAFE PUBLIC API =================

    fun showAdIfAvailable(activity: Activity, onComplete: () -> Unit) {

        if (!PremiumManager.shouldShowAds(activity)) {
            onComplete()
            return
        }

        appOpenAdManager?.showAdIfAvailable(activity, onComplete) ?: run {
            onComplete()
        }
    }

    fun loadAppOpenAd(activity: Activity) {
        appOpenAdManager?.loadAd(activity)
    }

    // ================= LIFECYCLE =================

    override fun onStart(owner: LifecycleOwner) {

        val activity = currentActivity ?: return

        if (!PremiumManager.shouldShowAds(this)) return

        val now = System.currentTimeMillis()
        if (now - lastShownTime < COOLDOWN) return

        lastShownTime = now

        appOpenAdManager?.showAdIfAvailable(activity) {}
    }

    override fun onActivityStarted(activity: Activity) {
        if (!PremiumManager.shouldShowAds(this)) return
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivityDestroyed(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    // ================= INNER ADS MANAGER =================

    private inner class AppOpenAdManager {

        private var appOpenAd: AppOpenAd? = null
        private var isLoading = false
        private var isShowing = false
        private var loadTime = 0L

        fun loadAd(context: Context) {

            if (isLoading || isAdAvailable()) return

            isLoading = true

            AppOpenAd.load(
                context,
                context.getString(R.string.opeApp),
                AdRequest.Builder().build(),
                object : AppOpenAd.AppOpenAdLoadCallback() {

                    override fun onAdLoaded(ad: AppOpenAd) {
                        appOpenAd = ad
                        isLoading = false
                        loadTime = System.currentTimeMillis()
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        isLoading = false
                    }
                }
            )
        }

        private fun isAdAvailable(): Boolean {
            return appOpenAd != null &&
                    (System.currentTimeMillis() - loadTime < 4 * 60 * 60 * 1000)
        }

        fun showAdIfAvailable(activity: Activity, onComplete: () -> Unit) {

            if (!PremiumManager.shouldShowAds(activity)) {
                onComplete()
                return
            }

            if (isShowing) return

            if (!isAdAvailable()) {
                loadAd(activity)
                onComplete()
                return
            }

            appOpenAd?.fullScreenContentCallback =
                object : FullScreenContentCallback() {

                    override fun onAdDismissedFullScreenContent() {
                        appOpenAd = null
                        isShowing = false
                        loadAd(activity)
                        onComplete()
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        appOpenAd = null
                        isShowing = false
                        loadAd(activity)
                        onComplete()
                    }

                    override fun onAdShowedFullScreenContent() {
                        isShowing = true
                    }
                }

            isShowing = true
            appOpenAd?.show(activity)
        }
    }

    // ================= CONFIG =================

    private fun initRemoteConfig() {
        val settings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(if (BuildConfig.DEBUG) 0 else 3600)
            .build()

        remoteConfig?.setConfigSettingsAsync(settings)
        remoteConfig?.fetchAndActivate()
    }

    override fun getDefaultLanguage(context: Context): Locale {
        return Locale.ENGLISH
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        var appClassIns: AppDelegate? = null
    }
}