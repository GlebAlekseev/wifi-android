package com.example.wifitrilateration.domain.entity


data class Router(
    val bssid: String,
    val level: Int,
    val coordinates: FeatureLatLng
)