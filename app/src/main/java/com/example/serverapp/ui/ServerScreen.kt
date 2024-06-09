package com.example.serverapp.ui


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.serverapp.ui.viewModel.ServerViewModel

@Composable
fun ServerApp(viewModel: ServerViewModel) {
    var serverPort by remember { mutableStateOf("8080") }
    val isServerRunning by viewModel.isServerRunning.collectAsState()
    val logs by viewModel.logs.collectAsState()
    val connectedClients by viewModel.connectedClients.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = serverPort,
            onValueChange = { serverPort = it },
            label = { Text("Server Port") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                viewModel.startServer(serverPort.toInt())
            },
            enabled = !isServerRunning,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Server")
        }
        Button(
            onClick = {
                viewModel.stopServer()
            },
            enabled = isServerRunning,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Stop Server")
        }
        Button(
            onClick = {
                viewModel.fetchLogs()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View Logs")
        }

        Text("Connected Clients:")
        connectedClients.forEach { client ->
            Text(text = client)
        }

        Text("Logs:")
        logs.forEach { log ->
            Text(text = log.message)
        }
    }
}
