package com.itoys.android.uikit.components.form

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.text.InputFilter
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.itoys.android.uikit.R
import com.itoys.android.uikit.databinding.UikitLayoutTextareaBinding
import com.itoys.android.utils.expansion.invalid
import com.itoys.android.utils.expansion.isNotBlank
import com.itoys.android.utils.expansion.size
import com.itoys.android.utils.expansion.sp2px
import com.itoys.android.utils.expansion.then
import com.itoys.android.utils.filter.EmojiFilter

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/4/11
 */
class TextareaFormView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    /** 文本框 */
    private var textareaEdit: AppCompatEditText? = null

    /** 最大长度 */
    private var maximum = 200

    /** 文本框内容 */
    private var textarea = ""

    init {
        initView(attrs)
    }

    /**
     * 初始化视图
     */
    private fun initView(attrs: AttributeSet?) {
        val binding = UikitLayoutTextareaBinding.inflate(
            LayoutInflater.from(context), this, false
        )
        this.addView(binding.root)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.TextareaFormView)
        // label
        setLabel(binding, ta)
        // 文本框
        setTextarea(binding, ta)
        // 设置指示器
        setIndicator(binding, ta)
        ta.recycle()
    }

    /**
     * 设置标题
     */
    private fun setLabel(root: UikitLayoutTextareaBinding, ta: TypedArray) {
        val label = ta.getString(R.styleable.TextareaFormView_textareaLabel).invalid()
        val labelSize = ta.getDimensionPixelSize(R.styleable.TextareaFormView_textareaLabelSize, 14.sp2px())
        val labelTypeface = ta.getString(R.styleable.TextareaFormView_textareaLabelTypeface)
        root.label.apply {
            text = label
            setTextSize(TypedValue.COMPLEX_UNIT_PX, labelSize.toFloat())
            if (labelTypeface.isNotBlank()) {
                typeface = Typeface.create(labelTypeface, Typeface.NORMAL)
            }
        }
    }

    /**
     * 设置文本框
     */
    private fun setTextarea(root: UikitLayoutTextareaBinding, ta: TypedArray) {
        maximum = ta.getInt(R.styleable.TextareaFormView_textareaMaximum, maximum)
        root.indicator.text = context.getString(R.string.uikit_remarks_count_indicator, 0, maximum)

        val disable = ta.getBoolean(R.styleable.TextareaFormView_textareaDisable, false)
        val placeholder = ta.getString(R.styleable.TextareaFormView_textareaPlaceholder)
        val enableEmoji = ta.getBoolean(R.styleable.TextareaFormView_textareaEnableEmoji, true)

        textareaEdit = root.edit
        root.edit.isEnabled = !disable
        root.edit.setHint(placeholder)

        // 设置最大长度
        var filters = arrayOf<InputFilter>()
        filters = filters.plus(InputFilter.LengthFilter(maximum))
        if (!enableEmoji) filters = filters.plus(EmojiFilter())
        root.edit.filters = filters

        // 监听文本框变化
        root.edit.addTextChangedListener {
            textarea = it.toString()

            root.indicator.text = context.getString(
                R.string.uikit_remarks_count_indicator,
                textarea.size(),
                maximum
            )
        }
    }

    /**
     * 设置指示器
     */
    private fun setIndicator(root: UikitLayoutTextareaBinding, ta: TypedArray) {
        val indicator = ta.getBoolean(R.styleable.TextareaFormView_textareaIndicator, true)
        root.indicator.visibility = indicator.then(View.VISIBLE, View.GONE)
    }

    /**
     * 获取文本框内容
     */
    fun textarea() = textarea

    /**
     * 设置文本框内容
     */
    fun setTextarea(text: String) {
        textareaEdit?.setText(text)
    }
}