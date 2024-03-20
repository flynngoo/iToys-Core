package com.itoys.android.location

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2024/3/3
 */
interface OnLocationCallback {

     /**
     * 定位成功
     */
    fun onLocation(location: LocationModel)

    /**
     * 定位失败
     *
     * @param msg 错误信息
     */
    fun onLocationFailed(msg: String) {
    }

    /**
     * 定位服务未开启
     */
    fun onLocationServiceNotEnabled() {
    }

    /**
     * 权限拒绝
     */
    fun onPermissionDenied() {

    }
}