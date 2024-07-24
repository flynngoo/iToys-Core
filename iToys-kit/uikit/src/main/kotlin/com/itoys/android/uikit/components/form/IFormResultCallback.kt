package com.itoys.android.uikit.components.form

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/29
 */
abstract class IFormResultCallback {

    /**
     * 内容结果是否准确
     */
    open fun isAccurate(accurate: Boolean) {}

    /**
     * 内容结果
     */
    open fun result(result: String) {}

    /**
     * 内容结果
     */
    open fun result(result: Boolean) {}

    /**
     * 内容id
     */
    open fun resultId(resultId: Int) {}

    /**
     * 点击
     */
    open fun click() {}

    /**
     * suffix icon 点击
     */
    open fun suffixIconClick() {}
}