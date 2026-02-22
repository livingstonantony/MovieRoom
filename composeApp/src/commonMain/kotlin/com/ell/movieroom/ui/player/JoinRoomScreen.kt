package com.ell.movieroom.ui.player

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ell.movieroom.utils.getRoomNumber
import com.ell.movieroom.utils.isEnabled


@Composable
fun JoinRoomScreenDialogDisplay(
    modifier: Modifier,
    enabled: Boolean,
    defaultRoomNumber: Int = 0,
    onAddDevice: (String, String) -> Unit
) {
    var displayDialog by remember { mutableStateOf(false) }
    var roomName by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (roomName.isEmpty()) {
            Button(
                onClick = {
                    displayDialog = true
                },
                enabled = enabled && roomName.isEmpty()
            ) {
                Text("Join Room")
            }
        } else {
            Text(
                text = roomName,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.DarkGray
            )
        }

    }
    if (displayDialog) {
        JoinRoomScreenDialog(
            modifier = modifier,
            enabled = enabled,
            defaultRoomNumber = defaultRoomNumber,
            onAddDevice = { deviceName, roomNameAr ->
                roomName = roomNameAr
                onAddDevice(deviceName, roomNameAr)
            },
            onClose = { displayDialog = false }
        )
    }
}

@Composable
fun JoinRoomScreenDialog(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    defaultRoomNumber: Int = 0,
    onAddDevice: (String, String) -> Unit,
    onClose: () -> Unit
) {
    Dialog(onDismissRequest = {}) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            JoinRoomScreen(
                modifier = modifier,
                enabled = enabled,
                defaultRoomNumber = defaultRoomNumber,
                onAddDevice = onAddDevice,
                onClose = onClose

            )
        }


    }

}

@Composable
fun JoinRoomScreen(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    defaultRoomNumber: Int = 0,
    onClose: () -> Unit,
    onAddDevice: (deviceName: String, roomName: String) -> Unit
) {
    var deviceName by remember { mutableStateOf("") }
    var roomName by remember { mutableStateOf("") }
    var roomNumber by remember { mutableStateOf(defaultRoomNumber) }

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(10.dp))

            // Room number section
            if (roomNumber.isEnabled()) {
                Text(
                    text = roomNumber.toString(),
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(10.dp))
            } else {
                Button(
                    onClick = {
                        roomNumber = getRoomNumber()
                        roomName = roomNumber.toString()
                    }
                ) {
                    Text("Create Room")
                }
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = roomName,
                onValueChange = { roomName = it },
                label = { Text("Room Name") },
                singleLine = true,
                enabled = enabled,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Device / nickname input
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = deviceName,
                onValueChange = { deviceName = it },
                label = { Text("Nick Name") },
                singleLine = true,
                enabled = enabled,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Done
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Join button
            Button(
                enabled = roomNumber.isEnabled()
                        && deviceName.isNotBlank()
                        && roomName.isNotBlank(),
                onClick = {
                    onClose()

                    onAddDevice(
                        deviceName.trim(),
                        roomName.trim()
                    )

                    // Clear inputs after join
                    deviceName = ""
                    roomName = ""
                    roomNumber = defaultRoomNumber

                }
            ) {
                Text("Join")
            }

            Spacer(Modifier.height(10.dp))

        }

        IconButton(
            onClick = {
                onClose()
            },
            modifier = Modifier.align(Alignment.TopEnd),
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close"
            )
        }
    }
}

/**
 * Helper extension
 */

@Preview(showBackground = true)
@Composable
fun JoinRoomScreenPreview_0() {

    MaterialTheme {
        JoinRoomScreen(onAddDevice = { deviceName, roomName ->
            println("DeviceName: $deviceName")
        }, defaultRoomNumber = 0, onClose = {})
    }
}

@Preview(showBackground = true)
@Composable
fun JoinRoomScreenPreview_12() {

    MaterialTheme {
        JoinRoomScreen(onAddDevice = { deviceName, roomName ->
            println("DeviceName: $deviceName")
        }, defaultRoomNumber = 123456, onClose = {})
    }
}
