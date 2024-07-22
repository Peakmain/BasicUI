package com.peakmain.ui.widget

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
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
    //圆角的角度
    private var mRadius = 0f

    //stroke 线条 线条宽度
    private var mNormalStrokeWidth = 0

    //线条颜色
    private var mNormalStrokeColor = 0

    //背景颜色
    private var mNormalBackgroundColor = 0

    //默认shape样式
    private lateinit var mGradientDrawable: GradientDrawable

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
     * 是否开启点击后水波纹动画效果
     */
    private var isActiveMotion = false

    /**
     * 按下去的颜色
     */
    private var mPressedColor = 0
    private var mColorStateList: ColorStateList? = null
    private fun parseAttrs(attrs: AttributeSet?) {
        mGradientDrawable = GradientDrawable()
        val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeLinearLayout)

        //获取背景色
        mNormalBackgroundColor =
            a.getColor(R.styleable.ShapeLinearLayout_shapeLlBackgroundColor, mNormalBackgroundColor)
        //获取线条宽度
        mNormalStrokeWidth = a.getDimensionPixelSize(
            R.styleable.ShapeLinearLayout_shapeLlStrokeWidth,
            mNormalStrokeWidth
        )
        //获取线条颜色
        mNormalStrokeColor =
            a.getColor(R.styleable.ShapeLinearLayout_shapeLlStrokeColor, mNormalStrokeColor)
        //获取弧度
        mRadius = a.getDimensionPixelSize(R.styleable.ShapeLinearLayout_shapeLlRadius, 0).toFloat()
        //开始颜色
        mStartColor = a.getColor(R.styleable.ShapeLinearLayout_shapeLlStartColor, mStartColor)
        //结束颜色
        mEndColor = a.getColor(R.styleable.ShapeLinearLayout_shapeLlEndColor, mEndColor)
        //设置渐变方向
        mOrientation = a.getInt(R.styleable.ShapeLinearLayout_shapeLlOrientation, mOrientation)
        //形状，默认是矩形
        mShape = a.getInt(R.styleable.ShapeLinearLayout_shapeLlShape, mShape)
        //是否开启点击后水波纹效果
        isActiveMotion = a.getBoolean(R.styleable.ShapeLinearLayout_shapeLlActiveMotion, isActiveMotion)
        //按下去的颜色
        mPressedColor = a.getColor(R.styleable.ShapeLinearLayout_shapeLlPressedColor, mPressedColor)

        val topLeftRadius: Int = a.getDimensionPixelSize(
            R.styleable.ShapeLinearLayout_shapeLlTopLeftRadius, mRadius.toInt()
        )
        val topRightRadius: Int = a.getDimensionPixelSize(
            R.styleable.ShapeLinearLayout_shapeLlTopRightRadius, mRadius.toInt()
        )
        val bottomLeftRadius: Int = a.getDimensionPixelSize(
            R.styleable.ShapeLinearLayout_shapeLlBottomLeftRadius, mRadius.toInt()
        )
        val bottomRightRadius: Int = a.getDimensionPixelSize(
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
        gravity = Gravity.CENTER
        a.recycle()
    }

    /**
     * 设置背景色 以及线条宽度和颜色
     */
    private fun setStroke() {
        initGradientDrawable().setColor(mNormalBackgroundColor)//设置背景颜色
        isClickable = true
        // 是否开启点击动效
        if (isActiveMotion) {
            if (background == null) {
                //水波纹 5.0以上
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (mPressedColor == 0)
                        mPressedColor = -0x99999a
                    background = RippleDrawable(
                        ColorStateList.valueOf(mPressedColor),
                        mGradientDrawable,
                        null
                    )
                }
            }

        } else if (background == null) {
            if (mPressedColor == 0) {
                if (mColorStateList == null) {
                    background = mGradientDrawable
                    return
                }
            }
            if (mPressedColor == 0) return
            mColorStateList = ColorStateList(
                arrayOf(
                    intArrayOf(android.R.attr.state_pressed),
                    intArrayOf()
                ),
                intArrayOf(
                    mPressedColor,
                    mNormalBackgroundColor
                )
            )
            initGradientDrawable().apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    color = mColorStateList
                }
            }
            background = mGradientDrawable
        }

    }

    fun setPressedColor(pressedColor: Int) {
        mPressedColor = pressedColor
        mColorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_pressed),
                intArrayOf()
            ),
            intArrayOf(
                mPressedColor,
                mNormalBackgroundColor
            )
        )
        mGradientDrawable.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                color = mColorStateList
            }
        }
    }

    private fun initGradientDrawable(): GradientDrawable {
        return mGradientDrawable.apply {
            //设置边线
            setStroke(mNormalStrokeWidth, mNormalStrokeColor)

            //设置弧度
            if (mRadius != 0f) {
                cornerRadius = mRadius
            }
            if (mStartColor != 0 && mEndColor != 0) {
                orientation =
                    if (mOrientation == 0) GradientDrawable.Orientation.LEFT_RIGHT else GradientDrawable.Orientation.TOP_BOTTOM
                colors = intArrayOf(mStartColor, mEndColor)
            }
            when (mShape) {
                0 -> {
                    shape = GradientDrawable.RECTANGLE
                }

                1 -> {
                    shape = GradientDrawable.OVAL
                }

                2 -> {
                    shape = GradientDrawable.LINE
                }

                3 -> {
                    shape = GradientDrawable.RING
                }
            }
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
    private fun setCornerRadii(radius: FloatArray) {
        mGradientDrawable.cornerRadii = radius
    }


    //按压后的shape样式
    init {
        parseAttrs(attrs)
    }


}