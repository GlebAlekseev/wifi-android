package com.example.wifitrilateration.ui.framework.map

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import com.example.wifitrilateration.domain.entity.*
import com.example.wifitrilateration.utils.getResourceColor
import com.glebalekseevjk.wifitrilateration.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.maps.android.ui.IconGenerator

class MapObjectsLoader(
    private val context: Context,
    private val googleMap: GoogleMap,
    private val mapObjectsHolder: MapObjectsHolder
) {
    fun loadObjects(mapObjects: List<IFeature>) {
        mapObjects.forEach { feature ->
            when (feature) {
                is PointFeature -> {
                    loadPointFeature(feature)
                }
                is LineStringFeature -> {
                    loadLineStringFeature(feature)
                }
                is PolygonFeature -> {
                    loadPolygonFeature(feature)
                }
            }
        }
    }

    private fun loadPointFeature(feature: PointFeature) {
        when (feature.typeElement) {
            PointFeatureType.MAIN_DOOR -> {
                val iconDrawable =
                    AppCompatResources.getDrawable(context, R.drawable.ic_baseline_exit_24)
                        ?: return
                val marker = googleMap.addMarker(
                    MarkerOptions()
                        .position(
                            LatLng(
                                feature.coordinates.lng,
                                feature.coordinates.lat
                            )
                        )
                        .icon(BitmapDescriptorFactory.fromBitmap(iconDrawable.toBitmap(35, 35)))
                )
                mapObjectsHolder.addMarker(marker, feature.level)
            }
            PointFeatureType.EMERGENCY_DOOR -> {
                val iconDrawable =
                    AppCompatResources.getDrawable(context, R.drawable.ic_baseline_exit_24)
                        ?: return
                val marker = googleMap.addMarker(
                    MarkerOptions()
                        .position(
                            LatLng(
                                feature.coordinates.lng,
                                feature.coordinates.lat
                            )
                        )
                        .icon(BitmapDescriptorFactory.fromBitmap(iconDrawable.toBitmap(35, 35)))
                )
                mapObjectsHolder.addMarker(marker, feature.level)
            }
            PointFeatureType.DOOR -> {
                val iconDrawable =
                    AppCompatResources.getDrawable(context, R.drawable.ic_baseline_exit_24)
                        ?: return
                val marker = googleMap.addMarker(
                    MarkerOptions()
                        .position(
                            LatLng(
                                feature.coordinates.lng,
                                feature.coordinates.lat
                            )
                        )
                        .icon(BitmapDescriptorFactory.fromBitmap(iconDrawable.toBitmap(35, 35)))
                )
                mapObjectsHolder.addMarker(marker, feature.level)
            }
            PointFeatureType.HELPER_TEXT -> {
                if (feature.name == "") return
                val iconGenerator = IconGenerator(context)
                iconGenerator.setContentPadding(0, 0, 0, 0)
                iconGenerator.setBackground(null)
                iconGenerator.setTextAppearance(R.style.helper_text)
                val bitmap = iconGenerator.makeIcon(feature.name)
                val groundOverlay = googleMap.addGroundOverlay(
                    GroundOverlayOptions()
                        .zIndex(150f)
                        .position(
                            LatLng(
                                feature.coordinates.lng,
                                feature.coordinates.lat
                            ), bitmap.width.toFloat() / 20
                        )
                        .bearing(65f)
                        .image(BitmapDescriptorFactory.fromBitmap(bitmap))
                )
                mapObjectsHolder.addGroundOverlay(groundOverlay, feature.level)
            }
        }
    }

    private fun loadLineStringFeature(feature: LineStringFeature) {
        val listLatLng = feature.coordinates.map { LatLng(it.lng, it.lat) }
        val polylineOptions = PolylineOptions()
            .add(*listLatLng.toTypedArray())

        when (feature.typeElement) {
            LineStringFeatureType.LADDER -> {
                polylineOptions.width(2f)
                polylineOptions.color(context.getResourceColor(R.color.linestring_ladder))
            }
            LineStringFeatureType.WALL -> {
                polylineOptions.width(4f)
                polylineOptions.color(context.getResourceColor(R.color.linestring_wall))
            }
            LineStringFeatureType.ROAD -> {
                polylineOptions.width(4f)
                polylineOptions.color(context.getResourceColor(R.color.linestring_road))
            }
        }
        val polyline = googleMap.addPolyline(polylineOptions)
        mapObjectsHolder.addPolyline(polyline, feature.level)
    }

    private fun loadPolygonFeature(feature: PolygonFeature) {
        val listLatLng = feature.coordinates.map { LatLng(it.lng, it.lat) }
        val polygonOptions = PolygonOptions()
            .add(*listLatLng.toTypedArray())

        when (feature.typeElement) {
            PolygonFeatureType.BUILDING -> {
                polygonOptions.fillColor(context.getResourceColor(R.color.building_fill))
                polygonOptions.strokeColor(context.getResourceColor(R.color.building_stroke))
                polygonOptions.strokeWidth(6f)
            }
            PolygonFeatureType.BACKGROUND -> {
                polygonOptions.fillColor(context.getResourceColor(R.color.background_fill))
                polygonOptions.strokeColor(context.getResourceColor(R.color.background_stroke))
                polygonOptions.strokeWidth(2f)
            }
            PolygonFeatureType.ROOM -> {
                polygonOptions.zIndex(100f)
                polygonOptions.strokeWidth(2f)
                polygonOptions.strokeColor(context.getResourceColor(R.color.room_stroke))
                when (feature.roomType) {
                    RoomType.CLASS -> {
                        polygonOptions.fillColor(context.getResourceColor(R.color.room_class_fill))
                    }
                    RoomType.COMPUTER_CLASS -> {
                        polygonOptions.fillColor(context.getResourceColor(R.color.room_computer_class_fill))
                    }
                    RoomType.LIBRARY -> {
                        polygonOptions.fillColor(context.getResourceColor(R.color.room_library_fill))
                    }
                    RoomType.SPORTS_HALL -> {
                        polygonOptions.fillColor(context.getResourceColor(R.color.room_sports_hall_fill))
                    }
                    RoomType.ASSEMBLY_HALL -> {
                        polygonOptions.fillColor(context.getResourceColor(R.color.room_assembly_hall_fill))
                    }
                    RoomType.SCENE -> {
                        polygonOptions.fillColor(context.getResourceColor(R.color.room_scene_fill))
                    }
                    RoomType.CHANGING_AREA -> {
                        polygonOptions.fillColor(context.getResourceColor(R.color.room_changing_area_fill))
                    }
                    RoomType.OFFICE -> {
                        polygonOptions.fillColor(context.getResourceColor(R.color.room_office_fill))
                    }
                    RoomType.TOILET_W -> {
                        polygonOptions.fillColor(context.getResourceColor(R.color.room_toilet_w_fill))
                    }
                    RoomType.TOILET_M -> {
                        polygonOptions.fillColor(context.getResourceColor(R.color.room_toilet_m_fill))
                    }
                    else -> throw RuntimeException("Unknown room type")
                }
            }
            PolygonFeatureType.WAY -> {
                polygonOptions.fillColor(context.getResourceColor(R.color.way_fill))
                polygonOptions.strokeColor(context.getResourceColor(R.color.way_stroke))
                polygonOptions.strokeWidth(5f)
            }
        }
        val polygon = googleMap.addPolygon(polygonOptions)
        mapObjectsHolder.addPolygon(polygon, feature.level)
    }

}