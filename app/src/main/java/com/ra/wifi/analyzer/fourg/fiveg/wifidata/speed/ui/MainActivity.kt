package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.BaseActivity
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.features.newScreen
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.isAdEnable
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.new_ads_manager.CollapsibleBanner
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.new_ads_manager.InterstitialAdsManager
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.new_ads_manager.NativeAdsManager
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.settings.AppLanguageObj
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.ConfigParam
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.NewScreen
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.Setting
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.R
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    val appL by lazy { AppLanguageObj() }
    lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var isFirstTime = true
    private var lastAdShownTime = 0L
    private var pendingAction: (() -> Unit)? = null
    private var mInterstitialAd: InterstitialAd? = null
    private var exitFragmentContainer: ConstraintLayout? = null

    var fragmentManager: FragmentManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //  initEvent()
        if (config.isAdEnable(ConfigParam.INTER_HOME))
            InterstitialAdsManager.getInstance().loadAdmobInterstitialExit(this@MainActivity)
//        NativeAdsManager.CheckNative(this, window.decorView.rootView)
        val nativeAdId = getString(R.string.nativeId) // Your Native Ad ID
        NativeAdsManager.ReqLoadNativeAd(
            config.isAdEnable(ConfigParam.NATIVE_HOME),
            this,
            window.decorView.rootView,
            nativeAdId
        )
        CollapsibleBanner.loadBanner(
            this,
            binding.bannerContainer,
            config.isAdEnable(ConfigParam.BANNER_HOME)
        )
