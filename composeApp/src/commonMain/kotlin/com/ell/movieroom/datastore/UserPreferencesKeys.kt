package com.ell.movieroom.datastore

// In composeApp/src/commonMain/kotlin/com/ell/movieroom/datastore/UserPreferencesKeys.kt

import androidx.datastore.preferences.core.stringPreferencesKey

object UserPreferencesKeys {
    // Define a key for storing the URI as a String
    val SAVED_URI = stringPreferencesKey("saved_uri")
}
