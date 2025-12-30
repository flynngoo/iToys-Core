package com.itoys.android.simple.upload

import android.R.attr.required
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.children
import com.itoys.android.core.activity.AbsMviActivity
import com.itoys.android.core.crash.catchCrash
import com.itoys.android.databinding.SimpleActivityUploadBinding
import com.itoys.android.uikit.components.toast.toast
import com.itoys.android.uikit.components.upload.OnUploadCallback
import com.itoys.android.uikit.components.upload.UploadGroupView
import com.itoys.android.uikit.components.upload.UploadHelper
import com.itoys.android.uikit.components.upload.UploadItem
import com.itoys.android.uikit.components.upload.UploadItemView
import com.itoys.android.uikit.components.upload.UploadListView
import com.itoys.android.uikit.components.upload.UploadType
import com.itoys.android.uikit.components.upload.UploadWithHeaderView
import com.itoys.android.uikit.components.upload.toFileItemList
import com.itoys.android.uikit.components.upload.toImageItemList
import com.itoys.android.uikit.components.upload.toImageUrlItemList
import com.itoys.android.utils.expansion.collect
import com.itoys.android.utils.expansion.doOnClick
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/5/15
 */
@AndroidEntryPoint
class UploadActivity : AbsMviActivity<SimpleActivityUploadBinding, UploadViewModel>() {

    private val uploadHelper = UploadHelper(this, object : OnUploadCallback {

        override fun onFileSelected(mark: String, item: UploadItem) {
            super.onFileSelected(mark, item)

            binding?.uploadContainer?.post {
                binding?.uploadContainer?.children?.forEach { view ->
                    when {
                        view is UploadItemView && view.uploadMark == mark -> {
                            view.loadPreview(item)

                            // 上传
                            viewModel?.sendIntent(UploadDemoIntent.UploadFileItem(mark, item))
                            return@forEach
                        }

                        view is UploadListView && view.uploadMark == mark -> {
                            view.addUploadItems(listOf(item))

                            viewModel?.sendIntent(
                                UploadDemoIntent.UploadFileItems(
                                    mark,
                                    listOf(item)
                                )
                            )
                            return@forEach
                        }

                        view is UploadWithHeaderView && view.uploadMark == mark -> {
                            view.uploadItemView().loadPreview(item)

                            // 上传
                            viewModel?.sendIntent(UploadDemoIntent.UploadFileItem(mark, item))
                            return@forEach
                        }

                        view is UploadGroupView && view.uploadMark == mark -> {
                            view.loadPreview(item)

                            // 上传
                            viewModel?.sendIntent(UploadDemoIntent.UploadFileItem(mark, item))
                            return@forEach
                        }
                    }
                }
            }
        }

        override fun onFileSelected(mark: String, items: List<UploadItem>) {
            super.onFileSelected(mark, items)
            binding?.uploadContainer?.post {
                binding?.uploadContainer?.children?.forEach { view ->
                    when {
                        view is UploadListView && view.uploadMark == mark -> {
                            view.addUploadItems(items)
                            viewModel?.sendIntent(UploadDemoIntent.UploadFileItems(mark, items))
                            return@forEach
                        }
                    }
                }
            }
        }

        override fun onSelectedFailed(mark: String, message: String) {
            super.onSelectedFailed(mark, message)
            toast(message)
        }

        override fun onFileDeselected(mark: String, position: Int) {
            super.onFileDeselected(mark, position)
            binding?.uploadContainer?.post {
                binding?.uploadContainer?.children?.forEach { view ->
                    when {
                        view is UploadItemView && view.uploadMark == mark -> {
                            view.removeItem()
                            return@forEach
                        }

                        view is UploadListView && view.uploadMark == mark -> {
                            view.removeItem(position)
                            return@forEach
                        }

                        view is UploadWithHeaderView && view.uploadMark == mark -> {
                            view.uploadItemView().removeItem()
                            return@forEach
                        }
                    }
                }
            }
        }
    })

