package com.peakmain.ui.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;

import com.peakmain.ui.R;


/**
 * author ：Peakmain
 * createTime：2020/2/28
 * mail:2726449200@qq.com
 * describe：自定义TextView
 */
public class ShapeTextView extends AppCompatTextView {
    //圆角的角度
    private float mRadius;
    //stroke 线条 线条宽度
    private int mNormalStrokeWidth = 0;
    //线条颜色
    private int mNormalStrokeColor = 0;

    //背景颜色
    private int mNormalBackgroundColor = 0;
    //默认shape样式
    private GradientDrawable mGradientDrawable;
    //渐变开始颜色
    private int mStartColor = 0;
    //渐变结束颜色
    private int mEndColor = 0;
    /**
     * 0，GradientDrawable.Orientation.LEFT_RIGHT
     * 1是GradientDrawable.Orientation.TOP_BOTTOM
     */
    private int mOrientation = 0;
    /**
     * RECTANGLE=0, OVAL=1, LINE=2, RING=3
     */
    private int mShape = 0;
    /**
     * 按压shape样式
     */
    private GradientDrawable mPressedGradientDrawable = new GradientDrawable();
    /**
     * 是否开启点击后水波纹动画效果
     */
    private boolean isActiveMotion = false;
    /**
     * 按下去的颜色
     */
    private int mPressedColor = 0xFF666666;

    private StateListDrawable mStateListDrawable = new StateListDrawable();

    //按压后的shape样式
    public ShapeTextView(Context context) {
        this(context, null);
    }

    public ShapeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mGradientDrawable = new GradientDrawable();
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ShapeTextView);

        //获取背景色
        mNormalBackgroundColor = a.getColor(R.styleable.ShapeTextView_shapeTvBackgroundColor, mNormalBackgroundColor);
        //获取线条宽度
        mNormalStrokeWidth = a.getDimensionPixelSize(R.styleable.ShapeTextView_shapeTvStrokeWidth, mNormalStrokeWidth);
        //获取线条颜色
        mNormalStrokeColor = a.getColor(R.styleable.ShapeTextView_shapeTvStrokeColor, mNormalStrokeColor);
        //获取弧度
        mRadius = a.getDimensionPixelSize(R.styleable.ShapeTextView_shapeTvRadius, 0);
        //开始颜色
        mStartColor = a.getColor(R.styleable.ShapeTextView_shapeTvStartColor, mStartColor);
        //结束颜色
        mEndColor = a.getColor(R.styleable.ShapeTextView_shapeTvEndColor, mEndColor);
        //设置渐变方向
        mOrientation = a.getInt(R.styleable.ShapeTextView_shapeTvOriention, mOrientation);
        //形状，默认是矩形
        mShape = a.getInt(R.styleable.ShapeTextView_shapeTvShape, mShape);
        //是否开启点击后水波纹效果
        isActiveMotion = a.getBoolean(R.styleable.ShapeTextView_shapeTvActiveMotion, isActiveMotion);
        //按下去的颜色
        mPressedColor = a.getColor(R.styleable.ShapeTextView_shapeTvPressedColor, mPressedColor);
        setStroke();

        setGravity(Gravity.CENTER);
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Drawable[] drawables = getCompoundDrawables();
        //图片在文字左侧居中
        Drawable drawableLeft = drawables[0];
        int drawablePadding = getCompoundDrawablePadding();
        if (drawableLeft != null) {
            float textWidth = getPaint().measureText(getText().toString());
            int drawableWidth;
            drawableWidth = drawableLeft.getIntrinsicWidth();
            float paddingWidth = textWidth + drawableWidth + drawablePadding;
            setPadding(0, getPaddingTop(), (int) (getWidth() - paddingWidth), getPaddingBottom());
            //设置偏移
            canvas.translate((getWidth() - paddingWidth) / 2, 0);
        }
        //图片在文字右侧居中
        Drawable drawableRight = drawables[2];
        if (drawableRight != null) {
            float textWidth = getPaint().measureText(getText().toString());
            int drawableWidth;
            drawableWidth = drawableRight.getIntrinsicWidth();
            float paddingWidth = textWidth + drawableWidth + drawablePadding;
            setPadding(0, getPaddingTop(), (int) (getWidth() - paddingWidth), getPaddingBottom());
            canvas.translate((getWidth() - paddingWidth) / 2, 0);
        }
        super.onDraw(canvas);
    }

    /**
     * 设置背景色 以及线条宽度和颜色
     */
    private void setStroke() {
        //设置边线
        mGradientDrawable.setStroke(mNormalStrokeWidth, mNormalStrokeColor);
        //设置背景颜色
        mGradientDrawable.setColor(mNormalBackgroundColor);
        //设置弧度
        mGradientDrawable.setCornerRadius(mRadius);
        if (mStartColor != 0 && mEndColor != 0) {
            if (mOrientation == 0)
                mGradientDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
            else
                mGradientDrawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
            mGradientDrawable.setColors(new int[]{mStartColor, mEndColor});
        }

        if (mShape == 0) {
            mGradientDrawable.setShape(GradientDrawable.RECTANGLE);
        } else if (mShape == 1) {
            mGradientDrawable.setShape(GradientDrawable.OVAL);
        } else if (mShape == 2) {
            mGradientDrawable.setShape(GradientDrawable.LINE);
        } else if (mShape == 3) {
            mGradientDrawable.setShape(GradientDrawable.RING);
        }
        // 是否开启点击动效
        if (isActiveMotion) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //水波纹 5.0以上
                setBackground(new RippleDrawable(ColorStateList.valueOf(mPressedColor), mGradientDrawable, null));
            } else {
                mPressedGradientDrawable.setColor(mPressedColor);
                if (mShape == 0) {
                    mPressedGradientDrawable.setShape(GradientDrawable.RECTANGLE);
                } else if (mShape == 1) {
                    mPressedGradientDrawable.setShape(GradientDrawable.OVAL);
                } else if (mShape == 2) {
                    mPressedGradientDrawable.setShape(GradientDrawable.LINE);
                } else if (mShape == 3) {
                    mPressedGradientDrawable.setShape(GradientDrawable.RING);
                }
                mPressedGradientDrawable.setCornerRadius(mRadius);
                mPressedGradientDrawable.setStroke(mNormalStrokeWidth, mNormalStrokeColor);
                mStateListDrawable.addState(new int[]{android.R.attr.state_pressed}, mPressedGradientDrawable);
                mStateListDrawable.addState(new int[]{}, mGradientDrawable);
                setBackground(mStateListDrawable);
            }
        } else {
            setBackground(mGradientDrawable);
        }

        // 可点击
        if (isActiveMotion) {
            setClickable(true);
        }

    }

    /**
     * 设置背景色
     *
     * @param normalBackgroundColor 背景颜色
     */
    public void setNormalBackgroundColor(int normalBackgroundColor) {
        mNormalBackgroundColor = normalBackgroundColor;
        setStroke();
    }

    /**
     * 设置线条宽度
     *
     * @param normalStrokeWidth 线条的宽度
     */
    public void setNormalStrokeWidth(int normalStrokeWidth) {
        mNormalStrokeWidth = normalStrokeWidth;
        setStroke();
    }

    /**
     * 设置线条颜色
     *
     * @param normalStrokeColor 线条的颜色
     */
    public void setNormalStrokeColor(int normalStrokeColor) {
        mNormalStrokeColor = normalStrokeColor;
        setStroke();
    }

    public void setRadius(float radius) {
        mRadius = radius;
        setStroke();
    }
}
