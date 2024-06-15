package com.itoys.android.uikit.components.upload

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.itoys.android.image.IMediaCallback
import com.itoys.android.image.ImageMedia
import com.itoys.android.image.RoundCornerType
import com.itoys.android.image.loadRoundCornerImage
import com.itoys.android.image.selectFromAlbum
import com.itoys.android.image.takePicture
import com.itoys.android.image.uikit.dialog.ChooseImageDialog
import com.itoys.android.uikit.R
import com.itoys.android.uikit.components.dialog.IDialogCallback
import com.itoys.android.uikit.components.dialog.IToysNoticeDialog
import com.itoys.android.uikit.components.image.IViewImageCallback
import com.itoys.android.uikit.databinding.UikitLayoutUploadImageBinding
import com.itoys.android.uikit.viewImage
import com.itoys.android.utils.expansion.doOnClick
import com.itoys.android.utils.expansion.drawable
import com.itoys.android.utils.expansion.gone
import com.itoys.android.utils.expansion.invalid
import com.itoys.android.utils.expansion.isBlank
import com.itoys.android.utils.expansion.isNotBlank
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
) : ConstraintLayout(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private val binding: UikitLayoutUploadImageBinding

    /** 是否必填 */
    private var isRequiredMark = true

    /** 是否可自定义 */
    private var isCustomizable = false

    /** 上传标识 */
    private var uploadMark = ""

    /** 上传文字 label */
    private val uploadTextLabel = "点击上传"

    /** 上传title */
    private var uploadTitle = ""

    /** 是否显示上传logo */
    private var isShowUploadLogo = true

    /** 是否显示上传文字 */
    private var isShowUploadText = true

    /** 图片 */
    private var imageView: AppCompatImageView? = null

    /** 图片圆角 */
    private var imageRoundCorner = 0

    /** 是否显示删除按钮 */
    private var showDelete = true

    /** 删除按钮 */
    private var deleteImageView: AppCompatImageView? = null

    /** owner */
    private var ownerActivity: AppCompatActivity? = null

    /** owner */
    private var ownerFragment: Fragment? = null

    /** 上传回调 */
    private var uploadImageCallback: IUploadCallback? = null

    /** 图片地址 */
    private var imageUrl = ""

    /** 上传是否可用 */
    private var uploadEnable = true

    /** 图片选择回调 */
    private val mediaCallback by lazy {
        object : IMediaCallback() {
            override fun onResult(result: ImageMedia) {
                super.onResult(result)
                setImage(result.mediaPath)

                uploadImageCallback?.upload(uploadMark, result.mediaPath)
            }
        }
    }

    /** 查看大图回调 */
    private val viewImageCallback by lazy {
        object : IViewImageCallback {
            override fun onDelete(position: Int) {
                deleteImage()
            }
        }
    }

    init {
        binding = UikitLayoutUploadImageBinding.inflate(
            LayoutInflater.from(context), this, false
        )
        this.addView(binding.root)

        initView(attrs)
    }

    /**
     * 初始化
     */
    private fun initView(attrs: AttributeSet?) {
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
        // 是否可用
        setUploadEnable(uploadEnable)
        // 释放
        ta.recycle()
    }

    /**
     * 设置owner
     */
    fun setOwner(owner: AppCompatActivity) {
        ownerActivity = owner
    }

    /**
     * 设置owner
     */
    fun setOwner(owner: Fragment) {
        ownerFragment = owner
    }

    /**
     * 自定义选择图片
     */
    fun customImageSelection(customizable: Boolean) {
        isCustomizable = customizable
    }


    /**
     * 获取fragmentManager
     */
    private fun fragmentManager(): FragmentManager? {
        return ownerFragment?.childFragmentManager ?: ownerActivity?.supportFragmentManager
    }

    /**
     * 设置标题
     */
    private fun setTitle(root: UikitLayoutUploadImageBinding, ta: TypedArray) {
        // 必填标识
        isRequiredMark = ta.getBoolean(R.styleable.UploadImageView_uploadImageRequiredMark, true)
        val requiredIcon = ta.getDrawable(R.styleable.UploadImageView_uploadImageRequiredMarkIcon)
        val requiredMarkIconSize = ta.getDimensionPixelOffset(R.styleable.UploadImageView_uploadImageRequiredMarkIconSize, ViewGroup.LayoutParams.WRAP_CONTENT)
        val requiredMarkMargin = ta.getDimensionPixelOffset(R.styleable.UploadImageView_uploadImageRequiredMarkMargin, 0)

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

        root.requiredMark.visibility = isRequiredMark.then(VISIBLE, GONE)

        uploadTitle = ta.getString(R.styleable.UploadImageView_uploadImageTitle).invalid()
        val titleSize = ta.getDimensionPixelOffset(R.styleable.UploadImageView_uploadImageTitleSize, -1)
        val titleColor = ta.getColor(R.styleable.UploadImageView_uploadImageTitleColor, -1)
        val titleMargin = ta.getDimensionPixelOffset(R.styleable.UploadImageView_uploadImageTitleMargin, 0)
        root.title.apply {
            visibility = uploadTitle.isNotBlank().then(View.VISIBLE, View.GONE)

            text = uploadTitle
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
        uploadEnable = ta.getBoolean(R.styleable.UploadImageView_uploadImageEnable, true)
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
        root.uploadImage.doOnClick {
            if (imageUrl.isBlank() && uploadEnable) {
                if (isCustomizable) {
                    uploadImageCallback?.customImageSelection(uploadMark)
                } else {
                    ChooseImageDialog.show {
                        fm = fragmentManager()

                        callback = object : ChooseImageDialog.ISelectCallback {
                            override fun selectFromAlbum() {
                                ownerActivity?.selectFromAlbum(callback = mediaCallback)
                                ownerFragment?.selectFromAlbum(callback = mediaCallback)
                            }

                            override fun takePicture() {
                                ownerActivity?.takePicture(callback = mediaCallback)
                                ownerFragment?.takePicture(callback = mediaCallback)
                            }
                        }
                    }
                }
            } else {
                viewImage()
            }
        }

        showDelete = ta.getBoolean(R.styleable.UploadImageView_uploadImageShowDelete, showDelete)
        root.deleteImage.loadRoundCornerImage(
            R.drawable.image_ic_delete,
            radius = imageRoundCorner,
            cornerType = RoundCornerType.DIAGONAL_FROM_TOP_RIGHT
        )

        deleteImageView = root.deleteImage
        root.deleteImage.doOnClick { showDeleteImageDialog() }
    }

    /**
     * 设置上传logo
     */
    private fun setUploadLogo(root: UikitLayoutUploadImageBinding, ta: TypedArray) {
        isShowUploadLogo = ta.getBoolean(R.styleable.UploadImageView_uploadImageShowLogo, true)
        val logo = ta.getDrawable(R.styleable.UploadImageView_uploadImageLogo)
        val logoSize = ta.getDimensionPixelOffset(R.styleable.UploadImageView_uploadImageLogoSize, ViewGroup.LayoutParams.WRAP_CONTENT)
        root.uploadLogo.visibility = isShowUploadLogo.then(VISIBLE, GONE)

        root.uploadLogo.setImageDrawable(
            logo ?: context.drawable(R.drawable.uikit_ic_upload_image)
        )
        val logoParams = root.uploadLogo.layoutParams as LayoutParams
        logoParams.width = logoSize
        logoParams.height = logoSize
        root.uploadLogo.layoutParams = logoParams
    }

    /**
     * 设置上传文字
     */
    private fun setUploadText(root: UikitLayoutUploadImageBinding, ta: TypedArray) {
        isShowUploadText = ta.getBoolean(R.styleable.UploadImageView_uploadImageShowText, true)
        val uploadText = ta.getString(R.styleable.UploadImageView_uploadImageText).invalid()
        val uploadTextSize = ta.getDimensionPixelOffset(R.styleable.UploadImageView_uploadImageTextSize, -1)
        val uploadTextColor = ta.getColor(R.styleable.UploadImageView_uploadImageTextColor, -1)
        root.uploadText.visibility = isShowUploadText.then(VISIBLE, GONE)
        root.uploadText.apply {
            text = (uploadText.size() > 0).then({ uploadText }, { uploadTextLabel + root.title.text.invalid() })
            if (uploadTextSize != -1) setTextSize(TypedValue.COMPLEX_UNIT_PX, uploadTextSize.toFloat())
            if (uploadTextColor != -1) setTextColor(uploadTextColor)
        }
    }

    /**
     * 设置上传是否可用
     */
    fun setUploadEnable(enable: Boolean) {
        uploadEnable = enable

        if (enable) {
            binding.uploadLogo.visibility = isShowUploadLogo.then(VISIBLE, GONE)
            binding.uploadText.visibility = isShowUploadText.then(VISIBLE, GONE)
            binding.requiredMark.visibility = isRequiredMark.then(VISIBLE, GONE)
            binding.deleteImage.visibility = this.imageUrl.isNotBlank().then(VISIBLE, GONE)
        } else {
            binding.uploadLogo.gone()
            binding.uploadText.gone()
            binding.requiredMark.gone()
            binding.deleteImage.gone()
        }
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
    fun setImage(url: String, needReload: Boolean = true) {
        this.imageUrl = url
        when {
            url.isBlank() -> {
                // 图片地址为空
                imageView?.setImageDrawable(null)
                deleteImageView?.gone()
            }
            else -> {
                if (needReload) {
                    // 需要重新加载图片
                    imageView?.loadRoundCornerImage(url = url, radius = imageRoundCorner)
                    if (showDelete && uploadEnable) deleteImageView?.visible()
                }
            }
        }
    }

    /**
     * 显示删除图片对话框
     */
    private fun showDeleteImageDialog() {
        IToysNoticeDialog.show {
            fm = fragmentManager()
            content = "确定删除${binding.uploadText.text.invalid().replace(uploadTextLabel, "")}吗?"

            callback = object : IDialogCallback() {

                override fun clickCenter() {
                    super.clickCenter()
                    deleteImage()
                }
            }
        }
    }

    /**
     * 查看大图
     */
    private fun viewImage() {
        val context = when {
            ownerActivity != null -> ownerActivity
            ownerFragment != null -> ownerFragment?.requireActivity()
            else -> null
        }

        context?.viewImage(
            imageUrl,
            showDownload = !(showDelete && uploadEnable),
            showDelete = showDelete && uploadEnable,
            callback = viewImageCallback
        )
    }

    /**
     * 删除图片
     */
    fun deleteImage() {
        this.imageUrl = ""
        imageView?.setImageDrawable(null)
        deleteImageView?.gone()
        uploadImageCallback?.delete(uploadMark)
    }

    /**
     * 获取图片标记
     */
    fun imageMark() = uploadMark

    /**
     * 获取图片地址
     */
    fun imageUrl(required: Boolean = false): String {
        // 图片地址是否有效
        val isValidUrl = imageUrl.isNotBlank() && imageUrl.startsWith("http")
        val checkUrl = required.then({ isValidUrl }, { imageUrl.isBlank() || isValidUrl })
        val uploadTitle = binding.uploadText.text.invalid().replace(uploadTextLabel, "")

        check(checkUrl) {
            (imageUrl.isBlank()).then(
                { "请选择「${uploadTitle}」" },
                { "「${uploadTitle}」上传失败, 请重新选择" })
        }
        return imageUrl
    }

    /**
     * 图片
     */
    fun image(required: Boolean = false) = imageMark() to imageUrl(required)
}