package com.itoys.android.hybrid

import android.app.Application
import com.itoys.android.image.loadImage
import com.itoys.android.logcat.logcat
import com.itoys.android.utils.expansion.invalid
import com.viatris.hybrid.webview.ViaWebkit

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/5/9
 */
object HybridInitialization {

    /**
     * 初始化.
     */
    fun initialization(application: Application, debug: Boolean = true) {
        ViaWebkit.init(application.applicationContext)
        ViaWebkit.setDebugging(debug)
        // 设置JSBridge服务
        ViaWebkit.setService(HybridService::class.java)
        // 设置图片加载器
        ViaWebkit.setImageLoader { _, imageView, url ->
            imageView.loadImage(url)
        }
        ViaWebkit.setJSEventListener { event ->
            // 此处可以用EventBus或者RxBus将消息发送出去，然后再对应的原生界面注册监听即可与WebView通讯
            logcat { "JS Event -> ${event?.eventName.invalid()}" }
        }
    }
}