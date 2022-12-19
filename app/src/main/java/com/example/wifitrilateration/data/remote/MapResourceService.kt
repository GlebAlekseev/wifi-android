package com.example.wifitrilateration.data.remote

import retrofit2.Call
import retrofit2.http.*

interface MapResourceService {
    @GET("get_map_geojson")
    fun getMapGeoJson(): Call<String>
}