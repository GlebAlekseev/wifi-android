package com.example.wifitrilateration.domain.interactor

import com.example.wifitrilateration.domain.entity.Result
import com.example.wifitrilateration.domain.entity.UserLocation
import com.example.wifitrilateration.domain.repository.LocalLocationRepository
import com.example.wifitrilateration.domain.repository.RemoteLocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationUseCase @Inject constructor(
    private val localLocationRepository: LocalLocationRepository,
    private val remoteLocationRepository: RemoteLocationRepository,
){
    fun getMyLocation(): Flow<Result<List<UserLocation>>> {
        return localLocationRepository.getMyLocation()
    }
    fun getLocations(): Flow<Result<Pair<List<UserLocation>, List<UserLocation>>>> {
        return remoteLocationRepository.getLocations()
    }
    fun sendMyLocation(userLocation: UserLocation): Flow<Result<Unit>>{
        return remoteLocationRepository.sendMyLocation(userLocation)
    }

}