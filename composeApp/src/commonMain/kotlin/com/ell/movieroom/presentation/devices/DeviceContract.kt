package com.ell.movieroom.presentation.devices

import com.ell.movieroom.data.model.DeviceDetails


data class DeviceState(
    val devices: List<DeviceDetails> = emptyList(),
    val isConnected: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface DeviceIntent {
    data object Connect : DeviceIntent
    data object Disconnect : DeviceIntent
    // data class SendUpdate(val device: DeviceDetails) : DeviceIntent // Example
}

sealed interface DeviceEffect {
    data class ShowToast(val message: String) : DeviceEffect
}
