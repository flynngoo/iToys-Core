package com.itoys.android.uikit.components.upload

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.drake.brv.annotaion.DividerOrientation
import com.drake.brv.utils.addModels
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.divider
import com.drake.brv.utils.grid
import com.drake.brv.utils.models
import com.drake.brv.utils.mutable
import com.drake.brv.utils.setup
import com.itoys.android.logcat.logcat
import com.itoys.android.uikit.R
import com.itoys.android.uikit.databinding.UikitLayoutUploadListBinding
import com.itoys.android.uikit.databinding.UikitItemUploadItemBinding
import com.itoys.android.utils.expansion.gone
import com.itoys.android.utils.expansion.invalid
import com.itoys.android.utils.expansion.isNotBlank
import com.itoys.android.utils.expansion.isNull
import kotlin.math.max
import kotlin.math.min

/**
 * @Author Flynn Gu
 * @Email gufanfan.worker@gmail.com
 * @Date 2025/11/20
 */
class UploadListView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private val binding: UikitLayoutUploadListBinding = UikitLayoutUploadListBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    private var uploadType: UploadType = UploadType.IMAGE

    private var helper: UploadHelper? = null

    /** 文件列表 */
    private val uploadFiles = arrayListOf<UploadItem>()

    /** 是否必填 */
    private var isRequiredMark = true

    /** 上传标识 */
    var uploadMark = ""

    /** 最大长度 */
    private var maximum = 9

    /** 最大选择长度 */
    private var selectMaximum = 9

    /**
     * 显示添加文件
     */
    var showPlus = true

    private var onUploadClick: ((String, UploadType, Int) -> Unit)? = null

    private var onPreviewListClick: ((String, List<UploadItem>, Int, Boolean) -> Unit)? = null

    private var onPreviewClick: ((String, UploadItem?) -> Unit)? = null

    private var onRetryClick: ((String, UploadItem?) -> Unit)? = null

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.UploadListView)
        uploadMark = ta.getString(R.styleable.UploadListView_uploadListMark).invalid()
        maximum = ta.getInt(R.styleable.UploadListView_uploadListMaximum, maximum)
        selectMaximum = ta.getInt(R.styleable.UploadListView_uploadSelectMaximum, selectMaximum)
        isRequiredMark = ta.getBoolean(R.styleable.UploadListView_uploadListRequired, isRequiredMark)
        setRequiredMark(isRequiredMark)

        val uploadTitle = ta.getString(R.styleable.UploadListView_uploadListTitle).invalid()
        binding.title.text = uploadTitle

        setupUploadList()
    }

    private fun setupUploadList() {
        binding.uploadList.apply {
            grid(spanCount = 3).divider {
                orientation = DividerOrientation.GRID
                setDivider(6, true)
            }.setup {
                addType<UploadItem>(R.layout.uikit_item_upload_item)

                onBind {
                    val itemBinding = getBinding<UikitItemUploadItemBinding>()
                    val item: UploadItem = getModel()
                    itemBinding.uploadItem.setUploadType(uploadType)

                    if (item.isEmpty()) {
                        itemBinding.uploadItem.updateStatusUI(UploadState.PENDING)
                    } else {
                        when (item.state) {
                            is UploadState.PENDING -> itemBinding.uploadItem.loadPreview(item)

                            else -> {
                                itemBinding.uploadItem.loadPreview(item, false)
                                itemBinding.uploadItem.updateStatusUI(item.state)
                            }
                        }
                    }

                    itemBinding.uploadItem.setOnUploadClickListener { _, _ ->
                        val size = min((maximum - uploadFiles.size), selectMaximum)
                        helper?.choose(uploadMark, uploadType, size)
                        onUploadClick?.invoke(uploadMark, uploadType, size)
                    }

                    itemBinding.uploadItem.setOnPreviewClickListener { _, item ->
                        if (uploadType == UploadType.IMAGE) {
                            helper?.preview(
                                uploadMark,
                                uploadFiles,
                                modelPosition,
                                showDownload = !showPlus,
                                showDelete = showPlus
                            )
                            onPreviewListClick?.invoke(uploadMark, uploadFiles, modelPosition, showPlus)
                        } else {
                            onPreviewClick?.invoke(uploadMark, item)
                            helper?.preview(uploadMark, item)
                        }
                    }

                    itemBinding.uploadItem.setOnRetryClickListener { _, uploadItem ->
                        onRetryClick?.invoke(uploadMark, uploadItem)
                    }

                    itemBinding.uploadItem.setOnDeleteClickListener { _ ->
                        removeItem(modelPosition)
                    }
                }
            }

            if (showPlus) models = listOf("".toImageItem())
        }
    }

    fun setUploadHelper(helper: UploadHelper) {
        this.helper = helper
    }

    fun setUploadType(uploadType: UploadType) {
        this.uploadType = uploadType
    }

    fun setRequiredMark(isRequiredMark: Boolean) {
        this.isRequiredMark = isRequiredMark

        if (!isRequiredMark) {
            binding.requiredMark.gone()
        }
    }

    fun setMaximum(max: Int) {
        this.maximum = max
    }

    fun setHeaderTitle(title: String) {
        binding.title.text = title
    }

    fun hideHeader() {
        binding.headerContainer.gone()
    }

    fun addUploadItems(items: List<UploadItem>) {
        uploadFiles.addAll(items)

        if (showPlus) {
            resetItems(items)
        } else {
            binding.uploadList.models = items
        }
    }

    private fun resetItems(items: List<UploadItem>) {
        var modelSize = binding.uploadList.models?.size ?: 0
        val lastModel = binding.uploadList.models?.last() as UploadItem?
        if (modelSize > 0 && (lastModel.isNull() || lastModel.isEmpty())) {
            modelSize -= 1
        }

        val totalSize = items.size + modelSize

        when {
            showPlus && totalSize < maximum -> {
                binding.uploadList.addModels(items, index = max(modelSize, 0))
            }

            else -> {
                if (showPlus) {
                    // 如果需要添加按钮情况下删除最后item
                    binding.uploadList.mutable.removeAt(modelSize)
                    binding.uploadList.bindingAdapter.notifyItemRemoved(modelSize)
                }

                binding.uploadList.addModels(items)
            }
        }

        // 刷新分割线
        binding.uploadList.postDelayed({ binding.uploadList.invalidateItemDecorations() }, 200)
    }

    fun removeItem(position: Int) {
        binding.uploadList.mutable.removeAt(position)
        binding.uploadList.bindingAdapter.notifyItemRemoved(position)
        // 删除照片
        uploadFiles.removeAt(position)

        if (showPlus) {
            binding.uploadList.postDelayed({
                val models = binding.uploadList.models ?: emptyList()

                when {
                    // 如果删除后没有图片 && 支持选择上传
                    models.isEmpty() -> binding.uploadList.models = listOf("".toImageItem())

                    // picture size == maximum删除后last != 空字符串，添加空字符串
                    else -> {
                        val lastModel = binding.uploadList.models?.last() as UploadItem?

                        if (!(lastModel.isNull() || lastModel.isEmpty())) {
                            binding.uploadList.addModels(listOf("".toImageItem()))
                        }
                    }
                }
            }, 200)
        }
    }

    fun updateStatusUI(id: String, state: UploadState) {
        val index = binding.uploadList.models?.indexOfFirst { it is UploadItem && it.id == id } ?: -1
        logcat { "updateStatusUI: id: $id, state: $state, index: $index" }

        if (index != -1) {
            val uploadItem = binding.uploadList.bindingAdapter.getModel<UploadItem>(index)
            uploadItem.state = state
            binding.uploadList.bindingAdapter.notifyItemChanged(index)
        }
    }

    fun setOnUploadClickListener(listener: (String, UploadType, Int) -> Unit) {
        onUploadClick = listener
    }

    fun setOnPreviewListClickListener(listener: (String, List<UploadItem>, Int, Boolean) -> Unit) {
        onPreviewListClick = listener
    }

    fun setOnPreviewClickListener(listener: (String, UploadItem?) -> Unit) {
        onPreviewClick = listener
    }

    fun setOnRetryClickListener(listener: (String, UploadItem?) -> Unit) {
        onRetryClick = listener
    }

    /**
     * 列表
     */
    fun itemUrls(required: Boolean = true): List<String> {
        val urlList = uploadFiles.map { it.verifyUrl(binding.title.text.invalid(), required) }

        // 过滤掉 null、空字符串、全是空白的内容
        val cleaned = urlList.filter { it.isNotBlank() }

        return if (required) {
            // required == true
            if (cleaned.isEmpty()) {
                throw IllegalArgumentException("请上传${binding.title.text}")
            }

            cleaned
        } else {
            // required == false 可为空
            cleaned
        }
    }
}