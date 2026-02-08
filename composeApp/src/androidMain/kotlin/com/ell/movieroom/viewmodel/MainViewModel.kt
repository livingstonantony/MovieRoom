package com.ell.movieroom.viewmodel

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.ell.movieroom.data.MetaDataReader
import com.ell.movieroom.data.VideoItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
class MainViewModel(
    private val savedStateHandle: SavedStateHandle,
    val player: Player,
    private val metaDataReader: MetaDataReader
) : ViewModel() {

    private val videoUri =
        savedStateHandle.getStateFlow<Uri?>("videoUri", null)

    val videoItem = videoUri
        .map { uri ->
            uri?.let {
                VideoItem(
                    contentUri = it,
                    mediaItem = MediaItem.fromUri(it),
                    name = metaDataReader
                        .getMetaDataFromUri(it)
                        ?.fileName ?: "NoName"
                )
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            null
        )

    init {
        player.prepare()
    }

    fun setVideo(uri: Uri) {
        savedStateHandle["videoUri"] = uri

        player.setMediaItem(MediaItem.fromUri(uri))
        player.prepare()
    }

    fun play() {
        player.play()
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}
