package com.azeemi.silentsignal

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.azeemi.silentsignal.ui.theme.SilentSignalTheme
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {
    private var message: String = ""
    private var timestamp: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Extract data if app is opened from notification
        handleIntent(intent)

        setContent {
            SilentSignalTheme {
                val navController = rememberNavController()
                SetupNavigation(navController, message, timestamp)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent) // ✅ Call the superclass implementation

        intent?.let {
            handleIntent(it) // ✅ Update message and timestamp

            // ✅ **Update UI dynamically**
            setContent {
                SilentSignalTheme {
                    val navController = rememberNavController()
                    SetupNavigation(navController, message, timestamp)
                }
            }
        }
    }

    private fun handleIntent(intent: Intent) {
        message = intent.getStringExtra("message") ?: ""
        timestamp = intent.getStringExtra("timestamp") ?: ""
    }
}

@Composable
fun SetupNavigation(navController: NavHostController, message: String, timestamp: String) {
    val startDestination =
        if (message.isNotEmpty()) "announcement_detail/${URLEncoder.encode(message, StandardCharsets.UTF_8.name())}/${URLEncoder.encode(timestamp, StandardCharsets.UTF_8.name())}"
        else "announcement_list"

    NavHost(navController, startDestination = startDestination) {
        composable("announcement_list") {
            AnnouncementListScreen(navController, title = "", body = "")
        }
        composable("announcement_detail/{message}/{timestamp}") { backStackEntry ->
            val msg = backStackEntry.arguments?.getString("message")?.let {
                URLDecoder.decode(it, StandardCharsets.UTF_8.name())
            } ?: ""
            val time = backStackEntry.arguments?.getString("timestamp")?.let {
                URLDecoder.decode(it, StandardCharsets.UTF_8.name())
            } ?: ""

            AnnouncementDetailScreen(msg, time)
        }
    }
}
