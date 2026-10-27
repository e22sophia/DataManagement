package com.example.datamanagement

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val dao =
        AppDatabase.get(application).readingDao()

    private val repository =
        SettingsRepository(application)

    val readings =
        dao.getAll()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    val averageTemperature =
        dao.averageTemperature()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                null
            )

    val settings =
        repository.settings
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                AppSettings()
            )

    fun addReading(
        temperature: Float,
        heartRate: Int
    ) {
        viewModelScope.launch {

            dao.insert(
                Reading(
                    deviceId = settings.value.deviceId,
                    temperature = temperature,
                    heartRate = heartRate
                )
            )
        }
    }

    fun deleteReading(reading: Reading) {

        viewModelScope.launch {
            dao.delete(reading)
        }
    }

    fun clearAll() {

        viewModelScope.launch {
            dao.clearAll()
        }
    }

    fun saveSettings(settings: AppSettings) {

        viewModelScope.launch {
            repository.save(settings)
        }
    }
}