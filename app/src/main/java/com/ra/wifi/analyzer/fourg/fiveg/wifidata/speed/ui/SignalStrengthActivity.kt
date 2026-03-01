package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.BaseActivity
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.R
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.databinding.ActivitySignalStrengthBinding
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.isAdEnable
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.FirebaseAds.AdmobAds
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adsManager.ADUnitPlacements
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adsManager.BannerAdsManager
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adsManager.NativeAdPair
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adsManager.loadCollapseBanner
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adsManager.loadNativeAds
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.new_ads_manager.NativeAdsManager
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.new_ads_manager.CollapsibleBanner
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.ConfigParam
import timber.log.Timber

class SignalStrengthActivity : BaseActivity() {
    lateinit var binding: ActivitySignalStrengthBinding
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("Myrehgffs", Context.MODE_PRIVATE)
    }
    private val REQUEST_CODE_PERMISSIONS = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignalStrengthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nativeAdId = getString(R.string.nativeId) // Your Native Ad ID
        NativeAdsManager.ReqLoadNativeAd(
            config.isAdEnable(ConfigParam.NATIVE_SIGNAL_STRENGTH),
            this,
            window.decorView.rootView,
            nativeAdId
        )
//        NativeAdsManager.CheckNative(this, window.decorView.rootView)
        CollapsibleBanner.loadBanner(
            this,
            binding.bannerContainer,
            config.isAdEnable(ConfigParam.BANNER_SIGNAL_STRENGTH)
        )

//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        toolBar()
        darkMode()
//        loadNativeAd()
//        nativeAds()
        if (arePermissionsGranted()) {
            val networkStrength = getNetworkStrength()
            val (strength1, strength2) = getNetworkStrength()
            updateSpeedMeter(strength1, strength2)
            // Do something with the network strength
        } else {
            requestPermissions()
        }

//        binding.pointerSpeedometerPer.text = "Yes Sirr"

    }

//    private fun nativeAds() {
//        MobileAds.initialize(this@SignalStrengthActivity)
//        val adLoader = AdLoader.Builder(this@SignalStrengthActivity, getString(R.string.signal_strength_native))
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
                binding.incomingText.setTextColor(resources.getColor(R.color.white))
                binding.pointerSpeedometerText.setTextColor(resources.getColor(R.color.white))
            }

            AppCompatDelegate.MODE_NIGHT_NO -> {
                binding.toolBar.title.setTextColor(resources.getColor(R.color.black))
                binding.incomingText.setTextColor(resources.getColor(R.color.black))
                binding.pointerSpeedometerText.setTextColor(resources.getColor(R.color.black))
            }

            else -> {
            }
        }
    }

    private fun toolBar() {
        binding.toolBar.backBtn.setOnClickListener { onBackPressed() }
        binding.toolBar.removeAdsbtn.visibility = View.GONE
        binding.toolBar.title.setText(resources.getString(R.string.signlaStrengh))
    }

    private fun arePermissionsGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_PHONE_STATE
            ),
            REQUEST_CODE_PERMISSIONS
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_PERMISSIONS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    val networkStrength = getNetworkStrength()
                    // Do something with the network strength

                    val (strength1, strength2) = getNetworkStrength()
                    updateSpeedMeter(strength1, strength2)
                    // Do something with the network strength

                } else {
                    Timber.e("Permissions denied.")
                }
                return
            }
        }
    }

    private fun dbmToPercentage(dbm: Int): Int {
        // Signal strength is usually in the range of -120 dBm (poor) to -40 dBm (excellent).
        // Anything lower than -120 dBm will be considered as 0%.
        // Anything higher than -40 dBm will be considered as 100%.
        if (dbm <= -120) return 0 // Poor signal
        if (dbm >= -40) return 100 // Excellent signal
        // Convert dBm to percentage
        // (dbm + 120) is the signal strength on a scale from 0 to 80.
        // We then divide by 80 to normalize it to a value between 0 and 1.
        // Finally, we multiply by 100 to convert it to a percentage.
        return ((dbm + 120) * 100) / 80
    }

