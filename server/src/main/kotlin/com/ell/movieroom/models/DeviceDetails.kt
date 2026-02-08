package com.ell.movieroom.models

import kotlinx.serialization.Serializable

@Serializable
data class DeviceDetails(
    val deviceId: String,
    val deviceName: String,
    val os: String,
    val version: String
)
