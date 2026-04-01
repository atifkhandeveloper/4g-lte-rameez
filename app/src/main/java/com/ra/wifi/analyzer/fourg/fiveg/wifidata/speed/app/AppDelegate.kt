package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.app

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.PackageManagerCompat.LOG_TAG
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner

import com.akexorcist.localizationactivity.ui.LocalizationApplication
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.isAdEnable
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.ConfigParam

import com.google.android.gms.tasks.Task
import com.google.firebase.BuildConfig
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.R
import timber.log.Timber
import java.util.Date
import java.util.Locale

class AppDelegate : LocalizationApplication(), ActivityLifecycleCallbacks,
    DefaultLifecycleObserver {
    private lateinit var sharedPreferences: SharedPreferences
    private var currentActivity: Activity? = null
    private var config: FirebaseRemoteConfig? = null
    private lateinit var appOpenAdManager: AppOpenAdManager

    override fun getDefaultLanguage(context: Context): Locale {
        return Locale.ENGLISH
    }

    override fun onCreate() {
        super<LocalizationApplication>.onCreate()
        FirebaseApp.initializeApp(this)
        config = FirebaseRemoteConfig.getInstance()
        appClassIns = this
        registerActivityLifecycleCallbacks(this)
        sharedPreferences = getSharedPreferences("Myrehgffs", Context.MODE_PRIVATE)
        val savedMode = if (sharedPreferences.contains("MODE")) {
            sharedPreferences.getInt("MODE", AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
//        AppOpenAdX(this)
        AppCompatDelegate.setDefaultNightMode(savedMode)
//        loadAdNativeBottomMain()
//        loadInterAd()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        initConfig()
        setNotificationChannels()

        registerActivityLifecycleCallbacks(this)
        appOpenAdManager = AppOpenAdManager()
    }

    private fun setNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannels(
                listOf(
                    NotificationChannel(
                        "FCM",
                        "App Notification",
                        NotificationManager.IMPORTANCE_HIGH
                    )
                )
            )
        }
    }

    private fun initConfig() {
        val time = if (BuildConfig.DEBUG) 0L
        else 3600L

        val configSettings =
            FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(time).build()

        config?.setConfigSettingsAsync(configSettings)
        config?.fetchAndActivate()?.addOnCompleteListener { task: Task<Boolean?> ->
            Log.d("Installations13423", task.isSuccessful.toString())
            if (task.isSuccessful) {
                val updated = task.result
            }
        }?.addOnFailureListener { e: Exception ->
            Log.d(
                "Installations13423",
                "Fail: " + e.message
            )
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onStart(owner)
        currentActivity?.let {
            // Show the ad (if available) when the app moves to foreground.
            appOpenAdManager.showAdIfAvailable(it)
        }

    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {
        if (!appOpenAdManager.isShowingAd) {
            currentActivity = activity
        }
    }

    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}


    fun showAdIfAvailable(activity: Activity, onShowAdCompleteListener: OnShowAdCompleteListener) {
        // We wrap the showAdIfAvailable to enforce that other classes only interact with MyApplication
        // class.
        appOpenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener)
    }

    fun loadAd(activity: Activity) {
        // We wrap the loadAd to enforce that other classes only interact with MyApplication
        // class.
        appOpenAdManager.loadAd(activity)
    }

    /**
     * Interface definition for a callback to be invoked when an app open ad is complete (i.e.
     * dismissed or fails to show).
     */
    interface OnShowAdCompleteListener {
        fun onShowAdComplete()
    }

    private inner class AppOpenAdManager {

        private var appOpenAd: AppOpenAd? = null
        private var isLoadingAd = false
        var isShowingAd = false

        /** Keep track of the time an app open ad is loaded to ensure you don't show an expired ad. */
        private var loadTime: Long = 0

        // [END manager_class]

        /**
         * Load an ad.
         *
         * @param context the context of the activity that loads the ad
         */
        fun loadAd(context: Context) {
            // Do not load ad if there is an unused ad or one is already loading.
            if (isLoadingAd || isAdAvailable()) {
                return
            }
            isLoadingAd = true
            // [START load_ad]
            AppOpenAd.load(
                context,
                resources.getString(R.string.opeApp),
                AdRequest.Builder().build(),
                object : AppOpenAd.AppOpenAdLoadCallback() {
                    override fun onAdLoaded(ad: AppOpenAd) {
                        // Called when an app open ad has loaded.


                        appOpenAd = ad
                        isLoadingAd = false
                        loadTime = Date().time
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        // Called when an app open ad has failed to load.


                        isLoadingAd = false
                    }
                },
            )
            // [END load_ad]
        }

        // [START ad_expiration]
        /** Check if ad was loaded more than n hours ago. */
        private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
            val dateDifference: Long = Date().time - loadTime
            val numMilliSecondsPerHour: Long = 3600000
            return dateDifference < numMilliSecondsPerHour * numHours
        }

        /** Check if ad exists and can be shown. */
        private fun isAdAvailable(): Boolean {
            // For time interval details, see: https://support.google.com/admob/answer/9341964
            return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)
        }

        // [END ad_expiration]

        /**
         * Show the ad if one isn't already showing.
         *
         * @param activity the activity that shows the app open ad
         */
        fun showAdIfAvailable(activity: Activity) {
            showAdIfAvailable(
                activity,
                object : OnShowAdCompleteListener {
                    override fun onShowAdComplete() {
                        // Empty because the user will go back to the activity that shows the ad.
                    }
                },
            )
        }

        /**
         * Show the ad if one isn't already showing.
         *
         * @param activity the activity that shows the app open ad
         * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
         */
        fun showAdIfAvailable(activity: Activity, onShowAdCompleteListener: OnShowAdCompleteListener) {
            // If the app open ad is already showing, do not show the ad again.
            if (isShowingAd) {

                return
            }

            // If the app open ad is not available yet, invoke the callback.
            if (!isAdAvailable()) {

                onShowAdCompleteListener.onShowAdComplete()
                    loadAd(activity)

                return
            }



            appOpenAd?.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    /** Called when full screen content is dismissed. */
                    override fun onAdDismissedFullScreenContent() {
                        // Set the reference to null so isAdAvailable() returns false.
                        appOpenAd = null
                        isShowingAd = false


                        onShowAdCompleteListener.onShowAdComplete()
                            loadAd(activity)

                    }

                    /** Called when fullscreen content failed to show. */
                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        appOpenAd = null
                        isShowingAd = false


                        onShowAdCompleteListener.onShowAdComplete()
                            loadAd(activity)

                    }

                    /** Called when fullscreen content is shown. */
                    override fun onAdShowedFullScreenContent() {

                    }
                }
            isShowingAd = true
            appOpenAd?.show(activity)
        }
    }

//    fun loadAdNativeBottomMain(AMCallback: (() -> Unit)? = null) {
//        nativeAd = null
//        homeNativeAd = null
//        loadNativeAds(
//            null,
//            R.layout.ad_unified_banner,
//            ADUnitPlacements.NATIVE_AD,
//            AMCallback = {
//                nativeAd = it
//                homeNativeAd = it
//                AMCallback?.invoke()
//            }, onError = {
//            })
//
//    }

//    private fun loadInterAd() {
//        loadInterstitialAd(ADUnitPlacements.INTER_AD_MAIN, false, {
//            otherInterstitial = it
//        }, {
//
//        })
//    }





    companion object {
        @SuppressLint("StaticFieldLeak")
        var appClassIns: AppDelegate? = null

        //        var otherInterstitial: InterAdPair? = null

    }
}
