package com.itoys.android.core.crash

import android.content.Context
import android.widget.Toast
import com.itoys.android.core.network.ResultException
import com.itoys.android.core.network.handlerException
import com.itoys.android.utils.expansion.invalid
import com.itoys.android.utils.expansion.then

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/4/17
 */
class ToastCrashListener : OnCrashListener {

    override fun onCrash(context: Context?, crashHandler: ICrashHandler?, throwable: Throwable?): Boolean {
        if (context == null || crashHandler == null || throwable == null) return false

        val ex: ResultException =
            (throwable is ResultException).then({ throwable as ResultException },
                { throwable.handlerException() })

        //处理异常
        Toast.makeText(context, ex.message.invalid("程序出现异常, 请处理"), Toast.LENGTH_SHORT).show()

        return true
    }
}