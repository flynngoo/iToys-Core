package com.itoys.android.uikit.components.indicator

import android.content.Context
import android.graphics.Typeface
import com.itoys.android.utils.expansion.isNotBlank
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/12/24
 */
class IToysPagerTitleView(context: Context) : SimplePagerTitleView(context) {

    var normalTextTypeface = ""
    var selectedTextTypeface = ""

    override fun onSelected(index: Int, totalCount: Int) {
        super.onSelected(index, totalCount)
        if (selectedTextTypeface.isNotBlank()) {
            typeface = Typeface.create(selectedTextTypeface, Typeface.NORMAL)
        }
    }

    override fun onDeselected(index: Int, totalCount: Int) {
        super.onDeselected(index, totalCount)
        if (normalTextTypeface.isNotBlank()) {
            typeface = Typeface.create(normalTextTypeface, Typeface.NORMAL)
        }
    }
}