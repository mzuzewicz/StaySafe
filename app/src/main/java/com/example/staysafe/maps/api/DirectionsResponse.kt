package com.example.staysafe.maps.api

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName

data class DirectionsResponse(
    val routes: List<Route>,
    val status: String
)

data class Route(
    @SerializedName("overview_polyline")
    val overviewPolyline: Polyline,
    val legs: List<Leg>
)

data class Polyline(
    val points: String
)

data class Leg(
    val distance: Distance,
    val duration: Duration,
    val steps: List<Step>
)

data class Distance(
    val text: String,
    val value: Int
)

data class Duration(
    val text: String,
    val value: Int
)

data class Step(
    val polyline: Polyline,
    val distance: Distance,
    val duration: Duration
)

enum class DirectionsMode {
    DRIVING,
    WALKING,
    BICYCLING,
    TRANSIT
}
data class DirectionsResult(
    val route: List<LatLng>,
    val distanceMeters: Int,
    val durationSeconds: Int,
    val encodedPolyline: String
)

