package com.example.wifitrilateration.data.remote

import com.example.wifitrilateration.data.remote.model.RouterPositionResponse
import com.example.wifitrilateration.domain.entity.Router
import retrofit2.Call
import retrofit2.http.*

interface RouterConfigurationService {
    @POST("set_routers_position")
    fun setRoutersPosition(@Body listRouter: List<Router>): Call<Void>

    @GET("get_routers_position")
    fun getRoutersPosition(): Call<List<RouterPositionResponse>>
}