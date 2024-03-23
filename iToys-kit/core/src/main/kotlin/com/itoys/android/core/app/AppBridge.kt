package com.itoys.android.core.app

import android.app.Activity

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/21
 */
object AppBridge {

    /**
     * Register the status of application changed callback.
     *
     * [callback] The status of application changed callback.
     */
    fun addAppStatusChangeCallback(callback: IAppStatusChangedCallback) {
        ActivityLifecycleImpl.addAppStatusChangedCallback(callback)
    }

    /**
     * Unregister the status of application changed callback.
     *
     * [callback] The status of application changed callback.
     */
    fun removeAppStatusChangeCallback(callback: IAppStatusChangedCallback) {
        ActivityLifecycleImpl.removeAppStatusChangedCallback(callback)
    }

    /**
     * Return the top activity in activity's stack.
     */
    fun getTopActivity(): Activity? {
        return ActivityLifecycleImpl.INSTANCE.getTopActivity()
    }
}