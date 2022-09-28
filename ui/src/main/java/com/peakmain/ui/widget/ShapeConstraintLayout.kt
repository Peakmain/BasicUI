package com.peakmain.ui.widget

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.peakmain.ui.R

/**
 * author ：Peakmain
 * createTime：2022/09/28
 * mail:2726449200@qq.com
 * describe：
 */
class ShapeConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
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

    private fun init(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ShapeConstraintLayout)
        mNormalBackgroundColor =
            ta.getColor(R.styleable.ShapeConstraintLayout_shapeClBackgroundColor, 0)
        mGradientDrawable = GradientDrawable()
        mRadius =
            ta.getDimensionPixelSize(R.styleable.ShapeConstraintLayout_shapeClRadius, 0).toFloat()
        mNormalStrokeColor = ta.getColor(R.styleable.ShapeConstraintLayout_shapeClStrokeColor, 0)
        mNormalStrokeWidth = ta.getColor(R.styleable.ShapeConstraintLayout_shapeClStrokeWidth, 0)
        mStartColor = ta.getColor(R.styleable.ShapeConstraintLayout_shapeClStartColor, 0)
        mEndColor = ta.getColor(R.styleable.ShapeConstraintLayout_shapeClEndColor, 0)
        val topLeftRadius: Int = ta.getDimensionPixelSize(
            R.styleable.ShapeConstraintLayout_shapeClTopLeftRadius, mRadius.toInt()
        )
        val topRightRadius: Int = ta.getDimensionPixelSize(
            R.styleable.ShapeConstraintLayout_shapeClTopRightRadius, mRadius.toInt()
        )
        val bottomLeftRadius: Int = ta.getDimensionPixelSize(
            R.styleable.ShapeConstraintLayout_shapeClBottomLeftRadius, mRadius.toInt()
        )
        val bottomRightRadius: Int = ta.getDimensionPixelSize(
            R.styleable.ShapeConstraintLayout_shapeClBottomRightRadius, mRadius.toInt()
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
        mGradientDrawable.setColor(mNormalBackgroundColor)
        if (mRadius != 0f) mGradientDrawable.cornerRadius = mRadius
        //设置边线
        if (mNormalStrokeWidth != 0 && mNormalStrokeColor != 0)
            mGradientDrawable.setStroke(mNormalStrokeWidth, mNormalStrokeColor)

        if(mStartColor!=0&&mEndColor!=0){
            mGradientDrawable.orientation=GradientDrawable.Orientation.LEFT_RIGHT
            mGradientDrawable.colors= intArrayOf(mStartColor,mEndColor)
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

    private fun setCornerRadii(radius: FloatArray) {
        mGradientDrawable.cornerRadii = radius
    }

    init {
        init(context, attrs)
    }


}