package com.peakmain.ui.loading

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnticipateInterpolator
import android.view.animation.LinearInterpolator
import com.peakmain.ui.R

/**
 * author ：Peakmain
 * createTime：2020/3/17
 * mail:2726449200@qq.com
 * describe：视察动画
 */
class InspectLoadingView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    // 旋转动画执行的时间
    private val ROTATION_ANIMATION_TIME: Long = 1400
    private var mInitParams = false
    private var mRotationRadius = 0f//绕着旋转的动画的半径 = 0f
    private var mCircleRadius=0f //小圆的半径 = 0f
    private var mCurrentRotationAngle = 0f

    // 小圆的颜色列表
    private var mCircleColors: IntArray

    //整体颜色背景
    private val mSplashColor = Color.WHITE

    // 代表当前状态所画动画
    private var mLoadingState: LoadingState? = null

    //画笔
    private var mPaint: Paint? = null
    private var cententX = 0
    private var cententY = 0
    private var mCurrentRotationRadius=0f //当前半径 = 0f

    // 空心圆初始半径
    private var mHoleRadius = 0f

    // 屏幕对角线的一半
    private var mDiagonalDist = 0f
    private var isShow = false
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isShow) {
            if (!mInitParams) {
                initParams()
            }
            if (mLoadingState == null) {
                mLoadingState = RotationState()
            }
            mLoadingState!!.drawable(canvas)
        }
    }

    fun show() {
        isShow = true
        invalidate()
    }

    //初始化参数
    private fun initParams() {
        mRotationRadius = measuredWidth / 4.toFloat()
        // 每个小圆的半径 = 大圆半径的 1/8
        mCircleRadius = mRotationRadius / 8
        mInitParams = true
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint!!.isDither = true
        cententX = width / 2
        cententY = height / 2
        mDiagonalDist = Math.sqrt(cententX * cententX + cententY * cententY.toDouble()).toFloat()
    }

    /**
     * 消失:给外部调用
     */
    fun hide() {
        //开始聚合动画
        //关闭动画
        if (mLoadingState is RotationState) {
            val rotationState = mLoadingState as RotationState
            rotationState.cancel()
        }
        mLoadingState = MergeState()
    }

    abstract inner class LoadingState {
        abstract fun drawable(canvas: Canvas)
    }

    /**
     * 展开动画
     */
    inner class ExpendState : LoadingState() {
        var mAnimator: ValueAnimator
        override fun drawable(canvas: Canvas) {
            //画笔的宽度
            val strokeWidth = mDiagonalDist - mHoleRadius
            mPaint!!.strokeWidth = strokeWidth
            mPaint!!.color = mSplashColor
            mPaint!!.style = Paint.Style.STROKE
            val radius = strokeWidth / 2 + mHoleRadius
            canvas.drawCircle(cententX.toFloat(), cententY.toFloat(), radius, mPaint)
        }

        init {
            mAnimator = ObjectAnimator.ofFloat(0f, mDiagonalDist) //从0到圆的对角线的一半
            mAnimator.duration = ROTATION_ANIMATION_TIME
            mAnimator.addUpdateListener { animation ->
                mHoleRadius = animation.animatedValue as Float
                invalidate()
            }
            mAnimator.start()
            mAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    animation?.cancel()
                }
            })
        }
    }

    /**
     * 聚合动画
     */
    inner class MergeState : LoadingState() {
        var mAnimator: ValueAnimator
        override fun drawable(canvas: Canvas) {
            //绘制白色背景
            canvas.drawColor(mSplashColor)
            //画6个圆
            //没份的角度
            val precentAngle = 2 * Math.PI / mCircleColors.size
            for (i in mCircleColors.indices) {
                mPaint!!.color = mCircleColors[i]
                //当前的角度=初始化的角度+旋转的角度
                val currentAngle = precentAngle * i + mCurrentRotationAngle
                val cx = (cententX + mCurrentRotationRadius * Math.cos(currentAngle)).toFloat()
                val cy = (cententY + mCurrentRotationRadius * Math.sin(currentAngle)).toFloat()
                canvas.drawCircle(cx, cy, mCircleRadius, mPaint)
            }
        }

        init {
            //从外圆的半径到中心位置
            mAnimator = ObjectAnimator.ofFloat(mRotationRadius, 0f)
            mAnimator.duration = ROTATION_ANIMATION_TIME / 2
            mAnimator.addUpdateListener { animation ->
                mCurrentRotationRadius = animation.animatedValue as Float
                invalidate()
            }
            // 开始的时候向后然后向前甩
            mAnimator.interpolator = AnticipateInterpolator(5f)
            mAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    animation?.cancel()
                    mLoadingState = ExpendState()
                }
            })
            //不断重复使用
            //mAnimator.setRepeatCount(-1);
            mAnimator.start()
        }
    }

    /**
     * 旋转动画
     */
    inner class RotationState : LoadingState() {
        var mAnimator: ValueAnimator
        override fun drawable(canvas: Canvas) {
            //绘制白色背景
            canvas.drawColor(mSplashColor)
            //画6个圆
            //没份的角度
            val precentAngle = 2 * Math.PI / mCircleColors.size
            for (i in mCircleColors.indices) {
                mPaint!!.color = mCircleColors[i]
                //当前的角度=初始化的角度+旋转的角度
                val currentAngle = precentAngle * i + mCurrentRotationAngle
                val cx = (cententX + mRotationRadius * Math.cos(currentAngle)).toFloat()
                val cy = (cententY + mRotationRadius * Math.sin(currentAngle)).toFloat()
                canvas.drawCircle(cx, cy, mCircleRadius, mPaint)
            }
        }

        fun cancel() {
            mAnimator.cancel()
        }

        init {
            //0-360度
            mAnimator = ObjectAnimator.ofFloat(0f, 2 * Math.PI.toFloat())
            mAnimator.duration = ROTATION_ANIMATION_TIME
            mAnimator.interpolator = LinearInterpolator()
            mAnimator.addUpdateListener { animation ->
                mCurrentRotationAngle = animation.animatedValue as Float
                invalidate()
            }
            //不断重复使用
            mAnimator.repeatCount = -1
            mAnimator.start()
        }
    }

    fun setCircleColors(circleColors: IntArray) {
        mCircleColors = circleColors
        invalidate()
    }

    init {
        mCircleColors = context.resources.getIntArray(R.array.inspect_circle_color)
    }
}