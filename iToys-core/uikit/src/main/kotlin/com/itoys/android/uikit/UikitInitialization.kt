package com.itoys.android.uikit

import android.app.Application
import com.drake.statelayout.StateConfig
import com.itoys.android.logcat.logcat
import com.itoys.android.uikit.components.empty.EmptyConfig

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/19
 */
object UikitInitialization {

    private lateinit var APPLICATION: Application

    /** Empty 配置 */
    private var emptyConfig: EmptyConfig? = null

    /**
     * 初始化 uikit.
     */
    fun initialization(application: Application) {
        this.APPLICATION = application
        initDataDisplay()

        // Brv 缺省页配置
        StateConfig.apply {
            emptyLayout = R.layout.uikit_layout_empty

            onEmpty {
                logcat { "StateConfig -> onEmpty" }
            }
        }
    }

    /**
     * 初始化数据显示
     */
    private fun initDataDisplay() {
        emptyConfig = EmptyConfig.Builder().build()
    }

    /**
     * 获取 Empty 配置
     */
    fun requireEmptyConfig(): EmptyConfig {
        if (emptyConfig == null) {
            initDataDisplay()
        }

        return emptyConfig!!
    }

    fun requireApp(): Application {
        return APPLICATION
    }
}