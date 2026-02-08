package com.ell.movieroom.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PlayerTopBar(
    onPickVideo: () -> Unit,
    onExitFullscreen: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(Color.Black.copy(alpha = 0.6f))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = onPickVideo) {
            Text("Pick Video")
        }

        onExitFullscreen?.let {
            Button(onClick = it) {
                Text("Exit")
            }
        }
    }
}

