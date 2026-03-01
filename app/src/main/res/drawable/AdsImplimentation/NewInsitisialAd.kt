package com.pie.whatsappstatussaver.AdsImplimentation

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.encoders.json.BuildConfig
import com.pie.whatsappstatussaver.R
import com.pie.whatsappstatussaver.StaticClass.Companion.showinsititalornot

object NewInsitisialAd {

    var mInterstitialAdIsLoading: Boolean = false
    var interstitialAd: InterstitialAd? = null

    val TAG = "InterstitialAds_"
    fun intestritialloadAd(context: Context) {
        val adRequest = AdRequest.Builder().build()
        try {

            // Log.d("interstital id",RemoteConfigs.interstitial_ad_id)
            InterstitialAd.load(
                context, context.getString(R.string.interstitial_ad_id), adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {

                        interstitialAd = null
                        mInterstitialAdIsLoading = false
                        Log.d(TAG, adError.toString())
                        intestritialloadAd(context)

                    }

                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        this@NewInsitisialAd.interstitialAd = interstitialAd
                        mInterstitialAdIsLoading = true
                        Log.d("cvv", "onAdLoaded: ")
                    }
                }
            )
        } catch (e: Exception) {


        }

    }

    fun show(context: Activity, adDissmiss: () -> Unit) {
        try {
            if (interstitialAd != null) {
                Log.i("shownativead", "ini= show dia")
                showinsititalornot = true
                Handler(Looper.getMainLooper()).postDelayed({
                    interstitialAd?.show(context)
                }, 10)
                interstitialAd?.fullScreenContentCallback =
                    object : FullScreenContentCallback() {

                        override fun onAdDismissedFullScreenContent() {

                            Log.d(TAG, "onAdDismissedFullScreenContent: ")
                            adDissmiss()
                            // Don't forget to set the ad reference to null so you
                            // don't show the ad a second time.
                            interstitialAd = null
                            showinsititalornot = false

                            mInterstitialAdIsLoading = false

                            Handler(Looper.getMainLooper()).postDelayed({
                                intestritialloadAd(context)
                            }, 40000)
                        }

                        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                            Log.d(TAG, "onAdFailedToShowFullScreenContent: ")
                            // Don't forget to set the ad reference to null so you
                            // don't show the ad a second time.
                            interstitialAd = null
                            showinsititalornot = false

                            mInterstitialAdIsLoading = false
                        }

                        override fun onAdShowedFullScreenContent() {
                            Log.d(TAG, "onAdShowedFullScreenContent: ")
                            // Called when ad is dismissed.
                        }
                    }

            } else {
                adDissmiss()
            }

        } catch (e: Exception) {

        }


    }

}