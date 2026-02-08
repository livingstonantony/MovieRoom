package com.ell.movieroom.routes


import com.ell.movieroom.models.DeviceDetails
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json



val devices = mutableListOf<DeviceDetails>()

fun Route.deviceRoutes() {


    route("/devices") {

        get {
            call.respond(
                mapOf(
                    "devices" to devices
                )
            )
        }

        post {
            val deviceDetails = call.receive<DeviceDetails>()
            println("Received device: $deviceDetails")

            devices.add(deviceDetails)

            messageResponseFlow.emit(devices)

            call.respond(
                mapOf(
                    "status" to "success",
                    "deviceId" to deviceDetails.deviceId
                )
            )
        }
    }
}
