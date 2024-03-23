package com.itoys.android.utils.expansion

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/3/23
 */

fun <T: Any, F: Flow<T>> LifecycleOwner.collect(flow: F, block: (T?) -> Unit) {
    lifecycleScope.launch {
        flow.collect { block(it) }
    }
}