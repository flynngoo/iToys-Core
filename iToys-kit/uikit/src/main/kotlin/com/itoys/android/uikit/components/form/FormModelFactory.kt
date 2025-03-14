package com.itoys.android.uikit.components.form

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.style.UnderlineSpan
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.RadioGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.getSpans
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import com.itoys.android.logcat.logcat
import com.itoys.android.uikit.R
import com.itoys.android.uikit.databinding.UikitLayoutFormEditBinding
import com.itoys.android.uikit.databinding.UikitLayoutFormRadioBinding
import com.itoys.android.uikit.databinding.UikitLayoutFormSelectBinding
import com.itoys.android.uikit.databinding.UikitLayoutFormSwitchBinding
import com.itoys.android.uikit.databinding.UikitLayoutItemRadioBinding
import com.itoys.android.uikit.model.RadioModel
import com.itoys.android.utils.expansion.doOnClick
import com.itoys.android.utils.expansion.dp2px
import com.itoys.android.utils.expansion.email
import com.itoys.android.utils.expansion.gone
import com.itoys.android.utils.expansion.idCard
import com.itoys.android.utils.expansion.isBlank
import com.itoys.android.utils.expansion.isNotBlank
import com.itoys.android.utils.expansion.isNotNull
import com.itoys.android.utils.expansion.landlinePhone
import com.itoys.android.utils.expansion.simpleMobile
import com.itoys.android.utils.expansion.layoutInflater
import com.itoys.android.utils.expansion.size
import com.itoys.android.utils.expansion.then
import com.itoys.android.utils.filter.DecimalDigitsInputFilter
import com.itoys.android.utils.filter.EmojiFilter
import com.itoys.android.utils.filter.IdCardInputFilter
import kotlin.math.max

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/28
 */
object FormModelFactory {

    /**
     * [formModel] @see [FormModel]
     */
    fun create(
        context: Context,
        @FormModel formModel: Int,
        config: FormContentConfig,
    ): View? {
        return when (formModel) {
            FormModel.TEXT -> generateTextModel(context, config)
            FormModel.NUMBER -> generateTextModel(context, config, inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)

            FormModel.PHONE -> generatePhoneModel(context, config)
            FormModel.MOBILE -> generateMobileModel(context, config)
            FormModel.ID_CARD -> generateIdCardModel(context, config)
            FormModel.EMAIL -> generateEmailModel(context, config)

            FormModel.DATE,
            FormModel.SELECT -> generateSelectModel(context, config)

            FormModel.RADIO -> generateRadioModel(context, config)

            FormModel.SWITCH -> generateSwitchModel(context, config)
            else -> null
        }
    }

