package com.example.serverapp.ui.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serverapp.ktor.Server
import com.example.serverapp.room.LogDao
import com.example.serverapp.room.LogEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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