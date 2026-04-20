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
import com.google.android.gms.ads.nativead.NativeAd
import com.google.firebase.analytics.FirebaseAnalytics
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
    private var nativeAd: NativeAd? = null

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

        if (PremiumManager.isPremium(this)) return

        val template = findViewById<TemplateView>(R.id.my_template)

        MobileAds.initialize(this)

        val adLoader = AdLoader.Builder(this, getString(R.string.nativeId))
            .forNativeAd { ad ->

                // 🔥 destroy previous ad first
                nativeAd?.destroy()
                nativeAd = ad

                // ✅ ADD THIS BLOCK HERE
                ad.setOnPaidEventListener { adValue ->
                    val revenue = adValue.valueMicros / 1_000_000.0
                    val currency = adValue.currencyCode

                    Log.d("Ads", "Native Revenue: $revenue $currency")

                    sendRevenueToFirebase(revenue, currency)
                }

                val styles = NativeTemplateStyle.Builder()
                    .withMainBackgroundColor(ColorDrawable(Color.WHITE))
                    .build()

                template.setStyles(styles)
                template.setNativeAd(ad)
                template.visibility = View.VISIBLE
            }
            .withAdListener(object : AdListener() {

                override fun onAdFailedToLoad(error: LoadAdError) {
                    template.visibility = View.GONE
                }
            })
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }



    fun sendRevenueToFirebase(value: Double, currency: String) {
        val bundle = Bundle().apply {
            putDouble("value", value)
            putString("currency", currency)
            putString("ad_platform", "admob")
            putString("ad_source", "admob")
            putString("ad_format", "native") // IMPORTANT
        }

        FirebaseAnalytics.getInstance(this)
            .logEvent("ad_impression", bundle)
    }

    private fun loadBanner() {
        val adView = AdView(this)
        adView.adUnitId = resources.getString(R.string.bannerId)

        adView.setAdSize(AdSize.getLargeAnchoredAdaptiveBannerAdSize(this, 360))
        this.adView = adView

        binding.adViewContainer.removeAllViews()
        binding.adViewContainer.addView(adView)

        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                Log.d(TAG, "Ad loaded.")
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                Log.i(TAG, "Ad failed to load: ${error.message}")
            }

            override fun onAdImpression() {
                Log.d(TAG, "Ad recorded an impression.")
            }
        }

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        // ✅ ADD THIS (VERY IMPORTANT)
        adView.setOnPaidEventListener { adValue ->
            val revenue = adValue.valueMicros / 1_000_000.0
            val currency = adValue.currencyCode

            Log.d(TAG, "Banner Revenue: $revenue $currency")

            sendRevenueToFirebase(revenue, currency)
        }
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