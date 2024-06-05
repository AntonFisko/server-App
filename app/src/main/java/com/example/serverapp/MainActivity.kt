package com.example.serverapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.serverapp.ktor.Server
import com.example.serverapp.room.AppDatabase
import com.example.serverapp.room.LogDao
import com.example.serverapp.room.LogEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val serverViewModel: ServerViewModel by viewModels {
        ServerViewModelFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ServerApp(serverViewModel)
        }
    }
}

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
        val logs by viewModel.logs.collectAsState()
        logs.forEach { log ->
            Text(text = log.message)
        }
    }
}

class ServerViewModelFactory(private val context: android.content.Context) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "log-database"
        ).build()
        @Suppress("UNCHECKED_CAST")
        return ServerViewModel(db.logDao()) as T
    }
}

class ServerViewModel(private val logDao: LogDao) : ViewModel() {
    private var server: Server? = null
    private val _isServerRunning = MutableStateFlow(false)
    val isServerRunning = _isServerRunning.asStateFlow()

    private val _logs = MutableStateFlow<List<LogEntry>>(emptyList())
    val logs = _logs.asStateFlow()

    fun startServer(port: Int) {
        server = Server(port).apply {
            start()
        }
        _isServerRunning.value = true
        addLog("Server started on port $port")
    }

    fun stopServer() {
        server?.stop()
        server = null
        _isServerRunning.value = false
        addLog("Server stopped")
    }

    fun fetchLogs() {
        viewModelScope.launch {
            _logs.value = logDao.getAllLogs()
        }
    }

    private fun addLog(message: String) {
        viewModelScope.launch {
            logDao.insertLog(LogEntry(0, System.currentTimeMillis(), message))
            fetchLogs()
        }
    }
}
