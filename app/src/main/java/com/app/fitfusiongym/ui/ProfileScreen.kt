package com.app.fitfusiongym.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.app.fitfusiongym.R
import com.app.fitfusiongym.UserViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

/**
 * Composable function for displaying and editing user profile information.
 *
 * @param userViewModel ViewModel to manage user data.
 */
@Composable
fun ProfileScreen(userViewModel: UserViewModel = viewModel()) {
    val user by userViewModel.user.collectAsState()

    // State variables for user profile fields
    var name by remember { mutableStateOf(user?.name ?: "") }
    var surname by remember { mutableStateOf(user?.surname ?: "") }
    var username by remember { mutableStateOf(user?.username ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var password by remember { mutableStateOf(user?.password ?: "") }
    var height by remember { mutableStateOf(user?.height?.toString() ?: "") }
    var weight by remember { mutableStateOf(user?.weight?.toString() ?: "") }
    var weightGoal by remember { mutableStateOf(user?.weightGoal?.toString() ?: "") }
    var likes by remember { mutableStateOf(user?.likes ?: emptyList()) }
    var profileImageUri by remember { mutableStateOf(user?.profileImageUri ?: Uri.EMPTY) }

    val originalUsername = user?.username ?: ""

    // Launch effect to initialize state when user data changes
    LaunchedEffect(user) {
        user?.let {
            name = it.name
            surname = it.surname
            username = it.username
            email = it.email
            password = it.password
            height = it.height.toString()
            weight = it.weight.toString()
            weightGoal = it.weightGoal.toString()
            likes = it.likes
            profileImageUri = it.profileImageUri ?: Uri.EMPTY
        }
    }

    // Launcher for picking an image from the device
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            profileImageUri = it
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 16.dp)
        ) {
            item {
                // Profile image section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (profileImageUri != Uri.EMPTY) {
                            // Display selected profile image
                            Image(
                                painter = rememberImagePainter(profileImageUri),
                                contentDescription = "Profile Image",
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .clickable { imagePickerLauncher.launch("image/*") },
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            // Display default profile image
                            Image(
                                painter = painterResource(id = R.drawable.ic_default_profile),
                                contentDescription = "Default Profile Image",
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .clickable { imagePickerLauncher.launch("image/*") },
                                contentScale = ContentScale.Crop
                            )
                        }
                        // Edit profile image text
                        Text(
                            text = "Edit profile image",
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .clickable { imagePickerLauncher.launch("image/*") }
                        )
                    }
                }
            }

            // Profile fields
            item { ProfileField(label = "Name", value = name, onValueChange = { name = it }) }
            item { ProfileField(label = "Surname", value = surname, onValueChange = { surname = it }) }
            item { ProfileField(label = "Username", value = username, onValueChange = { username = it }) }
            item { ProfileField(label = "Email", value = email, onValueChange = { email = it }) }
            item { ProfileField(label = "Password", value = password, onValueChange = { password = it }, isPassword = true) }
            item { ProfileField(label = "Height (cm)", value = height, onValueChange = { height = it }) }
            item { ProfileField(label = "Weight (Kg)", value = weight, onValueChange = { weight = it }) }
            item { ProfileField(label = "Weight Goal (Kg)", value = weightGoal, onValueChange = { weightGoal = it }) }

            // Likes section
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Likes",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                    Column(modifier = Modifier.weight(2f)) {
                        // Display each like
                        likes.forEach { like ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = like,
                                    fontSize = 14.sp,
                                    color = Color.Black,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(onClick = { likes = likes - like }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Remove Like", tint = Color.Red)
                                }
                            }
                        }

                        // Add new like
                        var newLike by remember { mutableStateOf("") }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            BasicTextField(
                                value = newLike,
                                onValueChange = { newLike = it },
                                modifier = Modifier
                                    .weight(1f)
                                    .background(Color.White)
                                    .padding(8.dp),
                                singleLine = true,
                                textStyle = LocalTextStyle.current.copy(color = Color.Black)
                            )
                            IconButton(
                                onClick = {
                                    if (newLike.isNotBlank()) {
                                        likes = likes + newLike.trim()
                                        newLike = ""
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Add Like", tint = Color.Green)
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                // Save changes button
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(onClick = {
                        coroutineScope.launch {
                            // Check if username is already taken
                            if (originalUsername != username && userViewModel.isUsernameTaken(username)) {
                                snackbarHostState.showSnackbar("Username already exists")
                            } else {
                                // Update user profile
                                val updatedUser = user?.copy(
                                    name = name,
                                    surname = surname,
                                    username = username,
                                    email = email,
                                    password = password,
                                    height = height.toFloatOrNull() ?: 0f,
                                    weight = weight.toFloatOrNull() ?: 0f,
                                    weightGoal = weightGoal.toFloatOrNull() ?: 0f,
                                    likes = likes,
                                    profileImageUri = profileImageUri.takeIf { it != Uri.EMPTY }
                                )
                                if (updatedUser != null) {
                                    userViewModel.updateUser(updatedUser)
                                    snackbarHostState.showSnackbar("Profile updated successfully")
                                }
                            }
                        }
                    }) {
                        Text("Save Changes")
                    }
                }
            }
        }

        // Snackbar for showing messages
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}

/**
 * Composable function for displaying a profile field.
 *
 * @param label Label for the profile field.
 * @param value Current value of the field.
 * @param onValueChange Callback to update the value.
 * @param isPassword Indicates if the field is for password input.
 */
@Composable
fun ProfileField(label: String, value: String, onValueChange: (String) -> Unit, isPassword: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
        Box(
            modifier = Modifier.weight(2f)
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(8.dp),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(color = Color.Black),
                visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
            )
        }
        Icon(Icons.Default.ArrowForward, contentDescription = "Edit", tint = Color.Gray)
    }
}
