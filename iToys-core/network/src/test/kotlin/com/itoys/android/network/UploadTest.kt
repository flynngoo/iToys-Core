package com.itoys.android.network

import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Test

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2024/1/2
 */
class UploadTest {

    /**
     * 当前时间
     */
    suspend fun printTime() {
        delay(100L)
        println(System.currentTimeMillis())
    }

    @Test
    fun uploadListFile() = runTest {
        val list = listOf("1", "1", "1", "1", "1", "1", "1", "1", "1")
        list.forEach { _ -> printTime() }
    }
}