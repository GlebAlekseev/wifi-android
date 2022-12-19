package com.example.wifitrilateration.utils

import android.content.Context
import com.example.wifitrilateration.MainApplication
import com.example.wifitrilateration.di.AppComponent

fun Context.getResourceString(resourceId: Int): String {
    return resources.getString(resourceId)
}

fun Context.getResourceColor(resourceId: Int): Int {
    return resources.getColor(resourceId)
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is MainApplication -> {
            appComponent
        }
        else -> {
            this.applicationContext.appComponent
        }
    }