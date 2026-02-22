package com.ell.movieroom.ui.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.ell.movieroom.data.model.DeviceDetails
import com.ell.movieroom.presentation.devices.DeviceIntent
import com.ell.movieroom.presentation.devices.DeviceState
import com.ell.movieroom.presentation.devices.DeviceViewModel
import com.ell.movieroom.utils.getRoomNumber
import kotlinx.coroutines.awaitCancellation


@Composable
fun DeviceScreen(
    viewModel: DeviceViewModel
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val state by viewModel.state.collectAsStateWithLifecycle()

    var isAllVideosDurationSync by rememberSaveable { mutableStateOf(false) }


    LaunchedEffect(lifecycle) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.onIntent(DeviceIntent.Connect)
            awaitCancellation()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        /*    Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    val allDevices = state.devices
                    isAllVideosDurationSync =
                        allDevices.all { it.duration == allDevices.firstOrNull()?.duration }
                }) {
                    Text("Verify")
                }
                Text(if (isAllVideosDurationSync) "Verified" else "Not Verified")

            }*/
        DeviceContent(state)
    }
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
            if (state.devices.isEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(16.dp),
                    text = "No devices found",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            } else {
                LazyColumn {
                    itemsIndexed(
                        items = state.devices,
                        key = { _, device -> device.deviceId ?: "" }
                    ) { index, device ->

                        Devices(device, state.devices.firstOrNull()?.duration)

                    }
                }
            }

        }
    }
}

@Composable
fun Devices(device: DeviceDetails, duration: Long?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${device.name.orEmpty()} : ${device.duration ?: 0}",
            modifier = Modifier.padding(16.dp)
        )
        Icon(
            imageVector =
                if (device.duration == duration)
                    Icons.Default.Check
                else
                    Icons.Default.Close,

            tint =
                if (device.duration == duration)
                    Color(0xFF139A13)
                else
                    Color.Red,

            contentDescription = null
        )

    }
}
