package com.itoys.android.utils.regex

import android.icu.lang.UProperty.EMOJI_MODIFIER

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/5/31
 */
object EmojiConst {


    /**
     * emoji匹配
     */
    const val EMOJI_CHARACTER = "[\\ud83c\\udc00-\\ud83e\\udfff]|[\\u2600-\\u27ff]"

    /**
     * 匹配变形符号
     */
    const val EMOJI_PRESENTATION_SELECTOR = "\\uFE0F"

    /**
     *匹配键帽
     */
    const val KEY_CAP_SYMBOL = "\\u20E3"

    /**
     * 键帽变体序列
     */
    const val KV_SEQUENCE = "[#*0-9]\\uFE0F?\\u20E3"

    /**
     * 匹配零宽字符
     */
    const val ZERO_WIDTH_JOINER = "\\u200D"

    /**
     * 正则
     */
    const val EMOJI_REGEX = "$KV_SEQUENCE|$ZERO_WIDTH_JOINER|$KEY_CAP_SYMBOL|$EMOJI_CHARACTER"
}