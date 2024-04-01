package com.itoys.android.uikit.components.viewpager

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

/**
 * @author Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @since 10/12/2020 14:55
 * @desc 禁用滑动的ViewPager（一般用于 APP 主页的 ViewPager + Fragment）
 */
class NoScrollViewPager : ViewPager {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    override fun setCurrentItem(item: Int) {
        val smoothScroll = when (currentItem) {
            0 -> item == currentItem + 1
            pageCount() - 1 -> item == currentItem - 1
            else -> abs(currentItem - item) == 1
        }
        super.setCurrentItem(item, smoothScroll)
    }

    fun pageCount(): Int {
        return if (null != adapter) adapter!!.count else 0
    }
}
