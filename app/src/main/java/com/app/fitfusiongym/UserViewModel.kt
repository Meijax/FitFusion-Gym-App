package com.app.fitfusiongym

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.app.fitfusiongym.database.AppDatabase
import com.app.fitfusiongym.database.entities.User
import com.app.fitfusiongym.database.entities.Workout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * Data class representing class information including image, title, schedule, trainer, and description.
 */
data class ClassInfo(
    val imageRes: Int, // Resource ID for the class image
    val title: String, // Title of the class
    val schedule: String, // Class schedule (day and time)
    val trainer: String, // Name of the trainer
    val description: String // Description of the class
)

/**
 * ViewModel for managing user and workout data.
 */
class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = AppDatabase.getDatabase(application).userDao() // DAO for user operations
    private val workoutDao = AppDatabase.getDatabase(application).workoutDao() // DAO for workout operations

    // StateFlow for managing user data
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    // StateFlow for managing all workouts
    private val _workouts = MutableStateFlow<List<Workout>>(emptyList())
    val workouts: StateFlow<List<Workout>> = _workouts.asStateFlow()

    // StateFlow for managing class information
    private val _classes = MutableStateFlow<List<ClassInfo>>(listOf(
        ClassInfo(R.drawable.yoga, "Yoga", "Mon : 11:00 - 12:00", "Edward", "Yoga is a practice that connects the body, breath, and mind. It uses physical postures, breathing exercises, and meditation to improve overall health."),
        ClassInfo(R.drawable.pilates, "Pilates", "Tue : 09:00 - 10:00", "Alice", "Pilates is a method of exercise that consists of low-impact flexibility and muscular strength and endurance movements."),
        ClassInfo(R.drawable.zumba, "Zumba", "Wed : 17:00 - 18:00", "John", "Zumba is a fitness program that combines Latin and international music with dance moves."),
        ClassInfo(R.drawable.cycling, "Cycling", "Fri : 08:00 - 09:00", "Michael", "Cycling classes are high-intensity workouts on stationary bikes, combining cardiovascular training with strength training."),
        ClassInfo(R.drawable.boxing, "Boxing", "Thu : 10:00 - 11:00", "Sara", "Boxing workouts are high-intensity workouts that combine strength training and cardio exercise.")
    ))
    val classes: StateFlow<List<ClassInfo>> = _classes.asStateFlow()

    // Flag to indicate if login was attempted
    var loginAttempted = false

    /**
     * Inserts a new user into the database.
     * @param user The user to be inserted.
     */
    fun insertUser(user: User) {
        viewModelScope.launch {
            userDao.insert(user)
        }
    }

    /**
     * Fetches a user from the database based on username and password.
     * @param username The username of the user.
     * @param password The password of the user.
     */
    fun fetchUser(username: String, password: String) {
        viewModelScope.launch {
            _user.value = userDao.getUser(username, password)
        }
    }

    /**
     * Updates user information in the database.
     * @param user The user with updated information.
     */
    fun updateUser(user: User) {
        viewModelScope.launch {
            userDao.updateUser(user)
            _user.value = user // Update the local user value to ensure UI updates
        }
    }

    /**
     * Clears the current user from the state.
     */
    fun clearUser() {
        _user.value = null
    }

    /**
     * Resets the login attempt flag.
     */
    fun resetLoginAttempt() {
        loginAttempted = false
    }

    /**
     * Checks if a username is already taken.
     * @param username The username to check.
     * @return True if the username is taken, false otherwise.
     */
    suspend fun isUsernameTaken(username: String): Boolean {
        return userDao.isUsernameTaken(username) > 0
    }

    /**
     * Allows a user to join a class by adding a workout entry.
     * @param userId The ID of the user joining the class.
     * @param title The title of the class.
     * @param day The day of the class.
     * @param startTime The start time of the class.
     * @param endTime The end time of the class.
     * @param callback Callback function to handle success or failure.
     */
    fun joinClass(userId: Int, title: String, day: String, startTime: String, endTime: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val exists = workoutDao.workoutExists(userId, title, day, startTime, endTime)
            if (exists > 0) {
                callback(false) // Class already exists
            } else {
                val color = generateRandomColor() // Generate a random color for the workout
                val workout = Workout(
                    userId = userId,
                    title = title,
                    day = day,
                    startTime = startTime,
                    endTime = endTime,
                    color = color // Assign the generated color
                )
                workoutDao.insert(workout)
                callback(true) // Successfully added the class
                loadWorkouts(userId) // Reload workouts to include the new one
            }
        }
    }

    /**
     * Loads all workouts for a given user.
     * @param userId The ID of the user.
     */
    fun loadWorkouts(userId: Int) {
        viewModelScope.launch {
            workoutDao.getWorkouts(userId).collect { workouts ->
                _workouts.value = workouts
            }
        }
    }

    /**
     * Deletes a workout entry from the database.
     * @param workout The workout to be deleted.
     */
    fun deleteWorkout(workout: Workout) {
        viewModelScope.launch {
            workoutDao.delete(workout)
            loadWorkouts(workout.userId) // Reload workouts after deletion
        }
    }

    /**
     * Generates a random color in hex format.
     * @return A hex string representing the color.
     */
    private fun generateRandomColor(): String {
        val random = Random(System.currentTimeMillis())
        return String.format("#%06X", random.nextInt(0xFFFFFF + 1))
    }
}
