package com.ell.movieroom.routes


import com.ell.movieroom.models.ChatMessage
import com.ell.movieroom.models.ClientMessage
import io.ktor.server.routing.Route
import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.serialization.json.Json


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
}