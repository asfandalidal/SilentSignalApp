package com.azeemi.silentsignal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.azeemi.silentsignal.presentation.AnnouncementScreen

@Composable
fun SetupNavigation(navController: NavHostController, message: String, timestamp: String) {
    NavHost(navController = navController, startDestination = "announcement_screen") {
        composable("announcement_screen") {
            AnnouncementScreen(
                message = message,
                expiryTime = timestamp
            )
        }
    }
}
