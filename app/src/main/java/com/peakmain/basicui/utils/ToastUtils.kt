package com.peakmain.basicui.utils

import android.R
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.peakmain.basicui.App

/**
 * author ：Peakmain
 * createTime：2020/3/10
 * mail:2726449200@qq.com
 * describe：
 */
class ToastUtils private constructor() {
    private class ApplicationContextWrapperForApi25 internal constructor() :
        ContextWrapper(App.app!!) {
        override fun getApplicationContext(): Context {
            return this
        }

        override fun getSystemService(name: String): Any {
            return if (Context.WINDOW_SERVICE == name) {
                // noinspection ConstantConditions
                WindowManagerWrapper(
                    baseContext.getSystemService(name) as WindowManager
                )
            } else super.getSystemService(name)
        }

        private class WindowManagerWrapper(private val base: WindowManager) : WindowManager {
            override fun getDefaultDisplay(): Display {
                return base.defaultDisplay
            }

            override fun removeViewImmediate(view: View) {
                base.removeViewImmediate(view)
            }

            override fun addView(view: View, params: ViewGroup.LayoutParams) {
                try {
                    base.addView(view, params)
                } catch (e: WindowManager.BadTokenException) {
                    Log.e("WindowManagerWrapper", e.message ?: "")
                } catch (throwable: Throwable) {
                    Log.e("WindowManagerWrapper", "[addView]", throwable)
                }
            }

            override fun updateViewLayout(view: View, params: ViewGroup.LayoutParams) {
                base.updateViewLayout(view, params)
            }

            override fun removeView(view: View) {
                base.removeView(view)
            }

        }
    }

    companion object {
        private const val COLOR_DEFAULT = -0x1000001
        private val HANDLER = Handler(Looper.getMainLooper())
        private var sToast: Toast? = null
        private var sGravity = -1
        private var sXOffset = -1
        private var sYOffset = -1
        private var sBgColor = COLOR_DEFAULT
        private var sBgResource = -1
        private var sMsgColor = COLOR_DEFAULT
        private var sMsgTextSize = -1

        /**
         * 设置gravity
         *
         * @param gravity The gravity.
         * @param xOffset X-axis offset, in pixel.
         * @param yOffset Y-axis offset, in pixel.
         */
        fun setGravity(gravity: Int, xOffset: Int, yOffset: Int) {
            sGravity = gravity
            sXOffset = xOffset
            sYOffset = yOffset
        }

        /**
         * 设置背景颜色
         *
         * @param backgroundColor The color of background.
         */
        fun setBgColor(@ColorInt backgroundColor: Int) {
            sBgColor = backgroundColor
        }

        /**
         * 设置背景资源
         *
         * @param bgResource The resource of background.
         */
        fun setBgResource(@DrawableRes bgResource: Int) {
            sBgResource = bgResource
        }

        /**
         * 设置消息的颜色
         *
         * @param msgColor The color of message.
         */
        fun setMsgColor(@ColorInt msgColor: Int) {
            sMsgColor = msgColor
        }

        /**
         * 设置消息的字体的大小
         *
         * @param textSize The text size of message.
         */
        fun setMsgTextSize(textSize: Int) {
            sMsgTextSize = textSize
        }

        /**
         * Show the sToast for a short period of time.
         *
         * @param text The text.
         */
        fun showShort(text: CharSequence) {
            show(text, Toast.LENGTH_SHORT)
        }

        /**
         * Show the sToast for a short period of time.
         *
         * @param resId The resource id for text.
         */
        fun showShort(@StringRes resId: Int) {
            show(resId, Toast.LENGTH_SHORT)
        }

        /**
         * Show the sToast for a short period of time.
         *
         * @param resId The resource id for text.
         * @param args  The args.
         */
        fun showShort(@StringRes resId: Int, vararg args: Any) {
            if (args != null && args.size == 0) {
                show(resId, Toast.LENGTH_SHORT)
            } else {
                show(resId, Toast.LENGTH_SHORT, *args)
            }
        }


        /**
         * Show the sToast for a long period of time.
         *
         * @param text The text.
         */
        fun showLong(text: CharSequence) {
            show(text, Toast.LENGTH_LONG)
        }

        /**
         * Show the sToast for a long period of time.
         *
         * @param resId The resource id for text.
         */
        fun showLong(@StringRes resId: Int) {
            show(resId, Toast.LENGTH_LONG)
        }

        /**
         * Show the sToast for a long period of time.
         *
         * @param resId The resource id for text.
         * @param args  The args.
         */
        fun showLong(@StringRes resId: Int, vararg args: Any) {
            if (args.isEmpty()) {
                show(resId, Toast.LENGTH_SHORT)
            } else {
                show(resId, Toast.LENGTH_LONG, *args)
            }
        }

        /**
         * Show the sToast for a long period of time.
         *
         * @param format The format.
         * @param args   The args.
         */
        fun showLong(format: String, vararg args: Any) {
            if (args != null && args.size == 0) {
                show(format, Toast.LENGTH_SHORT)
            } else {
                show(format, Toast.LENGTH_LONG, *args)
            }
        }

        /**
         * Show custom sToast for a short period of time.
         *
         * @param layoutId ID for an XML layout resource to load.
         */
        fun showCustomShort(@LayoutRes layoutId: Int): View? {
            val view = getView(layoutId)
            show(view, Toast.LENGTH_SHORT)
            return view
        }

        /**
         * Show custom sToast for a long period of time.
         *
         * @param layoutId ID for an XML layout resource to load.
         */
        fun showCustomLong(@LayoutRes layoutId: Int): View? {
            val view = getView(layoutId)
            show(view, Toast.LENGTH_LONG)
            return view
        }

        /**
         * Cancel the sToast.
         */
        fun cancel() {
            if (sToast != null) {
                sToast!!.cancel()
            }
        }

        private fun show(@StringRes resId: Int, duration: Int) {
            show(App.app!!.getResources().getText(resId).toString(), duration)
        }

        private fun show(@StringRes resId: Int, duration: Int, vararg args: Any) {
            show(java.lang.String.format(App.app!!.getResources().getString(resId), args), duration)
        }

        private fun show(format: String, duration: Int, vararg args: Any) {
            show(String.format(format, *args), duration)
        }

        private fun show(text: CharSequence, duration: Int) {
            HANDLER.post {
                cancel()
                sToast = Toast.makeText(App.app, text, duration)
                val tvMessage = sToast!!.view?.findViewById<TextView>(R.id.message)
                if (sMsgColor != COLOR_DEFAULT) {
                    tvMessage?.setTextColor(sMsgColor)
                }
                if (sMsgTextSize != -1) {
                    tvMessage?.textSize = sMsgTextSize.toFloat()
                }
                if (sGravity != -1 || sXOffset != -1 || sYOffset != -1) {
                    sToast?.setGravity(sGravity, sXOffset, sYOffset)
                }
                if (tvMessage != null)
                    setBg(tvMessage)
                showToast()
            }
        }

        private fun show(view: View?, duration: Int) {
            HANDLER.post {
                cancel()
                sToast = Toast(App.app)
                sToast!!.view = view
                sToast!!.duration = duration
                if (sGravity != -1 || sXOffset != -1 || sYOffset != -1) {
                    sToast!!.setGravity(sGravity, sXOffset, sYOffset)
                }
                setBg()
                showToast()
            }
        }

        private fun showToast() {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
                try {
                    val field = View::class.java.getDeclaredField("mContext")
                    field.isAccessible = true
                    field[sToast!!.view] = ApplicationContextWrapperForApi25()
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()
                }
            }
            sToast!!.show()
        }

