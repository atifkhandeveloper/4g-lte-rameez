package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.fcm

import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.splash.SplashActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.R

class FireBaseMessage : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        message.notification?.let {
            createNotification(it.title ?: "", it.body ?: "")
        }
    }

    private fun createNotification(title: String, body: String) {
        val intent = Intent(this, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val splashIntent =
            PendingIntent.getActivity(this, 101, intent, PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(this, "FCM")
        notification.apply {
            setSmallIcon(R.drawable.ic_app_icon)
            setContentTitle(title)
            setContentText(body)
            setPriority(NotificationCompat.PRIORITY_HIGH)
            setContentIntent(splashIntent)
        }
        with(NotificationManagerCompat.from(this)) {
            notify(1, notification.build())
        }
    }
}