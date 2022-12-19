object WifiCoordinator {
    fun calcDistance(rssi: Double): Double {
        val base = 10.0
        val exponent = -(rssi + 51.504) / 16.532
        return 730.24198315 + 52.33325511 * rssi + 1.35152407 * Math.pow(rssi, 2.0) + 0.01481265 * Math.pow(
            rssi,
            3.0
        ) + 0.00005900 * Math.pow(rssi, 4.0) + 0.00541703 * 180
    }

    fun calFeetToMeter(rssi: Double): Double {
        return rssi * 0.3048
    }

    fun calDistToDeg(dist: Double): Double {
        val result: Double
        val DistToDeg: Double
        val lat = 42
        val EarthRadius = 6367449.0
        val a = 6378137.0
        val b = 6356752.3
        val ang = lat * (Math.PI / 180)

        DistToDeg = 82602.89223259855
        result = dist / DistToDeg
        return result
    }

    fun getLongitude(
        Lat1: Double, Long1: Double, rssi1: Double,
        Lat2: Double, Long2: Double, rssi2: Double,
        Lat3: Double, Long3: Double, rssi3: Double
    ): Double {
        val dist1: Double
        val dist2: Double
        val dist3: Double
        var MyLat: Double
        val MyLong: Double
        dist1 = calDistToDeg(10.0)
        dist2 = calDistToDeg(12.0)
        dist3 = calDistToDeg(8.0)
        MyLong = ((2 * (Lat3 - Lat1) * (Math.pow(dist2, 2.0) - Math.pow(dist1, 2.0))
                - 2 * (Lat2 - Lat1) * (Math.pow(dist3, 2.0) - Math.pow(dist1, 2.0)))
                / (4 * (Lat2 - Lat1) * (Long3 - Long1) - 4 * (Lat3 - Lat1) * (Long2 - Long1)))
        return MyLong
    }

    fun getLatitude(
        Lat1: Double, Long1: Double, rssi1: Double,
        Lat2: Double, Long2: Double, rssi2: Double,
        Lat3: Double, Long3: Double, rssi3: Double
    ): Double {
        val magnitude = 100000000.0
        val dist1: Double
        val dist2: Double
        val dist3: Double
        val MyLat: Double
        var MyLong: Double
        dist1 = calDistToDeg(calcDistance(rssi1))
        dist2 = calDistToDeg(calcDistance(rssi2))
        dist3 = calDistToDeg(calcDistance(rssi3))
        MyLat = ((2 * (Long2 - Long1) * (Math.pow(dist3, 2.0) - Math.pow(dist1, 2.0))
                - 2 * (Long3 - Long1) * (Math.pow(dist2, 2.0) - Math.pow(dist1, 2.0)))
                / (4 * ((Lat2 - Lat1) * (Long3 - Long1) - (Lat3 - Lat1) * (Long2 - Long1))))
        return MyLat
    }

    fun myRotation(x: Double, y: Double, dist: Double, deg: Double): DoubleArray {
        val tmpX: Double
        val tmpY: Double
        val myLocation = DoubleArray(3)
        tmpX = x * Math.cos(Math.PI / 180 * deg) - y * Math.sin(Math.PI / 180 * deg)
        tmpY = x * Math.sin(Math.PI / 180 * deg) + y * Math.cos(Math.PI / 180 * deg)

        myLocation[0] = tmpX
        myLocation[1] = tmpY
        myLocation[2] = dist
        return myLocation
    }

    fun MyTrilateration(
        Lat1: Double, Long1: Double, rssi1: Double,
        Lat2: Double, Long2: Double, rssi2: Double,
        Lat3: Double, Long3: Double, rssi3: Double
    ): DoubleArray {

        val tmpWAP1 = DoubleArray(3)
        var tmpWAP2 = DoubleArray(3)
        var tmpWAP3 = DoubleArray(3)
        val dist1: Double
        val dist2: Double
        val dist3: Double
        val tmpLat2: Double
        val tmpLong2: Double
        val tmpLat3: Double
        val tmpLong3: Double
        val tmpSlide: Double
        var deg: Double
        val MyLat: Double
        val MyLong: Double
        var MyLocation = DoubleArray(2)

        dist1 = calDistToDeg(calFeetToMeter(calcDistance(rssi1)))
        dist2 = calDistToDeg(calFeetToMeter(calcDistance(rssi2)))
        dist3 = calDistToDeg(calFeetToMeter(calcDistance(rssi3)))

        tmpLat2 = Lat2 - Lat1
        tmpLong2 = Long2 - Long1
        tmpLat3 = Lat3 - Lat1
        tmpLong3 = Long3 - Long1
        tmpSlide = Math.sqrt(Math.pow(tmpLat2, 2.0) + Math.pow(tmpLong2, 2.0))

        deg = 180 / Math.PI * Math.acos(Math.abs(tmpLat2) / Math.abs(tmpSlide))

        if (tmpLat2 > 0 && tmpLong2 > 0) {
            deg = 360 - deg
        } else if (tmpLat2 < 0 && tmpLong2 > 0) {
            deg = 180 + deg
        } else if (tmpLat2 < 0 && tmpLong2 < 0) {
            deg = 180 - deg
        } else if (tmpLat2 > 0 && tmpLong2 < 0) {
            deg = deg
        }
        tmpWAP1[0] = 0.0
        tmpWAP1[1] = 0.0
        tmpWAP1[2] = dist1
        tmpWAP2 = myRotation(tmpLat2, tmpLong2, dist2, deg)
        tmpWAP3 = myRotation(tmpLat3, tmpLong3, dist3, deg)
        MyLat = (Math.pow(tmpWAP1[2], 2.0) - Math.pow(tmpWAP2[2], 2.0) + Math.pow(tmpWAP2[0], 2.0)) / (2 * tmpWAP2[0])
        MyLong = (Math.pow(tmpWAP1[2], 2.0) - Math.pow(tmpWAP3[2], 2.0) - Math.pow(MyLat, 2.0) + Math.pow(
            MyLat - tmpWAP3[0],
            2.0
        ) + Math.pow(
            tmpWAP3[1], 2.0
        )) / (2 * tmpWAP3[1])
        MyLocation = myRotation(MyLat, MyLong, 0.0, -deg)
        MyLocation[0] = MyLocation[0] + Lat1
        MyLocation[1] = MyLocation[1] + Long1
        return MyLocation
    }
}