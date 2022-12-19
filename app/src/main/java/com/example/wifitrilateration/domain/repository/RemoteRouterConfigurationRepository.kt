package com.example.wifitrilateration.domain.repository

import com.example.wifitrilateration.domain.entity.Router
import kotlinx.coroutines.flow.Flow
import com.example.wifitrilateration.domain.entity.Result


interface RemoteRouterConfigurationRepository {
    fun getRouterConfiguration(): Flow<Result<List<Router>>>
    fun sendRouterConfiguration(listRouter: List<Router>): Flow<Result<Unit>>
}