package com.example.wifitrilateration.data.repository

import com.example.wifitrilateration.data.framework.RouterRssiManager
import com.example.wifitrilateration.domain.repository.BroadcastReceiverRepository
import javax.inject.Inject

// Класс отвечает за отписку всех зрагестрированных в репозитории броадкаст ресиверов.
class BroadcastReceiverRepositoryImpl @Inject constructor(
    private val routerRssiManager: RouterRssiManager
) : BroadcastReceiverRepository {
    override fun unregisterReceivers() {
        routerRssiManager.unregisterReceiver()
    }
}