        private fun setBg() {
            if (sBgResource != -1) {
                val toastView = sToast!!.view
                toastView?.setBackgroundResource(sBgResource)
            } else if (sBgColor != COLOR_DEFAULT) {
                val toastView = sToast!!.view
                val background = toastView?.background
                if (background != null) {
                    background.colorFilter = PorterDuffColorFilter(sBgColor, PorterDuff.Mode.SRC_IN)
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        toastView?.background = ColorDrawable(sBgColor)
                    } else {
                        toastView?.setBackgroundDrawable(ColorDrawable(sBgColor))
                    }
                }
            }
        }

        private fun setBg(tvMsg: TextView) {
            if (sBgResource != -1) {
                val toastView = sToast!!.view
                toastView?.setBackgroundResource(sBgResource)
                tvMsg.setBackgroundColor(Color.TRANSPARENT)
            } else if (sBgColor != COLOR_DEFAULT) {
                val toastView = sToast!!.view
                val tvBg = toastView?.background
                val msgBg = tvMsg.background
                if (tvBg != null && msgBg != null) {
                    tvBg.colorFilter = PorterDuffColorFilter(sBgColor, PorterDuff.Mode.SRC_IN)
                    tvMsg.setBackgroundColor(Color.TRANSPARENT)
                } else if (tvBg != null) {
                    tvBg.colorFilter = PorterDuffColorFilter(sBgColor, PorterDuff.Mode.SRC_IN)
                } else if (msgBg != null) {
                    msgBg.colorFilter = PorterDuffColorFilter(sBgColor, PorterDuff.Mode.SRC_IN)
                } else {
                    toastView?.setBackgroundColor(sBgColor)
                }
            }
        }

        private fun getView(@LayoutRes layoutId: Int): View? {
            val inflate =
                App.app!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            return inflate?.inflate(layoutId, null)
        }
    }

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }
}