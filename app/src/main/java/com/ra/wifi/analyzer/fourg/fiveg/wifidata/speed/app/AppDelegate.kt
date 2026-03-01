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
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner

import com.akexorcist.localizationactivity.ui.LocalizationApplication
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.isAdEnable
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.ConfigParam
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.nativead.NativeAd
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
    private var appOpenAdManager: AppOpenAdManager? = null
    private var config: FirebaseRemoteConfig? = null

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
        appOpenAdManager = AppOpenAdManager()
        initConfig()
        setNotificationChannels()
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
        if (config?.isAdEnable(ConfigParam.APP_OPEN) == true)
            appOpenAdManager!!.showAdIfAvailable(currentActivity!!)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {
        if (!appOpenAdManager!!.isShowingAd) {
            currentActivity = activity
        }
    }

    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}

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

    inner class AppOpenAdManager {
        private var appOpenAd: AppOpenAd? = null
        private var isLoadingAd = false
        var isShowingAd = false
        private var loadTime: Long = 0

        private fun loadAd(context: Context) {
            if (isLoadingAd || isAdAvailable) {
                return
            }
            isLoadingAd = true
            val request = AdRequest.Builder().build()
            AppOpenAd.load(
                context,
                getString(R.string.opeApp),
                request,
                object : AppOpenAd.AppOpenAdLoadCallback() {
                    override fun onAdLoaded(ad: AppOpenAd) {
                        appOpenAd = ad
                        isLoadingAd = false
                        loadTime = Date().time
                        Timber.tag(LOG_TAG).d("onAdLoaded.")
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        isLoadingAd = false
                        Timber.tag(LOG_TAG).d("onAdFailedToLoad: %s", loadAdError.message)
                    }
                })
        }

        private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
            val dateDifference = Date().time - loadTime
            val numMilliSecondsPerHour: Long = 3600000
            return dateDifference < numMilliSecondsPerHour * numHours
        }

        private val isAdAvailable: Boolean
            get() = appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)

        fun showAdIfAvailable(
            activity: Activity,
//            onShowAdCompleteListener: AppOpenAdX.OnShowAdCompleteListener =
//                object : AppOpenAdX.OnShowAdCompleteListener {
//                    override fun onShowAdComplete() {
//                        // Empty because the user will go back to the activity that shows the ad.
//                    }
//                }
        ) {
            if (isShowingAd) {
                Timber.tag(LOG_TAG).d("The app open ad is already showing.")
                return
            }
            if (!isAdAvailable) {
                Timber.tag(LOG_TAG).d("The app open ad is not ready yet.")
//                onShowAdCompleteListener.onShowAdComplete()
                loadAd(activity)
                return
            }
            Timber.tag(LOG_TAG).d("Will show ad.")
            appOpenAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    appOpenAd = null
                    isShowingAd = false
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    appOpenAd = null
                    isShowingAd = false
                }

                override fun onAdShowedFullScreenContent() {}
            }
            isShowingAd = true
            appOpenAd!!.show(activity)
        }
    }


    companion object {
        @SuppressLint("StaticFieldLeak")
        var appClassIns: AppDelegate? = null

        //        var otherInterstitial: InterAdPair? = null
        var nativeAd: NativeAd? = null
        var homeNativeAd: NativeAd? = null
        private const val LOG_TAG = "AppOpenAdManager"
    }
}
