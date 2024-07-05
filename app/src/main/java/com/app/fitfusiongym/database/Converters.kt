package com.app.fitfusiongym.database

import android.net.Uri
import androidx.room.TypeConverter

/**
 * Type converters for Room to handle custom data types.
 * These converters allow Room to store complex data types in the database.
 */
class Converters {

    /**
     * Converts a Uri to a String for database storage.
     *
     * @param uri The Uri to convert.
     * @return The Uri as a String, or null if the Uri is null.
     */
    @TypeConverter
    fun fromUri(uri: Uri?): String? {
        return uri?.toString()
    }

    /**
     * Converts a String back to a Uri.
     *
     * @param uriString The String to convert.
     * @return The Uri represented by the String, or null if the String is null.
     */
    @TypeConverter
    fun toUri(uriString: String?): Uri? {
        return uriString?.let { Uri.parse(it) }
    }

    /**
     * Converts a list of Strings to a single String for database storage.
     * The list elements are joined with a semicolon.
     *
     * @param likes The list of Strings to convert.
     * @return The list as a single String, or null if the list is null.
     */
    @TypeConverter
    fun fromLikesList(likes: List<String>?): String? {
        return likes?.joinToString(";")
    }

    /**
     * Converts a String back to a list of Strings.
     * The String is split by semicolons to form the list elements.
     *
     * @param likesString The String to convert.
     * @return The list of Strings represented by the String, or null if the String is null.
     */
    @TypeConverter
    fun toLikesList(likesString: String?): List<String>? {
        return likesString?.split(";")
    }
}
