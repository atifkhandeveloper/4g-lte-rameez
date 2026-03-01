package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout

import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.BaseActivity
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.R
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.databinding.ActivitySettingsBinding
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.FirebaseAds.AdmobAds
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adsManager.ADUnitPlacements
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adsManager.BannerAdsManager
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adsManager.Intrestitial_Utis.MyInterstitialController
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adsManager.Intrestitial_Utis.MyInterstitialControllerExitBtn
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adsManager.NativeAdPair
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adsManager.isAppOpenAdShow
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adsManager.loadCollapseBanner
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adsManager.loadNativeAds
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.features.newScreen
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.isAdEnable
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.new_ads_manager.NativeAdsManager
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.new_ads_manager.InterstitialAdsManager
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.new_ads_manager.CollapsibleBanner
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.ConfigParam
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.SharedPrefObj
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.ConstantVariables
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.openUrlInBrowser

class SettingsActivity : BaseActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var binding: ActivitySettingsBinding

    var fabVisible = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nativeAdId = getString(R.string.nativeId) // Your Native Ad ID
        NativeAdsManager.ReqLoadNativeAd(
            config.isAdEnable(ConfigParam.NATIVE_SETTINGS),
            this,
            window.decorView.rootView,
            nativeAdId
        )
//        NativeAdsManager.CheckNative(this, window.decorView.rootView)
        CollapsibleBanner.loadBanner(
            this,
            binding.bannerContainer,
            config.isAdEnable(ConfigParam.BANNER_SETTINGS)
        )

//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        sharedPreferences = getSharedPreferences("Myrehgffs", Context.MODE_PRIVATE)
        darkMode()
        toolBar()
        binding.appLangBtn.setOnClickListener {
            startActivity(
                Intent(
                    this, SelectLangActivity::class.java
                ).putExtra("fromSetting", true)
            )
        }
//        binding.removeAdsBtn.setOnClickListener {
//            newScreen(PurchaseActivity::class.java)
//        }
        binding.howtoUse.setOnClickListener {
            newScreen(HowtoUseActivity::class.java)
        }

        binding.clPrivacyPolicy.setOnClickListener {
//            isAppOpenAdShow(false)
            openUrlInBrowser(
                "https://qappali.blogspot.com/2023/11/privacy-policy-of-4g-lte-only-network.html"
            )
        }
        binding.clShare.setOnClickListener {
//            isAppOpenAdShow(false)
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
                var shareMessage = "\nLet me recommend you this application\n\n"
                shareMessage =
                    """${shareMessage}https://play.google.com/store/apps/details?id=${packageName}""".trimIndent()
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                startActivity(Intent.createChooser(shareIntent, "choose one"))
            } catch (e: Exception) {
                e.toString()
            }
        }
        binding.clRateUs.setOnClickListener { }
//        loadNativeAd()
//        nativeAds()
    }

