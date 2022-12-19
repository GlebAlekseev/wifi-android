package com.example.wifitrilateration.data.framework

import android.content.Context
import android.content.pm.PackageManager
import com.example.wifitrilateration.ui.activity.PermissionActivity
import javax.inject.Inject

class PermissionManager @Inject constructor(private val context: Context) {
    private fun checkIfHasPermission(permission: String): Boolean {
        val res: Int = context.checkCallingOrSelfPermission(permission)
        return res == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermission(permission: String): Boolean{
        if(checkIfHasPermission(permission)) return true
        val intent = PermissionActivity.getIntent(context, permission)
        context.startActivity(intent)
        return checkIfHasPermission(permission)
    }
}