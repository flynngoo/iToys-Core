package com.itoys.android.scan.qrcode

import android.content.Intent
import android.os.Bundle
import com.google.zxing.Result
import com.hjq.bar.OnTitleBarListener
import com.hjq.bar.TitleBar
import com.itoys.android.scan.R
import com.itoys.android.uikit.initBar
import com.king.camera.scan.AnalyzeResult
import com.king.camera.scan.CameraScan
import com.king.camera.scan.analyze.Analyzer
import com.king.zxing.BarcodeCameraScanActivity
import com.king.zxing.DecodeConfig
import com.king.zxing.DecodeFormatManager
import com.king.zxing.analyze.QRCodeAnalyzer

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/16
 */
class QRCodeScanActivity : BarcodeCameraScanActivity() {

    /**
     * 标题栏
     */
    protected open var titleBar: TitleBar? = null

    /**
     * 标题栏点击时间
     */
    protected open var titleBarListener: OnTitleBarListener = object : OnTitleBarListener {
        override fun onLeftClick(titleBar: TitleBar?) {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBar(
            statusBarColor = R.color.uikit_colorful_transparent,
            fitsSystemWindows = false,
            statusBarDarkFont = false,
        )

        titleBar = findViewById(R.id.title_bar)
        titleBar?.setOnTitleBarListener(titleBarListener)
    }

    override fun getLayoutId(): Int = R.layout.scanning_activity_qrcode

    override fun createAnalyzer(): Analyzer<Result> {
        val decodeConfig = DecodeConfig()
        decodeConfig.hints = DecodeFormatManager.QR_CODE_HINTS
        decodeConfig.isFullAreaScan = true
        return QRCodeAnalyzer(decodeConfig)
    }

    override fun onScanResultCallback(result: AnalyzeResult<Result>) {
        cameraScan?.setAnalyzeImage(false)
        val resultIntent = Intent()
        resultIntent.putExtra(CameraScan.SCAN_RESULT, result.result.text)
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}