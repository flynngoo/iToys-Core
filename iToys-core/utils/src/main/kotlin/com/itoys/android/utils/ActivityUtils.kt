package com.itoys.android.utils

import android.app.Activity

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/21
 */
object ActivityUtils {

    /**
     * Return whether the activity is alive.
     */
    fun isActivityAlive(activity: Activity?): Boolean {
        return activity != null && !activity.isFinishing && !activity.isDestroyed
    }

    /**
     * Return Whether or not system main activity.
     */
    fun isMainLaunchActivity(activity: Activity): Boolean {
        val packageManager = UtilsInitialization.requireApp().packageManager
        val launchIntent = packageManager.getLaunchIntentForPackage(activity.packageName) ?: return false
        val launchComponentName = launchIntent.component
        val componentName = activity.componentName

        return (launchComponentName != null && componentName.equals(launchComponentName))
    }
}