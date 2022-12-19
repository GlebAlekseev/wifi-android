package com.example.wifitrilateration.ui.framework.map

import android.content.Context
import android.graphics.Color
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.Navigator
import com.example.wifitrilateration.domain.entity.*
import com.example.wifitrilateration.utils.getResourceColor
import com.glebalekseevjk.wifitrilateration.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.maps.android.ui.IconGenerator

class MapNavigatorLoader(
    private val context: Context,
    private val googleMap: GoogleMap,
    private val mapObjectsHolder: MapObjectsHolder
) {

    fun loadNavigator(userLocation: UserLocation){
        // Одинаковые группы - помечать единым цветом..
        val hex = Integer.toString(userLocation.user.group.toInt(),16)

        val iconDrawable =
            AppCompatResources.getDrawable(context, R.drawable.navigator)
                ?: return
        val wrappedIconDrawable = DrawableCompat.wrap(iconDrawable)
        DrawableCompat.setTint(wrappedIconDrawable, Color.parseColor("#$hex"))

        val marker = googleMap.addMarker(
            MarkerOptions()
                .position(
                    LatLng(
                        userLocation.lng,
                        userLocation.lat
                    )
                )
                .title(userLocation.user.displayName)
                .icon(BitmapDescriptorFactory.fromBitmap(wrappedIconDrawable.toBitmap(35, 35)))
        )
        if (marker != null) mapObjectsHolder.addNavigator(marker, userLocation.level)
    }

    fun loadNavigators(listNavigator: List<UserLocation>){
        listNavigator.forEach {
            loadNavigator(it)
        }

    }



}

