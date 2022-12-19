package com.example.wifitrilateration.domain.entity

data class UserLocation(
    val lat: Double,
    val lng: Double,
    val level: Int,
    val user: User
)