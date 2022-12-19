package com.example.wifitrilateration.domain.repository

import com.example.wifitrilateration.domain.entity.Result
import com.example.wifitrilateration.domain.entity.UserLocation
import kotlinx.coroutines.flow.Flow

interface RemoteLocationRepository {
    fun getLocations(): Flow<Result<Pair<List<UserLocation>,List<UserLocation>>>>
    fun sendMyLocation(userLocation: UserLocation): Flow<Result<Unit>>
}