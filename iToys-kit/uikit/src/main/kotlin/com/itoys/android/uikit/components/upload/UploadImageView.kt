package com.itoys.android.uikit.components.upload

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import com.itoys.android.image.RoundCornerType
import com.itoys.android.image.loadRoundCornerImage
import com.itoys.android.uikit.R
import com.itoys.android.uikit.databinding.UikitLayoutUploadImageBinding
import com.itoys.android.utils.expansion.doOnClick
import com.itoys.android.utils.expansion.drawable
import com.itoys.android.utils.expansion.gone
import com.itoys.android.utils.expansion.invalid
import com.itoys.android.utils.expansion.isBlank
import com.itoys.android.utils.expansion.size
import com.itoys.android.utils.expansion.then
import com.itoys.android.utils.expansion.visible

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/4/9
 */
class UploadImageView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    /**
     * 上传标识
     */
    private var uploadMark = ""

    /**
     * 上传文字
     */
    private val uploadTextLabel = "点击上传"

    /**
     * 图片
     */
    private var imageView: AppCompatImageView? = null

    /**
     * 图片圆角
     */
    private var imageRoundCorner = 0

    /**
     * 是否显示删除按钮
     */
    private var showDelete = true

    /**
     * 删除按钮
     */
    private var deleteImageView: AppCompatImageView? = null

    /** fm */
    private var fragmentManager: FragmentManager? = null

    /**
     * 上传回调
     */
    private var uploadImageCallback: IUploadCallback? = null

    /**
     * 图片地址
     */
    private var imageUrl = ""

    init {
        initView(attrs)
    }

    /**
     * 初始化
     */
    private fun initView(attrs: AttributeSet?) {
        val binding = UikitLayoutUploadImageBinding.inflate(
            LayoutInflater.from(context), this, false
        )
        this.addView(binding.root)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.UploadImageView)
        uploadMark = ta.getString(R.styleable.UploadImageView_uploadImageMark).invalid()
        // 标题
        setTitle(binding, ta)
        // 图片
        setImage(binding, ta)
        // 上传logo
        setUploadLogo(binding, ta)
        // 上传文字
        setUploadText(binding, ta)
        // 释放
        ta.recycle()
    }

    /**
     * 设置标题
     */
    private fun setTitle(root: UikitLayoutUploadImageBinding, ta: TypedArray) {
        // 必填标识
        val requiredMark = ta.getBoolean(R.styleable.UploadImageView_uploadImageRequiredMark, true)
        val requiredIcon = ta.getDrawable(R.styleable.UploadImageView_uploadImageRequiredMarkIcon)
        val requiredMarkIconSize = ta.getDimensionPixelOffset(R.styleable.UploadImageView_uploadImageRequiredMarkIconSize, ViewGroup.LayoutParams.WRAP_CONTENT)
        val requiredMarkMargin = ta.getDimensionPixelOffset(R.styleable.UploadImageView_uploadImageRequiredMarkMargin, 0)
        root.requiredMark.visibility = requiredMark.then(VISIBLE, GONE)
        if (requiredMark) {
            root.requiredMark.setImageDrawable(
                requiredIcon ?: context.drawable(R.drawable.uikit_ic_required_mark)
            )
            val requiredMarkParams = root.requiredMark.layoutParams as MarginLayoutParams
            requiredMarkParams.setMargins(
                requiredMarkMargin, 0, 0, 0
            )
            requiredMarkParams.width = requiredMarkIconSize
            requiredMarkParams.height = requiredMarkIconSize
            root.requiredMark.layoutParams = requiredMarkParams
        }

        val title = ta.getString(R.styleable.UploadImageView_uploadImageTitle).invalid()
        val titleSize = ta.getDimensionPixelOffset(R.styleable.UploadImageView_uploadImageTitleSize, -1)
        val titleColor = ta.getColor(R.styleable.UploadImageView_uploadImageTitleColor, -1)
        val titleMargin = ta.getDimensionPixelOffset(R.styleable.UploadImageView_uploadImageTitleMargin, 0)
        root.title.apply {
            text = title
            if (titleSize != -1) setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize.toFloat())
            if (titleColor != -1) setTextColor(titleColor)

            val titleParams = layoutParams as MarginLayoutParams
            titleParams.setMargins(
                titleMargin, 0, 0, 0
            )
            layoutParams = titleParams
        }
    }

    /**
     * 设置图片
     */
    private fun setImage(root: UikitLayoutUploadImageBinding, ta: TypedArray) {
        val imageWidth = ta.getDimensionPixelOffset(R.styleable.UploadImageView_uploadImageWidth, LayoutParams.MATCH_PARENT)
        val imageHeight = ta.getDimensionPixelOffset(R.styleable.UploadImageView_uploadImageHeight, LayoutParams.WRAP_CONTENT)
        val spacing = ta.getDimensionPixelOffset(R.styleable.UploadImageView_uploadImageSpacing, 0)
        imageRoundCorner = ta.getDimensionPixelOffset(R.styleable.UploadImageView_uploadImageRoundCorner, 0)

        val imageParams = root.uploadImage.layoutParams as MarginLayoutParams
        imageParams.setMargins(0, spacing, 0, 0)
        imageParams.width = imageWidth
        imageParams.height = imageHeight
        root.uploadImage.layoutParams = imageParams

        val imageBackground = ta.getDrawable(R.styleable.UploadImageView_uploadImageBackground)
            ?: context.drawable(R.drawable.uikit_background_upload_image)
        imageBackground?.let { root.imageBackground.loadRoundCornerImage(imageBackground, radius = imageRoundCorner) }

        imageView = root.uploadImage
        root.uploadImage.doOnClick { uploadImageCallback?.upload(uploadMark) }

        showDelete = ta.getBoolean(R.styleable.UploadImageView_uploadImageShowDelete, showDelete)
        root.deleteImage.loadRoundCornerImage(
            R.drawable.image_ic_delete,
            radius = imageRoundCorner,
            cornerType = RoundCornerType.DIAGONAL_FROM_TOP_RIGHT
        )

        deleteImageView = root.deleteImage
        root.deleteImage.doOnClick { uploadImageCallback?.delete(uploadMark) }
    }

    /**
     * 设置上传logo
     */
    private fun setUploadLogo(root: UikitLayoutUploadImageBinding, ta: TypedArray) {
        val showLogo = ta.getBoolean(R.styleable.UploadImageView_uploadImageShowLogo, true)
        val logo = ta.getDrawable(R.styleable.UploadImageView_uploadImageLogo)
        val logoSize = ta.getDimensionPixelOffset(R.styleable.UploadImageView_uploadImageLogoSize, ViewGroup.LayoutParams.WRAP_CONTENT)
        root.uploadLogo.visibility = showLogo.then(VISIBLE, GONE)
        if (showLogo) {
            root.uploadLogo.setImageDrawable(
                logo ?: context.drawable(R.drawable.uikit_ic_upload_image)
            )
            val logoParams = root.uploadLogo.layoutParams as ConstraintLayout.LayoutParams
            logoParams.width = logoSize
            logoParams.height = logoSize
            root.uploadLogo.layoutParams = logoParams
        }
    }

    /**
     * 设置上传文字
     */
    private fun setUploadText(root: UikitLayoutUploadImageBinding, ta: TypedArray) {
        val showText = ta.getBoolean(R.styleable.UploadImageView_uploadImageShowText, true)
        val uploadText = ta.getString(R.styleable.UploadImageView_uploadImageText).invalid()
        val uploadTextSize = ta.getDimensionPixelOffset(R.styleable.UploadImageView_uploadImageTextSize, -1)
        val uploadTextColor = ta.getColor(R.styleable.UploadImageView_uploadImageTextColor, -1)
        root.uploadText.visibility = showText.then(VISIBLE, GONE)
        if (showText) {
            root.uploadText.apply {
                text = (uploadText.size() > 0).then({ uploadText },
                    { uploadTextLabel + root.title.text })
                if (uploadTextSize != -1) setTextSize(TypedValue.COMPLEX_UNIT_PX, uploadTextSize.toFloat())
                if (uploadTextColor != -1) setTextColor(uploadTextColor)
            }
        }
    }

    /**
     * 设置 fm
     */
    fun setFragmentManager(fm: FragmentManager) {
        this.fragmentManager = fm
    }

    /**
     * 设置回调
     */
    fun setUploadImageCallback(uploadImageCallback: IUploadCallback?) {
        this.uploadImageCallback = uploadImageCallback
    }

    /**
     * 设置图片
     */
    fun setImage(url: String) {
        this.imageUrl = url
        if (url.isBlank()) {
            imageView?.setImageDrawable(null)
            deleteImageView?.gone()
        } else {
            imageView?.loadRoundCornerImage(url = url, radius = imageRoundCorner)
            if (showDelete) deleteImageView?.visible()
        }
    }

    /**
     * 删除图片
     */
    fun deleteImage() {
        this.imageUrl = ""
        imageView?.setImageDrawable(null)
        deleteImageView?.gone()
    }

    /**
     * 获取图片地址
     */
    fun imageUrl() = imageUrl
}