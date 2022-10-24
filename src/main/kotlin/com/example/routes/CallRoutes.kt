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

    webSocket("oncall/{doctor}/{type}") {
        val doctor = call.parameters["doctor"]?.toInt()
        val type = call.parameters["type"].toBoolean()
        val thisConnection = WebsocketUtil(this, doctor, type)
        connections += thisConnection
        try {
            while (true) {
                val patient = receiveDeserialized<Patient>()
                //initialize webrtc
                connections.forEach {
                    if(!it.type){
                        it.session.sendSerialized(ServerResponse(true, ""))
                    }
                    if(it.type){
                        it.session.sendSerialized(ServerResponse(true, "New patient", patient))
                    }
                }
            }
        } catch (e: Exception) {
            send(e.toString())
        } finally {
            connections -= thisConnection
        }
    }
}

