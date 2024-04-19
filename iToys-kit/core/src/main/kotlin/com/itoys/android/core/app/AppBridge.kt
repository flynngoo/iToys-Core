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
        LifecycleMonitor.addAppStatusChangedCallback(callback)
    }

    /**
     * Unregister the status of application changed callback.
     *
     * [callback] The status of application changed callback.
     */
    fun removeAppStatusChangeCallback(callback: IAppStatusChangedCallback) {
        LifecycleMonitor.removeAppStatusChangedCallback(callback)
    }

    /**
     * Return the top activity in activity's stack.
     */
    fun getTopActivity(): Activity? {
        return ActivityLifecycleImpl.INSTANCE.getTopActivity()
    }

    /**
     * Kill the activity.
     */
    fun killActivity(activityClass: Class<*>) {
        ActivityLifecycleImpl.INSTANCE.killActivity(activityClass)
    }

    /**
     * Kill all activities.
     */
    fun killAllActivities() {
        ActivityLifecycleImpl.INSTANCE.killAllActivities()
    }

    /**
     * Kill all activities，excluding specified activities.
     */
    fun killAllActivities(vararg excludeActivityClasses: Class<*>) {
        ActivityLifecycleImpl.INSTANCE.killAllActivities(excludeActivityClasses.toList())
    }
}