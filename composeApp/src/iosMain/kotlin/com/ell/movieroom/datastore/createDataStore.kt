package com.ell.movieroom.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

/**
 * This is the actual implementation for creating a DataStore on the iOS platform.
 * It does not require a context parameter.
 */
@OptIn(ExperimentalForeignApi::class)
actual fun createDataStore(context: Any?): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = {
            // 1. Get the path to the user's documents directory.
            val documentDirectory = NSSearchPathForDirectoriesInDomains(
                NSDocumentDirectory,
                NSUserDomainMask,
                true
            ).first() as String

            // 2. Append the common file name to create the full path.
            //    The 'dataStoreFileName' is the const val from commonMain.
            val fullPath = "$documentDirectory/$dataStoreFileName"

            // 3. The factory function expects a path, which we provide.
            fullPath.toPath()
        }
    )
}
