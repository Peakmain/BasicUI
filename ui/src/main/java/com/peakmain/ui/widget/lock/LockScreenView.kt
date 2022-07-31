package com.peakmain.ui.widget.lock

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.peakmain.ui.R
import com.peakmain.ui.utils.MathUtils.checkInRound
import com.peakmain.ui.utils.MathUtils.distance
import java.util.*

/**
 * author ：Peakmain
 * createTime：2020/3/15
 * mail:2726449200@qq.com
 * describe：锁屏view
 */
class LockScreenView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var isInit = false

    //二维数组初始化int[3][3]
    private val mPoints = Array(3) { arrayOfNulls<Point>(3) }

    //外圆的半径
    private var mDotRadius = 0.0

    //画笔
    private lateinit var mLinePaint: Paint
    private lateinit var mPressedPaint: Paint
    private lateinit var mErrorPaint: Paint
    private lateinit var mNormalPaint: Paint
    private lateinit var mArrowPaint: Paint

    //颜色
    private var mOuterPressedColor = -0x734528
    private var mInnerPressedColor = -0xfa690a
    private var mOuterNormalColor = -0x262627
    private var mInnerNormalColor = -0x6d6d6e
    private var mOuterErrorColor = -0x6fefce
    private var mInnerErrorColor = -0x15f6bb
    private var isTouchPoint = false

    // 选中的所有点
    private var mSelectPoints: MutableList<Point> = ArrayList()
    private var mIsHideArrow = false
    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.LockScreenView)
        mOuterNormalColor =
            ta.getColor(R.styleable.LockScreenView_lpNormalOutColor, mOuterNormalColor)
        mOuterPressedColor =
            ta.getColor(R.styleable.LockScreenView_lpPressOutColor, mOuterPressedColor)
        mOuterErrorColor = ta.getColor(R.styleable.LockScreenView_lpErrorOutColor, mOuterErrorColor)
        mInnerNormalColor =
            ta.getColor(R.styleable.LockScreenView_lpNormalInnerColor, mInnerNormalColor)
        mInnerPressedColor =
            ta.getColor(R.styleable.LockScreenView_lpPressInnerColor, mInnerPressedColor)
        mInnerErrorColor =
            ta.getColor(R.styleable.LockScreenView_lpErrorInnerColor, mInnerErrorColor)
        ta.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        //初始化九宫格，防止onDraw回调多次
        if (!isInit) {
            initPaint()
            initDot()
            isInit = true
        }
        // 绘制九个宫格
        drawShow(canvas)
    }

    private fun drawShow(canvas: Canvas) {
        for (i in 0..2) {
            for (point in mPoints[i]) {
                val status: Int = point!!.status
                if (status == point!!.STATUS_NORMAL) {
                    //先绘制外圆
                    mNormalPaint.color = mOuterNormalColor
                    canvas.drawCircle(point.centerX.toFloat(),
                        point.centerY.toFloat(),
                        mDotRadius.toFloat(),
                        mNormalPaint)
                    //后绘制内圆
                    mNormalPaint.color = mInnerNormalColor
                    canvas.drawCircle(point.centerX.toFloat(),
                        point.centerY.toFloat(),
                        mDotRadius.toFloat() / 6.0f,
                        mNormalPaint)
                }
                if (status == point.STATUS_PRESSED) {
                    mPressedPaint.color = mOuterPressedColor
                    canvas.drawCircle(point.centerX.toFloat(),
                        point.centerY.toFloat(),
                        mDotRadius.toFloat(),
                        mPressedPaint)
                    mPressedPaint.color = mInnerPressedColor
                    canvas.drawCircle(point.centerX.toFloat(),
                        point.centerY.toFloat(),
                        mDotRadius.toFloat() / 6,
                        mPressedPaint)
                }
                if (status == point.STATUS_ERROR) {
                    mErrorPaint.color = mOuterErrorColor
                    canvas.drawCircle(point.centerX.toFloat(),
                        point.centerY.toFloat(),
                        mDotRadius.toFloat(),
                        mErrorPaint)
                    mErrorPaint.color = mInnerErrorColor
                    canvas.drawCircle(point.centerX.toFloat(),
                        point.centerY.toFloat(),
                        mDotRadius.toFloat() / 6,
                        mErrorPaint)
                }
            }
        }
        //绘制两个点之间的连线和箭头
        drawLine(canvas)
    }

    /**
     * 绘制两个点之间的连线以及箭头
     */
    private fun drawLine(canvas: Canvas) {
        if (mSelectPoints.size >= 1) {
            //两个点之间绘制一条线
            var lastPoint = mSelectPoints[0]
            for (i in mSelectPoints.indices) {
                drawLine(lastPoint, mSelectPoints[i], canvas, mLinePaint)
                //两个点之间绘制箭头
                if (!mIsHideArrow) {
                    drawArrow(canvas,
                        mArrowPaint,
                        lastPoint,
                        mSelectPoints[i],
                        mDotRadius.toFloat() / 5,
                        38)
                }
                lastPoint = mSelectPoints[i]
            }
            // 绘制最后一个点到手指当前位置的连线
            // 如果手指在内圆里面就不要绘制
            val isInnerPoint = checkInRound(lastPoint.centerX.toFloat(),
                lastPoint.centerY.toFloat(),
                (mDotRadius / 4.0).toFloat(),
                mMovingX,
                mMovingY)
            if (!isInnerPoint && isTouchPoint) {
                drawLine(lastPoint,
                    Point(mMovingX.toInt(), mMovingY.toInt(), -1),
                    canvas,
                    mLinePaint)
            }
        }
    }

    // 手指触摸的位置
    private var mMovingX = 0f
    private var mMovingY = 0f
    override fun onTouchEvent(event: MotionEvent): Boolean {
        mMovingX = event.x
        mMovingY = event.y
        val point: Point?
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                for (selectPoint in mSelectPoints) {
                    selectPoint.status = selectPoint.STATUS_NORMAL
                }
                mSelectPoints = ArrayList()
                //判断手指是不是在一个圆里面
                point = getPoint
                if (point != null) {
                    isTouchPoint = true
                    mSelectPoints.add(point)
                    //改变当前的状态
                    point.status = point.STATUS_PRESSED

                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (isTouchPoint) {
                    point = getPoint
                    if (point != null) {
                        if (!mSelectPoints.contains(point)) {
                            mSelectPoints.add(point)
                        }
                        //改变当前的一个状态
                        point.status = point.STATUS_PRESSED
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                isTouchPoint = false
                val sb = StringBuilder()
                for (selectPoint in mSelectPoints) {
                    val index = selectPoint.index + 1
                    sb.append(index)
                }
                if (mLockSuccessListener != null) {
                    val result = mLockSuccessListener!!.lockResult
                    if (result == sb.toString()) {
                        mLockSuccessListener!!.onLockSuccess(result)
                    }
                }
                for (selectPoint in mSelectPoints) {
                    selectPoint.status = selectPoint.STATUS_ERROR
                }
                invalidate()
            }
        }
        return true
    }

    /**
     *
     */
    interface OnLockSuccessListener {
        val lockResult: String
        fun onLockSuccess(pwd: String?)
    }

    var mLockSuccessListener: OnLockSuccessListener? = null
    fun setOnLockSuccessListener(lockSuccessListener: OnLockSuccessListener?) {
        mLockSuccessListener = lockSuccessListener
    }

    /**
     * 获取点
     */
    private val getPoint: Point?
        private get() {
            for (i in 0..2) {
                for (point in mPoints[i]) {
                    if (checkInRound(point!!.centerX.toFloat(), point.centerY.toFloat(),
                            mDotRadius.toFloat(), mMovingX, mMovingY)
                    ) {
                        return point
                    }
                }
            }
            return null
        }

    /**
     * 画箭头
     */
    private fun drawArrow(
        canvas: Canvas,
        paint: Paint?,
        start: Point,
        end: Point,
        arrowHeight: Float,
        angle: Int
    ) {
        if (paint == null) return
        //两点之间的距离
        val d = distance(start.centerX.toDouble(),
            start.centerY.toDouble(),
            end.centerX.toDouble(),
            end.centerY.toDouble())
        val sinB = ((end.centerX - start.centerX) / d).toFloat()
        val cosB = ((end.centerY - start.centerY) / d).toFloat()
        val tanA = Math.tan(Math.toRadians(angle.toDouble())).toFloat()
        val h = (d - arrowHeight - mDotRadius * 1.1).toFloat()
        val l = arrowHeight * tanA
        val a = l * sinB
        val b = l * cosB
        val x0 = h * sinB
        val y0 = h * cosB
        val x1 = start.centerX + (h + arrowHeight) * sinB
        val y1 = start.centerY + (h + arrowHeight) * cosB
        val x2 = start.centerX + x0 - b
        val y2 = start.centerY + y0 + a
        val x3 = start.centerX + x0 + b
        val y3 = start.centerY + y0 - a
        val path = Path()
        path.moveTo(x1, y1)
        path.lineTo(x2, y2)
        path.lineTo(x3, y3)
        path.close()
        canvas.drawPath(path, paint)
    }

    private fun drawLine(start: Point, end: Point, canvas: Canvas, paint: Paint?) {
        if (paint == null) return
        //两点之间的距离
        val pointDistance = distance(start.centerX.toDouble(),
            start.centerY.toDouble(),
            end.centerX.toDouble(),
            end.centerY.toDouble())
        val dx = end.centerX - start.centerX.toDouble()
        val dy = end.centerY - start.centerY.toDouble()
        val rx = (dx / pointDistance * (mDotRadius / 6.0)).toFloat()
        val ry = (dy / pointDistance * (mDotRadius / 6.0)).toFloat()
        canvas.drawLine(start.centerX + rx,
            start.centerY + ry,
            end.centerX - rx,
            end.centerY - ry,
            paint)
    }

    /**
     * 初始化点
     */
    private fun initDot() {
        //宽高
        var width = width
        var height = height
        val squareWidth = width / 3
        // 兼容横竖屏
        var offsetX = 0
        var offsetY = 0
        if (height > width) {
            //横屏
            offsetY = (height - width) / 2
            height = width
        } else {
            offsetX = (width - height) / 2
            width = height
        }
        mDotRadius = width / 12.toDouble()
        mPoints[0][0] = Point(offsetX + squareWidth / 2, offsetY + squareWidth / 2, 0)
        mPoints[0][1] = Point(offsetX + squareWidth * 3 / 2, offsetY + squareWidth / 2, 1)
        mPoints[0][2] = Point(offsetX + squareWidth * 5 / 2, offsetY + squareWidth / 2, 2)
        mPoints[1][0] = Point(offsetX + squareWidth / 2, offsetY + squareWidth * 3 / 2, 3)
        mPoints[1][1] = Point(offsetX + squareWidth * 3 / 2, offsetY + squareWidth * 3 / 2, 4)
        mPoints[1][2] = Point(offsetX + squareWidth * 5 / 2, offsetY + squareWidth * 3 / 2, 5)
        mPoints[2][0] = Point(offsetX + squareWidth / 2, offsetY + squareWidth * 5 / 2, 6)
        mPoints[2][1] = Point(offsetX + squareWidth * 3 / 2, offsetY + squareWidth * 5 / 2, 7)
        mPoints[2][2] = Point(offsetX + squareWidth * 5 / 2, offsetY + squareWidth * 5 / 2, 8)
    }

    /**
     * 初始化画笔
     */
    private fun initPaint() {
        // new Paint 对象 ，设置 paint 颜色
        // 线的画笔
        mLinePaint = Paint()
        mLinePaint.color = mInnerPressedColor
        mLinePaint.style = Paint.Style.STROKE
        mLinePaint.isAntiAlias = true
        mLinePaint.strokeWidth = (mDotRadius / 9.0f).toFloat()
        // 按下的画笔
        mPressedPaint = Paint()
        mPressedPaint.style = Paint.Style.STROKE
        mPressedPaint.isAntiAlias = true
        mPressedPaint.strokeWidth = (mDotRadius / 6.0f).toFloat()
        // 错误的画笔
        mErrorPaint = Paint()
        mErrorPaint.style = Paint.Style.STROKE
        mErrorPaint.isAntiAlias = true
        mErrorPaint.strokeWidth = (mDotRadius / 6.0f).toFloat()
        // 默认的画笔
        mNormalPaint = Paint()
        mNormalPaint.style = Paint.Style.STROKE
        mNormalPaint.isAntiAlias = true
        mNormalPaint.strokeWidth = (mDotRadius / 9.0f).toFloat()
        // 箭头的画笔
        mArrowPaint = Paint()
        mArrowPaint.color = mInnerPressedColor
        mArrowPaint.style = Paint.Style.FILL
        //抗锯齿
        mArrowPaint.isAntiAlias = true
    }

    private inner class Point(val centerX: Int, val centerY: Int, val index: Int) {
        val STATUS_NORMAL = 1
        val STATUS_PRESSED = 2
        val STATUS_ERROR = 3

        //当前点的状态，有三种状态
        var status = STATUS_NORMAL
    }

    fun hideArrow() {
        mIsHideArrow = true
        invalidate()
    }

    init {
        initAttrs(context, attrs)
    }
}