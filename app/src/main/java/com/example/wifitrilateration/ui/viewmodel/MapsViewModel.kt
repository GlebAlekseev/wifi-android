package com.example.wifitrilateration.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.wifitrilateration.domain.entity.IFeature
import com.example.wifitrilateration.domain.entity.Result
import com.example.wifitrilateration.domain.entity.UserLocation
import com.example.wifitrilateration.domain.interactor.BroadcastReceiverUseCase
import com.example.wifitrilateration.domain.interactor.LocationUseCase
import com.example.wifitrilateration.domain.interactor.MapObjectsUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import javax.inject.Inject

class MapsViewModel @Inject constructor(
    private val mapObjectsUseCase: MapObjectsUseCase,
    private val locationUseCase: LocationUseCase,
    private val broadcastReceiverUseCase: BroadcastReceiverUseCase,
) : BaseViewModel() {
    override val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, exception ->
            val message = exception.message ?: "Неизвестная ошибка"
            _errorHandler.value = message
        }

    private var localLocationJob: Job? = null
    private var remoteLocationJob: Job? = null
    private var permissionJob: Job? = null

    fun getMapObjects(fileName: String): List<IFeature> {
        return mapObjectsUseCase.getMapObjects(fileName)
    }

    fun observeMyLocation(block: (Result<List<UserLocation>>) -> Unit) {
        localLocationJob = viewModelScope.launchWithExceptionHandler {
            locationUseCase.getMyLocation().collect { result ->
                block.invoke(result)
            }
        }
    }

    fun observeLocations(block: (Result<Pair<List<UserLocation>,List<UserLocation>>>) -> Unit) {
        remoteLocationJob = viewModelScope.launchWithExceptionHandler {
            locationUseCase.getLocations().collect { result ->
                block.invoke(result)
            }
        }
    }

    fun unregisterReceivers(){
        broadcastReceiverUseCase.unregisterReceivers()
    }

    override fun onCleared() {
        super.onCleared()
        localLocationJob?.cancel()
        remoteLocationJob?.cancel()
        permissionJob?.cancel()
    }
}