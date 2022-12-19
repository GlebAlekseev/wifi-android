package com.example.wifitrilateration.data.repository

import com.example.wifitrilateration.data.preferences.SharedPreferencesSynchronizedStorage
import com.example.wifitrilateration.data.preferences.SharedPreferencesUserStorage
import com.example.wifitrilateration.data.remote.AuthService
import com.example.wifitrilateration.data.remote.model.AuthResponse
import com.example.wifitrilateration.domain.entity.Result
import com.example.wifitrilateration.domain.entity.ResultStatus
import com.example.wifitrilateration.domain.repository.AuthRepository
import com.glebalekseevjk.yasmrhomework.data.local.dao.RouterDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val routerDao: RouterDao,
    private val authService: AuthService,
    private val userStorage: SharedPreferencesUserStorage,
    private val synchronizedStorage: SharedPreferencesSynchronizedStorage,
) : AuthRepository {
    override fun getTokenPair(code: String): Flow<Result<Unit>> = flow {
        emit(Result(ResultStatus.LOADING, Unit))
        val authResponse = runCatching {
            authService.getTokenPair(code).execute()
        }.getOrNull()
        val result = getResultFromAuthResponse(authResponse)
        if (result.status == ResultStatus.SUCCESS) {
            userStorage.setTokenPair(authResponse!!.body()!!.data!!)
        }
        emit(result)
    }.flowOn(Dispatchers.IO)

    override fun logout(): Flow<Result<Unit>> = flow {
        emit(Result(ResultStatus.LOADING, Unit))
        var message = ""
        try {
            userStorage.clear()
            synchronizedStorage.setSynchronizedStatus(SharedPreferencesSynchronizedStorage.SYNCHRONIZED)
            routerDao.deleteAll()
        } catch (err: Exception) {
            message = "Ошибка выхода"
        }
        emit(Result(ResultStatus.SUCCESS, Unit, message))
    }.flowOn(Dispatchers.IO)

    private fun getResultFromAuthResponse(authResponse: Response<AuthResponse>?): Result<Unit> {
        var message = ""
        var status = ResultStatus.SUCCESS
        val data = Unit
        authResponse?.body()?.data ?: run {
            message = "Ошибка парсинга ответа"
        }
        authResponse ?: run { message = "Нет соединения" }
        authResponse?.code().let {
            when (it) {
                200 -> {}
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
        if (message != "") {
            status = ResultStatus.FAILURE
        }
        return Result(status, data, message)
    }
}