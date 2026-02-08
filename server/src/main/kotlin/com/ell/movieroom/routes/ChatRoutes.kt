package com.ell.movieroom.routes


import io.ktor.server.routing.Route
import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.channels.ClosedReceiveChannelException

fun Route.registerChatRoutes() {
    // Map each session to its device name
    val clients = mutableMapOf<DefaultWebSocketServerSession, String>()

    webSocket("/chat") {
        try {
            // Step 1: Receive device name as the first message
            val deviceNameFrame = incoming.receive() as Frame.Text
            val deviceName = deviceNameFrame.readText()
            clients[this] = deviceName
            println("$deviceName connected.")

            // Step 2: Listen for messages
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val message = frame.readText()

                    // Broadcast to all clients
                    clients.keys.forEach { session ->
                        session.send("$deviceName: $message")
                    }

                    // Optional: log messages from Home specifically
                    if (deviceName.equals("Home", ignoreCase = true)) {
                        println("Message from Home: $message")
                    }
                }
            }
        } catch (e: ClosedReceiveChannelException) {
            println("Client disconnected: ${clients[this]}")
        } finally {
            // Remove session when disconnected
            println("${clients[this]} disconnected")
            clients.remove(this)
        }
    }
}
