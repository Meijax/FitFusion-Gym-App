package com.app.fitfusiongym.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.fitfusiongym.R
import com.app.fitfusiongym.UserViewModel
import kotlinx.coroutines.launch

/**
 * Composable function for the Login Screen.
 *
 * @param userViewModel ViewModel for handling user login.
 * @param onLoginSuccess Callback invoked when login is successful.
 * @param onSignupClick Callback invoked when the user clicks on Sign Up.
 */
@Composable
fun LoginScreen(userViewModel: UserViewModel, onLoginSuccess: () -> Unit, onSignupClick: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val user by userViewModel.user.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var loginAttempted by remember { mutableStateOf(false) }

    // Show a snackbar if login is attempted and the user is null
    LaunchedEffect(user) {
        if (loginAttempted) {
            if (user != null) {
                onLoginSuccess() // Trigger success callback
            } else {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Invalid username or password") // Show error message
                }
            }
            loginAttempted = false // Reset the login attempt flag
        }
    }

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
            // App logo
            Image(
                painter = painterResource(id = R.drawable.logo), // Replace with your logo resource
                contentDescription = "Logo",
                modifier = Modifier
                    .height(150.dp)
                    .padding(bottom = 24.dp),
                contentScale = ContentScale.Fit
            )

            // Login title
            Text(
                text = "Login",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Subtitle for login instructions
            Text(
                text = "Enter your username and password",
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Username input field
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Black, // Focused indicator color
                    unfocusedIndicatorColor = Color.LightGray, // Unfocused indicator color
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
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                visualTransformation = PasswordVisualTransformation(), // Hide password characters
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Black, // Focused indicator color
                    unfocusedIndicatorColor = Color.LightGray, // Unfocused indicator color
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedContainerColor = Color.White,
                    focusedLabelColor = Color.LightGray
                )
            )

            // Login button
            Button(
                onClick = {
                    coroutineScope.launch {
                        userViewModel.loginAttempted = true
                        userViewModel.fetchUser(username, password)
                        loginAttempted = true // Set the login attempt flag
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(text = "Login", color = Color.White)
            }

            // Row with clickable text for forgot password and sign up
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ClickableText(
                    text = AnnotatedString("Forgot Password?"),
                    onClick = { /* Handle forgot password click */ },
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Blue)
                )

                ClickableText(
                    text = AnnotatedString("Sign Up"),
                    onClick = { onSignupClick() }, // Trigger sign up callback
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Blue)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer text with terms and conditions notice
            Text(
                text = "By clicking continue, you agree to our Terms of Service and Privacy Policy",
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }

        // Snackbar for displaying error messages
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
