package com.itoys.android.core.router

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.itoys.android.logcat.logcat
import com.itoys.android.utils.expansion.invalid
import com.itoys.android.utils.expansion.isBlank
import com.jeremyliao.liveeventbus.LiveEventBus
import com.therouter.TheRouter
import com.therouter.router.Navigator
import com.therouter.router.interceptor.NavigationCallback

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/12/13
 */

/**
 * 导航到指定路由
 */
fun String?.navigation() {
    if (this.isBlank()) {
        navPageNotFound(this.invalid())
        return
    }

    TheRouter.build(this).navigation(callback = object : NavigationCallback() {
        override fun onLost(navigator: Navigator, requestCode: Int) {
            super.onLost(navigator, requestCode)
            logcat(Log.ERROR) { "Not found ${navigator.url} page, please check." }
            navPageNotFound(navigator.url.invalid())
        }

        override fun onArrival(navigator: Navigator) {
            super.onArrival(navigator)
            logcat { "${navigator.url} page, opened." }
        }
    })
}

/**
 * 导航到404页面
 *
 * @param url 导航失败url
 */
private fun navPageNotFound(url: String) {
    TheRouter.build(Address.PAGE_NOT_FOUND)
        .withString(ArgsKeys.PAGE_ADDRESS, url)
        .navigation()
}

/**
 * 发送空事件
 */
fun String.postEmptyEvent() {
    LiveEventBus.get(this, Any::class.java).post(null)
}

/**
 * 监听空事件
 */
fun String.observeEmptyEvent(owner: LifecycleOwner, callback: () -> Unit) {
    LiveEventBus.get(this, Any::class.java).observe(owner) { callback.invoke() }
}