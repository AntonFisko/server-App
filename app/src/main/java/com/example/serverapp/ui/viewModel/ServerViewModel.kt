package com.example.serverapp.ui.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serverapp.ktor.Server
import com.example.serverapp.room.LogDao
import com.example.serverapp.room.LogEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ServerViewModel(private val logDao: LogDao) : ViewModel() {
    private var server: Server? = null
    private val _isServerRunning = MutableStateFlow(false)
    val isServerRunning: StateFlow<Boolean> get() = _isServerRunning

    private val _logs = MutableStateFlow<List<LogEntry>>(emptyList())
    val logs: StateFlow<List<LogEntry>> get() = _logs

    fun startServer(port: Int) {
        if (server == null) {
            viewModelScope.launch(Dispatchers.IO) {
                server = Server(port).apply {
                    start()
                }
                _isServerRunning.value = true
                addLog("Server started on port $port")
            }
        }
    }

    fun stopServer() {
        viewModelScope.launch(Dispatchers.IO) {
            server?.stop()
            server = null
            _isServerRunning.value = false
            addLog("Server stopped")
        }
    }

    fun fetchLogs() {
        viewModelScope.launch {
            _logs.value = logDao.getAllLogs()
        }
    }

    private fun addLog(message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            logDao.insertLog(LogEntry(0, System.currentTimeMillis(), message))
            fetchLogs()
        }
    }
}