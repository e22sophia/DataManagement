package com.example.datamanagement

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadingDao {

    @Insert
    suspend fun insert(reading: Reading)

    @Delete
    suspend fun delete(reading: Reading)

    @Query("DELETE FROM readings")
    suspend fun clearAll()

    @Query("SELECT * FROM readings ORDER BY timestamp DESC")
    fun getAll(): Flow<List<Reading>>

    @Query("SELECT AVG(temperature) FROM readings")
    fun averageTemperature(): Flow<Float?>
}