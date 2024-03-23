package com.itoys.android.location.dialog

import android.content.res.Resources
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.hjq.permissions.XXPermissions
import com.itoys.android.location.LocationUtils
import com.itoys.android.location.R
import com.itoys.android.location.databinding.LocationDialogOpenPermissionBinding

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2024/3/4
 */
class OpenPermissionDialog : DialogFragment() {

    companion object {
        /**
         * 显示对话框
         */
        fun show(builder: Builder.() -> Unit) {
            val dialog = OpenPermissionDialog()
            dialog.builder = Builder.create().apply(builder)
            dialog.showDialog()
        }
    }

    /**
     * Dialog builder
     */
    private lateinit var builder: Builder

    /** view binding */
    private var binding: LocationDialogOpenPermissionBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.IToysLocation_Dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LocationDialogOpenPermissionBinding.inflate(inflater)
        initializeDialog()
        return binding?.root
    }

    /**
     * 初始化Dialog
     */
    private fun initializeDialog() {
        // 初始化dialog
        dialog?.window?.apply {
            // 设置 dialog 动画
            setWindowAnimations(R.style.IToysLocation_Dialog_Animation_Fade)
            // 点击蒙层时是否触发关闭事件
            isCancelable = false
            // 遮罩
            setDimAmount(0.5f)
            // 设置水平间距
            decorView.setPadding(dp2px(24), 0, dp2px(24), 0)
            // 设置dialog attributes
            val dialogAttributes = attributes
            dialogAttributes.width = WindowManager.LayoutParams.MATCH_PARENT
            dialogAttributes.height = WindowManager.LayoutParams.WRAP_CONTENT
            dialogAttributes.gravity = Gravity.CENTER
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
    private fun initialize() {
        binding?.btnOpenSettings?.setOnClickListener {
            when (builder.contentRes) {
                R.string.location_permission_denied -> {
                    XXPermissions.startPermissionActivity(requireContext())
                }

                R.string.location_service_not_enable -> {
                    LocationUtils.openLocationService(requireContext())
                }
            }

            dismiss()
        }

        binding?.btnCancel?.setOnClickListener { dismiss() }
    }

    /**
     * dp转px
     */
    private fun dp2px(dp: Int): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    fun showDialog() {
        if (builder.fm == null) {
            return
        }

        builder.fm?.let { show(it, javaClass.canonicalName ?: "OpenPermissionDialog") }
    }

    class Builder {

        /**
         * Fragment Manager
         */
        var fm: FragmentManager? = null

        /**
         * Dialog 内容
         */
        var contentRes = R.string.location_permission_denied

        companion object {

            /**
             * 创建 builder
             */
            fun create(): Builder = Builder()
        }
    }
}