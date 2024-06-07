package com.example.serverapp.ui.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.serverapp.room.AppDatabase

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