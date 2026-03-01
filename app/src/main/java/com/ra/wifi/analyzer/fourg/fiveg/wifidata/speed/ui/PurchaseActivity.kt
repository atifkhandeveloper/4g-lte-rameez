package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.databinding.ActivitySelectLangBinding
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.databinding.ContentPurchaseBinding
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.splash.SplashActivity
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.Setting


class PurchaseActivity : LocalizationActivity() {

    private var skuListSubscriptionsList: ArrayList<String>? = null
    lateinit var binding: ContentPurchaseBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ContentPurchaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Subscription Arraylist Id
        skuListSubscriptionsList = ArrayList()
        skuListSubscriptionsList!!.add(Setting.threeDayTrails)
        skuListSubscriptionsList!!.add(Setting.monthlyAds)
        skuListSubscriptionsList!!.add(Setting.removeAdsOneYearId)

        startSplashActivity()
        //Billing Method


    }



    private fun startSplashActivity() {
        finish()
        startActivity(Intent(this,SplashActivity::class.java));
    }




}