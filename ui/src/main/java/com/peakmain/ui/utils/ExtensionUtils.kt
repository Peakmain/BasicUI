package com.peakmain.ui.utils

import android.util.SparseArray

/**
 * author ：Peakmain
 * createTime：2021/4/28
 * mail:2726449200@qq.com
 * describe：kotlin扩展类
 */
inline operator fun <T> SparseArray<T>.set(key: Int, value: T) = put(key, value)

inline fun <T> SparseArray<T>.containsKey(key: Int) = indexOfKey(key) >= 0
inline fun <T> SparseArray<T>.forEach(action: (key: Int, value: T) -> Unit) {
    for (index in 0 until size()) {
        action(keyAt(index), valueAt(index))
    }
}