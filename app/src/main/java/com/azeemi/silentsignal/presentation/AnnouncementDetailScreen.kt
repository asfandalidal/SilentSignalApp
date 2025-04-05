package com.azeemi.silentsignal.presentation

import android.app.Activity
import android.view.WindowManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

@Composable
fun AnnouncementDetailScreen(message: String, expiryTime: String) {
    val context = LocalContext.current

    // Use a DateTimeFormatter to parse the expiryTime
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    // Try parsing the expiryTime string using the formatter
    val localDateTime = try {
        LocalDateTime.parse(expiryTime, formatter)
    } catch (e: Exception) {
        // Handle parsing error, fallback to current time if parsing fails
        LocalDateTime.now()
    }

    // Convert the parsed LocalDateTime to Pakistan Standard Time (PST)
    val pstTime = localDateTime.atZone(ZoneId.of("Asia/Karachi")).toLocalDateTime()

    // Format the time into 12-hour format (e.g., 4:05 PM)
    val formattedTime = DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault()).format(pstTime)

    LaunchedEffect(Unit) {
        if (context is Activity) {
            context.window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF323248))
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = message,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Expires at: $formattedTime",  // Show the formatted expiry time in 12-hour format
                    fontSize = 14.sp,
                    color = Color(0xFFB0B0D9)
                )
            }
        }
    }
}
