package com.ell.movieroom.routes


import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.Serializable

@Serializable
data class DeviceDetails(
    val deviceId: String,
    val deviceName: String,
    val os: String,
    val version: String
)

fun Route.deviceRoutes() {

    route("/devices") {

        post {
            val deviceDetails = call.receive<DeviceDetails>()
            println("Received device: $deviceDetails")

            call.respond(
                mapOf(
                    "status" to "success",
                    "deviceId" to deviceDetails.deviceId
                )
            )
        }
    }
}
