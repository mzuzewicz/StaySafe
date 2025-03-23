package com.example.staysafe.maps

import android.content.Context
import android.location.Geocoder
import android.telephony.SmsManager

import com.example.staysafe.maps.api.DirectionsApiService
import com.example.staysafe.maps.api.DirectionsMode
import com.example.staysafe.maps.api.DirectionsResult
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*

class JourneyRepository(
    private val context: Context,
    private val directionsApiService: DirectionsApiService
) {
    private val geocoder = Geocoder(context, Locale.getDefault())

    suspend fun geocodeAddress(address: String): LatLng? = withContext(Dispatchers.IO) {
        try {
            val results = geocoder.getFromLocationName(address, 1)
            results?.firstOrNull()?.let {
                LatLng(it.latitude, it.longitude)
            }
        } catch (e: IOException) {
            null
        }
    }

    suspend fun getDirections(
        origin: LatLng,
        destination: LatLng,
        mode: DirectionsMode
    ): DirectionsResult = withContext(Dispatchers.IO) {
        val response = directionsApiService.getDirections(
            origin = "${origin.latitude},${origin.longitude}",
            destination = "${destination.latitude},${destination.longitude}",
            mode = mode.name.lowercase()
        )

        val route = response.routes.firstOrNull()?.let { route ->
            val overview = route.overviewPolyline.points
            val points = PolyUtil.decode(overview)

            val leg = route.legs.firstOrNull()
            val distance = leg?.distance?.value ?: 0
            val duration = leg?.duration?.value ?: 0

            DirectionsResult(
                route = points,
                distanceMeters = distance,
                durationSeconds = duration,
                encodedPolyline = overview
            )
        } ?: DirectionsResult(
            route = listOf(origin, destination),
            distanceMeters = 0,
            durationSeconds = 0,
            encodedPolyline = ""
        )

        route
    }

    suspend fun sendEmergencyMessage(phoneNumber: String, message: String) = withContext(Dispatchers.IO) {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            true
        } catch (e: Exception) {
            false
        }
    }
}
