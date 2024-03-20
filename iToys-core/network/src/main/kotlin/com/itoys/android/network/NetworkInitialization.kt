package com.itoys.android.network

import android.app.Application
import android.util.Log
import com.itoys.android.logcat.logcat

/**
 * @author Fanfan.gu <a href="mailto:fanfan.work@outlook.com">Contact me.</a>
 * @date 17/03/2023
 * @desc 网络初始化
 */
object NetworkInitialization {

    private var _networkDependency: INetworkDependency? = null

    /**
     * 初始化
     */
    fun initialization(application: Application, apiUrl: String) {
        if (apiUrl.isBlank()) return
        logcat(priority = Log.INFO) { ">>>>>>>>>> Api url: $apiUrl <<<<<<<<<<" }
        initRetrofit(apiUrl)
    }

    fun initNetworkDependency(networkDependency: INetworkDependency) {
        _networkDependency = networkDependency
    }

    /**
     * 获取网络配置
     */
    fun requireNetworkDependency(): INetworkDependency? {
        return _networkDependency
    }
}