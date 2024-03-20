package com.itoys.android.location

import android.content.Context
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.itoys.android.location.amap.AMapEngine

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2024/3/1
 */
object LocationHelper {

    /** 定位引擎 */
    private val locationEngine: LocationEngine by lazy { AMapEngine() }

    /**
     * 定位
     */
    fun doLocation(
        context: Context,
        locationCallback: OnLocationCallback
    ) {
        if (!LocationUtils.isLocationServiceEnable(context)) {
            locationCallback.onLocationServiceNotEnabled()
            return
        }

        val permissionCallback = object : OnPermissionCallback {
            override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                if (allGranted) {
                    locationEngine.doLocation(context, locationCallback)
                } else {
                    requestLocationPermission(context, this, permissions)
                }
            }

            override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                if (doNotAskAgain) {
                    locationCallback.onPermissionDenied()
                } else {
                    requestLocationPermission(context, this, permissions)
                }
            }
        }

        requestLocationPermission(context, permissionCallback)
    }

    /**
     * 停止定位
     */
    fun stopLocation() {
        locationEngine.stopLocation()
    }

    /**
     * 请求定位权限
     */
    private fun requestLocationPermission(
        context: Context,
        callback: OnPermissionCallback,
        permission: List<String> = listOf(Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION)
    ) {
        XXPermissions.with(context)
            .permission(permission)
            .request(callback)
    }
}