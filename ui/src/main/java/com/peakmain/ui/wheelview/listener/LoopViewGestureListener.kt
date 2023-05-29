package com.peakmain.ui.wheelview.listener

import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import com.peakmain.ui.wheelview.view.WheelView

/**
 * author:Peakmain
 * createTime:2021/5/17
 * mail:2726449200@qq.com
 * describe：手势监听
 */
class LoopViewGestureListener(private val wheelView: WheelView) : SimpleOnGestureListener() {
    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        wheelView.scrollBy(velocityY)
        return true
    }

}