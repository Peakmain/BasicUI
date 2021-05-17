package com.peakmain.ui.wheelview.timer

import com.peakmain.ui.wheelview.view.WheelView
import java.util.*
import kotlin.math.abs

/**
 * author:Peakmain
 * createTime:2021/5/17
 * mail:2726449200@qq.com
 * describe： 滚动惯性的实现
 */
class InertiaTimerTask(
        private val mWheelView: WheelView, //手指离开屏幕时的初始速度
        private val mFirstVelocityY: Float
) :
    TimerTask() {
    private var mCurrentVelocityY //当前滑动速度
            : Float

    override fun run() {

        //防止闪动，对速度做一个限制。
        if (mCurrentVelocityY == Int.MAX_VALUE.toFloat()) {
            mCurrentVelocityY = if (abs(mFirstVelocityY) > 2000f) {
                if (mFirstVelocityY > 0) 2000f else -2000f
            } else {
                mFirstVelocityY
            }
        }

        //发送handler消息 处理平顺停止滚动逻辑
        if (abs(mCurrentVelocityY) in 0.0f..20f) {
            mWheelView.cancelFuture()
            mWheelView.handler
                .sendEmptyMessage(MessageHandler.WHAT_SMOOTH_SCROLL)
            return
        }
        val dy = (mCurrentVelocityY / 100f).toInt()
        mWheelView.totalScrollY = mWheelView.totalScrollY - dy
        if (!mWheelView.isLoop) {
            val itemHeight = mWheelView.itemHeight
            var top = -mWheelView.initPosition * itemHeight
            var bottom =
                (mWheelView.itemsCount - 1 - mWheelView.initPosition) * itemHeight
            if (mWheelView.totalScrollY - itemHeight * 0.25 < top) {
                top = mWheelView.totalScrollY + dy
            } else if (mWheelView.totalScrollY + itemHeight * 0.25 > bottom) {
                bottom = mWheelView.totalScrollY + dy
            }
            if (mWheelView.totalScrollY <= top) {
                mCurrentVelocityY = 40f
                mWheelView.totalScrollY = top
            } else if (mWheelView.totalScrollY >= bottom) {
                mWheelView.totalScrollY = bottom
                mCurrentVelocityY = -40f
            }
        }
        mCurrentVelocityY = if (mCurrentVelocityY < 0.0f) {
            mCurrentVelocityY + 20f
        } else {
            mCurrentVelocityY - 20f
        }

        //刷新UI
        mWheelView.handler
            .sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW)
    }

    /**
     * @param wheelView 滚轮对象
     * @param velocityY Y轴滑行速度
     */
    init {
        mCurrentVelocityY = Int.MAX_VALUE.toFloat()
    }
}