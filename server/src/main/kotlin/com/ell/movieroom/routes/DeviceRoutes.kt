package com.ell.movieroom.routes


import com.ell.movieroom.models.DeviceDetails
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

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
