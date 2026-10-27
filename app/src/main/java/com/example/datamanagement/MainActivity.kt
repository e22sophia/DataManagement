package com.example.datamanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val vm: MainViewModel = viewModel()

            val settings by vm.settings.collectAsStateWithLifecycle()

            MaterialTheme(
                colorScheme =
                    if (settings.darkMode) {
                        androidx.compose.material3.darkColorScheme()
                    } else {
                        androidx.compose.material3.lightColorScheme()
                    }
            ) {

                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {

                    HomeScreen(vm)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(vm: MainViewModel) {

    val settings by vm.settings.collectAsStateWithLifecycle()

    val readings by vm.readings.collectAsStateWithLifecycle()

    val averageTemperature by
    vm.averageTemperature.collectAsStateWithLifecycle()

    var deviceId by remember(settings.deviceId) {
        mutableStateOf(settings.deviceId)
    }

    var alertText by remember(settings.alertTemp) {
        mutableStateOf(settings.alertTemp.toString())
    }

    var temperatureText by remember {
        mutableStateOf("")
    }

    var heartRateText by remember {
        mutableStateOf("")
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    val timeFormat = remember {

        SimpleDateFormat(
            "dd MMM HH:mm:ss",
            Locale.getDefault()
        )
    }

    Scaffold(

        topBar = {

            TopAppBar(
                title = {
                    Text("IoT Data Manager")
                }
            )
        }

    ) { paddingValues ->

        LazyColumn(

            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp),

            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            // -------------------------
            // SETTINGS
            // -------------------------

            item {

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement =
                            Arrangement.spacedBy(8.dp)
                    ) {

                        Text(
                            text = "Settings (DataStore)",
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(

                            value = deviceId,

                            onValueChange = {
                                deviceId = it
                            },

                            label = {
                                Text("Device ID")
                            },

                            singleLine = true,

                            modifier =
                                Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(

                            value = alertText,

                            onValueChange = {
                                alertText = it
                            },

                            label = {
                                Text(
                                    "Alert temperature (°C)"
                                )
                            },

                            singleLine = true,

                            modifier =
                                Modifier.fillMaxWidth()
                        )

                        Row(
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Text(
                                text = "Dark mode",
                                modifier =
                                    Modifier.weight(1f)
                            )

                            Switch(

                                checked =
                                    settings.darkMode,

                                onCheckedChange = {
                                    vm.saveSettings(
                                        settings.copy(
                                            darkMode = it
                                        )
                                    )
                                }
                            )
                        }

                        Button(

                            onClick = {

                                val alert =
                                    alertText.toFloatOrNull()

                                if (
                                    deviceId.isBlank() ||
                                    alert == null
                                ) {

                                    errorMessage =
                                        "Enter a valid device ID and alert temperature"

                                } else {

                                    errorMessage = null

                                    vm.saveSettings(
                                        settings.copy(
                                            deviceId =
                                                deviceId.trim(),
                                            alertTemp =
                                                alert
                                        )
                                    )
                                }
                            },

                            modifier =
                                Modifier.fillMaxWidth()
                        ) {

                            Text("Save Settings")
                        }
                    }
                }
            }

            // -------------------------
            // ADD READING
            // -------------------------

            item {

                Card(
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier =
                            Modifier.padding(16.dp),

                        verticalArrangement =
                            Arrangement.spacedBy(8.dp)
                    ) {

                        Text(
                            text = "Add Reading (Room)",
                            fontWeight =
                                FontWeight.Bold
                        )

                        Row(
                            horizontalArrangement =
                                Arrangement.spacedBy(8.dp)
                        ) {

                            OutlinedTextField(

                                value =
                                    temperatureText,

                                onValueChange = {
                                    temperatureText = it
                                },

                                label = {
                                    Text("Temp °C")
                                },

                                singleLine = true,

                                modifier =
                                    Modifier.weight(1f)
                            )

                            OutlinedTextField(

                                value =
                                    heartRateText,

                                onValueChange = {
                                    heartRateText = it
                                },

                                label = {
                                    Text("Heart rate")
                                },

                                singleLine = true,

                                modifier =
                                    Modifier.weight(1f)
                            )
                        }

                        errorMessage?.let {

                            Text(
                                text = it,
                                color =
                                    MaterialTheme.colorScheme.error
                            )
                        }

                        Row(
                            horizontalArrangement =
                                Arrangement.spacedBy(8.dp)
                        ) {

                            Button(

                                onClick = {

                                    val temperature =
                                        temperatureText
                                            .toFloatOrNull()

                                    val heartRate =
                                        heartRateText
                                            .toIntOrNull()

                                    if (
                                        temperature == null ||
                                        heartRate == null
                                    ) {

                                        errorMessage =
                                            "Enter valid numbers"

                                    } else {

                                        errorMessage = null

                                        vm.addReading(
                                            temperature,
                                            heartRate
                                        )

                                        temperatureText = ""
                                        heartRateText = ""
                                    }
                                }
                            ) {

                                Text("Add")
                            }

                            OutlinedButton(

                                onClick = {
                                    vm.clearAll()
                                }
                            ) {

                                Text("Clear All")
                            }
                        }
                    }
                }
            }

            // -------------------------
            // SUMMARY
            // -------------------------

            item {

                Text(

                    text =
                        "Readings: ${readings.size}" +
                                (
                                        averageTemperature?.let {
                                            " | Avg Temp: %.1f °C"
                                                .format(it)
                                        } ?: ""
                                        ),

                    style =
                        MaterialTheme.typography.titleMedium
                )
            }

            // -------------------------
            // HISTORY
            // -------------------------

            items(
                items = readings,
                key = {
                    it.id
                }
            ) { reading ->

                val isAlert =
                    reading.temperature >
                            settings.alertTemp

                Card(

                    modifier =
                        Modifier.fillMaxWidth(),

                    colors =
                        CardDefaults.cardColors(

                            containerColor =
                                if (isAlert) {

                                    MaterialTheme
                                        .colorScheme
                                        .errorContainer

                                } else {

                                    MaterialTheme
                                        .colorScheme
                                        .surfaceVariant
                                }
                        )
                ) {

                    Row(

                        modifier =
                            Modifier.padding(12.dp),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Column(
                            modifier =
                                Modifier.weight(1f)
                        ) {

                            Text(

                                text =
                                    "${reading.deviceId}: " +
                                            "%.1f °C, ${reading.heartRate} /min"
                                                .format(
                                                    reading.temperature
                                                ),

                                fontWeight =
                                    FontWeight.SemiBold
                            )

                            Text(

                                text =
                                    timeFormat.format(
                                        Date(reading.timestamp)
                                    ) +
                                            if (isAlert) {
                                                "  ALERT"
                                            } else {
                                                ""
                                            },

                                style =
                                    MaterialTheme
                                        .typography
                                        .bodySmall
                            )
                        }

                        IconButton(

                            onClick = {
                                vm.deleteReading(reading)
                            }
                        ) {

                            Icon(

                                imageVector =
                                    Icons.Filled.Delete,

                                contentDescription =
                                    "Delete"
                            )
                        }
                    }
                }
            }

            item {

                Spacer(
                    modifier =
                        Modifier.height(24.dp)
                )
            }
        }
    }
}