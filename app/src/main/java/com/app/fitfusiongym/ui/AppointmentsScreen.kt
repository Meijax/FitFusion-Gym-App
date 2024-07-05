package com.app.fitfusiongym.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.fitfusiongym.R
import com.app.fitfusiongym.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Data class representing information about a fitness class.
 *
 * @property imageRes Resource ID of the class image.
 * @property title The title of the class.
 * @property schedule The schedule of the class (day and time).
 * @property trainer The name of the class trainer.
 * @property description A brief description of the class.
 */
data class ClassInfo(
    val imageRes: Int,
    val title: String,
    val schedule: String,
    val trainer: String,
    val description: String
)

/**
 * Composable function displaying the Appointments Screen.
 *
 * @param userViewModel The ViewModel handling user-related operations.
 * @param userId The ID of the current user.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppointmentsScreen(userViewModel: UserViewModel, userId: Int) {
    // Sample list of fitness classes
    val classes = listOf(
        ClassInfo(R.drawable.yoga, "Yoga", "Mon : 11:00 - 12:00", "Edward", "Yoga is a practice that connects the body, breath, and mind. It uses physical postures, breathing exercises, and meditation to improve overall health."),
        ClassInfo(R.drawable.pilates, "Pilates", "Tue : 09:00 - 10:00", "Alice", "Pilates is a method of exercise that consists of low-impact flexibility and muscular strength and endurance movements."),
        ClassInfo(R.drawable.zumba, "Zumba", "Wed : 17:00 - 18:00", "John", "Zumba is a fitness program that combines Latin and international music with dance moves."),
        ClassInfo(R.drawable.cycling, "Cycling", "Fri : 08:00 - 09:00", "Michael", "Cycling classes are high-intensity workouts on stationary bikes, combining cardiovascular training with strength training."),
        ClassInfo(R.drawable.boxing, "Boxing", "Thu : 10:00 - 11:00", "Sara", "Boxing workouts are high-intensity workouts that combine strength training and cardio exercise.")
    )

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Scaffold layout for the screen with a snackbar host
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            // Header text
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Appointments Bookings",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            // Horizontal list of class cards
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(classes) { classInfo ->
                    ClassCard(classInfo, userViewModel, userId, snackbarHostState, coroutineScope)
                }
            }
        }
    }
}

/**
 * Composable function displaying a card for a fitness class.
 *
 * @param classInfo Information about the class.
 * @param userViewModel The ViewModel handling user-related operations.
 * @param userId The ID of the current user.
 * @param snackbarHostState State for displaying snackbars.
 * @param coroutineScope Coroutine scope for launching coroutines.
 */
@Composable
fun ClassCard(
    classInfo: ClassInfo,
    userViewModel: UserViewModel,
    userId: Int,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp)
            .clickable { /* Handle class click */ }
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            // Class image
            Image(
                painter = painterResource(id = classInfo.imageRes),
                contentDescription = classInfo.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Class title
            Text(
                text = classInfo.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            // Class schedule
            Text(
                text = classInfo.schedule,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // Class trainer
            Text(
                text = "Trainer : ${classInfo.trainer}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Spacer to push the content up and center it between Trainer and Join Button
            Spacer(modifier = Modifier.weight(1f))

            // Class description
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 56.dp) // Ensure space for the button
            ) {
                Text(
                    text = classInfo.description,
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .align(Alignment.CenterHorizontally) // Center horizontally
                )
            }

            // Join Class button
            Button(
                onClick = {
                    val (day, time) = classInfo.schedule.split(": ")
                    val (startTime, endTime) = time.split(" - ")

                    // Attempt to join the class and show a snackbar with the result
                    userViewModel.joinClass(userId, classInfo.title, day, startTime, endTime) { success ->
                        coroutineScope.launch {
                            val message = if (success) {
                                "Class added successfully"
                            } else {
                                "You already joined this Class"
                            }
                            snackbarHostState.showSnackbar(message)
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = "Join Class")
            }
        }
    }
}
