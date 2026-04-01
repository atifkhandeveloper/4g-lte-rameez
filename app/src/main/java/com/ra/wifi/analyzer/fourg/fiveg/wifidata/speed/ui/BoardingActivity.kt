package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.viewpager.widget.ViewPager

import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.BaseActivity
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.R
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.FirebaseAds.AdmobAds
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adapters.ImageSliderAdapter
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.databinding.ActivityBoardingBinding
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.isAdEnable
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adsManager.ADUnitPlacements
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adsManager.NativeAdPair
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adsManager.loadNativeAds
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.ConfigParam
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.NewScreen
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.SharedPrefObj

class BoardingActivity : BaseActivity() {

    // creating object of ViewPager
    private lateinit var mViewPager: ViewPager

    // images array
    lateinit var binding: ActivityBoardingBinding
    private val images = intArrayOf(
        R.drawable.screenon, R.drawable.screentwo, R.drawable.screenthre, R.drawable.screenfour
    )

    // Creating Object of ViewPagerAdapter
    private lateinit var mViewPagerAdapter: ImageSliderAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        loadnative()


//        NativeAdsManager.CheckNative(this, window.decorView.rootView)

//        loadNativeAd()

        binding.doneBtn.setOnClickListener {
            val nextItem = mViewPager.currentItem + 1
            if (nextItem < mViewPagerAdapter.count) {
                mViewPager.currentItem = nextItem
            }
        }
        binding.skipBtn.setOnClickListener {
            SharedPrefObj.saveAuthToken(this@BoardingActivity, "UserRegistered")
            NewScreen.start(this, MainActivity::class.java)
            finish()
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                )
        // Initializing the ViewPager Object
        mViewPager = findViewById(R.id.viewPager)
        // Initializing the ViewPagerAdapter
        mViewPagerAdapter = ImageSliderAdapter(this, images)
        // Adding the Adapter to the ViewPager
        mViewPager.adapter = mViewPagerAdapter

        binding.dotsIndicator.attachTo(mViewPager)

        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                // Not needed for this implementation
            }

            override fun onPageSelected(position: Int) {
                if (position == 2) {
                    binding.doneBtn.setOnClickListener {
                        SharedPrefObj.saveAuthToken(this@BoardingActivity, "UserRegistered")
                        NewScreen.start(this@BoardingActivity, MainActivity::class.java)
                        finish()
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                // Not needed for this implementation
            }
        })

//        nativeAds()
    }

//    private fun nativeAds() {
//        MobileAds.initialize(this@BoardingActivity)
//        val adLoader = AdLoader.Builder(this@BoardingActivity, getString(R.string.how_to_use_native))
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
//        try {
//            AppDelegate.nativeAd?.apply {
//                NativeAdPair(this).populate(
//                    this@BoardingActivity,
//                    R.layout.ad_unified_banner,
//                    binding.adContainer
//                )
//                (application as AppDelegate).loadAdNativeBottomMain()
//            } ?: run {
//                loadNativeAds(
//                    binding.adContainer,
//                    R.layout.ad_unified_banner,
//                    ADUnitPlacements.NATIVE_AD
//                )
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

    fun getItem(i: Int): Int {
        return mViewPager.currentItem
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


    private fun loadnative() {

        MobileAds.initialize(this)

// Optional: set background color
        val background = ColorDrawable(Color.WHITE)

// Create AdLoader
        val adLoader = AdLoader.Builder(this, resources.getString(R.string.nativeId))
            .forNativeAd { nativeAd ->

                val styles = NativeTemplateStyle.Builder()
                    .withMainBackgroundColor(background)
                    .build()

                val template = findViewById<TemplateView>(R.id.my_template)
                template.setStyles(styles)
                template.setNativeAd(nativeAd)
            }
            .build()

// Load Ad
        adLoader.loadAd(AdRequest.Builder().build())
    }
}
