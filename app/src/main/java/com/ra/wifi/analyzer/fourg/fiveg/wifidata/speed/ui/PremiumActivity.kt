package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.android.billingclient.api.ProductDetails
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.R
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.billing.BillingRepository

class PremiumActivity : AppCompatActivity() {

    private lateinit var btnWeekly: RadioButton
    private lateinit var btnMonthly: RadioButton
    private lateinit var btnLifetime: RadioButton

    private lateinit var btnSubscribe: AppCompatButton
    private lateinit var btnRestore: TextView
    private lateinit var btnClose: ImageView

    private var interstitialAd: InterstitialAd? = null
    private var plans = emptyList<PlanUiModel>()

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paywall)

        initViews()
        initPlans()
        setupClicks()
        setupDefaultSelection()
        loadAd()

        showCloseButtonAfterDelay()

        BillingRepository.init(
            this,
            onPremiumUnlocked = {
                setResult(RESULT_OK)
                finish()
            }
        )
    }

    // ---------------- INIT VIEWS ----------------
    private fun initViews() {

        btnWeekly = findViewById(R.id.btnWeekly)
        btnMonthly = findViewById(R.id.btnYearly)
        btnLifetime = findViewById(R.id.lifeTime)

        btnSubscribe = findViewById(R.id.btnSubscribe)
        btnRestore = findViewById(R.id.btnRestore)
        btnClose = findViewById(R.id.btnClose)

        // hide initially
        btnClose.visibility = View.INVISIBLE
    }

    // ---------------- SHOW CLOSE AFTER 3 SEC ----------------
    private fun showCloseButtonAfterDelay() {
        handler.postDelayed({
            btnClose.visibility = View.VISIBLE
        }, 3000)
    }

    // ---------------- DEFAULT ----------------
    private fun setupDefaultSelection() {
        btnWeekly.isChecked = true
        updateButtonText("weekly_fourg")
    }

    // ---------------- LOAD PLANS ----------------
    private fun initPlans() {
        plans = BillingRepository.getCachedPlans()
        bindPlans()
    }

    private fun bindPlans() {

        btnWeekly.text = formatPlan("weekly_fourg")
        btnMonthly.text = formatPlan("monthly_subscription")
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

    // ---------------- CLICK ----------------
    private fun setupClicks() {

        btnWeekly.setOnClickListener {
            selectOnly(btnWeekly)
            updateButtonText("weekly_fourg")
        }

        btnMonthly.setOnClickListener {
            selectOnly(btnMonthly)
            updateButtonText("monthly_subscription")
        }

        btnLifetime.setOnClickListener {
            selectOnly(btnLifetime)
            updateButtonText("lifetime")
        }

        btnSubscribe.setOnClickListener {

            val selected = when {
                btnWeekly.isChecked -> plans.find { it.id == "weekly_fourg" }
                btnMonthly.isChecked -> plans.find { it.id == "monthly_subscription" }
                btnLifetime.isChecked -> plans.find { it.id == "lifetime" }
                else -> null
            } ?: return@setOnClickListener

            BillingRepository.launchPurchase(this, selected)
        }

        btnRestore.setOnClickListener {
            // BillingRepository.restorePurchases(this)
        }

        btnClose.setOnClickListener {
            showAd()
        }
    }

    // ---------------- SINGLE SELECT ----------------
    private fun selectOnly(selected: RadioButton) {
        btnWeekly.isChecked = selected == btnWeekly
        btnMonthly.isChecked = selected == btnMonthly
        btnLifetime.isChecked = selected == btnLifetime
    }

    // ---------------- BUTTON TEXT ----------------
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

        com.google.android.gms.ads.interstitial.InterstitialAd.load(
            this,
            getString(R.string.inter),
            com.google.android.gms.ads.AdRequest.Builder().build(),
            object : com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback() {

                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }

                override fun onAdFailedToLoad(error: com.google.android.gms.ads.LoadAdError) {
                    interstitialAd = null
                }
            }
        )
    }

    private fun showAd() {

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
}