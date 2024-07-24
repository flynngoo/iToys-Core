package com.itoys.android.uikit.components.upgrade

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonProperty
import com.itoys.android.utils.expansion.invalid

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/7/5
 */
data class UpgradeModel(
    @JsonProperty("isIgnorable")
    @JsonAlias("force", "isUpdate")
    val isIgnorable: Boolean?, // 是否可以忽略
    @JsonProperty("versionCode")
    @JsonAlias("version")
    val versionCode: Int?,
    @JsonProperty("versionName")
    @JsonAlias("name")
    val versionName: String?,
    @JsonProperty("updateLog")
    val upgradeLog: String?, // 升级日志
    @JsonProperty("apkUrl")
    @JsonAlias("downAddress")
    val apkUrl: String?, // apk url
    @JsonProperty("packageSize")
    @JsonAlias("apkSize")
    val apkSize: String?,
    @JsonProperty("md5")
    val md5: String? // md5 用来校验
)

/**
 * upgrade file name
 */
fun UpgradeModel?.filename() = "${this?.versionName.invalid()}-${this?.versionCode ?: 0}.apk"