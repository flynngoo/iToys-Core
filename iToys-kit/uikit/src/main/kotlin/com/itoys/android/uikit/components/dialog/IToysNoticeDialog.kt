package com.itoys.android.uikit.components.dialog

import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import androidx.core.text.HtmlCompat
import com.itoys.android.uikit.R
import com.itoys.android.uikit.databinding.UikitDialogLayoutNoticeBinding
import com.itoys.android.utils.expansion.color
import com.itoys.android.utils.expansion.doOnClick
import com.itoys.android.utils.expansion.dp2px
import com.itoys.android.utils.expansion.isBlank
import com.itoys.android.utils.expansion.then
import kotlin.math.max
import kotlin.math.min

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/12/3
 */
class IToysNoticeDialog : IToysDialog<UikitDialogLayoutNoticeBinding, IToysNoticeDialog.Builder>() {
    companion object {
        /**
         * 显示对话框
         */
        fun show(builder: Builder.() -> Unit) {
            val dialog = IToysNoticeDialog()
            dialog.builder = Builder.create().apply(builder)
            dialog.showDialog()
        }
    }

    /**
     * Dialog builder
     */
    private lateinit var builder: Builder

    override fun createViewBinding(inflater: LayoutInflater) = UikitDialogLayoutNoticeBinding.inflate(inflater)

    override fun builder() = builder

    override fun initialize() {
        binding?.title?.text = builder.title
        binding?.title?.visibility = (builder.title.isBlank()).then(View.GONE, View.VISIBLE)
        binding?.content?.text = HtmlCompat.fromHtml(builder.content, HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding?.content?.movementMethod = ScrollingMovementMethod.getInstance()
        binding?.content?.visibility = (builder.content.isBlank()).then(View.GONE, View.VISIBLE)

        binding?.btnLayout?.visibility =
            (builder.buttons.isEmpty() && builder.buttonRes.isEmpty()).then(View.GONE, View.VISIBLE)
        binding?.btnLayout?.orientation = builder.buttonLayout

        val buttonMaxSize = max(builder.buttonRes.size, builder.buttons.size)
        for (index in 0 until min(builder.maxButtons, buttonMaxSize)) {
            when (index) {
                0 -> setStartButtons(index)
                1 -> setCenterButtons(index)
                2 -> setEndButtons(index)
            }
        }
    }

    /**
     * 设置 Start button
     */
    private fun setStartButtons(index: Int) {
        binding?.btnStart?.visibility = View.VISIBLE
        binding?.btnStart?.text = buttonText(index)
        binding?.btnStart?.setTextColor(requireContext().color(buttonTextColor(index)))
        binding?.btnStart?.setBackgroundResource(buttonBackground(index))
        binding?.btnStart?.doOnClick {
            dismiss()
            builder.callback?.clickStart()
        }
    }

    /**
     * 设置 Center button
     */
    private fun setCenterButtons(index: Int) {
        binding?.spaceStart?.visibility = View.VISIBLE
        binding?.btnCenter?.visibility = View.VISIBLE
        binding?.btnCenter?.text = buttonText(index)
        binding?.btnCenter?.setTextColor(requireContext().color(buttonTextColor(index)))
        binding?.btnCenter?.setBackgroundResource(buttonBackground(index))
        binding?.btnCenter?.doOnClick {
            dismiss()
            builder.callback?.clickCenter()
        }
    }

    /**
     * 设置 End button
     */
    private fun setEndButtons(index: Int) {
        binding?.spaceEnd?.visibility = View.VISIBLE
        binding?.btnEnd?.visibility = View.VISIBLE
        binding?.btnEnd?.text = buttonText(index)
        binding?.btnEnd?.setTextColor(requireContext().color(buttonTextColor(index)))
        binding?.btnEnd?.setBackgroundResource(buttonBackground(index))
        binding?.btnEnd?.doOnClick {
            dismiss()
            builder.callback?.clickEnd()
        }
    }

    /**
     * 按钮背景
     */
    private fun buttonBackground(index: Int): Int {
        return try {
            builder.buttonsBackground[index]
        } catch (e: Exception) {
            R.drawable.uikit_primary_button_background_radius_6
        }
    }

    /**
     * 按钮字体
     */
    private fun buttonText(index: Int): String {
        return try {
            requireContext().getString(builder.buttonRes[index])
        } catch (e: Exception) {
            builder.buttons[index]
        }
    }

    /**
     * 按钮字体颜色
     */
    private fun buttonTextColor(index: Int): Int {
        return try {
            builder.buttonsTextColor[index]
        } catch (e: Exception) {
            R.color.uikit_colorful_white
        }
    }

    class Builder : AbsDialog.Builder() {

        companion object {

            /**
             * 创建 builder
             */
            fun create(): Builder = Builder()
        }

        override var horizontalSpacing: Int = 32.dp2px()

        /**
         * notice content.
         */
        var content: String = ""

        /**
         * 按钮 顺序(Start -> Center -> End)
         */
        var buttons = arrayOf("取消", "确认")

        /**
         * 按钮 顺序(Start -> Center -> End)
         */
        var buttonRes = arrayOf<Int>()

        /**
         * 按钮字体颜色 顺序(Start -> Center -> End)
         */
        var buttonsTextColor = arrayOf(
            R.color.uikit_colorful_666666,
            R.color.uikit_colorful_white,
        )

        /**
         * 按钮背景 顺序(Start -> Center -> End)
         */
        var buttonsBackground = arrayOf(
            R.drawable.uikit_rectangle_6_e5e6eb,
            R.drawable.uikit_primary_button_background_radius_6
        )

        /**
         * 最大button数量
         */
        var maxButtons = 3

        /**
         * Dialog 回调
         */
        var callback: IDialogCallback? = null
    }
}