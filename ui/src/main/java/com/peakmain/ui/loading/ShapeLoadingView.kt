package com.peakmain.ui.loading

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import com.peakmain.ui.R
import com.peakmain.ui.dialog.AlertDialog
import com.peakmain.ui.utils.SizeUtils.Companion.dp2px
import com.peakmain.ui.utils.SizeUtils.Companion.screenHeight
import com.peakmain.ui.utils.SizeUtils.Companion.screenWidth

/**
 * author ：Peakmain
 * createTime：2020/3/17
 * mail:2726449200@qq.com
 * describe：
 */
class ShapeLoadingView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {
    // 上面的形状
    private var mShapeView: ShapeView? = null

    // 中间的阴影
    private var mShadowView: View? = null
    private var mTranslationDistance = 0

    // 动画执行的时间
    private val ANIMATOR_DURATION: Long = 350

    // 是否停止动画
    private var mIsStopAnimator = false
    private var mDialog: AlertDialog? = null
    private var mBuilder: AlertDialog.Builder? = null
    private var mTvShapeName: TextView? = null

    /**
     * 初始化加载布局
     */
    private fun initLayout() {
        // 1. 记载写好的 ui_loading_view
        // 1.1 实例化View
        // View loadView = inflate(getContext(),R.layout.ui_loading_view,null);
        // 1.2 添加到该View
        // addView(loadView);
        // 找一下 插件式换肤资源加载的那一节的内容
        // this 代表把 ui_loading_view 加载到 LoadingView 中
        View.inflate(context, R.layout.ui_shape_view, this)
        mShapeView = findViewById(R.id.shape_view)
        mShadowView = findViewById(R.id.shadow_view)
        mTvShapeName = findViewById(R.id.tv_shape_name)
        post { // onResume 之后View绘制流程执行完毕之后（View的绘制流程源码分析那一章）
            startFallAnimator()
        }
        // onCreate() 方法中执行 ，布局文件解析 反射创建实例
    }

    // 开始下落动画
    private fun startFallAnimator() {
        if (mIsStopAnimator) {
            return
        }
        // 动画作用在谁的身上
        // 下落位移动画
        val translationAnimator = ObjectAnimator.ofFloat(mShapeView, context.getString(R.string.translationY), 0f, mTranslationDistance.toFloat())
        // 配合中间阴影缩小
        val scaleAnimator = ObjectAnimator.ofFloat(mShadowView, context.getString(R.string.scaleX), 1f, 0.3f)
        val animatorSet = AnimatorSet()
        animatorSet.duration = ANIMATOR_DURATION
        // 下落的速度应该是越来越快
        animatorSet.interpolator = AccelerateInterpolator()
        animatorSet.playTogether(translationAnimator, scaleAnimator)
        animatorSet.start()
        // 下落完之后就上抛了，监听动画执行完毕
        // 是一种思想，在 Adapter 中的 BannerView 写过
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                // 改变形状
                mShapeView!!.exchange()
                // 下落完之后就上抛了
                startUpAnimator()
                // 开始旋转
            }
        })
    }

    /**
     * 开始执行上抛动画
     */
    private fun startUpAnimator() {
        if (mIsStopAnimator) {
            return
        }
        // 动画作用在谁的身上
        // 下落位移动画
        val translationAnimator = ObjectAnimator.ofFloat(mShapeView, context.getString(R.string.translationY), mTranslationDistance.toFloat(), 0f)
        // 配合中间阴影缩小
        val scaleAnimator = ObjectAnimator.ofFloat(mShadowView, context.getString(R.string.scaleX), 0.3f, 1f)
        val animatorSet = AnimatorSet()
        animatorSet.duration = ANIMATOR_DURATION
        animatorSet.interpolator = DecelerateInterpolator()
        animatorSet.playTogether(translationAnimator, scaleAnimator)
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                // 上抛完之后就下落了
                startFallAnimator()
            }

            override fun onAnimationStart(animation: Animator) {
                // 开始旋转
                startRotationAnimator()
            }
        })
        // 执行 -> 监听的 onAnimationStart 方法
        animatorSet.start()
    }

    /**
     * 上抛的时候需要旋转
     */
    private fun startRotationAnimator() {
        var rotationAnimator: ObjectAnimator? = null
        when (mShapeView!!.currentShape) {
            ShapeView.Shape.Circle, ShapeView.Shape.Square ->                 // 180
                rotationAnimator = ObjectAnimator.ofFloat(mShapeView, context.getString(R.string.rotation), 0f, 180f)
            ShapeView.Shape.Triangle ->                 // 120
                rotationAnimator = ObjectAnimator.ofFloat(mShapeView, context.getString(R.string.rotation), 0f, -120f)
            else -> {
            }
        }
        rotationAnimator!!.duration = ANIMATOR_DURATION
        rotationAnimator.interpolator = DecelerateInterpolator()
        rotationAnimator.start()
    }

    /**
     * 显示loading
     */
    fun show() {
        if (mBuilder == null) {
            mBuilder = AlertDialog.Builder(context)
                    .setContentView(this)
                    .setOnCancelListener(DialogInterface.OnCancelListener { visibility = View.INVISIBLE })
                    .setCancelable(false)
                    .setWidthAndHeight(screenWidth * 2 / 3, screenHeight / 3)
        }
        mDialog = mBuilder!!.show()
    }

    /**
     * 隐藏loading
     */
    fun hide() {
        if (mDialog != null) {
            mDialog!!.dismiss()
        }
        visibility = View.INVISIBLE
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(View.INVISIBLE) // 不要再去排放和计算，少走一些系统的源码（View的绘制流程）
        // 清理动画
        mShapeView!!.clearAnimation()
        mShadowView!!.clearAnimation()
        // 把LoadingView从父布局移除
        val parent = parent as ViewGroup?
        if (parent != null) {
            parent.removeView(this) // 从父布局移除
            removeAllViews() // 移除自己所有的View
        }
        mIsStopAnimator = true
    }

    fun setLoadingName(name: CharSequence?) {
        mTvShapeName!!.text = name
        invalidate()
    }

    init {
        mTranslationDistance = dp2px(80f)
        initLayout()
    }
}