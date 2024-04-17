package com.itoys.android.utils

import android.app.Activity
import android.content.Context
import android.content.Intent

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

    /**
     * Open the main activity.
     */
    fun openLaunchActivity(context: Context) {
        val packageManager = UtilsInitialization.requireApp().packageManager
        val launchIntent = packageManager.getLaunchIntentForPackage(context.packageName) ?: return
        launchIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(launchIntent)
    }
}