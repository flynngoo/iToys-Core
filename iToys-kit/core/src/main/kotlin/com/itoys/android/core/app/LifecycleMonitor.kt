package com.itoys.android.core.app

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.itoys.android.logcat.logcat
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/4/17
 */
class LifecycleMonitor: DefaultLifecycleObserver {

    companion object {
        val INSTANCE: LifecycleMonitor by lazy { LifecycleMonitor() }

        private val appStatusList: CopyOnWriteArrayList<IAppStatusChangedCallback> by lazy { CopyOnWriteArrayList() }

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

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        logcat { ">>>>>>>> onCreate <<<<<<<< " }
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        logcat { ">>>>>>>> 前台 <<<<<<<< " }
        appStatusList.forEach { it.onForeground() }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        logcat { ">>>>>>>> onPause <<<<<<<< " }
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        logcat { ">>>>>>>> 后台 <<<<<<<< " }
        appStatusList.forEach { it.onBackground() }
    }
}