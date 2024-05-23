package com.itoys.android.utils.expansion

import com.itoys.android.utils.IdCardValidatorUtil
import com.itoys.android.utils.regex.Const.EMAIL
import com.itoys.android.utils.regex.Const.EMOJI
import com.itoys.android.utils.regex.Const.EXACT_MOBILE
import com.itoys.android.utils.regex.Const.HIDE_PHONE
import com.itoys.android.utils.regex.Const.ID_CARD_CHAR
import com.itoys.android.utils.regex.Const.LANDLINE_PHONE
import com.itoys.android.utils.regex.Const.SIMPLE_MOBILE
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.regex.Pattern
import kotlin.reflect.KClass

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/21
 */

/**
 * class tag name.
 */
val Class<out Any>.tagName: String
    get() = this.canonicalName ?: ""

/**
 * class tag name.
 */
val KClass<out Any>.tagName: String
    get() = this.java.canonicalName ?: ""

/**
 * 判断字符串是否为null或全为空格
 */
fun CharSequence?.isBlank(): Boolean {
    return (null == this || this.toString().trimString().size() == 0 || this == "null")
}

fun CharSequence?.isNotBlank(): Boolean {
    return !isBlank()
}

/**
 * 判断字符串是否有效, 如果为空默认返回长度为0的字符串
 */
fun CharSequence?.invalid(invalid: String = ""): String {
    return isBlank().then({ invalid }, { this.toString() })
}

/**
 * 字符串去掉空格, 如果字符串为空返回长度为0的字符串
 */
fun String?.trimString(): String {
    return this?.trim() ?: ""
}

/**
 * 去除字符串中的空格
 */
fun String?.removeSpace(): String {
    return this?.replace("\\s".toRegex(), "") ?: ""
}

/**
 * 字符串长度, 如果字符串为空返回0
 */
fun CharSequence?.size(): Int {
    return this?.length ?: 0
}

/**
 * @param addComma 是否需要添加逗号，默认不加
 * @param modeFloor 是否使用去尾法，默认true 1.5->1   2.8->2
 * @param decimalNum 小数点后位数
 */
fun String.formatNumber(
    addComma: Boolean = false,
    modeFloor: Boolean = true,
    decimalNum: Int? = DEFAULT_DECIMAL_NUMBER
): String = this.toBigDecimalWithNull().formatNumber(addComma, modeFloor, decimalNum)

fun String?.toBigDecimalWithNull(default: BigDecimal = BigDecimal.ZERO) =
    isNotBlank().then({
        try {
            this.invalid("0").toBigDecimal()
        } catch (e: NumberFormatException) {
            default
        }
    }, default)

fun String?.toIntWithNull(default: Int = 0) =
    isNotBlank().then({
        try {
            this.invalid("0").toInt()
        } catch (e: NumberFormatException) {
            default
        }
    }, default)

fun String?.toFloatWithNull(default: Float = 0f) =
    isNotBlank().then({
        try {
            this.invalid("0").toFloat()
        } catch (e: NumberFormatException) {
            default
        }
    }, default)

fun String?.toDoubleWithNull(default: Double = 0.00) =
    isNotBlank().then({
        try {
            this.invalid("0").toDouble()
        } catch (e: NumberFormatException) {
            default
        }
    }, default)

/**
 * 安全的 to int
 */
fun String?.safeParseInt() = try {
    this.invalid("0").toInt()
} catch (e: NumberFormatException) {
    0
}

/**
 * 安全的 to double
 */
fun String?.safeParseDouble() = try {
    this.invalid("0").toDouble()
} catch (e: NumberFormatException) {
    0.0
}

/**
 * 手机号-简单校验
 */
fun CharSequence?.simpleMobile() = this.isNotBlank() && SIMPLE_MOBILE.toRegex().matches(this.invalid())

/**
 * 手机号-精确校验
 */
fun CharSequence?.exactMobile() = this.isNotBlank() && EXACT_MOBILE.toRegex().matches(this.invalid())

