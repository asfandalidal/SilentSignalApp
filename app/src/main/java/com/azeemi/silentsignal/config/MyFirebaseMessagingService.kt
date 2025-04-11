package com.azeemi.silentsignal.config

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.azeemi.silentsignal.MainActivity
import com.azeemi.silentsignal.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        const val EXTRA_TITLE = "title"
        const val EXTRA_MESSAGE = "body"
        const val EXTRA_EXPIREAT = "expireAt"

        var onNewAnnouncement: ((String, String,String) -> Unit)? = null
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token generated: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            val data = remoteMessage.data
            val title = data[EXTRA_TITLE] ?: "New Announcement"
            val body = data[EXTRA_MESSAGE] ?: ""
            val expireAt = data[EXTRA_EXPIREAT]?:""
            Log.d("FCM", "Received announcement: title=$title, body=$body,expireAt=$expireAt")

            showNotification(title, body,expireAt)

            onNewAnnouncement?.invoke(title, body, expireAt)
        } else {
            Log.w("FCM", "Received message with empty data")
        }
    }

    private fun showNotification(title: String, body: String,expireAt:String) {
        val channelId = "announcements_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Announcements",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for announcement notifications"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra(EXTRA_TITLE, title)
            putExtra(EXTRA_MESSAGE, body)
            putExtra(EXTRA_EXPIREAT,expireAt)
        }

        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.alarm)
            .setContentTitle("New Announcement")
            .setContentText("Tap to view announcement")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI)
            .build()

        val notificationId = System.currentTimeMillis().toInt()
        notificationManager.notify(notificationId, notification)
    }
}
