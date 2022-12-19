package com.example.wifitrilateration.domain.interactor

import com.example.wifitrilateration.domain.entity.IFeature
import com.example.wifitrilateration.domain.repository.MapObjectsRepository
import javax.inject.Inject

class MapObjectsUseCase @Inject constructor(
    private val mapObjectsRepository: MapObjectsRepository
){
    fun getMapObjects(fileName: String): List<IFeature> {
        return mapObjectsRepository.getMapObjects(fileName)
    }
}