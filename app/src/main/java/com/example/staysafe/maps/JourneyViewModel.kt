package com.example.staysafe.maps

import android.app.Application
import android.icu.text.SimpleDateFormat
import android.location.Location
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.staysafe.maps.api.DirectionsMode
import com.example.staysafe.maps.api.DirectionsResult
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

class JourneyViewModel(application: Application,private val repository: JourneyRepository) : AndroidViewModel(application) {

    private val _journey = MutableStateFlow<Journey?>(null)
    val journey: StateFlow<Journey?> = _journey

    private val _currentLocation = MutableStateFlow<LatLng?>(null)
    val currentLocation: StateFlow<LatLng?> = _currentLocation

    private val _isTracking = MutableStateFlow(false)
    val isTracking: StateFlow<Boolean> = _isTracking

    private val _destinationReached = MutableStateFlow(false)
    val destinationReached: StateFlow<Boolean> = _destinationReached

    private val _journeyProgress = MutableStateFlow(0f)
    val journeyProgress: StateFlow<Float> = _journeyProgress

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _directionsMode = MutableStateFlow(DirectionsMode.DRIVING)
    val directionsMode: StateFlow<DirectionsMode> = _directionsMode

    private val _estimatedTimeOfArrival = MutableStateFlow<String>("Calculating...")
    val estimatedTimeOfArrival: StateFlow<String> = _estimatedTimeOfArrival

    val startCoordinates = MutableStateFlow<LatLng?>(null)
    val endCoordinates = MutableStateFlow<LatLng?>(null)
    val routePoints = MutableStateFlow<List<LatLng>?>(null)

    private val fusedLocationClient: FusedLocationProviderClient = LocationServices
        .getFusedLocationProviderClient(application)

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { location ->
                updateLocation(location)
            }
        }
    }

    fun setJourney(journey: Journey) {
        _journey.value = journey

        // Reset tracking state
        _destinationReached.value = false
        _journeyProgress.value = 0f
        _estimatedTimeOfArrival.value = "Calculating..."

        viewModelScope.launch {
            _isLoading.value = true

            try {
                val startLatLng = repository.geocodeAddress(journey.startDestination)
                startCoordinates.value = startLatLng

                val endLatLng = repository.geocodeAddress(journey.endDestination)
                endCoordinates.value = endLatLng

                if (startLatLng != null && endLatLng != null) {
                    fetchDirections(startLatLng, endLatLng)
                }
            } catch (e: Exception) {
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setDirectionsMode(mode: DirectionsMode) {
        if (_directionsMode.value != mode) {
            _directionsMode.value = mode

            startCoordinates.value?.let { start ->
                endCoordinates.value?.let { end ->
                    viewModelScope.launch {
                        fetchDirections(start, end)
                    }
                }
            }
        }
    }

    private suspend fun fetchDirections(start: LatLng, end: LatLng) {
        try {
            _isLoading.value = true

            val result = repository.getDirections(
                origin = start,
                destination = end,
                mode = _directionsMode.value
            )

            processDirectionsResult(result)
        } catch (e: Exception) {
            // Handle error
        } finally {
            _isLoading.value = false
        }
    }

    private fun processDirectionsResult(result: DirectionsResult) {
        routePoints.value = result.route

        if (result.durationSeconds > 0) {
            val eta = calculateEta(result.durationSeconds)
            _estimatedTimeOfArrival.value = eta

            viewModelScope.launch {
                startJourneyProgressTracking(result.durationSeconds)
            }
        }
    }

    private fun calculateEta(durationSeconds: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, durationSeconds)
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun startJourneyProgressTracking(totalDurationSeconds: Int) {
        val startTime = System.currentTimeMillis()

        viewModelScope.launch {
            while (_isTracking.value) {
                val elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000
                val progress = (elapsedSeconds.toFloat() / totalDurationSeconds).coerceIn(0f, 1f)
                _journeyProgress.value = progress

                if (progress >= 0.98f) {
                    _destinationReached.value = true
                    break
                }

                kotlinx.coroutines.delay(1000)
            }
        }
    }

    fun startTracking() {
        if (_isTracking.value) return

        _isTracking.value = true

        val locationRequest = LocationRequest.create().apply {
            interval = 5000
            fastestInterval = 2000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
        }
    }

    private fun updateLocation(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        _currentLocation.value = latLng

        endCoordinates.value?.let { destination ->
            val results = FloatArray(1)
            Location.distanceBetween(
                latLng.latitude, latLng.longitude,
                destination.latitude, destination.longitude,
                results
            )

            if (results[0] < 50) {
                _destinationReached.value = true
            }
        }
    }

    fun stopTracking() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        _isTracking.value = false
    }

    fun acknowledgeDestinationReached() {
        _destinationReached.value = false
    }

    fun sendEmergencyAlert() {
        viewModelScope.launch {
            _journey.value?.let { journey ->
                val currentLocationStr = _currentLocation.value?.let {
                    "lat: ${it.latitude}, lng: ${it.longitude}"
                } ?: "Unknown location"

                val message = "EMERGENCY ALERT! " +
                        "I need help at my current location: $currentLocationStr. " +
                        "I was traveling from ${journey.startDestination} to ${journey.endDestination}."

                repository.sendEmergencyMessage(journey.emergencyContactPhone, message)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopTracking()
    }

    companion object{
        class JourneyViewModelFactory(
            private val application: Application,
            private val repository: JourneyRepository
        ) : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(JourneyViewModel::class.java)) {
                    return JourneyViewModel(application, repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
