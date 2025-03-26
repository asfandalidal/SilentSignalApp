package com.azeemi.silentsignal

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

@Composable
fun AnnouncementListScreen(navController: NavController, title: String?, body: String?) {
    val announcements = remember { mutableStateListOf<Announcement>() }

    // If app opened from notification, add the announcement
    LaunchedEffect(title, body) {
        if (!title.isNullOrEmpty() && !body.isNullOrEmpty()) {
            announcements.add(Announcement(announcements.size + 1, body, title))
        }
    }

    // Function to add new announcements dynamically
    fun addAnnouncement(message: String, timestamp: String) {
        announcements.add(Announcement(announcements.size + 1, message, timestamp))
    }

    // Update UI dynamically when a new notification is received
    MyFirebaseMessagingService.onNewAnnouncement = { title, body ->
        addAnnouncement(body, title)  // Title acts as the timestamp
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
                        navController.navigate("announcementDetail/${announcement.message}/${announcement.timestamp}")
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
            .background(Color(0xFF323248))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF323248))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = announcement.message,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = announcement.timestamp,
                fontSize = 14.sp,
                color = Color(0xFFB0B0D9)
            )
        }
    }
}
