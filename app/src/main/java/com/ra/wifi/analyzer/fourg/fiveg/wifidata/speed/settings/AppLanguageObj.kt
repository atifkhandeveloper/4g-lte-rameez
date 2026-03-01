package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.settings

import android.content.Context
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.SharedPrefObj
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.ConstantVariables

class AppLanguageObj:LocalizationActivity() {

    fun checkApp(context: Context){
        fun checkLang(context: Context){
            if (SharedPrefObj.getString(this, ConstantVariables.LANG_KEY)== ConstantVariables.ENGLISH){
                setLanguage("en")
            }else if (SharedPrefObj.getString(this, ConstantVariables.LANG_KEY)== ConstantVariables.HINDI){
                setLanguage("hi")
            }else{
                setLanguage("cop")
            }
        }
    }
}