//        NativeAdsManager.CheckNative(this,window.decorView.rootView)
        fragmentManager = supportFragmentManager

        if (!Setting.isSubActivated){
            binding.preIconeBtn.visibility = View.VISIBLE
        }else{
            binding.preIconeBtn.visibility = View.GONE
        }

        binding.preIconeBtn.setOnClickListener{
            NewScreen.start(this, PurchaseActivity::class.java)
            finish()
        }


        // Initialize the Mobile Ads SDK.
        sharedPreferences = getSharedPreferences("Myrehgffs", Context.MODE_PRIVATE)
        buttonClicks()
        darkMode()
        appL.checkApp(this)
        if (!isNotificationPermission()) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 11)
        }

    }

    private fun isNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }


    private fun darkMode() {
        val savedMode = sharedPreferences.getInt("MODE", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        when (savedMode) {
            AppCompatDelegate.MODE_NIGHT_YES -> {
                binding.settingsIconeBtn.setImageResource(R.drawable.settingsicone_white)
                binding.moonIconeBtn.setImageResource(R.drawable.baseline_dark_mode_24)
                binding.fourGTitleLte.setTextColor(resources.getColor(R.color.white))
//                binding.spped.setTextColor(resources.getColor(R.color.white))
//                binding.sped.setTextColor(resources.getColor(R.color.white))
//                binding.ped.setTextColor(resources.getColor(R.color.white))
//                binding.pd.setTextColor(resources.getColor(R.color.white))
//                binding.spped.visibility = View.GONE
//                binding.sped.visibility = View.GONE
//                binding.ped.visibility = View.GONE
//                binding.pd.visibility = View.GONE
//                binding.speedIconeWh.visibility = View.GONE
//                binding.speedionceW.visibility = View.GONE
//                binding.dataUsW.visibility = View.GONE
//                binding.dataUsX.visibility = View.GONE
//                binding.dataUsageWhIcone.visibility = View.GONE
//                binding.signalWIonce.visibility = View.GONE
//                binding.signalWhIcone.visibility = View.GONE
//                binding.simInfoIonceW.visibility = View.GONE
//                binding.simInfoWhIcone.visibility = View.GONE
//                binding.darkImage.visibility = View.VISIBLE
//                binding.speedDarkTv.visibility = View.VISIBLE
//                binding.dataUsageDarkIm.visibility = View.VISIBLE
//                binding.dataDarkTv.visibility = View.VISIBLE
//                binding.signalDarkIm.visibility = View.VISIBLE
//                binding.signalDarkTv.visibility = View.VISIBLE
//                binding.siminfokTv.visibility = View.VISIBLE
//                binding.siminfoDarkIm.visibility = View.VISIBLE
                binding.clHowToUse.setBackgroundResource(R.drawable.round_shape_dark)
                binding.settinngs.setBackgroundResource(R.drawable.round_shape_dark)
                binding.spped.setBackgroundResource(R.drawable.round_shape_dark)
                binding.sped.setBackgroundResource(R.drawable.round_shape_dark)
                binding.ped.setBackgroundResource(R.drawable.round_shape_dark)
                binding.pd.setBackgroundResource(R.drawable.round_shape_dark)
//                binding.howImg.visibility = View.GONE
//                binding.settinngs.visibility = View.GONE
//                binding.lteWIc.visibility = View.GONE
//                binding.lteWIcone.visibility = View.GONE
//                binding.lteDark.visibility = View.VISIBLE
//                binding.lteDarkkTv.visibility = View.VISIBLE
                binding.howtoUseBtn.setTextColor(resources.getColor(R.color.white))
                binding.ping4g.setTextColor(resources.getColor(R.color.white))
                binding.settinngs.setTextColor(resources.getColor(R.color.white))
                binding.spped.setTextColor(resources.getColor(R.color.white))
                binding.sped.setTextColor(resources.getColor(R.color.white))
                binding.ped.setTextColor(resources.getColor(R.color.white))
                binding.pd.setTextColor(resources.getColor(R.color.white))

            }

            AppCompatDelegate.MODE_NIGHT_NO -> {
                binding.howtoUseBtn.setTextColor(resources.getColor(R.color.lightTextC))
                binding.clHowToUse.setBackgroundResource(R.drawable.roundedcgreen)
                binding.fourGTitleLte.setTextColor(resources.getColor(R.color.black))
                binding.settingsIconeBtn.setImageResource(R.drawable.settingsicone)
                binding.moonIconeBtn.setImageResource(R.drawable.moonicone)
                binding.settinngs.setTextColor(resources.getColor(R.color.lightTextC))
                binding.spped.setTextColor(resources.getColor(R.color.lightTextC))
                binding.sped.setTextColor(resources.getColor(R.color.lightTextC))
                binding.ped.setTextColor(resources.getColor(R.color.lightTextC))
                binding.pd.setTextColor(resources.getColor(R.color.lightTextC))
//                binding.spped.visibility = View.VISIBLE
//                binding.sped.visibility = View.VISIBLE
//                binding.ped.visibility = View.VISIBLE
//                binding.pd.visibility = View.VISIBLE
//                binding.speedIconeWh.visibility = View.VISIBLE
//                binding.speedionceW.visibility = View.VISIBLE
//                binding.dataUsW.visibility = View.VISIBLE
//                binding.dataUsX.visibility = View.VISIBLE
//                binding.dataUsageWhIcone.visibility = View.VISIBLE
//                binding.signalWIonce.visibility = View.VISIBLE
//                binding.signalWhIcone.visibility = View.VISIBLE
//                binding.simInfoIonceW.visibility = View.VISIBLE
//                binding.simInfoWhIcone.visibility = View.VISIBLE
//                binding.darkImage.visibility = View.GONE
//                binding.speedDarkTv.visibility = View.GONE
//                binding.dataUsageDarkIm.visibility = View.GONE
//                binding.dataDarkTv.visibility = View.GONE
//                binding.signalDarkIm.visibility = View.GONE
//                binding.signalDarkTv.visibility = View.GONE
//                binding.siminfokTv.visibility = View.GONE
//                binding.siminfoDarkIm.visibility = View.GONE
                binding.clHowToUse.setBackgroundResource(R.drawable.roundedc)
//                binding.settinngs.visibility = View.VISIBLE
//                binding.lteWIc.visibility = View.VISIBLE
//                binding.lteWIcone.visibility = View.VISIBLE
//                binding.lteDark.visibility = View.GONE
//                binding.lteDarkkTv.visibility = View.GONE
            }

            else -> {
            }
        }
        binding.moonIconeBtn.setOnClickListener {
            val currentMode = AppCompatDelegate.getDefaultNightMode()
            val newMode = if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.MODE_NIGHT_NO
            } else {
                AppCompatDelegate.MODE_NIGHT_YES
            }
            AppCompatDelegate.setDefaultNightMode(newMode)
            sharedPreferences.edit().putInt("MODE", newMode).apply()
            recreate()
        }
    }

    private fun buttonClicks() {
        binding.clHowToUse.setOnClickListener {
            InterstitialAdsManager.getInstance().showAdmobInterstitialInternal(
                config.isAdEnable(ConfigParam.INTER_HOME),
                this@MainActivity
            ) {
                newScreen(HowtoUseActivity::class.java)
            }

//            NativeAdsManager.CheckInitlimit(this, object : onAdShowed {
//                override fun onAdShow() {
//                    // Code to execute when the ad is shown
//                    newScreen(HowtoUseActivity::class.java)
//                }
//            })

//            MyInterstitialController.getInstance().showInterstitial(this) {
//                newScreen(HowtoUseActivity::class.java)
//            }
        }
//        binding.noAdsBtn.visibility = View.GONE
        binding.settingsIconeBtn.setOnClickListener {
            NewScreen.start(this, SettingsActivity::class.java)
        }
        binding.settingLayoutBtn.setOnClickListener {
            InterstitialAdsManager.getInstance().showAdmobInterstitialInternal(
                config.isAdEnable(ConfigParam.INTER_HOME),
                this@MainActivity
            ) {
                newScreen(LteSettingsActivity::class.java)
            }

//            NativeAdsManager.CheckInitlimit(this, object : onAdShowed {
//                override fun onAdShow() {
//                    // Code to execute when the ad is shown
//                    newScreen(LteSettingsActivity::class.java)
//                }
//            })

//            MyInterstitialController.getInstance().showInterstitial(this) {
//                NewScreen.start(this, LteSettingsActivity::class.java)
//            }

        }
        binding.dataUsageBtn.setOnClickListener {
            InterstitialAdsManager.getInstance().showAdmobInterstitialInternal(
                config.isAdEnable(ConfigParam.INTER_HOME),
                this@MainActivity
            ) {
                newScreen(DataUsageActivity::class.java)
            }

//            NativeAdsManager.CheckInitlimit(this, object : onAdShowed {
//                override fun onAdShow() {
//                    // Code to execute when the ad is shown
//                    newScreen(DataUsageActivity::class.java)
//                }
//            })

//            MyInterstitialController.getInstance().showInterstitial(this) {
//                NewScreen.start(this, DataUsageActivity::class.java)
//            }

        }
        binding.speedTestBtn.setOnClickListener {
            InterstitialAdsManager.getInstance().showAdmobInterstitialInternal(
                config.isAdEnable(ConfigParam.INTER_HOME),
                this@MainActivity
            ) {
                newScreen(SpeedTestActivity::class.java)
            }

//            NativeAdsManager.CheckInitlimit(this, object : onAdShowed {
//                override fun onAdShow() {
//                    // Code to execute when the ad is shown
//                    newScreen(SpeedTestActivity::class.java)
//                }
//            })

//            MyInterstitialController.getInstance().showInterstitial(this) {
//                NewScreen.start(this, SpeedTestActivity::class.java)
//            }

        }
        binding.simInfoBtn.setOnClickListener {
            InterstitialAdsManager.getInstance().showAdmobInterstitialInternal(
                config.isAdEnable(ConfigParam.INTER_HOME),
                this@MainActivity
            ) {
                newScreen(SimInfoActivity::class.java)
            }

//            NativeAdsManager.CheckInitlimit(this, object : onAdShowed {
//                override fun onAdShow() {
//                    // Code to execute when the ad is shown
//                    newScreen(SimInfoActivity::class.java)
//                }
//            })

//            MyInterstitialController.getInstance().showInterstitial(this) {
//                NewScreen.start(this, SimInfoActivity::class.java)
//            }

        }
        binding.signalSrengthBtn.setOnClickListener {
            InterstitialAdsManager.getInstance().showAdmobInterstitialInternal(
                config.isAdEnable(ConfigParam.INTER_HOME),
                this@MainActivity
            ) {
                newScreen(SignalStrengthActivity::class.java)
            }

//            NativeAdsManager.CheckInitlimit(this, object : onAdShowed {
//                override fun onAdShow() {
//                    // Code to execute when the ad is shown
//                    newScreen(SignalStrengthActivity::class.java)
//                }
//            })

//            MyInterstitialController.getInstance().showInterstitial(this) {
//                NewScreen.start(this, SignalStrengthActivity::class.java)
//            }

        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
//        MyInterstitialControllerExitBtn.getInstance().showInterstitial(this) {
//                newScreen(HowtoUseActivity::class.java)
//                showBottomSheetDialog()
        InterstitialAdsManager.getInstance().showAdmobInterstitialExit(this@MainActivity) {
            showExitDialog()
        }

//        AdmanagerAdsExit.CheckInitlimit(this, object : onAdShowed {
//            override fun onAdShow() {
//                // Code to execute when the ad is shown
////                newScreen(SignalStrengthActivity::class.java)
//                showExitDialog()
//            }
//        })


//            showExitDialog()
//        }
    }

    private fun showExitDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.exit_dialog_new, null)
        val dialog = AlertDialog.Builder(
            this,
            android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen
        )
            .setView(dialogView)
            .setCancelable(true)
            .create()

        // Load and display native ad
        val nativeAdId = getString(R.string.nativeId) // Your Native Ad ID
        NativeAdsManager.ReqLoadNativeAd(
            config.isAdEnable(ConfigParam.NATIVE_EXIT),
            this,
            dialogView,
            nativeAdId
        )

        dialogView.findViewById<ConstraintLayout>(R.id.speedTestBtn).setOnClickListener {
            NewScreen.start(this, SpeedTestActivity::class.java)
        }

        dialogView.findViewById<ConstraintLayout>(R.id.dataUsageBtn).setOnClickListener {
            NewScreen.start(this, DataUsageActivity::class.java)
        }

        dialogView.findViewById<ConstraintLayout>(R.id.signalSrengthBtn).setOnClickListener {
            NewScreen.start(this, SignalStrengthActivity::class.java)
        }

        dialogView.findViewById<ConstraintLayout>(R.id.simInfoBtn).setOnClickListener {
            NewScreen.start(this, SimInfoActivity::class.java)
        }

        dialogView.findViewById<TextView>(R.id.tv_cancelBtn).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<TextView>(R.id.tv_okBtn).setOnClickListener {
            dialog.dismiss()
            finishAffinity()
        }

        dialog.show()
    }


