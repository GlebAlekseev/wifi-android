package com.example.wifitrilateration

import android.app.Application
import com.example.wifitrilateration.data.repository.SchedulerRepositoryImpl
import com.example.wifitrilateration.di.AppComponent
import com.example.wifitrilateration.di.DaggerAppComponent
import javax.inject.Inject

class MainApplication : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    @Inject
    lateinit var schedulerRepositoryImpl: SchedulerRepositoryImpl

    override fun onCreate() {
        appComponent.injectMainApplication(this)
        super.onCreate()
        setupWorkers()
    }

    private fun setupWorkers() {
        schedulerRepositoryImpl.setupPeriodicTimeSendMyLocation()
    }
}