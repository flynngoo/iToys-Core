package com.itoys.android.utils.expansion

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/21
 */
fun Activity.actOpen(kClass: KClass<out Any>) {
    kClass.actOpen(this)
}

fun Activity.actOpen(kClass: KClass<out Any>, vararg extras: Pair<String, Any>) {
    val bundle = Bundle()
    extras.forEach { extra ->
        bundle.put(extra)
    }
    kClass.java.actOpen(this, bundle = bundle)
}

fun Fragment.actOpen(kClass: KClass<out Any>) {
    kClass.actOpen(requireContext())
}

fun Fragment.actOpen(kClass: KClass<out Any>, vararg extras: Pair<String, Any>) {
    val bundle = Bundle()
    extras.forEach { extra ->
        bundle.put(extra)
    }
    kClass.java.actOpen(requireContext(), bundle = bundle)
}

fun KClass<out Any>.actOpen(context: Context) {
    this.java.actOpen(context, bundle = Bundle())
}

fun KClass<out Any>.actOpen(context: Context, vararg extras: Pair<String, Any>) {
    val bundle = Bundle()
    extras.forEach { extra ->
        bundle.put(extra)
    }
    this.java.actOpen(context, bundle = bundle)
}

fun Class<out Any>.actOpen(context: Context) {
    actOpen(context, bundle = Bundle())
}

fun Class<out Any>.actOpen(context: Context, bundle: Bundle) {
    val intent = Intent(context, this)
    intent.putExtras(bundle)
    context.startActivity(intent)
}

/**
 * 在IO线程中启动
 */
infix fun Any.launchOnIO(block: suspend CoroutineScope.() -> Unit) {
    CoroutineScope(Dispatchers.IO).launch { block() }
}

val Activity.tagName: String
    get() = this.javaClass.tagName

val Activity.className: String
    get() = this.javaClass.name

val Fragment.tagName: String
    get() = this.javaClass.tagName

val Fragment.className: String
    get() = this.javaClass.name