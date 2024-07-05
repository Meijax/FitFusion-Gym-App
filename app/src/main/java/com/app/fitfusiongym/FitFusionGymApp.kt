package com.app.fitfusiongym

import android.app.Application
import com.app.fitfusiongym.database.AppDatabase

/**
 * Custom Application class for initializing the app's components.
 */
class FitFusionGymApp : Application() {

    // Lazy initialization of the AppDatabase instance.
    // The database is created only when it's accessed for the first time.
    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }
}
