package com.itoys.android.housing

import android.app.Activity
import android.app.Application
import android.util.Log
import com.itoys.android.common.app.AbsApp
import com.itoys.android.common.app.AppBridge
import com.itoys.android.common.app.IAppStatusChangedCallback
import com.itoys.android.location.LocationInitialization
import com.itoys.android.logcat.LoggerInitialization
import com.itoys.android.logcat.logcat
import com.itoys.android.login.Login
import com.itoys.android.network.INetworkDependency
import com.itoys.android.network.NetworkInitialization
import com.itoys.android.utils.SysUtils
import com.itoys.android.utils.UtilsInitialization
import com.itoys.android.utils.expansion.invalid
import dagger.hilt.android.HiltAndroidApp

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/20
 *
 * app.
 */

@HiltAndroidApp
class IToysApp : AbsApp() {

    override fun syncInit(application: Application) {
        // 初始化日志
        LoggerInitialization.initialization(application = application)
        // 初始化 network
        initNetwork(application)
        // 添加app状态回调
        addAppStatusCallback()
        // 初始化工具类
        UtilsInitialization.initialization(application = application)
        // 初始化定位
        LocationInitialization.initialization(application = application)
        // 同意隐私政策
        if (IToys.AgreePrivacy) {
            initCompliance(application)
        }
    }

    override suspend fun asyncInit(application: Application) {
        logcat(priority = Log.INFO) { ">>>>>>>>>> 异步初始化 <<<<<<<<<<" }
    }

    override fun globalInit(application: Application) {
        logcat(priority = Log.INFO) { ">>>>>>>>>> 全局初始化 <<<<<<<<<<" }
    }

    override fun initCompliance(application: Application) {
        logcat(priority = Log.INFO) { ">>>>>>>>>> 用户已经同意隐私政策 <<<<<<<<<<" }
        LocationInitialization.updatePrivacy(application, isAgree = true)
    }

    override fun debugMode(): Boolean = BuildConfig.DEBUG

    /**
     * 添加app状态回调
     */
    private fun addAppStatusCallback() {
        AppBridge.addAppStatusChangeCallback(object : IAppStatusChangedCallback {
            override fun onForeground(activity: Activity) {
                logcat(priority = Log.INFO) { ">>>>>>>>>> App 回到前台 <<<<<<<<<<" }
            }

            override fun onBackground(activity: Activity) {
                logcat(priority = Log.INFO) { ">>>>>>>>>> App 回到后台 <<<<<<<<<<" }
            }
        })
    }

    /**
     * 初始化网络
     */
    private fun initNetwork(application: Application) {
        // 初始化网络依赖
        NetworkInitialization.initNetworkDependency(object : INetworkDependency {
            override fun platform(): String = "Android"

            override fun appVersion(): Long = SysUtils.appVersionCode(application)

            override fun appVersionName(): String = SysUtils.appVersionName(application)

            override fun token(): String = Login.token.invalid()

            override fun specialUrls(): List<String> = listOf("login")

            override fun specialHeaders(): HashMap<String, String> = hashMapOf(
                "Authorization" to BuildConfig.LOGIN_SALT
            )
        })

        NetworkInitialization.initialization(
            application = application,
            apiUrl = BuildConfig.API_URL
        )
    }
}