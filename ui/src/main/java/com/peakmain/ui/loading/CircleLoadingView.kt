package com.peakmain.ui.loading

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.RelativeLayout
import com.peakmain.ui.dialog.AlertDialog
import com.peakmain.ui.utils.LogUtils
import com.peakmain.ui.utils.SizeUtils.dp2px
import com.peakmain.ui.utils.SizeUtils.screenHeight
import com.peakmain.ui.utils.SizeUtils.screenWidth

/**
 * author ：Peakmain
 * createTime：2020/3/4
 * mail:2726449200@qq.com
 * describe：圆形加载的loading
 */
class CircleLoadingView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    //三个view
    private val mLeftView: CircleView
    private val mMiddleView: CircleView
    private val mRightView: CircleView

    //距离
    private var mTranslationDistance = 30

    //时间
    private val ANIMATION_DRUATION: Long = 350

    //默认false，只有销毁的时候为true
    private var isStopAnimation = false
    private var mDialog: AlertDialog? = null
    private var mBuilder: AlertDialog.Builder? = null

    //展开动画
    private fun expendAnimation() {
        //如果停止动画就直接返回
        if (isStopAnimation) {
            return
        }
        //开启左边位移动画
        val leftTranslationAnimator =
            ObjectAnimator.ofFloat(mLeftView, "translationX", 0f, -mTranslationDistance.toFloat())
        //开启右边位移动画
        val rightTranslatioAnimation =
            ObjectAnimator.ofFloat(mRightView, "translationX", 0f, mTranslationDistance.toFloat())
        //两个动画一起
        val set = AnimatorSet()
        set.duration = ANIMATION_DRUATION
        set.playTogether(leftTranslationAnimator, rightTranslatioAnimation)
        set.interpolator = DecelerateInterpolator()
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                //动画结束之后往里面跑
                innerAnimation()
            }
        })
        set.start()
    }

    private fun innerAnimation() {
        if (isStopAnimation) {
            return
        }
        //开启左边位移动画
        val leftTranslationAnimator =
            ObjectAnimator.ofFloat(mLeftView, "translationX", -mTranslationDistance.toFloat(), 0f)
        //开启右边位移动画
        val rightTranslatioAnimation =
            ObjectAnimator.ofFloat(mRightView, "translationX", mTranslationDistance.toFloat(), 0f)
        //两个动画一起
        val set = AnimatorSet()
        set.duration = ANIMATION_DRUATION
        set.playTogether(leftTranslationAnimator, rightTranslatioAnimation)
        set.interpolator = AccelerateInterpolator()
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                //切换颜色 左边的给中间 中间的给右边  右边的给左边
                val mLeftViewColor = mLeftView.color
                val mRightViewColor = mRightView.color
                val mMiddleViewColor = mMiddleView.color
                mLeftView.exchangeColor(mRightViewColor)
                mRightView.exchangeColor(mMiddleViewColor)
                mMiddleView.exchangeColor(mLeftViewColor)
                expendAnimation()
            }
        })
        set.start()
    }

    private val circleView: CircleView
        get() {
            val circleView = CircleView(context)
            val params = LayoutParams(dp2px(10f), dp2px(10f))
            params.addRule(CENTER_IN_PARENT)
            circleView.layoutParams = params
            return circleView
        }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(View.INVISIBLE)
        //清理所有动画
        mLeftView.clearAnimation()
        mRightView.clearAnimation()
        val parent = parent as ViewGroup?
        if (parent != null) {
            //父布局中移除当前view
            parent.removeView(mLeftView)
            parent.removeView(mRightView)
            //移除自己的
            removeAllViews()
        }
        isStopAnimation = true
        LogUtils.e("TAG", "动画取消")
    }

    /**
     * 显示loading
     */
    fun show() {
        if (mBuilder == null) {
            mBuilder = AlertDialog.Builder(context)
                .setContentView(this)
                .setOnCancelListener(DialogInterface.OnCancelListener {
                    visibility = View.INVISIBLE
                })
                .setCancelable(false)
                .setWidthAndHeight(screenWidth / 2, screenHeight / 5)
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

    /**
     * 设置是否可以取消
     */
    fun setCancelable(flag: Boolean): CircleLoadingView {
        if (mDialog != null) {
            mDialog!!.setCancelable(flag)
        }
        return this
    }

    /**
     * 设置宽高
     */
    fun setWidthAndHeight(width: Int, height: Int): CircleLoadingView {
        if (mBuilder == null) {
            mBuilder = AlertDialog.Builder(context)
                .setContentView(this)
                .setOnCancelListener(DialogInterface.OnCancelListener {
                    visibility = View.INVISIBLE
                })
                .setCancelable(false)
                .setWidthAndHeight(width, height)
        } else {
            LogUtils.e("Please call Method before show Method")
        }
        return this
    }

    init {
        //设置相隔距离
        mTranslationDistance = dp2px(mTranslationDistance.toFloat())
        //设置背景
        setBackgroundColor(Color.WHITE)
        //绘制左边的圆形
        mLeftView = circleView
        mLeftView.exchangeColor(Color.BLUE)
        //绘制右边的圆
        mRightView = circleView
        mRightView.exchangeColor(Color.GREEN)
        //绘制中间的圆
        mMiddleView = circleView
        mMiddleView.exchangeColor(Color.RED)
        //添加圆
        addView(mLeftView)
        addView(mRightView)
        addView(mMiddleView)
        post { expendAnimation() }
    }
}