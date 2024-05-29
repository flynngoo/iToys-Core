package com.itoys.android.uikit.components.descriptions

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.google.android.material.textview.MaterialTextView
import com.itoys.android.uikit.R
import com.itoys.android.uikit.databinding.UikitLayoutDescriptionsBinding
import com.itoys.android.utils.expansion.invalid
import com.itoys.android.utils.expansion.isNotBlank
import com.itoys.android.utils.expansion.then

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/4/5
 */
class DescriptionsView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    /** label view */
    private var labelView: MaterialTextView? = null

    /** label */
    private var label = ""

    /** label 字体 */
    private var labelFontFamily = ""

    /** label 冒号 */
    private var labelColon = true

    /** label 字体大小 */
    private var labelSize = -1

    /** label 字体颜色 */
    private var labelColor = -1

    /** label weight */
    private var labelWeight = 2f

    /** content view */
    private var contentView: MaterialTextView? = null

    /** content */
    private var content = ""

    /** content 字体 */
    private var contentFontFamily = ""

    /** content 字体大小 */
    private var contentSize = -1

    /** content 字体颜色 */
    private var contentColor = -1

    /** content weight */
    private var contentWeight = 8f

    init {
        initView(attrs)
    }

    /**
     * 初始化
     */
    private fun initView(attrs: AttributeSet?) {
        val binding = UikitLayoutDescriptionsBinding.inflate(
            LayoutInflater.from(context), this, false
        )
        this.addView(binding.root)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.DescriptionsView)
        label = ta.getString(R.styleable.DescriptionsView_descLabel).invalid()
        labelFontFamily = ta.getString(R.styleable.DescriptionsView_descLabelFontFamily).invalid()
        labelColon = ta.getBoolean(R.styleable.DescriptionsView_descLabelColon, labelColon)
        labelSize = ta.getDimensionPixelOffset(R.styleable.DescriptionsView_descLabelSize, labelSize)
        labelColor = ta.getColor(R.styleable.DescriptionsView_descLabelColor, labelColor)
        labelWeight = ta.getFloat(R.styleable.DescriptionsView_descLabelWeight, labelWeight)

        labelView = binding.label
        binding.label.apply {
            // 设置文本
            text = labelColon.then("$label:", label)
            // 设置字体
            if (labelFontFamily.isNotBlank()) {
                typeface = Typeface.create(labelFontFamily, Typeface.NORMAL)
            }
            // 设置字体大小
            setTextSize(this, labelSize.toFloat())
            // 设置字体颜色
            setTextColor(this, labelColor)
        }

        content = ta.getString(R.styleable.DescriptionsView_descContent).invalid()
        contentFontFamily = ta.getString(R.styleable.DescriptionsView_descContentFontFamily).invalid()
        contentSize = ta.getDimensionPixelOffset(R.styleable.DescriptionsView_descContentSize, contentSize)
        contentColor = ta.getColor(R.styleable.DescriptionsView_descContentColor, contentColor)
        contentWeight = ta.getFloat(R.styleable.DescriptionsView_descContentWeight, contentWeight)
        ta.recycle()

        contentView = binding.content
        binding.content.apply {
            // 设置文本
            text = content
            // 设置字体
            if (contentFontFamily.isNotBlank()) {
                typeface = Typeface.create(contentFontFamily, Typeface.NORMAL)
            }
            // 设置字体大小
            setTextSize(this, contentSize.toFloat())
            // 设置字体颜色
            setTextColor(this, contentColor)
        }

        // 设置权重
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.root)
        constraintSet.setHorizontalWeight(binding.label.id, labelWeight)
        constraintSet.setHorizontalWeight(binding.content.id, contentWeight)
        constraintSet.applyTo(binding.root)
    }

    /**
     * 设置text size
     */
    private fun setTextSize(text: MaterialTextView, size: Float) {
        if (size != -1f) text.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    /**
     * 设置text color
     */
    private fun setTextColor(text: MaterialTextView, color: Int) {
        if (color != -1) text.setTextColor(color)
    }

    /**
     * 设置label
     */
    fun setLabel(label: String, withColon: Boolean = true) {
        this.label = label
        labelView?.text = withColon.then("$label:", label)
    }

    /**
     * 设置content
     */
    fun setContent(content: String) {
        this.content = content
        contentView?.text = this.content
    }

    /**
     * 获取content
     */
    fun content() = contentView?.text.toString()
}