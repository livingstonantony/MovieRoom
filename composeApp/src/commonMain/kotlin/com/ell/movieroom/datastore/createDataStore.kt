package com.ell.movieroom.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

// This declares a function that every platform (android, ios, etc.) must implement
expect fun createDataStore(context: Any? = null): DataStore<Preferences>
