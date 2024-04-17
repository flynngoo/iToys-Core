package com.itoys.android.utils.regex

import com.itoys.android.utils.expansion.isBlank
import com.itoys.android.utils.regex.Const.BUSINESS_LICENSE

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/4/15
 */
object RegexUtils {

    /**
     * 营业执照 统一社会信用代码（18位）
     * @param license
     * @return
     */
    fun isBusinessLicenseLength18(license: String): Boolean {
        if (license.isBlank() || license.length != 18 || !license.matches(BUSINESS_LICENSE.toRegex())) {
            return false
        }

        val str = "0123456789ABCDEFGHJKLMNPQRTUWXY"
        val ws = intArrayOf(1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28)
        val codes = arrayOfNulls<String>(2)
        codes[0] = license.substring(0, license.length - 1)
        codes[1] = license.substring(license.length - 1, license.length)

        var sum = 0
        for (i in 0..16) {
            sum += str.indexOf(codes[0]!![i]) * ws[i]
        }

        var c18 = 31 - sum % 31
        if (c18 == 31) {
            c18 = 0
        }

        return str[c18] == codes[1]!![0]
    }
}