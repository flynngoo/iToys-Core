package com.itoys.android.location

import android.content.Context

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2024/3/1
 */
interface LocationEngine {

    /**
     * 定位
     */
    fun doLocation(context: Context, locationCallback: OnLocationCallback)

    /**
     * 停止定位
     */
    fun stopLocation()

    /**
     * 销毁
     */
    fun destroy()
}