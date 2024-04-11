package com.itoys.android.image.uikit.dialog

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.itoys.android.image.R
import com.itoys.android.image.databinding.ImageDialogChooseImageBinding
import com.itoys.android.image.loadImage
import com.itoys.android.utils.expansion.dp2px
import com.itoys.android.utils.expansion.gone
import com.itoys.android.utils.expansion.invalid
import com.itoys.android.utils.expansion.isBlank

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/20
 */
class ChooseImageDialog : DialogFragment() {

    companion object {
        fun show(builder: Builder.() -> Unit) {
            val build = Builder.build().apply(builder)
            val dialog = ChooseImageDialog()
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
         * 示例图片标题
         */
        var demoImageTitle = ""

        /**
         * 示例图片
         */
        var demoImageDrawable: Drawable? = null

        /**
         * 示例图片链接
         */
        var demoImageUrl = ""

        /**
         * 示例图片文字
         */
        var demoImageText = ""

        /**
         * 回调
         */
        var callback: ISelectCallback? = null
    }

    private var binding: ImageDialogChooseImageBinding? = null

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
        return inflater.inflate(R.layout.image_dialog_choose_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ImageDialogChooseImageBinding.bind(view)

        binding?.demoImageTitle?.text = builder?.demoImageTitle.invalid()
        // 标题
        if (builder?.demoImageTitle.isBlank()) binding?.demoImageTitle?.gone()
        // 图片
        if (builder?.demoImageDrawable == null) {
            binding?.demoImage?.loadImage(builder?.demoImageUrl)
        } else {
            binding?.demoImage?.setImageDrawable(builder?.demoImageDrawable)
        }
        if (builder?.demoImageUrl.isBlank() || builder?.demoImageDrawable == null) binding?.demoImage?.gone()
        // 文字
        binding?.demoImageText?.text = builder?.demoImageText.invalid()
        if (builder?.demoImageText.isBlank()) binding?.demoImageText?.gone()

        binding?.takePicture?.setOnClickListener {
            builder?.callback?.takePicture()
            dismiss()
        }
        binding?.selectFromAlbum?.setOnClickListener {
            builder?.callback?.selectFromAlbum()
            dismiss()
        }
        binding?.cancel?.setOnClickListener { dismiss() }
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
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
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