package com.itoys.android.uikit.components.captcha

import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.itoys.android.uikit.R
import com.itoys.android.utils.InputUtils
import com.itoys.android.utils.expansion.color
import com.itoys.android.utils.expansion.dp2px
import com.itoys.android.utils.expansion.sp2px
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * 验证码输入框
 */
class VerificationInputView : RelativeLayout {

    private var mContext: Context? = null
    private var onInputListener: OnInputListener? = null

    private var mLinearLayout: LinearLayout? = null
    private var mRelativeLayouts: Array<RelativeLayout?>? = null
    private var mTextViews: Array<TextView?>? = null
    private var mUnderLineViews: Array<View?>? = null
    private var mCursorViews: Array<View?>? = null
    private var mEditText: EditText? = null
    private var mPopupWindow: PopupWindow? = null
    private var valueAnimator: ValueAnimator? = null

    private val mCodes: MutableList<String> = mutableListOf()

    /**
     * 输入框数量
     */
    private var mEtNumber = 0

    /**
     * 输入框类型
     */
    private var mEtInputType: VCInputType? = null

    /**
     * 输入框的宽度
     */
    private var mEtWidth = 0

    /**
     * 输入框的高度
     */
    private var mEtHeight = 0

    /**
     * 文字颜色
     */
    private var mEtTextColor = 0

    /**
     * 文字大小
     */
    private var mEtTextSize = 0f

    /**
     * 输入框间距
     */
    private var mEtSpacing = 0

    /**
     * 平分后的间距
     */
    private var mEtBisectSpacing = 0

    /**
     * 判断是否平分,默认平分
     */
    private var isBisect = false

    /**
     * 输入框宽度
     */
    private var mViewWidth = 0

    /**
     * 下划线默认颜色,焦点颜色,高度,是否展示
     */
    private var mEtUnderLineDefaultColor = 0
    private var mEtUnderLineFocusColor = 0
    private var mEtUnderLineHeight = 0
    private var mEtUnderLineShow = false

    /**
     * 光标宽高,颜色
     */
    private var mEtCursorWidth = 0
    private var mEtCursorHeight = 0
    private var mEtCursorColor = 0

    /**
     * 输入框的背景色、焦点背景色、是否有焦点背景色
     */
    private var mEtBackground = 0
    private var mEtFocusBackground = 0
    private var isFocusBackground = false

    enum class VCInputType {
        /**
         * 数字类型
         */
        NUMBER,

        /**
         * 数字密码
         */
        NUMBER_PASSWORD,

        /**
         * 文字
         */
        TEXT,

