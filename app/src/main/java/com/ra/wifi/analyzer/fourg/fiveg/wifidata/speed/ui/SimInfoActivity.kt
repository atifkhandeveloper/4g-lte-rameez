package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

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
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.databinding.ActivitySimInfoBinding
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.isAdEnable
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.FirebaseAds.AdmobAds
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adsManager.ADUnitPlacements
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adsManager.BannerAdsManager
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adsManager.NativeAdPair
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adsManager.loadCollapseBanner
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adsManager.loadNativeAds
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.ConfigParam

class SimInfoActivity : BaseActivity() {
    private val phoneStatePermissionCode = 101
    private var adView: AdView? = null

    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("Myrehgffs", Context.MODE_PRIVATE)
    }
    lateinit var binding: ActivitySimInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySimInfoBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        applyEdgeToEdgePadding(binding.root)
        setContentView(binding.root)




        checkPermission()
        toolBar()
        darkMode()

        if (PremiumManager.shouldShowAds(this)) {
            loadnative()
            loadBanner()
        }

//        nativeAds()
//        loadNativeAd()
    }

//    private fun nativeAds() {
//        MobileAds.initialize(this@SimInfoActivity)
//        val adLoader = AdLoader.Builder(this@SimInfoActivity, getString(R.string.sim_info_native))
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

    private fun darkMode() {
        val savedMode = sharedPreferences.getInt("MODE", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        when (savedMode) {
            AppCompatDelegate.MODE_NIGHT_YES -> {
                binding.toolBar.title.setTextColor(resources.getColor(R.color.white))
                binding.simOne.setTextColor(resources.getColor(R.color.green))
                binding.sim2.setTextColor(resources.getColor(R.color.green))
            }

//            AppCompatDelegate.MODE_NIGHT_NO -> {
//                binding.toolBar.title.setTextColor(resources.getColor(R.color.black))
//                binding.simOne.setTextColor(resources.getColor(R.color.blueC))
//                binding.sim2.setTextColor(resources.getColor(R.color.blueC))
//            }

            else -> {
            }
        }
    }

    private fun toolBar() {
        binding.toolBar.backBtn.setOnClickListener { onBackPressed() }
        binding.toolBar.removeAdsbtn.visibility = View.GONE
        binding.toolBar.title.setText(resources.getString(R.string.siminfo))
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_PHONE_STATE),
                phoneStatePermissionCode
            )
        } else {
            getSimDetails()
        }
    }


    private fun getSimDetails() {
        try {
            val subscriptionManager =
                getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager

            // Check for permission
// Check for permission
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val subscriptionInfoList: List<SubscriptionInfo> =
                    subscriptionManager.activeSubscriptionInfoList!!
                for ((index, subscriptionInfo) in subscriptionInfoList.withIndex()) {
                    val carrierName = subscriptionInfo.carrierName.toString()
                    val displayName = subscriptionInfo.displayName.toString()
                    val countryIso = subscriptionInfo.countryIso
                    val mnc = subscriptionInfo.mnc.toString()

                    // Determine SIM slot and set the details accordingly
                    when (index) {
                        0 -> { // SIM one
                            binding.carrierSimOne.setText(carrierName)
                            binding.countryCOdeSimOne.setText(countryIso)
                            binding.mncSimOneTv.setText(mnc)
                            binding.displayNameSimOne.setText(displayName)
                        }

                        1 -> { // SIM two
                            binding.carrierSimTwo.setText(carrierName)
                            binding.countryCOdeSimTwo.setText(countryIso)
                            binding.mncSimTwoTv.setText(mnc)
                            binding.displayNameSimTwo.setText(displayName)
                        }

                        else -> {
                            // Handle the possibility of more than two SIM slots if necessary
                        }
                    }
                }
            } else {
                println("Permission not granted")
            }
        } catch (e: Exception) {
        }
    }

//    private fun loadNativeAd() {
////        try {
////            AppDelegate.nativeAd?.apply {
////                NativeAdPair(this).populate(
////                    this@SimInfoActivity,
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
////            BannerAdsManager(this).loadCollapseBanner(binding.bannerAdView)
////        } catch (e: Exception) {
////            e.printStackTrace()
////        }
//
//
//    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == phoneStatePermissionCode && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getSimDetails()
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
}