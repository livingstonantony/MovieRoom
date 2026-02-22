package com.ell.movieroom.ui.player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun VideoStatus(duration: String, fileName: String) {

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        if (fileName.isNotEmpty()) {
            Text(
                text = fileName, modifier = Modifier.fillMaxWidth()
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        Text(text = duration)
    }
}