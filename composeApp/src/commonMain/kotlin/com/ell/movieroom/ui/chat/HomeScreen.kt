package com.ell.movieroom.ui.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.ell.movieroom.data.model.DeviceDetails
import com.ell.movieroom.ui.player.AddDeviceScreen
import com.ell.movieroom.ui.player.DeviceScreen
import com.ell.movieroom.ui.player.VideoControls
import com.ell.movieroom.ui.player.VideoStatus
import com.ell.movieroom.presentation.devices.DeviceViewModel

@Composable
fun HomeScreen(
    devicesViewModel: DeviceViewModel,
    isPlaying: Boolean,
    isLandscape: Boolean,
    onPickVideo: () -> Unit,
    onPlayPause: () -> Unit,
    onSeek: () -> Unit,
    onToggleOrientation: () -> Unit,
    duration: String = "00:50/23:00",
    durationInMilliSeconds: Long
){

    Column() {
        VideoControls(
            isPlaying = isPlaying,
            isLandscape = isLandscape,
            onPickVideo = onPickVideo,
            onPlayPause =onPlayPause,
            onSeek =  onSeek ,
            onToggleOrientation = onToggleOrientation,
            durationInMilliSeconds = durationInMilliSeconds
        )
        VideoStatus(duration = duration)
        AddDeviceScreen(){deviceName,roomName ->
            println("DeviceName: $deviceName")
            devicesViewModel.addDevice(
                DeviceDetails(
                    name = deviceName,
                    duration = 4000
                )
            )
        }
        DeviceScreen(devicesViewModel)

    }
}