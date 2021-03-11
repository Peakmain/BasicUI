package com.peakmain.ui.loading

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.peakmain.ui.R

/**
 * author ：Peakmain
 * createTime：2020/3/17
 * mail:2726449200@qq.com
 * describe：仿老版58同城加载loading
 */
class ShapeView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr) {
    private var mPaint: Paint? = null
    var currentShape = Shape.Circle
        private set
    private var mPath: Path? = null
    private var mCircleColor = Color.parseColor("#aa72d572")
    private var mRectColor = Color.parseColor("#aa738ffe")
    private var mTriangleColor = Color.parseColor("#aae84e40")

    enum class Shape {
        //三种形状
        Circle, Square, Triangle
    }

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0) {
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ShapeView)
        mCircleColor = ta.getColor(R.styleable.ShapeView_svCircleColor, mCircleColor)
        mRectColor = ta.getColor(R.styleable.ShapeView_svRectColor, mRectColor)
        mTriangleColor = ta.getColor(R.styleable.ShapeView_svTriangleColor, mTriangleColor)
        ta.recycle()
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint!!.isDither = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //保证是正方形
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(Math.min(width, height), Math.min(width, height))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val center = width / 2
        when (currentShape) {
            Shape.Circle -> {
                mPaint!!.color = mCircleColor
                canvas.drawCircle(center.toFloat(), center.toFloat(), center.toFloat(), mPaint)
            }
            Shape.Square -> {
                mPaint!!.color = mRectColor
                canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), mPaint)
            }
            Shape.Triangle -> {
                //绘制等边三角形
                mPaint!!.color = mTriangleColor
                if (mPath == null) {
                    mPath = Path()
                    mPath!!.moveTo(center.toFloat(), 0f)
                    mPath!!.lineTo(0f, (center * Math.sqrt(3.0)).toFloat())
                    mPath!!.lineTo(width.toFloat(), (center * Math.sqrt(3.0)).toFloat())
                    mPath!!.close()
                }
                canvas.drawPath(mPath, mPaint)
            }
        }
    }

    /**
     * 切换形状
     */
    fun exchange() {
        currentShape = when (currentShape) {
            Shape.Circle -> Shape.Square
            Shape.Square -> Shape.Triangle
            Shape.Triangle -> Shape.Circle
        }
        invalidate()
    }

    init {
        initAttrs(context, attrs)
    }
}