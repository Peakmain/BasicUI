package com.peakmain.ui.utils.fps

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.peakmain.ui.R
import com.peakmain.ui.constants.BasicUIUtils
import com.peakmain.ui.utils.ActivityUtils
import java.lang.ref.WeakReference
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

    fun addCallback(callback: FpsCallback): FpsMonitorUtils {
        mFpsView.addCallback(callback)
        return this
    }

    fun reset() {
        mFpsView.reset()
    }

    fun printMessage(isPrint: Boolean): FpsMonitorUtils {
        mFpsView.printMessage(isPrint)
        return this
    }

    interface FpsCallback {
        fun onFrame(fps: Double)
    }

    private class FpsView {
        private var mParams = WindowManager.LayoutParams()
        private var isPlaying = false
        private val mApplication = BasicUIUtils.application
        private var mView = WeakReference(View.inflate(mApplication, R.layout.ui_fps_view, null) as TextView)
        private val mDecimalFormat = DecimalFormat("#.0 fps")
        private var windowManager: WindowManager? = null
        private val mFrameMonitor = FrameMonitor()

        init {
            windowManager = mApplication!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            mParams.width = WindowManager.LayoutParams.WRAP_CONTENT
            mParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            mParams.flags =
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL

            mParams.format = PixelFormat.TRANSLUCENT
            mParams.gravity = Gravity.TOP or Gravity.END
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                mParams.type = WindowManager.LayoutParams.TYPE_TOAST
            }
            mFrameMonitor.addCallback(object : FpsCallback {
                override fun onFrame(fps: Double) {
                    mView.get()?.text = mDecimalFormat.format(fps)
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
                windowManager?.removeView(mView.get())
            }
        }

        private fun play() {
            if (!hasOverLaysPermission()) {
                ActivityUtils.startOverlaySettingActivity()
                return
            }
            mFrameMonitor.start()
            if (!isPlaying) {
                isPlaying = true
                windowManager?.addView(mView.get(), mParams)
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

        fun printMessage(isPrint: Boolean) {
            mFrameMonitor.printMessage(isPrint)
        }

    }

    fun hasOverLaysPermission(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(
            BasicUIUtils.application
        )
    }
}