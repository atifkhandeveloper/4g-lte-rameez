package com.pie.whatsappstatussaver.AdsImplimentation

import androidx.lifecycle.ProcessLifecycleOwner
import com.pie.whatsappstatussaver.AdsImplimentation.AppOpenManager
import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.pie.whatsappstatussaver.AdsImplimentation.SharedPrefRemote
import com.pie.whatsappstatussaver.R
import com.pie.whatsappstatussaver.StaticClass
import java.util.*

class AppOpenManager(private val myApplication: Application, private val context: Context) :
    LifecycleObserver {
    private var appOpenAd: AppOpenAd? = null
    private var loadCallback: AppOpenAd.AppOpenAdLoadCallback? = null
    private var loadTime: Long = 0

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    /**
     * Shows the ad if one isn't already showing.
     */
    fun showAdIfAvailable() {
        // Only show ad if there is not already an app open ad currently showing and an ad is available.
        if (!isShowingAd && isAdAvailable && !StaticClass.showinsititalornot) {
            Log.d(LOG_TAG, "Will show ad.")
            val fullScreenContentCallback: FullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        // Set the reference to null so isAdAvailable() returns false.
                        appOpenAd = null
                        isShowingAd = false
                        fetchAd()
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {}
                    override fun onAdShowedFullScreenContent() {
                        isShowingAd = true
                    }
                }
            appOpenAd!!.fullScreenContentCallback = fullScreenContentCallback
            if (!InsititialAdJavaClass.insitiailisshowing) {
                appOpenAd!!.show((context as Activity))
                //Toast.makeText(myApplication, "show open", Toast.LENGTH_SHORT).show();
            } else {
                 //Toast.makeText(myApplication, "show open nooot", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.d(LOG_TAG, "Can not show ad.")
            fetchAd()
        }
    }

    /**
     * Request an ad
     */
    fun fetchAd() {
        // Have unused ad, no need to fetch another.
        if (isAdAvailable) {
            return
        }
        loadCallback = object : AppOpenAd.AppOpenAdLoadCallback() {
            override fun onAdLoaded(ad: AppOpenAd) {
                appOpenAd = ad
                loadTime = Date().time
                Log.i(LOG_TAG, "Open app ad loaded successfully")
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                Log.i(LOG_TAG, "Failed to load open app ad: " + loadAdError.message)
            }
        }
        val s = SharedPrefRemote()
        var adidR = s.remoteValue("openAppId", context)
        if (adidR == "") {
            adidR = context.getString(R.string.app_open_ad_id)
            //Toast.makeText(activit, " if adid"+adidR, Toast.LENGTH_SHORT).show();
        }
        val request = AdRequest.Builder().build()
        AppOpenAd.load(
            context, adidR, request,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback as AppOpenAd.AppOpenAdLoadCallback
        )
    }

    /**
     * Utility method to check if ad was loaded more than n hours ago.
     */
    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
        val dateDifference = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * numHours
    }

    /**
     * Utility method that checks if ad exists and can be shown.
     */
    val isAdAvailable: Boolean
        get() = appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        showAdIfAvailable()
        Log.d(LOG_TAG, "onStart")
    }

    companion object {
        private const val LOG_TAG = "AppOpenManager"
        private const val AD_UNIT_ID = "ca-app-pub-6005767237808804/2457598523"
        private var isShowingAd = false
    }
}