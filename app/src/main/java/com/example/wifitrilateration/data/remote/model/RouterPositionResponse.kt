package com.example.wifitrilateration.data.remote.model

data class RouterPositionResponse(
    val bssid: String,
    val level: Int,
    val lat: Double,
    val lng: Double,
)