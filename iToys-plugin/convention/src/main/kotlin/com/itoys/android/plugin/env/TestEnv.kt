package com.itoys.android.plugin.env

import org.gradle.kotlin.dsl.provideDelegate

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/23
 */
class TestEnv : IEnv() {

    companion object {
        val INSTANCE: IEnv by lazy { TestEnv() }
    }

    override fun apiUrl() = stringValue(value = "http://panda.tianhuo.vip:8100/")

    override fun loginSalt() = stringValue(value = "Basic YXBwOmFwcF9zZWNyZXQ=")

    override fun umengKey() = ""

    override fun wxPayKey() = ""

    override fun aMapKey() = "38a0785152f2d43cbbf1d533a45975e0"
}