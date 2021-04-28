package com.peakmain.ui.utils

import android.support.v4.view.ViewCompat
import android.view.View

/**
 * author ：Peakmain
 * createTime：2021/4/28
 * mail:2726449200@qq.com
 * describe：view偏移量工具类
 */
class UIViewOffsetUtils(var mView: View) {
    private var mLayoutTop: Int = 0
    private var mLayoutLeft: Int = 0

    private var mOffsetTop: Int = 0
    private var mOffsetLeft: Int = 0

    private var mVerticalOffsetEnabled = true
    private var mHorizontalOffsetEnabled = true

    fun onViewLayout() {
        onViewLayout(true)
    }

    fun onViewLayout(applyOffset: Boolean) {
        mLayoutTop = mView.top
        mLayoutLeft = mView.left
        if (applyOffset) {
            applyOffset()
        }
    }

    fun applyOffset() {
        ViewCompat.offsetTopAndBottom(mView, mOffsetTop - (mView.top - mLayoutTop))
        ViewCompat.offsetLeftAndRight(mView, mOffsetLeft - (mView.left - mLayoutLeft))
    }

    /**
     * 设置上下的偏移量
     */
    fun setTopAndBottomOffset(offset: Int): Boolean {
        if (mVerticalOffsetEnabled && mOffsetTop != offset) {
            mOffsetTop = offset
            applyOffset()
            return true
        }
        return false
    }

    /**
     * 设置左右偏移量
     */
    fun setLeftAndRightOffset(offset: Int): Boolean {
        if (mHorizontalOffsetEnabled && mOffsetLeft != offset) {
            mOffsetLeft = offset
            applyOffset()
            return true
        }
        return false
    }

    /**
     * 设置偏移量
     */
    fun setOffset(leftOffset: Int, topOffset: Int): Boolean {
        if (!mHorizontalOffsetEnabled && !mVerticalOffsetEnabled) {
            return false
        } else if (mHorizontalOffsetEnabled && mVerticalOffsetEnabled) {
            if (mOffsetLeft != leftOffset || mOffsetTop != topOffset) {
                mOffsetLeft = leftOffset
                mOffsetTop = topOffset
                applyOffset()
                return true
            }
            return false
        } else if (mVerticalOffsetEnabled) {
            return setTopAndBottomOffset(topOffset)
        }
        return setLeftAndRightOffset(leftOffset)
    }
    fun getTopAndBottomOffset(): Int {
        return mOffsetTop
    }

    fun getLeftAndRightOffset(): Int {
        return mOffsetLeft
    }

    fun getLayoutTop(): Int {
        return mLayoutTop
    }

    fun getLayoutLeft(): Int {
        return mLayoutLeft
    }

    fun setHorizontalOffsetEnabled(horizontalOffsetEnabled: Boolean) {
        mHorizontalOffsetEnabled = horizontalOffsetEnabled
    }

    fun isHorizontalOffsetEnabled(): Boolean {
        return mHorizontalOffsetEnabled
    }

    fun setVerticalOffsetEnabled(verticalOffsetEnabled: Boolean) {
        mVerticalOffsetEnabled = verticalOffsetEnabled
    }

    fun isVerticalOffsetEnabled(): Boolean {
        return mVerticalOffsetEnabled
    }
}