package com.example.wifitrilateration.data.repository

import com.example.wifitrilateration.data.remote.RouterConfigurationService
import com.example.wifitrilateration.domain.entity.*
import com.example.wifitrilateration.domain.repository.RemoteRouterConfigurationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

// Класс отвечает за настройку расположения роутеров
class RemoteRouterConfigurationRepositoryImpl @Inject constructor(
    private val routerConfigurationService: RouterConfigurationService
) : RemoteRouterConfigurationRepository {
    override fun getRouterConfiguration(): Flow<Result<List<Router>>> = flow{
        emit(Result(ResultStatus.LOADING,emptyList()))
        val routersResponse = runCatching {
            routerConfigurationService.getRoutersPosition().execute()
        }.getOrNull()
        var message = ""
        var status = ResultStatus.SUCCESS
        var data: List<Router> = emptyList()
        routersResponse ?: run { message = "Нет соединения" }
        routersResponse?.code().let {
            when (it) {
                200 -> {
                    val routers = routersResponse?.body()!!.map {
                        Router(
                            it.bssid,
                            it.level,
                            FeatureLatLng(it.lat,it.lng)
                        )
                    }
                    data = routers
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

    override fun sendRouterConfiguration(listRouter: List<Router>): Flow<Result<Unit>> = flow {
        emit(Result(ResultStatus.LOADING,Unit))
        val routersResponse = runCatching {
            routerConfigurationService.setRoutersPosition(listRouter).execute()
        }.getOrNull()
        var message = ""
        var status = ResultStatus.SUCCESS
        routersResponse ?: run { message = "Нет соединения" }
        routersResponse?.code().let {
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