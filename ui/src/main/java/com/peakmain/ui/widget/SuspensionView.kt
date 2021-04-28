package com.peakmain.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.FrameLayout
import android.widget.ImageView
import com.peakmain.ui.R
import com.peakmain.ui.utils.SizeUtils
import com.peakmain.ui.utils.UIViewOffsetUtils
import kotlin.math.sqrt

/**
 * author ：Peakmain
 * createTime：2021/4/28
 * mail:2726449200@qq.com
 * describe：悬浮的view
 */
class SuspensionView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defAttrStyle: Int = 0) : FrameLayout(context, attributeSet, defAttrStyle) {
    private var mImageViewSize = 0
    private var mViewOffsetUtils: UIViewOffsetUtils
    private var mImageView: ImageView
    private var mTouchDownX = 0f
    private var mTouchDownY = 0f
    private var mLastTouchX = 0f
    private var mLastTouchY = 0f
    private var isDragging = false
    private var isTouchDownInImageView = false
    private var mTouchSlop = 0

    init {
        mImageViewSize = SizeUtils.dp2px(56f)
        mImageView = ImageView(context)
        mImageView.setImageResource(R.drawable.ui_ic_file_word)
        mImageView.isClickable = true

        var layoutParams = LayoutParams(mImageViewSize, mImageViewSize)
        layoutParams.gravity = Gravity.BOTTOM or Gravity.END
        layoutParams.bottomMargin = SizeUtils.dp2px(60f)
        layoutParams.rightMargin = SizeUtils.dp2px(24f)
        addView(mImageView, layoutParams)
        mViewOffsetUtils = UIViewOffsetUtils(mImageView)
        mTouchSlop = ViewConfiguration.get(context).scaledPagingTouchSlop
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mViewOffsetUtils.onViewLayout()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        val x = ev!!.x
        val y = ev.y
        val action = ev.action
        if (action == MotionEvent.ACTION_DOWN) {
            isTouchDownInImageView = isDownInImageView(x, y)
            mTouchDownX = x
            mLastTouchX = mTouchDownX
            mLastTouchY = y
            mTouchDownY = mLastTouchY
        } else if (action == MotionEvent.ACTION_MOVE) {
            if (!isDragging && isTouchDownInImageView) {
                val dx = x - mTouchDownX
                val dy = y - mTouchDownY
                if (sqrt(dx * dx + dy * dy.toDouble()) > mTouchSlop) {
                    isDragging = true
                }
            }
            if (isDragging) {
                var dx = (x - mLastTouchX).toInt()
                var dy = (y - mLastTouchY).toInt()
                val gx = mImageView.left
                val gy = mImageView.top
                val gw = mImageView.width
                val w = width
                val gh = mImageView.height
                val h = height
                /*界限处理*/
                if (gx + dx < 0) {
                    dx = -gx
                } else if (gx + dx + gw > w) {
                    dx = w - gw - gx
                }
                if (gy + dy < 0) {
                    dy = -gy
                } else if (gy + dy + gh > h) {
                    dy = h - gh - gy
                }
                mViewOffsetUtils.setLeftAndRightOffset(mViewOffsetUtils.getLeftAndRightOffset() + dx)
                mViewOffsetUtils.setTopAndBottomOffset(mViewOffsetUtils.getTopAndBottomOffset() + dy)
            }
            mLastTouchX = x
            mLastTouchY = y
        } else if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            isDragging = false
            isTouchDownInImageView = false
        }
        return isDragging
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event!!.x
        val y = event.y
        val action = event.action
        if (action == MotionEvent.ACTION_DOWN) {
            isTouchDownInImageView = isDownInImageView(x, y)
            mLastTouchX = x
            mTouchDownX = mLastTouchX
            mLastTouchY = y
            mTouchDownY = mLastTouchY
        } else if (action == MotionEvent.ACTION_MOVE) {
            if (!isDragging && isTouchDownInImageView) {
                val dx = (x - mTouchDownX).toInt()
                val dy = (y - mTouchDownY).toInt()
                if (sqrt(dx * dx + dy * dy.toDouble()) > mTouchSlop) {
                    isDragging = true
                }
            }
            if (isDragging) {
                var dx = (x - mLastTouchX).toInt()
                var dy = (y - mLastTouchY).toInt()
                val gx = mImageView.left
                val gy = mImageView.top
                val gw = mImageView.width
                val w = width
                val gh = mImageView.height
                val h = height
                if (gx + dx < 0) {
                    dx = -gx
                } else if (gx + dx + gw > w) {
                    dx = w - gw - gx
                }
                if (gy + dy < 0) {
                    dy = -gy
                } else if (gy + dy + gh > h) {
                    dy = h - gh - gy
                }
                mViewOffsetUtils.setLeftAndRightOffset(mViewOffsetUtils.getLeftAndRightOffset() + dx)
                mViewOffsetUtils.setTopAndBottomOffset(mViewOffsetUtils.getTopAndBottomOffset() + dy)
            }
            mLastTouchX = x
            mLastTouchY = y
        } else if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            isDragging = false
            isTouchDownInImageView = false
        }
        return isDragging || super.onTouchEvent(event)
    }

    private fun isDownInImageView(x: Float, y: Float): Boolean {
        return mImageView.left < x && mImageView.right > x && mImageView.top < y && mImageView.bottom > y
    }
}