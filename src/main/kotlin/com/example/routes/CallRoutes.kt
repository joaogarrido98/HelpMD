package com.example.routes

import com.example.models.Patient
import com.example.models.ServerResponse
import com.example.models.SignalingMessage
import com.example.tools.SessionManager
import com.example.tools.WebsocketUtil
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import java.util.*
import kotlin.collections.LinkedHashSet

fun Route.callRoutes() {
    webSocket("call/{doctor}/{user}") {
        val sessionID = UUID.randomUUID()
        try {
            SessionManager.onSessionStarted(sessionID, this)

            for (frame in incoming) {
                when (frame) {
                    is Frame.Text -> {
                        SessionManager.onMessage(sessionID, frame.readText())
                    }
                    else -> Unit
                }
            }
            SessionManager.onSessionClose(sessionID)
        } catch (e: ClosedReceiveChannelException) {
            SessionManager.onSessionClose(sessionID)
        } catch (e: Throwable) {
            SessionManager.onSessionClose(sessionID)
        }
    }



}

