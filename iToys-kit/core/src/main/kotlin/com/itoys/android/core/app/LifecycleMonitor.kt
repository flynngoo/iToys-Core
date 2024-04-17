package com.itoys.android.core.app

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.itoys.android.logcat.logcat

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/4/17
 */
class LifecycleMonitor: DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        logcat { ">>>>>>>> onCreate <<<<<<<< " }
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        logcat { ">>>>>>>> 前台 <<<<<<<< " }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        logcat { ">>>>>>>> onPause <<<<<<<< " }
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        logcat { ">>>>>>>> 后台 <<<<<<<< " }
    }
}