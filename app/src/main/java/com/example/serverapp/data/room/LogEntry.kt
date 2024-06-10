package com.example.serverapp.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "logs")
data class LogEntry(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val timestamp: Long,
    val message: String
)