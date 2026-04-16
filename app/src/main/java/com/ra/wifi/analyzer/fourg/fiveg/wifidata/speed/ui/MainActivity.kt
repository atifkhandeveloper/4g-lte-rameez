package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
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
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory

import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.BaseActivity
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.features.newScreen
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.isAdEnable
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.settings.AppLanguageObj
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.ConfigParam
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.NewScreen
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.Setting
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.R
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.core.PremiumManager
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : BaseActivity() {

    val appL by lazy { AppLanguageObj() }
    lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var isFirstTime = true
    private var lastAdShownTime = 0L
    private var pendingAction: (() -> Unit)? = null
    private var exitFragmentContainer: ConstraintLayout? = null

    var fragmentManager: FragmentManager? = null
    private var interstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //  initEvent()

//        NativeAdsManager.CheckNative(this,window.decorView.rootView)
        fragmentManager = supportFragmentManager


        if (PremiumManager.shouldShowAds(this)) {
            loadnative()
            loadAd()
            binding.pro.visibility = View.VISIBLE
        }

        showGoogleRateDialog()

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
                binding.fourGTitleLte.setTextColor(resources.getColor(R.color.black))
//
                binding.clHowToUse.setBackgroundResource(R.drawable.round_shape_dark)
                binding.settinngs.setBackgroundResource(R.drawable.round_shape_dark)
                binding.spped.setBackgroundResource(R.drawable.round_shape_dark)
                binding.sped.setBackgroundResource(R.drawable.round_shape_dark)
                binding.ped.setBackgroundResource(R.drawable.round_shape_dark)
                binding.pd.setBackgroundResource(R.drawable.round_shape_dark)

                binding.howtoUseBtn.setTextColor(resources.getColor(R.color.black))
                binding.ping4g.setTextColor(resources.getColor(R.color.black))
                binding.settinngs.setTextColor(resources.getColor(R.color.black))
                binding.spped.setTextColor(resources.getColor(R.color.black))
                binding.sped.setTextColor(resources.getColor(R.color.black))
                binding.ped.setTextColor(resources.getColor(R.color.black))
                binding.pd.setTextColor(resources.getColor(R.color.black))

            }

//            AppCompatDelegate.MODE_NIGHT_NO -> {
//                binding.howtoUseBtn.setTextColor(resources.getColor(R.color.lightTextC))
//                binding.clHowToUse.setBackgroundResource(R.drawable.roundedcgreen)
//                binding.fourGTitleLte.setTextColor(resources.getColor(R.color.black))
//                binding.settingsIconeBtn.setImageResource(R.drawable.settingsicone)
//                binding.moonIconeBtn.setImageResource(R.drawable.moonicone)
//                binding.settinngs.setTextColor(resources.getColor(R.color.lightTextC))
//                binding.spped.setTextColor(resources.getColor(R.color.lightTextC))
//                binding.sped.setTextColor(resources.getColor(R.color.lightTextC))
//                binding.ped.setTextColor(resources.getColor(R.color.lightTextC))
//                binding.pd.setTextColor(resources.getColor(R.color.lightTextC))
//
//                binding.clHowToUse.setBackgroundResource(R.drawable.roundedc)
////
//            }

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


    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        onBackPressed()
