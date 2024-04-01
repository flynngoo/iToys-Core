package com.itoys.android.plugin

import com.android.builder.model.ApiVersion
import com.itoys.android.plugin.env.DevEnv
import com.itoys.android.plugin.env.ProdEnv
import com.itoys.android.plugin.env.TestEnv
import com.itoys.android.plugin.env.UatEnv

object AppConfig {

    // 最高支持到android 13.
    val targetSdkVersion: ApiVersion = object : ApiVersion {
        override fun getApiLevel(): Int = 34

        override fun getCodename(): String = "UpsideDownCake"

        override fun getApiString(): String = "Android 14"
    }

    // 最低版本支持到android 7.
    val minSdkVersion: ApiVersion = object : ApiVersion {
        override fun getApiLevel(): Int = 24

        override fun getCodename(): String = "Nougat"

        override fun getApiString(): String = "Android 7"
    }
    const val buildToolsVersion: String = "34.0.0"

    // app 包名.
    const val appId: String = "com.itoys.android"

    // app 名称.
    const val appName: String = "IToys-Simple"

    // app版本code.
    const val versionCode: Int = 100000

    private const val majorVersion: Int = 1
    private const val minorVersion: Int = 0
    const val patchVersion: Int = 0

    // app版本名称.
    const val versionName: String = "$majorVersion.$minorVersion.$patchVersion"

    // 依赖.
    const val testRunner: String = "androidx.test.runner.AndroidJUnitRunner"
    const val defaultProguardFile: String = "proguard-android-optimize.txt"
    const val proguardRulesFile: String = "proguard-rules.pro"

    // app module 名称
    const val appModuleName: String = "simple"

    // 设计稿-宽度
    private const val designWidth: Int = 375

    // 设计稿-高度
    private const val designHeight: Int = 667

    /** app xml配置项. */
    val appManifestPlaceholders: HashMap<String, Any> by lazy {
        hashMapOf<String, Any>().apply {
            // app 名称.
            put("app_name", appName)
            put("design_width", designWidth)
            put("design_height", designHeight)
        }
    }

    /** app uat环境 资源配置. */
    val uatAppManifestPlaceholders: HashMap<String, Any> by lazy {
        hashMapOf<String, Any>().apply {
            // app 名称.
            put("app_name", appName)
            put("design_width", designWidth)
            put("design_height", designHeight)
        }
    }

    /** app preview(test)环境 资源配置. */
    val previewAppManifestPlaceholders: HashMap<String, Any> by lazy {
        hashMapOf<String, Any>().apply {
            // app 名称.
            put("app_name", "${appName}(Test)")
            put("design_width", designWidth)
            put("design_height", designHeight)
        }
    }

    /** app dev环境 资源配置. */
    val debugAppManifestPlaceholders: HashMap<String, Any> by lazy {
        hashMapOf<String, Any>().apply {
            // app 名称.
            put("app_name", "${appName}(Debug)")
            put("design_width", designWidth)
            put("design_height", designHeight)
        }
    }
}