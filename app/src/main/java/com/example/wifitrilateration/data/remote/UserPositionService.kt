package com.example.wifitrilateration.data.remote

import com.example.wifitrilateration.data.remote.model.UserPositionResponse
import com.example.wifitrilateration.domain.entity.FeatureLatLng
import retrofit2.Call
import retrofit2.http.*

interface UserPositionService {
    @GET("get_positions")
    fun getPositions(): Call<UserPositionResponse>

    @POST("send_position")
    fun sendPosition(@Body featureLatLng: FeatureLatLng): Call<Void>
}