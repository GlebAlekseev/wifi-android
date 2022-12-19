package com.example.wifitrilateration.data.repository

import android.content.Context
import androidx.work.*
import com.example.wifitrilateration.data.worker.SendMyLocationWorker
import com.example.wifitrilateration.domain.repository.SchedulerRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SchedulerRepositoryImpl @Inject constructor(context: Context) : SchedulerRepository {
    private val workManager: WorkManager by lazy {
        WorkManager.getInstance(context)
    }

    override fun setupPeriodicTimeSendMyLocation() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()
        val repeatingRequest = PeriodicWorkRequestBuilder<SendMyLocationWorker>(30, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniquePeriodicWork(
            SendMyLocationWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }
}