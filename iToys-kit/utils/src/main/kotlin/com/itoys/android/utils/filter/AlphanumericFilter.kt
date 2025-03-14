package com.itoys.android.utils.filter

import android.text.InputFilter
import android.text.Spanned
import com.itoys.android.utils.expansion.invalid
import com.itoys.android.utils.expansion.isAlphanumeric
import com.itoys.android.utils.expansion.isBlank

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/4/14
 * @Description 字母+数字过滤
 */
class AlphanumericFilter : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        if (source.isAlphanumeric()) {
            return source
        }

        return ""
    }
}