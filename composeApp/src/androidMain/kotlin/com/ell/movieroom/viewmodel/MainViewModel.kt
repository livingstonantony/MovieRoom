package com.ell.movieroom.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.ell.movieroom.data.MetaDataReader
import com.ell.movieroom.data.VideoItem
import com.ell.movieroom.utils.toVideoTime
import com.ell.movieroom.utils.toVideoTimeRounded
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    private val savedStateHandle: SavedStateHandle,
    val player: Player,
    private val metaDataReader: MetaDataReader
) : ViewModel() {

    val TAG = javaClass.simpleName

    private val videoUri =
        savedStateHandle.getStateFlow<Uri?>("videoUri", null)
    private val _durationMs = MutableStateFlow(0L)
    val durationMs = _durationMs.asStateFlow()


    private val _currentPositionMs = MutableStateFlow(0L)
    val currentPosition = _currentPositionMs.asStateFlow()
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()


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

        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_READY) {
                    _durationMs.value = player.duration
                }
                _currentPositionMs.value = player.currentPosition
                Log.d(
                    TAG,
                    "onPlaybackStateChanged: $state, position=${player.currentPosition.toVideoTimeRounded()} ms"
                )
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                _isPlaying.value = isPlaying

                _currentPositionMs.value = player.currentPosition

                Log.d(
                    TAG,
                    "onIsPlayingChanged: $isPlaying, position=${player.currentPosition.toVideoTimeRounded()} ms"
                )
            }


            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ) {
                _currentPositionMs.value = player.currentPosition

                Log.d(
                    TAG,
                    "onPositionDiscontinuity:Seeked to: ${newPosition.positionMs.toVideoTimeRounded()} ms"
                )
            }

        })
    }

    fun setVideo(uri: Uri) {
        savedStateHandle["videoUri"] = uri

        player.setMediaItem(MediaItem.fromUri(uri))
        player.prepare()
    }

    fun play() {
        player.play()
    }

    fun seekTo(duration: Long = 20) {
        player.seekTo(duration * 1000L)
    }

    fun pause() {
        player.pause()
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}
