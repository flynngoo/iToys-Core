package com.itoys.android.uikit.components.upgrade

import android.view.LayoutInflater
import com.itoys.android.logcat.logcat
import com.itoys.android.uikit.R
import com.itoys.android.uikit.components.dialog.AbsDialog
import com.itoys.android.uikit.components.dialog.IToysDialog
import com.itoys.android.uikit.components.toast.toast
import com.itoys.android.uikit.databinding.UikitDialogUpgradeBinding
import com.itoys.android.utils.PathUtils
import com.itoys.android.utils.expansion.doOnClick
import com.itoys.android.utils.expansion.gone
import com.itoys.android.utils.expansion.invalid
import com.itoys.android.utils.expansion.launchOnIO
import com.itoys.android.utils.expansion.md5
import com.itoys.android.utils.expansion.then
import com.itoys.android.utils.expansion.toFile
import com.itoys.android.utils.expansion.visible
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.core.cause.EndCause
import com.liulishuo.okdownload.core.listener.DownloadListener4WithSpeed
import com.liulishuo.okdownload.kotlin.enqueue4WithSpeed
import com.liulishuo.okdownload.kotlin.listener.createListener4WithSpeed
import java.io.File

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/7/5
 */
class UpgradeDialog : IToysDialog<UikitDialogUpgradeBinding, UpgradeDialog.Builder>() {

    companion object {
        /**
         * 显示对话框
         */
        fun show(builder: Builder.() -> Unit) {
            val dialog = UpgradeDialog()
            dialog.builder = Builder.create().apply(builder)
            dialog.showDialog()
        }
    }

    class Builder : AbsDialog.Builder() {

        companion object {

            /**
             * 创建 builder
             */
            fun create(): Builder = Builder()
        }

        /** 升级信息 */
        var upgradeData: UpgradeModel? = null
    }

    /**
     * Dialog builder
     */
    private lateinit var builder: Builder

    override fun builder() = builder

    /**
     * 下载总长度
     */
    private var totalLength: Long = 0

    override fun createViewBinding(inflater: LayoutInflater) = UikitDialogUpgradeBinding.inflate(layoutInflater)

    override fun initialize() {
        binding?.apply {
            // check 是否强制更新
            isCancelable = builder.upgradeData?.isIgnorable == false
            (isCancelable).then(
                { cancelUpgrade.visible() },
                { cancelUpgrade.gone() }
            )

            // 升级版本
            upgradeVersion.text = getString(R.string.uikit_upgrade_version, builder.upgradeData?.versionName.invalid())
            // 升级内容
            upgradeContent.text = builder.upgradeData?.upgradeLog.invalid()
            // 升级包大小
            upgradeApkSize.text = getString(R.string.uikit_upgrade_apk_size, builder.upgradeData?.apkSize.invalid())
            // 取消升级
            cancelUpgrade.doOnClick { dismiss() }
            // 升级
            upgradeNow.doOnClick { upgradeNow() }
        }
    }

    /**
     * 升级
     */
    private fun upgradeNow() {
        val downloadPath = "${PathUtils.getInternalAppFilesPath()}/${builder.upgradeData.filename()}"
        val apkFile = downloadPath.toFile()

        when {
            apkFile.exists() -> installApk(apkFile)

            else -> downloadUpgradeApk()
        }
    }

    /**
     * 下载升级 apk
     */
    private fun downloadUpgradeApk() {
        binding?.upgradeNow?.gone()
        binding?.cancelUpgrade?.gone()
        binding?.downloadProgress?.visible()
        binding?.downloadProgress?.progress = 0

        // 开始下载
        DownloadTask.Builder(
            builder.upgradeData?.apkUrl.invalid(),
            PathUtils.getInternalAppFilesPath().toFile()
        )
            .setFilename(builder.upgradeData.filename())
            // the minimal interval millisecond for callback progress
            .setMinIntervalMillisCallbackProcess(30)
            // do re-download even if the task has already been completed in the past.
            .setPassIfAlreadyCompleted(false)
            .setConnectionCount(1)
            .build().enqueue4WithSpeed(
                onInfoReadyWithSpeed = { _, info, _, _ ->
                    logcat { "Upgrade infoReady size: ${info.totalLength}." }
                    totalLength = info.totalLength
                },
                onProgressWithSpeed = { _, currentOffset, taskSpeed ->
                    logcat { "Upgrade download speed: ${taskSpeed.averageSpeed()}." }
                    binding?.downloadProgress?.progress = ((currentOffset * 1.0f / totalLength) * 100).toInt()
                },
            ) { task, cause, _, _ ->
                logcat { "Upgrade download taskEnd -> $cause." }

                if (cause == EndCause.COMPLETED) {
                    task.file?.let { installApk(it) }
                } else {
                    toast("更新包下载失败，请重新下载!")

                    binding?.upgradeNow?.visible()
                    binding?.downloadProgress?.gone()
                    (isCancelable).then(
                        { binding?.cancelUpgrade?.visible() },
                        { binding?.cancelUpgrade?.gone() }
                    )
                }
            }
    }

    /**
     * 安装 apk
     */
    private fun installApk(apk: File) {
        launchOnIO {
            val apkMd5 = apk.md5()

            if (builder.upgradeData?.md5.equals(apkMd5, ignoreCase = true)) {
                // md5 一致 发送安装广播
                // 关闭弹窗
                binding?.root?.post {
                    dismiss()

                    ApkInstallReceiver.sendBroadcast(
                        context = requireContext().applicationContext,
                        apk.absolutePath
                    )
                }
            } else {
                // md5 不一致 通知用户重新下载
                apk.delete()

                binding?.root?.post {
                    toast("MD5不一致，请重新下载!")
                    binding?.upgradeNow?.visible()
                    binding?.downloadProgress?.gone()
                    (isCancelable).then(
                        { binding?.cancelUpgrade?.visible() },
                        { binding?.cancelUpgrade?.gone() }
                    )
                }
            }
        }
    }
}