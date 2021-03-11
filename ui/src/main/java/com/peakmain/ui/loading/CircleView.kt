package com.peakmain.ui.loading

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * author ：Peakmain
 * createTime：2020/3/4
 * mail:2726449200@qq.com
 * describe：圆形的view
 */
class CircleView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    /**
     * 返回颜色
     */
    var color = 0
        private set
    private var mPaint: Paint? = null
    private fun initPaint() {
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        //防抖动
        mPaint!!.isDither = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cx = width / 2
        val cy = height / 2
        canvas.drawCircle(cx.toFloat(), cy.toFloat(), cx.toFloat(), mPaint)
    }

    /**
     * 切换颜色
     */
    fun exchangeColor(color: Int) {
        this.color = color
        mPaint!!.color = this.color
        invalidate()
    }

    init {
        initPaint()
    }
}