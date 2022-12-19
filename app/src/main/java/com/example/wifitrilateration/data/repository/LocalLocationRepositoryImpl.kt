package com.example.wifitrilateration.data.repository

import androidx.lifecycle.asFlow
import com.example.wifitrilateration.data.framework.PermissionManager
import com.example.wifitrilateration.data.framework.RouterRssiManager
import com.example.wifitrilateration.data.framework.entity.RouterRssi
import com.example.wifitrilateration.data.preferences.SharedPreferencesUserStorage
import com.example.wifitrilateration.domain.entity.*
import com.example.wifitrilateration.domain.repository.LocalLocationRepository
import com.example.wifitrilateration.utils.Trilateration
import com.glebalekseevjk.yasmrhomework.data.local.dao.RouterDao
import com.glebalekseevjk.yasmrhomework.data.local.model.RouterDbModel
import com.glebalekseevjk.yasmrhomework.domain.mapper.Mapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

// Класс отвечает за получение объектов для GoogleMaps
class LocalLocationRepositoryImpl @Inject constructor(
    private val routerDao: RouterDao,
    private val mapper: Mapper<Router, RouterDbModel>,
    private val routerRssiManager: RouterRssiManager,
    private val userStorage: SharedPreferencesUserStorage,
    private val permissionManager: PermissionManager
) : LocalLocationRepository {
    override fun getMyLocation(): Flow<Result<List<UserLocation>>> = flow {
        emit(Result(ResultStatus.LOADING, emptyList()))
        try {
            if (!checkPermissions()) {
                emit(Result(ResultStatus.FAILURE, emptyList()))
                return@flow
            }
            val listRouterRssi = routerRssiManager.getRouterRssiList()

            listRouterRssi.combine(
                routerDao.getAll().asFlow()
            ) { listRouterRssi, routerPositionsDb ->
                Pair(listRouterRssi, routerPositionsDb)
            }.collect { (listRouterRssi, routerPositionsDb) ->
                val routerPositionsList = routerPositionsDb.map { mapper.mapDbModelToItem(it) }
                // Первые три, что есть в БД
                val resultList: MutableList<Pair<Router, RouterRssi>> =
                    emptyList<Pair<Router, RouterRssi>>().toMutableList()
                for (routerRssi in listRouterRssi) {
                    if (resultList.size >= 3) break
                    val res = routerPositionsList.filter { it.bssid == routerRssi.bssid }
                    if (!res.isEmpty()) {
                        resultList.add(Pair(res.first(), routerRssi))
                    }
                }
                if (resultList.size < 3) throw RuntimeException("trilateration requires three points")

                val latLng = Trilateration.executeTrilateration(
                    resultList[0].first.coordinates.lat,
                    resultList[0].first.coordinates.lng,
                    resultList[0].second.rssi,
                    resultList[1].first.coordinates.lat,
                    resultList[1].first.coordinates.lng,
                    resultList[1].second.rssi,
                    resultList[2].first.coordinates.lat,
                    resultList[2].first.coordinates.lng,
                    resultList[2].second.rssi,
                )

                val user = User(
                    userStorage.getUserId(),
                    userStorage.getDisplayName() ?: "",
                    userStorage.getGroup() ?: ""
                )


                val userLocation = UserLocation(latLng.lat, latLng.lng, resultList.first().first.level, user)
                emit(Result(ResultStatus.SUCCESS, listOf(userLocation)))
            }
        } catch (err: Exception) {
            emit(Result(ResultStatus.FAILURE, emptyList()))
        }
    }.flowOn(Dispatchers.Default)


    private fun checkPermissions(): Boolean{
        if (!permissionManager.checkPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION))
            return false
        if (!permissionManager.checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION))
            return false
        if (!permissionManager.checkPermission(android.Manifest.permission.ACCESS_WIFI_STATE))
            return false
        if (!permissionManager.checkPermission(android.Manifest.permission.CHANGE_WIFI_STATE))
            return false
        if (!permissionManager.checkPermission(android.Manifest.permission.ACCESS_NETWORK_STATE))
            return false
        return true
    }

}