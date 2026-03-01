package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat

import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.BaseActivity
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.R
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.databinding.ActivitySelectLangBinding
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.FirebaseAds.AdmobAds
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adsManager.ADUnitPlacements
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adsManager.BannerAdsManager
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adsManager.NativeAdPair
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adsManager.loadNativeAds
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.features.newScreen
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.isAdEnable
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.new_ads_manager.InterstitialAdsManager
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.new_ads_manager.NativeAdsManager
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.ConfigParam
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.SharedPrefObj
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.ConstantVariables

class SelectLangActivity : BaseActivity() {
    var seletedLang = "en"
    lateinit var binding: ActivitySelectLangBinding

    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("Myrehgffs", Context.MODE_PRIVATE)
    }

    var fromSetting = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectLangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nativeAdId = getString(R.string.nativeId) // Your Native Ad ID
        NativeAdsManager.ReqLoadNativeAd(   config.isAdEnable(ConfigParam.NATIVE_LANGUAGE),this, window.decorView.rootView, nativeAdId)
//        NativeAdsManager.CheckNative(this, window.decorView.rootView)

        selectLanguage()
        toolbar()
        checkLang()
        selectLang(seletedLang)
//        loadNativeAd()
//        nativeAds()
        darkMode()
        fromSetting = intent.getBooleanExtra("fromSetting", false)
    }

    private fun darkMode() {
        val savedMode = sharedPreferences.getInt("MODE", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        when (savedMode) {
            AppCompatDelegate.MODE_NIGHT_YES -> {
                // Your existing code for dark mode
//                binding.title.setTextColor(resources.getColor(R.color.white))
//                binding.tv.setTextColor(resources.getColor(R.color.white))
            }

            AppCompatDelegate.MODE_NIGHT_NO -> {
                // Your existing code for light mode
//                binding.title.setTextColor(resources.getColor(R.color.lightTextC))
//                binding.tv.setTextColor(resources.getColor(R.color.lightTextC))

            }
        }
    }

//    private fun nativeAds() {
//        MobileAds.initialize(this@SelectLangActivity)
//        val adLoader = AdLoader.Builder(this@SelectLangActivity, getString(R.string.on_boarding_native))
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

    private fun checkLang() {
        val key = SharedPrefObj.getString(this, ConstantVariables.LANG_KEY)
        if (key == ConstantVariables.HINDI) {
//            binding.defLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.chineLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.spanishBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.afrecianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.itlianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.indianLangbtn.setBackgroundResource(R.drawable.strokeshapegreen)
//            binding.clKorean.setBackgroundResource(R.drawable.strokeshape)
//            binding.clJapanese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clMalay.setBackgroundResource(R.drawable.strokeshape)
//            binding.clVietnamese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clIndonesianBtn.setBackgroundResource(R.drawable.strokeshape)

            binding.defaulR.setImageResource(R.drawable.nullc)
            binding.indianLangR.setImageResource(R.drawable.radio)
            binding.chineLanR.setImageResource(R.drawable.nullc)
            binding.spanishR.setImageResource(R.drawable.nullc)
            binding.itlianLaR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.ivKoreanRadio.setImageResource(R.drawable.nullc)
            binding.ivJapaneseRadio.setImageResource(R.drawable.nullc)
            binding.ivMalayRadio.setImageResource(R.drawable.nullc)
            binding.ivVietnameseRadio.setImageResource(R.drawable.nullc)
            binding.ivIndonesian.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.ivKoreanRadio.setImageResource(R.drawable.nullc)
            binding.ivJapaneseRadio.setImageResource(R.drawable.nullc)
            binding.ivMalayRadio.setImageResource(R.drawable.nullc)
            binding.ivVietnameseRadio.setImageResource(R.drawable.nullc)
            binding.ivIndonesian.setImageResource(R.drawable.nullc)
        } else if (key == ConstantVariables.CHINISE) {
//            binding.defLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.indianLangbtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.chineLangBtn.setBackgroundResource(R.drawable.strokeshapegreen)
//            binding.spanishBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.afrecianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.itlianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.clKorean.setBackgroundResource(R.drawable.strokeshape)
//            binding.clJapanese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clMalay.setBackgroundResource(R.drawable.strokeshape)
//            binding.clVietnamese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clIndonesianBtn.setBackgroundResource(R.drawable.strokeshape)

            binding.defaulR.setImageResource(R.drawable.nullc)
            binding.indianLangR.setImageResource(R.drawable.nullc)
            binding.chineLanR.setImageResource(R.drawable.radio)
            binding.spanishR.setImageResource(R.drawable.nullc)
            binding.itlianLaR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.ivKoreanRadio.setImageResource(R.drawable.nullc)
            binding.ivJapaneseRadio.setImageResource(R.drawable.nullc)
            binding.ivMalayRadio.setImageResource(R.drawable.nullc)
            binding.ivVietnameseRadio.setImageResource(R.drawable.nullc)
            binding.ivIndonesian.setImageResource(R.drawable.nullc)
        } else if (key == ConstantVariables.SPANISH) {
//            binding.defLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.indianLangbtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.chineLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.spanishBtn.setBackgroundResource(R.drawable.strokeshapegreen)
//            binding.afrecianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.itlianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.clKorean.setBackgroundResource(R.drawable.strokeshape)
//            binding.clJapanese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clMalay.setBackgroundResource(R.drawable.strokeshape)
//            binding.clVietnamese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clIndonesianBtn.setBackgroundResource(R.drawable.strokeshape)

            binding.defaulR.setImageResource(R.drawable.nullc)
            binding.indianLangR.setImageResource(R.drawable.nullc)
            binding.chineLanR.setImageResource(R.drawable.nullc)
            binding.spanishR.setImageResource(R.drawable.radio)
            binding.itlianLaR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.ivKoreanRadio.setImageResource(R.drawable.nullc)
            binding.ivJapaneseRadio.setImageResource(R.drawable.nullc)
            binding.ivMalayRadio.setImageResource(R.drawable.nullc)
            binding.ivVietnameseRadio.setImageResource(R.drawable.nullc)
            binding.ivIndonesian.setImageResource(R.drawable.nullc)
        } else if (key == ConstantVariables.AFRECIAN) {
//            binding.defLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.indianLangbtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.chineLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.spanishBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.afrecianBtn.setBackgroundResource(R.drawable.strokeshapegreen)
//            binding.itlianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.clKorean.setBackgroundResource(R.drawable.strokeshape)
//            binding.clJapanese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clMalay.setBackgroundResource(R.drawable.strokeshape)
//            binding.clVietnamese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clIndonesianBtn.setBackgroundResource(R.drawable.strokeshape)
            binding.defaulR.setImageResource(R.drawable.nullc)
            binding.indianLangR.setImageResource(R.drawable.nullc)
            binding.chineLanR.setImageResource(R.drawable.nullc)
            binding.spanishR.setImageResource(R.drawable.nullc)
            binding.itlianLaR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.radio)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.ivKoreanRadio.setImageResource(R.drawable.nullc)
            binding.ivJapaneseRadio.setImageResource(R.drawable.nullc)
            binding.ivMalayRadio.setImageResource(R.drawable.nullc)
            binding.ivVietnameseRadio.setImageResource(R.drawable.nullc)
            binding.ivIndonesian.setImageResource(R.drawable.nullc)
        } else if (key == ConstantVariables.ITLIAN) {
//            binding.defLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.indianLangbtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.chineLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.spanishBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.afrecianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.itlianBtn.setBackgroundResource(R.drawable.strokeshapegreen)
//            binding.clKorean.setBackgroundResource(R.drawable.strokeshape)
//            binding.clJapanese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clMalay.setBackgroundResource(R.drawable.strokeshape)
//            binding.clVietnamese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clIndonesianBtn.setBackgroundResource(R.drawable.strokeshape)
            binding.defaulR.setImageResource(R.drawable.nullc)
            binding.indianLangR.setImageResource(R.drawable.nullc)
            binding.chineLanR.setImageResource(R.drawable.nullc)
            binding.spanishR.setImageResource(R.drawable.nullc)
            binding.itlianLaR.setImageResource(R.drawable.radio)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.ivKoreanRadio.setImageResource(R.drawable.nullc)
            binding.ivJapaneseRadio.setImageResource(R.drawable.nullc)
            binding.ivMalayRadio.setImageResource(R.drawable.nullc)
            binding.ivVietnameseRadio.setImageResource(R.drawable.nullc)
            binding.ivIndonesian.setImageResource(R.drawable.nullc)
        } else if (key == ConstantVariables.KOREAN) {
//            binding.defLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.indianLangbtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.chineLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.spanishBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.afrecianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.itlianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.clKorean.setBackgroundResource(R.drawable.strokeshapegreen)
//            binding.clJapanese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clMalay.setBackgroundResource(R.drawable.strokeshape)
//            binding.clVietnamese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clIndonesianBtn.setBackgroundResource(R.drawable.strokeshape)
            binding.defaulR.setImageResource(R.drawable.nullc)
            binding.indianLangR.setImageResource(R.drawable.nullc)
            binding.chineLanR.setImageResource(R.drawable.nullc)
            binding.spanishR.setImageResource(R.drawable.nullc)
            binding.itlianLaR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.ivKoreanRadio.setImageResource(R.drawable.radio)
            binding.ivJapaneseRadio.setImageResource(R.drawable.nullc)
            binding.ivMalayRadio.setImageResource(R.drawable.nullc)
            binding.ivVietnameseRadio.setImageResource(R.drawable.nullc)
            binding.ivIndonesian.setImageResource(R.drawable.nullc)
        } else if (key == ConstantVariables.VIETNAMESE) {
//            binding.defLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.indianLangbtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.chineLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.spanishBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.afrecianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.itlianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.clKorean.setBackgroundResource(R.drawable.strokeshape)
//            binding.clJapanese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clMalay.setBackgroundResource(R.drawable.strokeshape)
//            binding.clVietnamese.setBackgroundResource(R.drawable.strokeshapegreen)
//            binding.clIndonesianBtn.setBackgroundResource(R.drawable.strokeshape)
            binding.defaulR.setImageResource(R.drawable.nullc)
            binding.indianLangR.setImageResource(R.drawable.nullc)
            binding.chineLanR.setImageResource(R.drawable.nullc)
            binding.spanishR.setImageResource(R.drawable.nullc)
            binding.itlianLaR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.ivKoreanRadio.setImageResource(R.drawable.nullc)
            binding.ivJapaneseRadio.setImageResource(R.drawable.nullc)
            binding.ivMalayRadio.setImageResource(R.drawable.nullc)
            binding.ivVietnameseRadio.setImageResource(R.drawable.radio)
            binding.ivIndonesian.setImageResource(R.drawable.nullc)
        } else if (key == ConstantVariables.JAPANESE) {
//            binding.defLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.indianLangbtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.chineLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.spanishBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.afrecianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.itlianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.clKorean.setBackgroundResource(R.drawable.strokeshape)
//            binding.clJapanese.setBackgroundResource(R.drawable.strokeshapegreen)
//            binding.clMalay.setBackgroundResource(R.drawable.strokeshape)
//            binding.clVietnamese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clIndonesianBtn.setBackgroundResource(R.drawable.strokeshape)
            binding.defaulR.setImageResource(R.drawable.nullc)
            binding.indianLangR.setImageResource(R.drawable.nullc)
            binding.chineLanR.setImageResource(R.drawable.nullc)
            binding.spanishR.setImageResource(R.drawable.nullc)
            binding.itlianLaR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.ivKoreanRadio.setImageResource(R.drawable.nullc)
            binding.ivJapaneseRadio.setImageResource(R.drawable.radio)
            binding.ivMalayRadio.setImageResource(R.drawable.nullc)
            binding.ivVietnameseRadio.setImageResource(R.drawable.nullc)
            binding.ivIndonesian.setImageResource(R.drawable.nullc)
        } else if (key == ConstantVariables.MALAY) {
//            binding.defLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.indianLangbtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.chineLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.spanishBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.afrecianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.itlianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.clKorean.setBackgroundResource(R.drawable.strokeshape)
//            binding.clJapanese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clMalay.setBackgroundResource(R.drawable.strokeshapegreen)
//            binding.clVietnamese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clIndonesianBtn.setBackgroundResource(R.drawable.strokeshape)
            binding.defaulR.setImageResource(R.drawable.nullc)
            binding.indianLangR.setImageResource(R.drawable.nullc)
            binding.chineLanR.setImageResource(R.drawable.nullc)
            binding.spanishR.setImageResource(R.drawable.nullc)
            binding.itlianLaR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.ivKoreanRadio.setImageResource(R.drawable.nullc)
            binding.ivJapaneseRadio.setImageResource(R.drawable.nullc)
            binding.ivMalayRadio.setImageResource(R.drawable.radio)
            binding.ivVietnameseRadio.setImageResource(R.drawable.nullc)
            binding.ivIndonesian.setImageResource(R.drawable.nullc)
        } else if (key == ConstantVariables.INDONESIAN) {
//            binding.defLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.indianLangbtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.chineLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.spanishBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.afrecianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.itlianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.clKorean.setBackgroundResource(R.drawable.strokeshape)
//            binding.clJapanese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clMalay.setBackgroundResource(R.drawable.strokeshape)
//            binding.clVietnamese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clIndonesianBtn.setBackgroundResource(R.drawable.strokeshapegreen)
            binding.defaulR.setImageResource(R.drawable.nullc)
            binding.indianLangR.setImageResource(R.drawable.nullc)
            binding.chineLanR.setImageResource(R.drawable.nullc)
            binding.spanishR.setImageResource(R.drawable.nullc)
            binding.itlianLaR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.ivKoreanRadio.setImageResource(R.drawable.nullc)
            binding.ivJapaneseRadio.setImageResource(R.drawable.nullc)
            binding.ivMalayRadio.setImageResource(R.drawable.nullc)
            binding.ivVietnameseRadio.setImageResource(R.drawable.nullc)
            binding.ivIndonesian.setImageResource(R.drawable.radio)
        } else {
//            binding.defLangBtn.setBackgroundResource(R.drawable.strokeshapegreen)
//            binding.indianLangbtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.chineLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.spanishBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.afrecianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.itlianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.clKorean.setBackgroundResource(R.drawable.strokeshape)
//            binding.clJapanese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clMalay.setBackgroundResource(R.drawable.strokeshape)
//            binding.clVietnamese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clIndonesianBtn.setBackgroundResource(R.drawable.strokeshape)
            binding.defaulR.setImageResource(R.drawable.radio)
            binding.indianLangR.setImageResource(R.drawable.nullc)
            binding.chineLanR.setImageResource(R.drawable.nullc)
            binding.spanishR.setImageResource(R.drawable.nullc)
            binding.itlianLaR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.ivKoreanRadio.setImageResource(R.drawable.nullc)
            binding.ivJapaneseRadio.setImageResource(R.drawable.nullc)
            binding.ivMalayRadio.setImageResource(R.drawable.nullc)
            binding.ivVietnameseRadio.setImageResource(R.drawable.nullc)
            binding.ivIndonesian.setImageResource(R.drawable.nullc)
        }
    }

    private fun selectLanguage() {
        binding.defLangBtn.setOnClickListener {
//            binding.defLangBtn.setBackgroundResource(R.drawable.strokeshapegreen)
//            binding.indianLangbtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.chineLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.spanishBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.afrecianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.itlianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.clKorean.setBackgroundResource(R.drawable.strokeshape)
//            binding.clJapanese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clMalay.setBackgroundResource(R.drawable.strokeshape)
//            binding.clVietnamese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clIndonesianBtn.setBackgroundResource(R.drawable.strokeshape)
            binding.defaulR.setImageResource(R.drawable.radio)
            binding.indianLangR.setImageResource(R.drawable.nullc)
            binding.chineLanR.setImageResource(R.drawable.nullc)
            binding.spanishR.setImageResource(R.drawable.nullc)
            binding.itlianLaR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.ivKoreanRadio.setImageResource(R.drawable.nullc)
            binding.ivJapaneseRadio.setImageResource(R.drawable.nullc)
            binding.ivMalayRadio.setImageResource(R.drawable.nullc)
            binding.ivVietnameseRadio.setImageResource(R.drawable.nullc)
            binding.ivIndonesian.setImageResource(R.drawable.nullc)
            selectLang(ConstantVariables.ENGLISH)
        }

        binding.indianLangbtn.setOnClickListener {
//            binding.defLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.chineLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.spanishBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.afrecianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.itlianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.indianLangbtn.setBackgroundResource(R.drawable.strokeshapegreen)
//            binding.clKorean.setBackgroundResource(R.drawable.strokeshape)
//            binding.clJapanese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clMalay.setBackgroundResource(R.drawable.strokeshape)
//            binding.clVietnamese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clIndonesianBtn.setBackgroundResource(R.drawable.strokeshape)

            binding.defaulR.setImageResource(R.drawable.nullc)
            binding.indianLangR.setImageResource(R.drawable.radio)
            binding.chineLanR.setImageResource(R.drawable.nullc)
            binding.spanishR.setImageResource(R.drawable.nullc)
            binding.itlianLaR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.ivKoreanRadio.setImageResource(R.drawable.nullc)
            binding.ivJapaneseRadio.setImageResource(R.drawable.nullc)
            binding.ivMalayRadio.setImageResource(R.drawable.nullc)
            binding.ivVietnameseRadio.setImageResource(R.drawable.nullc)
            binding.ivIndonesian.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.ivKoreanRadio.setImageResource(R.drawable.nullc)
            binding.ivJapaneseRadio.setImageResource(R.drawable.nullc)
            binding.ivMalayRadio.setImageResource(R.drawable.nullc)
            binding.ivVietnameseRadio.setImageResource(R.drawable.nullc)
            binding.ivIndonesian.setImageResource(R.drawable.nullc)
            selectLang(ConstantVariables.HINDI)
        }

        binding.chineLangBtn.setOnClickListener {
//            binding.defLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.indianLangbtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.chineLangBtn.setBackgroundResource(R.drawable.strokeshapegreen)
//            binding.spanishBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.afrecianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.itlianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.clKorean.setBackgroundResource(R.drawable.strokeshape)
//            binding.clJapanese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clMalay.setBackgroundResource(R.drawable.strokeshape)
//            binding.clVietnamese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clIndonesianBtn.setBackgroundResource(R.drawable.strokeshape)

            binding.defaulR.setImageResource(R.drawable.nullc)
            binding.indianLangR.setImageResource(R.drawable.nullc)
            binding.chineLanR.setImageResource(R.drawable.radio)
            binding.spanishR.setImageResource(R.drawable.nullc)
            binding.itlianLaR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.ivKoreanRadio.setImageResource(R.drawable.nullc)
            binding.ivJapaneseRadio.setImageResource(R.drawable.nullc)
            binding.ivMalayRadio.setImageResource(R.drawable.nullc)
            binding.ivVietnameseRadio.setImageResource(R.drawable.nullc)
            binding.ivIndonesian.setImageResource(R.drawable.nullc)
            selectLang(ConstantVariables.CHINISE)
        }

        binding.spanishBtn.setOnClickListener {
//            binding.defLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.indianLangbtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.chineLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.spanishBtn.setBackgroundResource(R.drawable.strokeshapegreen)
//            binding.afrecianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.itlianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.clKorean.setBackgroundResource(R.drawable.strokeshape)
//            binding.clJapanese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clMalay.setBackgroundResource(R.drawable.strokeshape)
//            binding.clVietnamese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clIndonesianBtn.setBackgroundResource(R.drawable.strokeshape)

            binding.defaulR.setImageResource(R.drawable.nullc)
            binding.indianLangR.setImageResource(R.drawable.nullc)
            binding.chineLanR.setImageResource(R.drawable.nullc)
            binding.spanishR.setImageResource(R.drawable.radio)
            binding.itlianLaR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.ivKoreanRadio.setImageResource(R.drawable.nullc)
            binding.ivJapaneseRadio.setImageResource(R.drawable.nullc)
            binding.ivMalayRadio.setImageResource(R.drawable.nullc)
            binding.ivVietnameseRadio.setImageResource(R.drawable.nullc)
            binding.ivIndonesian.setImageResource(R.drawable.nullc)
            selectLang(ConstantVariables.SPANISH)
        }

        binding.itlianBtn.setOnClickListener {
//            binding.defLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.indianLangbtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.chineLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.spanishBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.afrecianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.itlianBtn.setBackgroundResource(R.drawable.strokeshapegreen)
//            binding.clKorean.setBackgroundResource(R.drawable.strokeshape)
//            binding.clJapanese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clMalay.setBackgroundResource(R.drawable.strokeshape)
//            binding.clVietnamese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clIndonesianBtn.setBackgroundResource(R.drawable.strokeshape)
            binding.defaulR.setImageResource(R.drawable.nullc)
            binding.indianLangR.setImageResource(R.drawable.nullc)
            binding.chineLanR.setImageResource(R.drawable.nullc)
            binding.spanishR.setImageResource(R.drawable.nullc)
            binding.itlianLaR.setImageResource(R.drawable.radio)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.ivKoreanRadio.setImageResource(R.drawable.nullc)
            binding.ivJapaneseRadio.setImageResource(R.drawable.nullc)
            binding.ivMalayRadio.setImageResource(R.drawable.nullc)
            binding.ivVietnameseRadio.setImageResource(R.drawable.nullc)
            binding.ivIndonesian.setImageResource(R.drawable.nullc)
            selectLang(ConstantVariables.ITLIAN)
        }

        binding.afrecianBtn.setOnClickListener {
//            binding.defLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.indianLangbtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.chineLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.spanishBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.afrecianBtn.setBackgroundResource(R.drawable.strokeshapegreen)
//            binding.itlianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.clKorean.setBackgroundResource(R.drawable.strokeshape)
//            binding.clJapanese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clMalay.setBackgroundResource(R.drawable.strokeshape)
//            binding.clVietnamese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clIndonesianBtn.setBackgroundResource(R.drawable.strokeshape)
            binding.defaulR.setImageResource(R.drawable.nullc)
            binding.indianLangR.setImageResource(R.drawable.nullc)
            binding.chineLanR.setImageResource(R.drawable.nullc)
            binding.spanishR.setImageResource(R.drawable.nullc)
            binding.itlianLaR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.radio)
            binding.ivKoreanRadio.setImageResource(R.drawable.nullc)
            binding.ivJapaneseRadio.setImageResource(R.drawable.nullc)
            binding.ivMalayRadio.setImageResource(R.drawable.nullc)
            binding.ivVietnameseRadio.setImageResource(R.drawable.nullc)
            binding.ivIndonesian.setImageResource(R.drawable.nullc)
            selectLang(ConstantVariables.AFRECIAN)
        }

        binding.clKorean.setOnClickListener {
//            binding.defLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.indianLangbtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.chineLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.spanishBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.afrecianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.itlianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.clKorean.setBackgroundResource(R.drawable.strokeshapegreen)
//            binding.clJapanese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clMalay.setBackgroundResource(R.drawable.strokeshape)
//            binding.clVietnamese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clIndonesianBtn.setBackgroundResource(R.drawable.strokeshape)
            binding.defaulR.setImageResource(R.drawable.nullc)
            binding.indianLangR.setImageResource(R.drawable.nullc)
            binding.chineLanR.setImageResource(R.drawable.nullc)
            binding.spanishR.setImageResource(R.drawable.nullc)
            binding.itlianLaR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.ivKoreanRadio.setImageResource(R.drawable.radio)
            binding.ivJapaneseRadio.setImageResource(R.drawable.nullc)
            binding.ivMalayRadio.setImageResource(R.drawable.nullc)
            binding.ivVietnameseRadio.setImageResource(R.drawable.nullc)
            binding.ivIndonesian.setImageResource(R.drawable.nullc)
            selectLang(ConstantVariables.KOREAN)
        }

        binding.clVietnamese.setOnClickListener {
//            binding.defLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.indianLangbtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.chineLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.spanishBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.afrecianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.itlianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.clKorean.setBackgroundResource(R.drawable.strokeshape)
//            binding.clJapanese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clMalay.setBackgroundResource(R.drawable.strokeshape)
//            binding.clVietnamese.setBackgroundResource(R.drawable.strokeshapegreen)
//            binding.clIndonesianBtn.setBackgroundResource(R.drawable.strokeshape)
            binding.defaulR.setImageResource(R.drawable.nullc)
            binding.indianLangR.setImageResource(R.drawable.nullc)
            binding.chineLanR.setImageResource(R.drawable.nullc)
            binding.spanishR.setImageResource(R.drawable.nullc)
            binding.itlianLaR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.ivKoreanRadio.setImageResource(R.drawable.nullc)
            binding.ivJapaneseRadio.setImageResource(R.drawable.nullc)
            binding.ivMalayRadio.setImageResource(R.drawable.nullc)
            binding.ivVietnameseRadio.setImageResource(R.drawable.radio)
            binding.ivIndonesian.setImageResource(R.drawable.nullc)
            selectLang(ConstantVariables.VIETNAMESE)
        }

        binding.clJapanese.setOnClickListener {
//            binding.defLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.indianLangbtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.chineLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.spanishBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.afrecianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.itlianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.clKorean.setBackgroundResource(R.drawable.strokeshape)
//            binding.clJapanese.setBackgroundResource(R.drawable.strokeshapegreen)
//            binding.clMalay.setBackgroundResource(R.drawable.strokeshape)
//            binding.clVietnamese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clIndonesianBtn.setBackgroundResource(R.drawable.strokeshape)
            binding.defaulR.setImageResource(R.drawable.nullc)
            binding.indianLangR.setImageResource(R.drawable.nullc)
            binding.chineLanR.setImageResource(R.drawable.nullc)
            binding.spanishR.setImageResource(R.drawable.nullc)
            binding.itlianLaR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.ivKoreanRadio.setImageResource(R.drawable.nullc)
            binding.ivJapaneseRadio.setImageResource(R.drawable.radio)
            binding.ivMalayRadio.setImageResource(R.drawable.nullc)
            binding.ivVietnameseRadio.setImageResource(R.drawable.nullc)
            binding.ivIndonesian.setImageResource(R.drawable.nullc)
            selectLang(ConstantVariables.JAPANESE)
        }

        binding.clMalay.setOnClickListener {
//            binding.defLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.indianLangbtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.chineLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.spanishBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.afrecianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.itlianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.clKorean.setBackgroundResource(R.drawable.strokeshape)
//            binding.clJapanese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clMalay.setBackgroundResource(R.drawable.strokeshapegreen)
//            binding.clVietnamese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clIndonesianBtn.setBackgroundResource(R.drawable.strokeshape)
            binding.defaulR.setImageResource(R.drawable.nullc)
            binding.indianLangR.setImageResource(R.drawable.nullc)
            binding.chineLanR.setImageResource(R.drawable.nullc)
            binding.spanishR.setImageResource(R.drawable.nullc)
            binding.itlianLaR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.ivKoreanRadio.setImageResource(R.drawable.nullc)
            binding.ivJapaneseRadio.setImageResource(R.drawable.nullc)
            binding.ivMalayRadio.setImageResource(R.drawable.radio)
            binding.ivVietnameseRadio.setImageResource(R.drawable.nullc)
            binding.ivIndonesian.setImageResource(R.drawable.nullc)
            selectLang(ConstantVariables.MALAY)
        }

        binding.clIndonesianBtn.setOnClickListener {
//            binding.defLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.indianLangbtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.chineLangBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.spanishBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.afrecianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.itlianBtn.setBackgroundResource(R.drawable.strokeshape)
//            binding.clKorean.setBackgroundResource(R.drawable.strokeshape)
//            binding.clJapanese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clMalay.setBackgroundResource(R.drawable.strokeshape)
//            binding.clVietnamese.setBackgroundResource(R.drawable.strokeshape)
//            binding.clIndonesianBtn.setBackgroundResource(R.drawable.strokeshapegreen)
            binding.defaulR.setImageResource(R.drawable.nullc)
            binding.indianLangR.setImageResource(R.drawable.nullc)
            binding.chineLanR.setImageResource(R.drawable.nullc)
            binding.spanishR.setImageResource(R.drawable.nullc)
            binding.itlianLaR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.afrecianLanR.setImageResource(R.drawable.nullc)
            binding.ivKoreanRadio.setImageResource(R.drawable.nullc)
            binding.ivJapaneseRadio.setImageResource(R.drawable.nullc)
            binding.ivMalayRadio.setImageResource(R.drawable.nullc)
            binding.ivVietnameseRadio.setImageResource(R.drawable.nullc)
            binding.ivIndonesian.setImageResource(R.drawable.radio)
            selectLang(ConstantVariables.INDONESIAN)
        }
    }

