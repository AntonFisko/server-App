package com.example.serverapp.ktor

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.isActive
import java.time.Duration

class Server(private val port: Int) {
    private val server = embeddedServer(Netty, port) {
        install(WebSockets) {
            pingPeriod = Duration.ofMinutes(1)
            timeout = Duration.ofMinutes(15)
            maxFrameSize = Long.MAX_VALUE
            masking = false
        }
        install(ContentNegotiation) {
            json()
        }
        routing {
            webSocket("/ws") {
                try {
                    while (isActive) {
                        incoming.consumeEach { frame ->
                            if (frame is Frame.Text) {
                                val receivedText = frame.readText()
                                // Обработка полученного сообщения
                                outgoing.send(Frame.Text("Received: $receivedText"))
                            }
                        }
                    }
                } catch (e: Exception) {
                    println("Error in WebSocket: ${e.localizedMessage}")
                }
            }
        }
    }

    fun start() {
        server.start(wait = true)
    }

    fun stop() {
        server.stop(1000, 10000)
    }
}