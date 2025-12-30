package com.itoys.android.simple.upload

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/4/18
 */
data class QNToken(
    @JsonProperty("domain")
    val domain: String?,
    @JsonProperty("token")
    val token: String?,
)
