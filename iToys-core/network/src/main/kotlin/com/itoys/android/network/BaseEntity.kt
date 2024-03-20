package com.itoys.android.network

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author Fanfan.gu <a href="mailto:fanfan.work@outlook.com">Contact me.</a>
 * @date 17/03/2023
 * @desc 结果请求基类
 */

class BaseEntity<T>(
    @JsonProperty("code")
    var errorCode: Int = -1,
    @JsonProperty("msg")
    var msg: String? = null,
    @JsonProperty("data")
    var data: T? = null,
    @JsonProperty("timestamp")
    var timestamp: Long = 0,
)

class PageEntity<T>(
    @JsonProperty("size")
    val pageNum: Int,
    @JsonProperty("pages")
    val pages: Int,
    @JsonProperty("current")
    val current: Int,
    @JsonProperty("total")
    val total: Int,
    @JsonProperty("records")
    val list: List<T>?,
)

data class FilePath(
    @JsonProperty("id")
    val id: String?,
    @JsonProperty("path")
    val path: String?
)

/**
 * 字典
 */
data class DictModel(
    @JsonProperty("dictKey")
    val dictKey: Int?,
    @JsonProperty("dictValue")
    val dictValue: String?,
)

fun Iterable<DictModel>.toDictValueList(): List<String?> = map(DictModel::dictValue)

enum class Sex(val sex: Int, val text: String) {

    /** 男 */
    Male(1, "男"),

    /** 女 */
    Female(2, "女");

    companion object {
        fun sex(sex: Int?): String {
            return when (sex) {
                Male.sex -> Male.text
                Female.sex -> Female.text
                else -> ""
            }
        }
    }
}

enum class Whether(val whether: Int, val text: String, val checked: Boolean) {

    /** 是 */
    Yes(0, "是", true),

    /** 否 */
    No(1, "否", false);

    companion object {
        fun whether(whether: Int?): String {
            return when (whether) {
                Yes.whether -> Yes.text
                No.whether -> No.text
                else -> ""
            }
        }
    }
}