//    private fun getNetworkStrength(): Pair<Int, Int> {
//        var strength1 = -1
//        var strength2 = -1
//
//        val manager = getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val telephonyManager = applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//
//            // Check if permissions are granted, return default values if not
//            if (ActivityCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.READ_PHONE_STATE
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                return Pair(strength1, strength2)
//            }
//
//            val subscriptionInfoList = manager.activeSubscriptionInfoList
//            if (subscriptionInfoList != null) {
//                for (info in subscriptionInfoList) {
//                    val subscriptionId = info.subscriptionId
//                    val cellInfoList = telephonyManager.allCellInfo
//
//                    if (cellInfoList != null) {
//                        for (cellInfo in cellInfoList) {
//                            if (cellInfo is android.telephony.CellInfoGsm) {
//                                val cellSignalStrength = cellInfo.cellSignalStrength
//                                val signalStrength = cellSignalStrength.dbm
//
//                                if (info.simSlotIndex == 0) {
//                                    strength1 = signalStrength
//                                } else if (info.simSlotIndex == 1) {
//                                    strength2 = signalStrength
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        Timber.i("final strength   sim1 $strength1  sim2 $strength2")
//        return Pair(strength1, strength2)
//    }

    private fun getNetworkStrength(): Pair<Int, Int> {
        var strength1 = -1
        var strength2 = -1

        val manager =
            getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val telephonyManager =
                applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

            // Check if permissions are granted, return default values if not
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return Pair(strength1, strength2)
            }

            if (telephonyManager.allCellInfo != null) {
                // ... (rest of your code)
            }
        }

        Timber.i("final strength   sim1 $strength1  sim2 $strength2")
        return Pair(strength1, strength2)
    }


    // ...

    private fun updateSpeedMeter(strength1: Int, strength2: Int) {
        val percentage1 = dbmToPercentage(strength1)
        val percentage2 = dbmToPercentage(strength2)

        val handler = Handler(Looper.getMainLooper())

        // Post a delayed Runnable to run after one second (1000 milliseconds)
        handler.postDelayed({
            binding.pointerSpeedometerOne.setSpeedAt(percentage1.toFloat())
            binding.pointerSpeedometerTwo.setSpeedAt(percentage2.toFloat())
            binding.incomingText.text =
                "${resources.getString(R.string.sim1Sg)} -${percentage1} dBm"
            binding.pointerSpeedometerText.text =
                "${resources.getString(R.string.sim2Sg)} -${percentage2} dBm"

            // Update the live percentage in the pointerSpeedometerPer TextView
            binding.pointerSpeedometerPer.text =
                "Live Percentage: ${(percentage1 + percentage2) / 2}%"
        }, 1000)
    }

// ...


//    private fun updateSpeedMeter(strength1: Int, strength2: Int) {
//        val percentage1 = dbmToPercentage(strength1)
//        val percentage2 = dbmToPercentage(strength2)
//
//        val handler = Handler(Looper.getMainLooper())
//
//        // Post a delayed Runnable to run after one second (1000 milliseconds)
//        handler.postDelayed({
//            binding.pointerSpeedometerOne.setSpeedAt(percentage1.toFloat())
//            binding.pointerSpeedometerTwo.setSpeedAt(percentage2.toFloat())
//            binding.incomingText.text="${resources.getString(R.string.sim1Sg)} -${percentage1} dBm"
//            binding.pointerSpeedometerText.text="${resources.getString(R.string.sim2Sg)} -${percentage2} dBm"
//        }, 1000)
//
//    }

//    private fun loadNativeAd() {
////        try {
////            AppDelegate.nativeAd?.apply {
////                NativeAdPair(this).populate(
////                    this@SignalStrengthActivity,
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
//    }
}
