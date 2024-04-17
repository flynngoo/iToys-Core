package com.itoys.android.core.crash

import android.content.Context
import android.util.Log
import com.itoys.android.logcat.asLog
import com.itoys.android.logcat.logcat

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/4/17
 */
class CrashHandler : Thread.UncaughtExceptionHandler, ICrashHandler {

    companion object {
        private val INSTANCE: CrashHandler by lazy { CrashHandler() }

        fun instance(): CrashHandler = INSTANCE
    }

    private var context: Context? = null

    private var defaultHandler: Thread.UncaughtExceptionHandler? = null

    private var crashListener: OnCrashListener? = null

    /**
     * 初始化
     */
    fun initialization(context: Context): CrashHandler {
        this.context = context
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
        return this
    }

    /**
     * 设置崩溃监听
     *
     * @param listener
     * @return
     */
    fun setOnCrashListener(listener: OnCrashListener) {
        crashListener = listener
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        logcat(priority = Log.ERROR) { e.asLog() }

        val isHandled = crashListener?.onCrash(context, this, e)

        if (!handleException(e) && defaultHandler != null || isHandled != true) {
            defaultHandler?.uncaughtException(t, e)
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param throwable 出错信息
     * @return {@code true}:处理了该异常信息;<br>{@code false}:该异常信息未处理。
     */
    private fun handleException(throwable: Throwable?):Boolean {
        if (throwable == null || context == null || crashListener == null) return false

        return true
    }

    override fun setIsHandledCrash(isHandled: Boolean) {
    }

    override fun setIsNeedReopen(isNeedReopen: Boolean) {
    }
}