package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.BaseActivity
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.app.AppDelegate
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.core.PremiumManager
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.databinding.ActivitySplashBinding
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.isAdEnable
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.settings.AppLanguageObj
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui.MainActivity
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui.PremiumActivity
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui.SelectLangActivity
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.ConfigParam
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.SharedPrefObj
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.isOnline

class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding
    private var animator: ObjectAnimator? = null
    private lateinit var appUpdateManager: AppUpdateManager

    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("Myrehgffs", Context.MODE_PRIVATE)
    }

    private val launchCountKey = "LAUNCH_COUNT"
    private val updateType = AppUpdateType.IMMEDIATE
    private val appLanguage by lazy { AppLanguageObj() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        appLanguage.checkApp(this)

        appUpdateManager = AppUpdateManagerFactory.create(this)
        checkForAppUpdate()

//        applyDarkMode()

        incrementLaunchCount()

        if (PremiumManager.shouldShowAds(this)) {
            loadSplashAd()
        }




        animateProgressBar()
    }

    // ----------------------------
    // Progress Animation
    // ----------------------------

    private fun animateProgressBar() {
        val progressBar = binding.progressBar

        val duration = if (!isOnline()) 3000L else 12000L

        animator = ObjectAnimator.ofInt(progressBar, "progress", 0, progressBar.max)
        animator?.duration = duration

        animator?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                showSplashAdThenNavigate()
            }
        })

        animator?.start()
    }

    // ----------------------------
    // Ads
    // ----------------------------

    private fun loadSplashAd() {

    }

    private fun showSplashAdThenNavigate() {

        (application as AppDelegate).showAdIfAvailable(
            this@SplashActivity,
            object : AppDelegate.OnShowAdCompleteListener {

                override fun onShowAdComplete() {

                    // Use Activity context properly
                    if (PremiumManager.isPremium(this@SplashActivity)) {
                        startActivity(
                            Intent(this@SplashActivity, MainActivity::class.java)
                        )
                    } else {
                        startNextActivity()
                    }

                    finish()
                }
            }
        )
    }






    // ----------------------------
    // Navigation Logic
    // ----------------------------

    private fun startNextActivity() {

        when (getLaunchCount()) {

            1 -> {
                startActivity(Intent(this, SelectLangActivity::class.java))
            }

            2 -> {
                startActivity(Intent(this, SecondTimeOnly::class.java))
            }

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

    private fun incrementLaunchCount() {
        val launchCount = sharedPreferences.getInt(launchCountKey, 0)
        sharedPreferences.edit()
            .putInt(launchCountKey, launchCount + 1)
            .apply()
    }

    private fun getLaunchCount(): Int {
        return sharedPreferences.getInt(launchCountKey, 0)
    }

    // ----------------------------
    // Dark Mode
    // ----------------------------

//    private fun applyDarkMode() {
//        val savedMode = sharedPreferences.getInt(
//            "MODE",
//            AppCompatDelegate.MODE_NIGHT_YES
//        )
//
//        AppCompatDelegate.setDefaultNightMode(savedMode)
//    }

    // ----------------------------
    // In-App Update
    // ----------------------------

    private fun checkForAppUpdate() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->

            val isUpdateAvailable =
                info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE

            val isAllowed =
                updateType == AppUpdateType.IMMEDIATE &&
                        info.isImmediateUpdateAllowed

            if (isUpdateAvailable && isAllowed) {
                appUpdateManager.startUpdateFlowForResult(
                    info,
                    updateType,
                    this,
                    123
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()

        animator?.resume()

        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            if (info.updateAvailability()
                == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    info,
                    AppUpdateType.IMMEDIATE,
                    this,
                    123
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        animator?.pause()
    }

    override fun onDestroy() {
        animator?.removeAllListeners()
        animator = null
        super.onDestroy()
    }
}