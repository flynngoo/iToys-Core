package com.itoys.android.utils.regex

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/30
 */
object Const {

    /**
     * email 正则
     */
    const val EMAIL = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+\$"

    /**
     * 手机号(精确) 正则
     */
    const val EXACT_MOBILE = "^((13[0-9])|(14[0,1,4-8])|(15[^4])|(16[2,5,6,7])|(17[^9])|(18[0-9])|(19[^4]))\\d{8}\$"

    /**
     * 手机号(简单) 正则
     */
    const val SIMPLE_MOBILE = "^[1]\\d{10}\$"

    /**
     * 隐藏手机号 正则
     */
    const val HIDE_PHONE = "(\\d{3})\\d{4}(\\d{4})"

    /**
     * 身份证号 正则
     */
    const val ID_CARD_CHAR = "^[1234567890Xx]+\$"
}