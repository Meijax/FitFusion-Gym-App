package com.app.fitfusiongym.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.fitfusiongym.database.entities.Workout
import kotlinx.coroutines.flow.Flow

/**
 * DAO for managing workout-related operations in the database.
 */
@Dao
interface WorkoutDao {

    /**
     * Retrieves all workouts for a specific user as a Flow.
     *
     * @param userId The ID of the user to retrieve workouts for.
     * @return A Flow emitting a list of workouts.
     */
    @Query("SELECT * FROM workout WHERE userId = :userId")
    fun getWorkouts(userId: Int): Flow<List<Workout>>

    /**
     * Inserts a new workout. Replaces existing workout if there's a conflict.
     *
     * @param workout The workout entity to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workout: Workout)

    /**
     * Deletes a workout from the database.
     *
     * @param workout The workout entity to delete.
     */
    @Delete
    suspend fun delete(workout: Workout)

    /**
     * Checks if a workout with the same details already exists for a specific user.
     *
     * @param userId The ID of the user.
     * @param title The title of the workout.
     * @param day The day of the workout.
     * @param startTime The start time of the workout.
     * @param endTime The end time of the workout.
     * @return The count of workouts with the specified details.
     */
    @Query("SELECT COUNT(*) FROM workout WHERE userId = :userId AND title = :title AND day = :day AND startTime = :startTime AND endTime = :endTime")
    suspend fun workoutExists(userId: Int, title: String, day: String, startTime: String, endTime: String): Int
}
