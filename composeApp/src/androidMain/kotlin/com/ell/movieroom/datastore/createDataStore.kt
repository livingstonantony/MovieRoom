package com.ell.movieroom.datastore

// In composeApp/src/androidMain/kotlin/com/ell/movieroom/datastore/createDataStore.kt

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.ell.movieroom.DATA_STORE_FILE_NAME
import java.io.File

// This is the actual implementation for the Android platform.
actual fun createDataStore(context: Any?): DataStore<Preferences> {
    val androidContext = context as? Context
        ?: throw IllegalStateException("Android context is required.")
    return PreferenceDataStoreFactory.create(
        produceFile = {
            File(androidContext.filesDir, DATA_STORE_FILE_NAME)
        }
    )
}
