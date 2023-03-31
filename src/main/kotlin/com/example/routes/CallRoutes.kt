package com.example.routes

import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.util.*
import kotlin.collections.LinkedHashSet

fun Route.callRoutes() {
   data class Connection(val session: DefaultWebSocketSession, val type: Boolean)

    val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
    //doctor or user joins

    //if is doctor
    // if there's no one there send "connected" and then in frontend doctor side creates offer
    // when user joins send "ready"
    // on ready send offer to other user and wait for answer

    //on app I show a screen until the doctor as created an offer and sent it over

    webSocket("call/{booking}") {
        val type = call.request.queryParameters["type"].toBoolean()
        val thisConnection = Connection(this, type)
        connections += thisConnection
        //when both are connected tell doctor to start
        if (connections.size == 2) {
            connections.first {
                it.type
            }.session.send("ready connected")
        }
        try {
            for (frame in incoming) {
                frame as? Frame.Text ?: continue
                connections.forEach {
                    if (it.type != thisConnection.type) {
                        it.session.send(frame.readText())
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

