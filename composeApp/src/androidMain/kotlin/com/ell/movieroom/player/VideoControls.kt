package com.ell.movieroom.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VideoControls(
    isPlaying: Boolean,
    onPlayPause: () -> Unit,
    onSeek: () -> Unit,
    onToggleOrientation: () -> Unit,
    isLandscape: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = onPlayPause) {
            Text(if (isPlaying) "Pause" else "Play")
        }

        Button(onClick = onSeek) {
            Text("00:20")
        }

        Button(onClick = onToggleOrientation) {
            Text(if (isLandscape) "Portrait" else "Fullscreen")
        }
    }
}
