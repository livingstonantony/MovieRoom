package com.ell.movieroom.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.annotation.OptIn
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.ell.movieroom.data.MetaDataReader
import com.ell.movieroom.data.VideoItem
import com.ell.movieroom.utils.toVideoTime
import com.ell.movieroom.utils.toVideoTimeRounded
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
    val durationMs: StateFlow<Long> = _durationMs.asStateFlow()


    private val _currentPositionMs = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPositionMs.asStateFlow()
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    val _fileName = MutableStateFlow("")
    val fileName: StateFlow<String> = _fileName.asStateFlow()


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

            override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                if (!timeline.isEmpty) {
                    val duration = player.duration
                    if (duration != C.TIME_UNSET) {
                        _durationMs.value = duration
                    }
                }

                Log.d(TAG, "duration=${player.duration}, state=${player.playbackState}")
            }

            override fun onPlaybackStateChanged(state: Int) {

                _currentPositionMs.value = player.currentPosition
                Log.d(
                    TAG,
                    "onPlaybackStateChanged: $state, position=${player.currentPosition.toVideoTimeRounded()} ms"
                )
                if (state == Player.STATE_READY) {
                    val duration = player.duration
                    Log.d("Exo", "Duration = $duration")
                }
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

        _fileName.value = metaDataReader
            .getMetaDataFromUri(uri)
            ?.fileName ?: "NoName"
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
