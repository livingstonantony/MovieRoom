package com.ell.movieroom


import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.ell.movieroom.plugins.configureSerialization
import com.ell.movieroom.routes.deviceRoutes
import com.ell.movieroom.routes.registerChatRoutes
import io.ktor.server.application.*
import io.ktor.server.response.respondText
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import java.net.Inet4Address
import java.net.NetworkInterface
import java.time.Duration
import kotlin.time.Duration.Companion.seconds

fun main() {
    printSystemIp()
    embeddedServer(
        Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    configureSerialization()

    install(WebSockets) {
        pingPeriod = 15.seconds
        timeout = 15.seconds
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }
        deviceRoutes()
        registerChatRoutes()
    }
}


fun printSystemIp() {
    val ip = NetworkInterface.getNetworkInterfaces().toList()
        .flatMap { it.inetAddresses.toList() }
        .filterIsInstance<Inet4Address>()
        .firstOrNull { !it.isLoopbackAddress }
        ?.hostAddress

    println("Server IP: ${ip ?: "Not found"}")
}