    /**
     * 设置回调
     */
    fun setCallback(
        contentView: View?,
        @FormModel formModel: Int,
        callback: IFormResultCallback?,
    ) {
        if (contentView == null) return

        when (formModel) {
            FormModel.TEXT,
            FormModel.NUMBER -> {
                val edit: AppCompatEditText = contentView.findViewById(R.id.form_edit)

                if (edit.isEnabled) {
                    edit.addTextChangedListener {
                        val text = it.toString()

                        if (edit.isFocused) {
                            callback?.result(text)
                        }
                        callback?.isAccurate(text.size() > 0)
                    }
                } else {
                    contentView.doOnClick { callback?.click() }
                }
            }

            FormModel.PHONE -> {
                val edit: AppCompatEditText = contentView.findViewById(R.id.form_edit)

                edit.addTextChangedListener {
                    val phone = it.toString()

                    if (edit.isFocused) {
                        callback?.result(phone)
                    }
                    callback?.isAccurate(phone.simpleMobile() || phone.landlinePhone())
                }
            }

            FormModel.MOBILE -> {
                val edit: AppCompatEditText = contentView.findViewById(R.id.form_edit)

                edit.addTextChangedListener {
                    val mobile = it.toString()

                    if (edit.isFocused) {
                        callback?.result(mobile)
                    }
                    callback?.isAccurate(mobile.simpleMobile())
                }
            }

            FormModel.ID_CARD -> {
                val edit: AppCompatEditText = contentView.findViewById(R.id.form_edit)

                edit.addTextChangedListener {
                    val idCard = it.toString()

                    if (edit.isFocused) {
                        callback?.result(idCard)
                    }
                    callback?.isAccurate(idCard.idCard())
                }
            }

            FormModel.EMAIL -> {
                val edit: AppCompatEditText = contentView.findViewById(R.id.form_edit)

                edit.addTextChangedListener {
                    val email = it.toString()

                    if (edit.isFocused) {
                        callback?.result(email)
                    }
                    callback?.isAccurate(email.email())
                }
            }

            FormModel.DATE -> {
                val selectView: ConstraintLayout = contentView.findViewById(R.id.form_select)
                val selectBinding = UikitLayoutFormSelectBinding.bind(selectView)
                selectBinding.formSelect.doOnClick {

                }
            }

            FormModel.ADDRESS,
            FormModel.DATETIME,
            FormModel.SELECT -> {
                val selectView: ConstraintLayout = contentView.findViewById(R.id.form_select)
                val selectBinding = UikitLayoutFormSelectBinding.bind(selectView)
                selectBinding.formSelect.doOnClick { callback?.click() }

                selectBinding.suffixIcon.doOnClick { callback?.suffixIconClick() }
            }

            FormModel.RADIO -> {
                val formRadio: RadioGroup = contentView.findViewById(R.id.form_radio)
                formRadio.setOnCheckedChangeListener { _, checkedId ->
                    callback?.resultId(checkedId)
                }
            }

            FormModel.SWITCH -> {
                val formSwitch: SwitchCompat = contentView.findViewById(R.id.form_switch)
                formSwitch.setOnCheckedChangeListener { _, isChecked ->
                    callback?.result(isChecked)
                }
            }

            else -> {
                logcat { "自定义不需要" }
            }
        }
    }

    /**
     * [formModel] @see [FormModel]
     */
    fun updateContent(
        contentView: View?,
        content: String?,
        @FormModel formModel: Int,
        config: FormContentConfig,
    ) {
        if (contentView == null) return

        when (formModel) {
            FormModel.TEXT,
            FormModel.NUMBER,
            FormModel.PHONE,
            FormModel.MOBILE,
            FormModel.ID_CARD,
            FormModel.EMAIL -> {
                val edit: AppCompatEditText = contentView.findViewById(R.id.form_edit)
                edit.setText(content)
            }

            FormModel.ADDRESS,
            FormModel.DATE,
            FormModel.DATETIME,
            FormModel.SELECT -> {
                val selectView: ConstraintLayout = contentView.findViewById(R.id.form_select)
                val selectBinding = UikitLayoutFormSelectBinding.bind(selectView)
                selectBinding.content.text = (content.isNotBlank()).then(content, config.placeholder)

                selectBinding.content.setTextColor(
                    (content.isBlank() && config.placeholderColor != 0).then(
                        config.placeholderColor,
                        config.contentColor
                    )
                )
            }

            FormModel.SWITCH -> {
                val formSwitch: SwitchCompat = contentView.findViewById(R.id.form_switch)
                formSwitch.isChecked = content.equals("true", ignoreCase = true)
            }

            else -> {
                logcat { "自定义不需要" }
            }
        }
    }

    /**
     * [formModel] @see [FormModel]
     */
    fun updateContentEnable(
        contentView: View?,
        contentEnable: Boolean,
        @FormModel formModel: Int,
    ) {
        if (contentView == null) return

        when (formModel) {
            FormModel.TEXT,
            FormModel.NUMBER,
            FormModel.PHONE,
            FormModel.MOBILE,
            FormModel.ID_CARD,
            FormModel.EMAIL -> {
                val edit: AppCompatEditText = contentView.findViewById(R.id.form_edit)
                edit.isEnabled = contentEnable
            }

            FormModel.ADDRESS,
            FormModel.DATE,
            FormModel.DATETIME,
            FormModel.SELECT -> {
                val selectView: ConstraintLayout = contentView.findViewById(R.id.form_select)
                selectView.isEnabled = contentEnable
            }

            FormModel.RADIO -> {
                val formRadio: RadioGroup = contentView.findViewById(R.id.form_radio)
                formRadio.children.forEach { radio -> radio.isEnabled = contentEnable }
            }

            else -> {
                logcat { "自定义不需要" }
            }
        }
    }

