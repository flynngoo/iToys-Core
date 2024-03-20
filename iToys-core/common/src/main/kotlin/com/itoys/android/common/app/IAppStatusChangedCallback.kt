package com.itoys.android.common.app

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
    fun onForeground(activity: Activity)

    /**
     * 回到后台
     */
    fun onBackground(activity: Activity)
}