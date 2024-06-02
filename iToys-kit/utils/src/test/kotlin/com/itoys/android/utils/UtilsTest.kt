package com.itoys.android.utils

import com.itoys.android.utils.expansion.email
import com.itoys.android.utils.expansion.isEmoji
import com.itoys.android.utils.expansion.landlinePhone
import com.itoys.android.utils.expansion.plusDouble
import com.itoys.android.utils.expansion.roundToNearestTen
import com.itoys.android.utils.expansion.simpleMobile
import com.itoys.android.utils.expansion.string
import com.itoys.android.utils.expansion.stripTrailingZeros
import com.itoys.android.utils.expansion.toCNY
import com.itoys.android.utils.regex.Const
import com.itoys.android.utils.regex.EmojiConst
import org.junit.Test
import java.util.regex.Pattern

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2024/1/10
 */
class UtilsTest {

    /**
     * 邮箱正则
     */
    @Test
    fun runEmailRegex() {
        assert("abcd123@qq.com".email())
    }

    /**
     * 手机/座机号正则
     */
    @Test
    fun runPhoneRegex() {
        val phone = "1213232781"

        assert(phone.simpleMobile() || phone.landlinePhone())
    }

    /**
     * list to string
     */
    @Test
    fun runListToString() {
        val numbers = listOf(11, 22, 3, 41, 52, 6)
        val string = numbers.joinToString(separator = "\n")
        print(string)
    }

    /**
     * string split.
     */
    @Test
    fun stringSplit() {
        val ids = "1, 2, 3, 4"
        println(ids.split(", ")[0])
    }

    /**
     * Map sort
     */
    @Test
    fun testMapSorted() {

        /* val unsortedMap = mapOf("c" to 1, "b" to 2, "a" to 3)

        val sortedMap = unsortedMap.toSortedMap(compareBy { it })

        println(sortedMap) */

        // 初始化
        val map = mapOf(2 to "a", 1 to "c", 3 to "d", 5 to "q", 4 to "1")
        // 转换的核心代码
        val toMap = map.toList().sortedBy { it.first }.toMap()
        // 输出代码
        println(toMap)
    }

    @Test
    fun listCopy() {
        val cities = listOf("Berlin", "Munich", "Hamburg")
        val copied = cities.toCollection(mutableListOf())
        println(copied.containsAll(cities))
    }

    @Test
    fun roundToNearestTen() {
        val number = 16675

        println("Rounded up to the nearest multiple of 10 is ${number.roundToNearestTen()}")
    }

    @Test
    fun toCNY() {
        val amount = "9999999990"

        println("$amount CNY is ${amount.toCNY()}")
    }

    /**
     * emoji
     * 🫀 ❤️ 🆕 6⃣️
     * 🫀🥰🤦🤦🥰🤦🤦🤦🤦🤦🥸🥸🤓🤓🤓🥸🥸
     */
    @Test
    fun isEmoji() {
        println("️谷".isEmoji())
    }

    /**
     * number to string
     */
    @Test
    fun numberToString() {
        val number: Float? = 0f

        println("Result: " + number.string())
    }

    /**
     * removeTrailingZeros
     */
    @Test
    fun removeTrailingZeros() {
        val number = 0

        println("Result: " + number.stripTrailingZeros())
    }

    @Test
    fun testPlusDouble() {
        val numberA = 35.8
        val numberB = 0.55

        println("Result: " + numberA.plusDouble(numberB))
    }
}