package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed

import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.ConfigParam
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import org.json.JSONException
import org.json.JSONObject

fun FirebaseRemoteConfig.isAdEnable(param: ConfigParam): Boolean {
    val key: String = this.getString(ConfigParam.JSON_ADS_BOOLEAN.key)
    return try {
        val jsonObject = JSONObject(key)
        return jsonObject.getBoolean(param.key)
    } catch (e: JSONException) {
        true
    }
}
