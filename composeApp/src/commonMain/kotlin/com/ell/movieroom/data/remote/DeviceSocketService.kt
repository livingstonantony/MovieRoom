package com.ell.movieroom.data.remote


import com.ell.movieroom.data.model.DeviceSocketResponse
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.url
import io.ktor.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.Json

class DeviceSocketService(private val client: HttpClient) {
    fun observeDevices(): Flow<DeviceSocketResponse> = channelFlow {
        client.webSocket(
            request = {
                url("ws://192.168.31.238:8080/devices")
            }
        ) {
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    send(
                        Json.decodeFromString<DeviceSocketResponse>(
                            frame.readText()
                        )
                    )
                }
            }
        }
    }

}

