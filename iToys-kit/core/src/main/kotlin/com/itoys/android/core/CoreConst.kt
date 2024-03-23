package com.itoys.android.core

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/3/23
 */

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