package com.example.tools

import io.ktor.server.websocket.*

class WebsocketUtil(val session: WebSocketServerSession, val doctor: Int?, val type: Boolean) {
}