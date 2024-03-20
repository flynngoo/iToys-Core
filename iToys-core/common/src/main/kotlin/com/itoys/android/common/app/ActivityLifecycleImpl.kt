package com.itoys.android.common.app

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.itoys.android.logcat.asLog
import com.itoys.android.logcat.logcat
import com.itoys.android.utils.ActivityUtils
import com.itoys.android.utils.expansion.tagName
import com.itoys.android.utils.expansion.then
import java.util.LinkedList
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/21
 */
class ActivityLifecycleImpl : Application.ActivityLifecycleCallbacks {

    companion object {

        private const val CLASS_NAME_ACTIVITY_THREAD = "android.app.ActivityThread"
        private const val FIELD_NAME_ACTIVITY_THREAD = "sCurrentActivityThread"
        private const val METHOD_NAME_ACTIVITY_THREAD = "currentActivityThread"

        val INSTANCE: ActivityLifecycleImpl by lazy { ActivityLifecycleImpl() }

        private val activityList: LinkedList<Activity> by lazy { LinkedList() }

        private val appStatusList: CopyOnWriteArrayList<IAppStatusChangedCallback> by lazy { CopyOnWriteArrayList() }

        /**
         * 注册activity 生命周期回调
         */
        fun install(application: Application) {
            application.registerActivityLifecycleCallbacks(INSTANCE)
        }

        /**
         * 取下注册activity 生命周期回调
         */
        fun uninstall(application: Application) {
            activityList.clear()
            appStatusList.clear()
            application.unregisterActivityLifecycleCallbacks(INSTANCE)
        }

        /**
         * 添加app 状态回调
         */
        fun addAppStatusChangedCallback(callback: IAppStatusChangedCallback) {
            appStatusList.add(callback)
        }

        /**
         * 移除app 状态回调
         */
        fun removeAppStatusChangedCallback(callback: IAppStatusChangedCallback) {
            appStatusList.remove(callback)
        }
    }

    /** 是否回到后台 */
    private var isBackground: Boolean = false

    private var mConfigCount: Int = 0
    private var mForegroundCount: Int = 0

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        logcat(priority = Log.INFO) { "${activity.tagName} created!" }

        if (activityList.isEmpty()) {
            postStatus(activity)
        }

        setTopActivity(activity)
    }

    override fun onActivityStarted(activity: Activity) {
        logcat(priority = Log.INFO) { "${activity.tagName} started!" }

        if (!isBackground) {
            setTopActivity(activity)
        }

        if (mConfigCount < 0) {
            ++mConfigCount
        } else {
            ++mForegroundCount
        }
    }

    override fun onActivityResumed(activity: Activity) {
        logcat(priority = Log.INFO) { "${activity.tagName} resumed!" }

        setTopActivity(activity)
        if (isBackground) {
            isBackground = false
            postStatus(activity)
        }
    }

    override fun onActivityPaused(activity: Activity) {
        logcat(priority = Log.INFO) { "${activity.tagName} paused!" }
    }

    override fun onActivityStopped(activity: Activity) {
        logcat(priority = Log.INFO) { "${activity.tagName} stopped!" }

        if (activity.isChangingConfigurations) {
            --mConfigCount
        } else {
            --mForegroundCount

            if (mForegroundCount <= 0) {
                isBackground = true
                postStatus(activity, isForeground = false)
            }
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        logcat(priority = Log.INFO) { "${activity.tagName} save outState!" }
    }

    override fun onActivityDestroyed(activity: Activity) {
        logcat(priority = Log.INFO) { "${activity.tagName} destroyed!" }

        activityList.remove(activity)
    }

    /**
     * 当前 app 是否在前台状态回调
     */
    private fun postStatus(activity: Activity, isForeground: Boolean = true) {
        if (appStatusList.isEmpty()) return

        appStatusList.forEach { callback ->
            isForeground.then(
                { callback.onForeground(activity) },
                { callback.onBackground(activity) })
        }
    }

    /**
     * 使当前 activity 保持在最上面
     */
    private fun setTopActivity(activity: Activity) {
        if (activityList.contains(activity)) {
            if (activityList.first != activity) {
                activityList.remove(activity)
                activityList.addFirst(activity)
            }
        } else {
            activityList.addFirst(activity)
        }
    }

    /**
     * 获取top activity.
     */
    fun getTopActivity(): Activity? {
        val activityList = getActivityList()
        activityList.forEach { activity ->
            if (!ActivityUtils.isActivityAlive(activity)) {
                return@forEach
            }

            return activity
        }

        return null
    }

    private fun getActivityList(): List<Activity> {
        if (activityList.isNotEmpty()) {
            return LinkedList(activityList)
        }

        val reflectActivityList = getActivityListByReflect()
        activityList.addAll(reflectActivityList)
        return LinkedList(activityList)
    }

    /**
     * 通过反射获取activity list.
     */
    private fun getActivityListByReflect(): List<Activity> {
        val list: LinkedList<Activity> = LinkedList()
        var topActivity: Activity? = null

        try {
            val activityThread = getActivityThread()
            val mActivitiesField = activityThread?.javaClass?.getDeclaredField("mActivities")
            mActivitiesField?.isAccessible = true
            val mActivities = mActivitiesField?.get(activityThread)
            if (mActivities !is Map<*, *>) return list

            mActivities.values.forEach {  activityRecord ->
                val activityRecordClass = activityRecord?.javaClass
                val activityField = activityRecordClass?.getDeclaredField("activity")
                activityField?.isAccessible = true
                val activity = activityField?.get(activityRecordClass) as Activity?

                if (topActivity == null) {
                    val pauseField = activityRecordClass?.getDeclaredField("paused")
                    pauseField?.isAccessible = true
                    if (pauseField?.getBoolean(activityRecord) == false) {
                        topActivity = activity
                    } else {
                        activity?.let { list.add(activity) }
                    }
                } else {
                    activity?.let { list.add(activity) }
                }
            }
        } catch (e: Exception) {
            e.logcat()
        }

        if (topActivity != null) list.addFirst(topActivity)

        return list
    }

    /**
     * Return activity thread by reflect.
     */
    @SuppressLint("PrivateApi")
    private fun getActivityThread(): Any? {
        return try {
            val activityThreadClass = Class.forName(CLASS_NAME_ACTIVITY_THREAD)
            val activityThread = getActivityThreadInActivityThreadStaticField(activityThreadClass)
            (activityThread != null).then({ activityThread },
                { getActivityThreadInActivityThreadStaticMethod(activityThreadClass) })
        } catch (e: Exception) {
            e.logcat()
            null
        }
    }

    /**
     * Return activity thread by reflect field.
     */
    private fun getActivityThreadInActivityThreadStaticField(activityThread: Class<*>): Any? {
        return try {
            val field = activityThread.getDeclaredField(FIELD_NAME_ACTIVITY_THREAD)
            field.isAccessible = true
            field.get(null)
        } catch (e: Exception) {
            e.logcat()
            null
        }
    }

    /**
     * Return activity thread by reflect method.
     */
    private fun getActivityThreadInActivityThreadStaticMethod(activityThread: Class<*>): Any? {
        return try {
            activityThread.getMethod(METHOD_NAME_ACTIVITY_THREAD).invoke(null)
        } catch (e: Exception) {
            e.logcat()
            null
        }
    }
}