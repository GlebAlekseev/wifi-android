package com.example.wifitrilateration.domain.repository

import com.example.wifitrilateration.domain.entity.Result
import com.example.wifitrilateration.domain.entity.UserLocation
import kotlinx.coroutines.flow.Flow

interface LocalLocationRepository {
    fun getMyLocation(): Flow<Result<List<UserLocation>>>
}