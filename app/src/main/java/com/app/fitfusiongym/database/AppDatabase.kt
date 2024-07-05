package com.app.fitfusiongym.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.fitfusiongym.database.dao.UserDao
import com.app.fitfusiongym.database.dao.WorkoutDao
import com.app.fitfusiongym.database.entities.User
import com.app.fitfusiongym.database.entities.Workout

/**
 * The main database class for the FitFusionGym application.
 * This class defines the database configuration and serves as the main access point to the persisted data.
 *
 * @property userDao Provides access to user-related database operations.
 * @property workoutDao Provides access to workout-related database operations.
 */
@Database(entities = [User::class, Workout::class], version = 9, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Returns an instance of UserDao for accessing user-related database operations.
     */
    abstract fun userDao(): UserDao

    /**
     * Returns an instance of WorkoutDao for accessing workout-related database operations.
     */
    abstract fun workoutDao(): WorkoutDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Returns the singleton instance of the database.
         * If the instance is null, it creates the database.
         *
         * @param context The application context.
         * @return The singleton instance of AppDatabase.
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fitfusiongym_database"
                ).fallbackToDestructiveMigration() // Recreates the database if no migration strategy is provided
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
