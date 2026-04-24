package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.firebase.analytics.FirebaseAnalytics
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.*
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.core.PremiumManager
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.databinding.ActivityMainBinding
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.features.newScreen
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.settings.AppLanguageObj
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.NewScreen
import kotlin.random.Random
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.R


class MainActivity : BaseActivity() {

    val appL by lazy { AppLanguageObj() }
    lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    private var interstitialAd: InterstitialAd? = null
    private var nativeAd: NativeAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        applyEdgeToEdgePadding(binding.root)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("Myrehgffs", Context.MODE_PRIVATE)

        setupUI()
        checkNotificationPermission()

        BillingRepository.init(
            this,
            onPremiumUnlocked = {

                runOnUiThread {

                    // refresh UI instantly
                    interstitialAd = null
                    binding.pro.visibility = View.GONE
                }
            }
        )
    }

    // ---------------- LIFECYCLE FIX (IMPORTANT) ----------------
//    override fun onResume() {
//        super.onResume()
//
//        // 🔥 ALWAYS REFRESH PREMIUM STATE
//        if (PremiumManager.isPremium(this)) {
//            disableAdsUI()
//        } else {
//            enableAdsUI()
//            loadAd()
//            loadnative()
//        }
//    }

    // ---------------- UI STATE ----------------
    private fun enableAdsUI() {
        binding.pro.visibility = View.VISIBLE
    }

    private fun disableAdsUI() {
        binding.pro.visibility = View.GONE
        findViewById<TemplateView>(R.id.my_template)?.visibility = View.GONE
        interstitialAd = null
    }

    // ---------------- SETUP ----------------
    private fun setupUI() {
        showGoogleRateDialog()
        buttonClicks()
        darkMode()
    }

    // ---------------- PERMISSION ----------------
    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 11)
            }
        }
    }

    // ---------------- BACK ----------------
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
//        onBackPressed()
        showExitDialog()
    }

    private fun showExitDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.exit_dialog_new, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        val template =
            dialogView.findViewById<TemplateView>(R.id.my_template)

        // Load Native Ad inside Exit Dialog
        loadNativeForExitDialog(template)

        dialogView.findViewById<ConstraintLayout>(R.id.speedTestBtn).setOnClickListener {
            showInterstitial { newScreen(SpeedTestActivity::class.java) }
        }

        dialogView.findViewById<TextView>(R.id.tv_okBtn).setOnClickListener {
            dialog.dismiss()
            finishAffinity()
        }

        dialogView.findViewById<TextView>(R.id.tv_cancelBtn).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    // ---------------- ADS SAFETY ----------------
    private fun isPremium() = PremiumManager.isPremium(this)

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


    private fun loadNativeForExitDialog(template: TemplateView) {

        if (PremiumManager.isPremium(this)) {
            template.visibility = View.GONE
            return
        }

        MobileAds.initialize(this)

        val adLoader = AdLoader.Builder(this, getString(R.string.nativeId))
            .forNativeAd { ad ->

                // Destroy previous ad first
                nativeAd?.destroy()
                nativeAd = ad

                // Revenue Tracking
                ad.setOnPaidEventListener { adValue ->
                    val revenue = adValue.valueMicros / 1_000_000.0
                    val currency = adValue.currencyCode

                    Log.d("Ads", "Exit Dialog Native Revenue: $revenue $currency")

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

    private fun loadAd() {
        if (isPremium()) return

        InterstitialAd.load(
            this,
            getString(R.string.inter),
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {

                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad

                    // ✅ ADD THIS
                    interstitialAd?.setOnPaidEventListener { adValue ->
                        val revenue = adValue.valueMicros / 1_000_000.0
                        val currency = adValue.currencyCode

                        Log.d("Ads", "Interstitial Revenue: $revenue $currency")

                        sendRevenueToFirebase(revenue, currency)
                    }
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd = null
                }
            }
        )
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

    private fun showInterstitial(action: () -> Unit) {

        if (isPremium()) {
            action()
            return
        }

        if (interstitialAd == null) {
            action()
            loadAd()
            return
        }

        interstitialAd?.fullScreenContentCallback =
            object : FullScreenContentCallback() {

                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    loadAd()
                    action()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    interstitialAd = null
                    loadAd()
                    action()
                }
            }

        interstitialAd?.show(this)
    }

    private fun showAdWithRandom(action: () -> Unit) {

        if (isPremium()) {
            action()
            return
        }

        if (Random.nextInt(10) < 8) {
            showInterstitial(action)
        } else {
            action()
        }
    }

    // ---------------- BUTTONS ----------------
    private fun buttonClicks() {

        binding.clHowToUse.setOnClickListener {
            showAdWithRandom { newScreen(HowtoUseActivity::class.java) }
        }

        binding.settingLayoutBtn.setOnClickListener {
            showAdWithRandom { newScreen(LteSettingsActivity::class.java) }
        }

        binding.dataUsageBtn.setOnClickListener {
            showAdWithRandom { newScreen(DataUsageActivity::class.java) }
        }

        binding.speedTestBtn.setOnClickListener {
            lockFeature { showAdWithRandom { newScreen(SpeedTestActivity::class.java) } }
        }

        binding.simInfoBtn.setOnClickListener {
            lockFeature { showAdWithRandom { newScreen(SimInfoActivity::class.java) } }
        }

        binding.signalSrengthBtn.setOnClickListener {
            showAdWithRandom { newScreen(SignalStrengthActivity::class.java) }
        }

        binding.settingsIconeBtn.setOnClickListener {
            showAdWithRandom { NewScreen.start(this, SettingsActivity::class.java) }
        }

        binding.pro.setOnClickListener {
            startActivity(Intent(this, PremiumActivity::class.java))
        }
    }

    private fun lockFeature(action: () -> Unit) {
        if (isPremium()) {
            action()
        } else {
            startActivity(Intent(this, PremiumActivity::class.java))
        }
    }

    // ---------------- RATE ----------------
    private fun showGoogleRateDialog() {
        val manager = ReviewManagerFactory.create(this)

        manager.requestReviewFlow().addOnCompleteListener {
            if (it.isSuccessful) {
                manager.launchReviewFlow(this, it.result)
            }
        }
    }

    // ---------------- DARK MODE ----------------
    private fun darkMode() {
        binding.moonIconeBtn.setOnClickListener {
            val newMode =
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                    AppCompatDelegate.MODE_NIGHT_NO
                else
                    AppCompatDelegate.MODE_NIGHT_YES

            AppCompatDelegate.setDefaultNightMode(newMode)
            sharedPreferences.edit().putInt("MODE", newMode).apply()
            recreate()
        }
    }
    override fun onResume() {
        super.onResume()

        if (PremiumManager.isPremium(this)) {

            interstitialAd = null

            binding.pro.visibility = View.GONE

            findViewById<TemplateView>(R.id.my_template)?.apply {
                visibility = View.GONE
            }

            nativeAd?.destroy()
            nativeAd = null

        } else {
            loadAd()
            loadnative()
        }
    }
    override fun onDestroy() {
        nativeAd?.destroy()
        nativeAd = null
        super.onDestroy()
    }


}