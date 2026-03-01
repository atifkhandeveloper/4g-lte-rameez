package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils

import android.os.Handler
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.databinding.ActivityNetSpeedBinding
import dev.jahidhasanco.networkusage.NetworkType
import dev.jahidhasanco.networkusage.NetworkUsageManager

class SpeedometerUpdateRunnable(
    private val handler: Handler,
    private val networkUsage: NetworkUsageManager,
    private val binding: ActivityNetSpeedBinding
) : Runnable {

    private enum class State {
        DOWNLOADING, UPLOADING, PAUSED_DOWNLOAD, PAUSED_UPLOAD, RESET
    }

    private var state = State.DOWNLOADING

    override fun run() {
        val now = networkUsage.getUsageNow(NetworkType.ALL)
        val speeds = NetSpeed.calculateSpeed(now.timeTaken, now.downloads, now.uploads)

        // Update UI outside of the try-catch block.
        binding.apply {
            incomingTv.text = "${speeds[1].speed} ${speeds[1].unit}"
            outgoingTv.text = "${speeds[2].speed} ${speeds[2].unit}"
        }

        val speedPercentage: Float = when (state) {
            State.DOWNLOADING, State.UPLOADING -> {
                try {
                    // Convert the String speed value to Float and calculate the percentage
                    val speed = if (state == State.DOWNLOADING) speeds[1].speed else speeds[2].speed
                    val speedFloat = speed.toFloat()
                    speedFloat / MAX_SPEED * 100 // Assuming MAX_SPEED is the max value on the speedometer
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                    0f
                }
            }
            else -> 0f
        }

        // Set the speed on the speedometer
        binding.pointerSpeedometer.setSpeedAt(speedPercentage)

        // Decide what to do next based on the current state
        when (state) {
            State.DOWNLOADING -> {
                state = State.PAUSED_DOWNLOAD
                handler.postDelayed(this, 3000) // Run after 3 seconds
            }
            State.PAUSED_DOWNLOAD -> {
                state = State.UPLOADING
                handler.postDelayed(this, 1000) // Pause for 1 second
            }
            State.UPLOADING -> {
                state = State.PAUSED_UPLOAD
                handler.postDelayed(this, 3000) // Run after 3 seconds
            }
            State.PAUSED_UPLOAD -> {
                state = State.RESET
                handler.postDelayed(this, 1000) // Pause for 1 second
            }
            State.RESET -> {
                state = State.DOWNLOADING
                handler.post(this) // Update instantly to start over
            }
        }
    }

    companion object {
        private const val MAX_SPEED = 100 // Set this to the maximum value your speedometer can display
    }
}
