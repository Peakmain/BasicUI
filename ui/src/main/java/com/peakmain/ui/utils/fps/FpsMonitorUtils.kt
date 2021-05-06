package com.peakmain.ui.utils.fps

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.TextView
import com.peakmain.ui.R
import com.peakmain.ui.constants.BasicUIUtils
import com.peakmain.ui.utils.ActivityUtils
import com.peakmain.ui.utils.PermissionUtils
import java.text.DecimalFormat

/**
 * author ：Peakmain
 * createTime：2021/5/6
 * mail:2726449200@qq.com
 * describe：
 */
object FpsMonitorUtils {

    private val mFpsView = FpsView()
    fun toggle() {
        mFpsView.toggle()
    }

    fun addCallback(callback: FpsCallback) {
        mFpsView.addCallback(callback)
    }
     fun reset(){
         mFpsView.reset()
     }
    interface FpsCallback {
        fun onFrame(fps: Double)
    }

    private class FpsView {
        private var mParams = WindowManager.LayoutParams()
        private var isPlaying = false
        private val mApplication = BasicUIUtils.application
        private var mView = LayoutInflater.from(mApplication).inflate(R.layout.ui_fps_view, null, false) as TextView
        private val mDecimalFormat = DecimalFormat("#.0 fps")
        private var windowManager: WindowManager? = null
        private val mFrameMonitor = FrameMonitor()

        init {
            windowManager = mApplication!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            mParams.width = WindowManager.LayoutParams.WRAP_CONTENT
            mParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL

            mParams.format = PixelFormat.TRANSLUCENT
            mParams.gravity = Gravity.TOP or Gravity.RIGHT
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                mParams.type = WindowManager.LayoutParams.TYPE_TOAST
            }
            mFrameMonitor.addCallback(object : FpsCallback {
                override fun onFrame(fps: Double) {
                    mView.text = mDecimalFormat.format(fps)
                }

            })
            ActivityUtils.mInstance.addFrontBackCallback(object : ActivityUtils.FrontBackCallback {
                override fun onChanged(front: Boolean) {
                    if (front) {
                        play()
                    } else {
                        stop()
                    }
                }

            })

        }

        private fun stop() {
            mFrameMonitor.stop()
            if (isPlaying) {
                isPlaying = false
                windowManager?.removeView(mView)
            }
        }

        private fun play() {
            if (!PermissionUtils.hasOverLaysPermission()) {
                ActivityUtils.startOverlaySettingActivity()
                return
            }
            mFrameMonitor.start()
            if (!isPlaying) {
                isPlaying = true
                windowManager?.addView(mView, mParams)
            }
        }

        fun toggle() {
            if (isPlaying) {
                stop()
            } else {
                play()
            }
        }

        fun addCallback(callback: FpsCallback) {
            mFrameMonitor.addCallback(callback)
        }

        fun reset() {
            mFrameMonitor.reset()
        }

    }
}