package com.example.wifitrilateration.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.wifitrilateration.data.framework.entity.RouterRssi
import com.example.wifitrilateration.domain.entity.*
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.inject.Inject
import com.example.wifitrilateration.domain.interactor.RouterConfigurationUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first

import com.example.wifitrilateration.domain.entity.Result
import com.example.wifitrilateration.domain.interactor.RSSIManagerUseCase

class RoutersNowViewModel @Inject constructor(
    private val rssiManagerUseCase: RSSIManagerUseCase
) : BaseViewModel() {
    override val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, exception ->
            val message = exception.message ?: "Неизвестная ошибка"
            _errorHandler.value = message
        }


    fun observeRSSIDevices(block: (Result<List<RouterRssi>>) -> Unit) {
        viewModelScope.launchWithExceptionHandler {
            rssiManagerUseCase.getRSSIDevices().collect {
                block.invoke(it)
            }
        }
    }
}