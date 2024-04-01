package com.itoys.android.core.network

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/14
 */
class RequestAction<T> {

    /**
     * 开始请求
     */
    var start: ((Boolean) -> Unit)? = null
        private set

    /**
     * 发起请求
     */
    var request: (suspend () -> T?)? = null
        private set

    /**
     * 请求成功
     */
    var success: ((T?) -> Unit)? = null
        private set

    /**
     * 请求失败
     */
    var failure: ((ResultException) -> Unit)? = null
        private set

    /**
     * 请求结束
     */
    var finish: (() -> Unit)? = null
        private set

    fun request(block: suspend () -> T?) {
        request = block
    }

    fun start(block: (Boolean) -> Unit) {
        start = block
    }

    fun success(block: (T?) -> Unit) {
        success = block
    }

    fun failure(block: (ResultException) -> Unit) {
        failure = block
    }

    fun finish(block: () -> Unit) {
        finish = block
    }
}
