package com.itoys.android.image.glide.progress

import android.text.TextUtils
import com.itoys.android.image.glide.progress.ProgressResponseBody.InternalProgressListener
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.ConcurrentHashMap

/**
 * 进度监听器管理类
 * 加入图片加载进度监听，加入Https支持\
 */
object ProgressManager {
    private val listenersMap = ConcurrentHashMap<String, OnProgressListener>()

    /**glide 下载进度的主要逻辑 需要在GlideModule注入*/
    fun OkHttpClient.Builder.glideProgressInterceptor(): OkHttpClient.Builder =
        this.addNetworkInterceptor { chain: Interceptor.Chain ->
            val request = chain.request()
            val response = chain.proceed(request)
            response.newBuilder().run {
                val body = response.body
                if (body != null) {
                    this.body(ProgressResponseBody(request.url.toString(), LISTENER, body))
                }
                this.build()
            }
        }


    private val LISTENER = object : InternalProgressListener {
        override fun onProgress(url: String, bytesRead: Long, totalBytes: Long) {
            getProgressListener(url)?.let {
                val percentage = (bytesRead * 1f / totalBytes * 100f).toInt()
                val isComplete = percentage >= 100
                it.invoke(isComplete, percentage, bytesRead, totalBytes)
                if (isComplete) {
                    removeListener(url)
                }
            }
        }
    }

    fun addListener(url: String, listener: OnProgressListener) {
        if (!TextUtils.isEmpty(url) && listener != null) {
            listenersMap[url] = listener
            listener.invoke(false, 1, 0, 0)
        }
    }

    fun removeListener(url: String) {
        if (!TextUtils.isEmpty(url)) {
            listenersMap.remove(url)
        }
    }

    fun getProgressListener(url: String?): OnProgressListener {
        return if (TextUtils.isEmpty(url) || listenersMap.size == 0) {
            null
        } else listenersMap[url]
    }
}