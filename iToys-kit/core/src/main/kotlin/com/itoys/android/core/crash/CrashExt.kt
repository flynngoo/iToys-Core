package com.itoys.android.core.crash

import com.itoys.android.core.network.ResultException
import com.itoys.android.core.network.handlerException
import com.itoys.android.uikit.components.toast.toast
import com.itoys.android.utils.expansion.invalid
import com.itoys.android.utils.expansion.then

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/4/17
 */
fun catchCrash(block: () -> Unit) {
    try {
        block()
    } catch (throwable: Throwable) {
        val ex: ResultException =
            (throwable is ResultException).then({ throwable as ResultException },
                { throwable.handlerException() })
        toast(ex.message.invalid("程序出现异常, 请查看日志并处理"))
    }
}