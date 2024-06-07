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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Duration

data class GestureCommand(val type: String, val duration: Long)
data class GestureResult(val status: String, val message: String)

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
                                val command = Json.decodeFromString<GestureCommand>(receivedText)

                                // Обработка полученной команды жестов
                                val result = processGestureCommand(command)

                                // Отправка результата обратно клиенту
                                val resultJson = Json.encodeToString(result)
                                outgoing.send(Frame.Text(resultJson))
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
        try {
            server.start(wait = true)
            println("Server started on port $port")
        } catch (e: Exception) {
            println("Failed to start server: ${e.localizedMessage}")
        }
    }

    fun stop() {
        try {
            server.stop(1000, 10000)
            println("Server stopped")
        } catch (e: Exception) {
            println("Failed to stop server: ${e.localizedMessage}")
        }
    }

    private fun processGestureCommand(command: GestureCommand): GestureResult {
        return when (command.type) {
            "SWIPE_UP" -> GestureResult("SUCCESS", "Swipe up executed for ${command.duration}ms")
            "SWIPE_DOWN" -> GestureResult("SUCCESS", "Swipe down executed for ${command.duration}ms")
            else -> GestureResult("ERROR", "Unknown gesture command")
        }
    }
}