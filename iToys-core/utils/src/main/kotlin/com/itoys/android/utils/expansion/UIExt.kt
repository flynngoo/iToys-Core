package com.itoys.android.utils.expansion

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.graphics.drawable.NinePatchDrawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.itoys.android.utils.compat.ScreenSizeCompat

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/21
 */

/**
 * 颜色
 */
fun Context.color(@ColorRes colorResId: Int): Int {
    return ContextCompat.getColor(this.applicationContext, colorResId)
}

fun Context.drawable(@DrawableRes drawableResId: Int): Drawable? {
    return AppCompatResources.getDrawable(this, drawableResId)
}

fun Context.tint9PatchDrawable(@DrawableRes drawableResId: Int): NinePatchDrawable? {
    return AppCompatResources.getDrawable(this, drawableResId) as NinePatchDrawable?
}

fun Context.screenWidth() = ScreenSizeCompat.screenSize(this).width

fun Context.screenHeight() = ScreenSizeCompat.screenSize(this).height

fun Drawable.tintIcon(@ColorInt tintColor: Int): Drawable {
    this.colorFilter = PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
    return this
}