package com.itoys.android.core.crash

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.itoys.android.utils.ActivityUtils

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/4/17
 */
class StartAppReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let { ActivityUtils.openLaunchActivity(it) }
    }
}