package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

abstract class BaseActivity : LocalizationActivity() {
    protected val config: FirebaseRemoteConfig by lazy { FirebaseRemoteConfig.getInstance() }

    fun enableEdgeToEdge() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    fun applyEdgeToEdgePadding(root: View) {

        ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->

            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }
    }
}