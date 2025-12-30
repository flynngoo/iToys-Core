package com.itoys.android.uikit.components.upload

import android.R.attr.spacing
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.models
import com.itoys.android.logcat.logcat
import com.itoys.android.uikit.R
import com.itoys.android.uikit.databinding.UikitLayoutUploadWithHeaderBinding
import com.itoys.android.utils.expansion.dp2px
import com.itoys.android.utils.expansion.drawable
import com.itoys.android.utils.expansion.empty
import com.itoys.android.utils.expansion.invalid
import com.itoys.android.utils.expansion.isNotBlank
import com.itoys.android.utils.expansion.then

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/4/9
 */
class UploadWithHeaderView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private val binding: UikitLayoutUploadWithHeaderBinding =
        UikitLayoutUploadWithHeaderBinding.inflate(
            LayoutInflater.from(context), this, true
        )

    /** 是否必填 */
    private var isRequiredMark = true

    /** 上传标识 */
    var uploadMark = ""

    /** 上传title */
    private var uploadTitle = ""

    private var uploadType: UploadType = UploadType.IMAGE

    private var helper: UploadHelper? = null

    private var onRetryClick: ((String, UploadItem?) -> Unit)? = null

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.UploadWithHeaderView)
        uploadMark = ta.getString(R.styleable.UploadWithHeaderView_uploadMark).invalid()

        // title
        // 必填标识
        isRequiredMark = ta.getBoolean(R.styleable.UploadWithHeaderView_uploadRequiredMark, true)
        val requiredIcon = ta.getDrawable(R.styleable.UploadWithHeaderView_uploadRequiredMarkIcon)
        val requiredMarkIconSize = ta.getDimensionPixelOffset(
            R.styleable.UploadWithHeaderView_uploadRequiredMarkIconSize,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val requiredMarkMargin =
            ta.getDimensionPixelOffset(R.styleable.UploadWithHeaderView_uploadRequiredMarkMargin, 0)

        binding.requiredMark.setImageDrawable(
            requiredIcon ?: context.drawable(R.drawable.uikit_ic_required_mark)
        )
        val requiredMarkParams = binding.requiredMark.layoutParams as MarginLayoutParams
        requiredMarkParams.setMargins(
            requiredMarkMargin, 0, 0, 0
        )
        requiredMarkParams.width = requiredMarkIconSize
        requiredMarkParams.height = requiredMarkIconSize
        binding.requiredMark.layoutParams = requiredMarkParams

        binding.requiredMark.visibility = isRequiredMark.then(VISIBLE, GONE)

        uploadTitle = ta.getString(R.styleable.UploadWithHeaderView_uploadTitle).invalid()
        binding.uploadItem.uploadLabel = uploadTitle
        val titleSize = ta.getDimensionPixelOffset(R.styleable.UploadWithHeaderView_uploadTitleSize, -1)
        val titleColor = ta.getColor(R.styleable.UploadWithHeaderView_uploadTitleColor, -1)
        val titleMargin = ta.getDimensionPixelOffset(R.styleable.UploadWithHeaderView_uploadTitleMargin, 4.dp2px())
        binding.title.apply {
            visibility = uploadTitle.isNotBlank().then(VISIBLE, GONE)

            text = uploadTitle
            if (titleSize != -1) setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize.toFloat())
            if (titleColor != -1) setTextColor(titleColor)

            val titleParams = layoutParams as MarginLayoutParams
            titleParams.setMargins(
                titleMargin, 0, 0, 0
            )
            layoutParams = titleParams
        }

        val uploadItemWidth = ta.getDimensionPixelOffset(R.styleable.UploadWithHeaderView_uploadItemWidth, LayoutParams.MATCH_PARENT)
        val uploadItemHeight = ta.getDimensionPixelOffset(R.styleable.UploadWithHeaderView_uploadItemHeight, LayoutParams.MATCH_PARENT)
        if (uploadItemWidth != LayoutParams.MATCH_PARENT) {
            val uploadItemParams = binding.uploadItem.layoutParams as LayoutParams
            uploadItemParams.width = uploadItemWidth
            binding.uploadItem.layoutParams = uploadItemParams
        }
        binding.uploadItem.setLayoutHeight(uploadItemHeight)

        binding.uploadItem.uploadMark = uploadMark
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.uploadItem.setOnRetryClickListener { _, uploadItem ->
            onRetryClick?.invoke(uploadMark, uploadItem)
        }
    }

    fun uploadItemView() = binding.uploadItem

    fun setUploadHelper(helper: UploadHelper) {
        this.helper = helper
        binding.uploadItem.setUploadHelper(helper)
    }

    fun loadPreview(item: UploadItem, updateStatusUI: Boolean = true) {
        binding.uploadItem.loadPreview(item, updateStatusUI)
    }

    fun updateStatusUI(state: UploadState) {
        binding.uploadItem.updateStatusUI(state)
    }

    fun setUploadType(uploadType: UploadType) {
        this.uploadType = uploadType
    }

    fun setOnRetryClickListener(listener: (String, UploadItem?) -> Unit) {
        onRetryClick = listener
    }

    fun itemUrl(required: Boolean = true): String {
        return binding.uploadItem.itemUrl(required)
    }
}