package com.itoys.android.location.amap

import android.content.Context
import android.util.Log
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.itoys.android.location.LocationEngine
import com.itoys.android.location.LocationModel
import com.itoys.android.location.OnLocationCallback

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2024/3/1
 */
class AMapEngine : LocationEngine {

    // 声明AMapLocationClient类对象
    private var mLocationClient: AMapLocationClient? = null

    // 声明定位回调监听器
    private val mLocationListener: AMapLocationListener by lazy {
        AMapLocationListener { aMapLocation ->
            if (aMapLocation == null) {
                this.locationCallback?.onLocationFailed("获取位置失败")
                return@AMapLocationListener
            }

            if (aMapLocation.errorCode == 0) {
                val location = LocationModel(
                    aMapLocation.address,
                    aMapLocation.latitude,
                    aMapLocation.longitude,
                    aMapLocation.province,
                    aMapLocation.city,
                    aMapLocation.district,
                    aMapLocation.street,
                    aMapLocation.streetNum,
                    aMapLocation.adCode
                )

                Log.i("AMapEngine", "定位成功: $location")
                this.locationCallback?.onLocation(location)
            } else {
                this.locationCallback?.onLocationFailed(aMapLocation.errorInfo ?: "获取位置失败")
            }
        }
    }

    /**
     * 定位回调监听器
     */
    private var locationCallback: OnLocationCallback? = null

    private fun initLocation(context: Context) {
        if (mLocationClient == null) {
            mLocationClient = AMapLocationClient(context.applicationContext)
            mLocationClient?.apply {
                // 设置定位回调监听器
                setLocationListener(mLocationListener)
                // 定位选项
                val options = AMapLocationClientOption()
                options.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
                // 设置单次定位
                options.isOnceLocation = true
                // 返回地址描述
                options.isNeedAddress = true
                // 关闭模拟位置
                options.isMockEnable = false
                // 请求超时时间 30s
                options.httpTimeOut = 30000
                // 设置定位
                setLocationOption(options)
            }
        }
    }

    override fun doLocation(context: Context, locationCallback: OnLocationCallback) {
        initLocation(context)
        this.locationCallback = locationCallback

        mLocationClient?.stopLocation()
        mLocationClient?.startLocation()
    }

    override fun stopLocation() {
        mLocationClient?.stopLocation()
    }

    override fun destroy() {
        mLocationClient?.stopLocation()
        mLocationClient = null
    }
}