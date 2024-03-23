package com.itoys.android.location

import android.app.Application
import com.amap.api.location.AMapLocationClient

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/19
 */
object LocationInitialization {

    /**
     * 初始化.
     */
    fun initialization(application: Application, debug: Boolean = true) {
        // 设置包含隐私政策，并展示用户授权弹窗
        // isContains: 是隐私权政策是否包含高德开平隐私权政策  true是包含
        // isShow: 隐私权政策是否弹窗展示告知用户 true是展示
        AMapLocationClient.updatePrivacyShow(application, true, true)
        updatePrivacy(application, isAgree = false)
    }

    /**
     * 更新是否同意用户授权政策
     */
    fun updatePrivacy(application: Application, isAgree: Boolean) {
        // 设置是否同意用户授权政策 <b>必须在AmapLocationClient实例化之前调用</b>
        // isAgree:隐私权政策是否取得用户同意  true是用户同意
        AMapLocationClient.updatePrivacyAgree(application, isAgree)
    }
}