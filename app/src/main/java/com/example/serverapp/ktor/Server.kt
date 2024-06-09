package com.example.serverapp.ktor

import android.content.Context
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.origin
import io.ktor.server.routing.routing
import io.ktor.server.websocket.DefaultWebSocketServerSession
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

class Server(
    private val port: Int,
    private val context: Context,
    private val onClientConnected: (String) -> Unit,
    private val onClientDisconnected: (String) -> Unit
) {
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
                val clientInfo = call.request.origin.remoteHost
                onClientConnected(clientInfo)
                try {
                    handleWebSocketSession(this)
                } catch (e: Exception) {
                    println("Error in WebSocket: ${e.localizedMessage}")
                } finally {
                    onClientDisconnected(clientInfo)
                }
            }
        }
    }

    private suspend fun handleWebSocketSession(session: DefaultWebSocketServerSession) {
        while (session.isActive) {
            session.incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    val receivedText = frame.readText()
                    val command = Json.decodeFromString<GestureCommand>(receivedText)

                    // Обработка полученной команды жестов
                    val result = processGestureCommand(command, session)

                    // Отправка результата обратно клиенту
                    val resultJson = Json.encodeToString(result)
                    session.outgoing.send(Frame.Text(resultJson))
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

    private suspend fun processGestureCommand(command: GestureCommand, session: DefaultWebSocketServerSession): GestureResult {
        return when (command.type) {
            "CHROME_OPENED" -> {
                // Отправка команд жестов клиенту
                session.sendGestureCommands()
                GestureResult("SUCCESS", "Chrome opened detected, gesture commands sent")
            }
            "SWIPE_UP" -> GestureResult("SUCCESS", "Swipe up executed for ${command.duration}ms")
            "SWIPE_DOWN" -> GestureResult("SUCCESS", "Swipe down executed for ${command.duration}ms")
            else -> GestureResult("ERROR", "Unknown gesture command")
        }
    }

    private suspend fun DefaultWebSocketServerSession.sendGestureCommands() {
        // Пример отправки команд жестов
        val commands = listOf(
            GestureCommand("SWIPE_UP", 1000),
            GestureCommand("SWIPE_DOWN", 1000)
        )
        commands.forEach { command ->
            val commandJson = Json.encodeToString(command)
            outgoing.send(Frame.Text(commandJson))
        }
    }
}