        /**
         * 文字密码
         */
        TEXT_PASSWORD
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        mContext = context
        val typedArray: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.VerificationInputView)
        mEtNumber = typedArray.getInteger(R.styleable.VerificationInputView_boxNumber, 4)
        val inputType = typedArray.getInt(
            R.styleable.VerificationInputView_boxInputType,
            VCInputType.NUMBER.ordinal
        )
        mEtInputType = VCInputType.values()[inputType]
        mEtWidth = typedArray.getDimensionPixelSize(
            R.styleable.VerificationInputView_boxWidth,
            40.dp2px()
        )
        mEtHeight = typedArray.getDimensionPixelSize(
            R.styleable.VerificationInputView_boxHeight,
            40.dp2px()
        )
        mEtTextColor = typedArray.getColor(
            R.styleable.VerificationInputView_boxTextColor,
            Color.BLACK
        )
        mEtTextSize = typedArray.getDimensionPixelSize(
            R.styleable.VerificationInputView_boxTextSize,
            14.sp2px()
        ).toFloat()
        mEtBackground =
            typedArray.getResourceId(R.styleable.VerificationInputView_boxBackground, -1)
        if (mEtBackground < 0) {
            mEtBackground = typedArray.getColor(
                R.styleable.VerificationInputView_boxBackground,
                Color.WHITE
            )
        }
        isFocusBackground =
            typedArray.hasValue(R.styleable.VerificationInputView_boxFocusBackground)
        mEtFocusBackground = typedArray.getResourceId(
            R.styleable.VerificationInputView_boxFocusBackground,
            -1
        )
        if (mEtFocusBackground < 0) {
            mEtFocusBackground = typedArray.getColor(
                R.styleable.VerificationInputView_boxFocusBackground,
                Color.WHITE
            )
        }
        isBisect = typedArray.hasValue(R.styleable.VerificationInputView_boxSpacing)
        if (isBisect) {
            mEtSpacing = typedArray.getDimensionPixelSize(
                R.styleable.VerificationInputView_boxSpacing,
                0
            )
        }
        mEtCursorWidth = typedArray.getDimensionPixelOffset(
            R.styleable.VerificationInputView_boxCursorWidth,
            2.dp2px()
        )
        mEtCursorHeight = typedArray.getDimensionPixelOffset(
            R.styleable.VerificationInputView_boxCursorHeight,
            30.dp2px()
        )
        mEtCursorColor = typedArray.getColor(
            R.styleable.VerificationInputView_boxCursorColor,
            context.color(R.color.uikit_colorful_C3C3C3)
        )
        mEtUnderLineHeight = typedArray.getDimensionPixelOffset(
            R.styleable.VerificationInputView_boxUnderlineHeight,
            1.dp2px()
        )
        mEtUnderLineDefaultColor = typedArray.getColor(
            R.styleable.VerificationInputView_boxUnderlineDefaultColor,
            context.color(R.color.uikit_colorful_F0F0F0)
        )
        mEtUnderLineFocusColor = typedArray.getColor(
            R.styleable.VerificationInputView_boxUnderlineFocusColor,
            context.color(R.color.uikit_colorful_C3C3C3)
        )
        mEtUnderLineShow =
            typedArray.getBoolean(R.styleable.VerificationInputView_boxUnderlineShow, false)
        initView()
        typedArray.recycle()
    }

    private fun initView() {
        mRelativeLayouts = arrayOfNulls(mEtNumber)
        mTextViews = arrayOfNulls(mEtNumber)
        mUnderLineViews = arrayOfNulls(mEtNumber)
        mCursorViews = arrayOfNulls(mEtNumber)
        mLinearLayout = LinearLayout(mContext)
        mLinearLayout?.orientation = LinearLayout.HORIZONTAL
        mLinearLayout?.gravity = Gravity.CENTER_HORIZONTAL
        mLinearLayout?.layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        for (i in 0 until mEtNumber) {
            val relativeLayout = RelativeLayout(mContext)
            relativeLayout.layoutParams = getEtLayoutParams(i)
            setEtBackground(relativeLayout, mEtBackground)
            mRelativeLayouts!![i] = relativeLayout
            val textView = TextView(mContext)
            initTextView(textView)
            relativeLayout.addView(textView)
            mTextViews!![i] = textView
            val cursorView = View(mContext)
            initCursorView(cursorView)
            relativeLayout.addView(cursorView)
            mCursorViews!![i] = cursorView
            if (mEtUnderLineShow) {
                val underLineView = View(mContext)
                initUnderLineView(underLineView)
                relativeLayout.addView(underLineView)
                mUnderLineViews!![i] = underLineView
            }
            mLinearLayout!!.addView(relativeLayout)
        }
        addView(mLinearLayout)
        mEditText = EditText(mContext)
        initEdittext(mEditText!!)
        addView(mEditText)
        setCursorColor()
    }

    private fun initTextView(textView: TextView) {
        val lp: LayoutParams =
            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        textView.layoutParams = lp
        textView.textAlignment = TEXT_ALIGNMENT_CENTER
        textView.gravity = Gravity.CENTER
        textView.setTextColor(mEtTextColor)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mEtTextSize)
        setInputType(textView)
        textView.setPadding(0, 0, 0, 0)
    }

    private fun initCursorView(view: View) {
        val layoutParams = LayoutParams(mEtCursorWidth, mEtCursorHeight)
        layoutParams.addRule(CENTER_IN_PARENT)
        view.layoutParams = layoutParams
    }

    private fun initUnderLineView(view: View) {
        val layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mEtUnderLineHeight)
        layoutParams.addRule(ALIGN_PARENT_BOTTOM)
        view.layoutParams = layoutParams
        view.setBackgroundColor(mEtUnderLineDefaultColor)
    }

    private fun initEdittext(editText: EditText) {
        val layoutParams: LayoutParams =
            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.addRule(ALIGN_TOP, mLinearLayout!!.id)
        layoutParams.addRule(ALIGN_BOTTOM, mLinearLayout!!.id)
        editText.layoutParams = layoutParams
        setInputType(editText)
        editText.setBackgroundColor(Color.TRANSPARENT)
        editText.setTextColor(Color.TRANSPARENT)
        editText.isCursorVisible = false
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (editable.isNotEmpty()) {
                    mEditText!!.setText("")
                    setCode(editable.toString())
                }
            }
        })
        // 监听验证码删除按键
        editText.setOnKeyListener { view: View?, keyCode: Int, keyEvent: KeyEvent ->
            if (keyCode == KeyEvent.KEYCODE_DEL && keyEvent.action == KeyEvent.ACTION_DOWN && mCodes.size > 0) {
                mCodes.removeAt(mCodes.size - 1)
                showCode()
                return@setOnKeyListener true
            }
            false
        }
        editText.setOnLongClickListener { v: View? ->
            showPaste()
            false
        }
        getEtFocus(editText)
    }

    private fun initPopupWindow() {
        mPopupWindow =
            PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val tv = TextView(mContext)
        tv.text = "粘贴"
        tv.textSize = 14.0f
        tv.setTextColor(Color.BLACK)
        tv.setBackgroundResource(R.drawable.uikit_layer_paste)
        tv.setPadding(30, 10, 30, 10)
        tv.setOnClickListener { _: View? ->
            setCode(getClipboardString())
            mPopupWindow!!.dismiss()
        }
        mPopupWindow!!.contentView = tv
        mPopupWindow!!.width = LinearLayout.LayoutParams.WRAP_CONTENT // 设置菜单的宽度
        mPopupWindow!!.height = LinearLayout.LayoutParams.WRAP_CONTENT
        mPopupWindow!!.isFocusable = true // 获取焦点
        mPopupWindow!!.isTouchable = true // 设置PopupWindow可触摸
        mPopupWindow!!.isOutsideTouchable = true // 设置非PopupWindow区域可触摸
        //设置点击隐藏 popwindow
        val dw = ColorDrawable(Color.TRANSPARENT)
        mPopupWindow!!.setBackgroundDrawable(dw)
    }

    private fun setEtBackground(rl: RelativeLayout?, background: Int) {
        if (background > 0) {
            rl?.setBackgroundResource(background)
        } else {
            rl?.setBackgroundColor(background)
        }
    }

    private fun getClipboardString(): String? {
        val clipboardManager: ClipboardManager =
            mContext!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        //获取剪贴板中第一条数据
        if (clipboardManager.hasPrimaryClip() && clipboardManager.primaryClipDescription
                ?.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) == true
        ) {
            val itemAt: ClipData.Item = clipboardManager.primaryClip!!.getItemAt(0)
            if (!(TextUtils.isEmpty(itemAt.text))) {
                return itemAt.text.toString()
            }
        }
        return null
    }

    private fun getEtLayoutParams(i: Int): LinearLayout.LayoutParams? {
        val layoutParams = LinearLayout.LayoutParams(mEtWidth, mEtHeight)
        var spacing: Int
        if (!isBisect) {
            spacing = mEtBisectSpacing / 2
        } else {
            spacing = mEtSpacing / 2
            //如果大于最大平分数，将设为最大值
            if (mEtSpacing > mEtBisectSpacing) {
                spacing = mEtBisectSpacing / 2
            }
        }
        if (i == 0) {
            layoutParams.leftMargin = 0
            layoutParams.rightMargin = spacing
        } else if (i == mEtNumber - 1) {
            layoutParams.leftMargin = spacing
            layoutParams.rightMargin = 0
        } else {
            layoutParams.leftMargin = spacing
            layoutParams.rightMargin = spacing
        }
        return layoutParams
    }

    private fun setInputType(textView: TextView) {
        when (mEtInputType) {
            VCInputType.NUMBER_PASSWORD -> {
                textView.inputType =
                    InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
                textView.transformationMethod = AsteriskPasswordTransformationMethod()
            }

            VCInputType.TEXT -> textView.inputType =
                InputType.TYPE_CLASS_TEXT

            VCInputType.TEXT_PASSWORD -> {
                textView.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_NUMBER_VARIATION_PASSWORD
                textView.transformationMethod = AsteriskPasswordTransformationMethod()
            }

            else -> textView.inputType = InputType.TYPE_CLASS_NUMBER
        }
    }

    /**
     * 展示自定义的粘贴板
     */
    private fun showPaste() {
        //去除输入框为数字模式，但粘贴板不是数字模式
        if ((mEtInputType == VCInputType.NUMBER || mEtInputType == VCInputType.NUMBER_PASSWORD) && !isNumeric(
                getClipboardString()
            )
        ) {
            return
        }
        if (!TextUtils.isEmpty(getClipboardString())) {
            if (mPopupWindow == null) {
                initPopupWindow()
            }
            mPopupWindow!!.showAsDropDown(mTextViews!![0], 0, 20)
            InputUtils.hideSoftInput(context as Activity)
        }
    }

    /**
     * 判断粘贴板上的是不是数字
     *
     * @param str
     * @return
     */
    private fun isNumeric(str: String?): Boolean {
        if (TextUtils.isEmpty(str)) {
            return false
        }
        val pattern: Pattern = Pattern.compile("[0-9]*")
        val isNum: Matcher = pattern.matcher(str)
        return if (!isNum.matches()) {
            false
        } else true
    }

    private fun setCode(code: String?) {
        if (TextUtils.isEmpty(code)) {
            return
        }
        for (i in 0 until code!!.length) {
            if (mCodes.size < mEtNumber) {
                mCodes.add(code[i].toString())
            }
        }
        showCode()
    }

    private fun showCode() {
        for (i in 0 until mEtNumber) {
            val textView = mTextViews!![i]
            if (mCodes.size > i) {
                textView!!.text = mCodes[i]
            } else {
                textView!!.text = ""
            }
        }
        setCursorColor() //设置高亮跟光标颜色
        setCallBack() //回调
    }

    /**
     * 设置焦点输入框底部线、光标颜色、背景色
     */
    private fun setCursorColor() {
        valueAnimator?.cancel()
        for (i in 0 until mEtNumber) {
            val cursorView = mCursorViews!![i]
            cursorView?.setBackgroundColor(Color.TRANSPARENT)
            if (mEtUnderLineShow) {
                val underLineView = mUnderLineViews!![i]
                underLineView?.setBackgroundColor(mEtUnderLineDefaultColor)
            }
            if (isFocusBackground) {
                setEtBackground(mRelativeLayouts!![i], mEtBackground)
            }
        }
        if (mCodes.size < mEtNumber) {
            setCursorView(mCursorViews!![mCodes.size])
            if (mEtUnderLineShow) {
                mUnderLineViews!![mCodes.size]?.setBackgroundColor(mEtUnderLineFocusColor)
            }
            if (isFocusBackground) {
                setEtBackground(mRelativeLayouts!![mCodes.size], mEtFocusBackground)
            }
        }
    }

    /**
     * 设置焦点色变换动画
     *
     * @param view
     */
    private fun setCursorView(view: View?) {
        valueAnimator =
            ObjectAnimator.ofInt(
                view,
                "backgroundColor",
                mEtCursorColor,
                R.color.uikit_colorful_transparent
            )
        valueAnimator?.duration = 1500
        valueAnimator?.repeatCount = -1
        valueAnimator?.repeatMode = ValueAnimator.RESTART
        valueAnimator?.setEvaluator(TypeEvaluator { fraction: Float, startValue: Any?, endValue: Any? -> if (fraction <= 0.5f) startValue else endValue })
        valueAnimator?.start()
    }

    private fun setCallBack() {
        if (onInputListener == null) {
            return
        }
        if (mCodes.size == mEtNumber) {
            onInputListener?.onComplete(getCode())
        } else {
            onInputListener?.onInput()
        }
    }

    /**
     * 获得验证码
     *
     * @return 验证码
     */
    fun getCode(): String {
        val sb = StringBuilder()
        for (code in mCodes) {
            sb.append(code)
        }
        return sb.toString()
    }

    /**
     * 清空验证码
     */
    fun clearCode() {
        mCodes.clear()
        showCode()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mViewWidth = measuredWidth
        updateETMargin()
    }

    private fun updateETMargin() {
        //平分Margin，把第一个TextView跟最后一个TextView的间距同设为平分
        mEtBisectSpacing = (mViewWidth - mEtNumber * mEtWidth) / (mEtNumber - 1)
        for (i in 0 until mEtNumber) {
            mLinearLayout!!.getChildAt(i).layoutParams = getEtLayoutParams(i)
        }
    }

    private fun getEtFocus(editText: EditText) {
        editText.isFocusable = true
        editText.isFocusableInTouchMode = true
        editText.requestFocus()
        InputUtils.showSoftInput(context, editText)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        InputUtils.hideSoftInput(context as Activity)
        valueAnimator?.cancel()
    }

    //定义回调
    interface OnInputListener {
        fun onComplete(code: String?)

        fun onInput()
    }

    fun setOnInputListener(onInputListener: OnInputListener?) {
        this.onInputListener = onInputListener
    }
}