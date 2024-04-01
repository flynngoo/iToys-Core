package com.itoys.android.uikit.components.viewpager.transformer

import android.view.View

import androidx.viewpager2.widget.ViewPager2

class FadeOutTransformation : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {

        page.translationX = -position * page.width

        page.alpha = 1 - Math.abs(position)
    }
}
