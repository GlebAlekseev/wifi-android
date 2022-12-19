package com.example.wifitrilateration.data.repository

import com.example.wifitrilateration.data.remote.UserPositionService
import com.example.wifitrilateration.domain.entity.*
import com.example.wifitrilateration.domain.repository.RemoteLocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


// Класс отвечает за отправку/получение локации других пользователей
class RemoteLocationRepositoryImpl @Inject constructor(
    private val userPositionService: UserPositionService
) : RemoteLocationRepository {

    override fun getLocations(): Flow<Result<Pair<List<UserLocation>,List<UserLocation>>>>  = flow {
        emit(Result(ResultStatus.LOADING, Pair(emptyList(),emptyList())))
        val locationsResponse = runCatching {
            userPositionService.getPositions().execute()
        }.getOrNull()
        var message = ""
        var status = ResultStatus.SUCCESS
        var data: Pair<List<UserLocation>,List<UserLocation>> = Pair(emptyList(), emptyList())
        locationsResponse ?: run { message = "Нет соединения" }
        locationsResponse?.code().let {
            when (it) {
                200 -> {
                    val allUsers = if (locationsResponse?.body()?.allUsers != null){
                        locationsResponse.body()!!.allUsers!!.map { UserLocation(
                            it.lat,
                            it.lng,
                            it.level,
                            User(
                                it.userId,
                                it.displayName,
                                it.group,
                            )
                        ) }
                    }else emptyList()
                    val groupUsers = if (locationsResponse?.body()?.groupUsers != null){
                        locationsResponse.body()!!.groupUsers.map { UserLocation(
                            it.lat,
                            it.lng,
                            it.level,
                            User(
                               it.userId,
                               it.displayName,
                               it.group,
                            )
                        ) }
                    }else emptyList()
                    data = Pair(allUsers,groupUsers)
                }
                400 -> {
                    message = "Ошибка клиента"
                }
                401 -> {
                    message = "Не авторизован"
                    status = ResultStatus.UNAUTHORIZED
                }
                500 -> {
                    message = "Ошибка сервера"
                }
                else -> {
                    message = "Неизвестная ошибка"
                }
            }
        }
        emit(Result(status,data,message))
    }.flowOn(Dispatchers.IO)

    override fun sendMyLocation(userLocation: UserLocation): Flow<Result<Unit>> = flow {
        emit(Result(ResultStatus.LOADING,Unit))
        val locationsResponse = runCatching {
            userPositionService.sendPosition(FeatureLatLng(userLocation.lat,userLocation.lng)).execute()
        }.getOrNull()
        var message = ""
        var status = ResultStatus.SUCCESS
        locationsResponse ?: run { message = "Нет соединения" }
        locationsResponse?.code().let {
            when (it) {
                200 -> {
                }
                400 -> {
                    message = "Ошибка клиента"
                }
                401 -> {
                    message = "Не авторизован"
                    status = ResultStatus.UNAUTHORIZED
                }
                500 -> {
                    message = "Ошибка сервера"
                }
                else -> {
                    message = "Неизвестная ошибка"
                }
            }
        }
        emit(Result(status,Unit,message))
    }.flowOn(Dispatchers.IO)
}