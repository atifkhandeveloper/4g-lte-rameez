package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.features

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import java.math.BigInteger
import java.net.HttpURLConnection
import java.net.InetAddress
import java.net.URL
import java.nio.ByteOrder

fun Context.showCustomToast(m:String){
    Toast.makeText(this, m, Toast.LENGTH_SHORT).show()
}

fun Context.newScreen(c:Class<*>){
    startActivity(Intent(this,c))
}



