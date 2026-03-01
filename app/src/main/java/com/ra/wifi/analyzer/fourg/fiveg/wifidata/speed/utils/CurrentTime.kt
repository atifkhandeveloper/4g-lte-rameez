package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils

import java.text.SimpleDateFormat
import java.util.*

object CurrentTime {
    private val sdf = SimpleDateFormat("hh:mm a dd/MMM/yyyy", Locale.getDefault())
    private val time=SimpleDateFormat("hh:mm a",Locale.getDefault())
    private val dateMonth=SimpleDateFormat("dd MMM",Locale.getDefault())
    val currentDateAndTime = sdf.format(Date())
    val currentTime= time.format(Date())
    val currentDate= dateMonth.format(Date())

}