package com.itoys.android.scan

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.itoys.android.scan.qrcode.QRCodeScanActivity
import com.king.camera.scan.CameraScan

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/16
 */
class QRCodeResultContract : ActivityResultContract<Int, String?>() {
    override fun createIntent(context: Context, input: Int): Intent {
        return Intent(context, QRCodeScanActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        return if (resultCode == RESULT_OK) {
            intent?.extras?.getString(CameraScan.SCAN_RESULT)
        } else {
            ""
        }
    }
}