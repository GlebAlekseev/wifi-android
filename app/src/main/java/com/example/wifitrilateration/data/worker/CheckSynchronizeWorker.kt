package com.example.wifitrilateration.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.wifitrilateration.data.preferences.SharedPreferencesSynchronizedStorage
import com.example.wifitrilateration.data.repository.LocalRouterConfigurationRepositoryImpl
import com.example.wifitrilateration.data.repository.RemoteRouterConfigurationRepositoryImpl
import com.example.wifitrilateration.domain.entity.ResultStatus
import com.example.wifitrilateration.domain.interactor.RouterConfigurationUseCase
import com.example.wifitrilateration.domain.repository.LocalRouterConfigurationRepository
import com.example.wifitrilateration.utils.appComponent
import javax.inject.Inject

class CheckSynchronizeWorker(private val appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {
    @Inject
    lateinit var synchronizedStorage: SharedPreferencesSynchronizedStorage
    @Inject
    lateinit var remoteRouterConfigurationRepositoryImpl: RemoteRouterConfigurationRepositoryImpl
    @Inject
    lateinit var localRouterConfigurationRepositoryImpl: LocalRouterConfigurationRepositoryImpl

    override suspend fun doWork(): Result {
        return try {
            appContext.appComponent.injectCheckSynchronizeWorker(this)
            val routerConfigurationUseCase = RouterConfigurationUseCase(
                localRouterConfigurationRepositoryImpl,
                remoteRouterConfigurationRepositoryImpl
            )
            if (synchronizedStorage.getSynchronizedStatus()) return Result.success()
            var status = Result.success()
            routerConfigurationUseCase.getRouterConfigurationRemote().collect { result ->
                if (result.status == ResultStatus.SUCCESS) {
                    val listRouter = result.data
                    remoteRouterConfigurationRepositoryImpl.sendRouterConfiguration(listRouter).collect{ result ->
                        when(result.status){
                            ResultStatus.SUCCESS -> {}
                            ResultStatus.LOADING -> {}
                            else -> {
                                status = Result.failure()
                            }
                        }
                    }
                }
                if (result.status != ResultStatus.SUCCESS && result.status != ResultStatus.LOADING) {
                    status = Result.failure()
                }
                if (result.status != ResultStatus.LOADING) return@collect
            }
            return status
        } catch (err: Exception) {
            return Result.failure()
        }
    }
    companion object {
        const val WORK_NAME = "CheckSynchronizedWorker"
    }
}