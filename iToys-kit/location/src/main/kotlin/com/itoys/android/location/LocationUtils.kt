package com.itoys.android.location

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2024/3/4
 */
object LocationUtils {

    private val Context.locationManager: LocationManager?
        get() = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager?

    /**
     * 定位服务打开状态
     */
    @JvmStatic
    fun isLocationServiceEnable(context: Context): Boolean {
        val locationManager = context.locationManager ?: return false

        val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        return gps || network
    }

    /**
     * 打开定位服务
     */
    @JvmStatic
    fun openLocationService(context: Context) {
        val location = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        location.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(location)
    }
}