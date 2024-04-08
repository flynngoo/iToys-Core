package com.itoys.android.uikit.components.form

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.textview.MaterialTextView
import com.itoys.android.logcat.logcat
import com.itoys.android.uikit.R
import com.itoys.android.uikit.components.toast.toast
import com.itoys.android.uikit.databinding.UikitLayoutFormBinding
import com.itoys.android.uikit.model.RadioModel
import com.itoys.android.utils.expansion.color
import com.itoys.android.utils.expansion.doOnClick
import com.itoys.android.utils.expansion.dp2px
import com.itoys.android.utils.expansion.drawable
import com.itoys.android.utils.expansion.invalid
import com.itoys.android.utils.expansion.isBlank
import com.itoys.android.utils.expansion.isNotBlank
import com.itoys.android.utils.expansion.size
import com.itoys.android.utils.expansion.then

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/20
 */
class IToysFormView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    /** 16 dp */
    private val px16: Int by lazy { 16.dp2px() }

    /** 最大高度 */
    private var maxHeight = 0

    /** 必填标识 */
    private var requiredMark = false

    /** 必填标识-icon */
    private var requiredIcon: Drawable? = null

    /** 必填标识-icon宽 */
    private var requiredMarkIconWidth = -1

    /** 必填标识-icon高 */
    private var requiredMarkIconHeight = -1

    /** 必填标识-start margin */
    private var requiredMarkStartMargin = 0

    /** 必填标识-end margin */
    private var requiredMarkEndMargin = 0

    /** 必填标识-label view */
    private var labelView: MaterialTextView? = null

    /** label 文本 */
    private var labelText = ""

    /** label 宽 */
    private var labelWidth = -1

    /** label 文本字体大小 */
    private var labelSize = -1

    /** label 文本字体颜色 */
    private var labelColor = 0

    /** label 是否在表单标签字段右侧显示冒号 */
    private var labelColon = false

    /** label 文本字体对齐方式 */
    private var labelAlign = FormTextAlign.START

    /** label-start margin */
    private var labelStartMargin = 0

    /** label-end margin */
    private var labelEndMargin = 0

    /** 表单类型 */
    private var formModel = FormModel.TEXT

    /** content view */
    private var contentView: FrameLayout? = null

    /** 表单是否可点击 */
    private var formEnable = true

    /** 占位符 */
    private var placeholder = ""

    /** 占位符 文本字体颜色 */
    private var placeholderColor = 0

    /** 内容 文本字体大小 */
    private var contentSize = -1

    /** 内容 文本字体颜色 */
    private var contentColor = 0

    /** 内容 文本字体对齐方式 */
    private var contentAlign = FormTextAlign.START

    /** 内容 文本字数限制, 默认不限制 */
    private var contentMaxLength = -1

    /** 内容-start margin */
    private var contentStartMargin = 0

    /** 内容-end margin */
    private var contentEndMargin = 0

    /** 内容-suffix icon */
    private var suffixIcon: Drawable? = null

    /** 内容-suffix size */
    private var suffixIconSize: Int = px16

    /** 内容-suffix text */
    private var suffixText = ""

    /** 内容-suffix text size */
    private var suffixTextSize = -1

    /** 内容-suffix text color */
    private var suffixTextColor = -1

    /** 内容-suffix end margin */
    private var suffixEndMargin: Int = px16

    /** 分割线 */
    private var separatorView: View? = null

    /** 分割线 */
    private var separator = false

    /** 分割线颜色 */
    private var separatorColor = -1

    /** 分割线 高度 */
    private var separatorHeight = 0

    /** 分割线 缩进 */
    private var separatorIndent = 0

    /** 分割线 结尾缩进 */
    private var separatorEndIndent = 0

    /** 核查 */
    private var autoCheck = false

    /** 错误提示 */
    private var errorMessage = ""

    /** 表单内容 */
    private var formContent = ""

    /** 表单内容准确 */
    private var contentAccurate = false

    /**
     * 结果回调
     */
    private var resultCallback: IFormResultCallback? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        initView(attrs)
    }

    /**
     * 初始化form view.
     */
    private fun initView(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.IToysFormView)

        // 最大高度
        maxHeight = ta.getDimensionPixelOffset(R.styleable.IToysFormView_formMaxHeight, 54.dp2px())
        // 表单
        val formView = addFormView()

        // 核查
        autoCheck = ta.getBoolean(R.styleable.IToysFormView_formAutoCheck, autoCheck)
        // 错误消息
        errorMessage = ta.getString(R.styleable.IToysFormView_formErrorMessage).invalid()

        // 必填标识
        requiredMark = ta.getBoolean(R.styleable.IToysFormView_formRequiredMark, requiredMark)
        requiredIcon = ta.getDrawable(R.styleable.IToysFormView_formRequiredMarkIcon)
        requiredMarkIconWidth = ta.getDimensionPixelOffset(
            R.styleable.IToysFormView_formRequiredMarkIconWidth, requiredMarkIconWidth
        )
        requiredMarkIconHeight = ta.getDimensionPixelOffset(
            R.styleable.IToysFormView_formRequiredMarkIconHeight, requiredMarkIconHeight
        )
        requiredMarkStartMargin = ta.getDimensionPixelOffset(
            R.styleable.IToysFormView_formRequiredMarkStartMargin, requiredMarkStartMargin
        )
        requiredMarkEndMargin = ta.getDimensionPixelOffset(
            R.styleable.IToysFormView_formRequiredMarkEndMargin, requiredMarkEndMargin
        )
        setRequiredMark(formView.requiredMark)

        // label
        labelText = ta.getString(R.styleable.IToysFormView_formLabel).invalid()
        labelWidth = ta.getDimensionPixelOffset(R.styleable.IToysFormView_formLabelWidth, labelWidth)
        labelSize = ta.getDimensionPixelOffset(R.styleable.IToysFormView_formLabelSize, labelSize)
        labelColor = ta.getColor(
            R.styleable.IToysFormView_formLabelColor,
            context.color(R.color.uikit_colorful_1D2129)
        )
        labelColon = ta.getBoolean(R.styleable.IToysFormView_formLabelColon, labelColon)
        labelAlign = ta.getInt(R.styleable.IToysFormView_formLabelAlign, labelAlign)
        labelStartMargin = ta.getDimensionPixelOffset(
            R.styleable.IToysFormView_formLabelStartMargin, labelStartMargin
        )
        labelEndMargin = ta.getDimensionPixelOffset(
            R.styleable.IToysFormView_formLabelEndMargin, labelEndMargin
        )
        setLabelView(formView.label)

        // content
        formModel = ta.getInt(R.styleable.IToysFormView_formModel, formModel)
        contentMaxLength = ta.getInt(R.styleable.IToysFormView_formContentMaxLength, contentMaxLength)
        placeholder = ta.getString(R.styleable.IToysFormView_formPlaceholder).invalid()
        placeholderColor = ta.getColor(
            R.styleable.IToysFormView_formPlaceholderColor, context.color(R.color.uikit_colorful_C9CDD4)
        )
        contentSize =
            ta.getDimensionPixelOffset(R.styleable.IToysFormView_formContentSize, contentSize)
        contentColor = ta.getColor(
            R.styleable.IToysFormView_formContentColor, context.color(R.color.uikit_colorful_1D2129)
        )
        contentAlign = ta.getInt(R.styleable.IToysFormView_formContentAlign, contentAlign)
        contentStartMargin = ta.getDimensionPixelOffset(
            R.styleable.IToysFormView_formContentStartMargin, contentStartMargin
        )
        contentEndMargin = ta.getDimensionPixelOffset(
            R.styleable.IToysFormView_formContentEndMargin, contentEndMargin
        )
        formEnable = ta.getBoolean(R.styleable.IToysFormView_formEnable, formEnable)
        contentView = formView.content
        setContentView(formView.content)

        // suffix
        suffixIcon = ta.getDrawable(R.styleable.IToysFormView_formSuffixIcon)
        suffixIconSize = ta.getDimensionPixelOffset(R.styleable.IToysFormView_formSuffixIconSize, px16)
        suffixText = ta.getString(R.styleable.IToysFormView_formSuffixText).invalid()
        suffixTextSize = ta.getDimensionPixelOffset(
            R.styleable.IToysFormView_formSuffixTextSize, suffixTextSize
        )
        suffixTextColor = ta.getColor(
            R.styleable.IToysFormView_formSuffixTextColor, suffixTextColor
        )
        suffixEndMargin = ta.getDimensionPixelOffset(
            R.styleable.IToysFormView_formSuffixEndMargin, px16
        )
        setSuffix(formView.suffixIcon, formView.suffixText)

        // separator
        separator = ta.getBoolean(R.styleable.IToysFormView_formBottomSeparator, separator)
        separatorColor = ta.getColor(
            R.styleable.IToysFormView_formBottomSeparatorColor,
            context.color(R.color.uikit_colorful_E5E6EB)
        )
        separatorHeight = ta.getDimensionPixelOffset(
            R.styleable.IToysFormView_formBottomSeparatorHeight,
            separatorHeight
        )
        separatorIndent = ta.getDimensionPixelOffset(
            R.styleable.IToysFormView_formBottomSeparatorIndent,
            separatorIndent
        )
        separatorEndIndent = ta.getDimensionPixelOffset(
            R.styleable.IToysFormView_formBottomSeparatorEndIndent,
            separatorEndIndent
        )
        separatorView = formView.bottomSeparator
        setSeparator(formView.bottomSeparator)

        ta.recycle()
    }

    /**
     * 添加表单view
     */
    private fun addFormView(): UikitLayoutFormBinding {
        val binding = UikitLayoutFormBinding.inflate(
            LayoutInflater.from(context), this, false
        )
        val formLayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, maxHeight)
        this.addView(binding.root, formLayoutParams)
        return binding
    }

    /**
     * 设置必填标识
     */
    private fun setRequiredMark(requiredMark: AppCompatImageView) {
        requiredMark.visibility = this.requiredMark.then(VISIBLE, GONE)
        if (this.requiredMark) {
            requiredMark.setImageDrawable(
                requiredIcon ?: context.drawable(R.drawable.uikit_icon_required_mark)
            )
            val requiredMarkLayoutParams = requiredMark.layoutParams as MarginLayoutParams
            requiredMarkLayoutParams.setMargins(
                requiredMarkStartMargin, 0, requiredMarkEndMargin, 0
            )
            if (requiredMarkIconWidth > 0) {
                requiredMarkLayoutParams.width = requiredMarkIconWidth
            }

            if (requiredMarkIconHeight > 0) {
                requiredMarkLayoutParams.height = requiredMarkIconHeight
            }
            requiredMark.layoutParams = requiredMarkLayoutParams
        }
    }

    /**
     * 设置label
     */
    private fun setLabelView(label: MaterialTextView) {
        this.labelView = label
        label.text = labelColon.then("${labelText}: ", labelText)
        if (labelSize > 0)
            label.setTextSize(TypedValue.COMPLEX_UNIT_PX, labelSize.toFloat())
        label.setTextColor(labelColor)
        label.gravity = when (labelAlign) {
            FormTextAlign.START -> Gravity.START or Gravity.CENTER_VERTICAL
            FormTextAlign.CENTER -> Gravity.CENTER
            FormTextAlign.END -> Gravity.END or Gravity.CENTER_VERTICAL
            else -> Gravity.START
        }

        val labelLayoutParams = label.layoutParams as MarginLayoutParams
        labelLayoutParams.setMargins(
            labelStartMargin, 0, labelEndMargin, 0
        )
        labelLayoutParams.width =
            (labelWidth > 0).then(labelWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
        label.layoutParams = labelLayoutParams

        label.visibility = labelText.isBlank().then(View.GONE, View.VISIBLE)
    }

    /**
     * 设置content
     */
    private fun setContentView(content: FrameLayout) {
        val config = FormContentConfig(
            placeholder,
            placeholderColor,
            contentSize,
            contentColor,
            contentAlign,
            contentMaxLength,
            formEnable,
        )

        val contentLayoutParams = content.layoutParams as MarginLayoutParams
        contentLayoutParams.setMargins(
            contentStartMargin, 0, contentEndMargin, 0,
        )
        contentLayoutParams.height = maxHeight
        content.layoutParams = contentLayoutParams
        val contentView = FormModelFactory.create(
            context,
            formModel,
            config,
        )
        contentView?.let { content.addView(contentView) }

        FormModelFactory.setCallback(contentView, formModel, object : IFormResultCallback() {
            override fun isAccurate(accurate: Boolean) {
                contentAccurate = accurate
            }

            override fun result(result: String) {
                formContent = result
            }
        })
    }

    /**
     * 设置 form suffix
     */
    private fun setSuffix(suffixIcon: AppCompatImageView, suffixText: MaterialTextView) {
        suffixIcon.visibility = (this.suffixIcon == null).then(View.GONE, View.VISIBLE)
        if (suffixIcon.visibility == View.VISIBLE) {
            suffixIcon.setImageDrawable(this.suffixIcon)
            val suffixIconLayoutParams = suffixIcon.layoutParams as MarginLayoutParams
            suffixIconLayoutParams.width = suffixIconSize
            suffixIconLayoutParams.height = suffixIconSize
            suffixIconLayoutParams.marginEnd = suffixEndMargin
            suffixIcon.layoutParams = suffixIconLayoutParams
        }

        suffixIcon.doOnClick { resultCallback?.suffixIconClick() }

        suffixText.visibility = (this.suffixText.isBlank()).then(View.GONE, View.VISIBLE)
        if (suffixText.visibility == View.VISIBLE) {
            suffixText.text = this.suffixText
            if (suffixTextSize > -1) {
                suffixText.setTextSize(TypedValue.COMPLEX_UNIT_PX, suffixTextSize.toFloat())
            }

            if (suffixTextColor > -1) {
                suffixText.setTextColor(suffixTextColor)
            }
            val suffixTextLayoutParams = suffixText.layoutParams as MarginLayoutParams
            suffixTextLayoutParams.marginEnd = suffixEndMargin
            suffixText.layoutParams = suffixTextLayoutParams
        }
    }

    /**
     * 设置分割线
     */
    private fun setSeparator(separator: View) {
        separator.visibility = this.separator.then(VISIBLE, GONE)
        separator.setBackgroundColor(separatorColor)
        val separatorLayoutParams = separator.layoutParams as MarginLayoutParams
        separatorLayoutParams.setMargins(separatorIndent, 0, separatorEndIndent, 0)
        if (separatorHeight > 0) {
            separatorLayoutParams.height = separatorHeight
        }
        separator.layoutParams = separatorLayoutParams
    }

    /**
     * 隐藏分割线
     */
    fun hideSeparator(isHide: Boolean) {
        separatorView?.visibility = isHide.then(View.GONE, View.VISIBLE)
    }

    /**
     * label
     */
    fun label() = labelText

    /**
     * 设置label文本
     */
    fun setLabel(label: String?) {
        labelText = label.invalid()
        labelView?.text = labelText
        labelView?.visibility = labelText.isBlank().then(View.GONE, View.VISIBLE)
    }

    /**
     * 设置表单内容
     */
    fun setContent(content: String) {
        formContent = content

        val config = FormContentConfig(
            placeholder,
            placeholderColor,
            contentSize,
            contentColor,
            contentAlign,
            contentMaxLength,
            formEnable,
        )

        FormModelFactory.updateContent(contentView, content, formModel, config)
    }

    /**
     * 设置表单内容
     */
    fun setContent(radioList: List<RadioModel>?) {
        FormModelFactory.updateRadioModel(contentView, radioList)
    }

    /**
     * 获取表单内容
     */
    fun formContent(): String? {
        logcat { "$labelText form content is $formContent!" }

        if (autoCheck && !contentAccurate) {
            toast(errorMessage)
            return null
        }

        if (formContent.size() > 0 && !contentAccurate && errorMessage.isNotBlank()) {
            toast(errorMessage)
            return null
        }

        return formContent
    }

    /**
     * 设置result 回调
     */
    fun setResultCallback(resultCallback: IFormResultCallback) {
        this.resultCallback = resultCallback
        FormModelFactory.setCallback(contentView, formModel, resultCallback)
    }
}