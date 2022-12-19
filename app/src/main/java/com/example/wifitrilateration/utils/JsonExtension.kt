package com.example.wifitrilateration.utils

import org.json.JSONObject

fun JSONObject.getStringOrNull(property: String): String? {
    return if (has(property))
        this.getString(property)
    else
        null
}

fun JSONObject.getIntOrNull(property: String): Int? {
    return if (has(property))
        this.getInt(property)
    else
        null
}