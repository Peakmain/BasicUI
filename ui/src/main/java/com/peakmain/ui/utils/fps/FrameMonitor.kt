package com.peakmain.ui.utils.fps

import android.view.Choreographer
import com.peakmain.ui.utils.ActivityUtils
import com.peakmain.ui.utils.LogUtils
import java.util.concurrent.TimeUnit

/**
 * author ：Peakmain
 * createTime：2021/5/6
 * mail:2726449200@qq.com
 * describe：
 */
internal class FrameMonitor : Choreographer.FrameCallback {
    private val mChoreographer = Choreographer.getInstance()
    private var mFrameStartTime: Long = 0

    //1s绘制了多少帧
    private var mFrameCount = 0
    private var mCallbackLists = arrayListOf<FpsMonitorUtils.FpsCallback>()
    private var isPrintMessage = false
    override fun doFrame(frameTimeNanos: Long) {
        val currentTimeMills = TimeUnit.NANOSECONDS.toMillis(frameTimeNanos)
        if (mFrameStartTime > 0) {
            val time = currentTimeMills - mFrameStartTime
            mFrameCount++
            if (time > 1000) {
                val fps = mFrameCount * 1000 / time.toDouble()
                val topActivity = ActivityUtils.mInstance.getTopActivity(true)
                if (isPrintMessage) {
                    LogUtils.d("当前Activity是:$topActivity,它的fps是:$fps")
                }
                mCallbackLists.forEach {
                    it.onFrame(fps)
                }
                mFrameCount = 0
                mFrameStartTime = currentTimeMills
            }
        } else {
            mFrameStartTime = currentTimeMills
        }
        start()
    }

    fun start() {
        mChoreographer.postFrameCallback(this)
    }

    fun stop() {
        mFrameStartTime = 0
        mChoreographer.removeFrameCallback(this)
    }

    fun addCallback(callback: FpsMonitorUtils.FpsCallback) {
        mCallbackLists.add(callback)
    }

    fun reset() {
        mFrameStartTime = 0
        mChoreographer.removeFrameCallback(this)
        mFrameCount = 0
        mCallbackLists.clear()
    }

    fun printMessage(print: Boolean) {
        this.isPrintMessage = print
    }
}