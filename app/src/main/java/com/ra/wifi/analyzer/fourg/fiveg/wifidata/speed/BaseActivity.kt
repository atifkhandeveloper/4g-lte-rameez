package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed

import androidx.appcompat.app.AppCompatActivity
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

abstract class BaseActivity : LocalizationActivity() {
    protected val config: FirebaseRemoteConfig by lazy { FirebaseRemoteConfig.getInstance() }
}