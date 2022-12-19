package com.example.wifitrilateration.domain.interactor

import com.example.wifitrilateration.domain.repository.BroadcastReceiverRepository
import javax.inject.Inject

class BroadcastReceiverUseCase @Inject constructor(
    private val broadcastReceiverRepository: BroadcastReceiverRepository,
){
    fun unregisterReceivers() {
        broadcastReceiverRepository.unregisterReceivers()
    }
}