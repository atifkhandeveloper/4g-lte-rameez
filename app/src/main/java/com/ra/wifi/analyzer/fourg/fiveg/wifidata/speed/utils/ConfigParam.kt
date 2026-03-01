package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils

enum class ConfigParam(val key: String) {
    JSON_ADS_BOOLEAN("ads"),

    //Open
    APP_OPEN("app_open"),

    //Interstitials
    INTER_HOME("inter_home"),
    INTER_SPLASH("inter_splash"),
    INTER_SETTINGS("inter_settings"),

    //Natives
    NATIVE_LANGUAGE("native_LANGUAGE"),
    BANNER_HOME("banner_HOME"),
    NATIVE_HOME("native_HOME"),
    BANNER_LTE_SETTINGS("banner_LTE_SETTINGS"),
    NATIVE_LTE_SETTINGS("native_LTE_SETTINGS"),
    NATIVE_SETTINGS("native_SETTINGS"),
    BANNER_NET_SPEED("banner_NET_SPEED"),
    NATIVE_NET_SPEED("native_NET_SPEED"),
    BANNER_SIGNAL_STRENGTH("banner_SIGNAL_STRENGTH"),
    BANNER_SETTINGS("banner_SETTINGS"),
    BANNER_SIM_INFO("banner_SIM_INFO"),
    NATIVE_SIM_INFO("native_SIM_INFO"),
    NATIVE_SECOND_TIME("native_SECOND_TIME"),
    NATIVE_SIGNAL_STRENGTH("native_SIGNAL_STRENGTH"),
    NATIVE_EXIT("native_EXIT"),
    NATIVE_ANIMATION("native_ANIMATION"),
    NATIVE_BOARDING("native_BOARDING"),
    NATIVE_HOW_TO_USE("native_HOW_TO_USE"),
    NATIVE_SPEED_TEST("native_SPEED_TEST"),
    BANNER_DATA_USAGE("banner_DATA_USAGE"),
    NATIVE_DATA_USAGE("native_DATA_USAGE")
    ;

}