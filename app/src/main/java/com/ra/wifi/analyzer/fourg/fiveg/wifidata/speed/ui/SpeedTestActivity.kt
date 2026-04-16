package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatDelegate

import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.BaseActivity
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.R
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.core.PremiumManager
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.databinding.ActivitySpeedTestBinding
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.FirebaseAds.AdmobAds
//import com.daimajia.androidanimations.library.Techniques
//import com.daimajia.androidanimations.library.YoYo
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.features.newScreen
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.isAdEnable
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.ConfigParam

class SpeedTestActivity : BaseActivity() {
    lateinit var binding: ActivitySpeedTestBinding
    private var isFirstTime = true
    private var lastAdShownTime = 0L
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("Myrehgffs", Context.MODE_PRIVATE)
    }
    private var pendingAction: (() -> Unit)? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySpeedTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        NativeAdsManager.CheckNative(this, window.decorView.rootView)

//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        goSeedTest()
        toolBar()
        darkMode()
        if (PremiumManager.shouldShowAds(this)) {
            loadnative()
        }

//        loadNativeAd()
//        nativeAds()

        // Load the pulse animation from the XML file
//        val pulseAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.pulse_animation)
//
//        // Set the animation to the TextView
//        binding.textView.startAnimation(pulseAnimation)

    }

//    private fun nativeAds() {
//        MobileAds.initialize(this@SpeedTestActivity)
//        val adLoader = AdLoader.Builder(this@SpeedTestActivity, getString(R.string.speed_test_and_data_usage_native))
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
                binding.textView2.setTextColor(resources.getColor(R.color.white))
                binding.serverTxt.setTextColor(resources.getColor(R.color.white))
                binding.ipTxt.setTextColor(resources.getColor(R.color.white))
                binding.serverImg.setColorFilter(getColor(R.color.white), PorterDuff.Mode.SRC_IN)
                binding.ipImg.setColorFilter(getColor(R.color.white), PorterDuff.Mode.SRC_IN)

            }
//            AppCompatDelegate.MODE_NIGHT_NO -> {
//                binding.textView2.setTextColor(resources.getColor(R.color.white))
//                binding.toolBar.title.setTextColor(resources.getColor(R.color.white))
//                binding.serverTxt.setTextColor(resources.getColor(R.color.black))
//                binding.ipTxt.setTextColor(resources.getColor(R.color.black))
//                binding.serverImg.setColorFilter(getColor(R.color.black), PorterDuff.Mode.SRC_IN)
//                binding.ipImg.setColorFilter(getColor(R.color.black), PorterDuff.Mode.SRC_IN)
//            }
            else -> {
            }
        }
    }

    private fun toolBar() {

        binding.toolBar.removeAdsbtn.visibility=View.GONE
        binding.toolBar.backBtn.setOnClickListener { onBackPressed() }
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

//    private fun goSeedTest() {
//        binding.textView.setOnClickListener {
//            binding.textView.visibility = View.GONE
//            binding.textView2.visibility = View.VISIBLE
//            binding.progressBar.visibility = View.VISIBLE
//
//            // Stop the animation
//            binding.textView.clearAnimation()
//
//            // Delay for 3 seconds
//            Handler(Looper.getMainLooper()).postDelayed({
//                // Make the TextView visible after 3 seconds
//
//                newScreen(NetSpeedActivity::class.java)
//
//                // Restore the visibility of TextView and hide others
//                binding.textView.visibility = View.VISIBLE
//                binding.textView2.visibility = View.GONE
//                binding.progressBar.visibility = View.GONE
//
//                // Restart the animation
//                val pulseAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.pulse_animation)
//                binding.textView.startAnimation(pulseAnimation)
//
//            }, 3000) // 3000 milliseconds = 3 seconds
//        }
//    }


    private fun goSeedTest() {
        binding.textView.setOnClickListener {
            binding.textView.visibility= View.GONE
            binding.textView2.visibility= View.VISIBLE
            binding.progressBar.visibility= View.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                // Make the TextView visible after 3 seconds

                 newScreen(NetSpeedActivity::class.java)
                 binding.textView.visibility= View.VISIBLE
                 binding.textView2.visibility= View.INVISIBLE
                 binding.progressBar.visibility= View.INVISIBLE

            }, 2000)
        }
    }
//    private fun loadNativeAd() {
//        try {
//            AppDelegate.nativeAd?.apply {
//                NativeAdPair(this).populate(
//                    this@SpeedTestActivity,
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

}