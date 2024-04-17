package com.itoys.android.core.crash

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/4/17
 */
interface ICrashHandler {

    /**
     * 是否处理完成崩溃 【设置了这个之后，将退出崩溃处理】
     *
     * @param isHandled 是否已处理完毕
     */
    fun setIsHandledCrash(isHandled: Boolean)

    /**
     * 是否需要重启程序 【设置了这个之后，在退出崩溃处理之后将自动重启程序】
     *
     * @param isNeedReopen
     */
    fun setIsNeedReopen(isNeedReopen: Boolean)
}