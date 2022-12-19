package com.example.wifitrilateration.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.wifitrilateration.data.repository.LocalLocationRepositoryImpl
import com.example.wifitrilateration.data.repository.RemoteLocationRepositoryImpl
import com.example.wifitrilateration.domain.entity.ResultStatus.*
import com.example.wifitrilateration.domain.interactor.LocationUseCase
import com.example.wifitrilateration.utils.appComponent
import javax.inject.Inject

// Раз в 30 минут отправляет на сервер координату на основе WIFI трилатерации.
class SendMyLocationWorker(private val appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params) {
    @Inject
    lateinit var locationRepositoryImpl: LocalLocationRepositoryImpl
    @Inject
    lateinit var remoteLocationRepositoryImpl: RemoteLocationRepositoryImpl


    override suspend fun doWork(): Result {
        try {
            appContext.appComponent.injectSendMyLocationWorker(this)
            val locationUseCase = LocationUseCase(
                locationRepositoryImpl,
                remoteLocationRepositoryImpl
            )
            var status = Result.success()
            locationUseCase.getMyLocation().collect{ result ->
                when(result.status){
                    SUCCESS -> {
                        val myLocation = result.data[0]
                        locationUseCase.sendMyLocation(myLocation)
                    }
                    LOADING -> {}
                    else -> {
                        status = Result.failure()
                    }
                }
            }
            return status
        }catch (err: Exception){
            return Result.failure()
        }
    }
    companion object {
        const val WORK_NAME = "SendMyLocationWorker"
    }
}