package com.itoys.android.uikit.components.selector

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton
import com.itoys.android.uikit.R
import com.itoys.android.utils.expansion.then

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2024/1/1
 */
class RadioButtonSelector(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatRadioButton(context, attrs, defStyleAttr) {

    /** 选择器 icon */
    private var selectorIcon = 0

    /** 选择器 选中 icon */
    private var selectorCheckedIcon = 0

    /** 选择器 text color */
    private var selectorTextColor = 0

    /** 选择器 选中 text color */
    private var selectorCheckedTextColor = 0

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        initSelector(attrs)
    }

    /**
     * 初始化选择器
     */
    private fun initSelector(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.RadioButtonSelector)
        selectorIcon = ta.getResourceId(R.styleable.RadioButtonSelector_selectorIconUnChecked, selectorIcon)
        selectorCheckedIcon = ta.getResourceId(R.styleable.RadioButtonSelector_selectorIconChecked, selectorIcon)
        selectorTextColor = ta.getColor(R.styleable.RadioButtonSelector_selectorTextColorUnChecked, selectorTextColor)
        selectorCheckedTextColor = ta.getColor(R.styleable.RadioButtonSelector_selectorTextColorChecked, selectorTextColor)
        ta.recycle()

        changeSelectorStyle(isChecked)

        setOnCheckedChangeListener { _, isChecked -> changeSelectorStyle(isChecked) }
    }

    /**
     * change 选择器样式
     */
    private fun changeSelectorStyle(isChecked: Boolean) {
        if (selectorIcon != 0 || selectorCheckedIcon != 0) {
            setButtonDrawable(isChecked.then(selectorCheckedIcon, selectorIcon))
        }

        if (selectorTextColor != 0 || selectorCheckedTextColor != 0) {
            setTextColor(isChecked.then(selectorCheckedTextColor, selectorTextColor))
        }
    }
}