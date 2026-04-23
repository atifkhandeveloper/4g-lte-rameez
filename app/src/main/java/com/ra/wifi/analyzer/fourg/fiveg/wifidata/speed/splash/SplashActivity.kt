package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.app.AppDelegate
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.core.PremiumManager
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.databinding.ActivitySplashBinding
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui.MainActivity
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui.PremiumActivity
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui.SelectLangActivity
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.SharedPrefObj
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.isOnline

class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding
    private var animator: ObjectAnimator? = null

    private var hasNavigated = false
    private var nativeAd: NativeAd? = null

    private val sharedPreferences by lazy {
        getSharedPreferences("Myrehgffs", Context.MODE_PRIVATE)
    }

    private val launchCountKey = "LAUNCH_COUNT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        applyEdgeToEdgePadding(binding.root)
        setContentView(binding.root)

        incrementLaunchCount()
        animateProgressBar()

        if (PremiumManager.shouldShowAds(this)) {
            loadnative()
        }
    }

    // ---------------- SPLASH ANIMATION ----------------

    private fun animateProgressBar() {

        val duration = if (!isOnline()) 3000L else 12000L

        animator = ObjectAnimator.ofInt(
            binding.progressBar,
            "progress",
            0,
            binding.progressBar.max
        )

        animator?.duration = duration

        animator?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                handleSplashEnd()
            }
        })

        animator?.start()
    }

    // ---------------- SPLASH END ----------------

    private fun handleSplashEnd() {

        if (hasNavigated) return
        hasNavigated = true

        val app = application as AppDelegate

        // ✅ PREMIUM → NO ADS
        if (PremiumManager.isPremium(this)) {
            navigateDirect()
            return
        }

        // ✅ ADS FLOW (SAFE CALLBACK)
        app.showAdIfAvailable(this) {
            navigateDirect()
        }
    }

    // ---------------- NAVIGATION ----------------

    private fun navigateDirect() {

        if (PremiumManager.isPremium(this)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        when (getLaunchCount()) {

            0 -> startActivity(Intent(this, PremiumActivity::class.java))

            1 -> startActivity(Intent(this, SelectLangActivity::class.java))

            else -> {
                if (SharedPrefObj.getToken(this) != null) {
                    startActivity(Intent(this, PremiumActivity::class.java))
                } else {
                    startActivity(Intent(this, SelectLangActivity::class.java))
                }
            }
        }

        finish()
    }

    // ---------------- LAUNCH COUNT ----------------

    private fun incrementLaunchCount() {
        val count = sharedPreferences.getInt(launchCountKey, 0)
        sharedPreferences.edit().putInt(launchCountKey, count + 1).apply()
    }

    private fun getLaunchCount(): Int {
        return sharedPreferences.getInt(launchCountKey, 0)
    }

    // ---------------- LIFECYCLE ----------------

    override fun onPause() {
        super.onPause()
        animator?.pause()
    }

    override fun onResume() {
        super.onResume()
        animator?.resume()
    }

    override fun onDestroy() {
        animator?.removeAllListeners()
        animator = null
        super.onDestroy()
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