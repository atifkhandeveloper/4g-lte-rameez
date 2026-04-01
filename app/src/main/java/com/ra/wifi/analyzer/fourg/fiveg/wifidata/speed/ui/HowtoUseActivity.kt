package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
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
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.databinding.ActivityHowtoUseBinding
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.isAdEnable
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.ConfigParam

class HowtoUseActivity : BaseActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    // creating object of ViewPager
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
        setContentView(binding.root)



        loadnative()
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


    private fun loadnative(){

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