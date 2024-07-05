package com.app.fitfusiongym.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.fitfusiongym.ClassInfo
import com.app.fitfusiongym.UserViewModel
import com.app.fitfusiongym.database.entities.Workout

/**
 * Composable function for the Dashboard Screen.
 *
 * @param onLogout Callback to handle logout action.
 * @param workouts List of workouts for the user.
 */
@Composable
fun DashboardScreen(
    onLogout: () -> Unit,
    workouts: List<Workout>
) {
    val userViewModel: UserViewModel = viewModel()
    val classes by userViewModel.classes.collectAsState()

    // LazyColumn to display the dashboard items
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp) // Decrease the space between items
    ) {
        item {
            // Header for the dashboard
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "FitFusion Gym",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Welcome",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Spacer(modifier = Modifier.height(8.dp))

            // Available Classes header
            Text(
                text = "Available Classes",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.Black,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // LazyRow to display available classes
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                items(classes) { classInfo ->
                    AvailableClassCard(classInfo)
                }
            }
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        item {
            // Workout Schedule header
            Text(
                text = "Workout Schedule",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.Black,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Display each workout in the schedule
        items(workouts) { workout ->
            WorkoutScheduleItem(workout)
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        item {
            // Notifications header
            Text(
                text = "Notifications",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.Black,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Display notifications
        items(sampleNotifications) { notification ->
            NotificationItem(notification)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            // Logout button
            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Logout")
            }
        }
    }
}

/**
 * Composable function to display a card for an available class.
 *
 * @param classInfo Information about the class.
 */
@Composable
fun AvailableClassCard(classInfo: ClassInfo) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .width(150.dp)
            .height(100.dp)
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = classInfo.title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.Black
            )
            Text(
                text = classInfo.schedule,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black.copy(alpha = 0.6f)
            )
        }
    }
}

/**
 * Composable function to display a workout schedule item.
 *
 * @param workout The workout to display.
 */
@Composable
fun WorkoutScheduleItem(workout: Workout) {
    Card(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.LightGray), // Add border to the card
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp), // Decrease vertical padding
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = workout.title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.Black
            )
            Text(
                text = "${workout.startTime} - ${workout.endTime}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
            Text(
                text = workout.day,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
        }
    }
}
