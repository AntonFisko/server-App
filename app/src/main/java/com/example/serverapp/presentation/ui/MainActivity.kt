package com.example.serverapp.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.serverapp.presentation.viewModel.ServerViewModel
import com.example.serverapp.presentation.viewModel.ServerViewModelFactory

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