package com.example.wifitrilateration.data.remote

import com.example.wifitrilateration.data.remote.model.GroupResponse
import retrofit2.Call
import retrofit2.http.*

interface GroupService {
    @GET("auth/group")
    fun getGroup(): Call<GroupResponse>

    @POST("auth/group")
    fun joinGroup(@Body targetGroup: String): Call<GroupResponse>

    @DELETE("auth/group")
    fun leaveGroup(): Call<GroupResponse>
}