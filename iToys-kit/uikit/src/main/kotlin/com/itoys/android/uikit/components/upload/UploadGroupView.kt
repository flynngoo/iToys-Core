package com.itoys.android.uikit.components.upload

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.itoys.android.uikit.R
import com.itoys.android.uikit.components.upload.exception.EmptyImageException
import com.itoys.android.uikit.components.upload.exception.UploadFailedException
import com.itoys.android.uikit.databinding.UikitLayoutUploadGroupBinding
import com.itoys.android.utils.SysUtils
import com.itoys.android.utils.expansion.gone
import com.itoys.android.utils.expansion.invalid
import com.itoys.android.utils.expansion.isBlank
import com.itoys.android.utils.expansion.isNotBlank
import com.itoys.android.utils.expansion.visible

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/4/9
 */
class UploadGroupView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private val binding: UikitLayoutUploadGroupBinding = UikitLayoutUploadGroupBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    /** 上传标识 */
    var uploadMark = ""

    private var currentUploadMark = ""

    private val itemIdMap = mutableMapOf<String, UploadItemView>()

    private var helper: UploadHelper? = null

    private var onGroupUploadClick: ((String, UploadType) -> Unit)? = null

    private var onGroupPreviewClick: ((String, UploadItem?) -> Unit)? = null

    private var onGroupRetryClick: ((String, UploadItem?) -> Unit)? = null

    private val uploadItemUploadClick: ((String, UploadType) -> Unit) = { mark, uploadType ->
        currentUploadMark = mark
        helper?.choose(uploadMark, uploadType)
        onGroupUploadClick?.invoke(uploadMark, uploadType)
    }

    private val uploadItemPreviewClick: ((String, UploadItem?) -> Unit) = { mark, uploadItem ->
        currentUploadMark = mark
        helper?.preview(uploadMark, uploadItem)
        onGroupPreviewClick?.invoke(uploadMark, uploadItem)
    }

    private val uploadItemRetryClick: ((String, UploadItem?) -> Unit) = { mark, uploadItem ->
        currentUploadMark = mark
        onGroupRetryClick?.invoke(uploadMark, uploadItem)
    }

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.UploadGroupView)
        val requiredMark = ta.getBoolean(R.styleable.UploadGroupView_uploadGroupRequiredMark, false)
        if (requiredMark) {
            binding.requiredMark.visible()
        }

        uploadMark = ta.getString(R.styleable.UploadGroupView_uploadGroupMark).invalid()

        val title = ta.getString(R.styleable.UploadGroupView_uploadGroupTitle).invalid()
        binding.title.text = title
        binding.uploadItemLeft.uploadLabel = title
        binding.uploadItemRight.uploadLabel = title

        val optionalTitle = ta.getString(R.styleable.UploadGroupView_uploadGroupOptionalTitle).invalid()
        binding.optionalTitle.text = optionalTitle
        if (optionalTitle.isBlank()) binding.optionalTitle.gone()

        val subtitle = ta.getString(R.styleable.UploadGroupView_uploadGroupSubtitle).invalid()
        binding.subtitle.text = subtitle
        if (subtitle.isBlank()) binding.subtitle.gone()

        val leftLabel = ta.getString(R.styleable.UploadGroupView_uploadGroupLeftLabel).invalid()
        binding.uploadItemLeft.setLabelText(leftLabel)

        val rightLabel = ta.getString(R.styleable.UploadGroupView_uploadGroupRightLabel).invalid()
        binding.uploadItemRight.setLabelText(rightLabel)

        // onUploadClick
        binding.uploadItemLeft.setOnUploadClickListener(uploadItemUploadClick)
        binding.uploadItemRight.setOnUploadClickListener(uploadItemUploadClick)

        // onPreviewClick
        binding.uploadItemLeft.setOnPreviewClickListener(uploadItemPreviewClick)
        binding.uploadItemRight.setOnPreviewClickListener(uploadItemPreviewClick)

        // onRetryClick
        binding.uploadItemLeft.setOnRetryClickListener(uploadItemRetryClick)
        binding.uploadItemRight.setOnRetryClickListener(uploadItemRetryClick)
    }

    fun setTitle(title: String) {
        binding.title.text = title
    }

    fun setOptionalTitle(optionalTitle: String) {
        binding.optionalTitle.text = optionalTitle
    }

    fun setSubtitle(subtitle: String) {
        binding.subtitle.text = subtitle
        if (subtitle.isNotBlank()) binding.subtitle.visible()
    }

    fun setUploadLabel(leftLabel: String, rightLabel: String) {
        binding.uploadItemLeft.setLabelText(leftLabel)
        binding.uploadItemRight.setLabelText(rightLabel)
    }

    fun setUploadType(leftType: UploadType, rightType: UploadType) {
        binding.uploadItemLeft.setUploadType(leftType)
        binding.uploadItemRight.setUploadType(rightType)
    }

    fun setUploadHelper(helper: UploadHelper) {
        this.helper = helper
    }

    fun setOnUploadClickListener(listener: (String, UploadType) -> Unit) {
        onGroupUploadClick = listener
    }

    fun setOnPreviewClickListener(listener: (String, UploadItem?) -> Unit) {
        onGroupPreviewClick = listener
    }

    fun setOnRetryClickListener(listener: (String, UploadItem?) -> Unit) {
        onGroupRetryClick = listener
    }

    fun leftUploadItemView() = binding.uploadItemLeft

    fun rightUploadItemView() = binding.uploadItemRight

    fun loadPreview(item: UploadItem) {
        when (currentUploadMark) {
            binding.uploadItemLeft.uploadMark -> {
                binding.uploadItemLeft.loadPreview(item)
                itemIdMap[item.id] = binding.uploadItemLeft
            }
            binding.uploadItemRight.uploadMark -> {
                binding.uploadItemRight.loadPreview(item)
                itemIdMap[item.id] = binding.uploadItemRight
            }
        }
    }

    fun updateStatusUI(itemId: String, state: UploadState) {
        itemIdMap[itemId]?.updateStatusUI(state)
    }

    fun removeItem() {
        when (currentUploadMark) {
            binding.uploadItemLeft.uploadMark -> binding.uploadItemLeft.removeItem()
            binding.uploadItemRight.uploadMark -> binding.uploadItemRight.removeItem()
        }
    }

    fun verifyAllUrl(): Pair<String, String> {
        val leftUrl = binding.uploadItemLeft.itemUrl()
        val rightUrl = binding.uploadItemRight.itemUrl()
        return leftUrl to rightUrl
    }

    /**
     * 校验规则：
     * - 至少 imageUrl 或 fileUrl 有值
     * - 对于有值的项，必须是 http/https URL
     *
     * 返回 true 表示允许提交；false 表示不允许
     */
    fun verifyUrl(required: Boolean = true): Boolean {
        val leftUrl = binding.uploadItemLeft.itemUrl(required = false)
        val rightUrl = binding.uploadItemRight.itemUrl(required = false)

        val leftProvided = leftUrl.isNotBlank()
        val rightProvided = rightUrl.isNotBlank()

        // 1. 如果必传 → 至少需要一个有效 URL
        if (required) {
            if (!leftProvided && !rightProvided) {
                throw EmptyImageException("「${binding.title.text}」请提供至少一个")
            }
        }

        // 2. 对于“有值”的项 → 必须是 http/https URL
        if (leftProvided && !SysUtils.isHttpUrl(leftUrl)) throw UploadFailedException("「${binding.title.text}」上传失败, 请重新选择")
        if (rightProvided && !SysUtils.isHttpUrl(rightUrl)) throw UploadFailedException("「${binding.title.text}」上传失败, 请重新选择")

        return true
    }

    /**
     * 获取left地址
     */
    fun validLeftUrl() = binding.uploadItemLeft.itemUrl(required = false)

    /**
     * 获取right地址
     */
    fun validRightUrl() = binding.uploadItemRight.itemUrl(required = false)
}