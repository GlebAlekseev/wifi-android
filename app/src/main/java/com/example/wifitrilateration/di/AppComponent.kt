package com.example.wifitrilateration.di

import android.content.Context
import com.example.wifitrilateration.MainApplication
import com.example.wifitrilateration.data.worker.CheckSynchronizeWorker
import com.example.wifitrilateration.data.worker.SendMyLocationWorker
import com.example.wifitrilateration.di.module.LocalStorageModule
import com.example.wifitrilateration.di.module.RemoteStorageModule
import com.example.wifitrilateration.di.module.RepositoryModule
import dagger.BindsInstance
import dagger.Component

@Component(modules = [RepositoryModule::class, LocalStorageModule::class, RemoteStorageModule::class])
interface AppComponent {
    fun injectMainApplication(application: MainApplication)
    fun injectSendMyLocationWorker(sendMyLocationWorker: SendMyLocationWorker)
    fun injectCheckSynchronizeWorker(synchronizeWorker: CheckSynchronizeWorker)
    fun createMapsFragmentSubcomponent(): MapsFragmentSubcomponent
    fun createMainActivitySubcomponent(): MainActivitySubcomponent
    fun createRouterConfigFragmentSubComponent(): RoutersConfigFragmentSubcomponent
    fun createRoutersNowFragmentSubcomponent(): RoutersNowFragmentSubcomponent
    @Component.Factory
    interface Builder {
        fun create(@BindsInstance context: Context): AppComponent
    }
}