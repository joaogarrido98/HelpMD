package com.example.routes

import com.example.models.ServerResponse
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.util.*
import kotlin.collections.LinkedHashSet

fun Route.callRoutes() {
    class WebsocketUtil(val session: WebSocketServerSession, userType: Boolean, user: Int) {
        val userId: Int = user
        val type: Boolean = userType
    }

    val connections = Collections.synchronizedSet<WebsocketUtil?>(LinkedHashSet())

    webSocket("call/{booking}") {
        val type = call.request.queryParameters["type"].toBoolean()
        val user = call.parameters["user"]!!.toInt()
        val thisConnection = WebsocketUtil(this, type, user)
        connections += thisConnection
        try {
            send("You are connected! There are ${connections.count()} users here.")
            for (frame in incoming) {
                frame as? Frame.Text ?: continue
                val receivedText = frame.readText()
                val textWithUsername = "[${thisConnection.userId}]: $receivedText"
                connections.forEach {
                    it.session.send(textWithUsername)
                }
            }
        } catch (e: Exception) {
            sendSerialized(ServerResponse(false, e.localizedMessage))
        } finally {
            connections -= thisConnection
        }
    }
}

