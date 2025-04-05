package com.azeemi.silentsignal.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.azeemi.silentsignal.domain.model.Announcement
import com.azeemi.silentsignal.config.MyFirebaseMessagingService
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlin.math.exp

@Composable
fun AnnouncementListScreen(navController: NavController, viewModel: MainViewModel) {
    val announcements = remember { mutableStateListOf<Announcement>() }

    val message by viewModel.message
    val expireAt by viewModel.expireAt

    // Update the list when message and timestamp are received
    LaunchedEffect(message, expireAt) {
        if (message.isNotEmpty() && expireAt.isNotEmpty()) {
            announcements.add(Announcement(System.currentTimeMillis().toInt(), message, expireAt))
        }
    }

    fun addAnnouncement(message: String, expireAt: String) {
        announcements.add(Announcement(System.currentTimeMillis().toInt(), message, expireAt))
    }

    // Listen for new announcements via FCM
    LaunchedEffect(Unit) {
        MyFirebaseMessagingService.onNewAnnouncement = { _, body, expireAt ->
            viewModel.updateMessage(body,expireAt)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E2D))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (announcements.isEmpty()) {
            Text(
                text = "No Announcements Yet!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        } else {
            LazyColumn {
                items(announcements) { announcement ->
                    AnnouncementCard(announcement) {
                        val encodedMessage = URLEncoder.encode(announcement.message, StandardCharsets.UTF_8.name())
                        val encodedTime = URLEncoder.encode(announcement.expiresAt, StandardCharsets.UTF_8.name())
                        navController.navigate("announcement_detail/$encodedMessage/$encodedTime")
                    }
                }
            }
        }
    }
}


@Composable
fun AnnouncementCard(announcement: Announcement, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF323248))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = announcement.message,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = announcement.expiresAt,
                fontSize = 14.sp,
                color = Color(0xFFB0B0D9)
            )
        }
    }
}
