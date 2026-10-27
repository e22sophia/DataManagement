package com.example.datamanagement

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(
    name = "settings"
)

data class AppSettings(
    val deviceId: String = "W01",
    val alertTemp: Float = 37.5f,
    val darkMode: Boolean = false
)

class SettingsRepository(
    private val context: Context
) {

    private object Keys {

        val DEVICE_ID =
            stringPreferencesKey("device_id")

        val ALERT_TEMP =
            floatPreferencesKey("alert_temp")

        val DARK_MODE =
            booleanPreferencesKey("dark_mode")
    }

    val settings: Flow<AppSettings> =
        context.dataStore.data.map { preferences ->

            AppSettings(
                deviceId =
                    preferences[Keys.DEVICE_ID] ?: "W01",

                alertTemp =
                    preferences[Keys.ALERT_TEMP] ?: 37.5f,

                darkMode =
                    preferences[Keys.DARK_MODE] ?: false
            )
        }

    suspend fun save(settings: AppSettings) {

        context.dataStore.edit { preferences ->

            preferences[Keys.DEVICE_ID] =
                settings.deviceId

            preferences[Keys.ALERT_TEMP] =
                settings.alertTemp

            preferences[Keys.DARK_MODE] =
                settings.darkMode
        }
    }
}