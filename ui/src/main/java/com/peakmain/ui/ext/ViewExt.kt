package com.peakmain.ui.ext

import android.view.View
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat

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

fun View.addOnPreDrawListener(block: ((Int) -> Unit)? = null) {
    viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            viewTreeObserver.removeOnPreDrawListener(this)
            block?.invoke(measuredHeight)
            return true
        }

    })
}

fun View.getTopLocationOnScreen(): Int {
    val location = IntArray(2)
    getLocationOnScreen(location)
    return location[1]
}

fun Group.setOnGroupClickListener(block: (() -> Unit)? = null) {
    val parentView = parent as? View
    val count = referencedIds.size
    for (i in 0 until count) {
        val id: Int = referencedIds[i]
        val view = parentView?.findViewById<View>(id)
        view?.setOnClickListener {
            block?.invoke()
        }
    }
}

fun TextView?.setTextColorCompat(@ColorRes id: Int) {
    this?.let {
        setTextColor(ContextCompat.getColor(context, id))
    }
}
fun TextView?.setBackgroundColorCompat(@ColorRes id: Int) {
    this?.let {
        setBackgroundColor(ContextCompat.getColor(context, id))
    }
}