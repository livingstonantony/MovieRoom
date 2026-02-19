package com.ell.movieroom.models

import kotlinx.serialization.Serializable

@Serializable
data class DeviceDetails(
    var deviceId: String? = null,
    val name: String? = "",
    val duration: Long? = 0L,
    val roomName: String? = null,
)
@Serializable
data class DevicesResponse(
    val status: String,
    val devices: List<DeviceDetails>
)
