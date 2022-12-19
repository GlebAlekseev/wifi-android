package com.example.wifitrilateration.domain.repository

import com.example.wifitrilateration.data.framework.entity.RouterRssi
import kotlinx.coroutines.flow.Flow
import com.example.wifitrilateration.domain.entity.Result

interface RSSIManagerRepository {
    fun getRSSIDevices(): Flow<Result<List<RouterRssi>>>
}