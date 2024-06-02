package com.itoys.android.utils.expansion

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.max
import kotlin.math.pow

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/21
 */

/**
 * 默认保留小数点后10位
 */
const val DEFAULT_DECIMAL_NUMBER = 2

/**
 * 默认分隔符为千分位
 */
const val DEFAULT_SEPARATE_NUMBER = 3


/**
 * @param addComma 是否需要添加逗号，默认不加
 * @param modeFloor 是否使用去尾法，默认true 1.5->1   2.8->2
 * @param decimalNum 小数点后位数
 */
fun Number.formatNumber(
    addComma: Boolean = false,
    modeFloor: Boolean = true,
    decimalNum: Int? = DEFAULT_DECIMAL_NUMBER
): String {
    var decimal = decimalNum
    if (decimal == null) {
        decimal = DEFAULT_DECIMAL_NUMBER
    }
    val decimalFormat = DecimalFormat()
    decimalFormat.maximumFractionDigits = decimal
    decimalFormat.groupingSize = if (addComma) DEFAULT_SEPARATE_NUMBER else 0
    if (modeFloor) decimalFormat.roundingMode = RoundingMode.FLOOR
    return decimalFormat.format(this)
}

/**
 * if low using default value instead.
 */
fun Int.thanLess(defaultValue: Int = 0): Int = (this < defaultValue).then(defaultValue, this)

/**
 * 分 -> 元
 */
fun Int?.fenToYuan(): Double {
    if (this == null) return 0.00
    return BigDecimal(this).divide(BigDecimal(100)).toDouble()
}

/**
 * 元 -> 分
 */
fun Int?.toFen(): Int {
    if (this == null) return 0
    return 100 * this
}

/**
 * 元 -> 分
 */
fun Double?.toFen(): Int {
    if (this == null) return 0
    return (100 * this).toInt()
}

fun Number?.string(invalid: String = "0") = (this != null).then(
    "$this", invalid
)

/**
 * 向上取整到最接近的10的倍数
 */
fun Int.roundToNearestTen(): Int {
    val digits = this.countDigits()
    val base = max(10.0, 10.0.pow(digits - 2)).toInt()
    val base2 = 10.0.pow(digits - 3).toInt()

    return if (base > 100) {
        (this + 9 * base2) / base * base
    } else {
        (this + 9) / base * base
    }
}

/**
 * 使用对数运算整数位数
 */
fun Int.countDigits(): Int {
    if (this == 0) {
        return 1
    }
    return (floor(log10(this.toDouble())) + 1).toInt()
}

/**
 * 填充0
 */
fun Int.padZeroes(targetLength: Int = 2): String {
    return "%0${targetLength}d".format(this)
}

fun Number?.toCNY() = string().toCNY()

fun Number?.stripTrailingZeros(): String {
    val number = string()

    return (number.contains('.')).then(
        { number.removeSuffix("0").removeSuffix(".") },
        { number })
}

fun Number?.withYuan() = "${stripTrailingZeros()} 元"

/**
 * double plus double
 *
 * 避免精度丢失问题
 */
fun Double.plusDouble(other: Double): Double {
    val result = toBigDecimal().plus(other.toBigDecimal())
    return result.toDouble()
}