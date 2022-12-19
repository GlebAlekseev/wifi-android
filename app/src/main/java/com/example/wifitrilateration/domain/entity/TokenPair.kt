package com.example.wifitrilateration.domain.entity

data class TokenPair(
    val accessToken: String,
    val refreshToken: String,
    val expiresAt: Long,
    val userId: Long,
    val displayName: String,
    val role: String,
    val group: String
    )