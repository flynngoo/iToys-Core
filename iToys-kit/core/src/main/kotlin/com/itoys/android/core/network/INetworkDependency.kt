package com.itoys.android.core.network

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/13
 */
interface INetworkDependency {

    /**
     * 平台
     */
    fun platform(): String

    /**
     * 获取当前版本号
     */
    fun appVersion(): Long

    /**
     * 获取当前版本名称
     */
    fun appVersionName(): String

    /**
     * token key
     */
    fun tokenKey() : String

    /**
     * token
     */
    fun token(): String

    /**
     * 请求头
     */
    fun headers(): Map<String, String>

    /**
     * 特殊请求url
     *
     * 在特殊请求url里, 会带上特殊请求头
     */
    fun specialUrls(): List<String>

    /**
     * 特殊请求头
     */
    fun specialHeaders(): Map<String, String>
}