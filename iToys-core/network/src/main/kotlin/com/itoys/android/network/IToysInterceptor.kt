package com.itoys.android.network

import android.os.Build
import com.itoys.android.utils.expansion.invalid
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/13
 */
class IToysInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()

        val networkDependency = NetworkInitialization.requireNetworkDependency()

        // Token
        val token = networkDependency?.token().invalid()
        if (token.isNotEmpty()) {
            builder.addHeader("blade-auth", token)
        }
        // 平台
        builder.addHeader("platform", networkDependency?.platform().invalid())
        // 版本号
        builder.addHeader("AppVersion", (networkDependency?.appVersion() ?: 0).toString())
        // 版本名称
        builder.addHeader("AppVersionName", networkDependency?.appVersionName().invalid())
        // 手机型号
        builder.addHeader("PhoneModel", "${Build.MANUFACTURER}-${Build.MODEL}")
        // Android 系统版本
        builder.addHeader("SystemVersion", "Android ${Build.VERSION.RELEASE}")

        // 特殊请求
        val url = chain.request().url
        if (networkDependency?.specialUrls()?.contains(url.pathSegments.last()) == true) {
            // 特殊请求头
            networkDependency.specialHeaders().forEach { (t, u) ->
                builder.addHeader(t, u)
            }
        }

        return chain.proceed(builder.build())
    }
}