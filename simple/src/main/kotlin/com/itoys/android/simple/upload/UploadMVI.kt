package com.itoys.android.simple.upload

import com.itoys.android.core.mvi.AbsViewModel
import com.itoys.android.core.mvi.IUIIntent
import com.itoys.android.core.mvi.IUIState
import com.itoys.android.core.upload.TokenType
import com.itoys.android.logcat.logcat
import com.itoys.android.uikit.components.upload.UploadItem
import com.itoys.android.uikit.components.upload.UploadState
import com.itoys.android.uikit.components.upload.UploadType
import com.itoys.android.utils.expansion.invalid
import com.itoys.android.utils.expansion.isNull
import com.itoys.android.utils.expansion.launchOnIO
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/5/15
 */
sealed class UploadDemoIntent : IUIIntent {

    data class UploadFileItem(val mark: String, val item: UploadItem) : UploadDemoIntent()

    data class UploadFileItems(val mark: String, val items: List<UploadItem>) : UploadDemoIntent()

    data class UploadFile(val mark: String, val file: File?) : UploadDemoIntent()
}

sealed class UploadDemoState : IUIState {

    data object OnInitial : UploadDemoState()

    data class UpdateUploadState(
        val mark: String,
        val uploadId: String,
        val uploadState: UploadState
    ) : UploadDemoState()
}

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val upload: JJQNUpload,
) : AbsViewModel<UploadDemoIntent, UploadDemoState>() {

    override fun createUIState() = UploadDemoState.OnInitial

    override fun handlerIntent(intent: UploadDemoIntent) {
        when (intent) {
            is UploadDemoIntent.UploadFileItem -> uploadFile(intent)

            is UploadDemoIntent.UploadFileItems -> uploadFiles(intent)

            is UploadDemoIntent.UploadFile -> uploadFile(intent)
        }
    }

    private fun uploadFile(intent: UploadDemoIntent.UploadFileItem) {
        if (intent.item.type == UploadType.DOCUMENT) {
            uploadFile(UploadDemoIntent.UploadFile(intent.mark, intent.item.file))
            return
        }

        launchOnIO {
            uploadFile(intent.mark, intent.item.localPath, intent.item.id)
        }
    }

    private fun uploadFile(intent: UploadDemoIntent.UploadFile) {
        launchOnIO { uploadFile(intent.mark, intent.file) }
    }

    private fun uploadFiles(intent: UploadDemoIntent.UploadFileItems) {
        launchOnIO {
            intent.items.forEach { uploadItem ->
                if (uploadItem.type == UploadType.IMAGE) {
                    uploadFile(intent.mark, uploadItem.localPath, uploadItem.id)
                } else {
                    uploadFile(intent.mark, uploadItem.file, uploadItem.id)
                }
            }
        }
    }

    private suspend fun uploadFile(mark: String, path: String, id: String) {
        upload.uploadFile(
            filePath = path,
            category = Category.Reimbursement.description,
            tokenType = TokenType.Public,
            folder = "demo",
            progress = { progress ->
                sendUIState(
                    UploadDemoState.UpdateUploadState(
                        mark,
                        id,
                        UploadState.Progress(id, progress)
                    )
                )
            },
            success = {
                logcat { "上传文件成功: $mark -> $it" }
                sendUIState(
                    UploadDemoState.UpdateUploadState(
                        mark,
                        id,
                        UploadState.Success(id, it)
                    )
                )
            },
            handleEx = {
                logcat { "上传文件失败: $mark -> $it" }
                sendUIState(
                    UploadDemoState.UpdateUploadState(
                        mark,
                        id,
                        UploadState.Error(id, it.message.invalid("上传失败, 请重试"))
                    )
                )
            },
        )
    }

    private suspend fun uploadFile(mark: String, file: File?, id: String = "") {
        if (file.isNull()) {
            logcat { "上传文件失败: $mark -> 文件不能为空" }
            sendUIState(
                UploadDemoState.UpdateUploadState(
                    mark,
                    id,
                    UploadState.Error(id, "请重新选择文件")
                )
            )
            return
        }

        logcat { "上传文件: $mark -> ${file?.name}" }

        upload.uploadFile(
            file = file!!,
            category = Category.Reimbursement.description,
            tokenType = TokenType.Public,
            folder = "demo",
            progress = { progress ->
                sendUIState(
                    UploadDemoState.UpdateUploadState(
                        mark,
                        id,
                        UploadState.Progress(id, progress)
                    )
                )
            },
            success = {
                logcat { "上传文件成功: $mark -> $it" }
                sendUIState(
                    UploadDemoState.UpdateUploadState(
                        mark,
                        id,
                        UploadState.Success(id, it)
                    )
                )
            },
            handleEx = {
                logcat { "上传文件失败: $mark -> $it" }
                sendUIState(
                    UploadDemoState.UpdateUploadState(
                        mark,
                        id,
                        UploadState.Error(id, it.message.invalid("上传失败, 请重试"))
                    )
                )
            },
        )
    }
}