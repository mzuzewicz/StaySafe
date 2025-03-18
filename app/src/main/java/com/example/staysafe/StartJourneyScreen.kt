package com.example.staysafe

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartJourneyScreen(navController: NavController) {
    val drawerState = remember { mutableStateOf(false) }

    Scaffold()
    { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                ) {
                Text("Start Tracking Your Journey")

                Spacer(modifier = Modifier.height(16.dp))

                JourneyForm { startLocation, endLocation, departureTime, arrivalTime ->
                    val startLocation = startLocation
                    val endLocation = endLocation
                    val departureHour = departureTime.hour
                    val departureMinute = departureTime.minute
                    val arrivalHour = arrivalTime.hour
                    val arrivalMinute = arrivalTime.minute

                    //pass variables to next screen
                    navController.navigate("TrackingScreen?departureHour=$departureHour&departureMinute=$departureMinute&arrivalHour=$arrivalHour&arrivalMinute=$arrivalMinute")
                }
            }
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
//onStartJourney passes all input to the next screen
fun JourneyForm( onStartJourney: (startLocation: String, endLocation: String, TimePickerState, TimePickerState) -> Unit) {
    var startLocation by remember { mutableStateOf("") }
    var endLocation by remember { mutableStateOf("") }

    var departureTime = rememberTimePickerState()
    var arrivalTime = rememberTimePickerState()

    Column(
        modifier = Modifier
            .fillMaxWidth(),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(value = startLocation,
            onValueChange = { startLocation = it },
            label = { Text("Start Location") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = endLocation,
            onValueChange = { endLocation = it },
            label = { Text("End Location") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Time of Departure")
        TimeInput(departureTime)


        Text("Estimated Arrival Time")
        TimeInput(arrivalTime)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onStartJourney(startLocation, endLocation, departureTime, arrivalTime) }) {
            Text(text = "Start Journey")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeInput(label: String, state: TimePickerState) {
    val context = LocalContext.current
    val timePickerDialog = remember {
        TimePickerDialog(
            context,
            { _, hour, minute ->
                state.hour = hour
                state.minute = minute
            },
            state.hour,
            state.minute,
            true
        )
    }

    OutlinedTextField(
        value = "%02d:%02d".format(state.hour, state.minute),
        onValueChange = {}, // Read-only input
        label = { Text(label) },
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { timePickerDialog.show() }) {
                Icon(imageVector = Icons.Filled.Create, contentDescription = "Select Time")
            }
        },
        modifier = Modifier.padding(8.dp)
    )
}