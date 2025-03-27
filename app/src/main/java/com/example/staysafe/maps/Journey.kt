package com.example.staysafe.maps


data class Journey(
    val startDestination: String,
    val endDestination: String,
    val emergencyContactName: String,
    val emergencyContactPhone: String,
    val estimatedDurationMinutes: Int
)