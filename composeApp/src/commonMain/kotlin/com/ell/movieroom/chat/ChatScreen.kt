package com.ell.movieroom.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ell.movieroom.player.DeviceScreen
import com.ell.movieroom.player.VideoControls
import com.ell.movieroom.player.VideoStatus
import com.ell.movieroom.presentation.devices.DeviceViewModel
import com.ell.movieroom.utils.toVideoTimeRounded

@Composable
fun ChatScreen(
    devicesViewModel: DeviceViewModel,
    isPlaying: Boolean,
    isLandscape: Boolean,
    onPickVideo: () -> Unit,
    onPlayPause: () -> Unit,
    onSeek: () -> Unit,
    onToggleOrientation: () -> Unit,
    duration: String = "00:50/23:00"
){

    Column() {
        VideoControls(
            isPlaying = isPlaying,
            isLandscape = isLandscape,
            onPickVideo = onPickVideo,
            onPlayPause =onPlayPause,
            onSeek =  onSeek ,
            onToggleOrientation = onToggleOrientation
        )
        VideoStatus(duration = duration)
        DeviceScreen(devicesViewModel)
    }
}