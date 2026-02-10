package com.ell.movieroom.player

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ell.movieroom.presentation.devices.DeviceIntent
import com.ell.movieroom.presentation.devices.DeviceState
import com.ell.movieroom.presentation.devices.DeviceViewModel
import kotlinx.coroutines.awaitCancellation


@Composable
fun DeviceScreen(
    viewModel: DeviceViewModel
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(lifecycle) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.onIntent(DeviceIntent.Connect)
            awaitCancellation()
        }
    }

    DeviceContent(state)
}

@Composable
fun DeviceContent(state: DeviceState) {
    when {
        state.isLoading -> {
            CircularProgressIndicator()
        }

        state.error != null -> {
            Text("Error: ${state.error}")
        }

        else -> {
            LazyColumn {
                items(
                    items = state.devices,
                    key = { it.deviceId?:"" }
                ) { device ->
                    Text(
                        text = device.name?:"",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
