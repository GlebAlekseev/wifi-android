package com.example.wifitrilateration.domain.repository

import com.example.wifitrilateration.domain.entity.Router
import kotlinx.coroutines.flow.Flow
import com.example.wifitrilateration.domain.entity.Result


interface LocalRouterConfigurationRepository {
    fun getRouterConfiguration(): Flow<Result<List<Router>>>
    fun addRouterConfiguration(routerConfig: Router): Flow<Result<List<Router>>>
    fun deleteRouterConfiguration(routerConfig: Router): Flow<Result<List<Router>>>
    fun replaceRouterConfiguration(routerConfigList: List<Router>): Flow<Result<List<Router>>>
}