//    private fun showExitDialog() {
//        val dialogView = LayoutInflater.from(this).inflate(R.layout.exit_dialog_new, null)
//        val dialog = AlertDialog.Builder(
//            this,
//            android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen
//        )
//            .setView(dialogView)
//            .setCancelable(true)
//            .create()
//
//        val nativeAdId = getString(R.string.how_to_use_native) // Your Native Ad ID
//        NativeAdsManager.CheckNative(this, window.decorView.rootView, nativeAdId)
//
////        MobileAds.initialize(this@MainActivity)
////        val adLoader = AdLoader.Builder(this@MainActivity, getString(R.string.exit_native))
////            .forNativeAd { nativeAd ->
////                val styles = NativeTemplateStyle.Builder()
////                    .withMainBackgroundColor(ColorDrawable(Color.parseColor("#EFEDED")))
////                    .build()
////                val template = dialogView.findViewById<TemplateView>(R.id.my_storage_template)
////                template.setStyles(styles)
////                template.setNativeAd(nativeAd)
////                template.visibility = View.VISIBLE
////            }
////            .build()
////        adLoader.loadAd(AdRequest.Builder().build())
//
//        dialogView.findViewById<ConstraintLayout>(R.id.speedTestBtn).setOnClickListener {
//            NewScreen.start(this, SpeedTestActivity::class.java)
//        }
//
//        dialogView.findViewById<ConstraintLayout>(R.id.dataUsageBtn).setOnClickListener {
//            NewScreen.start(this, DataUsageActivity::class.java)
//        }
//
//        dialogView.findViewById<ConstraintLayout>(R.id.signalSrengthBtn).setOnClickListener {
//            NewScreen.start(this, SignalStrengthActivity::class.java)
//        }
//
//        dialogView.findViewById<ConstraintLayout>(R.id.simInfoBtn).setOnClickListener {
//            NewScreen.start(this, SimInfoActivity::class.java)
//        }
//
//        dialogView.findViewById<TextView>(R.id.tv_cancelBtn).setOnClickListener {
//            dialog.dismiss()
//        }
//
//        dialogView.findViewById<TextView>(R.id.tv_okBtn).setOnClickListener {
//            dialog.dismiss()
//            finishAffinity()
//        }
//
//        dialog.show()
//    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.exitFragmentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
