package com.azeemi.silentsignal.presentation

import android.app.Activity
import android.view.WindowManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun AnnouncementScreen(message: String, expiryTime: String) {

    var showCard by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val localDateTime = try {
        LocalDateTime.parse(expiryTime, formatter)
    } catch (e: Exception) {
        LocalDateTime.now()
    }

    val localTime = localDateTime.atZone(ZoneId.of("Asia/Karachi")).toLocalDateTime()
    val formattedTime = DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault()).format(localTime)

    LaunchedEffect(Unit) {
        if (context is Activity) {
            context.window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        if(message.isEmpty() && expiryTime.isEmpty())
        {
            Text("No Announcement yet!")
        }
        else {
            if (showCard) {
                AnnouncementCard(
                    message = message,
                    expiryTime = formattedTime,
                    onClick = { showCard = false }
                )
            }
        }
    }
}
