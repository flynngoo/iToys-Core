package com.itoys.android.uikit.components.toast

import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import com.itoys.android.uikit.R
import com.itoys.android.uikit.databinding.UikitLayoutToastyBinding
import com.itoys.android.uikit.databinding.UikitLayoutToastyVerticalBinding
import com.itoys.android.utils.expansion.color
import com.itoys.android.utils.expansion.drawable
import com.itoys.android.utils.expansion.layoutInflater
import com.itoys.android.utils.expansion.tintIcon

/**
 * @author Fanfan.gu <a href="mailto:fanfan.work@outlook.com">Contact me.</a>
 * @date 09/04/2023
 * @desc toasty
 */
object Toasty {

    private var currentTypeface = Typeface.create("sans-serif", Typeface.NORMAL)

    // sp
    private var toastTextSize: Int = 14

    private var tintIcon: Boolean = false
    private var allowQueue: Boolean = false
    private var toastGravity: Int = Gravity.CENTER
    private var xOffset: Int = 0
    private var yOffset: Int = 0
    private var supportDarkTheme: Boolean = true
    private var isRTL: Boolean = false
    private var lastToast: Toast? = null

    const val LENGTH_SHORT: Int = Toast.LENGTH_SHORT
    const val LENGTH_LONG: Int = Toast.LENGTH_LONG

    fun normal(
        context: Context,
        @StringRes message: Int,
        duration: Int = LENGTH_SHORT,
        direction: ToastyDirection = ToastyDirection.Horizontal,
    ): Toast {
        return custom(
            context = context,
            message = context.getString(message),
            duration = duration,
            toastyDirection = direction,
            toastyStatus = null
        )
    }

    fun normal(
        context: Context,
        message: CharSequence,
        duration: Int = LENGTH_SHORT,
        direction: ToastyDirection = ToastyDirection.Horizontal,
    ): Toast {
        return custom(
            context = context,
            message = message,
            duration = duration,
            toastyDirection = direction,
            toastyStatus = null
        )
    }

    fun info(
        context: Context,
        @StringRes message: Int,
        duration: Int = LENGTH_SHORT,
        direction: ToastyDirection = ToastyDirection.Horizontal,
    ): Toast {
        return custom(
            context = context,
            message = context.getString(message),
            duration = duration,
            toastyDirection = direction,
            toastyStatus = ToastyStatus.INFO
        )
    }

    fun info(
        context: Context,
        message: CharSequence,
        duration: Int = LENGTH_SHORT,
        direction: ToastyDirection = ToastyDirection.Horizontal,
    ): Toast {
        return custom(
            context = context,
            message = message,
            duration = duration,
            toastyDirection = direction,
            toastyStatus = ToastyStatus.INFO
        )
    }

    fun warning(
        context: Context,
        @StringRes message: Int,
        duration: Int = LENGTH_SHORT,
        direction: ToastyDirection = ToastyDirection.Horizontal,
    ): Toast {
        return custom(
            context = context,
            message = context.getString(message),
            duration = duration,
            toastyDirection = direction,
            toastyStatus = ToastyStatus.WARING
        )
    }

    fun warning(
        context: Context,
        message: CharSequence,
        duration: Int = LENGTH_SHORT,
        direction: ToastyDirection = ToastyDirection.Horizontal,
    ): Toast {
        return custom(
            context = context,
            message = message,
            duration = duration,
            toastyDirection = direction,
            toastyStatus = ToastyStatus.WARING
        )
    }

    fun success(
        context: Context,
        @StringRes message: Int,
        duration: Int = LENGTH_SHORT,
        direction: ToastyDirection = ToastyDirection.Horizontal,
    ): Toast {
        return custom(
            context = context,
            message = context.getString(message),
            duration = duration,
            toastyDirection = direction,
            toastyStatus = ToastyStatus.SUCCESS
        )
    }

    fun success(
        context: Context,
        message: CharSequence,
        duration: Int = LENGTH_SHORT,
        direction: ToastyDirection = ToastyDirection.Horizontal,
    ): Toast {
        return custom(
            context = context,
            message = message,
            duration = duration,
            toastyDirection = direction,
            toastyStatus = ToastyStatus.SUCCESS
        )
    }

    fun error(
        context: Context,
        @StringRes message: Int,
        duration: Int = LENGTH_SHORT,
        direction: ToastyDirection = ToastyDirection.Horizontal,
    ): Toast {
        return custom(
            context = context,
            message = context.getString(message),
            duration = duration,
            toastyDirection = direction,
            toastyStatus = ToastyStatus.ERROR
        )
    }

    fun error(
        context: Context,
        message: CharSequence,
        duration: Int = LENGTH_SHORT,
        direction: ToastyDirection = ToastyDirection.Horizontal,
    ): Toast {
        return custom(
            context = context,
            message = message,
            duration = duration,
            toastyDirection = direction,
            toastyStatus = ToastyStatus.ERROR
        )
    }

    fun custom(
        context: Context,
        message: CharSequence,
        duration: Int = LENGTH_SHORT,
        toastyDirection: ToastyDirection = ToastyDirection.Horizontal,
        toastyStatus: ToastyStatus?,
    ): Toast {
        return when (toastyDirection) {
            ToastyDirection.Horizontal -> {
                customHorizontal(
                    context = context,
                    message = message,
                    duration = duration,
                    toastyStatus = toastyStatus
                )
            }

            ToastyDirection.Vertical -> {
                customVertical(
                    context = context,
                    message = message,
                    duration = duration,
                    toastyStatus = toastyStatus
                )
            }
        }
    }

