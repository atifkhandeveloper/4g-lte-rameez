package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.analytics.FirebaseAnalytics
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.BaseActivity
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.R
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.billing.BillingRepository
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.core.PremiumManager
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.databinding.ActivityPaywallBinding
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.databinding.ActivityPurchaseBinding
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.databinding.ContentPurchaseBinding
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui.MainActivity

class PremiumActivity : BaseActivity() {

    private lateinit var btnWeekly: RadioButton
    private lateinit var btnYearly: RadioButton
    private lateinit var btnLifetime: RadioButton

    private lateinit var btnSubscribe: AppCompatButton
    private lateinit var btnRestore: TextView
    private lateinit var btnClose: ImageView

    private var interstitialAd: InterstitialAd? = null
    private var plans = emptyList<PlanUiModel>()

    private val handler = Handler(Looper.getMainLooper())

    // prevent multiple ad triggers
    private var isAdShownOnClose = false
    lateinit var binding: ActivityPaywallBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaywallBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        applyEdgeToEdgePadding(binding.root)
        setContentView(binding.root)

        // ✅ If already premium → skip screen
        if (PremiumManager.isPremium(this)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        initViews()
        initPlans()
        setupClicks()
        setupDefaultSelection()

        // ✅ Load ads only for non-premium
        if (PremiumManager.shouldShowAds(this)) {
            loadAd()
        }

        showCloseButtonAfterDelay()

        BillingRepository.init(
            this,
            onPremiumUnlocked = {
                setResult(RESULT_OK)

                // ✅ Refresh app (remove ads everywhere)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        )
    }

    // ---------------- INIT ----------------
    private fun initViews() {
        btnWeekly = findViewById(R.id.btnWeekly)
        btnYearly = findViewById(R.id.btnYearly)
        btnLifetime = findViewById(R.id.lifeTime)

        btnSubscribe = findViewById(R.id.btnSubscribe)
        btnRestore = findViewById(R.id.btnRestore)
        btnClose = findViewById(R.id.btnClose)

        btnClose.visibility = View.INVISIBLE
    }

    private fun showCloseButtonAfterDelay() {
        handler.postDelayed({
            btnClose.visibility = View.VISIBLE
        }, 3000)
    }

    private fun setupDefaultSelection() {
        btnWeekly.isChecked = true
        updateButtonText("weekly_fourg")
    }

    // ---------------- PLANS ----------------
    private fun initPlans() {
        plans = BillingRepository.getCachedPlans()
        bindPlans()
    }

    private fun bindPlans() {
        btnWeekly.text = formatPlan("weekly_fourg")
        btnYearly.text = formatPlan("monthly_subscription")
        btnLifetime.text = formatPlan("lifetime")
    }

    private fun formatPlan(id: String): String {
        val plan = plans.find { it.id == id } ?: return "Loading..."

        return buildString {
            append(plan.title)
            append("\n")

            if (plan.hasFreeTrial) {
                append("3 Days Free Trial • ")
            }

            append(plan.price)

            if (plan.isBestValue) {
                append(" ⭐ BEST VALUE")
            }
        }
    }

    // ---------------- CLICKS ----------------
    private fun setupClicks() {

        btnWeekly.setOnClickListener {
            selectOnly(btnWeekly)
            updateButtonText("weekly_fourg")
        }

        btnYearly.setOnClickListener {
            selectOnly(btnYearly)
            updateButtonText("monthly_subscription")
        }

        btnLifetime.setOnClickListener {
            selectOnly(btnLifetime)
            updateButtonText("lifetime")
        }

        btnSubscribe.setOnClickListener {

            val selected = when {
                btnWeekly.isChecked -> plans.find { it.id == "weekly_fourg" }
                btnYearly.isChecked -> plans.find { it.id == "monthly_subscription" }
                btnLifetime.isChecked -> plans.find { it.id == "lifetime" }
                else -> null
            } ?: return@setOnClickListener

            BillingRepository.launchPurchase(this, selected)
        }

        btnRestore.setOnClickListener {
            // BillingRepository.restorePurchases(this)
        }

        // ✅ CLOSE BUTTON WITH AD (NON-PREMIUM ONLY)
        btnClose.setOnClickListener {

            if (PremiumManager.isPremium(this)) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                return@setOnClickListener
            }

            if (!isAdShownOnClose) {
                isAdShownOnClose = true
                showAdOnClose()
            } else {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun selectOnly(selected: RadioButton) {
        btnWeekly.isChecked = selected == btnWeekly
        btnYearly.isChecked = selected == btnYearly
        btnLifetime.isChecked = selected == btnLifetime
    }

    private fun updateButtonText(planId: String) {
        btnSubscribe.text = when (planId) {
            "weekly_fourg" -> "START FREE TRIAL"
            "monthly_subscription" -> "SUBSCRIBE NOW"
            "lifetime" -> "BUY LIFETIME"
            else -> "SUBSCRIBE"
        }
    }

    // ---------------- ADS ----------------
    private fun loadAd() {

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

    private fun showAdOnClose() {

        if (!PremiumManager.shouldShowAds(this)) {

            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        if (interstitialAd == null) {

            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        interstitialAd?.fullScreenContentCallback =
            object : FullScreenContentCallback() {

                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    loadAd()

                    startActivity(Intent(this@PremiumActivity, MainActivity::class.java))
                    finish()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    interstitialAd = null
                    loadAd()
                    startActivity(Intent(this@PremiumActivity, MainActivity::class.java))
                    finish()
                }
            }

        interstitialAd?.show(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
    override fun onBackPressed() {
        // Do nothing
    }
}