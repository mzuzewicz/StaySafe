package com.example.staysafe

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.staysafe.maps.JourneyViewModel
import com.example.staysafe.maps.api.DirectionsMode
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendTrackingScreen(
    viewModel: JourneyViewModel,
    onJourneyComplete: () -> Unit
) {
    val context = LocalContext.current
    val journey = viewModel.journey.collectAsState().value
    val currentLocation = viewModel.currentLocation.collectAsState().value
    val isTracking = viewModel.isTracking.collectAsState().value
    val destinationReached = viewModel.destinationReached.collectAsState().value
    val directionsMode = viewModel.directionsMode.collectAsState().value
    val eta = viewModel.estimatedTimeOfArrival.collectAsState().value

    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                isMyLocationEnabled = true,
                mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                    context,
                    R.raw.map_style
                )
            )
        )
    }

    val cameraPositionState = rememberCameraPositionState()

    var hasMovedCamera by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.startCoordinates.value) {
        val startCoordinates = viewModel.startCoordinates.value
        if (!hasMovedCamera && startCoordinates != null) {
            cameraPositionState.move(
                CameraUpdateFactory.newLatLngZoom(startCoordinates, 15f)
            )
            hasMovedCamera = true
        }
    }

    LaunchedEffect(Unit) {
        viewModel.startTracking()
    }

    LaunchedEffect(destinationReached) {
        if (destinationReached) {
            viewModel.stopTracking()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = mapProperties,
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = true,
                myLocationButtonEnabled = true
            )
        ) {
            journey?.let { journeyDetails ->
                viewModel.startCoordinates.value?.let { startCoords ->
                    Marker(
                        state = MarkerState(position = startCoords),
                        title = "Start: ${journeyDetails.startDestination}"
                    )
                }

                viewModel.endCoordinates.value?.let { endCoords ->
                    Marker(
                        state = MarkerState(position = endCoords),
                        title = "Destination: ${journeyDetails.endDestination}"
                    )
                }

                viewModel.routePoints.value?.let { points ->
                    Polyline(
                        points = points,
                        color = MaterialTheme.colorScheme.primary,
                        width = 8f
                    )
                }
            }

            currentLocation?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "Current Location"
                )
            }
        }

        Card(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DirectionsMode.values().forEach { mode ->
                    val isSelected = mode == directionsMode

                    IconButton(
                        onClick = { viewModel.setDirectionsMode(mode) },
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                                shape = CircleShape
                            )
                    ) {
                        // Replace with proper icons as needed
                        /* Icon(...) */
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            journey?.let {
                Text(
                    text = "Journey: ${it.startDestination} to ${it.endDestination}",
                    style = MaterialTheme.typography.bodyMedium
                )

                LinearProgressIndicator(
                    progress = viewModel.journeyProgress.collectAsState().value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                Text(
                    text = "ETA: $eta",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:999")
                        context.startActivity(intent)
                        viewModel.sendEmergencyAlert()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Emergency"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("EMERGENCY")
                }

                Button(
                    onClick = {
                        viewModel.stopTracking()
                        onJourneyComplete()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Complete Journey"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("COMPLETE")
                }
            }
        }

        if (destinationReached) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Destination Reached") },
                text = { Text("You have reached your destination!") },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.acknowledgeDestinationReached()
                            onJourneyComplete()
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }
}
