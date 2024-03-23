package com.itoys.android.utils

import com.itoys.android.utils.expansion.isBlank
import com.itoys.android.utils.expansion.size
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 06/04/2022 22:35
 * @desc 身份证合法性校验
 * 18位身份证号码：第7、8、9、10位为出生年份(四位数)，第11、第12位为出生月份，第13、14位代表出生日期，第17位代表性别，奇数为男，偶数为女;
 */
object IdCardValidatorUtil {

    /** 每位加权因子 */
    private val power = listOf(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2)

    @JvmStatic
    fun isValidatedAllIdCard(idCard: String?) : Boolean {
        if (idCard.size() < 15) return false

        var realIdCard: String = idCard!!
        if (idCard.size() == 15)
            realIdCard = convertIdCarBy15bit(realIdCard)

        if (idCard.size() == 18) {
            return isValidate18IdCard(realIdCard)
        }

        return false
    }

    private fun isValidate18IdCard(idCard: String): Boolean {
        val idCard17 = idCard.substring(0, 17)
        val idCardCode = idCard.substring(17, 18)

        var checkCode = ""

        if (!isDigital(idCard17)) {
            return false
        }

        val c = idCard17.toCharArray()
        if (c.isNotEmpty()) {
            val bit = convertCharToInt(c)
            val sum17 = getPowerSum(bit)
            checkCode = getCheckCodeBySum(sum17)
            if (checkCode.isBlank()) {
                return false
            }

            if (!idCardCode.equals(checkCode, ignoreCase = true)) {
                return false
            }

            return true
        }

        return false
    }

    private fun convertIdCarBy15bit(idCard: String): String {
        var idCard17 = ""

        if (isDigital(idCard)) {
            val birthday = idCard.substring(6, 12)

            try {
                val birthdate = SimpleDateFormat("yyMMdd", Locale.getDefault()).parse(birthday)
                if (birthdate != null) {
                    val calendar = Calendar.getInstance()
                    calendar.time = birthdate
                    val year = calendar.get(Calendar.YEAR).toString()
                    idCard17 = idCard.substring(0, 6) + year + idCard.substring(8)
                    val c = idCard17.toCharArray()
                    var checkCode = ""

                    if (c.isNotEmpty()) {
                        val bit = convertCharToInt(c)
                        val sum17 = getPowerSum(bit)

                        checkCode = getCheckCodeBySum(sum17)

                        if (checkCode.isBlank()) {
                            return ""
                        }

                        idCard17 += checkCode
                    }
                }
            } catch (e: Exception) {
                return ""
            }
        }

        return idCard17
    }

    private fun isDigital(str: String): Boolean {
        return str.matches("^[0-9]*$".toRegex())
    }

    private fun convertCharToInt(array: CharArray): Array<Int> {
        val bit = Array(array.size) { 0 }
        array.forEachIndexed { index, char ->
            bit[index] = char.toString().toInt()
        }
        return bit
    }

    /**
     * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
     */
    private fun getPowerSum(bit: Array<Int>): Int {
        var sum = 0

        if (power.size != bit.size) {
            return sum
        }

        bit.forEachIndexed { bitIndex, bitInt ->
            sum += bitInt * power[bitIndex]
        }

        return sum
    }

    /**
     * 将和值与11取模得到余数进行校验码判断
     */
    private fun getCheckCodeBySum(sum17: Int): String {
        return when(sum17 % 11) {
            10 -> "2"
            9 -> "3"
            8 -> "4"
            7 -> "5"
            6 -> "6"
            5 -> "7"
            4 -> "8"
            3 -> "9"
            2 -> "X"
            1 -> "0"
            0 -> "1"
            else -> ""
        }
    }
}