package com.example.wifitrilateration.ui.framework.map

import com.google.android.gms.maps.model.GroundOverlay
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.Polyline

class MapObjectsHolder {
    var currentLevel: Int = 0
        private set

    private val floorsMapMarker: MutableMap<Int, MutableList<Marker>> = mutableMapOf()
    private val floorsMapGroundOverlay: MutableMap<Int, MutableList<GroundOverlay>> = mutableMapOf()
    private val floorsMapPolyline: MutableMap<Int, MutableList<Polyline>> = mutableMapOf()
    private val floorsMapPolygon: MutableMap<Int, MutableList<Polygon>> = mutableMapOf()

    private val floorsMapNavigator: MutableMap<Int, MutableList<Marker>> = mutableMapOf()


    // add map objects
    fun addMarker(marker: Marker?, level: Int) {
        marker ?: return
        val list = floorsMapMarker[level]
        if (list == null) floorsMapMarker[level] = mutableListOf(marker)
        else list.add(marker)
    }

    fun addGroundOverlay(groundOverlay: GroundOverlay?, level: Int) {
        groundOverlay ?: return
        val list = floorsMapGroundOverlay[level]
        if (list == null) floorsMapGroundOverlay[level] = mutableListOf(groundOverlay)
        else list.add(groundOverlay)
    }

    fun addPolyline(polyline: Polyline?, level: Int) {
        polyline ?: return
        val list = floorsMapPolyline[level]
        if (list == null) floorsMapPolyline[level] = mutableListOf(polyline)
        else list.add(polyline)
    }

    fun addPolygon(polygon: Polygon?, level: Int) {
        polygon ?: return
        val list = floorsMapPolygon[level]
        if (list == null) floorsMapPolygon[level] = mutableListOf(polygon)
        else list.add(polygon)
    }

    // remove map objects
    fun removeMarker(marker: Marker, level: Int) {
        val list = floorsMapMarker[level] ?: return
        val contains = list.contains(marker)
        if (contains) {
            list.remove(marker)
        }
    }

    fun removeGroundOverlay(groundOverlay: GroundOverlay, level: Int) {
        val list = floorsMapGroundOverlay[level] ?: return
        val contains = list.contains(groundOverlay)
        if (contains) {
            list.remove(groundOverlay)
        }
    }

    fun removePolyline(polyline: Polyline, level: Int) {
        val list = floorsMapPolyline[level] ?: return
        val contains = list.contains(polyline)
        if (contains) {
            list.remove(polyline)
        }
    }

    fun removePolygon(polygon: Polygon, level: Int) {
        val list = floorsMapPolygon[level] ?: return
        val contains = list.contains(polygon)
        if (contains) {
            list.remove(polygon)
        }
    }

    fun setVisibleLevel(level: Int) {
        for (i in 0..3) {
            val listMarker = floorsMapMarker[i]
            val listGroundOverlay = floorsMapGroundOverlay[i]
            val listPolyline = floorsMapPolyline[i]
            val listPolygon = floorsMapPolygon[i]
            val floorsNavigator = floorsMapNavigator[i]
            if (i == level) {
                listMarker?.forEach { it.isVisible = true }
                listGroundOverlay?.forEach { it.isVisible = true }
                listPolyline?.forEach { it.isVisible = true }
                listPolygon?.forEach { it.isVisible = true }
                floorsNavigator?.forEach { it.isVisible = true }
            } else {
                listMarker?.forEach { it.isVisible = false }
                listGroundOverlay?.forEach { it.isVisible = false }
                listPolyline?.forEach { it.isVisible = false }
                listPolygon?.forEach { it.isVisible = false }
                floorsNavigator?.forEach { it.isVisible = false }
            }
        }
        currentLevel = level
    }

    //
    fun addNavigator(marker: Marker?, level: Int){
        marker ?: return
        val list = floorsMapNavigator[level]
        if (list == null) floorsMapNavigator[level] = mutableListOf(marker)
        else list.add(marker)
    }

    fun clearNavigators() {
        floorsMapNavigator.clear()
    }




    companion object {
        const val MIN_FLOOR = 0
        const val MAX_FLOOR = 3
    }
}