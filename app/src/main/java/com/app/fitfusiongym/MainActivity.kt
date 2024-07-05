package com.app.fitfusiongym

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.fitfusiongym.ui.*
import com.app.fitfusiongym.ui.theme.FitFusionGymTheme
import com.app.fitfusiongym.database.entities.Workout

/**
 * MainActivity is the entry point of the app, managing the app's lifecycle and setting up the content view.
 */
class MainActivity : ComponentActivity() {
    // ViewModel for managing user-related operations
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitFusionGymTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController() // Navigation controller for managing app navigation
                    var isLoggedIn by remember { mutableStateOf(false) } // State to manage login status
                    var showSignup by remember { mutableStateOf(false) } // State to manage signup screen visibility

                    if (isLoggedIn) {
                        val userId = userViewModel.user.collectAsState().value?.id ?: 0
                        // Load user workouts when user ID changes
                        LaunchedEffect(userId) {
                            userViewModel.loadWorkouts(userId)
                        }
                        val workouts by userViewModel.workouts.collectAsState() // Observe workouts from ViewModel

                        // Show main content if logged in
                        MainContent(
                            navController = navController,
                            userViewModel = userViewModel,
                            userId = userId,
                            workouts = workouts,
                            onLogout = {
                                // Handle logout
                                userViewModel.clearUser()
                                userViewModel.resetLoginAttempt()
                                isLoggedIn = false
                            }
                        )
                    } else {
                        // Show signup or login screen based on state
                        if (showSignup) {
                            SignupScreen(
                                userViewModel,
                                onSignupSuccess = {
                                    showSignup = false // Hide signup screen on successful signup
                                },
                                onLoginClick = {
                                    showSignup = false // Switch to login screen
                                }
                            )
                        } else {
                            LoginScreen(
                                userViewModel,
                                onLoginSuccess = {
                                    isLoggedIn = true // Update state to show main content on successful login
                                },
                                onSignupClick = {
                                    showSignup = true // Switch to signup screen
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * MainContent displays the main navigation and content of the app after user login.
 */
@Composable
fun MainContent(
    navController: NavHostController,
    userViewModel: UserViewModel,
    userId: Int,
    workouts: List<Workout>,
    onLogout: () -> Unit
) {
    Scaffold(
        bottomBar = { NavigationBar(navController) } // Add bottom navigation bar
    ) { innerPadding ->
        // Navigation host to manage different screens
        NavHost(
            navController = navController,
            startDestination = "dashboard", // Start with the dashboard screen
            modifier = Modifier.padding(innerPadding) // Adjust padding for the scaffold
        ) {
            composable("dashboard") { DashboardScreen(onLogout, workouts) }
            composable("workout_schedule") { WorkoutScheduleScreen(userViewModel) }
            composable("appointments") { AppointmentsScreen(userViewModel, userId) }
            composable("notifications") { NotificationsScreen() }
            composable("profile") { ProfileScreen(userViewModel) }
        }
    }
}

/**
 * NavigationBar provides a bottom navigation bar for switching between different app sections.
 */
@Composable
fun NavigationBar(navController: NavHostController) {
    NavigationBar(
        modifier = Modifier.height(56.dp), // Set height of the navigation bar
        containerColor = Color.White, // Background color of the navigation bar
        contentColor = Color.Black // Color of the navigation icons
    ) {
        // Navigation bar items for different sections
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard", tint = Color.Black) },
            selected = navController.currentBackStackEntry?.destination?.route == "dashboard",
            onClick = {
                navController.navigate("dashboard") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.DateRange, contentDescription = "Workout Schedule", tint = Color.Black) },
            selected = navController.currentBackStackEntry?.destination?.route == "workout_schedule",
            onClick = {
                navController.navigate("workout_schedule") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Add, contentDescription = "Appointments", tint = Color.Black) },
            selected = navController.currentBackStackEntry?.destination?.route == "appointments",
            onClick = {
                navController.navigate("appointments") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.Black) },
            selected = navController.currentBackStackEntry?.destination?.route == "notifications",
            onClick = {
                navController.navigate("notifications") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.Black) },
            selected = navController.currentBackStackEntry?.destination?.route == "profile",
            onClick = {
                navController.navigate("profile") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}
