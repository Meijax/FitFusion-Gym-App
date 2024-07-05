package com.app.fitfusiongym.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Composable function for displaying the Notifications Screen.
 */
@Composable
fun NotificationsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize() // Fill the available space
            .padding(16.dp) // Padding around the entire screen
            .background(Color.White) // Background color for the screen
    ) {
        // Title for the Notifications screen
        Text(
            text = "Notifications",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp) // Padding below the title
        )

        // LazyColumn to display a list of notifications
        LazyColumn(
            modifier = Modifier.fillMaxSize() // Fill the available space
        ) {
            items(sampleNotifications) { notification ->
                NotificationItem(notification) // Display each notification item
            }
        }
    }
}

/**
 * Composable function for displaying an individual notification item.
 *
 * @param notification The notification data to be displayed.
 */
@Composable
fun NotificationItem(notification: Notification) {
    Card(
        shape = RoundedCornerShape(8.dp), // Rounded corners for the card
        modifier = Modifier
            .fillMaxWidth() // Card should fill the width of the parent
            .padding(vertical = 8.dp), // Vertical padding between items
        colors = CardDefaults.cardColors(containerColor = Color.LightGray) // Card background color
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp) // Padding inside the card
        ) {
            // Notification title
            Text(
                text = notification.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp) // Padding below the title
            )
            // Notification description
            Text(
                text = notification.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp) // Padding below the description
            )
            // Notification timestamp
            Text(
                text = notification.timestamp,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black.copy(alpha = 0.6f) // Lighter color for the timestamp
            )
        }
    }
}
