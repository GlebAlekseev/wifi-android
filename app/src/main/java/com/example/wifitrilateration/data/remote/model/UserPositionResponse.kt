package com.example.wifitrilateration.data.remote.model

data class UserPositionResponse(
    val allUsers: List<UserResponse>?,
    val groupUsers: List<UserResponse>
)

data class UserResponse(
    val userId: Long,
    val displayName: String,
    val group: String,
    val level: Int,
    val lat: Double,
    val lng: Double,
)