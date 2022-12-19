package com.example.wifitrilateration.data.repository

import com.example.wifitrilateration.data.framework.RouterRssiManager
import com.example.wifitrilateration.data.framework.entity.RouterRssi
import com.example.wifitrilateration.data.preferences.SharedPreferencesSynchronizedStorage
import com.example.wifitrilateration.data.preferences.SharedPreferencesUserStorage
import com.example.wifitrilateration.data.remote.AuthService
import com.example.wifitrilateration.data.remote.model.AuthResponse
import com.example.wifitrilateration.domain.entity.Result
import com.example.wifitrilateration.domain.entity.ResultStatus
import com.example.wifitrilateration.domain.repository.AuthRepository
import com.example.wifitrilateration.domain.repository.RSSIManagerRepository
import com.glebalekseevjk.yasmrhomework.data.local.dao.RouterDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.Response
import javax.inject.Inject


class RSSIManagerRepositoryImpl @Inject constructor(
    private val rssiManager: RouterRssiManager
) : RSSIManagerRepository {
    override fun getRSSIDevices(): Flow<Result<List<RouterRssi>>> = rssiManager.getRouterRssiList().map { Result(status = ResultStatus.SUCCESS,it,) }
}