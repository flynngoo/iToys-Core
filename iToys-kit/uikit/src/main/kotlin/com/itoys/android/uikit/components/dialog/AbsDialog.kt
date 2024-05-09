package com.itoys.android.uikit.components.dialog

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.itoys.android.logcat.asLog
import com.itoys.android.logcat.logcat
import com.itoys.android.uikit.R
import com.itoys.android.utils.expansion.tagName
import com.itoys.android.utils.expansion.then

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/12/2
 */
abstract class AbsDialog<out B : AbsDialog.Builder> : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.IToysAndroid_Dialog)
    }

    /**
     * dialog builder
     */
    abstract fun builder(): B

    /**
     * 初始化Dialog
     */
    protected fun initializeDialog() {
        // 初始化dialog
        dialog?.window?.apply {
            // 设置 dialog 动画
            setWindowAnimations(builder().windowAnimationsId)
            // 点击蒙层时是否触发关闭事件
            isCancelable = builder().closeOnOverlayClick
            // 遮罩
            setDimAmount(builder().showOverlay.then(0.5f, 0f))
            // 设置水平间距
            decorView.setPadding(builder().horizontalSpacing, 0, builder().horizontalSpacing, 0)
            // 设置dialog attributes
            val dialogAttributes = attributes
            dialogAttributes.width = builder().dialogWidth
            dialogAttributes.height = builder().dialogHeight
            dialogAttributes.gravity = builder().dialogGravity
            attributes = dialogAttributes
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    /**
     * 初始化
     */
    abstract fun initialize()

    open fun showDialog() {
        if (builder().fm == null) {
            logcat(priority = Log.ERROR) { NullPointerException("Please setter fragment manager!").asLog() }
            return
        }

        val tagName = javaClass.tagName

        builder().fm?.apply {
            try {
                val oldFragment = findFragmentByTag(tagName)
                if (oldFragment == null || (!oldFragment.isAdded && !oldFragment.isVisible)) {
                    show(this, tagName)
                }
            } catch (e: Exception) {
                logcat(priority = Log.ERROR) { e.asLog() }
            }
        }
    }

    abstract class Builder {

        /**
         * Fragment Manager
         */
        open var fm: FragmentManager? = null

        /**
         * Dialog 动画效果, 默认淡入淡出
         */
        open var windowAnimationsId = R.style.IToysAndroid_Dialog_Animation_Fade

        /**
         * Dialog 水平间距
         */
        open var horizontalSpacing = 0

        /**
         * Dialog 宽, 默认为屏幕宽度
         */
        open var dialogWidth = ViewGroup.LayoutParams.MATCH_PARENT

        /**
         * Dialog 高, 默认为自适应
         */
        open var dialogHeight = ViewGroup.LayoutParams.WRAP_CONTENT

        /**
         * Dialog 位置, 默认为居中
         */
        open var dialogGravity = Gravity.CENTER

        /**
         * 是否显示遮罩层
         */
        open var showOverlay = true

        /**
         * 点击蒙层时是否触发关闭事件
         */
        open var closeOnOverlayClick = true

        /**
         * notice title.
         */
        open var title: String = ""

        /**
         * 多按钮排列方式。可选项：horizontal/vertical, 默认：horizontal
         */
        open var buttonLayout = LinearLayout.HORIZONTAL
    }
}