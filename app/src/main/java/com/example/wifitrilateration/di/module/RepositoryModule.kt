package com.example.wifitrilateration.di.module

import com.example.wifitrilateration.data.repository.*
import com.example.wifitrilateration.domain.repository.*
import com.example.wifitrilateration.ui.viewmodel.RoutersConfigViewModel
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {
    @Binds
    fun bindLocalLocationRepository(locationRepositoryImpl: LocalLocationRepositoryImpl): LocalLocationRepository
    @Binds
    fun bindRemoteLocationRepository(remoteLocationRepositoryImpl: RemoteLocationRepositoryImpl): RemoteLocationRepository
    @Binds
    fun bindMapObjectsRepository(mapObjectsRepositoryImpl: MapObjectsRepositoryImpl): MapObjectsRepository
    @Binds
    fun bindBroadcastReceiverRepository(broadcastReceiverRepositoryImpl: BroadcastReceiverRepositoryImpl): BroadcastReceiverRepository
    @Binds
    fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository
    @Binds
    fun bindRouterConfigurationRepository(remoteRouterConfigurationRepositoryImpl: RemoteRouterConfigurationRepositoryImpl): RemoteRouterConfigurationRepository
    @Binds
    fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    fun bindLocalRouterConfigurationRepositoryImpl(routerConfigurationRepositoryImpl: LocalRouterConfigurationRepositoryImpl): LocalRouterConfigurationRepository

    @Binds
    fun bindRSSIManagerRepositoryImpl(rssiManagerRepositoryImpl: RSSIManagerRepositoryImpl): RSSIManagerRepository

}
