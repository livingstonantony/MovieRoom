package com.ell.movieroom.data.model

import kotlinx.serialization.Serializable
@Serializable
data class DeviceDetails(
    val deviceId: String? = null,
    val name: String? = "",
    val duration: Long? = 0L,
)

@Serializable
data class DeviceSocketResponse(
    val devices: List<DeviceDetails>
)
