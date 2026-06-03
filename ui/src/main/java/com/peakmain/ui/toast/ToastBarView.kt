package com.peakmain.ui.toast

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.peakmain.ui.R
import com.peakmain.ui.listener.SimpleAnimationListener
import com.peakmain.ui.utils.SizeUtils.getStatusBarHeight
import com.peakmain.ui.widget.ShapeLinearLayout

internal class ToastBarView : LinearLayout {

    private var slideOutAnimation: Animation? = null
    private var containerView: ShapeLinearLayout? = null
    private var tvMessage: TextView? = null
    private var duration: Long = 2000
    private var animationIn: Int = 0
    private var animationOut: Int = 0

    private var statusBarHeightApplied = false

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    )

    private fun initViews(
        @LayoutRes rootView: Int,
        viewInitializer: ToastBar.CustomViewInitializer?
    ) {
        val layoutRes = if (rootView != 0) rootView else R.layout.layout_toast_tips
        inflate(context, layoutRes, this)
        viewInitializer?.initView(getChildAt(0))

        containerView = findViewById(R.id.toast_container)
        tvMessage = findViewById(R.id.tv_message)

        if (containerView == null || tvMessage == null) {
            throw RuntimeException("Your custom toast view is missing required views")
        }
    }

    fun setParams(params: ToastBar.Params) {
        initViews(params.customViewResource, params.viewInitializer)

        duration = params.duration
        animationIn = params.animationIn
        animationOut = params.animationOut

        if (params.backgroundColor != 0) {
            containerView?.apply {
                setNormalBackgroundColor(ContextCompat.getColor(context, params.backgroundColor))
            }
        }

        // 添加状态栏高度到顶部内边距
        containerView?.setPadding(
            containerView!!.paddingLeft,
            containerView!!.paddingTop + getStatusBarHeight(),
            containerView!!.paddingRight,
            containerView!!.paddingBottom
        )

        createInAnim()
        createOutAnim()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, 0, r, containerView!!.measuredHeight)
    }

    private fun createInAnim() {
        val slideInAnimation = AnimationUtils.loadAnimation(context, animationIn)
        slideInAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                postDelayed({ dismiss() }, duration)
            }
            override fun onAnimationRepeat(animation: Animation) {}
        })
        setAnimation(slideInAnimation)
    }

    private fun createOutAnim() {
        slideOutAnimation = AnimationUtils.loadAnimation(context, animationOut)
    }

    fun dismiss() {
        dismiss(null)
    }

    fun dismiss(onDismiss: (() -> Unit)?) {
        slideOutAnimation!!.setAnimationListener(object : SimpleAnimationListener() {
            override fun onAnimationEnd(animation: Animation?) {
                onDismiss?.invoke()
                visibility = View.GONE
                removeFromParent()
            }
        })
        startAnimation(slideOutAnimation)
    }

    private fun removeFromParent() {
        postDelayed({
            val parent: ViewParent? = parent
            if (parent != null) {
                this@ToastBarView.clearAnimation()
                (parent as ViewGroup).removeView(this@ToastBarView)
            }
        }, 200)
    }
}