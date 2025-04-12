package com.azeemi.silentsignal.presentation.screen

import android.app.Activity
import android.view.WindowManager
import androidx.compose.foundation.layout.*
import java.time.ZonedDateTime
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.azeemi.silentsignal.presentation.components.AnnouncementCard
import com.azeemi.silentsignal.presentation.components.MessageCard
import com.azeemi.silentsignal.presentation.components.WaitingPlaceholder
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun AnnouncementScreen(
    messageState: State<String>,
    expiryTimeState: State<String>
) {
    val message = messageState.value
    val expiryTime = expiryTimeState.value
    val context = LocalContext.current
    var showCard by remember { mutableStateOf(true) }
    var showMessage by remember { mutableStateOf(false) }
    var currentTime by remember { mutableStateOf(LocalDateTime.now()) }

    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val parsedExpiryTime = try {
        ZonedDateTime.of(
            LocalDateTime.parse(expiryTime, inputFormatter),
            ZoneId.of("Asia/Karachi")
        )
    } catch (e: Exception) {
        ZonedDateTime.now(ZoneId.of("Asia/Karachi")).plusYears(100)
    }


    val formattedTime =
        DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault()).format(parsedExpiryTime)

    // Reset UI when new message arrives
    LaunchedEffect(message + expiryTime) {
        showCard = true
        showMessage = false
    }

    // Update current time every second
    LaunchedEffect(Unit) {
        if (context is Activity) {
            context.window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }
        while (true) {
            delay(1000)
            val now = ZonedDateTime.now(ZoneId.of("Asia/Karachi"))
            currentTime = now.toLocalDateTime()

            if (!showMessage && message.isNotEmpty() &&
                (now.isAfter(parsedExpiryTime) || now.isEqual(parsedExpiryTime))
            ) {
                showCard = false
                showMessage = true
            }
        }
    }

    // UI
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when {
            message.isEmpty() && expiryTime.isEmpty() -> {
                MessageCard(visible = true, message = "No Announcement yet!")
            }

            showCard -> {
                AnnouncementCard(
                    message = message,
                    expiryTime = formattedTime,
                    onClick = { showCard = false })
            }

            showMessage && message.isNotEmpty() -> {
                MessageCard(visible = true, message = "No new Update Available!")
            }

            else -> {
                WaitingPlaceholder()
            }
        }
    }
}
