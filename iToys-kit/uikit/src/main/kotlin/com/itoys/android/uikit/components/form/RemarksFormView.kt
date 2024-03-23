package com.itoys.android.uikit.components.form

import android.content.Context
import android.graphics.Typeface
import android.text.InputFilter
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textview.MaterialTextView
import com.itoys.android.uikit.R
import com.itoys.android.uikit.databinding.UikitLayoutRemarksBinding
import com.itoys.android.utils.expansion.invalid
import com.itoys.android.utils.expansion.isBlank
import com.itoys.android.utils.expansion.isNotBlank
import com.itoys.android.utils.expansion.size
import com.itoys.android.utils.expansion.then

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/12/19
 */
class RemarksFormView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    /** 标题 */
    private var title = ""

    /** 标题字体大小 */
    private var titleSize = -1

    /** 标题 typeface */
    private var titleTypeface = ""

    /** 标题背景 */
    private var titleBackground = 0

    /** 标题 */
    private var titleView: MaterialTextView? = null

    /** 最大长度 */
    private var maximum = 200

    /** 备注 */
    private var remarksView: AppCompatEditText? = null

    private var remarksIndicator = true

    /** count 指示器 */
    private var remarksIndicatorView: MaterialTextView? = null

    /** 禁用 */
    private var remarksDisable = false

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        initView(attrs)
    }

    /**
     * 初始备注
     */
    private fun initView(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.RemarksFormView)
        title = ta.getString(R.styleable.RemarksFormView_remarksTitle).invalid()
        titleSize = ta.getDimensionPixelSize(R.styleable.RemarksFormView_remarksTitleSize, titleSize)
        titleTypeface = ta.getString(R.styleable.RemarksFormView_remarksTitleTypeface).invalid()
        titleBackground = ta.getColor(R.styleable.RemarksFormView_remarksTitleBackground, titleBackground)
        maximum = ta.getInt(R.styleable.RemarksFormView_remarksMaxLength, maximum)
        remarksIndicator = ta.getBoolean(R.styleable.RemarksFormView_remarksIndicator, remarksIndicator)
        remarksDisable = ta.getBoolean(R.styleable.RemarksFormView_remarksDisable, remarksDisable)
        ta.recycle()

        val binding = UikitLayoutRemarksBinding.inflate(
            LayoutInflater.from(context), this, false
        )
        this.addView(binding.root)

        titleView = binding.title
        setTitle(title)
        // 设置标题size
        if (titleSize != -1) {
            titleView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize.toFloat())
        }

        // 设置标题颜色
        if (titleBackground != 0) {
            titleView?.setBackgroundColor(titleBackground)
        }
        // 设置标题typeface
        if (titleTypeface.isNotBlank()) {
            titleView?.typeface = Typeface.create(titleTypeface, Typeface.NORMAL)
        }

        remarksView = binding.edit
        remarksView?.isEnabled = !remarksDisable
        remarksIndicatorView = binding.indicator
        remarksIndicatorView?.visibility = remarksIndicator.then(View.VISIBLE, View.GONE)
        resetCountText()
        var filters = arrayOf<InputFilter>()
        filters = filters.plus(InputFilter.LengthFilter(maximum))
        remarksView?.filters = filters
        remarksView?.addTextChangedListener {
            resetCountText()
        }
    }

    /**
     * 设置标题
     */
    fun setTitle(title: String) {
        if (title.isBlank()) return
        titleView?.text = title
    }

    /**
     * 设置备注
     */
    fun setRemarks(remarks: String) {
        remarksView?.setText(remarks)
    }

    /**
     * 设置备注最大长度
     */
    fun setRemarksMaxSize(maximum: Int) {
        this.maximum = maximum
        var filters = arrayOf<InputFilter>()
        filters = filters.plus(InputFilter.LengthFilter(maximum))
        remarksView?.filters = filters
        resetCountText()
    }

    /**
     * 设置备注长度
     */
    private fun resetCountText() {
        remarksIndicatorView?.text = context.getString(
            R.string.uikit_remarks_count_indicator,
            remarksView?.text.size(),
            maximum
        )
    }

    /**
     * 备注
     */
    fun remarks() = remarksView?.text.toString()
}