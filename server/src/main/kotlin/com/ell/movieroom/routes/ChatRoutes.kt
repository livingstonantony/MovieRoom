package com.ell.movieroom.routes


import com.ell.movieroom.models.ChatMessage
import com.ell.movieroom.models.DeviceDetails
import io.ktor.server.routing.Route
import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

val messageResponseFlow = MutableSharedFlow<List<DeviceDetails>>()
val sharedFlow = messageResponseFlow.asSharedFlow()

fun Route.registerChatRoutes() {
    val clients = mutableSetOf<DefaultWebSocketServerSession>()


    val json = Json { ignoreUnknownKeys = true }

    webSocket("/chat") {
        clients.add(this)
        try {
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    // Decode full ChatMessage including 'from'
                    val chatMessage = json.decodeFromString<ChatMessage>(frame.readText())

                    // Broadcast to all clients exactly as sent
                    val broadcastJson = json.encodeToString(chatMessage)
                    clients.forEach { session ->
                        session.send(broadcastJson)
                    }

                    // Optional: log if from Home
                    if (chatMessage.from.equals("Home", ignoreCase = true)) {
                        println("Message from Home: ${chatMessage.message}")
                    }
                }
            }
        } catch (e: ClosedReceiveChannelException) {
            println("Client disconnected")
        } finally {
            clients.remove(this)
        }
    }

    webSocket("/devices") {
        val broadcastJson = json.encodeToString(mapOf("devices" to devices))

        send(broadcastJson)

        val job = launch {
            sharedFlow.collect { message ->
                val broadcastJson = json.encodeToString(mapOf("devices" to message))
                send(broadcastJson)
            }
        }

        runCatching {
            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    clients.forEach { session ->
                        val broadcastJson = json.encodeToString(mapOf("devices" to devices))

                        session.send(broadcastJson)
                    }

                    messageResponseFlow.emit(devices)

                }
            }
        }.onFailure { exception ->
            println("WebSocket exception: ${exception.localizedMessage}")
        }.also {
            job.cancel()
        }
    }
}