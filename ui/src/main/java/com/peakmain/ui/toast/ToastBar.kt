package com.peakmain.ui.toast

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.peakmain.ui.R

open class ToastBar private constructor(
    private val context: Activity,
    params: Params?
) {
    companion object {
        fun build(activity: Activity): Builder {
            return Builder(activity)
        }

        fun dismiss(activity: Activity) {
            ToastBar(activity, null)
        }
    }

    private var barView: ToastBarView? = null

    init {
        if (params == null) {
            dismiss()
        } else {
            barView = ToastBarView(context).apply {
                setParams(params)
            }
        }
    }

    private fun show() {
        barView?.let { bar ->
            val decorView = context.window.decorView as ViewGroup
            if (bar.parent == null) {
                addBar(decorView, bar)
            }
        }
    }

    private fun dismiss() {
        val decorView = context.window.decorView as ViewGroup
        removeFromParent(decorView)
    }

    private fun removeFromParent(parent: ViewGroup) {
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            if (child is ToastBarView) {
                child.dismiss()
                return
            }
        }
    }

    private fun addBar(parent: ViewGroup, bar: ToastBarView) {
        if (bar.parent != null) {
            return
        }

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            if (child is ToastBarView) {
                child.dismiss { parent.addView(bar) }
                return
            }
        }

        parent.addView(bar)
    }

    open class Builder(internal val context: Activity) {

        internal val params = Params()

        fun setMessage(message: String): Builder {
            params.message = message
            return this
        }

        fun setDuration(duration: Long): Builder {
            params.duration = duration
            return this
        }

        fun setBackground(background: Int): Builder {
            params.backgroundColor = background
            return this
        }

        fun setCustomView(@LayoutRes customView: Int): Builder {
            params.customViewResource = customView
            return this
        }

        fun setCustomViewInitializer(viewInitializer: CustomViewInitializer): Builder {
            params.viewInitializer = viewInitializer
            return this
        }

        fun show(): ToastBar {
            val bar = ToastBar(context, params)
            bar.show()
            return bar
        }
    }

    internal class Params {
        var message: String? = null
        var backgroundColor: Int = 0
        var duration: Long = 2000
        var customViewResource: Int = 0
        var animationIn: Int = R.anim.slide_in_from_top
        var animationOut: Int = R.anim.slide_out_to_top
        var viewInitializer: CustomViewInitializer? = null
    }

    fun interface CustomViewInitializer {
        fun initView(view: View)
    }
}