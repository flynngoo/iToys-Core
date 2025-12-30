package com.itoys.android.uikit.components.upload

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.itoys.android.image.loadRoundCornerImage
import com.itoys.android.logcat.logcat
import com.itoys.android.uikit.R
import com.itoys.android.uikit.components.upload.exception.EmptyImageException
import com.itoys.android.uikit.databinding.UikitUploadItemViewBinding
import com.itoys.android.utils.expansion.doOnClick
import com.itoys.android.utils.expansion.dp2px
import com.itoys.android.utils.expansion.empty
import com.itoys.android.utils.expansion.gone
import com.itoys.android.utils.expansion.invalid
import com.itoys.android.utils.expansion.isNotBlank
import com.itoys.android.utils.expansion.isNotNull
import com.itoys.android.utils.expansion.isNull
import com.itoys.android.utils.expansion.visible

/**
 * @Author Flynn Gu
 * @Email gufanfan.worker@gmail.com
 * @Date 2025/11/20
 */
class UploadItemView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private val binding: UikitUploadItemViewBinding = UikitUploadItemViewBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    private var helper: UploadHelper? = null

    private var onDeleteClick: ((String) -> Unit)? = null

    private var onUploadClick: ((String, UploadType) -> Unit)? = null

    private var onPreviewClick: ((String, UploadItem?) -> Unit)? = null

    private var onRetryClick: ((String, UploadItem?) -> Unit)? = null

    /** 上传标识 */
    var uploadMark = ""

    var uploadLabel = ""

    private var uploadType: UploadType = UploadType.IMAGE

    private var currentItem: UploadItem? = null

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.UploadItemView)
        uploadMark = ta.getString(R.styleable.UploadItemView_itemMark).invalid()

        setupClickListeners()
    }

    private fun setupClickListeners() {
        // 预览图
        binding.uploadPreview.doOnClick {
            if (currentItem.isNull() || (currentItem.isEmpty() && currentItem?.isCanDelete == true)) {
                onUploadClick?.invoke(uploadMark, uploadType)
                helper?.choose(uploadMark, uploadType)
            } else {
                helper?.preview(uploadMark, currentItem)
                onPreviewClick?.invoke(uploadMark, currentItem)
            }
        }

        // 重试
        binding.retryContainer.doOnClick {
            if (currentItem.isNull() || (currentItem.isEmpty() && currentItem?.isCanDelete == true)) {
                onUploadClick?.invoke(uploadMark, uploadType)
            } else {
                updateStatusUI(UploadState.Progress(currentItem?.id.invalid(), 0))
                onRetryClick?.invoke(uploadMark, currentItem)
            }
        }

        // 删除
        binding.ivDelete.doOnClick {
            onDeleteClick?.invoke(uploadMark)
            removeItem()
        }
    }

    fun setLabelText(label: String) {
        binding.labelText.text = label
    }

    fun setUploadType(uploadType: UploadType) {
        this.uploadType = uploadType
    }

    fun loadPreview(item: UploadItem, updateStatusUI: Boolean = true) {
        logcat { "Loading preview for item: $item" }
        currentItem = item
        if (updateStatusUI) updateStatusUI(item.state)

        when (item.type) {
            UploadType.IMAGE -> {
                val imageUrl = item.itemUrl()
                if (imageUrl.isNotBlank()) {
                    binding.fileContainer.gone()
                    binding.uploadPreview.loadRoundCornerImage(item.itemUrl(), radius = 8.dp2px())
                }
            }

            UploadType.DOCUMENT -> {
                val fileUrl = item.itemUrl()
                if (fileUrl.isNotBlank()) binding.fileContainer.visible()
            }
        }

        // 当需要更新UI status 并且 state == PENDING
        if (updateStatusUI && item.state == UploadState.PENDING) {
            updateStatusUI(UploadState.Progress(item.id, 0))
        }
    }

    fun updateStatusUI(state: UploadState) {
        logcat { "updateStatusUI: $state" }

        when (state) {
            is UploadState.PENDING -> {
                binding.uploadPreview.isEnabled = true
                binding.uploadPreview.setImageDrawable(null)
                binding.statusContainer.gone()
                binding.fileContainer.gone()
            }

            is UploadState.Progress -> {
                if (currentItem?.id != state.uploadId) return

                binding.uploadPreview.isEnabled = false
                binding.statusContainer.visible()
                binding.uploadProgress.visible()
                binding.retryContainer.gone()
                if (uploadType == UploadType.DOCUMENT) {
                    binding.fileContainer.visible()
                } else {
                    binding.fileContainer.gone()
                }
                // 上传过程中允许删除 避免网络不好一直卡在上传中
                if (currentItem.isNotNull()) binding.ivDelete.visible()

                binding.uploadProgress.setProgress(state.progress, true)
            }

            is UploadState.Success -> {
                if (currentItem?.id != state.uploadId) return

                binding.uploadPreview.isEnabled = true
                binding.statusContainer.gone()
                binding.uploadProgress.gone()

                currentItem?.apply {
                    url = state.url

                    if (url.isNotBlank()) {
                        if (uploadType == UploadType.DOCUMENT) {
                            binding.fileContainer.visible()
                        } else {
                            binding.fileContainer.gone()
                        }
                    }

                    if (isCanDelete && url.isNotBlank()) binding.ivDelete.visible()
                }
            }

            is UploadState.Error -> {
                if (currentItem?.id != state.uploadId) return

                binding.uploadPreview.isEnabled = false
                binding.statusContainer.visible()
                binding.uploadProgress.gone()
                binding.retryContainer.visible()
                binding.retryText.text = state.message
                if (uploadType == UploadType.DOCUMENT) {
                    binding.fileContainer.visible()
                } else {
                    binding.fileContainer.gone()
                }
                binding.ivDelete.visible()
            }
        }
    }

    fun setLayoutHeight(height: Int) {
        if (height > 0) {
            val uploadItemParams = binding.root.layoutParams as LayoutParams
            uploadItemParams.height = height
            binding.root.layoutParams = uploadItemParams
        }
    }

    fun removeItem() {
        currentItem = null
        binding.ivDelete.gone()
        updateStatusUI(UploadState.PENDING)
    }

    fun setUploadHelper(helper: UploadHelper) {
        this.helper = helper
    }

    fun setOnUploadClickListener(listener: (String, UploadType) -> Unit) {
        onUploadClick = listener
    }

    fun setOnPreviewClickListener(listener: (String, UploadItem?) -> Unit) {
        onPreviewClick = listener
    }

    fun setOnRetryClickListener(listener: (String, UploadItem?) -> Unit) {
        onRetryClick = listener
    }

    fun setOnDeleteClickListener(listener: (String) -> Unit) {
        onDeleteClick = listener
    }

    fun itemUrl(required: Boolean = true): String {
        if (required && (currentItem.isNull() || currentItem.isEmpty())) throw EmptyImageException("请上传$uploadLabel")

        return currentItem?.verifyUrl(uploadLabel, required) ?: String.empty()
    }
}