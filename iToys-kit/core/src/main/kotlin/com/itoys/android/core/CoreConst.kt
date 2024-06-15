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
) {
    // 选中状态
    var selected = false
}

fun DictModel?.dictKey() = this?.dictKey ?: 0

fun Iterable<DictModel>.toDictValueList(): List<String?> = map(DictModel::dictValue)

/**
 * 性别枚举
 *
 * @param code 性别值
 * @param description 性别描述
 */
enum class Gender(val code: Int, val description: String) {
    /**
     * 男
     */
    MALE(1, "男"),

    /**
     * 女
     */
    FEMALE(2, "女"),

    /**
     * 其他
     */
    OTHER(3, "其他");



    companion object {

        /**
         * 根据性别值获取对应的枚举描述
         *
         * @param code 性别值
         * @return 对应的枚举描述
         */
        fun getDescription(code: Int?): String {
            return when (code) {
                MALE.code -> MALE.description
                FEMALE.code -> FEMALE.description
                else -> OTHER.description
            }
        }

        /**
         * 根据性别值获取对应的枚举值
         *
         * @param code 性别值
         * @return 对应的枚举值
         */
        fun fromCode(code: Int?): Gender {
            return when (code) {
                MALE.code -> MALE
                FEMALE.code -> FEMALE
                else -> OTHER
            }
        }
    }
}
