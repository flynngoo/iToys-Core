package com.itoys.android.uikit

import android.app.Application
import com.drake.statelayout.StateConfig
import com.itoys.android.logcat.logcat
import com.itoys.android.uikit.components.upgrade.ApkInstallReceiver
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/19
 */
object UikitInitialization {

    private lateinit var APPLICATION: Application

    private var imageFolder = ""

    /**
     * 初始化 uikit.
     */
    fun initialization(application: Application, imageFolder: String = "iToysAndroid") {
        this.APPLICATION = application
        this.imageFolder = imageFolder

        // Brv 缺省页配置
        StateConfig.apply {
            emptyLayout = R.layout.uikit_layout_empty
            loadingLayout = R.layout.uikit_layout_loading

            onEmpty {
                logcat { "StateConfig -> onEmpty" }
            }
        }

        // 全局刷新配置
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ -> ClassicsHeader(context) }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ -> ClassicsFooter(context) }

        // 注册install广播
        ApkInstallReceiver.registerReceiver(application)
    }

    fun requireApp(): Application {
        return APPLICATION
    }

    fun requireImageFolder(): String {
        return imageFolder
    }
}