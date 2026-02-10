package com.ell.movieroom.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun VideoControls(
    isPlaying: Boolean,
    isLandscape: Boolean,
    onPickVideo: () -> Unit,
    onPlayPause: () -> Unit,
    onSeek: () -> Unit,
    onToggleOrientation: () -> Unit,
    duration: String = "00:50/23:00"
) {
    Column(
        modifier = Modifier
//            .background(Color.Black.copy(alpha = 0.6f))
//            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Pick video
        Button(onClick = onPickVideo) {
            Text("Pick Video")
        }

        // Play / Pause
/*        Button(onClick = onPlayPause) {
            Text(if (isPlaying) "Pause" else "Play")
        }*/

        // Seek
/*        Button(onClick = onSeek) {
            Text("00:20")
        }*/

        // Fullscreen toggle
        Button(onClick = onToggleOrientation) {
            Text(if (isLandscape) "Exit Fullscreen" else "Fullscreen")
        }
    }
}
