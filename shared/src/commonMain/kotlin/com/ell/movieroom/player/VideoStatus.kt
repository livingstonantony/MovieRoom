package com.ell.movieroom.player

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun VideoStatus(duration: String = "00:50/23:00") {

    Column(
        modifier = Modifier

    ) {

        Text(text = duration)
    }
}