//    private fun nativeAds() {
//        MobileAds.initialize(this@SettingsActivity)
//        val adLoader = AdLoader.Builder(this@SettingsActivity, getString(R.string.settings_native))
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
//
//            }
//            .build()
//        adLoader.loadAd(AdRequest.Builder().build())
//    }

    private fun toolBar() {
        binding.toolBar.backBtn.setOnClickListener {
            onBackPressed()
        }
        binding.toolBar.removeAdsbtn.visibility = View.GONE
        binding.toolBar.title.text = resources.getString(R.string.letSetting)
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
//        MyInterstitialController.getInstance().showInterstitial(this) {
        InterstitialAdsManager.getInstance().showAdmobInterstitialInternal(
            config.isAdEnable(ConfigParam.INTER_SETTINGS),
            this@SettingsActivity
        ) {
            finish()
        }

//        NativeAdsManager.CheckInitlimit(this, object : onAdShowed {
//            override fun onAdShow() {
//                // Code to execute when the ad is shown
//                finish()
//            }
//        })
//            finish()
//        }
    }

    private fun changeLanguage() {
        val dialogView = layoutInflater.inflate(R.layout.language_alert_dialog, null)

        val dialog = AlertDialog.Builder(this).setView(dialogView).setCancelable(false).create()
        val englishLanLayout = dialogView.findViewById<ConstraintLayout>(R.id.defLangBtn)
        val hindiLanLayout = dialogView.findViewById<ConstraintLayout>(R.id.indianLangbtn)
        val chinseLanLayout = dialogView.findViewById<ConstraintLayout>(R.id.chineLangBtn)
        val itlianLanLayout = dialogView.findViewById<ConstraintLayout>(R.id.itlianBtn)
        val afrecianLanLayout = dialogView.findViewById<ConstraintLayout>(R.id.afrecianBtn)
        val spanishLanLayout = dialogView.findViewById<ConstraintLayout>(R.id.spanishBtn)
        val englishLanRR = dialogView.findViewById<ImageView>(R.id.defaulR)
        val hindiLanRR = dialogView.findViewById<ImageView>(R.id.indianLangR)
        val chinseLanRR = dialogView.findViewById<ImageView>(R.id.chineLanR)
        val itlianLanRR = dialogView.findViewById<ImageView>(R.id.itlianLaR)
        val spanishLanRR = dialogView.findViewById<ImageView>(R.id.spanishR)
        val afrecianLanRR = dialogView.findViewById<ImageView>(R.id.afrecianLanR)
        val dismis = dialogView.findViewById<ImageView>(R.id.doneBtn)
        dismis.setOnClickListener { dialog.dismiss() }
        dialog.show()
        val key = SharedPrefObj.getString(this, ConstantVariables.LANG_KEY)
        if (key == ConstantVariables.CHINISE) {
            englishLanLayout.setBackgroundResource(R.drawable.strokeshape)
            hindiLanLayout.setBackgroundResource(R.drawable.strokeshape)
            chinseLanLayout.setBackgroundResource(R.drawable.strokeshapegreen)
            spanishLanLayout.setBackgroundResource(R.drawable.strokeshape)
            afrecianLanLayout.setBackgroundResource(R.drawable.strokeshape)
            itlianLanLayout.setBackgroundResource(R.drawable.strokeshape)
            englishLanRR.setImageResource(R.drawable.nullc)
            hindiLanRR.setImageResource(R.drawable.nullc)
            chinseLanRR.setImageResource(R.drawable.radio)
            itlianLanRR.setImageResource(R.drawable.nullc)
            spanishLanRR.setImageResource(R.drawable.nullc)
            afrecianLanRR.setImageResource(R.drawable.nullc)
        } else if (key == ConstantVariables.HINDI) {
            englishLanLayout.setBackgroundResource(R.drawable.strokeshape)
            hindiLanLayout.setBackgroundResource(R.drawable.strokeshapegreen)
            chinseLanLayout.setBackgroundResource(R.drawable.strokeshape)
            spanishLanLayout.setBackgroundResource(R.drawable.strokeshape)
            afrecianLanLayout.setBackgroundResource(R.drawable.strokeshape)
            itlianLanLayout.setBackgroundResource(R.drawable.strokeshape)
            englishLanRR.setImageResource(R.drawable.nullc)
            hindiLanRR.setImageResource(R.drawable.radio)
            chinseLanRR.setImageResource(R.drawable.nullc)
            itlianLanRR.setImageResource(R.drawable.nullc)
            spanishLanRR.setImageResource(R.drawable.nullc)
            afrecianLanRR.setImageResource(R.drawable.nullc)
        } else if (key == ConstantVariables.SPANISH) {
            englishLanLayout.setBackgroundResource(R.drawable.strokeshape)
            hindiLanLayout.setBackgroundResource(R.drawable.strokeshape)
            chinseLanLayout.setBackgroundResource(R.drawable.strokeshape)
            spanishLanLayout.setBackgroundResource(R.drawable.strokeshapegreen)
            afrecianLanLayout.setBackgroundResource(R.drawable.strokeshape)
            itlianLanLayout.setBackgroundResource(R.drawable.strokeshape)
            englishLanRR.setImageResource(R.drawable.nullc)
            hindiLanRR.setImageResource(R.drawable.nullc)
            chinseLanRR.setImageResource(R.drawable.nullc)
            itlianLanRR.setImageResource(R.drawable.nullc)
            spanishLanRR.setImageResource(R.drawable.radio)
            afrecianLanRR.setImageResource(R.drawable.nullc)
        } else if (key == ConstantVariables.AFRECIAN) {
            englishLanLayout.setBackgroundResource(R.drawable.strokeshape)
            hindiLanLayout.setBackgroundResource(R.drawable.strokeshape)
            chinseLanLayout.setBackgroundResource(R.drawable.strokeshape)
            spanishLanLayout.setBackgroundResource(R.drawable.strokeshape)
            afrecianLanLayout.setBackgroundResource(R.drawable.strokeshapegreen)
            itlianLanLayout.setBackgroundResource(R.drawable.strokeshape)
            englishLanRR.setImageResource(R.drawable.nullc)
            hindiLanRR.setImageResource(R.drawable.nullc)
            chinseLanRR.setImageResource(R.drawable.nullc)
            itlianLanRR.setImageResource(R.drawable.nullc)
            spanishLanRR.setImageResource(R.drawable.nullc)
            afrecianLanRR.setImageResource(R.drawable.radio)
        } else if (key == ConstantVariables.ITLIAN) {
            englishLanLayout.setBackgroundResource(R.drawable.strokeshape)
            hindiLanLayout.setBackgroundResource(R.drawable.strokeshape)
            chinseLanLayout.setBackgroundResource(R.drawable.strokeshape)
            spanishLanLayout.setBackgroundResource(R.drawable.strokeshape)
            afrecianLanLayout.setBackgroundResource(R.drawable.strokeshape)
            itlianLanLayout.setBackgroundResource(R.drawable.strokeshapegreen)
            englishLanRR.setImageResource(R.drawable.nullc)
            hindiLanRR.setImageResource(R.drawable.nullc)
            chinseLanRR.setImageResource(R.drawable.nullc)
            itlianLanRR.setImageResource(R.drawable.radio)
            spanishLanRR.setImageResource(R.drawable.nullc)
            afrecianLanRR.setImageResource(R.drawable.nullc)
        } else {
            englishLanLayout.setBackgroundResource(R.drawable.strokeshapegreen)
            hindiLanLayout.setBackgroundResource(R.drawable.strokeshape)
            chinseLanLayout.setBackgroundResource(R.drawable.strokeshape)
            spanishLanLayout.setBackgroundResource(R.drawable.strokeshape)
            afrecianLanLayout.setBackgroundResource(R.drawable.strokeshape)
            itlianLanLayout.setBackgroundResource(R.drawable.strokeshape)
            englishLanRR.setImageResource(R.drawable.radio)
            hindiLanRR.setImageResource(R.drawable.nullc)
            chinseLanRR.setImageResource(R.drawable.nullc)
            itlianLanRR.setImageResource(R.drawable.nullc)
            spanishLanRR.setImageResource(R.drawable.nullc)
            afrecianLanRR.setImageResource(R.drawable.nullc)
        }
        englishLanLayout.setOnClickListener {
            englishLanLayout.setBackgroundResource(R.drawable.strokeshapegreen)
            hindiLanLayout.setBackgroundResource(R.drawable.strokeshape)
            chinseLanLayout.setBackgroundResource(R.drawable.strokeshape)
            spanishLanLayout.setBackgroundResource(R.drawable.strokeshape)
            afrecianLanLayout.setBackgroundResource(R.drawable.strokeshape)
            itlianLanLayout.setBackgroundResource(R.drawable.strokeshape)
            SharedPrefObj.saveString(this, ConstantVariables.LANG_KEY, ConstantVariables.ENGLISH)
            englishLanRR.setImageResource(R.drawable.radio)
            hindiLanRR.setImageResource(R.drawable.nullc)
            chinseLanRR.setImageResource(R.drawable.nullc)
            setLanguage("en")
            dialog.dismiss()
        }

        hindiLanLayout.setOnClickListener {
            englishLanLayout.setBackgroundResource(R.drawable.strokeshape)
            hindiLanLayout.setBackgroundResource(R.drawable.strokeshapegreen)
            chinseLanLayout.setBackgroundResource(R.drawable.strokeshape)
            spanishLanLayout.setBackgroundResource(R.drawable.strokeshape)
            afrecianLanLayout.setBackgroundResource(R.drawable.strokeshape)
            itlianLanLayout.setBackgroundResource(R.drawable.strokeshape)
            SharedPrefObj.saveString(this, ConstantVariables.LANG_KEY, ConstantVariables.HINDI)
            hindiLanRR.setImageResource(R.drawable.radio)
            englishLanRR.setImageResource(R.drawable.nullc)
            chinseLanRR.setImageResource(R.drawable.nullc)
            setLanguage("hi")
            dialog.dismiss()
        }

        chinseLanLayout.setOnClickListener {
            englishLanLayout.setBackgroundResource(R.drawable.strokeshape)
            hindiLanLayout.setBackgroundResource(R.drawable.strokeshape)
            chinseLanLayout.setBackgroundResource(R.drawable.strokeshapegreen)
            spanishLanLayout.setBackgroundResource(R.drawable.strokeshape)
            afrecianLanLayout.setBackgroundResource(R.drawable.strokeshape)
            itlianLanLayout.setBackgroundResource(R.drawable.strokeshape)
            SharedPrefObj.saveString(this, ConstantVariables.LANG_KEY, ConstantVariables.CHINISE)
            chinseLanRR.setImageResource(R.drawable.radio)
            englishLanRR.setImageResource(R.drawable.nullc)
            hindiLanRR.setImageResource(R.drawable.nullc)
            setLanguage("cop") // The language code for Chinese should be 'zh'
            dialog.dismiss()
        }
        spanishLanLayout.setOnClickListener {
            englishLanLayout.setBackgroundResource(R.drawable.strokeshape)
            hindiLanLayout.setBackgroundResource(R.drawable.strokeshape)
            chinseLanLayout.setBackgroundResource(R.drawable.strokeshape)
            spanishLanLayout.setBackgroundResource(R.drawable.strokeshapegreen)
            afrecianLanLayout.setBackgroundResource(R.drawable.strokeshape)
            itlianLanLayout.setBackgroundResource(R.drawable.strokeshape)
            SharedPrefObj.saveString(this, ConstantVariables.LANG_KEY, ConstantVariables.SPANISH)
            englishLanRR.setImageResource(R.drawable.nullc)
            hindiLanRR.setImageResource(R.drawable.nullc)
            chinseLanRR.setImageResource(R.drawable.nullc)
            itlianLanRR.setImageResource(R.drawable.nullc)
            spanishLanRR.setImageResource(R.drawable.nullc)
            afrecianLanRR.setImageResource(R.drawable.radio)
            setLanguage("es") // The language code for Chinese should be 'zh'
            dialog.dismiss()
        }
        itlianLanLayout.setOnClickListener {
            englishLanLayout.setBackgroundResource(R.drawable.strokeshape)
            hindiLanLayout.setBackgroundResource(R.drawable.strokeshape)
            chinseLanLayout.setBackgroundResource(R.drawable.strokeshape)
            spanishLanLayout.setBackgroundResource(R.drawable.strokeshape)
            afrecianLanLayout.setBackgroundResource(R.drawable.strokeshape)
            itlianLanLayout.setBackgroundResource(R.drawable.strokeshapegreen)
            SharedPrefObj.saveString(this, ConstantVariables.LANG_KEY, ConstantVariables.ITLIAN)
            englishLanRR.setImageResource(R.drawable.nullc)
            hindiLanRR.setImageResource(R.drawable.nullc)
            chinseLanRR.setImageResource(R.drawable.nullc)
            itlianLanRR.setImageResource(R.drawable.radio)
            spanishLanRR.setImageResource(R.drawable.nullc)
            afrecianLanRR.setImageResource(R.drawable.nullc)
            setLanguage("it") // The language code for Chinese should be 'zh'
            dialog.dismiss()
        }
        afrecianLanLayout.setOnClickListener {
            englishLanLayout.setBackgroundResource(R.drawable.strokeshape)
            hindiLanLayout.setBackgroundResource(R.drawable.strokeshape)
            chinseLanLayout.setBackgroundResource(R.drawable.strokeshape)
            spanishLanLayout.setBackgroundResource(R.drawable.strokeshape)
            afrecianLanLayout.setBackgroundResource(R.drawable.strokeshapegreen)
            itlianLanLayout.setBackgroundResource(R.drawable.strokeshape)
            SharedPrefObj.saveString(this, ConstantVariables.LANG_KEY, ConstantVariables.AFRECIAN)
            englishLanRR.setImageResource(R.drawable.nullc)
            hindiLanRR.setImageResource(R.drawable.nullc)
            chinseLanRR.setImageResource(R.drawable.nullc)
            itlianLanRR.setImageResource(R.drawable.nullc)
            spanishLanRR.setImageResource(R.drawable.nullc)
            afrecianLanRR.setImageResource(R.drawable.radio)
            setLanguage("af") // The language code for Chinese should be 'zh'
            dialog.dismiss()
        }

    }

    private fun darkMode() {
        val savedMode = sharedPreferences.getInt("MODE", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        when (savedMode) {
            AppCompatDelegate.MODE_NIGHT_YES -> {
                binding.toolBar.title.setTextColor(resources.getColor(R.color.white))
                binding.dakrMTv.text = resources.getString(R.string.darkMode)
            }

            AppCompatDelegate.MODE_NIGHT_NO -> {
                binding.toolBar.title.setTextColor(resources.getColor(R.color.black))
                binding.dakrMTv.text = resources.getString(R.string.defaultMode)
            }

            else -> {
                binding.dakrMTv.text = resources.getString(R.string.defaultMode)
            }
        }
        binding.darkModeBtnik.setOnClickListener {
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


//    private fun loadNativeAd() {
////        try {
////            AppDelegate.nativeAd?.apply {
////                NativeAdPair(this).populate(
////                    this@SettingsActivity,
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