package com.peakmain.ui.recyclerview.itemdecoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.text.TextUtils
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.peakmain.ui.recyclerview.group.GroupRecyclerBean
import com.peakmain.ui.utils.SizeUtils.Companion.dp2px

/**
 * author ：Peakmain
 * createTime：2020/2/29
 * mail:2726449200@qq.com
 * describe：基本悬浮列表
 */
abstract class BaseSuspenisonItemDecoration<T : GroupRecyclerBean<*>?> :
    RecyclerView.ItemDecoration {
    private var mData: List<T>
    private var mBgPaint: Paint? = null
    private var mTextPaint: TextPaint? = null
    private var mBounds: Rect

    //置顶距离文字的高度 默认是30
    private var mSectionHeight: Int
    private var mBgColor: Int
    private var mTextColor: Int
    private var mTextSize: Int

    //两个置顶模块之间的距离，默认是10
    private var topHeight: Int

    //文字距离左边的padding
    private var mPaddingLeft: Int
    private var mPaddingRight: Int
    private var mPaddingTop: Int
    private var mPaddingBottom: Int
    private var isCenter = false

    constructor(context: Context?, data: List<T>) {
        mData = data
        mBgColor = ContextCompat.getColor(context!!, android.R.color.white)
        mSectionHeight = dp2px(30f)
        topHeight = dp2px(10f)
        mTextSize = dp2px(10f)
        mTextColor = ContextCompat.getColor(context, com.peakmain.ui.R.color.ui_color_4A4A4A)
        mPaddingLeft = dp2px(10f)
        mPaddingBottom = dp2px(5f)
        mPaddingRight = dp2px(10f)
        mPaddingTop = dp2px(5f)
        initPaint()
        mBounds = Rect()
    }

    constructor(builder: Builder<T>) {
        mData = builder.mData
        mBgColor =
            if (builder.mBgColor != 0) builder.mBgColor else ContextCompat.getColor(builder.mContext,
                android.R.color.white)
        mSectionHeight = if (builder.mSectionHeight != 0) builder.mSectionHeight else dp2px(30f)
        topHeight = if (builder.topHeight != 0) builder.topHeight else dp2px(10f)
        mTextSize = if (builder.mTextSize != 0) builder.mTextSize else dp2px(10f)
        mTextColor = if (builder.mTextColor != 0) builder.mTextColor else ContextCompat.getColor(
            builder.mContext,
            com.peakmain.ui.R.color.ui_color_4A4A4A)
        mPaddingLeft = if (builder.mPaddingLeft != 0) builder.mPaddingLeft else dp2px(10f)
        mPaddingBottom = if (builder.mPaddingBottom != 0) builder.mPaddingBottom else dp2px(5f)
        mPaddingRight = if (builder.mPaddingRight != 0) builder.mPaddingRight else dp2px(10f)
        mPaddingTop = if (builder.mPaddingTop != 0) builder.mPaddingTop else dp2px(5f)
        isCenter = builder.mIsCeneter
        initPaint()
        mBounds = Rect()
    }

    private fun initPaint() {
        mBgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBgPaint!!.color = mBgColor
        mTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint!!.textSize = mTextSize.toFloat()
        mTextPaint!!.color = mTextColor
    }

    fun setData(data: List<T>) {
        mData = data
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child
                .layoutParams as RecyclerView.LayoutParams
            val position = params.viewLayoutPosition
            if (mData.isNotEmpty() && position <= mData.size - 1 && position > -1) {
                if (position == 0) {
                    drawSection(c, left, right, child, params, position)
                } else {
                    if (null != getTopText(mData, position)
                        && getTopText(mData, position) != getTopText(mData, position - 1)
                    ) {
                        drawSection(c, left, right, child, params, position)
                    }
                }
            }
        }
    }

    private fun drawSection(
        c: Canvas, left: Int, right: Int, child: View,
        params: RecyclerView.LayoutParams, position: Int
    ) {
        val topText = getTopText(mData, position)
        if (!TextUtils.isEmpty(topText)) {
            val rect = Rect(left,
                child.top - params.topMargin - mSectionHeight,
                right,
                child.top - params.topMargin)
            c.drawRect(rect, mBgPaint!!)
            mTextPaint!!.getTextBounds(topText,
                0,
                getTopText(mData, position)!!.length,
                mBounds)
            if (isCenter) {
                mTextPaint?.apply {
                    textAlign = Paint.Align.CENTER
                    c.drawText(topText ?: "",
                        rect.centerX().toFloat(),
                        child.top - params.topMargin - mSectionHeight / 2 + mBounds.height() / 2.toFloat(),
                        this)
                }

            } else {
                mTextPaint?.apply {
                    c.drawText(topText ?: "",
                        child.paddingLeft + mPaddingLeft.toFloat(),
                        child.top - params.topMargin - mSectionHeight / 2 + mBounds.height() / 2.toFloat(),
                        this)
                }
            }
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        var pos = 0
        pos = (parent.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        if (pos < 0) {
            return
        }
        if (mData.isEmpty()) {
            return
        }
        var section = getTopText(mData, pos)
        val child = parent.findViewHolderForLayoutPosition(pos)?.itemView
        var flag = false
        if (pos + 1 < mData.size) {
            if (null != section && section != getTopText(mData, pos + 1)) {
                if (child!!.height + child.top < mSectionHeight) {
                    c.save()
                    flag = true
                    c.translate(0f, child.height + child.top - mSectionHeight.toFloat())
                }
            }
        }
        val rect = Rect(parent.paddingLeft,
            parent.paddingTop,
            parent.right - parent.paddingRight,
            parent.paddingTop + mSectionHeight + mPaddingBottom / 2 + mPaddingTop / 2)
        c.drawRect(rect, mBgPaint!!)
        if (!TextUtils.isEmpty(section)) {
            mTextPaint!!.getTextBounds(section, 0, section!!.length, mBounds)
            if (isCenter) {
                mTextPaint!!.textAlign = Paint.Align.CENTER
                mTextPaint?.apply {
                    c.drawText(section ?: "",
                        rect.centerX().toFloat(),
                        parent.paddingTop + mSectionHeight - (mSectionHeight / 2 - mBounds.height() / 2) + mPaddingBottom / 4 + (mPaddingTop / 4).toFloat(),
                        this)
                }
            } else {
                mTextPaint?.apply {
                    c.drawText(section ?: "",
                        child!!.paddingLeft + mPaddingLeft.toFloat(),
                        parent.paddingTop + mSectionHeight - (mSectionHeight / 2 - mBounds.height() / 2) + mPaddingBottom / 4 + (mPaddingTop / 4).toFloat(),
                        this)
                }
            }
        } else if (pos == 0 || mData[pos]!!.isHeader) {
            section = getTopText(mData, pos + 1)
            if (!TextUtils.isEmpty(section)) {
                mTextPaint!!.getTextBounds(section, 0, section!!.length, mBounds)
                if (isCenter) {
                    mTextPaint?.apply {
                        c.drawText(section,
                            rect.centerX().toFloat(),
                            parent.paddingTop + mSectionHeight - (mSectionHeight / 2 - mBounds.height() / 2) + mPaddingBottom / 4 + (mPaddingTop / 4).toFloat(),
                            this)
                    }
                } else {
                    mTextPaint?.apply {
                        c.drawText(section,
                            child!!.paddingLeft + mPaddingLeft.toFloat(),
                            parent.paddingTop + mSectionHeight - (mSectionHeight / 2 - mBounds.height() / 2) + mPaddingBottom / 4 + (mPaddingTop / 4).toFloat(),
                            this)
                    }
                }
            }
        }
        if (flag) {
            c.restore()
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        if (mData.isNotEmpty() && position <= mData.size - 1 && position > -1) {
            val layoutManager = parent.layoutManager
            if (layoutManager is GridLayoutManager) {
                if (position / (getSpanCount(parent) + 1) == 0) {
                    outRect[0, topHeight, 0] = 0
                } else {
                    if (mData[position]!!.isHeader) {
                        outRect[0, mSectionHeight + topHeight / 2, 0] = 0
                    }
                }
            } else {
                if (position == 0) {
                    outRect[0, mSectionHeight, 0] = 0
                } else {
                    if (null != getTopText(mData, position)
                        && getTopText(mData, position) != getTopText(mData, position - 1)
                    ) {
                        outRect[0, mSectionHeight + topHeight, 0] = 0
                    }
                }
            }
        }
    }

    /**
     * 设置置顶的文字
     */
    abstract fun getTopText(data: List<T>?, position: Int): String?
    private fun getSpanCount(parent: RecyclerView): Int {
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            return layoutManager.spanCount
        }
        return 1
    }

    abstract class Builder<T : GroupRecyclerBean<*>?>(var mContext: Context, val mData: List<T>) {
        var mBgColor = 0
        var mSectionHeight = 0
        var topHeight = 0
        var mTextSize = 0
        var mTextColor = 0
        var mPaddingLeft = 0
        var mPaddingRight = 0
        var mPaddingTop = 0
        var mPaddingBottom = 0
        var mIsCeneter = false

        /**
         * 设置背景颜色
         *
         * @param bgColor 背景颜色
         */
        fun setBgColor(@ColorInt bgColor: Int): Builder<*> {
            mBgColor = bgColor
            return this
        }

        /**
         * 设置文字到左边的距离
         *
         * @param paddingLeft paddingLeft
         */
        fun setPaddingLeft(paddingLeft: Int): Builder<*> {
            mPaddingLeft = paddingLeft
            return this
        }

        /**
         * 设置文字到底部的距离
         *
         * @param paddingBottom paddingBottom
         */
        fun setPaddingBottom(paddingBottom: Int): Builder<*> {
            mPaddingBottom = paddingBottom
            return this
        }

        /**
         * 设置文字到右边的距离
         *
         * @param paddingRight paddingRight
         */
        fun setPaddingRight(paddingRight: Int): Builder<*> {
            mPaddingRight = paddingRight
            return this
        }

        /**
         * 设置文字到顶部的距离
         *
         * @param paddingTop paddingTop
         */
        fun setPaddingTop(paddingTop: Int): Builder<*> {
            mPaddingTop = paddingTop
            return this
        }

        /**
         * 置顶距离文字的高度 默认是30
         */
        fun setSectionHeight(sectionHeight: Int): Builder<*> {
            mSectionHeight = sectionHeight
            return this
        }

        /**
         * 两个置顶模块之间的距离，默认是10
         *
         * @param topHeight topHeight
         */
        fun setTopHeight(topHeight: Int): Builder<*> {
            this.topHeight = topHeight
            return this
        }

        /**
         * 设置文字的大小
         *
         * @param textSize 文字大小
         */
        fun setTextSize(textSize: Int): Builder<*> {
            mTextSize = textSize
            return this
        }

        /**
         * 设置文字的颜色
         *
         * @param textColor 文字的颜色
         */
        fun setTextColor(textColor: Int): Builder<*> {
            mTextColor = textColor
            return this
        }

        fun setTextCenter(isCenter: Boolean): Builder<*> {
            mIsCeneter = isCenter
            return this
        }

        abstract fun create(): BaseSuspenisonItemDecoration<*>?

    }
}