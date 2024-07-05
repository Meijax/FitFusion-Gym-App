package com.app.fitfusiongym.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.app.fitfusiongym.database.entities.User

/**
 * DAO for managing user-related operations in the database.
 */
@Dao
interface UserDao {

    /**
     * Inserts a new user. Replaces existing user if there's a conflict.
     *
     * @param user The user entity to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    /**
     * Retrieves a user by username and password.
     *
     * @param username The username to search for.
     * @param password The password to search for.
     * @return The user entity if found, otherwise null.
     */
    @Query("SELECT * FROM user WHERE username = :username AND password = :password")
    suspend fun getUser(username: String, password: String): User?

    /**
     * Checks if a username is already taken.
     *
     * @param username The username to check.
     * @return The number of users with the specified username.
     */
    @Query("SELECT COUNT(*) FROM user WHERE username = :username")
    suspend fun isUsernameTaken(username: String): Int

    /**
     * Retrieves a user by their ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return The user entity if found, otherwise null.
     */
    @Query("SELECT * FROM user WHERE id = :userId")
    suspend fun getUserById(userId: Int): User?

    /**
     * Updates an existing user.
     *
     * @param user The user entity with updated information.
     */
    @Update
    suspend fun updateUser(user: User)
}
