package com.ell.movieroom.ui.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ell.movieroom.data.model.DeviceDetails
import com.ell.movieroom.ui.player.DeviceScreen
import com.ell.movieroom.ui.player.VideoControls
import com.ell.movieroom.ui.player.VideoStatus
import com.ell.movieroom.presentation.devices.DeviceViewModel
import com.ell.movieroom.ui.player.JoinRoomScreenDialogDisplay
import com.ell.movieroom.utils.isEnabled

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
) {

    Column() {
        VideoControls(
            isPlaying = isPlaying,
            isLandscape = isLandscape,
            onPickVideo = onPickVideo,
            onPlayPause = onPlayPause,
            onSeek = onSeek,
            onToggleOrientation = onToggleOrientation,
            durationInMilliSeconds = durationInMilliSeconds
        )
        VideoStatus(duration = duration)
        JoinRoomScreenDialogDisplay(
            modifier = Modifier,
            enabled = true,
            defaultRoomNumber = 0
        ) { deviceName, roomName ->
            println("DeviceName: $deviceName")
            devicesViewModel.addDevice(
                DeviceDetails(
                    name = deviceName,
                    duration = 4000
                )
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {

            },
            enabled = durationInMilliSeconds.isEnabled()
        ) {
            Text("JOIN")
        }
        DeviceScreen(devicesViewModel)

    }
}