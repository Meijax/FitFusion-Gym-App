package com.app.fitfusiongym.ui

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.fitfusiongym.UserViewModel
import com.app.fitfusiongym.database.entities.User
import kotlinx.coroutines.launch

/**
 * Composable function for the signup screen where users can create a new account.
 *
 * @param userViewModel ViewModel for managing user data.
 * @param onSignupSuccess Callback function to be called upon successful signup.
 * @param onLoginClick Callback function to navigate to the login screen.
 */
@Composable
fun SignupScreen(userViewModel: UserViewModel = viewModel(), onSignupSuccess: () -> Unit, onLoginClick: () -> Unit) {
    // State variables to hold user input values
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    // Coroutine scope for asynchronous operations
    val coroutineScope = rememberCoroutineScope()

    // SnackbarHostState for displaying snackbars
    val snackbarHostState = remember { SnackbarHostState() }

    // Main container for the signup screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title of the screen
            Text(
                text = "Signup",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Subtitle of the screen
            Text(
                text = "Create your account",
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Username input field
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username *") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Black,
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedContainerColor = Color.White,
                    focusedLabelColor = Color.LightGray
                )
            )

            // Password input field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password *") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Black,
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedContainerColor = Color.White,
                    focusedLabelColor = Color.LightGray
                )
            )

            // Name input field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Black,
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedContainerColor = Color.White,
                    focusedLabelColor = Color.LightGray
                )
            )

            // Surname input field
            OutlinedTextField(
                value = surname,
                onValueChange = { surname = it },
                label = { Text("Surname") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Black,
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedContainerColor = Color.White,
                    focusedLabelColor = Color.LightGray
                )
            )

            // Email input field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email *") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Black,
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedContainerColor = Color.White,
                    focusedLabelColor = Color.LightGray
                )
            )

            // Signup button
            Button(
                onClick = {
                    // Validate input fields
                    if (username.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()) {
                        coroutineScope.launch {
                            // Check if username is taken
                            if (userViewModel.isUsernameTaken(username)) {
                                snackbarHostState.showSnackbar("Username is already taken")
                            } else {
                                // Create new user
                                val user = User(
                                    username = username,
                                    password = password,
                                    name = name,
                                    surname = surname,
                                    email = email,
                                    height = 0f, // Default value
                                    weight = 0f, // Default value
                                    weightGoal = 0f, // Default value
                                    likes = emptyList(), // Default value
                                    profileImageUri = Uri.EMPTY
                                )
                                // Insert user into the database
                                userViewModel.insertUser(user)
                                // Notify success and call callback
                                onSignupSuccess()
                            }
                        }
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Username, Password, and Email are required")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(text = "Signup", color = Color.White)
            }

            // Navigate to login screen
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ClickableText(
                    text = AnnotatedString("Already have an account? Login"),
                    onClick = { onLoginClick() },
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Blue)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Terms of Service and Privacy Policy disclaimer
            Text(
                text = "By clicking continue, you agree to our Terms of Service and Privacy Policy",
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }

        // Snackbar for displaying messages
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
