package com.itoys.android.plugin.env

import org.gradle.kotlin.dsl.provideDelegate

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/23
 */
class ProdEnv : IEnv() {

    companion object {
        val INSTANCE: IEnv by lazy { ProdEnv() }
    }

    override fun apiUrl() = stringValue(value = "http://panda.tianhuo.vip:8100/")

    override fun loginSalt() = stringValue(value = "Basic YXBwOmFwcF9zZWNyZXQ=")

    override fun umengKey() = ""

    override fun wxPayKey() = ""

    override fun aMapKey() = "2b6d68f948fa142ab08eff457ccf8c0f"
}