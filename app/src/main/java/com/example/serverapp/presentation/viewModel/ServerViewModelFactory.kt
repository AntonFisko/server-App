package com.example.serverapp.presentation.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.serverapp.data.room.AppDatabase

class ServerViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "log-database"
        ).build()
        @Suppress("UNCHECKED_CAST")
        return ServerViewModel(context, db.logDao()) as T
    }
}