package com.glebalekseevjk.yasmrhomework.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RouterDbModel(
    @PrimaryKey
    @ColumnInfo(name = "bssid") val bssid: String,
    @ColumnInfo(name = "level") val level: Int,
    @ColumnInfo(name = "lat") val lat: Double,
    @ColumnInfo(name = "lng") val lng: Double
)