package com.example.wifitrilateration.utils

object Trilateration {
    fun executeTrilateration(
        lat1: Double, lng1: Double, rssi1: Double,
        lat2: Double, lng2: Double, rssi2: Double,
        lat3: Double, lng3: Double, rssi3: Double
    ): LatLngTrilaterationResult{
        val result = WifiCoordinator.MyTrilateration(
            lat1,
            lng1,
            rssi1,
            lat2,
            lng2,
            rssi2,
            lat3,
            lng3,
            rssi3,
        )
        return LatLngTrilaterationResult(result[0],result[1])
    }
    data class LatLngTrilaterationResult(
        val lat: Double,
        val lng: Double,
    )
}