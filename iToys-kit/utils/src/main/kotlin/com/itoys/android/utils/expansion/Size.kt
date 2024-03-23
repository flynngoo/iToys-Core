package com.itoys.android.utils.expansion

import android.content.res.Resources

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/21
 */
/**
 * dp转px
 */
fun Int?.dp2px(): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return ((this ?: 0) * scale + 0.5f).toInt()
}

/**
 * px转dp
 */
fun Int?.px2dp(): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return ((this ?: 0) * scale + 0.5f).toInt()
}

/**
 * sp转px
 */
fun Int?.sp2px(): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return ((this ?: 0) * scale + 0.5f).toInt()
}

/**
 * px转sp
 */
fun Int?.px2sp(): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return ((this ?: 0) * scale + 0.5f).toInt()
}

/**
 * dp转px
 */
fun Float?.dp2px(): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return ((this ?: 0f) * scale + 0.5f).toInt()
}

/**
 * px转dp
 */
fun Float?.px2dp(): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return ((this ?: 0f) * scale + 0.5f).toInt()
}

/**
 * sp转px
 */
fun Float?.sp2px(): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return ((this ?: 0f) * scale + 0.5f).toInt()
}

/**
 * px转sp
 */
fun Float?.px2sp(): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return ((this ?: 0f) * scale + 0.5f).toInt()
}