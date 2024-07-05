package com.app.fitfusiongym.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Entity class representing a workout in the database.
 * Each workout is associated with a user.
 *
 * @property id The unique identifier for the workout. Auto-generated by the database.
 * @property userId The ID of the user who created the workout. This is a foreign key referencing the User entity.
 * @property title The title or name of the workout.
 * @property day The day of the week the workout is scheduled for (e.g., "Monday").
 * @property startTime The start time of the workout (in a suitable time format, e.g., "08:00").
 * @property endTime The end time of the workout (in a suitable time format, e.g., "09:00").
 * @property color The color associated with the workout, represented as a hex string (e.g., "#FF5733").
 */
@Entity(
    tableName = "workout",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val title: String,
    val day: String,
    val startTime: String,
    val endTime: String,
    val color: String
)
