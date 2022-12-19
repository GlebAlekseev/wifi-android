package com.example.wifitrilateration.domain.repository

import com.example.wifitrilateration.domain.entity.IFeature

interface MapObjectsRepository {
    fun getMapObjects(fileName: String): List<IFeature>
}