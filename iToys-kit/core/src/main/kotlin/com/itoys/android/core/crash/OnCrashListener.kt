package com.itoys.android.core.crash

import android.content.Context

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/4/17
 */
interface OnCrashListener {

    /**
     * 发生崩溃
     * @param context
     * @param crashHandler
     * @param throwable
     */
    fun onCrash(context: Context?, crashHandler: ICrashHandler?, throwable: Throwable?): Boolean
}