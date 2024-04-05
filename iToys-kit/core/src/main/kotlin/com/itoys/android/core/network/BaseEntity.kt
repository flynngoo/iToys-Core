package com.itoys.android.core.network

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author Fanfan.gu <a href="mailto:fanfan.work@outlook.com">Contact me.</a>
 * @date 17/03/2023
 * @desc 结果请求基类
 */

class BaseEntity<T>(
    @JsonProperty("code")
    @JsonAlias("status")
    var errorCode: Int = -1,
    @JsonProperty("msg")
    @JsonAlias("message")
    var msg: String? = null,
    @JsonProperty("data")
    var data: T? = null,
    @JsonProperty("timestamp")
    var timestamp: Long = 0,
)

class BaseListEntity<T>(
    @JsonProperty("code")
    @JsonAlias("status")
    var errorCode: Int = -1,
    @JsonProperty("msg")
    @JsonAlias("message")
    var msg: String? = null,
    @JsonProperty("data")
    var data: List<T>? = null,
    @JsonProperty("timestamp")
    var timestamp: Long = 0,
)

class PageEntity<T>(
    @JsonProperty("size")
    @JsonAlias("pageSize")
    val pageNum: Int,
    @JsonProperty("pages")
    val pages: Int,
    @JsonProperty("current")
    @JsonAlias("pageNum")
    val current: Int,
    @JsonProperty("total")
    val total: Int,
    @JsonProperty("records")
    @JsonAlias("list")
    val list: List<T>?,
)

class BasePageEntity<T>(
    @JsonProperty("code")
    @JsonAlias("status")
    var errorCode: Int = -1,
    @JsonProperty("msg")
    @JsonAlias("message")
    var msg: String? = null,
    @JsonProperty("data")
    var data: PageEntity<T>? = null,
    @JsonProperty("timestamp")
    var timestamp: Long = 0,
)

data class FilePath(
    @JsonProperty("id")
    val id: String?,
    @JsonProperty("path")
    val path: String?
)