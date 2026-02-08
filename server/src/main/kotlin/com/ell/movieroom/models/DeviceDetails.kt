package com.ell.movieroom.models

import kotlinx.serialization.Serializable

@Serializable
data class DeviceDetails(
    val deviceId: String?=null,
    val name: String?="",
    val duration: Long?=0L,
)
