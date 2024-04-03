package com.itoys.android.core.network

import android.app.Application
import com.itoys.android.core.GlobalConfig
import com.itoys.android.utils.expansion.invalid

/**
 * @author Fanfan.gu <a href="mailto:fanfan.work@outlook.com">Contact me.</a>
 * @date 17/03/2023
 * @desc 网络初始化
 */
object NetworkInitialization {

    /**
     * api成功code
     */
    private var apiSuccessfulCode = ApiResultCode.SUCCESSFUL

    /**
     * 资源host
     */
    private var resourcesHostUrl: String = ""

    /**
     * 网络依赖
     */
    private var networkDependency: INetworkDependency? = null

    /**
     * 全局http请求处理类
     */
    private var globalHttpHandler: GlobalHttpHandler = GlobalHttpHandler.DEFAULT

    /**
     * 初始化
     */
    fun initialization(application: Application, globalConfig: GlobalConfig) {
        initRetrofit(globalConfig.apiHostUrl.invalid())
        apiSuccessfulCode = globalConfig.apiSuccessfulCode
        resourcesHostUrl = globalConfig.resourcesHostUrl.invalid()
        networkDependency = globalConfig.networkDependency
        globalHttpHandler = globalConfig.globalHttpHandler
    }

    /**
     * 获取网络配置
     */
    fun requireNetworkDependency(): INetworkDependency? {
        return networkDependency
    }

    /**
     * 获取api成功code
     */
    fun requireApiSuccessfulCode() = apiSuccessfulCode

    /**
     * 获取资源host
     */
    fun requireResourcesHostUrl() = resourcesHostUrl

    /**
     * 获取全局http请求处理类
     */
    fun requireGlobalHttpHandler() = globalHttpHandler
}