    /**
     * 横向 toast
     */
    private fun customHorizontal(
        context: Context,
        message: CharSequence,
        duration: Int,
        toastyStatus: ToastyStatus?,
    ): Toast {
        val currentToast = Toast.makeText(context, "", duration)
        val layoutInflater = context.layoutInflater ?: LayoutInflater.from(context)
        val toastLayoutBinding = UikitLayoutToastyBinding.inflate(layoutInflater)

        toastLayoutBinding.toastyMessage.apply {
            text = message
            setTypeface(currentTypeface, Typeface.NORMAL)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, toastTextSize.toFloat())
        }

        toastLayoutBinding.viewsToastyRoot.background = context.drawable(R.drawable.uikit_toast_frame)
        if (toastyStatus != null) {
            val icon = context.drawable(toastyStatus.icon)
            val tintColor = context.color(toastyStatus.tintColor)

            toastLayoutBinding.viewsToastyRoot.background = if (toastyStatus.shouldTint) {
                context.drawable(R.drawable.uikit_toast_frame)?.tintIcon(tintColor)
            } else {
                context.drawable(R.drawable.uikit_toast_frame)
            }

            if (toastyStatus.withIcon) {
                toastLayoutBinding.toastyIcon.visibility = View.VISIBLE
                if (isRTL) {
                    toastLayoutBinding.viewsToastyRoot.layoutDirection = View.LAYOUT_DIRECTION_RTL
                }

                toastLayoutBinding.toastyIcon.background = if (tintIcon) {
                    icon?.tintIcon(tintColor)
                } else {
                    icon
                }
            }

            toastLayoutBinding.toastyMessage.setTextColor(context.getColor(toastyStatus.textColor))
        }

        currentToast.view = toastLayoutBinding.root

        if (!allowQueue) {
            lastToast?.cancel()
            lastToast = currentToast
        }

        currentToast.setGravity(toastGravity, xOffset, yOffset)
        return currentToast
    }

    /**
     * 竖向 toast
     */
    private fun customVertical(
        context: Context,
        message: CharSequence,
        duration: Int,
        toastyStatus: ToastyStatus?,
    ): Toast {
        val currentToast = Toast.makeText(context, "", duration)
        val layoutInflater = context.layoutInflater ?: LayoutInflater.from(context)
        val toastLayoutBinding = UikitLayoutToastyVerticalBinding.inflate(layoutInflater)

        toastLayoutBinding.toastyMessage.apply {
            text = message
            setTypeface(currentTypeface, Typeface.NORMAL)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, toastTextSize.toFloat())
        }

        if (toastyStatus != null) {
            val icon = context.drawable(toastyStatus.icon)

            toastLayoutBinding.viewsToastyRoot.background =
                context.drawable(R.drawable.uikit_toast_frame_vertical)

            if (toastyStatus.withIcon) {
                toastLayoutBinding.toastyIcon.visibility = View.VISIBLE
                if (isRTL) {
                    toastLayoutBinding.viewsToastyRoot.layoutDirection = View.LAYOUT_DIRECTION_RTL
                }

                toastLayoutBinding.toastyIcon.background = icon
            }

            toastLayoutBinding.toastyMessage.setTextColor(context.getColor(toastyStatus.textColor))
        }

        currentToast.view = toastLayoutBinding.root

        if (!allowQueue) {
            lastToast?.cancel()
            lastToast = currentToast
        }

        currentToast.setGravity(toastGravity, xOffset, yOffset)
        return currentToast
    }

    class Config {

        companion object {
            val INSTANCE = Config()
        }

        private var typeface = currentTypeface
        private var textSize = toastTextSize
        private var tintIcon = Toasty.tintIcon
        private var allowQueue = Toasty.allowQueue
        private var toastGravity = Toasty.toastGravity
        private var xOffset = Toasty.xOffset
        private var yOffset = Toasty.yOffset
        private var supportDarkTheme = Toasty.supportDarkTheme
        private var isRTL = Toasty.isRTL

        fun setToastTypeface(typeface: Typeface): Config {
            this.typeface = typeface
            return this
        }

        fun setTextSize(textSize: Int): Config {
            this.textSize = textSize
            return this
        }

        fun tintIcon(tintIcon: Boolean): Config {
            this.tintIcon = tintIcon
            return this
        }

        fun allowQueue(allowQueue: Boolean): Config {
            this.allowQueue = allowQueue
            return this
        }

        fun setGravity(gravity: Int): Config {
            this.toastGravity = gravity
            return this
        }

        fun supportDarkTheme(supportDarkTheme: Boolean): Config {
            this.supportDarkTheme = supportDarkTheme
            return this
        }

        fun setRTL(isRTL: Boolean): Config {
            this.isRTL = isRTL
            return this
        }

        fun build() {
            currentTypeface = typeface
            toastTextSize = textSize
            Toasty.tintIcon = tintIcon
            Toasty.allowQueue = allowQueue
            Toasty.toastGravity = toastGravity
            Toasty.xOffset = xOffset
            Toasty.yOffset = yOffset
            Toasty.supportDarkTheme = supportDarkTheme
            Toasty.isRTL = isRTL
        }
    }
}