package com.example.datamanagement

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "readings")
data class Reading(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val deviceId: String,

    val temperature: Float,

    val heartRate: Int,

    val timestamp: Long = System.currentTimeMillis()
)