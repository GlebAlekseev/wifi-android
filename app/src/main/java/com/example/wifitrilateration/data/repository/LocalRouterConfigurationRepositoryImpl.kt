package com.example.wifitrilateration.data.repository

import androidx.lifecycle.asFlow
import com.example.wifitrilateration.domain.entity.*
import com.glebalekseevjk.yasmrhomework.data.local.dao.RouterDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import com.example.wifitrilateration.domain.entity.Result
import com.example.wifitrilateration.domain.repository.LocalRouterConfigurationRepository
import com.glebalekseevjk.yasmrhomework.data.local.model.RouterDbModel
import com.glebalekseevjk.yasmrhomework.domain.mapper.Mapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

// Класс отвечает за настройку расположения роутеров
class LocalRouterConfigurationRepositoryImpl @Inject constructor(
    private val routerDao: RouterDao,
    private val mapper: Mapper<Router, RouterDbModel>
) : LocalRouterConfigurationRepository {
    override fun getRouterConfiguration(): Flow<Result<List<Router>>> = flow{
        emit(Result(ResultStatus.LOADING,emptyList()))
        try {
            routerDao.getAll().asFlow().collect { list ->
                val list = list.map { mapper.mapDbModelToItem(it) }
                emit(Result(ResultStatus.SUCCESS, list))
            }
        } catch (err: Exception) {
            emit(
                Result(
                    ResultStatus.FAILURE,
                    emptyList(),
                    "Неизвестная ошибка"
                )
            )
        }
    }.flowOn(Dispatchers.IO)

    override fun addRouterConfiguration(routerConfig: Router): Flow<Result<List<Router>>> = flow {
        emit(Result(ResultStatus.LOADING,emptyList()))

        try {
            val routerConfigDb = mapper.mapItemToDbModel(routerConfig)
            routerDao.insert(routerConfigDb)
            emit(Result(ResultStatus.SUCCESS, listOf(routerConfig)))
        } catch (err: Exception) {
            emit(
                Result(
                    ResultStatus.FAILURE,
                    emptyList(),
                    "Неизвестная ошибка"
                )
            )
        }

    }.flowOn(Dispatchers.IO)

    override fun deleteRouterConfiguration(routerConfig: Router): Flow<Result<List<Router>>> = flow {
        emit(Result(ResultStatus.LOADING,emptyList<Router>()))
        try {
            routerDao.delete(routerConfig.bssid.toLong())
            emit(Result(ResultStatus.SUCCESS, emptyList()))
        } catch (err: Exception) {
            emit(
                Result(
                    ResultStatus.FAILURE,
                    emptyList(),
                    "Неизвестная ошибка"
                )
            )
        }
    }.flowOn(Dispatchers.IO)

    override fun replaceRouterConfiguration(routerConfigList: List<Router>): Flow<Result<List<Router>>> = flow {
        emit(Result(ResultStatus.LOADING,emptyList()))

        try {
            val todoListDb = routerConfigList.map { mapper.mapItemToDbModel(it) }
            routerDao.replaceAll(todoListDb)
            emit(Result(ResultStatus.SUCCESS, routerConfigList))
        } catch (err: Exception) {
            emit(
                Result(
                    ResultStatus.FAILURE,
                    emptyList(),
                    "Неизвестная ошибка"
                )
            )
        }
    }.flowOn(Dispatchers.IO)


}