package com.example.staysafe.maps.api

import retrofit2.http.GET
import retrofit2.http.Query

interface DirectionsApiService {
    @GET("maps/api/directions/json")
    suspend fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("mode") mode: String,
        @Query("key") apiKey: String = API_KEY
    ): DirectionsResponse

    companion object {
        private const val API_KEY = "AIzaSyDWjDkVnkIijqy9HBKnmSUxK0M39x7no6Y"
    }
}