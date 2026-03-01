package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils

import android.content.Context
import android.content.SharedPreferences
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.R


object SharedPrefObj {
    const val USER_TOKEN = "uskjkljljer_token"
    fun saveAuthToken(context: Context, token: String) {
        saveString(context, USER_TOKEN, token)
    }
    fun getToken(context: Context): String? {
        return getString(context, USER_TOKEN)
    }
    fun saveString(context: Context, key: String, value: String) {
        val prefs: SharedPreferences =
            context.getSharedPreferences("dshfjasjkjsd", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }
    fun removeString(context: Context, key: String) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove(key)
        editor.apply()
    }
    fun getString(context: Context, key: String): String? {
        val prefs: SharedPreferences =
            context.getSharedPreferences("dshfjasjkjsd", Context.MODE_PRIVATE)
        return prefs.getString(key, null)
    }
    fun clearData(context: Context){
        val editor = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE).edit()
        editor.clear()
        editor.apply()
    }
    fun saveInt(context: Context, key: String, value: Int) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun removeInt(context: Context, key: String) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove(key)
        editor.apply()
    }

    fun getInt(context: Context, key: String, defaultValue: Int): Int {
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        return prefs.getInt(key, defaultValue)
    }
}