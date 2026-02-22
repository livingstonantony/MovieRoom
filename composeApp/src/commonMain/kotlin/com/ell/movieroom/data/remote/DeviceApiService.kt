package com.ell.movieroom.data.remote

import com.ell.movieroom.data.model.DeviceDetails
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class DeviceApiService(private val client: HttpClient) {

    val URL = "http://192.168.31.238:8080/devices"
    suspend fun addDevice(device: DeviceDetails) {
        client.post(URL) {
            contentType(ContentType.Application.Json)
            setBody(device)
        }
    }
    suspend fun deleteDevice(device: DeviceDetails) {
        client.delete ("$URL/${device.deviceId}") {
            contentType(ContentType.Application.Json)
            setBody(device)
        }
    }
}
