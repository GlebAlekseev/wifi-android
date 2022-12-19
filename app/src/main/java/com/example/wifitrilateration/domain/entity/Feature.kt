package com.example.wifitrilateration.domain.entity


interface IFeature {
    val name: String
    val description: String
    val level: Int
}

data class FeatureLatLng(
    val lat: Double,
    val lng: Double
)

// Point

data class PointFeature (
    val coordinates: FeatureLatLng,
    val typeElement: PointFeatureType,
    override val name: String,
    override val description: String,
    override val level: Int
): IFeature

enum class PointFeatureType {
    MAIN_DOOR,
    EMERGENCY_DOOR,
    DOOR,
    HELPER_TEXT
}

// LineString

data class LineStringFeature (
    val coordinates: List<FeatureLatLng>,
    val typeElement: LineStringFeatureType,
    override val name: String,
    override val description: String,
    override val level: Int,
): IFeature

enum class LineStringFeatureType {
    LADDER,
    WALL,
    ROAD
}

// Polygon

data class PolygonFeature (
    val coordinates: List<FeatureLatLng>,
    val typeElement: PolygonFeatureType,
    override val name: String,
    override val description: String,
    override val level: Int,
    val roomType: RoomType?
): IFeature

enum class PolygonFeatureType {
    BUILDING,
    ROOM,
    WAY,
    BACKGROUND,
}
enum class RoomType {
    CLASS,
    COMPUTER_CLASS,
    LIBRARY,
    SPORTS_HALL,
    ASSEMBLY_HALL,
    SCENE,
    CHANGING_AREA,
    OFFICE,
    TOILET_W,
    TOILET_M,
}