    /**
     * 生成Text Model 表单
     */
    private fun generateTextModel(
        context: Context,
        config: FormContentConfig,
        inputType: Int = InputType.TYPE_CLASS_TEXT,
    ): View? {
        context.layoutInflater?.let { layoutInflater ->
            val editBinding = UikitLayoutFormEditBinding.inflate(layoutInflater)
            editBinding.formEdit.inputType = inputType
            editBinding.formEdit.isEnabled = config.isEnable
            setEditStyle(editBinding.formEdit, config)
            var filters = arrayOf<InputFilter>()
            if (config.maxLength > 0) filters = filters.plus(LengthFilter(config.maxLength))
            if (!config.enableEmoji) filters = filters.plus(EmojiFilter())
            if (config.enableAmount) filters = filters.plus(DecimalDigitsInputFilter(2))
            editBinding.formEdit.filters = filters
            return editBinding.root
        }

        return null
    }

    /**
     * 生成Phone Model 表单
     */
    private fun generatePhoneModel(
        context: Context,
        config: FormContentConfig
    ): View? {
        context.layoutInflater?.let { layoutInflater ->
            val editBinding = UikitLayoutFormEditBinding.inflate(layoutInflater)
            editBinding.formEdit.inputType = InputType.TYPE_CLASS_PHONE
            editBinding.formEdit.isEnabled = config.isEnable
            setEditStyle(editBinding.formEdit, config)
            editBinding.formEdit.filters = arrayOf(LengthFilter(max(11, config.maxLength)))
            return editBinding.root
        }

        return null
    }

    /**
     * 生成Mobile Model 表单
     */
    private fun generateMobileModel(
        context: Context,
        config: FormContentConfig
    ): View? {
        context.layoutInflater?.let { layoutInflater ->
            val editBinding = UikitLayoutFormEditBinding.inflate(layoutInflater)
            editBinding.formEdit.inputType = InputType.TYPE_CLASS_PHONE
            editBinding.formEdit.isEnabled = config.isEnable
            setEditStyle(editBinding.formEdit, config)
            editBinding.formEdit.filters = arrayOf(LengthFilter(11))
            return editBinding.root
        }

        return null
    }

    /**
     * 生成Id Card Model 表单
     */
    private fun generateIdCardModel(
        context: Context,
        config: FormContentConfig,
    ): View? {
        context.layoutInflater?.let { layoutInflater ->
            val editBinding = UikitLayoutFormEditBinding.inflate(layoutInflater)
            editBinding.formEdit.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            editBinding.formEdit.isEnabled = config.isEnable
            setEditStyle(editBinding.formEdit, config)
            var filters = arrayOf<InputFilter>(IdCardInputFilter())
            if (config.maxLength > 0) filters = filters.plus(LengthFilter(config.maxLength))
            editBinding.formEdit.filters = filters
            return editBinding.root
        }

        return null
    }

    /**
     * 生成Email Model 表单
     */
    private fun generateEmailModel(
        context: Context,
        config: FormContentConfig,
    ): View? {
        context.layoutInflater?.let { layoutInflater ->
            val editBinding = UikitLayoutFormEditBinding.inflate(layoutInflater)
            editBinding.formEdit.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            editBinding.formEdit.isEnabled = config.isEnable
            setEditStyle(editBinding.formEdit, config)
            var filters = arrayOf<InputFilter>()
            if (config.maxLength > 0) filters = filters.plus(LengthFilter(config.maxLength))
            if (!config.enableEmoji) filters = filters.plus(EmojiFilter())
            editBinding.formEdit.filters = filters
            return editBinding.root
        }

        return null
    }

    /**
     * 设置edit style
     */
    private fun setEditStyle(
        edit: AppCompatEditText,
        config: FormContentConfig
    ): AppCompatEditText {
        if (config.contentSize > 0) {
            edit.setTextSize(TypedValue.COMPLEX_UNIT_PX, config.contentSize.toFloat())
        }

        if (config.contentColor != 0) {
            edit.setTextColor(config.contentColor)
        }

        // 占位符
        edit.hint = config.placeholder
        // 单行，多行展示
        edit.isSingleLine = false

        if (config.placeholderColor != 0) {
            edit.setHintTextColor(config.placeholderColor)
        }

        edit.gravity = when (config.contentAlign) {
            FormTextAlign.START -> Gravity.START or Gravity.CENTER_VERTICAL
            FormTextAlign.END -> Gravity.END or Gravity.CENTER_VERTICAL
            else -> Gravity.START
        }
        return edit
    }