    private val onRetryClick: ((String, UploadItem?) -> Unit) = { mark, item ->
        item?.let {
            viewModel?.sendIntent(UploadDemoIntent.UploadFileItem(mark, item))
        }
    }

    override val viewModel: UploadViewModel? by viewModels()

    override fun createViewBinding() = SimpleActivityUploadBinding.inflate(layoutInflater)

    override fun initialize(savedInstanceState: Bundle?) {
        setupUploadList()

        setupUploadGroup()
    }

    private fun setupUploadList() {
        binding?.uploadList?.apply {
            // showPlus = false
            // setUploadType(UploadType.DOCUMENT)
            setUploadHelper(uploadHelper)

            setOnRetryClickListener { mark, item ->
                item?.let {
                    viewModel?.sendIntent(UploadDemoIntent.UploadFileItems(mark, listOf(item)))
                }
            }

            // addUploadItems(
            //     listOf(
            //         "http://img.bjjjst.com/2025/11/22/reimbursement/travel/1763797566952-1763797566542270.jpeg?e=1763801564&token=rw2JtvaJOTSTapsGwUuduGjjEZfNRVWlUsJ25Q07:iRDYQp3Vq6yoqYwNbveC7RuDt3Q=",
            //         "http://img.bjjjst.com/2025/11/22/reimbursement/travel/1763797566955-1763797566620340.png?e=1763801564&token=rw2JtvaJOTSTapsGwUuduGjjEZfNRVWlUsJ25Q07:GMhnrVKhbZnQ-fBgPSWWgeMdZlw="
            //     ).toImageUrlItemList(isCanDelete = false)
            // )
        }
    }

    private fun setupUploadGroup() {
        binding?.uploadGroup?.apply {
            uploadMark = "mark4"
            setTitle("Mark4")
            setOptionalTitle("(Optional)")
            setSubtitle("Mark4 subtitle")
            setUploadHelper(uploadHelper)

            setOnRetryClickListener(onRetryClick)
        }
    }

    override fun addClickListen() {
        super.addClickListen()
        binding?.uploadItem?.apply {
            // setUploadType(UploadType.DOCUMENT)

            setUploadHelper(uploadHelper)

            setOnRetryClickListener(onRetryClick)
        }

        binding?.upload1?.apply {
            setUploadHelper(uploadHelper)
            // setUploadType(UploadType.DOCUMENT)

            setOnRetryClickListener(onRetryClick)
        }

        binding?.vin?.apply {
            setUploadHelper(uploadHelper)
            // setUploadType(UploadType.DOCUMENT)

            setOnRetryClickListener(onRetryClick)
        }

        binding?.frontLeft?.apply {
            setUploadHelper(uploadHelper)
            // setUploadType(UploadType.DOCUMENT)

            setOnRetryClickListener(onRetryClick)
        }

        binding?.upload?.doOnClick {
            catchCrash {
                binding?.uploadList?.itemUrls(required = false)
            }
        }
    }

    override fun addObserver() {
        super.addObserver()
        viewModel?.apply { collect(uiState, ::uiCollect) }
    }

    private fun uiCollect(state: UploadDemoState?) {
        when (state) {
            is UploadDemoState.UpdateUploadState -> {
                binding?.uploadContainer?.children?.forEach { view ->
                    if (view is UploadItemView && view.uploadMark == state.mark) {
                        view.updateStatusUI(state.uploadState)
                        return@forEach
                    }

                    when {
                        view is UploadItemView && view.uploadMark == state.mark -> {
                            view.updateStatusUI(state.uploadState)
                            return@forEach
                        }

                        view is UploadWithHeaderView && view.uploadMark == state.mark -> {
                            view.uploadItemView().updateStatusUI(state.uploadState)
                            return@forEach
                        }

                        view is UploadGroupView && view.uploadMark == state.mark -> {
                            view.updateStatusUI(state.uploadId, state.uploadState)
                            return@forEach
                        }
                    }
                }
            }

            else -> {}
        }
    }
}