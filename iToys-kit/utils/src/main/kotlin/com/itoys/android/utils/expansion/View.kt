package com.itoys.android.utils.expansion

import android.view.View

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/21
 */
/** 连续点击时长 */
private var continuousClickTime = 600

/** 上次点击事件 */
private var lastClickTime = 0L

/** 上次点击标志 */
private var lastClickTag = 0

/**
 * view 点击.
 *
 * 防止连续点击.
 */
fun View.doOnClick(block: () -> Unit) {
    this.setOnClickListener {
        val currentTag = this.id
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastClickTime < continuousClickTime && lastClickTag == currentTag) {
            return@setOnClickListener
        }

        lastClickTime = currentTimeMillis
        lastClickTag = currentTag
        block()
    }
}

/**
 * view 是否可见.
 */
fun View.isVisible() = this.visibility == View.VISIBLE

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}