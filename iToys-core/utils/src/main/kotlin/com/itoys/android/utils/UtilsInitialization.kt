package com.itoys.android.utils

import android.app.Application
import com.tencent.mmkv.MMKV

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/21
 */
object UtilsInitialization {

    private lateinit var APPLICATION: Application

    fun initialization(application: Application) {
        APPLICATION = application
        MMKV.initialize(application)
    }

    fun requireApp(): Application {
        return APPLICATION
    }
}