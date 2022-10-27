package com.peakmain.ui.widget

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.LinearLayout
import com.peakmain.ui.R

/**
 * author ：Peakmain
 * createTime：2020/3/1
 * mail:2726449200@qq.com
 * describe：自定义LinearLayout
 */
class ShapeLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var mRadius = 0f

    //线条的颜色、宽度
    private var mNormalStrokeWidth = 0
    private var mNormalStrokeColor = 0

    //背景颜色
    private var mNormalBackgroundColor = 0

    //渐变的资源
    private lateinit var mGradientDrawable: GradientDrawable

    //渐变开始颜色
    private var mStartColor = 0

    //渐变结束颜色
    private var mEndColor = 0

    /**
     * 初始化属性
     */
    private fun init(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ShapeLinearLayout)
        mNormalBackgroundColor =
            ta.getColor(R.styleable.ShapeLinearLayout_shapeLlBackgroundColor, 0)
        mGradientDrawable = GradientDrawable()
        mRadius = ta.getDimensionPixelSize(R.styleable.ShapeLinearLayout_shapeLlRadius, 0).toFloat()
        mNormalStrokeColor = ta.getColor(R.styleable.ShapeLinearLayout_shapeLlStrokeColor, 0)
        mNormalStrokeWidth =
            ta.getDimensionPixelSize(R.styleable.ShapeLinearLayout_shapeLlStrokeWidth, 0)
        mStartColor = ta.getColor(R.styleable.ShapeLinearLayout_shapeLlStartColor, 0)
        mEndColor = ta.getColor(R.styleable.ShapeLinearLayout_shapeLlEndColor, 0)
        val topLeftRadius: Int = ta.getDimensionPixelSize(
            R.styleable.ShapeLinearLayout_shapeLlTopLeftRadius, mRadius.toInt()
        )
        val topRightRadius: Int = ta.getDimensionPixelSize(
            R.styleable.ShapeLinearLayout_shapeLlTopRightRadius, mRadius.toInt()
        )
        val bottomLeftRadius: Int = ta.getDimensionPixelSize(
            R.styleable.ShapeLinearLayout_shapeLlBottomLeftRadius, mRadius.toInt()
        )
        val bottomRightRadius: Int = ta.getDimensionPixelSize(
            R.styleable.ShapeLinearLayout_shapeLlBottomRightRadius, mRadius.toInt()
        )
        if (topLeftRadius != mRadius.toInt() || topRightRadius != mRadius.toInt() || bottomLeftRadius != mRadius.toInt() || bottomRightRadius != mRadius.toInt()) {
            setCornerRadii(
                floatArrayOf(
                    topLeftRadius.toFloat(), topLeftRadius.toFloat(),
                    topRightRadius.toFloat(), topRightRadius.toFloat(),
                    bottomRightRadius.toFloat(), bottomRightRadius.toFloat(),
                    bottomLeftRadius.toFloat(), bottomLeftRadius
                        .toFloat()
                )
            )
        }
        ta.recycle()
        background = mGradientDrawable


        setStroke()
    }

    /**
     * 设置边角背景及边线
     */
    private fun setStroke() {
        //设置背景色
        mGradientDrawable.setColor(mNormalBackgroundColor)
        //设置弧度
        if (mRadius != 0f) {
            mGradientDrawable.cornerRadius = mRadius
        }
        //设置边线
        mGradientDrawable.setStroke(mNormalStrokeWidth, mNormalBackgroundColor)
        if (mStartColor != 0 && mEndColor != 0) {
            mGradientDrawable.orientation = GradientDrawable.Orientation.LEFT_RIGHT
            mGradientDrawable.colors = intArrayOf(mStartColor, mEndColor)
        }
    }

    /**
     * 设置圆弧
     * @param radius 圆弧的角度
     */
    fun setRadius(radius: Float) {
        mRadius = radius
        mGradientDrawable.cornerRadius = mRadius
        setStroke()
    }

    /**
     * 设置线宽度
     * @param strokeWidth 线条的宽度
     */
    fun setNormalStrokeWidth(strokeWidth: Int) {
        mNormalStrokeWidth = strokeWidth
        mGradientDrawable.setStroke(mNormalStrokeWidth, mNormalStrokeColor)
        setStroke()
    }

    /**
     * 设置线的颜色
     * @param strokeColor 线条的颜色
     */
    fun setNormalStrokeColor(strokeColor: Int) {
        mNormalStrokeColor = strokeColor
        mGradientDrawable.setStroke(mNormalStrokeWidth, mNormalStrokeColor)
        setStroke()
    }

    /**
     * 设置背景色
     * @param backgroundColor 背景颜色
     */
    fun setNormalBackgroundColor(backgroundColor: Int) {
        mNormalBackgroundColor = backgroundColor
        mGradientDrawable.setColor(mNormalBackgroundColor)
        setStroke()
    }

    /**
     * 设置四周的圆角
     */
    private fun setCornerRadii(radius: FloatArray) {
        mGradientDrawable.cornerRadii = radius
    }

    init {
        init(context, attrs)
    }
}