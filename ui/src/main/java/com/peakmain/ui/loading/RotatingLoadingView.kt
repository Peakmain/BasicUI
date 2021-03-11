package com.peakmain.ui.loading

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.DialogInterface
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.peakmain.ui.R
import com.peakmain.ui.dialog.AlertDialog
import com.peakmain.ui.utils.SizeUtils.Companion.screenHeight
import com.peakmain.ui.utils.SizeUtils.Companion.screenWidth

/**
 * author ：Peakmain
 * createTime：2020/3/17
 * mail:2726449200@qq.com
 * describe：视察动画
 */
class RotatingLoadingView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    // 旋转动画执行的时间
    private val ROTATION_ANIMATION_TIME: Long = 1400
    private var mInitParams = false
    private var mRotationRadius=0f //绕着旋转的动画的半径 = 0f
    private var mCircleRadius=0f //小圆的半径 = 0f
    private var mCurrentRotationAngle = 0f

    // 小圆的颜色列表
    private var mCircleColors: IntArray

    //整体颜色背景
    private val mSplashColor = Color.WHITE

    // 代表当前状态所画动画
    private var mLoadingState: RotationState? = null

    //画笔
    private var mPaint: Paint? = null
    private var cententX = 0
    private var cententY = 0
    private var mDialog: AlertDialog? = null
    private var mBuilder: AlertDialog.Builder? = null
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!mInitParams) {
            initParams()
        }
        if (mLoadingState == null) {
            mLoadingState = RotationState()
        }
        mLoadingState!!.drawable(canvas)
    }

    fun show() {
        if (mBuilder == null) {
            mBuilder = AlertDialog.Builder(context)
                    .setContentView(this)
                    .setOnCancelListener(DialogInterface.OnCancelListener { visibility = INVISIBLE })
                    .setCancelable(false)
                    .setWidthAndHeight(screenWidth * 2 / 3, screenHeight / 3)
        }
        mDialog = mBuilder!!.show()
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
    }

    /**
     * 消失:给外部调用
     */
    fun hide() {
        //开始聚合动画
        //关闭动画
        if (mDialog != null) {
            mLoadingState!!.cancel()
            mDialog!!.dismiss()
        }
        visibility = INVISIBLE
    }

    abstract inner class LoadingState {
        abstract fun drawable(canvas: Canvas)
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
    }

    init {
        mCircleColors = context.resources.getIntArray(R.array.rotation_loading_view)
    }
}