//        MyInterstitialControllerExitBtn.getInstance().showInterstitial(this) {
//                newScreen(HowtoUseActivity::class.java)
//                showBottomSheetDialog()
//        showExitDialog()


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



        dialogView.findViewById<ConstraintLayout>(R.id.speedTestBtn).setOnClickListener {
            interstitialAd?.show(this)
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


    private fun replaceFragment(fragment: Fragment) {
        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.exitFragmentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun loadAd() {
        // Request a new ad if one isn't already loaded.


        // [START load_ad]
        InterstitialAd.load(
            this,
            resources.getString(R.string.inter),
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded.")
                    interstitialAd = ad
                    // [START_EXCLUDE silent]
                    // [END_EXCLUDE]
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.message)
                    interstitialAd = null
                    // [START_EXCLUDE silent]
                    val error =
                        "domain: ${adError.domain}, code: ${adError.code}, " + "message: ${adError.message}"

                    // [END_EXCLUDE]
                }
            },
        )
        // [END load_ad]
    }

    private fun showInterstitial() {
        if (interstitialAd != null) {
            // [START set_fullscreen_callback]
            interstitialAd?.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        Log.d(TAG, "Ad was dismissed.")
                        val intent = Intent(this@MainActivity, HowtoUseActivity::class.java)
                        startActivity(intent)
                        loadAd()
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        interstitialAd = null
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        // Called when fullscreen content failed to show.
                        Log.d(TAG, "Ad failed to show.")
                        val intent = Intent(this@MainActivity, HowtoUseActivity::class.java)
                        startActivity(intent)
                        loadAd()
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        interstitialAd = null
                    }

                    override fun onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        Log.d(TAG, "Ad showed fullscreen content.")
                    }

                    override fun onAdImpression() {
                        // Called when an impression is recorded for an ad.
                        Log.d(TAG, "Ad recorded an impression.")
                    }

                    override fun onAdClicked() {
                        // Called when ad is clicked.
                        Log.d(TAG, "Ad was clicked.")
                    }
                }
            // [END set_fullscreen_callback]

            // [START show_ad]
            interstitialAd?.show(this)
            // [END show_ad]
        } else {
            loadAd()
        }


    }


    private fun showAdWithRandom(action: () -> Unit) {

        // 🚫 PREMIUM USERS SKIP ADS
        if (PremiumManager.shouldShowAds(this).not()) {
            action()
            return
        }

        val randomNumber = Random.nextInt(0, 10)

        if (randomNumber < 8 && interstitialAd != null) {

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

        } else {
            action()
        }
    }


    private fun buttonClicks() {

        binding.clHowToUse.setOnClickListener {
            showAdWithRandom {
                newScreen(HowtoUseActivity::class.java)
            }
        }

        binding.settingLayoutBtn.setOnClickListener {
            showAdWithRandom {
                newScreen(LteSettingsActivity::class.java)
            }
        }

        binding.dataUsageBtn.setOnClickListener {
            showAdWithRandom {
                newScreen(DataUsageActivity::class.java)
            }
        }

        binding.speedTestBtn.setOnClickListener {
            lockFeature {
                showAdWithRandom {
                    newScreen(SpeedTestActivity::class.java)
                }
            }
        }

        binding.simInfoBtn.setOnClickListener {
            lockFeature {
                showAdWithRandom {
                    newScreen(SimInfoActivity::class.java)
                }
            }
        }

        binding.signalSrengthBtn.setOnClickListener {
            showAdWithRandom {
                newScreen(SignalStrengthActivity::class.java)
            }
        }

        binding.settingsIconeBtn.setOnClickListener {
            showAdWithRandom {
                NewScreen.start(this, SettingsActivity::class.java)
            }
        }
        binding.pro.setOnClickListener {
            startActivity(Intent(this, PremiumActivity::class.java))
        }
    }


    private fun lockFeature(action: () -> Unit) {
        if (PremiumManager.isPremium(this)) {
            action()
        } else {
            startActivity(Intent(this, PremiumActivity::class.java))
        }
    }

    private fun showGoogleRateDialog() {

        val manager: ReviewManager = ReviewManagerFactory.create(this)

        val request = manager.requestReviewFlow()

        request.addOnCompleteListener { task ->

            if (task.isSuccessful) {

                val reviewInfo = task.result

                val flow = manager.launchReviewFlow(this, reviewInfo)

                flow.addOnCompleteListener {
                    // Review flow completed (user may or may not submit rating)
                }

            } else {
                // Fallback (optional)
                // openPlayStore()
            }
        }
    }

}
