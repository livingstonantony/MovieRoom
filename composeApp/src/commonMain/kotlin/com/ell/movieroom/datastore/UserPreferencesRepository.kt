package com.ell.movieroom.datastore

// In composeApp/src/commonMain/kotlin/com/ell/movieroom/datastore/UserPreferencesRepository.kt

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {

    /**
     * A flow that emits the currently saved URI string whenever it changes.
     * If no URI is stored, it emits a null value.
     */
    val savedUriFlow: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[UserPreferencesKeys.SAVED_URI]
        }

    /**
     * Saves a URI string to DataStore.
     * @param uri The string representation of the URI to save.
     */
    suspend fun saveUri(uri: String) {
        dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.SAVED_URI] = uri
        }
    }

    /**
     * Clears the saved URI from DataStore.
     */
    suspend fun clearUri() {
        dataStore.edit { preferences ->
            preferences.remove(UserPreferencesKeys.SAVED_URI)
        }
    }
}
