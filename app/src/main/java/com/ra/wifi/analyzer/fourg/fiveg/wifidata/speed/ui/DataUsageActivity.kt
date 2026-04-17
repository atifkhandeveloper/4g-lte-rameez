package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui

import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Process
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager

import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.BaseActivity
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.R
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.FirebaseAds.AdmobAds
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adapters.DataUsagesAdapter
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.core.PremiumManager
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.databinding.ActivityDataUsageBinding
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.isAdEnable
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adsManager.ADUnitPlacements
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adsManager.BannerAdsManager
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adsManager.NativeAdPair
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adsManager.loadCollapseBanner
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adsManager.loadNativeAds
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.modals.UsagesData
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.ConfigParam
import dev.jahidhasanco.networkusage.*

class DataUsageActivity : BaseActivity() {
    private val networkUsage by lazy { NetworkUsageManager(this, Util.getSubscriberId(this)) }
    private val usagesDataList: ArrayList<UsagesData> by lazy { ArrayList() }
    val dataUsagesAdapter by lazy { DataUsagesAdapter(usagesDataList) }
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("Myrehgffs", Context.MODE_PRIVATE)
    }
    lateinit var binding: ActivityDataUsageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataUsageBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        applyEdgeToEdgePadding(binding.root)
        setContentView(binding.root)

//        NativeAdsManager.CheckNative(this, window.decorView.rootView)


        toolBar()
        setupPermissions()
        refreshLayout()
        setupData()
        darkMode()

        if (PremiumManager.shouldShowAds(this)) {
            loadnative()
        }

//        nativeAds()
//        loadNativeAd()
    }

    private fun toolBar() {

        binding.toolBar.removeAdsbtn.visibility = View.GONE
        binding.toolBar.title.setText("Data Usage")
        binding.toolBar.backBtn.setOnClickListener { onBackPressed() }
    }

//    private fun nativeAds() {
//        MobileAds.initialize(this@DataUsageActivity)
//        val adLoader = AdLoader.Builder(this@DataUsageActivity, getString(R.string.speed_test_and_data_usage_native))
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

//    private fun loadNativeAd() {
//        try {
//            BannerAdsManager(this).loadCollapseBanner(binding.bannerAdView)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

    private fun darkMode() {
        val savedMode = sharedPreferences.getInt("MODE", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        when (savedMode) {
            AppCompatDelegate.MODE_NIGHT_YES -> {
                binding.title.setTextColor(resources.getColor(R.color.white))
            }

            AppCompatDelegate.MODE_NIGHT_NO -> {
//                binding.title.setTextColor(resources.getColor(R.color.black))
            }

            else -> {
            }
        }
    }

    private fun setupData() {
        val handler = Handler(Looper.getMainLooper())
        val runnableCode = object : Runnable {
            override fun run() {
//                val now = networkUsage.getUsageNow(NetworkType.ALL)
//                val speeds = NetSpeed.calculateSpeed(now.timeTaken, now.downloads, now.uploads)
//                val todayM = networkUsage.getUsage(Interval.today, NetworkType.MOBILE)
//                val todayW = networkUsage.getUsage(Interval.today, NetworkType.WIFI)
//                binding.wifiUsagesTv.text = "WiFi: " + Util.formatData(todayW.downloads, todayW.uploads)[2]
//                binding.dataUsagesTv.text = "Mobile: " + Util.formatData(todayM.downloads, todayM.uploads)[2]
//                binding.apply {
//                    totalSpeedTv.text = speeds[0].speed + "\n" + speeds[0].unit
//                    downUsagesTv.text = "Down: " + speeds[1].speed + speeds[1].unit
//                    upUsagesTv.text = "Up: " + speeds[2].speed + speeds[2].unit
//                }
//                handler.postDelayed(this, 1000)
            }
        }

        runnableCode.run()

        val last30DaysWIFI = networkUsage.getMultiUsage(
            Interval.lastMonthDaily, NetworkType.WIFI
        )

        val last30DaysMobile = networkUsage.getMultiUsage(
            Interval.lastMonthDaily, NetworkType.MOBILE
        )

        for (i in last30DaysWIFI.indices) {
            usagesDataList.add(
                UsagesData(
                    Util.formatData(
                        last30DaysMobile[i].downloads,
                        last30DaysMobile[i].uploads
                    )[2],
                    Util.formatData(
                        last30DaysWIFI[i].downloads,
                        last30DaysWIFI[i].uploads
                    )[2],
                    last30DaysWIFI[i].date
                )
            )
        }

        val last7DaysTotalWIFI = networkUsage.getUsage(
            Interval.last7days, NetworkType.WIFI
        )

        val last7DaysTotalMobile = networkUsage.getUsage(
            Interval.last7days, NetworkType.MOBILE
        )

        val last30DaysTotalWIFI = networkUsage.getUsage(
            Interval.last30days, NetworkType.WIFI
        )

        val last30DaysTotalMobile = networkUsage.getUsage(
            Interval.last30days, NetworkType.MOBILE
        )

        usagesDataList.add(
            UsagesData(
                Util.formatData(
                    last7DaysTotalMobile.downloads,
                    last7DaysTotalMobile.uploads
                )[2],
                Util.formatData(
                    last7DaysTotalWIFI.downloads,
                    last7DaysTotalWIFI.uploads
                )[2],
                "Last 7 Days"
            )
        )

        binding.wifiDataThisMonth.text = Util.formatData(
            last30DaysTotalWIFI.downloads,
            last30DaysTotalWIFI.uploads
        )[2]

        binding.mobileDataThisMonth.text = Util.formatData(
            last30DaysTotalMobile.downloads,
            last30DaysTotalMobile.uploads
        )[2]

        binding.monthlyDataUsagesRv.layoutManager = LinearLayoutManager(this)
        binding.monthlyDataUsagesRv.setHasFixedSize(true)
        binding.monthlyDataUsagesRv.adapter = dataUsagesAdapter

    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_PHONE_STATE
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_PHONE_STATE), 34
            )
        }

        if (!checkUsagePermission()) {
            Toast.makeText(this, "Please allow usage access", Toast.LENGTH_SHORT).show()
        }

    }

    private fun checkUsagePermission(): Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        var mode: Int = appOps.checkOpNoThrow(
            "android:get_usage_stats", Process.myUid(),
            packageName

        )
        val granted = mode == AppOpsManager.MODE_ALLOWED
        if (!granted) {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            startActivity(intent)
            return false
        }
        return true
    }

    private fun refreshLayout() {
        binding.swipRefresLayout.setColorSchemeColors(
            ContextCompat.getColor(this, android.R.color.holo_orange_dark),
            ContextCompat.getColor(this, android.R.color.holo_green_dark),
            ContextCompat.getColor(this, android.R.color.darker_gray),
            ContextCompat.getColor(this, android.R.color.holo_blue_dark)
        )
        binding.swipRefresLayout.setOnRefreshListener {
            usagesDataList.clear()
            setupData()
            binding.swipRefresLayout.isRefreshing = false

        }
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
}