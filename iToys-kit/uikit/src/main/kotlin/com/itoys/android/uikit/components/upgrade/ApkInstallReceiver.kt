package com.itoys.android.uikit.components.upgrade

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.itoys.android.utils.SysUtils
import com.itoys.android.utils.expansion.isBlank
import com.itoys.android.utils.expansion.stringExtra
import com.itoys.android.utils.expansion.toFile

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/7/5
 */
class ApkInstallReceiver : BroadcastReceiver() {

    companion object {

        /** 广播 action */
        private const val ACTION_INSTALL = "com.itoys.android.INSTALL_APK"

        /** Intent args 文件路径 */
        private const val ARGS_APK_PATH = "args_apk_path"

        /**
         * 注册本地广播
         */
        fun registerReceiver(context: Context) {
            val intentFilter = IntentFilter(ACTION_INSTALL)
            val installReceiver = ApkInstallReceiver()
            LocalBroadcastManager.getInstance(context).registerReceiver(installReceiver, intentFilter)
        }

        /**
         * 发送本地广播
         */
        fun sendBroadcast(context: Context, apkPath: String) {
            val intent = Intent(ACTION_INSTALL)
            intent.putExtra(ARGS_APK_PATH, apkPath)
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        installApk(context, intent?.extras.stringExtra(ARGS_APK_PATH))
    }

    /**
     * 安装 apk
     */
    private fun installApk(context: Context?, apkPath: String) {
        if (apkPath.isBlank()) return
        context?.let { SysUtils.installApp(context, apkPath.toFile()) }
    }
}