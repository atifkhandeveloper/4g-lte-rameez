package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope

import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.features.showCustomToast
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.InternetConnection
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.R
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.databinding.ActivityLtedetailsBinding
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.SharedPrefObj
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.ConstantVariables
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class LTEDetailsActivity : LocalizationActivity() {
    lateinit var binding: ActivityLtedetailsBinding
    private val phoneStatePermissionCode = 101
    var settingsBoolean = false
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("Myrehgffs", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLtedetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPhoneStatePermission()
        toolBar()
        selectPhoneIndex()
        poneIndextDetails()
        openNetworkSettings()
        refreshLayout()
        binding.runPingBtn.setOnClickListener {
            lifecycleScope.launch {
                val host = "www.google.com" // Replace with actual host
                val port = 80 // HTTP port
                val timeout = 3000 // Timeout in milliseconds (3 seconds)
                val pingTime = tcpPing(host, port, timeout)
                if (pingTime.toString().isNotEmpty()) {
                    binding.hostFirstTv.text = "Pass"
                } else {
                    binding.hostFirstTv.text = "Fail"
                }
                if (performPingOperation(host).toString().isNotEmpty()) {
                    binding.pingHostSTv.text = "Pass"
                } else {
                    binding.pingHostSTv.text = "Fail"
                }
                binding.datasentTv.text = pingTime.toString()
                binding.datareceivedTv.text = performPingOperation(host).toString()
            }
        }

        binding.resetbtn.setOnClickListener {
            val telephonyManager = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            binding.phoneNumberTv.setOnClickListener { }

            val networkType = telephonyManager.networkType
            val networkTypeString = when (networkType) {
                TelephonyManager.NETWORK_TYPE_GSM -> "GSM"
                TelephonyManager.NETWORK_TYPE_GPRS -> "GPRS"
                TelephonyManager.NETWORK_TYPE_EDGE -> "EDGE"
                TelephonyManager.NETWORK_TYPE_UMTS -> "UMTS"
                TelephonyManager.NETWORK_TYPE_HSDPA -> "HSDPA"
                TelephonyManager.NETWORK_TYPE_HSUPA -> "HSUPA"
                TelephonyManager.NETWORK_TYPE_HSPA -> "HSPA"
                TelephonyManager.NETWORK_TYPE_CDMA -> "CDMA"
                TelephonyManager.NETWORK_TYPE_EVDO_0 -> "EVDO_0"
                TelephonyManager.NETWORK_TYPE_EVDO_A -> "EVDO_A"
                TelephonyManager.NETWORK_TYPE_EVDO_B -> "EVDO_B"
                TelephonyManager.NETWORK_TYPE_1xRTT -> "1xRTT"
                TelephonyManager.NETWORK_TYPE_LTE -> "LTE"
                TelephonyManager.NETWORK_TYPE_IDEN -> "iDEN"
                TelephonyManager.NETWORK_TYPE_TD_SCDMA -> "TD-SCDMA"
                TelephonyManager.NETWORK_TYPE_IWLAN -> "IWLAN"
                TelephonyManager.NETWORK_TYPE_NR -> "5G NR"
                // Other types can be added here
                else -> "Unknown" // Assume "Auto" for unknown or automatically selected types
            }
        }


        darkMode()
    }

    private fun darkMode() {
        val savedMode = sharedPreferences.getInt("MODE", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        when (savedMode) {
            AppCompatDelegate.MODE_NIGHT_YES -> {
                binding.ime.setTextColor(resources.getColor(R.color.white))
                binding.phhoeN.setTextColor(resources.getColor(R.color.white))
                binding.currenNet.setTextColor(resources.getColor(R.color.white))
                binding.currenSb.setTextColor(resources.getColor(R.color.white))
                binding.roaming.setTextColor(resources.getColor(R.color.white))
                binding.dataService.setTextColor(resources.getColor(R.color.white))
                binding.signalStrenth.setTextColor(resources.getColor(R.color.white))
                binding.phhoneN.setTextColor(resources.getColor(R.color.white))
                binding.imme.setTextColor(resources.getColor(R.color.white))
                binding.phhoeN.setTextColor(resources.getColor(R.color.white))
                binding.imegh.setTextColor(resources.getColor(R.color.white))
                binding.phneN.setTextColor(resources.getColor(R.color.white))
                binding.hhoeN.setTextColor(resources.getColor(R.color.white))
                binding.phoneN.setTextColor(resources.getColor(R.color.white))

            }

            AppCompatDelegate.MODE_NIGHT_NO -> {
                binding.ime.setTextColor(resources.getColor(R.color.black))
                binding.phhoeN.setTextColor(resources.getColor(R.color.black))
                binding.currenNet.setTextColor(resources.getColor(R.color.black))
                binding.currenSb.setTextColor(resources.getColor(R.color.black))
                binding.roaming.setTextColor(resources.getColor(R.color.black))
                binding.dataService.setTextColor(resources.getColor(R.color.black))
                binding.signalStrenth.setTextColor(resources.getColor(R.color.black))
                binding.phhoneN.setTextColor(resources.getColor(R.color.black))
                binding.imme.setTextColor(resources.getColor(R.color.black))
                binding.phhoeN.setTextColor(resources.getColor(R.color.black))
                binding.imegh.setTextColor(resources.getColor(R.color.black))
                binding.phneN.setTextColor(resources.getColor(R.color.black))
                binding.hhoeN.setTextColor(resources.getColor(R.color.black))
                binding.phoneN.setTextColor(resources.getColor(R.color.black))

            }

            else -> {
            }
        }

    }

    suspend fun performPingOperation(host: String): String {
        val runtime = Runtime.getRuntime()
        val pingResult = StringBuilder()
        val pingCommand = "/system/bin/ping -c 1 $host"

        try {
            val process = runtime.exec(pingCommand)
            val returnVal = process.waitFor()
            val output = process.inputStream.bufferedReader().use { it.readText() }
            if (returnVal == 0) {
                pingResult.append("Ping successful")
                output.lines().forEach {
                    if (it.contains("bytes from")) {
                        pingResult.append(it.substringBefore("time")).append("")
                    }
                }
            } else {
                pingResult.append("Ping not successful.\n")
            }
        } catch (e: IOException) {
            pingResult.append("Ping failed due to an IO exception: ${e.message}")
        } catch (e: InterruptedException) {
            pingResult.append("Ping was interrupted: ${e.message}")
        }

        return pingResult.toString()
    }


    private fun refreshLayout() {
        binding.swipeRefreshLayout.setColorSchemeColors(
            ContextCompat.getColor(this, android.R.color.holo_orange_dark),
            ContextCompat.getColor(this, android.R.color.holo_green_dark),
            ContextCompat.getColor(this, R.color.redtext),
            ContextCompat.getColor(this, android.R.color.holo_blue_dark)
        )
        binding.swipeRefreshLayout.setOnRefreshListener {
            selectPhoneIndex()
            poneIndextDetails()
            openNetworkSettings()
            getSimDetails()
            getMobileNetworkMode()
            onPhoneStatePermissionGranted()
            val (strength1, strength2) = getNetworkStrength()
            checkSignalStrength(strength1, strength2)
            binding.swipeRefreshLayout.isRefreshing = false

        }
    }

    private fun poneIndextDetails() {
        if (InternetConnection.isInternetAvailable(this)) {
            binding.dataServiceTv.text = resources.getString(R.string.connected)
        } else {
            binding.dataServiceTv.text = resources.getString(R.string.disconnected)
        }
    }

    private fun selectPhoneIndex() {
        binding.selectedPhoneIndexTv.text =
            SharedPrefObj.getString(this, ConstantVariables.PHONE_INDEX)
        binding.selectPhoneIndexBtn.setOnClickListener {
            binding.customPhoneIndexCardViev.visibility = View.VISIBLE
        }
        binding.phone0.setOnClickListener {
            getSimDetails()
            SharedPrefObj.saveString(this, ConstantVariables.PHONE_INDEX, "Phone0")
            binding.selectedPhoneIndexTv.text =
                SharedPrefObj.getString(this, ConstantVariables.PHONE_INDEX)
            binding.customPhoneIndexCardViev.visibility = View.GONE
        }
        binding.phone1.setOnClickListener {
            getSimDetails()
            SharedPrefObj.saveString(this, ConstantVariables.PHONE_INDEX, "Phone1")
            binding.selectedPhoneIndexTv.text =
                SharedPrefObj.getString(this, ConstantVariables.PHONE_INDEX)
            binding.customPhoneIndexCardViev.visibility = View.GONE
        }
    }

    private fun toolBar() {
        binding.toolBar.backBtn.visibility = View.GONE
        binding.toolBar.title.setText(resources.getString(R.string.phoneInfo))
        binding.toolBar.removeAdsbtn.setImageResource(R.drawable.baseline_more)
        binding.imeiTv.setText(getIMEINumber())
    }

    private fun openNetworkSettings() {
        binding.toolBar.removeAdsbtn.setOnClickListener {
            if (!settingsBoolean) {
                binding.toolbarMenu.visibility = View.VISIBLE
                settingsBoolean = true
            } else {
                binding.toolbarMenu.visibility = View.GONE
                settingsBoolean = false
            }
        }
        binding.lteSettingsBtn.setOnClickListener {
            val intent = Intent(Settings.ACTION_DEVICE_INFO_SETTINGS)
            startActivity(intent)

        }
        binding.mobileNetworkSettingsBtn.setOnClickListener {
            val intent = Intent(Settings.ACTION_DEVICE_INFO_SETTINGS)
            startActivity(intent)
            binding.toolbarMenu.visibility = View.GONE
        }
    }

    suspend fun tcpPing(host: String, port: Int, timeout: Int): Long? {
        val socket = Socket()
        return try {
            val startTime = System.currentTimeMillis()

            // Connect with a timeout
            withContext(Dispatchers.IO) {
                socket.connect(InetSocketAddress(host, port), timeout)
            }

            val endTime = System.currentTimeMillis()

            endTime - startTime // Return the round-trip time
        } catch (e: IOException) {
            // If an IOException occurs, print the stack trace and return null
            e.printStackTrace()
            null
        } finally {
            // Make sure to close the socket
            try {
                socket.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun requestPhoneStatePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                phoneStatePermissionCode,
            )
        } else {
            getSimDetails()
            getMobileNetworkMode()
            onPhoneStatePermissionGranted()
            val (strength1, strength2) = getNetworkStrength()
            checkSignalStrength(strength1, strength2)
        }
    }

    private fun checkSignalStrength(strength1: Int, strength2: Int) {
        val d = dbmToPercentage(strength1)
        binding.signalStrenthTv.text = "- ${d.toString()} dBm"
    }

    private fun dbmToPercentage(dbm: Int): Int {
        if (dbm <= -120) return 0 // Poor signal
        if (dbm >= -40) return 100 // Excellent signal
        return ((dbm + 120) * 100) / 80 // Convert dBm to percentage
    }

    private fun onPhoneStatePermissionGranted() {
        // Actions to perform after permission is granted
        binding.imeiTv.text = getIMEINumber()
        binding.phoneNumberTv.text = getPhoneNumber()
        getSimDetails()
        // Any additional actions that require the permission
    }

    fun getPhoneNumber(): String {
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (telephonyManager.isNetworkRoaming) {
            binding.roamingTv.text = resources.getString(R.string.yes)
        } else {
            binding.roamingTv.text = resources.getString(R.string.no)
        }
        return try {
            val phoneNumber = telephonyManager.line1Number
            if (phoneNumber.isNullOrEmpty()) {
                "Unavailable"
            } else {
                phoneNumber
            }
        } catch (e: SecurityException) {
            "Permission Denied"
        }
    }

    fun getIMEINumber(): String {
        return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    }

    private fun getSimDetails() {
        try {
            val subscriptionManager =
                getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager

            // Check for permission
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val (strength1, strength2) = getNetworkStrength()
                checkSignalStrength(strength1, strength2)
                val subscriptionInfoList: List<SubscriptionInfo> =
                    subscriptionManager.activeSubscriptionInfoList!!

                for ((index, subscriptionInfo) in subscriptionInfoList.withIndex()) {
                    val carrierName = subscriptionInfo.carrierName.toString()
                    val displayName = subscriptionInfo.displayName.toString()
                    val countryIso = subscriptionInfo.countryIso
                    val mnc = subscriptionInfo.mnc.toString()
                    val key = SharedPrefObj.getString(this, ConstantVariables.PHONE_INDEX)

                    // Determine SIM slot and set the details accordingly
                    when (index) {
                        0 -> { // SIM one
                            if (key == "Phone0") {
                                binding.phoneNumberTv.text = displayName
                                binding.subidOfDefSimTv.text = carrierName
                            }
                        }

                        1 -> { // SIM two
                            if (key == "Phone1") {
                                binding.phoneNumberTv.text = displayName
                                binding.subidOfDefSimTv.text = carrierName
                            }
                        }

                        else -> {
                            // Handle the possibility of more than two SIM slots if necessary
                        }
                    }
                }
            } else {
                println("Permission not granted")
            }
        } catch (e: Exception) {
        }
    }

    private fun getNetworkStrength(): Pair<Int, Int> {
        var strength1 = -1
        var strength2 = -1

        val manager =
            getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val telephonyManager =
                applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

            // Check if permissions are granted, return default values if not
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return Pair(strength1, strength2)
            }

            if (telephonyManager.allCellInfo != null) {
                // ... (rest of your code)
            }
        }

        Timber.i("final strength   sim1 $strength1  sim2 $strength2")
        return Pair(strength1, strength2)
    }

    fun getMobileNetworkMode() {
        val telephonyManager = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        binding.phoneNumberTv.setOnClickListener { }

        val networkType = telephonyManager.networkType
        val networkTypeString = when (networkType) {
            TelephonyManager.NETWORK_TYPE_GSM -> "GSM"
            TelephonyManager.NETWORK_TYPE_GPRS -> "GPRS"
            TelephonyManager.NETWORK_TYPE_EDGE -> "EDGE"
            TelephonyManager.NETWORK_TYPE_UMTS -> "UMTS"
            TelephonyManager.NETWORK_TYPE_HSDPA -> "HSDPA"
            TelephonyManager.NETWORK_TYPE_HSUPA -> "HSUPA"
            TelephonyManager.NETWORK_TYPE_HSPA -> "HSPA"
            TelephonyManager.NETWORK_TYPE_CDMA -> "CDMA"
            TelephonyManager.NETWORK_TYPE_EVDO_0 -> "EVDO_0"
            TelephonyManager.NETWORK_TYPE_EVDO_A -> "EVDO_A"
            TelephonyManager.NETWORK_TYPE_EVDO_B -> "EVDO_B"
            TelephonyManager.NETWORK_TYPE_1xRTT -> "1xRTT"
            TelephonyManager.NETWORK_TYPE_LTE -> "LTE"
            TelephonyManager.NETWORK_TYPE_IDEN -> "iDEN"
            TelephonyManager.NETWORK_TYPE_TD_SCDMA -> "TD-SCDMA"
            TelephonyManager.NETWORK_TYPE_IWLAN -> "IWLAN"
            TelephonyManager.NETWORK_TYPE_NR -> "5G NR"
            // Other types can be added here
            else -> "Unknown" // Assume "Auto" for unknown or automatically selected types
        }

        // Set the text of the TextView to the network type.
        binding.currenNetworkTv.text = networkTypeString
        return if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        } else {

        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == phoneStatePermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getSimDetails()
                getMobileNetworkMode()
                onPhoneStatePermissionGranted()
                val (strength1, strength2) = getNetworkStrength()
                checkSignalStrength(strength1, strength2)

            } else {
                showCustomToast("Permission Denied")
                // Permission denied. Inform the user and handle the situation
            }
        }
    }

}