package com.itoys.android.utils.filter

import android.text.InputFilter
import android.text.Spanned
import com.itoys.android.utils.expansion.idCardChar
import com.itoys.android.utils.expansion.invalid
import com.itoys.android.utils.expansion.isBlank

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/30
 */
class IdCardInputFilter : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence {
        if (source.isBlank()) return ""
        val char = source.invalid()

        if (!Character.isLetterOrDigit(char[start])) {
            return ""
        }

        if (!char.idCardChar()) {
            return ""
        }

        return char
    }
}