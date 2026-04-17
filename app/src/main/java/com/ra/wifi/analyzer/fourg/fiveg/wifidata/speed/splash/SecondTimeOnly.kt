package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.splash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds

import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.BaseActivity
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.R
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.core.PremiumManager
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.databinding.ActivitySecondTimeOnlyBinding
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.isAdEnable
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui.AnimationActivity
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.FirebaseAds.AdmobAds
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui.MainActivity
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui.PremiumActivity
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.ConfigParam

class SecondTimeOnly : BaseActivity() {

    var stableData = false
    var impoveStreaming = false
    var onlineGame = false
    var videoCalls = false
    var fastBrowser = false
    var networkSwitch = false
    var counter = 0
    lateinit var binding: ActivitySecondTimeOnlyBinding
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("Myrehgffs", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondTimeOnlyBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        applyEdgeToEdgePadding(binding.root)
        setContentView(binding.root)

        if (PremiumManager.shouldShowAds(this)) {
            loadnative()
        }




        binding.nextBtn.setOnClickListener {
            if (counter == 0) {
                Toast.makeText(this, "Please Select Any Category", Toast.LENGTH_SHORT).show()
            } else {

                    startActivity(Intent(this@SecondTimeOnly, PremiumActivity::class.java))
                    finish()


            }
        }
        handerSelection()
        darkMode()
    }

    private fun darkMode() {
        val savedMode = sharedPreferences.getInt("MODE", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        when (savedMode) {
            AppCompatDelegate.MODE_NIGHT_YES -> {
                binding.mainSecond.setBackgroundColor(Color.DKGRAY)
                binding.l.setBackgroundColor(Color.DKGRAY)
                binding.nextBtn.setImageResource(R.drawable.tick_white)
                // Your existing code for dark mode
            }

            AppCompatDelegate.MODE_NIGHT_NO -> {
                binding.mainSecond.setBackgroundColor(Color.WHITE)
                binding.l.setBackgroundColor(Color.WHITE)
                binding.nextBtn.setImageResource(R.drawable.tick_black)
                // Your existing code for light mode
            }

//            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> {
//                // Update saved mode based on system mode
//                val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
//                val newSavedMode = when (nightModeFlags) {
//                    Configuration.UI_MODE_NIGHT_YES -> {
//                        showCustomToast("da")
//                    }
//                    Configuration.UI_MODE_NIGHT_NO -> {
//                        showCustomToast("whi")
//
//                    }
//                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
//                }
//
//                // Trigger theme update based on new saved mode
//                darkMode() // Recursively call to apply updated theme
//            }

        }
    }

//    private fun nativeAds() {
//        MobileAds.initialize(this@SecondTimeOnly)
//        val adLoader = AdLoader.Builder(this@SecondTimeOnly, getString(R.string.on_boarding_native))
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

    private fun handerSelection() {
        binding.brScannerBox.setOnClickListener {
            if (!videoCalls) {
                counter++
                videoCalls = true
                binding.brScannerBox.setImageResource(
                    R.drawable.baseline_check_box_fill
                )
            } else {
                counter--
                videoCalls = false
                binding.brScannerBox.setImageResource(
                    R.drawable.baseline_check_box_outline_blank_24
                )
            }
        }
        binding.qrScannerBox.setOnClickListener {
            if (!impoveStreaming) {
                counter++
                impoveStreaming = true
                binding.qrScannerBox.setImageResource(
                    R.drawable.baseline_check_box_fill
                )
            } else {
                counter--
                impoveStreaming = false
                binding.qrScannerBox.setImageResource(
                    R.drawable.baseline_check_box_outline_blank_24
                )
            }
        }
        binding.brCodeReaderBox.setOnClickListener {
            if (!onlineGame) {
                counter++
                onlineGame = true
                binding.brCodeReaderBox.setImageResource(
                    R.drawable.baseline_check_box_fill
                )
            } else {
                counter--
                onlineGame = false
                binding.brCodeReaderBox.setImageResource(
                    R.drawable.baseline_check_box_outline_blank_24
                )
            }
        }
        binding.qrCodeReaderBox.setOnClickListener {
            if (!stableData) {
                counter++
                stableData = true
                binding.qrCodeReaderBox.setImageResource(
                    R.drawable.baseline_check_box_fill
                )
            } else {
                counter--
                stableData = false
                binding.qrCodeReaderBox.setImageResource(
                    R.drawable.baseline_check_box_outline_blank_24
                )
            }
        }
        binding.brCodeMakerBox.setOnClickListener {
            if (!networkSwitch) {
                counter++
                networkSwitch = true
                binding.brCodeMakerBox.setImageResource(
                    R.drawable.baseline_check_box_fill
                )
            } else {
                counter--
                networkSwitch = false
                binding.brCodeMakerBox.setImageResource(
                    R.drawable.baseline_check_box_outline_blank_24
                )
            }
        }
        binding.qrMakerBox.setOnClickListener {
            if (!fastBrowser) {
                counter++
                fastBrowser = true
                binding.qrMakerBox.setImageResource(
                    R.drawable.baseline_check_box_fill
                )
            } else {
                counter--
                fastBrowser = false
                binding.qrMakerBox.setImageResource(
                    R.drawable.baseline_check_box_outline_blank_24
                )
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
//    private fun showAdNativeBottomMain() {
//        try {
//            AppDelegate.nativeAdOther?.apply {
//                NativeAdPair(this).populate(
//                    this@HowtoUseActivity,
//                    R.layout.ad_unified_banner,
//                    binding.adsPlaceHolder
//                )
//                AppDelegate.nativeAdOther = null
//                (application as AppDelegate).loadAdNativeBottomMain()
//            } ?: run {
//                loadNativeAds(null,
//                    R.layout.ad_unified_banner,
//                    ADUnitPlacements.NATIVE_AD,
//                    AMCallback = {
//                        binding.adsPlaceHolder?.removeAllViews()
//                        NativeAdPair(it).populate(
//                            this@HowtoUseActivity,
//                            R.layout.ad_unified_banner,
//                            binding.adsPlaceHolder
//                        )
//                    },
//                    onError = {
//                        binding.adsPlaceHolder?.visibility = View.GONE
//                    })
//
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
}