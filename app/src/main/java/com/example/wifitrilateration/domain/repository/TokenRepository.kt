package com.example.wifitrilateration.domain.repository

import com.example.wifitrilateration.domain.entity.TokenPair

interface UserRepository {
    fun getTokenPair(): TokenPair?
    fun setTokenPair(tokenPair: TokenPair)
    fun clear()
    fun getExpiresAt(): Long?
    fun getRefreshToken(): String?
    fun getAccessToken(): String?
    fun getUserId(): Long
    fun getDisplayName(): String?
    fun getRole(): String?
    fun getGroup(): String?
}