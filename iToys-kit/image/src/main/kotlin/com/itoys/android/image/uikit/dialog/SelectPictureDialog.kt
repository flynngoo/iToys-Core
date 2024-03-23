package com.itoys.android.image.uikit.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.itoys.android.image.R
import com.itoys.android.image.databinding.ImageDialogSelectPictureBinding
import com.itoys.android.utils.expansion.dp2px

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/20
 */
class SelectPictureDialog : DialogFragment() {

    companion object {
        fun show(builder: Builder.() -> Unit) {
            val build = Builder.build().apply(builder)
            val dialog = SelectPictureDialog()
            dialog.setBuilder(build)
            build.fm?.apply { dialog.showDialog(this) }
        }
    }

    private var builder: Builder? = null

    fun setBuilder(builder: Builder) {
        this.builder = builder
    }

    class Builder {
        companion object {
            fun build(): Builder {
                return Builder()
            }
        }

        /**
         * Fragment Manager
         */
        var fm: FragmentManager? = null

        /**
         * 回调
         */
        var callback: ISelectCallback? = null
    }

    private var binding: ImageDialogSelectPictureBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.ImageDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initDialog()
        return inflater.inflate(R.layout.image_dialog_select_picture, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ImageDialogSelectPictureBinding.bind(view)
        binding?.takeAPicture?.setOnClickListener {
            builder?.callback?.takePicture()
            dismiss()
        }
        binding?.selectFromAlbum?.setOnClickListener {
            builder?.callback?.selectFromAlbum()
            dismiss()
        }
        binding?.ivClose?.setOnClickListener { dismiss() }
    }

    /**
     * 初始化对话框
     */
    private fun initDialog() {
        dialog?.window?.apply {
            // 设置动画
            setWindowAnimations(R.style.ImageDialog_Animation)
            decorView.setPadding(0, 0, 0, 0)
            val params = attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = 280.dp2px()
            params.gravity = Gravity.BOTTOM
            attributes = params
            isCancelable = true
        }
    }

    /**
     * 显示对话框
     */
    fun showDialog(fm: FragmentManager) {
        show(fm, javaClass.simpleName)
    }

    interface ISelectCallback {

        /**
         * 拍照
         */
        fun takePicture()

        /**
         * 从相册选择
         */
        fun selectFromAlbum()
    }
}