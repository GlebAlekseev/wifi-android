package com.example.wifitrilateration.domain.interactor

import com.example.wifitrilateration.data.framework.entity.RouterRssi
import com.example.wifitrilateration.domain.entity.Result
import javax.inject.Inject
import com.example.wifitrilateration.domain.entity.TokenPair
import com.example.wifitrilateration.domain.repository.RSSIManagerRepository
import com.example.wifitrilateration.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class RSSIManagerUseCase @Inject constructor(
    private val rssiManagerRepository: RSSIManagerRepository
){
    fun getRSSIDevices(): Flow<Result<List<RouterRssi>>> = rssiManagerRepository.getRSSIDevices()
}