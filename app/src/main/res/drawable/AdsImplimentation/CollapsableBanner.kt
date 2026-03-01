package com.pie.whatsappstatussaver.AdsImplimentation


import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.pie.whatsappstatussaver.AdsImplimentation.adsConstants.AdsConstants.getAdSizeAds
import com.pie.whatsappstatussaver.R
import com.pie.whatsappstatussaver.localization.BaseActivity


abstract class BaseCollapsableBannerActivity : BaseActivity() {
    private var bannerAd: AdView? = null
    private var adFrame: LinearLayout? = null

    private var isAdLoadCalled: Boolean = false
    private var isRequesting: Boolean = false

    fun loadCollapsableBannerAd(adFrame: LinearLayout) {
        this.adFrame = adFrame
        isAdLoadCalled = true
        loadSingleCollapsableBannerAd()
    }

    private fun destroyCollapsableBannerAd() {
        try {
            bannerAd?.removeAllViews()
        } catch (_: Exception) {
        }
        try {
            adFrame?.removeAllViews()
        } catch (_: Exception) {
        }

        bannerAd?.destroy()
        bannerAd = null
    }

    private fun loadSingleCollapsableBannerAd() {
        if (isAdLoadCalled) {
            adFrame?.let {
                if (bannerAd == null) {
                    try {
                        if (!isRequesting) {
                            isRequesting = true
                            it.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
                            try {
                                val collapseBannerAd = AdView(mContext).apply {
                                    this.adUnitId = getString(R.string.collapsible_banner_ad_id)
                                    this.setAdSize(getAdSizeAds(mContext))
                                    this.loadAd(
                                        AdRequest.Builder().addNetworkExtrasBundle(
                                            AdMobAdapter::class.java,
                                            Bundle().apply {
                                                putString("collapsible", "bottom")
                                            }).build()
                                    )
                                }

                                collapseBannerAd.adListener = object : AdListener() {
                                    override fun onAdLoaded() {
                                        super.onAdLoaded()
                                        if (isFinishing || isDestroyed || isChangingConfigurations) {
                                            collapseBannerAd.destroy()
                                            return
                                        }
                                        collapseBannerAd.adListener = object : AdListener() {}
                                        bannerAd = collapseBannerAd
                                        isRequesting = false
                                        adFrame?.visibility = View.VISIBLE
                                        adFrame?.removeAllViews()
                                        adFrame?.addView(bannerAd)
                                    }

                                    override fun onAdFailedToLoad(p0: LoadAdError) {
                                        super.onAdFailedToLoad(p0)
                                        bannerAd = null
                                        isRequesting = false
                                        if (isFinishing || isDestroyed || isChangingConfigurations) {
                                            collapseBannerAd.destroy()
                                            return
                                        }
                                        adFrame?.removeAllViews()
                                        adFrame?.visibility = View.GONE
                                    }
                                }
                            } catch (e: Exception) {
                                isRequesting = false
                            } catch (e: OutOfMemoryError) {
                                isRequesting = false
                            }
                        }
                    } catch (_: Exception) {
                    }
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        if (bannerAd == null) {
            loadSingleCollapsableBannerAd()
        }
        bannerAd?.resume()
    }


    override fun onPause() {
        super.onPause()
        bannerAd?.pause()
    }

    override fun onDestroy() {
        destroyCollapsableBannerAd()
        super.onDestroy()
    }

}