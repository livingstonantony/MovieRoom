package com.ell.movieroom.data

import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import androidx.core.net.toUri

data class MetaData(
    val fileName: String
)

interface MetaDataReader {
    fun getMetaDataFromUri(contentUri: Uri): MetaData?
}
class MetaDataReaderImpl(
    private val app: Application
) : MetaDataReader {

    override fun getMetaDataFromUri(contentUri: Uri): MetaData? {
        if (contentUri.scheme != ContentResolver.SCHEME_CONTENT) {
            return null
        }

        val fileName = app.contentResolver
            .query(
                contentUri,
                arrayOf(MediaStore.MediaColumns.DISPLAY_NAME),
                null,
                null,
                null
            )
            ?.use { cursor ->
                if (!cursor.moveToFirst()) return null

                val index =
                    cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)

                cursor.getString(index)
            }

        return fileName?.let {
            MetaData(fileName = it)
        }
    }
}
