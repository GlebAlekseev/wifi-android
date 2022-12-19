package com.example.wifitrilateration.data.repository

import com.example.wifitrilateration.data.jsonparser.FeatureJsonParser
import com.example.wifitrilateration.data.local.AssetsManager
import com.example.wifitrilateration.domain.entity.IFeature
import com.example.wifitrilateration.domain.repository.MapObjectsRepository
import javax.inject.Inject

// Класс отвечает за получение объектов для GoogleMaps
class MapObjectsRepositoryImpl @Inject constructor(
    private val assetsManager: AssetsManager
) : MapObjectsRepository {
    override fun getMapObjects(fileName: String): List<IFeature> {
        val geoJson = assetsManager.getStringFromAsset(fileName)
        return FeatureJsonParser.parseGeoJson(geoJson)
    }
}