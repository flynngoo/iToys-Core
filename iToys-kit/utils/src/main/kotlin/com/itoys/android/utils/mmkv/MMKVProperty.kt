package com.itoys.android.utils.mmkv

import com.tencent.mmkv.MMKV
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/15
 */
class MMKVProperty<V>(
    private val decode: MMKV.(String, V) -> V,
    private val encode: MMKV.(String, V) -> Boolean,
    private val defaultValue: V
) : ReadWriteProperty<MMKVOwner, V> {

    override fun getValue(thisRef: MMKVOwner, property: KProperty<*>): V {
        return thisRef.mmkv.decode(property.name, defaultValue)
    }

    override fun setValue(thisRef: MMKVOwner, property: KProperty<*>, value: V) {
        thisRef.mmkv.encode(property.name, value)
    }
}