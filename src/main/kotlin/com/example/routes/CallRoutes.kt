package com.example.routes

import com.example.models.Patient
import com.example.models.ServerResponse
import com.example.tools.WebsocketUtil
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.util.*
import kotlin.collections.LinkedHashSet

fun Route.callRoutes() {
    val connections = Collections.synchronizedSet<WebsocketUtil?>(LinkedHashSet())

    webSocket("on-call/{doctor}/{type}") {
        val doctor = call.parameters["doctor"]?.toInt()
        val type = call.parameters["type"].toBoolean()
        val sessionID = UUID.randomUUID()
        val thisConnection = WebsocketUtil(this, doctor, type)
        try {
            if (connections.count() == 2) {
                thisConnection.session.sendSerialized(ServerResponse(false, "Doctor is currently unavailable"))
                return@webSocket
            }
            if (!type) {
                connections.first().session.sendSerialized(ServerResponse(true, "Patient has joined the call"))
            }
            connections += thisConnection
            while (true){
                val patient = receiveDeserialized<Patient>()
                //initialiaze webrtc
            }
        } catch (e: Exception) {
            send(e.toString())
        } finally {
            connections -= thisConnection
        }
    }
}

