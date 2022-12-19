package com.example.wifitrilateration.data.remote.model

import com.example.wifitrilateration.domain.entity.TokenPair

data class MapGeoJsonResponse(
    val message: String,
    val data: TokenPair?
)