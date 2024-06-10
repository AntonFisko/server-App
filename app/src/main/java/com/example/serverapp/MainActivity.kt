package com.example.serverapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.serverapp.ui.ServerApp
import com.example.serverapp.ui.viewModel.ServerViewModel
import com.example.serverapp.ui.viewModel.ServerViewModelFactory

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