package com.example.wifitrilateration.domain.repository

import kotlinx.coroutines.flow.Flow
import com.example.wifitrilateration.domain.entity.Result

interface AuthRepository {
    fun getTokenPair(code: String): Flow<Result<Unit>>
    fun logout(): Flow<Result<Unit>>
}