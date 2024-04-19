package com.itoys.android.core.app

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Process
import android.util.Log
import com.itoys.android.logcat.logcat
import com.itoys.android.utils.ActivityUtils
import com.itoys.android.utils.expansion.tagName
import com.itoys.android.utils.expansion.then
import java.util.LinkedList

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
            application.unregisterActivityLifecycleCallbacks(INSTANCE)
        }
    }

    /** 是否回到后台 */
    private var isBackground: Boolean = false

    private var mConfigCount: Int = 0
    private var mForegroundCount: Int = 0

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        logcat(priority = Log.INFO) { "${activity.tagName} created!" }

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

    /**
     * 关闭指定的 [Activity] class 的所有的实例
     *
     * @param activityClass
     */
    fun killActivity(activityClass: Class<*>) {
        synchronized(ActivityLifecycleImpl) {
            val iterator = activityList.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next()

                if (next.javaClass == activityClass) {
                    iterator.remove()
                    next.finish()
                }
            }
        }
    }

    /**
     * 关闭所有 [Activity]
     */
    fun killAllActivities() {
        synchronized(ActivityLifecycleImpl) {
            val iterator = activityList.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next()
                iterator.remove()
                next.finish()
            }
        }
    }

    /**
     * 关闭所有 [Activity],排除指定的 [Activity]
     *
     * @param excludeActivityClasses activity class
     */
    fun killAllActivities(excludeActivityClasses: List<Class<*>>) {
        synchronized(ActivityLifecycleImpl) {
            val iterator = activityList.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next()
                if (excludeActivityClasses.contains(next.javaClass)) {
                    continue
                }

                iterator.remove()
                next.finish()
            }
        }
    }

    /**
     * 退出应用程序
     *
     * 此方法经测试在某些机型上并不能完全杀死 App 进程, 几乎试过市面上大部分杀死进程的方式, 但都发现没卵用, 所以此
     * 方法如果不能百分之百保证能杀死进程, 就不能贸然调用 [release()] 释放资源, 否则会造成其他问题, 如果您
     * 有测试通过的并能适用于绝大多数机型的杀死进程的方式, 望告知
     */
    fun exitApp() {
        try {
            killAllActivities()
            Process.killProcess(Process.myPid())
            System.exit(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
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