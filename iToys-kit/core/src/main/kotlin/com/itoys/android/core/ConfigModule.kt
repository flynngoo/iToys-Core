package com.itoys.android.core

import com.itoys.android.core.network.ApiResultCode
import com.itoys.android.core.network.GlobalHttpHandler
import com.itoys.android.core.network.INetworkDependency

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/3/24
 */
class GlobalConfig(
    val debug: Boolean,
    val apiHostUrl: String?,
    val apiSuccessfulCode: Int,
    val resourcesHostUrl: String?,
    val networkDependency: INetworkDependency?,
    val globalHttpHandler: GlobalHttpHandler,
) {

    companion object {

        @JvmStatic
        fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    class Builder {

        /** debug模式 */
        private var debug: Boolean = false

        /** api host url */
        private var apiHostUrl: String? = null

        /** api成功code */
        private var apiSuccessfulCode = ApiResultCode.SUCCESSFUL

        /** 资源 host url */
        private var resourcesHostUrl: String? = null

        /** 网络依赖 */
        private var networkDependency: INetworkDependency? = null

        /** 全局http请求处理类 */
        private var globalHttpHandler: GlobalHttpHandler = GlobalHttpHandler.DEFAULT

        fun debugMode(debug: Boolean): Builder {
            this.debug = debug
            return this
        }

        /**
         * 设置api host url
         */
        fun apiHostUrl(url: String): Builder {
            apiHostUrl = url
            return this
        }

        fun apiSuccessfulCode(code: Int): Builder {
            apiSuccessfulCode = code
            return this
        }

        /**
         * 设置资源 host url
         */
        fun resourcesHostUrl(url: String): Builder {
            resourcesHostUrl = url
            return this
        }

        /**
         * 设置全局http请求处理类
         */
        fun globalHttpHandler(handler: GlobalHttpHandler): Builder {
            this.globalHttpHandler = handler
            return this
        }

        /**
         * 设置网络依赖
         */
        fun networkDependency(networkDependency: INetworkDependency): Builder {
            this.networkDependency = networkDependency
            return this
        }

        /**
         * 分页配置
         *
         * @param pageKey 页码key
         * @param pageSizeKey 分页数量key
         * @param searchKey 搜索关键字key
         */
        fun pagingConfig(
            pageKey: String = "current",
            pageSizeKey: String = "size",
            searchKey: String = "keywords",
            countdownTimerFuture: Long = 1500L
        ) : Builder {
            CoreConfig.pageKey = pageKey
            CoreConfig.pageSizeKey = pageSizeKey
            CoreConfig.searchKey = searchKey
            CoreConfig.searchCountdownTimerFuture = countdownTimerFuture
            return this
        }

        fun build(): GlobalConfig {
            return GlobalConfig(
                this.debug,
                this.apiHostUrl,
                this.apiSuccessfulCode,
                this.resourcesHostUrl,
                this.networkDependency,
                this.globalHttpHandler
            )
        }
    }
}