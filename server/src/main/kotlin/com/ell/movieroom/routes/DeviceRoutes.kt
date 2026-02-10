package com.ell.movieroom.routes


import com.ell.movieroom.models.DeviceDetails
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import java.util.UUID


val devices = mutableListOf<DeviceDetails>()

fun Route.deviceRoutes() {


    route("/devices") {

        get {
            val broadcastJson = json.encodeToJsonElement<List<DeviceDetails>>( devices)
            call.respond(
                mapOf(
                    "status" to "success",
                    "devices" to broadcastJson
                )
            )
        }

        post {
            val deviceDetails = call.receive<DeviceDetails>()
            println("Received device: $deviceDetails")

            deviceDetails.apply {
                deviceId = UUID.randomUUID().toString()
            }
            devices.add(deviceDetails)

            messageResponseFlow.emit(devices)

            call.respond(
                mapOf(
                    "status" to "success",
                    "deviceId" to deviceDetails.deviceId
                )
            )
        }
        put("/{id}") {
            val id = call.parameters["id"]
            if (id == null) {
                call.respond(mapOf("status" to "error", "message" to "Missing id"))
                return@put
            }

            val updatedDevice = call.receive<DeviceDetails>()
            val index = devices.indexOfFirst { it.deviceId == id }
            if (index == -1) {
                call.respond(mapOf("status" to "error", "message" to "Device not found"))
                return@put
            }

            devices[index] = devices[index].copy(
                duration = updatedDevice.duration
            )

            messageResponseFlow.emit(devices)

            call.respond(mapOf("status" to "success", "deviceId" to devices[index].deviceId))
        }
        delete("/{id}") {
            val id = call.parameters["id"]
            if (id == null) {
                call.respond(mapOf("status" to "error", "message" to "Missing id"))
                return@delete
            }

            val removed = devices.removeIf { it.deviceId == id }
            if (!removed) {
                call.respond(mapOf("status" to "error", "message" to "Device not found"))
                return@delete
            }

            messageResponseFlow.emit(devices)

            call.respond(mapOf("status" to "success", "deletedId" to id))
        }
    }

}
