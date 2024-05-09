package com.itoys.android.hybrid

import com.therouter.TheRouter

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/5/9
 */
object HybridRouter {

    /**
     * 导航到 hybrid
     */
    fun navHybrid(url: String) {
        TheRouter.build(HybridAddress.ADDRESS)
            .withString(HybridArgs.ARGS_KEY_URL, url)
            .navigation()
    }

    /**
     * 导航到 hybrid
     */
    fun navHybrid(url: String, title: String) {
        TheRouter.build(HybridAddress.ADDRESS)
            .withString(HybridArgs.ARGS_KEY_URL, url)
            .withString(HybridArgs.ARGS_KEY_TITLE, title)
            .navigation()
    }
}