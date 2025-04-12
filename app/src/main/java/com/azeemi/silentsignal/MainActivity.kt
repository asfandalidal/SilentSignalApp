package com.azeemi.silentsignal

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.azeemi.silentsignal.navigation.SetupNavigation
import com.azeemi.silentsignal.ui.theme.SilentSignalTheme
import com.azeemi.silentsignal.config.MyFirebaseMessagingService
import com.azeemi.silentsignal.config.subscribeFirebaseNotification
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        subscribeFirebaseNotification()

        val body = intent?.getStringExtra(MyFirebaseMessagingService.EXTRA_MESSAGE).orEmpty()
        val expireAt = intent?.getStringExtra(MyFirebaseMessagingService.EXTRA_EXPIREAT).orEmpty()

        val liveAnnouncementMessage = mutableStateOf(body)
        val liveAnnouncementExpireAt = mutableStateOf(expireAt)

        MyFirebaseMessagingService.onNewAnnouncement = { _, newMessage, newExpireAt ->
            liveAnnouncementMessage.value = newMessage
            liveAnnouncementExpireAt.value = newExpireAt
        }

        setContent {
            SilentSignalTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    SetupNavigation(
                        navController = navController,
                        messageState = liveAnnouncementMessage,
                        timestampState = liveAnnouncementExpireAt
                    )
                }
            }
        }
    }
}
