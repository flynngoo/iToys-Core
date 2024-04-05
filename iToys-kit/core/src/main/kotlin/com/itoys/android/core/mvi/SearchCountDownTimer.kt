package com.itoys.android.core.mvi

import android.os.CountDownTimer
import com.itoys.android.logcat.logcat

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/14
 */
class SearchCountDownTimer(
    private val viewModel: AbsListViewModel<*, *>,
    private val searchKeywords: String,
    millisInFuture: Long,
    countDownInterval: Long,
) : CountDownTimer(millisInFuture, countDownInterval) {

    override fun onTick(millisUntilFinished: Long) {
        logcat { "搜索倒计时: $millisUntilFinished" }
    }

    override fun onFinish() {
        viewModel.countdownEnded(searchKeywords)
    }
}