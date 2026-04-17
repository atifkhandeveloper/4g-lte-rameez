package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate

import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.BaseActivity
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.R
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.core.PremiumManager
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.databinding.ActivityLteSettingsBinding
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.isAdEnable
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.FirebaseAds.AdmobAds
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adsManager.BannerAdsManager
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.ConfigParam
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.NetSpeed
import dev.jahidhasanco.networkusage.Interval
import dev.jahidhasanco.networkusage.NetworkType
import dev.jahidhasanco.networkusage.NetworkUsageManager
import dev.jahidhasanco.networkusage.Util

class LteSettingsActivity : BaseActivity() {
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("Myrehgffs", Context.MODE_PRIVATE)
    }
    lateinit var binding: ActivityLteSettingsBinding
    private var adView: AdView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLteSettingsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        applyEdgeToEdgePadding(binding.root)
        setContentView(binding.root)

//        NativeAdsManager.CheckNative(this, window.decorView.rootView)


        toolbar()
        darkMode()

        if (PremiumManager.shouldShowAds(this)) {
            loadnative()
            loadBanner()
        }

//        loadNativeAd()
//        nativeAds()

        // Load the pulse animation from the XML file
//        val pulseAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.pulse_animation)
//
//        // Set the animation to the below and above android 11
//        binding.below11Android.startAnimation(pulseAnimation)
//        binding.above11Android.startAnimation(pulseAnimation)

        val networkUsage = NetworkUsageManager(this, Util.getSubscriberId(this))
        val handler = Handler(Looper.getMainLooper())
        val runnableCode = object : Runnable {
            override fun run() {
                val now = networkUsage.getUsageNow(NetworkType.ALL)
                val speeds = NetSpeed.calculateSpeed(now.timeTaken, now.downloads, now.uploads)
                val todayM = networkUsage.getUsage(Interval.today, NetworkType.MOBILE)
                val todayW = networkUsage.getUsage(Interval.today, NetworkType.WIFI)

//                binding.wifiUsagesTv.text = "WiFi: " + Util.formatData(todayW.downloads, todayW.uploads)[2]
//                binding.dataUsagesTv.text = "Mobile: " + Util.formatData(todayM.downloads, todayM.uploads)[2]
                binding.apply {
                    incomingTv.text = "${speeds[1].speed + speeds[1].unit}"
                    outgoingTv.text = "${speeds[2].speed + speeds[2].unit}"
                }
                var speedPercentage: Float
                try {
                    speedPercentage =
                        speeds[1].speed.toFloat() // Convert the String speed value to Float
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                    speedPercentage = 0f // default value in case of a conversion error
                }
                handler.postDelayed(this, 1000)

            }
        }

        runnableCode.run()
    }

    private fun darkMode() {
        val savedMode = sharedPreferences.getInt("MODE", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        when (savedMode) {
            AppCompatDelegate.MODE_NIGHT_YES -> {
                binding.toolBar.title.setTextColor(resources.getColor(R.color.white))
                binding.spped.setTextColor(resources.getColor(R.color.white))
                binding.data.setTextColor(resources.getColor(R.color.white))
//                binding.speedDarkTv.visibility= View.VISIBLE
//                binding.darkImage.visibility= View.VISIBLE
//                binding.spped.visibility= View.GONE
//                binding.below11Whh.visibility= View.GONE
//                binding.below11IconeW.visibility= View.GONE

                binding.spped.setBackgroundResource(R.drawable.round_shape_dark)
                binding.data.setBackgroundResource(R.drawable.round_shape_dark)

                binding.arrowDown.setBackgroundResource(R.drawable.lte_arrow_down_ward_white)
                binding.arrowUp.setBackgroundResource(R.drawable.lte_arrow_upward_white)


//                binding.darkImageAbove.visibility= View.VISIBLE
//                binding.above11Tv.visibility= View.VISIBLE
//                binding.data.visibility= View.GONE
//                binding.above11W.visibility= View.GONE
//                binding.above11WW.visibility= View.GONE
            }

            AppCompatDelegate.MODE_NIGHT_NO -> {
//                binding.speedDarkTv.visibility= View.GONE
//                binding.darkImage.visibility= View.GONE
//                binding.spped.visibility= View.VISIBLE
//                binding.below11Whh.visibility= View.VISIBLE
//                binding.below11IconeW.visibility= View.VISIBLE

//                binding.darkImageAbove.visibility= View.GONE
//                binding.above11Tv.visibility= View.GONE
//                binding.data.visibility= View.VISIBLE
//                binding.above11W.visibility= View.VISIBLE
//                binding.above11WW.visibility= View.VISIBLE
                binding.toolBar.title.setTextColor(resources.getColor(R.color.black))

                binding.arrowDown.setBackgroundResource(R.drawable.lte_arrow_down_ward)
                binding.arrowUp.setBackgroundResource(R.drawable.lte_arrow_upward)
            }

            else -> {
            }
        }
    }

    private fun toolbar() {
        binding.toolBar.backBtn.setOnClickListener { onBackPressed() }
        binding.toolBar.title.setText(resources.getString(R.string.fourgSettings))
        binding.toolBar.removeAdsbtn.visibility = View.GONE

        binding.below11Android.setOnClickListener {
            try {
                val intent = Intent("android.intent.action.MAIN")
                intent.setClassName("com.android.settings", "com.android.settings.RadioInfo")
                startActivity(intent)
                return@setOnClickListener
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    this,
                    "Sorry! it seems that your phone doesn't support this feature",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
        }

        binding.above11Android.setOnClickListener {
            try {
                val intent = Intent("android.intent.action.MAIN")
                intent.setClassName("com.android.phone", "com.android.phone.settings.RadioInfo")
                startActivity(intent)
                return@setOnClickListener
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    this,
                    "Sorry! it seems that your phone doesn't support this feature",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
        }

    }

    private fun loadnative() {

        MobileAds.initialize(this)

        val background = ColorDrawable(Color.WHITE)

        val template = findViewById<TemplateView>(R.id.my_template)

        // default hidden until ad loads
        template.visibility = View.GONE

        val adLoader = AdLoader.Builder(this, resources.getString(R.string.nativeId))
            .forNativeAd { nativeAd ->

                val styles = NativeTemplateStyle.Builder()
                    .withMainBackgroundColor(background)
                    .build()

                template.setStyles(styles)
                template.setNativeAd(nativeAd)

                // ✅ SHOW when ad is loaded
                template.visibility = View.VISIBLE
            }
            .withAdListener(object : com.google.android.gms.ads.AdListener() {

                override fun onAdFailedToLoad(loadAdError: com.google.android.gms.ads.LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)

                    // ❌ HIDE when ad fails
                    template.visibility = View.GONE
                }
            })
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun loadBanner() {
        // [START create_ad_view]
        // Create a new ad view.
        val adView = AdView(this)
        adView.adUnitId = resources.getString(R.string.bannerId)
        // [START set_ad_size]
        // Request a large anchored adaptive banner with a width of 360.
        adView.setAdSize(AdSize.getLargeAnchoredAdaptiveBannerAdSize(this, 360))
        // [END set_ad_size]
        this.adView = adView

        // Replace ad container with new ad view.
        binding.adViewContainer.removeAllViews()
        binding.adViewContainer.addView(adView)
        // [END create_ad_view]

        // Listen for ad events.
        adView.adListener =
            object : AdListener() {
                override fun onAdLoaded() {
                    // Called when an ad is loaded.
                    Log.d(TAG, "Ad loaded.")
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    // Called when an ad request failed.
                    Log.i(TAG, "Ad failed to load: ${error.message}")
                }

                override fun onAdOpened() {
                    // Called when an ad opens an overlay that covers the screen.
                    Log.d(TAG, "Ad opened.")
                }

                override fun onAdClicked() {
                    // Called when a click is recorded for an ad.
                    Log.d(TAG, "Ad clicked.")
                }

                override fun onAdImpression() {
                    // Called when an impression is recorded for an ad.
                    Log.d(TAG, "Ad recorded an impression.")
                }

                override fun onAdClosed() {
                    // Called when the user is about to return to the application
                    // after tapping on an ad.
                    Log.d(TAG, "Ad closed.")
                }
            }

        // [START load_ad]
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        // [END load_ad]
    }


//    private fun nativeAds() {
//        MobileAds.initialize(this@LteSettingsActivity)
//        val adLoader = AdLoader.Builder(this@LteSettingsActivity, getString(R.string.lte_native))
//            .forNativeAd { nativeAd ->
//                val styles = NativeTemplateStyle.Builder()
//                    .withMainBackgroundColor(ColorDrawable(Color.parseColor("#EFEDED")))
//                    .build()
//                val template = findViewById<TemplateView>(R.id.my_storage_template)
//                val ad_container = findViewById<ConstraintLayout>(R.id.ad_container)
//                template.setStyles(styles)
//                template.setNativeAd(nativeAd)
//                template.visibility = View.VISIBLE
//                ad_container.visibility = View.VISIBLE
//            }
//            .build()
//        adLoader.loadAd(AdRequest.Builder().build())
//    }

//    private fun loadNativeAd() {
////        try {
////            AppDelegate.nativeAd?.apply {
////                NativeAdPair(this).populate(
////                    this@LteSettingsActivity,
////                    R.layout.ad_unified_banner,
////                    binding.adContainer
////                )
////                (application as AppDelegate).loadAdNativeBottomMain()
////            } ?: run {
////                loadNativeAds(
////                    binding.adContainer,
////                    R.layout.ad_unified_banner,
////                    ADUnitPlacements.NATIVE_AD
////                )
////            }
////        } catch (e: Exception) {
////            e.printStackTrace()
////        }
//
////        try {
////            BannerAdsManager(this).loadCollapseBanner(binding.bannerAdView)
////        } catch (e: Exception) {
////            e.printStackTrace()
////        }
//    }
}