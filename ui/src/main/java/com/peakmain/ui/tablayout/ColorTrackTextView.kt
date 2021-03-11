package com.peakmain.ui.tablayout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import com.peakmain.ui.R

/**
 * author: peakmain
 * createdata：2019/7/12
 * mail: 2726449200@qq.com
 * desiption:
 */
class ColorTrackTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr) {
    // 1. 实现一个文字两种颜色 - 绘制不变色字体的画笔,原始的颜色
    private var mOriginPaint: Paint? = null

    // 2. 实现一个文字两种颜色 - 绘制变色字体的画笔,变色的颜色
    private var mChangePaint: Paint? = null

    //当前的进度
    private var mCurrentProgress = 0.0f

    // 3.实现不同朝向
    private var mDirection = Direction.LEFT_TO_RIGHT

    //4.默认不显示下划线
    private var mIsShowUnderLine = false

    enum class Direction {
        //从左到右
        LEFT_TO_RIGHT,  //从右到左
        RIGHT_TO_LEFT
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ColorTrackTextView)
        val changeColor = ta.getColor(R.styleable.ColorTrackTextView_changeTextColor, textColors.defaultColor)
        val originColor = ta.getColor(R.styleable.ColorTrackTextView_originTextColor, textColors.defaultColor)
        mIsShowUnderLine = ta.getBoolean(R.styleable.ColorTrackTextView_isShowTextUnderLine, false)
        mOriginPaint = getPaintByColor(originColor)
        mChangePaint = getPaintByColor(changeColor)
        ta.recycle()
    }

    /**
     * 通过颜色返回画笔
     */
    private fun getPaintByColor(color: Int): Paint {
        val paint = Paint()
        //设置颜色
        paint.color = color
        //设置抗锯齿
        paint.isAntiAlias = true
        //设置防抖动
        paint.isDither = true
        //设置字体的大小
        paint.textSize = textSize
        return paint
    }

    override fun onDraw(canvas: Canvas) {
        //super.onDraw(canvas);
        //中间值
        val middle = (mCurrentProgress * width).toInt()
        //从左到右绘制
        if (mDirection == Direction.LEFT_TO_RIGHT) {
            //绘制变色
            //左边是红色右边是蓝色
            drawText(canvas, mChangePaint, 0, middle)
            drawText(canvas, mOriginPaint, middle, width)
        } //从右到左绘制,左边是蓝色，右边是红色
        else if (mDirection == Direction.RIGHT_TO_LEFT) {
            // 右边是红色左边是蓝色
            drawText(canvas, mChangePaint, width - middle, width)
            // 绘制变色
            drawText(canvas, mOriginPaint, 0, width - middle)
        }
    }

    /**
     * 绘制Text
     */
    private fun drawText(canvas: Canvas, paint: Paint?, start: Int, end: Int) {
        canvas.save()
        //绘制不变色
        val rect = Rect(start, 0, end, height)
        canvas.clipRect(rect)
        val text = text.toString()
        val bounds = Rect()
        paint!!.getTextBounds(text, 0, text.length, bounds)
        //获取文字的宽度
        val x = width / 2 - bounds.width() / 2.toFloat()
        //基线
        val fontMetrics = paint.fontMetricsInt
        val dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
        val baseLine = dy + height / 2
        canvas.drawText(text, x, baseLine.toFloat(), paint)
        if (mIsShowUnderLine && paint !== mOriginPaint) {
            canvas.drawLine(start.toFloat(), height.toFloat(), end.toFloat(), height.toFloat(), paint)
        }
        canvas.restore()
    }

    /**
     * 设置方向
     */
    fun setDirection(direction: Direction) {
        mDirection = direction
    }

    /**
     * 设置当前进度
     */
    fun setCurrentProgress(currentProgress: Float) {
        mCurrentProgress = currentProgress
        invalidate()
    }

    /**
     * 设置改变颜色
     */
    fun setChangeColor(changeColor: Int) {
        mChangePaint!!.color = changeColor
    }

    /**
     * 设置原始的原色
     */
    fun setOriginColor(originColor: Int) {
        mOriginPaint!!.color = originColor
    }

    /**
     * 设置是否显示下划线
     *
     * @param showUnderLine true代表显示 false代表不显示
     */
    fun setShowUnderLine(showUnderLine: Boolean) {
        mIsShowUnderLine = showUnderLine
    }

    init {
        init(context, attrs)
    }
}