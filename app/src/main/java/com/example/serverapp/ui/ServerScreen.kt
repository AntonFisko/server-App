package com.example.serverapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp




//@Composable
//fun ServerScreen( viewModel: ServerViewModel) {//
//    var port by remember { mutableStateOf("") }
//    val isRunning by remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        TextField(
//            value = port,
//            onValueChange = { port = it },
//            label = { Text("Port") },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        Row {
//            Button(
//                onClick = {
//                    viewModel.saveConfig(port = port)
//                },
//                modifier = Modifier.weight(1f)
//            ) {
//                Text("Config")
//            }
//            Spacer(modifier = Modifier.width(8.dp))
//            Button(
//                onClick = {
////                    isRunning = !isRunning
//                    if (isRunning) {
//                        viewModel.startServer()
//                    } else {
//                        viewModel.stopServer()
//                    }
//                },
//                modifier = Modifier.weight(1f)
//            ) {
//                Text(if (isRunning) "Stop" else "Start")
//            }
//        }
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(
//            onClick = {
//                viewModel.showLogs()
//            }
//        ) {
//            Text("Logs")
//        }
//    }
//}
