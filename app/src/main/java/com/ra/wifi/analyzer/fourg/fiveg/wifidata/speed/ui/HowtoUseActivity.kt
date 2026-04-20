package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewpager.widget.ViewPager

import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.nativead.NativeAd
import com.google.firebase.analytics.FirebaseAnalytics
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.BaseActivity
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.R
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.FirebaseAds.AdmobAds
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adapters.ImageSliderAdapter
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.core.PremiumManager
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.databinding.ActivityHowtoUseBinding
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.isAdEnable
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.ConfigParam

class HowtoUseActivity : BaseActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    // creating object of ViewPager
    private var nativeAd: NativeAd? = null
    private lateinit var mViewPager: ViewPager

    // images array
    lateinit var binding: ActivityHowtoUseBinding
    private val images = intArrayOf(
        R.drawable.screenon, R.drawable.screentwo, R.drawable.screenthre, R.drawable.screenfour
    )

    // Creating Object of ViewPagerAdapter
    private lateinit var mViewPagerAdapter: ImageSliderAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHowtoUseBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        applyEdgeToEdgePadding(binding.root)
        setContentView(binding.root)

        if (PremiumManager.shouldShowAds(this)) {
            loadnative()
        }

//        nativeAds()
//        NativeAdsManager.CheckNative(this, window.decorView.rootView)

        sharedPreferences = getSharedPreferences("Myrehgffs", Context.MODE_PRIVATE)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding.doneBtn.setOnClickListener {
            val nextItem = mViewPager.currentItem + 1
            if (nextItem < mViewPagerAdapter.count) {
                mViewPager.currentItem = nextItem
            } else {
                finish()
            }
        }
        binding.skipBtn.setOnClickListener {
            val previousItem = mViewPager.currentItem - 1
            if (previousItem >= 0) {
                mViewPager.currentItem = previousItem
            } else {

            }
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
                if (position == 0) {
                    binding.skipBtn.setOnClickListener { onBackPressed() }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                // Not needed for this implementation
            }
        })
    }

//    private fun nativeAds() {
//        MobileAds.initialize(this@HowtoUseActivity)
//        val adLoader = AdLoader.Builder(this@HowtoUseActivity, getString(R.string.how_to_use_native))
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


            }

//            AppCompatDelegate.MODE_NIGHT_NO -> {
//
//            }

            else -> {
            }
        }
    }


    fun getItem(i: Int): Int {
        return mViewPager.currentItem
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

}