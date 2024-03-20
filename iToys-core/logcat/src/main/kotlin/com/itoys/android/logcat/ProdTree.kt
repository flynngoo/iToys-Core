package com.itoys.android.logcat

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/20
 */
class ProdTree : LoggerTree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        // 控制Release包不打印日志
    }
}