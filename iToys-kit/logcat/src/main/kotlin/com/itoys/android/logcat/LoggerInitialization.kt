package com.itoys.android.logcat

import android.app.Application
import timber.log.Timber

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/19
 */
object LoggerInitialization {

    /**
     * 初始化 logger.
     */
    fun initialization(application: Application, debug: Boolean = true) {
        Timber.plant(if (debug) LoggerTree() else ProdTree())
    }
}