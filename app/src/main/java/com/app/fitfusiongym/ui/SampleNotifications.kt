package com.app.fitfusiongym.ui

data class Notification(
    val title: String,
    val description: String,
    val timestamp: String
)

val sampleNotifications = listOf(
    Notification("Workout Reminder", "Don't forget your workout at 5 PM today!", "10 minutes ago"),
    Notification("New Message", "You have a new message from your coach.", "1 hour ago"),
    Notification("Achievement Unlocked", "Congratulations! You have completed 10 workouts!", "2 days ago"),
    Notification("Membership Renewal", "Your gym membership is due for renewal.", "1 week ago")
)
