package com.itoys.android.core.app

import android.app.Activity

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/21
 */
interface IAppStatusChangedCallback {

    /**
     * 回到前台
     */
    fun onForeground()

    /**
     * 回到后台
     */
    fun onBackground()
}