package com.azeemi.silentsignal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.azeemi.silentsignal.navigation.SetupNavigation
import com.azeemi.silentsignal.ui.theme.SilentSignalTheme
import com.azeemi.silentsignal.config.MyFirebaseMessagingService
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlin.math.exp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)

        // Extract extras from the intent (from notification tap)
        val body = intent?.getStringExtra(MyFirebaseMessagingService.EXTRA_MESSAGE).orEmpty()
        val expireAt = intent?.getStringExtra(MyFirebaseMessagingService.EXTRA_EXPIREAT).orEmpty()

        setContent {
            SilentSignalTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    SetupNavigation(
                        navController = navController,
                        message = body,
                        timestamp = expireAt
                    )
                }
            }
        }
    }
}
