package com.example.wifitrilateration.data.remote.model

data class GroupResponse(
    val message: String,
    val data: List<UserPublic>?
)
data class UserPublic(
    val id: Long,
    val displayName: String,
    val role: String,
    val group: String,
)