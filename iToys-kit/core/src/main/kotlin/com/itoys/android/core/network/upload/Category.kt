package com.itoys.android.core.network.upload

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2024/1/1
 */
enum class Category(val category: String) {
    /** 头像图片 */
    Avatar("avatar"),

    /** 部门图片 */
    Department("department"),

    /** 房间图片 */
    Housing("housing"),

    /** 工单图片 */
    Ticket("ticket"),

    /** 合同图片 */
    Contract("contract"),

    /** 文件 */
    File(""),
}

enum class TokenType(val type: Int) {

    /** 私有 */
    Private(1),

    /** 公开 */
    Public(2),
}