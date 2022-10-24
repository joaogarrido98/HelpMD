package com.example.tools

import io.ktor.server.websocket.*

class WebsocketUtil(val session: DefaultWebSocketServerSession, val doctor: Int?, val type: Boolean) {
}