/**
 * 座机-校验
 */
fun CharSequence?.landlinePhone() = this.isNotBlank() && LANDLINE_PHONE.toRegex().matches(this.invalid())

/**
 * 隐藏手机号中间4位
 */
fun CharSequence?.hidePhoneNumber(invalid: String = ""): String {
    if (isBlank()) return invalid

    if (size() < 11) return this.invalid(invalid)

    return this?.replace(HIDE_PHONE.toRegex(), "$1****$2").invalid()
}

/**
 * 身份证号
 */
fun CharSequence?.idCard() = IdCardValidatorUtil.isValidatedAllIdCard(this.invalid())

/**
 * 身份证号 字符
 */
fun CharSequence?.idCardChar() = ID_CARD_CHAR.toRegex().matches(this.invalid())

/**
 * 邮箱
 */
fun CharSequence?.email() = this.isNotBlank() && EMAIL.toRegex().matches(this.invalid())

/**
 * 表情
 */
fun CharSequence?.isEmoji() = Pattern.compile(EMOJI, Pattern.UNICODE_CASE or Pattern.CASE_INSENSITIVE).matcher(this.invalid()).find()

/**
 * 空字符串
 */
fun String.Companion.empty() = ""

// 数字与汉字大写的映射
val numberDigitMap = mapOf(
    '0' to "零", '1' to "壹", '2' to "贰", '3' to "叁", '4' to "肆",
    '5' to "伍", '6' to "陆", '7' to "柒", '8' to "捌", '9' to "玖"
)

// 定义汉字数字和单位
val numbersCN = arrayOf("零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖")
// 单位映射
val currencyUnits = listOf("元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟")

fun String.toCNY(): String {
    val amount = BigDecimal(this)

    // 将金额拆解为整数和小数部分
    val wholePart = amount.toLong()
    val decimalPart = amount.subtract(BigDecimal(wholePart)).setScale(2, RoundingMode.HALF_UP).multiply(BigDecimal("100")).toInt()

    val sb = StringBuilder()

    if (wholePart == 0L && decimalPart == 0) {
        sb.insert(0, "${numbersCN[0]}${currencyUnits[0]}")
        return sb.toString()
    }

    // 标记是否上一位数字为零
    var lastNonZero = false

    // 处理整数部分
    if (wholePart != 0L) {
        val wholePartReversed = wholePart.string().reversed()
        for (index in wholePartReversed.indices) {
            val digit = wholePartReversed[index].toString().toInt()
            val unitDigit = index % 12

            insertChineseYuan(wholePart, sb, digit, unitDigit)

            if (digit == 0 && lastNonZero) {
                sb.insert(0, numbersCN[digit])
            }

            lastNonZero  = digit != 0
        }

        // 添加元
        sb.append(currencyUnits[0])
    }

    // 处理小数部分
    if (decimalPart > 0) {
        if (decimalPart / 10 != 0) {
            sb.append(numbersCN[decimalPart / 10])
            sb.append("角")
        }

        if (decimalPart % 10 != 0) {
            sb.append(numbersCN[decimalPart % 10])
            sb.append("分")
        }
    } else {
        sb.append("整")
    }

    return sb.toString()
}

/**
 * 将数字转换为中文数字
 */
private fun insertChineseYuan(wholePart: Long, sb: StringBuilder, digit: Int, unitDigit: Int) {
    when {
        digit != 0 -> sb.insert(0, chineseYuan(digit, unitDigit))
        unitDigit == 4 && wholePart < 100000000 -> sb.insert(0, chineseYuan(digit, unitDigit))
        unitDigit == 8 -> sb.insert(0, chineseYuan(digit, unitDigit))
    }
}

private fun chineseYuan(digit: Int, unitDigit: Int): String {
    return when {
        digit == 0 && unitDigit > 0 -> currencyUnits[unitDigit]
        digit > 0 && unitDigit == 0 -> numbersCN[digit]
        else -> "${numbersCN[digit]}${currencyUnits[unitDigit]}"
    }
}