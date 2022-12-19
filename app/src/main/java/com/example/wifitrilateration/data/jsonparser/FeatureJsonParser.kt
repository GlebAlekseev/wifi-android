package com.example.wifitrilateration.data.jsonparser

import com.example.wifitrilateration.domain.entity.*
import com.example.wifitrilateration.utils.getIntOrNull
import com.example.wifitrilateration.utils.getStringOrNull
import org.json.JSONArray
import org.json.JSONObject
import kotlin.reflect.KClass

object FeatureJsonParser {
    fun parseGeoJson(jsonString: String): List<IFeature> {
        val mutableListFeature = mutableListOf<IFeature>()
        val jsonObject = JSONObject(jsonString)
        val features =
            jsonObject.optJSONArray("features") ?: throw RuntimeException("features is missing")
        for (i in 0 until features.length()) {
            val feature = parseFeature(features[i])
            mutableListFeature.add(feature)
        }
        return mutableListFeature
    }

    private fun parseFeature(feature: Any): IFeature {
        val jsonFeature = JSONObject(feature.toString())
        val featureImplType = getFeatureImplType(jsonFeature)
        return when (featureImplType) {
            PointFeature::class -> {
                parsePointFeature(jsonFeature)
            }
            LineStringFeature::class -> {
                parseLineStringFeature(jsonFeature)
            }
            PolygonFeature::class -> {
                parsePolygonFeature(jsonFeature)
            }
            else -> throw RuntimeException("Unknown feature implementation")
        }
    }

    private fun getFeatureImplType(jsonObject: JSONObject): KClass<out IFeature> {
        // check point
        val geometry = jsonObject["geometry"]
        val geometryType = JSONObject(geometry.toString())["type"]
        return when (geometryType) {
            "Point" -> PointFeature::class
            "LineString" -> {
                val properties = jsonObject["properties"]
                val propertiesType = JSONObject(properties.toString()).getStringOrNull("type")

                when (propertiesType) {
                    "polygon" -> PolygonFeature::class
                    "linestring" -> LineStringFeature::class
                    else -> LineStringFeature::class
                }
            }
            else -> throw RuntimeException("Unknown geometry type in feature json")
        }
    }

    private fun parsePointFeature(jsonObject: JSONObject): PointFeature {
        val geometry = jsonObject["geometry"]
        val properties = jsonObject["properties"]
        val coordinatesRaw = JSONObject(geometry.toString()).getJSONArray("coordinates")

        val coordinates = FeatureLatLng(coordinatesRaw.getDouble(0), coordinatesRaw.getDouble(1))
        val level = JSONObject(properties.toString()).getIntOrNull("level")
            ?: throw RuntimeException("feature property level is bad")
        val typeElement = parsePointFeatureType(
            JSONObject(properties.toString()).getStringOrNull("type_element")
                ?: throw RuntimeException("type element is null")
        )

        val name = JSONObject(properties.toString()).getStringOrNull("name") ?: ""
        val description = JSONObject(properties.toString()).getStringOrNull("description") ?: ""

        return PointFeature(coordinates, typeElement, name, description, level)
    }

    private fun parseLineStringFeature(jsonObject: JSONObject): LineStringFeature {
        val geometry = jsonObject["geometry"]
        val properties = jsonObject["properties"]
        val coordinatesJsonArray = JSONObject(geometry.toString()).getJSONArray("coordinates")

        val coordinates = parseFeatureLatLngList(coordinatesJsonArray)

        val level = JSONObject(properties.toString()).getIntOrNull("level")
            ?: throw RuntimeException("feature property level is bad")
        val typeElement = parseLineStringFeatureType(
            JSONObject(properties.toString()).getStringOrNull("type_element")
                ?: throw RuntimeException("type element is null")
        )
        val name = JSONObject(properties.toString()).getStringOrNull("name") ?: ""
        val description = JSONObject(properties.toString()).getStringOrNull("description") ?: ""

        return LineStringFeature(coordinates, typeElement, name, description, level)
    }

    private fun parsePolygonFeature(jsonObject: JSONObject): PolygonFeature {
        val geometry = jsonObject["geometry"]
        val properties = jsonObject["properties"]
        val coordinatesJsonArray = JSONObject(geometry.toString()).getJSONArray("coordinates")
        val room = JSONObject(properties.toString()).getStringOrNull("room")

        val coordinates = parseFeatureLatLngList(coordinatesJsonArray)
        val level = JSONObject(properties.toString()).getIntOrNull("level")
            ?: throw RuntimeException("feature property level is bad")
        val typeElement = parsePolygonFeatureType(
            JSONObject(properties.toString()).getStringOrNull("type_element")
                ?: throw RuntimeException("type element is null")
        )
        val name = JSONObject(properties.toString()).getStringOrNull("name") ?: ""
        val description = JSONObject(properties.toString()).getStringOrNull("description") ?: ""
        val roomType = if (room != null) parseRoomType(room) else null

        return PolygonFeature(coordinates, typeElement, name, description, level, roomType)
    }

    // is not json parser
    private fun parsePointFeatureType(type: String): PointFeatureType {
        return when (type) {
            "main_door" -> PointFeatureType.MAIN_DOOR
            "emergency_door" -> PointFeatureType.EMERGENCY_DOOR
            "door" -> PointFeatureType.DOOR
            "helper_text" -> PointFeatureType.HELPER_TEXT
            else -> throw RuntimeException("Unknown point feature type")
        }
    }

    private fun parseFeatureLatLngList(jsonArray: JSONArray): List<FeatureLatLng> {
        val mutableList = mutableListOf<FeatureLatLng>()
        for (i in 0 until jsonArray.length()) {
            val result = jsonArray.getJSONArray(i)
            mutableList.add(
                FeatureLatLng(
                    result.getDouble(0),
                    result.getDouble(1)
                )
            )
        }
        return mutableList
    }

    private fun parseLineStringFeatureType(type: String): LineStringFeatureType {
        return when (type) {
            "ladder" -> LineStringFeatureType.LADDER
            "wall" -> LineStringFeatureType.WALL
            "road" -> LineStringFeatureType.ROAD
            else -> throw RuntimeException("Unknown linestring feature type")
        }
    }

    private fun parsePolygonFeatureType(type: String): PolygonFeatureType {
        return when (type) {
            "room" -> PolygonFeatureType.ROOM
            "building" -> PolygonFeatureType.BUILDING
            "background" -> PolygonFeatureType.BACKGROUND
            "way" -> PolygonFeatureType.WAY
            else -> throw RuntimeException("Unknown polygon feature type")
        }
    }

    private fun parseRoomType(room: String): RoomType {
        return when (room) {
            "class" -> RoomType.CLASS
            "computer_class" -> RoomType.COMPUTER_CLASS
            "library" -> RoomType.LIBRARY
            "sports_hall" -> RoomType.SPORTS_HALL
            "assembly_hall" -> RoomType.ASSEMBLY_HALL
            "scene" -> RoomType.SCENE
            "changing_area" -> RoomType.CHANGING_AREA
            "office" -> RoomType.OFFICE
            "toilet_w" -> RoomType.TOILET_W
            "toilet_m" -> RoomType.TOILET_M
            else -> throw RuntimeException("Unknown room type in feature")
        }
    }
}
