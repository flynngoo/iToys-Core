package com.itoys.android.uikit

import com.itoys.android.uikit.components.empty.EmptyConfig
import com.itoys.android.uikit.components.empty.StateChangedHandler

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/22
 */
object UikitBridge {

    /**
     * Empty 配置
     */
    fun emptyConfig(): EmptyConfig {
        return UikitInitialization.requireEmptyConfig()
    }

    /**
     * Empty - 空状态改变处理
     */
    fun getEmptyChangedHandler(): StateChangedHandler {
        return emptyConfig().stateChangedHandler()
    }

    /**
     * Empty - 加载中布局
     */
    fun emptyLoadingLayout(): Int {
        return emptyConfig().loadingLayout()
    }

    /**
     * Empty - 加载中布局
     */
    fun emptyLayout(): Int {
        return emptyConfig().emptyLayout()
    }

    /**
     * Empty - 自定义图片
     */
    fun emptyImage(): Int {
        return emptyConfig().emptyImage()
    }

    /**
     * Empty - 自定义描述
     */
    fun emptyDescText(): Int {
        return emptyConfig().emptyDescText()
    }

    /**
     * Empty - 自定义错误图片
     */
    fun emptyErrorImage(): Int {
        return emptyConfig().errorImage()
    }

    /**
     * Empty - 自定义错误描述
     */
    fun emptyErrorDescText(): Int {
        return emptyConfig().errorDescText()
    }

    /**
     * Empty - 重试id list
     */
    fun emptyRetryIds(): IntArray {
        return emptyConfig().retryIds()
    }
}