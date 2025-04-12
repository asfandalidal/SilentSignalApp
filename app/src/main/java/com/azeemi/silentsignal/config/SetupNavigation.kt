package com.azeemi.silentsignal.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.azeemi.silentsignal.presentation.screen.AnnouncementScreen

@Composable
fun SetupNavigation(
    navController: NavHostController,
    messageState: State<String>,
    timestampState: State<String>
) {
    NavHost(navController = navController, startDestination = "announcement_screen") {
        composable("announcement_screen") {
            AnnouncementScreen(
                messageState = messageState,
                expiryTimeState = timestampState
            )
        }
    }
}
