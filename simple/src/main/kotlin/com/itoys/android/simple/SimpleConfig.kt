package com.itoys.android.simple

import android.app.Application
import com.itoys.android.BuildConfig
import com.itoys.android.core.GlobalConfig
import com.itoys.android.core.network.INetworkDependency
import com.itoys.android.utils.SysUtils

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/3/24
 */
/**
 * API成功code
 */
private const val API_SUCCESSFUL_CODE = 1

fun simpleConfig(application: Application): GlobalConfig {
    return GlobalConfig.build {
        debugMode(BuildConfig.DEBUG)
        apiHostUrl(BuildConfig.API_URL)
        apiSuccessfulCode(API_SUCCESSFUL_CODE)
        networkDependency(object : INetworkDependency {

            override fun appVersion(): Long = SysUtils.appVersionCode(application)

            override fun appVersionName(): String = SysUtils.appVersionName(application)

            override fun headers() = mapOf<String, String>()

            override fun platform() = "Android"

            override fun specialHeaders() = mapOf<String, String>()

            override fun specialUrls() = listOf<String>()

            override fun token() = ""

            override fun tokenKey() = "ClientAuthorization"
        })
    }
}