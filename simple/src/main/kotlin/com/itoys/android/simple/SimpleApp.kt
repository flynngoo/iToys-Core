package com.itoys.android.simple

import android.app.Application
import com.itoys.android.core.GlobalConfig
import com.itoys.android.core.app.AbsApp
import dagger.hilt.android.HiltAndroidApp

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/3/24
 */
@HiltAndroidApp
class SimpleApp : AbsApp() {
    override val globalConfig: GlobalConfig by lazy { simpleConfig(application = this@SimpleApp) }

    override fun syncInit(application: Application) {
    }

    override suspend fun asyncInit(application: Application) {
    }

    override fun globalInit(application: Application) {
    }

    override fun initCompliance(application: Application) {
    }
}