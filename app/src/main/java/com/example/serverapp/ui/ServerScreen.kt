package com.example.serverapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        androidx.compose.material.TextField(
            value = serverPort,
            onValueChange = { serverPort = it },
            label = { androidx.compose.material.Text("Server Port") },
            modifier = Modifier.fillMaxWidth()
        )
        androidx.compose.material.Button(
            onClick = {
                viewModel.startServer(serverPort.toInt())
            },
            enabled = !isServerRunning,
            modifier = Modifier.fillMaxWidth()
        ) {
            androidx.compose.material.Text("Start Server")
        }
        androidx.compose.material.Button(
            onClick = {
                viewModel.stopServer()
            },
            enabled = isServerRunning,
            modifier = Modifier.fillMaxWidth()
        ) {
            androidx.compose.material.Text("Stop Server")
        }
        androidx.compose.material.Button(
            onClick = {
                viewModel.fetchLogs()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            androidx.compose.material.Text("View Logs")
        }
        val logs by viewModel.logs.collectAsState()
        logs.forEach { log ->
            androidx.compose.material.Text(text = log.message)
        }
    }
}