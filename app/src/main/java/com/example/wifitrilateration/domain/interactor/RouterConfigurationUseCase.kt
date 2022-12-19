package com.example.wifitrilateration.domain.interactor

import com.example.wifitrilateration.domain.entity.Router
import com.example.wifitrilateration.domain.repository.RemoteRouterConfigurationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.example.wifitrilateration.domain.entity.Result
import com.example.wifitrilateration.domain.repository.LocalRouterConfigurationRepository

class RouterConfigurationUseCase @Inject constructor(
    private val localRouterConfigurationRepository: LocalRouterConfigurationRepository,
    private val remoteRouterConfigurationRepository: RemoteRouterConfigurationRepository
){
    fun getRouterConfigurationRemote(): Flow<Result<List<Router>>> {
        return remoteRouterConfigurationRepository.getRouterConfiguration()
    }
    fun sendRouterConfigurationRemote(listRouter: List<Router>): Flow<Result<Unit>> {
        return remoteRouterConfigurationRepository.sendRouterConfiguration(listRouter)
    }


    fun getRouterConfigurationLocal(): Flow<Result<List<Router>>> {
        return localRouterConfigurationRepository.getRouterConfiguration()
    }

    fun replaceRouterConfiguration(routerConfigList: List<Router>): Flow<Result<List<Router>>> {
        return localRouterConfigurationRepository.replaceRouterConfiguration(routerConfigList)
    }

    fun addRouterConfiguration(routerConfig: Router): Flow<Result<List<Router>>> {
        return localRouterConfigurationRepository.addRouterConfiguration(routerConfig)
    }
    fun deleteRouterConfiguration(routerConfig: Router): Flow<Result<List<Router>>> {
        return localRouterConfigurationRepository.deleteRouterConfiguration(routerConfig)
    }
}