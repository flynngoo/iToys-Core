package com.itoys.android.plugin.env

import org.gradle.kotlin.dsl.provideDelegate

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/23
 */
class DevEnv : IEnv() {

    companion object {
        val INSTANCE: IEnv by lazy { DevEnv() }
    }

    /** 内网 */
    override fun apiUrl() = stringValue(value = "http://192.168.1.200:8807/")

    /** 外网 */
    // override fun apiUrl() = stringValue(value = "http://panda.tianhuo.vip:8100/")

    override fun loginSalt() = stringValue(value = "Basic YXBwOmFwcF9zZWNyZXQ=")

    override fun umengKey() = stringValue(value = "")

    override fun wxPayKey() = stringValue(value = "")

    override fun aMapKey() = "c087f63470df6280cf99087d62b0b2f5"
}