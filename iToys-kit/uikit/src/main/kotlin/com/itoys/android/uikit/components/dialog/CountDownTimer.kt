package com.itoys.android.uikit.components.dialog

import android.os.CountDownTimer
import com.itoys.android.logcat.logcat

/**
 * @Author Flynn Gu
 * @Email gufanfan.worker@gmail.com
 * @Date 2025/5/15
 */
class CountDownTimer (
    private val dialog: AbsDialog<*>,
    private val searchKeywords: String,
    millisInFuture: Long,
    countDownInterval: Long = 1000L,
) : CountDownTimer(millisInFuture, countDownInterval) {

    override fun onTick(millisUntilFinished: Long) {
        logcat { "Dialog 倒计时: $millisUntilFinished" }
    }

    override fun onFinish() {
        dialog.countdownEnded(searchKeywords)
    }
}