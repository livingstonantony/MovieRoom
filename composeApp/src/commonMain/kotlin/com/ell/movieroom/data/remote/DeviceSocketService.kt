package com.ell.movieroom.data.remote


import com.ell.movieroom.data.model.ChatSocketResponse
import com.ell.movieroom.data.model.CommandSocketResponse
import com.ell.movieroom.data.model.DeviceSocketResponse
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.url
import io.ktor.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
class DeviceSocketService(private val client: HttpClient) {

    private val json = Json { ignoreUnknownKeys = true }

    private var deviceSession: WebSocketSession? = null
    private var chatSession: WebSocketSession? = null
    private var commandSession: WebSocketSession? = null

    fun observeDevices(): Flow<DeviceSocketResponse> =
        observeSocket(
            path = "/devices",
            serializer = DeviceSocketResponse.serializer(),
            onSessionChange = { deviceSession = it }
        )

    fun observeChat(): Flow<ChatSocketResponse> =
        observeSocket(
            path = "/chat",
            serializer = ChatSocketResponse.serializer(),
            onSessionChange = { chatSession = it }
        )

    fun observeCommands(): Flow<CommandSocketResponse> =
        observeSocket(
            path = "/command",
            serializer = CommandSocketResponse.serializer(),
            onSessionChange = { commandSession = it }
        )

    suspend fun sendDevice(data: DeviceSocketResponse) {
        send(deviceSession, DeviceSocketResponse.serializer(), data)
    }

    suspend fun sendChat(data: ChatSocketResponse) {
        send(chatSession, ChatSocketResponse.serializer(), data)
    }

    suspend fun sendCommand(data: CommandSocketResponse) {
        send(commandSession, CommandSocketResponse.serializer(), data)
    }

    private fun <T> observeSocket(
        path: String,
        serializer: KSerializer<T>,
        onSessionChange: (WebSocketSession?) -> Unit
    ): Flow<T> = channelFlow {
        client.webSocket(
            urlString = "ws://192.168.31.238:8080$path"
        ) {
            onSessionChange(this)

            try {
                for (frame in incoming) {
                    if (frame is Frame.Text) {
                        send(
                            json.decodeFromString(
                                serializer,
                                frame.readText()
                            )
                        )
                    }
                }
            } finally {
                onSessionChange(null)
            }
        }
    }

    private suspend fun <T> send(
        session: WebSocketSession?,
        serializer: KSerializer<T>,
        data: T
    ) {
        val socket = session ?: error("Socket not connected")
        val text = json.encodeToString(serializer, data)
        socket.send(Frame.Text(text))
    }
}