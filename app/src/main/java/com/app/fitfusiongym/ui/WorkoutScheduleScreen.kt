package com.app.fitfusiongym.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.fitfusiongym.UserViewModel
import com.app.fitfusiongym.database.entities.Workout

/**
 * Composable function for displaying and managing the workout schedule.
 *
 * @param userViewModel ViewModel for managing user data and workout sessions.
 */
@Composable
fun WorkoutScheduleScreen(userViewModel: UserViewModel) {
    // List of days of the week for scheduling
    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri")

    // List of times for scheduling workouts
    val timesOfDay = listOf(
        "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30",
        "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00"
    )

    // State to manage the currently selected workout for deletion
    var selectedWorkout by remember { mutableStateOf<Workout?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    // Observe the list of workouts from the ViewModel
    val workouts by userViewModel.workouts.collectAsState()

    // Display a confirmation dialog for workout deletion
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Confirm Deletion") },
            text = { Text(text = "Are you sure you want to delete this workout?") },
            confirmButton = {
                TextButton(onClick = {
                    selectedWorkout?.let {
                        userViewModel.deleteWorkout(it)
                        selectedWorkout = null
                    }
                    showDialog = false
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("No")
                }
            }
        )
    }

    // Main layout for the workout schedule screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Header with the title and delete button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Workout Schedule",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            IconButton(
                onClick = { showDialog = true },
                enabled = selectedWorkout != null // Enable delete button only if a workout is selected
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = if (selectedWorkout != null) Color.Black else Color.Gray)
            }
        }

        // LazyColumn to display the workout schedule
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Header Row for days of the week
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    Box(
                        modifier = Modifier.width(60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Empty box for spacing in the time column
                    }
                    daysOfWeek.forEach { day ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.White)
                                .border(1.dp, Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = day,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }
                }
            }

            // Rows for time slots and workout sessions
            items(timesOfDay.size) { timeIndex ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Time Column
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(60.dp)
                            .border(1.dp, Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = timesOfDay[timeIndex],
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                    }
                    // Columns for days of the week
                    daysOfWeek.forEach { day ->
                        // Find the workout session for the current day and time
                        val session = workouts.find { it.day.trim() == day.trim() && it.startTime.trim() == timesOfDay[timeIndex].trim() }
                        if (session != null) {
                            Log.d("WorkoutSchedule", "Match found for ${session.title} at $day ${timesOfDay[timeIndex]}, color: ${session.color}")
                        } else {
                            Log.d("WorkoutSchedule", "No match for $day ${timesOfDay[timeIndex]}")
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(60.dp)
                                .background(
                                    session?.color?.let { Color(android.graphics.Color.parseColor(it)) }
                                        ?: Color.White,
                                    RoundedCornerShape(4.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (selectedWorkout == session) Color.Black else Color.LightGray,
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .clickable {
                                    // Select or deselect the workout session
                                    selectedWorkout = if (selectedWorkout == session) null else session
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            session?.let {
                                Text(
                                    text = it.title,
                                    fontSize = 10.sp,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