    /**
     * 添加edit InputFilter
     */
    fun addEditFilter(
        @FormModel formModel: Int,
        contentView: View?,
        filters: Array<InputFilter>
    ) {
        when (formModel) {
            FormModel.TEXT,
            FormModel.NUMBER,
            FormModel.PHONE,
            FormModel.ID_CARD,
            FormModel.EMAIL -> {
                val edit: AppCompatEditText? = contentView?.findViewById(R.id.form_edit)
                edit?.filters = edit?.filters?.plus(filters)
            }
        }
    }

    /**
     * 生成Select Model 表单
     */
    private fun generateSelectModel(
        context: Context,
        config: FormContentConfig,
    ): View? {
        context.layoutInflater?.let { layoutInflater ->
            val selectBinding = UikitLayoutFormSelectBinding.inflate(layoutInflater)
            selectBinding.formSelect.isEnabled = config.isEnable
            if (config.contentSize > 0) {
                selectBinding.content.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    config.contentSize.toFloat()
                )
            }
            selectBinding.content.text = config.placeholder
            if (config.placeholderColor != 0) {
                selectBinding.content.setTextColor(config.placeholderColor)
            }
            selectBinding.content.gravity = when (config.contentAlign) {
                FormTextAlign.START -> Gravity.START or Gravity.CENTER_VERTICAL
                FormTextAlign.END -> Gravity.END or Gravity.CENTER_VERTICAL
                else -> Gravity.START
            }
            return selectBinding.root
        }

        return null
    }

    /**
     * 生成Radio Model 表单
     */
    private fun generateRadioModel(context: Context, config: FormContentConfig): View? {
        context.layoutInflater?.let { layoutInflater ->
            val radioBinding = UikitLayoutFormRadioBinding.inflate(layoutInflater)

            if (config.contentAlign == FormTextAlign.START) {
                radioBinding.formRadio.gravity = Gravity.START or Gravity.CENTER_VERTICAL
            }

            return radioBinding.root
        }

        return null
    }

    /**
     * 生成Switch Model 表单
     */
    private fun generateSwitchModel(context: Context, config: FormContentConfig): View? {
        context.layoutInflater?.let { layoutInflater ->
            val switchBinding = UikitLayoutFormSwitchBinding.inflate(layoutInflater)
            switchBinding.formSwitch.isChecked = config.placeholder.isNotBlank()
            switchBinding.formSwitch.isEnabled = config.isEnable
            return switchBinding.root
        }

        return null
    }

    /**
     * 更新 Radio Model
     */
    fun updateRadioModel(
        contentView: View?,
        contentSize: Int,
        radioList: List<RadioModel>?,
    ) {
        if (contentView == null) return

        val radioGroup: RadioGroup = contentView.findViewById(R.id.form_radio)
        radioGroup.removeAllViews()

        radioList?.forEach { item ->
            val layoutInflater = LayoutInflater.from(contentView.context)
            val radioView = UikitLayoutItemRadioBinding.inflate(layoutInflater)
            val radioLayoutParams = RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT
            )
            radioView.root.id = item.id
            radioView.root.text = item.label
            if (contentSize > 0) {
                radioView.root.setTextSize(TypedValue.COMPLEX_UNIT_PX, contentSize.toFloat())
            }
            radioView.root.isChecked = item.isChecked
            radioLayoutParams.marginStart = 6.dp2px()
            radioLayoutParams.leftMargin = 6.dp2px()
            radioGroup.addView(radioView.root, radioLayoutParams)
        }
    }

    /**
     * 修改Select Model Suffix
     */
    fun changeSelectSuffix(
        contentView: View?,
        suffixIcon: Drawable?,
    ) {
        if (contentView == null) return

        val selectView: ConstraintLayout = contentView.findViewById(R.id.form_select)
        val selectBinding = UikitLayoutFormSelectBinding.bind(selectView)

        if (suffixIcon.isNotNull()) {
            selectBinding.suffixIcon.setImageDrawable(suffixIcon)
        } else {
            selectBinding.suffixIcon.gone()
        }
    }
}