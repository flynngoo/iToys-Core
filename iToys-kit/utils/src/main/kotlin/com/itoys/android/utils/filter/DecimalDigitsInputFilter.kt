package com.itoys.android.utils.filter

import android.text.InputFilter
import android.text.Spanned
import com.itoys.android.utils.expansion.invalid
import com.itoys.android.utils.expansion.isBlank
import java.util.regex.Pattern

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/4/14
 */
class DecimalDigitsInputFilter(private val decimalDigits: Int) : InputFilter {

    private val pattern: Pattern

    init {
        val regex = String.format("[0-9]*+(\\.[0-9]{0,%d})?", decimalDigits)
        pattern = Pattern.compile(regex)
    }

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        if ("." == source && dest.isBlank()) {
            return "0."
        }

        val newText = StringBuilder(dest.invalid())
        if (source.isBlank()) {
            newText.replace(dstart, dend, "")
        } else {
            newText.insert(dstart, source)
        }

        val matcher = pattern.matcher(newText)
        if (!matcher.matches()) {
            return ""
        }

        return source
    }
}