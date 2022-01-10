package com.peakmain.ui.widget

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import com.peakmain.ui.R


/**
 * author ：Peakmain
 * createTime：2020/2/28
 * mail:2726449200@qq.com
 * describe：自定义TextView
 */
class ShapeTextView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    //圆角的角度
    private var mRadius = 0f

    //stroke 线条 线条宽度
    private var mNormalStrokeWidth = 0

    //线条颜色
    private var mNormalStrokeColor = 0

    //背景颜色
    private var mNormalBackgroundColor = 0

    //默认shape样式
    private var mGradientDrawable: GradientDrawable? = null

    //渐变开始颜色
    private var mStartColor = 0

    //渐变结束颜色
    private var mEndColor = 0

    /**
     * 0，GradientDrawable.Orientation.LEFT_RIGHT
     * 1是GradientDrawable.Orientation.TOP_BOTTOM
     */
    private var mOrientation = 0

    /**
     * RECTANGLE=0, OVAL=1, LINE=2, RING=3
     */
    private var mShape = 0

    /**
     * 按压shape样式
     */
    private val mPressedGradientDrawable = GradientDrawable()

    /**
     * 是否开启点击后水波纹动画效果
     */
    private var isActiveMotion = false

    /**
     * 按下去的颜色
     */
    private var mPressedColor = -0x99999a
    private val mStateListDrawable =
            StateListDrawable()

    private fun init(attrs: AttributeSet?) {
        mGradientDrawable = GradientDrawable()
        val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeTextView)

        //获取背景色
        mNormalBackgroundColor =
                a.getColor(R.styleable.ShapeTextView_shapeTvBackgroundColor, mNormalBackgroundColor)
        //获取线条宽度
        mNormalStrokeWidth = a.getDimensionPixelSize(
                R.styleable.ShapeTextView_shapeTvStrokeWidth,
                mNormalStrokeWidth
        )
        //获取线条颜色
        mNormalStrokeColor =
                a.getColor(R.styleable.ShapeTextView_shapeTvStrokeColor, mNormalStrokeColor)
        //获取弧度
        mRadius = a.getDimensionPixelSize(R.styleable.ShapeTextView_shapeTvRadius, 0).toFloat()
        //开始颜色
        mStartColor = a.getColor(R.styleable.ShapeTextView_shapeTvStartColor, mStartColor)
        //结束颜色
        mEndColor = a.getColor(R.styleable.ShapeTextView_shapeTvEndColor, mEndColor)
        //设置渐变方向
        mOrientation = a.getInt(R.styleable.ShapeTextView_shapeTvOriention, mOrientation)
        //形状，默认是矩形
        mShape = a.getInt(R.styleable.ShapeTextView_shapeTvShape, mShape)
        //是否开启点击后水波纹效果
        isActiveMotion = a.getBoolean(R.styleable.ShapeTextView_shapeTvActiveMotion, isActiveMotion)
        //按下去的颜色
        mPressedColor = a.getColor(R.styleable.ShapeTextView_shapeTvPressedColor, mPressedColor)

        val topLeftRadius: Int = a.getDimensionPixelSize(
                R.styleable.ShapeTextView_topLeftRadius, mRadius.toInt())
        val topRightRadius: Int = a.getDimensionPixelSize(
                R.styleable.ShapeTextView_topRightRadius, mRadius.toInt())
        val bottomLeftRadius: Int = a.getDimensionPixelSize(
                R.styleable.ShapeTextView_bottomLeftRadius, mRadius.toInt())
        val bottomRightRadius: Int = a.getDimensionPixelSize(
                R.styleable.ShapeTextView_bottomRightRadius, mRadius.toInt())
        if (topLeftRadius != mRadius.toInt() || topRightRadius != mRadius.toInt() || bottomLeftRadius != mRadius.toInt() || bottomRightRadius != mRadius.toInt()) {
            setCornerRadii(floatArrayOf(
                    topLeftRadius.toFloat(), topLeftRadius.toFloat(),
                    topRightRadius.toFloat(), topRightRadius.toFloat(),
                    bottomRightRadius.toFloat(), bottomRightRadius.toFloat(),
                    bottomLeftRadius.toFloat(), bottomLeftRadius
                    .toFloat()))
        }
        setStroke()
        gravity = Gravity.CENTER
        a.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        val drawables: Array<Drawable> = compoundDrawables
        //图片在文字左侧居中
        val drawableLeft = drawables[0]
        val drawablePadding: Int = compoundDrawablePadding
        if (drawableLeft != null) {
            val textWidth: Float = paint.measureText(text.toString())
            val drawableWidth: Int = drawableLeft.intrinsicWidth
            val paddingWidth = textWidth + drawableWidth + drawablePadding
            setPadding(0, paddingTop, (width - paddingWidth).toInt(), paddingBottom)
            //设置偏移
            canvas.translate((width - paddingWidth) / 2, 0f)
        }
        //图片在文字右侧居中
        val drawableRight = drawables[2]
        if (drawableRight != null) {
            val textWidth = paint.measureText(text.toString())
            val drawableWidth = drawableRight.intrinsicWidth
            val paddingWidth = textWidth + drawableWidth + drawablePadding
            setPadding(0, paddingTop, (width - paddingWidth).toInt(), paddingBottom)
            canvas.translate((width - paddingWidth) / 2, 0F)
        }
        super.onDraw(canvas)
    }

    /**
     * 设置背景色 以及线条宽度和颜色
     */
    private fun setStroke() {
        //设置边线
        mGradientDrawable!!.setStroke(mNormalStrokeWidth, mNormalStrokeColor)
        //设置背景颜色
        mGradientDrawable!!.setColor(mNormalBackgroundColor)
        //设置弧度
        mGradientDrawable!!.cornerRadius = mRadius
        if (mStartColor != 0 && mEndColor != 0) {
            if (mOrientation == 0) mGradientDrawable!!.orientation =
                    GradientDrawable.Orientation.LEFT_RIGHT else mGradientDrawable!!.orientation =
                    GradientDrawable.Orientation.TOP_BOTTOM
            mGradientDrawable!!.colors = intArrayOf(mStartColor, mEndColor)
        }
        if (mShape == 0) {
            mGradientDrawable!!.shape = GradientDrawable.RECTANGLE
        } else if (mShape == 1) {
            mGradientDrawable!!.shape = GradientDrawable.OVAL
        } else if (mShape == 2) {
            mGradientDrawable!!.shape = GradientDrawable.LINE
        } else if (mShape == 3) {
            mGradientDrawable!!.shape = GradientDrawable.RING
        }
        // 是否开启点击动效
        if (isActiveMotion) {
            if (background == null) {
                //水波纹 5.0以上
                background = RippleDrawable(
                        ColorStateList.valueOf(mPressedColor),
                        mGradientDrawable,
                        null
                )
            }

        } else {
            if (background == null)
                background = mGradientDrawable
        }

        // 可点击
        if (isActiveMotion) {
            isClickable = true
        }
    }

    /**
     * 设置背景色
     *
     * @param normalBackgroundColor 背景颜色
     */
    fun setNormalBackgroundColor(normalBackgroundColor: Int) {
        mNormalBackgroundColor = normalBackgroundColor
        background = null
        setStroke()
    }

    /**
     * 设置线条宽度
     *
     * @param normalStrokeWidth 线条的宽度
     */
    fun setNormalStrokeWidth(normalStrokeWidth: Int) {
        mNormalStrokeWidth = normalStrokeWidth
        setStroke()
    }

    /**
     * 设置线条颜色
     *
     * @param normalStrokeColor 线条的颜色
     */
    fun setNormalStrokeColor(normalStrokeColor: Int) {
        mNormalStrokeColor = normalStrokeColor
        setStroke()
    }

    fun setRadius(radius: Float) {
        mRadius = radius
        setStroke()
    }
    /**
     * 设置四周的圆角
     */
    private fun setCornerRadii( radius: FloatArray) {
        mGradientDrawable!!.cornerRadii = radius
    }
    
    
    //按压后的shape样式
    init {
        init(attrs)
    }
    
    
}