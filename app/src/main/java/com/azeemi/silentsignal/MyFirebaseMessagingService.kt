package com.azeemi.silentsignal

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        var onNewAnnouncement: ((String, String) -> Unit)? = null
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        remoteMessage.notification?.let {
            val title = it.title ?: "New Announcement"
            val body = it.body ?: ""

            // Hide the body in notification tray
            showNotification(title, body)

            // Notify UI dynamically
            onNewAnnouncement?.invoke(title, body)
        }
    }

    private fun showNotification(title: String, body: String) {
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

        val encodedMessage = URLEncoder.encode(body, StandardCharsets.UTF_8.name())
        val encodedTimestamp = URLEncoder.encode(title, StandardCharsets.UTF_8.name())

        // Create an intent to open MainActivity with the announcement details
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("message", encodedMessage)
            putExtra("timestamp", encodedTimestamp)
        }

        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.alarm)
            .setContentTitle(title)
            .setContentText("Tap to view announcement")  // Hides actual body in tray
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI)
            .build()

        notificationManager.notify(0, notification)
    }
}
