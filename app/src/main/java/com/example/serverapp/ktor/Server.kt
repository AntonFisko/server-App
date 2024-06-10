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
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Duration

@Serializable
data class GestureCommand(val type: String, val duration: Long, val message: String? = null)
data class GestureResult(val status: String, val message: String)

class Server(
    private val port: Int,
    private val context: Context,
    private val onClientConnected: (String) -> Unit,
    private val onClientDisconnected: (String) -> Unit,
    private val onLog: (String) -> Unit
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
                    onLog("Received message: $receivedText") // Log received message

                    val command = try {
                        Json.decodeFromString<GestureCommand>(receivedText)
                    } catch (e: Exception) {
                        onLog("Failed to decode message: ${e.localizedMessage}")
                        return@consumeEach
                    }

                    // Handle received gesture command and send new commands
                    handleReceivedCommand(command, session)
                }
            }
        }
    }

    private suspend fun handleReceivedCommand(command: GestureCommand, session: DefaultWebSocketServerSession) {
        when (command.type) {
            "RESULT" -> {
                // Handle the result from the client
                onLog("Client result: ${command.message}")
            }
            else -> {
                // Send new gesture commands to the client
                val newCommand = generateGestureCommand()
                val commandJson = Json.encodeToString(newCommand)
                session.outgoing.send(Frame.Text(commandJson))
            }
        }
    }

    private fun generateGestureCommand(): GestureCommand {
        // Generate a random gesture command (example)
        val commandType = if (Math.random() > 0.5) "SWIPE_UP" else "SWIPE_DOWN"
        val duration = (500..1500).random().toLong()
        return GestureCommand(commandType, duration)
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
}