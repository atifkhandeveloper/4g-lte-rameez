//package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.new_ads_manager
//
//import android.app.Activity
//import android.app.Application
//import android.app.Application.ActivityLifecycleCallbacks
//import android.content.Context
//import android.os.Bundle
//import android.util.Log
//import androidx.lifecycle.DefaultLifecycleObserver
//import androidx.lifecycle.LifecycleOwner
//import androidx.lifecycle.ProcessLifecycleOwner
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.R
//import com.google.android.gms.ads.AdError
//import com.google.android.gms.ads.AdRequest
//import com.google.android.gms.ads.FullScreenContentCallback
//import com.google.android.gms.ads.LoadAdError
//import com.google.android.gms.ads.MobileAds
//import com.google.android.gms.ads.appopen.AppOpenAd
//import java.util.Date
//
//class AppOpen : Application(), ActivityLifecycleCallbacks, DefaultLifecycleObserver {
//    private var appOpenAdManager: AppOpenAdManager? = null
//    private var currentActivity: Activity? = null
//
//    override fun onCreate() {
//        super<Application>.onCreate()
//        registerActivityLifecycleCallbacks(this)
//        Log.d(TAG, "Google Mobile Ads SDK Version: " + MobileAds.getVersion())
//        MobileAds.initialize(this) {
//            // Initialization completed
//        }
//        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
//        appOpenAdManager = AppOpenAdManager()
//    }
//
//    override fun onStart(owner: LifecycleOwner) {
//        super<DefaultLifecycleObserver>.onStart(owner)
//        appOpenAdManager!!.showAdIfAvailable(currentActivity!!)
//    }
//
//    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
//    override fun onActivityStarted(activity: Activity) {
//        if (!appOpenAdManager!!.isShowingAd) {
//            currentActivity = activity
//        }
//    }
//
//    override fun onActivityResumed(activity: Activity) {}
//    override fun onActivityPaused(activity: Activity) {}
//    override fun onActivityStopped(activity: Activity) {}
//    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
//    override fun onActivityDestroyed(activity: Activity) {}
//
//    fun showAdIfAvailable(activity: Activity, onShowAdCompleteListener: OnShowAdCompleteListener) {
//        appOpenAdManager!!.showAdIfAvailable(activity, onShowAdCompleteListener)
//    }
//
//    interface OnShowAdCompleteListener {
//        fun onShowAdComplete()
//    }
//
//    inner class AppOpenAdManager {
//        private var appOpenAd: AppOpenAd? = null
//        private var isLoadingAd = false
//        var isShowingAd = false
//        private var loadTime: Long = 0
//
//        private fun loadAd(context: Context) {
//            if (isLoadingAd || isAdAvailable) {
//                return
//            }
//            isLoadingAd = true
//            val request = AdRequest.Builder().build()
//            AppOpenAd.load(context, getString(R.string.appOpenAds), request, object : AppOpenAd.AppOpenAdLoadCallback() {
//                override fun onAdLoaded(ad: AppOpenAd) {
//                    appOpenAd = ad
//                    isLoadingAd = false
//                    loadTime = Date().time
//                    Log.d(LOG_TAG, "onAdLoaded.")
//                }
//
//                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
//                    isLoadingAd = false
//                    Log.d(LOG_TAG, "onAdFailedToLoad: " + loadAdError.message)
//                }
//            })
//        }
//
//        private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
//            val dateDifference = Date().time - loadTime
//            val numMilliSecondsPerHour: Long = 3600000
//            return dateDifference < numMilliSecondsPerHour * numHours
//        }
//
//        private val isAdAvailable: Boolean
//            private get() = appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)
//
//        fun showAdIfAvailable(
//            activity: Activity,
//            onShowAdCompleteListener: OnShowAdCompleteListener =
//                object : OnShowAdCompleteListener {
//                    override fun onShowAdComplete() {
//                        // Empty because the user will go back to the activity that shows the ad.
//                    }
//                }
//        ) {
//            if (isShowingAd) {
//                Log.d(LOG_TAG, "The app open ad is already showing.")
//                return
//            }
//            if (!isAdAvailable) {
//                Log.d(LOG_TAG, "The app open ad is not ready yet.")
//                onShowAdCompleteListener.onShowAdComplete()
//                loadAd(activity)
//                return
//            }
//            Log.d(LOG_TAG, "Will show ad.")
//            appOpenAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
//                override fun onAdDismissedFullScreenContent() {
//                    appOpenAd = null
//                    isShowingAd = false
//                    onShowAdCompleteListener.onShowAdComplete()
//                    loadAd(activity)
//                }
//
//                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
//                    appOpenAd = null
//                    isShowingAd = false
//                    onShowAdCompleteListener.onShowAdComplete()
//                    loadAd(activity)
//                }
//
//                override fun onAdShowedFullScreenContent() {}
//            }
//            isShowingAd = true
//            appOpenAd!!.show(activity)
//        }
//    }
//
//    companion object {
//        private const val TAG = "MyApplication"
//        private const val LOG_TAG = "AppOpenAdManager"
//    }
//}