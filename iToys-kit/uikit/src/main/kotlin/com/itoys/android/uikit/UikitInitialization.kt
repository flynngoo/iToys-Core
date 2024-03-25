package com.itoys.android.uikit

import android.app.Application
import com.drake.statelayout.StateConfig
import com.itoys.android.logcat.logcat

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/19
 */
object UikitInitialization {

    private lateinit var APPLICATION: Application

    /**
     * 初始化 uikit.
     */
    fun initialization(application: Application) {
        this.APPLICATION = application

        // Brv 缺省页配置
        StateConfig.apply {
            emptyLayout = R.layout.uikit_layout_empty

            onEmpty {
                logcat { "StateConfig -> onEmpty" }
            }
        }
    }

    fun requireApp(): Application {
        return APPLICATION
    }
}