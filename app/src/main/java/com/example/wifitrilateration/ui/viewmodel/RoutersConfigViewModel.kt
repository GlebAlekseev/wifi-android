package com.example.wifitrilateration.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.wifitrilateration.domain.entity.*
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.inject.Inject
import com.example.wifitrilateration.domain.interactor.RouterConfigurationUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first

import com.example.wifitrilateration.domain.entity.Result

class RoutersConfigViewModel @Inject constructor(
    private val routerConfigurationUseCase: RouterConfigurationUseCase
) : BaseViewModel() {
    override val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, exception ->
            val message = exception.message ?: "Неизвестная ошибка"
            _errorHandler.value = message
        }

    private var getRouterConfigListJob: Job? = null

    private var _routerConfigViewState = MutableStateFlow(RouterConfigurationtViewState.PLUG)
    val routerConfigViewState: StateFlow<RouterConfigurationtViewState> by lazy {
        observeRouterConfiguration()
        _routerConfigViewState
    }

    fun observeRouterConfiguration() {
        getRouterConfigListJob = viewModelScope.launchWithExceptionHandler {
            routerConfigurationUseCase.getRouterConfigurationLocal().collect { result ->
                _routerConfigViewState.value = _routerConfigViewState.value.copy(
                    result = Result(
                        result.status,
                        result.data,
                        result.message
                    )
                )
            }
        }
        routerConfigurationUseCase.getRouterConfigurationLocal()
    }

    fun deleteRouterConfig(
        router: Router,
        snackBarBlock: suspend (router: Router) -> Boolean,
        block: (Result<Router>) -> Unit
    ) {
        viewModelScope.launchWithExceptionHandler {
            val deleteResult =
                routerConfigurationUseCase.deleteRouterConfiguration(router)
                    .first { it.status != ResultStatus.LOADING }
            val deletedItem = if (deleteResult.status == ResultStatus.SUCCESS) {
                deleteResult.data.first()
            } else {
                throw RuntimeException("БД не может удалить запись")
            }

            if (snackBarBlock(deletedItem)) {
                val addResult =
                    routerConfigurationUseCase.addRouterConfiguration((deletedItem))

                routerConfigurationUseCase.deleteRouterConfiguration(deletedItem)
                    .collect { result ->
                        if (result.status == ResultStatus.SUCCESS) {
                            val response = result.data
                            block.invoke(
                                Result(
                                    result.status,
                                    result.data.first(),
                                    result.message
                                )
                            )
                        } else {
                            block.invoke(
                                Result(
                                    result.status,
                                    Router("", 0, FeatureLatLng(0.0, 0.0)),
                                    result.message
                                )
                            )
                        }
                    }
            }
        }
    }

    fun addTodo(router: Router, block: (Result<Router>) -> Unit) {
        viewModelScope.launchWithExceptionHandler {
            val addResult =
                routerConfigurationUseCase.addRouterConfiguration(router).first { it.status != ResultStatus.LOADING }
            if (addResult.status == ResultStatus.SUCCESS) {
                routerConfigurationUseCase.addRouterConfiguration(router).collect { result ->
                    if (result.status == ResultStatus.SUCCESS) {
                        val response = result.data
                        block.invoke(
                            Result(
                                result.status,
                                result.data.first(),
                                result.message
                            )
                        )
                    }else{
                        block.invoke(Result(result.status, Router("", 0, FeatureLatLng(0.0, 0.0)), result.message))
                    }
                }
            } else {
                throw RuntimeException("БД не может добавить")
            }
        }
    }
}