//    private fun loadNativeAd() {
//        try {
//            AppDelegate.nativeAd?.apply {
//                NativeAdPair(this).populate(
//                    this@SelectLangActivity,
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
//
////        BannerAdsManager(this).loadCollapseBanner(binding.bannerAdView)
//    }

//    private fun loadNativeAd() {
//        try {
//            AppDelegate.nativeAd?.apply {
//                NativeAdPair(this).populate(
//                    this@SelectLangActivity,
//                    R.layout.ad_unified_native,
//                    binding.adContainer
//                )
//                (application as AppDelegate).loadAdNativeBottomMain()
//            } ?: run {
//                loadNativeAds(
//                    binding.adContainer,
//                    R.layout.ad_unified_native,
//                    ADUnitPlacements.NATIVE_AD
//                )
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

    private fun selectLang(lang: String) {
        seletedLang = lang
        binding.toolBar.removeAdsbtn.setOnClickListener {
            SharedPrefObj.saveString(this, ConstantVariables.LANG_KEY, seletedLang)
            setLanguage(seletedLang)
            if (fromSetting) {
                InterstitialAdsManager.getInstance().showAdmobInterstitialSplash(this@SelectLangActivity) {
                    startActivity(Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
                }
            } else {
                InterstitialAdsManager.getInstance().showAdmobInterstitialSplash(this@SelectLangActivity) {
                    newScreen(BoardingActivity::class.java)
                    finish()
                }

            }
        }
    }

    private fun toolbar() {
        val typeface =
            ResourcesCompat.getFont(this, R.font.poppins_bold) // replace with your font resource
        binding.toolBar.title.typeface = typeface
        binding.toolBar.removeAdsbtn.setImageResource(R.drawable.check_circle_language)
        binding.toolBar.backBtn.visibility = View.GONE
        binding.toolBar.title.text = resources.getString(R.string.settLangT)

    }
}