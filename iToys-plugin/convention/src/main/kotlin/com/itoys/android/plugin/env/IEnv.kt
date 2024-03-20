package com.itoys.android.plugin.env

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/23
 */
abstract class IEnv {

    /**
     * 接口 api 地址
     */
    abstract fun apiUrl(): String

    /**
     * 登录-盐
     */
    abstract fun loginSalt(): String

    /**
     * 友盟 key
     */
    abstract fun umengKey(): String

    /**
     * 微信支付 key
     */
    abstract fun wxPayKey(): String

    /**
     * 高德地图 key
     */
    abstract fun aMapKey(): String

    fun stringValue(value: String) = "\"${value}\""
}