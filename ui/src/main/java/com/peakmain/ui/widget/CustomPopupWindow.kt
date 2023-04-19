package com.peakmain.ui.widget

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.*
import android.widget.PopupWindow
import androidx.annotation.RequiresApi

/**
 * author ：Peakmain
 * createTime：2020/2/29
 * mail:2726449200@qq.com
 * describe：popupwindow 的封装
 */
class CustomPopupWindow private constructor(private val mContext: Context) :
    PopupWindow.OnDismissListener {
    var width = 0
        private set
    var height = 0
        private set
    private var mIsFocusable = true
    private var mIsOutside = true
    private var mResLayoutId = -1
    private var mContentView: View? = null
    var popupWindow: PopupWindow? = null
        private set
    private var mAnimationStyle = -1
    private var mClippEnable = true
    private var mIgnoreCheekPress = false
    private var mInputMode = -1
    private var mOnDismissListener: PopupWindow.OnDismissListener? = null
    private var mSoftInputMode = -1
    private var mTouchable = true
    private var mOnTouchListener: View.OnTouchListener? = null
    private var mWindow: Window? = null

    /**
     * 弹出PopWindow 背景是否变暗，默认不会变暗。
     */
    private var mIsBackgroundDark = false

    /**
     * 背景变暗的值，0 - 1
     */
    private var mBackgroundDrakValue = 0f

    /**
     * 设置是否允许点击 PopupWindow之外的地方，关闭PopupWindow
     * 默认点击pop之外的地方可以关闭
     */
    private var enableOutsideTouchDisMiss = true

    fun showAsDropDown(anchor: View?): CustomPopupWindow {
        if (popupWindow != null) {
            popupWindow!!.showAsDropDown(anchor)
        }
        return this
    }

    fun <T : View> getView(id: Int): T {
        return mContentView!!.findViewById(id)
    }

    fun setBackgroundDark(): CustomPopupWindow {
        val alpha =
            if (mBackgroundDrakValue > 0 && mBackgroundDrakValue < 1) mBackgroundDrakValue else DEFAULT_ALPHA
        if (mWindow != null) {
            val params = mWindow!!.attributes
            params.alpha = alpha
            mWindow!!.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            mWindow!!.attributes = params
        }
        return this
    }

    /**
     * @param anchor 被设置的布局
     * @param xOff   x偏移量
     * @param yOff   y偏移量
     * @return CustomPopupWindow
     */
    fun showAsDropDown(anchor: View?, xOff: Int, yOff: Int): CustomPopupWindow {
        if (popupWindow != null) {
            popupWindow!!.showAsDropDown(anchor, xOff, yOff)
        }
        return this
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun showAsDropDown(anchor: View?, xOff: Int, yOff: Int, gravity: Int): CustomPopupWindow {
        if (popupWindow != null) {
            popupWindow!!.showAsDropDown(anchor, xOff, yOff, gravity)
        }
        return this
    }

    /**
     * 相对于父控件的位置（通过设置Gravity.CENTER，下方Gravity.BOTTOM等 ），可以设置具体位置坐标
     *
     * @param parent  父控件
     * @param gravity 相对于父控件的位置
     * @param x       the popup's x location offset
     * @param y       the popup's y location offset
     * @return CustomPopupWindow
     */
    fun showAtLocation(parent: View?, gravity: Int, x: Int, y: Int): CustomPopupWindow {
        if (popupWindow != null) {
            popupWindow!!.showAtLocation(parent, gravity, x, y)
        }
        return this
    }

    /**
     * 添加一些属性设置
     *
     * @param popupWindow
     */
    private fun apply(popupWindow: PopupWindow) {
        popupWindow.isClippingEnabled = mClippEnable
        if (mIgnoreCheekPress) {
            popupWindow.setIgnoreCheekPress()
        }
        if (mInputMode != -1) {
            popupWindow.inputMethodMode = mInputMode
        }
        if (mSoftInputMode != -1) {
            popupWindow.softInputMode = mSoftInputMode
        }
        if (mOnDismissListener != null) {
            popupWindow.setOnDismissListener(mOnDismissListener)
        }
        if (mOnTouchListener != null) {
            popupWindow.setTouchInterceptor(mOnTouchListener)
        }
        popupWindow.isTouchable = mTouchable
    }

    private fun build(): PopupWindow {
        if (mContentView == null) {
            mContentView = LayoutInflater.from(mContext).inflate(mResLayoutId, null)
        }

        // 2017.3.17 add
        // 获取当前Activity的window
        val activity = mContentView!!.context as Activity
        if (mIsBackgroundDark) {
            //如果设置的值在0 - 1的范围内，则用设置的值，否则用默认值
            val alpha =
                if (mBackgroundDrakValue > 0 && mBackgroundDrakValue < 1) mBackgroundDrakValue else DEFAULT_ALPHA
            mWindow = activity.window
            val params = mWindow?.attributes
            params?.alpha = alpha
            mWindow?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            mWindow?.attributes = params
        }
        popupWindow = if (width != 0 && height != 0) {
            PopupWindow(mContentView, width, height)
        } else {
            PopupWindow(
                mContentView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        if (mAnimationStyle != -1) {
            popupWindow!!.animationStyle = mAnimationStyle
        }
        apply(popupWindow!!) //设置一些属性
        if (width == 0 || height == 0) {
            popupWindow!!.contentView.measure(
                View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED
            )
            //如果外面没有设置宽高的情况下，计算宽高并赋值
            width = popupWindow!!.contentView.measuredWidth
            height = popupWindow!!.contentView.measuredHeight
        }

        // 添加dissmiss 监听
        popupWindow!!.setOnDismissListener(this)

        // 判断是否点击PopupWindow之外的地方关闭 popWindow
        if (!enableOutsideTouchDisMiss) {
            //注意这三个属性必须同时设置，不然不能disMiss，以下三行代码在Android 4.4 上是可以，然后在Android 6.0以上，下面的三行代码就不起作用了，就得用下面的方法
            popupWindow!!.isFocusable = true
            popupWindow!!.isOutsideTouchable = false
            popupWindow!!.setBackgroundDrawable(null)
            //注意下面这三个是contentView 不是PopupWindow
            popupWindow!!.contentView.isFocusable = true
            popupWindow!!.contentView.isFocusableInTouchMode = true
            popupWindow!!.contentView.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindow!!.dismiss()
                    return@OnKeyListener true
                }
                false
            })
            //在Android 6.0以上 ，只能通过拦截事件来解决
            popupWindow!!.setTouchInterceptor(object : View.OnTouchListener {
                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    val x = event.x.toInt()
                    val y = event.y.toInt()
                    if (event.action == MotionEvent.ACTION_DOWN
                        && (x < 0 || x >= width || y < 0 || y >= height)
                    ) {
                        Log.e(TAG, "out side ")
                        Log.e(
                            TAG,
                            "width:" + popupWindow!!.width + "height:" + popupWindow!!.height + " x:" + x + " y  :" + y
                        )
                        return true
                    } else if (event.action == MotionEvent.ACTION_OUTSIDE) {
                        Log.e(TAG, "out side ...")
                        return true
                    }
                    return false
                }
            })
        } else {
            popupWindow!!.isFocusable = mIsFocusable
            popupWindow!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            popupWindow!!.isOutsideTouchable = mIsOutside
        }
        // update
        popupWindow!!.update()
        return popupWindow!!
    }

    override fun onDismiss() {
        dissmiss()
    }

    /**
     * 关闭popWindow
     */
    fun dissmiss() {
        if (mOnDismissListener != null) {
            mOnDismissListener!!.onDismiss()
        }
        //如果设置了背景变暗，那么在dissmiss的时候需要还原
        if (mWindow != null) {
            val params = mWindow!!.attributes
            params.alpha = 1.0f
            mWindow!!.attributes = params
        }
        if (popupWindow != null && popupWindow!!.isShowing) {
            popupWindow!!.dismiss()
        }
    }

    class PopupWindowBuilder(context: Context) {
        private val mCustomPopupWindow: CustomPopupWindow = CustomPopupWindow(context)
        fun size(width: Int, height: Int): PopupWindowBuilder {
            mCustomPopupWindow.width = width
            mCustomPopupWindow.height = height
            return this
        }

        fun setFocusable(focusable: Boolean): PopupWindowBuilder {
            mCustomPopupWindow.mIsFocusable = focusable
            return this
        }

        /**
         * 设置布局View
         * @param resLayoutId 布局Id
         */
        fun setView(resLayoutId: Int): PopupWindowBuilder {
            mCustomPopupWindow.mResLayoutId = resLayoutId
            mCustomPopupWindow.mContentView = null
            return this
        }
        /**
         * 设置布局View
         * @param view View
         */
        fun setView(view: View?): PopupWindowBuilder {
            mCustomPopupWindow.mContentView = view
            mCustomPopupWindow.mResLayoutId = -1
            return this
        }

        fun setOutsideTouchable(outsideTouchable: Boolean): PopupWindowBuilder {
            mCustomPopupWindow.mIsOutside = outsideTouchable
            return this
        }

        fun setOnClickListener(
            id: Int,
            onClickListener: View.OnClickListener?
        ): PopupWindowBuilder {
            mCustomPopupWindow.popupWindow
                ?.contentView
                ?.findViewById<View>(id)
                ?.setOnClickListener(onClickListener)
            return this
        }

        /**
         * 设置弹窗动画
         */
        fun setAnimationStyle(animationStyle: Int): PopupWindowBuilder {
            mCustomPopupWindow.mAnimationStyle = animationStyle
            return this
        }

        fun setClippingEnable(enable: Boolean): PopupWindowBuilder {
            mCustomPopupWindow.mClippEnable = enable
            return this
        }

        fun setIgnoreCheekPress(ignoreCheekPress: Boolean): PopupWindowBuilder {
            mCustomPopupWindow.mIgnoreCheekPress = ignoreCheekPress
            return this
        }

        fun setInputMethodMode(mode: Int): PopupWindowBuilder {
            mCustomPopupWindow.mInputMode = mode
            return this
        }

        fun setOnDissmissListener(onDissmissListener: PopupWindow.OnDismissListener?): PopupWindowBuilder {
            mCustomPopupWindow.mOnDismissListener = onDissmissListener
            return this
        }

        fun setSoftInputMode(softInputMode: Int): PopupWindowBuilder {
            mCustomPopupWindow.mSoftInputMode = softInputMode
            return this
        }

        fun setTouchable(touchable: Boolean): PopupWindowBuilder {
            mCustomPopupWindow.mTouchable = touchable
            return this
        }

        fun setTouchIntercepter(touchIntercepter: View.OnTouchListener?): PopupWindowBuilder {
            mCustomPopupWindow.mOnTouchListener = touchIntercepter
            return this
        }

        /**
         * 设置背景变暗是否可用
         *
         * @param isDark isDark
         */
        fun enableBackgroundDark(isDark: Boolean): PopupWindowBuilder {
            mCustomPopupWindow.mIsBackgroundDark = isDark
            return this
        }

        /**
         * 设置背景变暗的值
         *
         * @param darkValue darkValue
         */
        fun setBgDarkAlpha(darkValue: Float): PopupWindowBuilder {
            mCustomPopupWindow.mBackgroundDrakValue = darkValue
            return this
        }

        /**
         * 设置是否允许点击 PopupWindow之外的地方，关闭PopupWindow
         *
         * @param disMiss disMiss
         */
        fun enableOutsideTouchableDissmiss(disMiss: Boolean): PopupWindowBuilder {
            mCustomPopupWindow.enableOutsideTouchDisMiss = disMiss
            return this
        }

        fun getView(id: Int): PopupWindowBuilder {
            mCustomPopupWindow.mContentView!!.findViewById<View>(id)
            return this
        }

        /**
         * 创建CustomPopupWindow
         */
        fun create(): CustomPopupWindow {
            //构建PopWindow
            mCustomPopupWindow.build()
            return mCustomPopupWindow
        }

    }

    companion object {
        private val TAG = CustomPopupWindow::class.java.simpleName
        private const val DEFAULT_ALPHA = 0.7f
    }

}