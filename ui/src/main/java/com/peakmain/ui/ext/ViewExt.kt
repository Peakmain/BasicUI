package com.peakmain.ui.ext

import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener

/**
 * author ：Peakmain
 * createTime：2023/04/17
 * mail:2726449200@qq.com
 * describe：
 */
fun View.addOnGlobalLayoutListener(block: ((Int) -> Unit)? = null) {
    viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            block?.invoke(measuredHeight)
        }
    })
}