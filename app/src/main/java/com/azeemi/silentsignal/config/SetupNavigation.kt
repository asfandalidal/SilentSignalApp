package com.azeemi.silentsignal.navigation

import androidx.compose.runtime.*
import androidx.navigation.*
import androidx.navigation.compose.*
import com.azeemi.silentsignal.presentation.AnnouncementDetailScreen
import com.azeemi.silentsignal.presentation.AnnouncementListScreen
import com.azeemi.silentsignal.presentation.MainViewModel
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun SetupNavigation(navController: NavHostController, message: String, timestamp: String) {
    LaunchedEffect(message, timestamp) {
        if (message.isNotEmpty() && timestamp.isNotEmpty()) {
            val encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8.name())
            val encodedTimestamp = URLEncoder.encode(timestamp, StandardCharsets.UTF_8.name())
            navController.navigate("announcement_detail/$encodedMessage/$encodedTimestamp") {
                popUpTo("announcement_list") { inclusive = false }
                launchSingleTop = true
            }
        }
    }

    NavHost(navController, startDestination = "announcement_list") {
        composable("announcement_list") {
            AnnouncementListScreen(navController = navController, viewModel = MainViewModel())
        }
        composable(
            "announcement_detail/{message}/{timestamp}",
            arguments = listOf(
                navArgument("message") { type = NavType.StringType },
                navArgument("timestamp") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val msg = runCatching {
                URLDecoder.decode(
                    backStackEntry.arguments?.getString("message") ?: "",
                    StandardCharsets.UTF_8.name()
                )
            }.getOrElse { "Invalid message" }

            val time = runCatching {
                URLDecoder.decode(
                    backStackEntry.arguments?.getString("timestamp") ?: "",
                    StandardCharsets.UTF_8.name()
                )
            }.getOrElse { "Unknown time" }

            AnnouncementDetailScreen(msg, time)
        }
    }
}
