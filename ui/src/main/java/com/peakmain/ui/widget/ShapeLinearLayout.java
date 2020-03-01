package com.peakmain.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.peakmain.ui.R;


/**
 * author ：Peakmain
 * createTime：2020/3/1
 * mail:2726449200@qq.com
 * describe：自定义Linearlayout
 */
public class ShapeLinearLayout extends LinearLayout {
    private float mRadius = 0;

    //线条的颜色、宽度
    private int mNormalStrokeWidth = 0;
    private int mNormalStrokeColor = 0;

    //背景颜色
    private int mNormalBackgroundColor = 0;
    //渐变的资源
    private GradientDrawable mGradientDrawable;
    //渐变开始颜色
    private int mStartColor = 0;
    //渐变结束颜色
    private int mEndColor = 0;

    public ShapeLinearLayout(Context context) {
        this(context, null);
    }

    public ShapeLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化属性
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ShapeLinearLayout);
        mNormalBackgroundColor = ta.getColor(R.styleable.ShapeLinearLayout_shapeLlBackgroundColor, 0);
        mRadius = ta.getDimensionPixelSize(R.styleable.ShapeLinearLayout_shapeLlRadius, 0);
        mNormalStrokeColor = ta.getColor(R.styleable.ShapeLinearLayout_shapeLlStrokeColor, 0);
        mNormalStrokeWidth = ta.getDimensionPixelSize(R.styleable.ShapeLinearLayout_shapeLlStrokeWidth, 0);
        mStartColor = ta.getColor(R.styleable.ShapeLinearLayout_shapeLlStartColor, 0);
        mEndColor = ta.getColor(R.styleable.ShapeLinearLayout_shapeLlEndColor, 0);
        ta.recycle();

        mGradientDrawable = new GradientDrawable();
        setBackground(mGradientDrawable);
        setStroke();
    }

    /**
     * 设置边角背景及边线
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setStroke() {
        //设置背景色
        mGradientDrawable.setColor(mNormalBackgroundColor);
        //设置弧度
        mGradientDrawable.setCornerRadius(mRadius);
        //设置边线
        mGradientDrawable.setStroke(mNormalStrokeWidth, mNormalBackgroundColor);
        if (mStartColor != 0 && mEndColor != 0) {
           mGradientDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
           mGradientDrawable.setColors(new int[]{mStartColor,mEndColor});
        }
    }

    /**
     * 设置圆弧
     * @param radius 圆弧的角度
     */
    public void setRadius(float radius) {
        mRadius = radius;
        mGradientDrawable.setCornerRadius(mRadius);
    }
    /**
     * 设置线宽度
     * @param strokeWidth 线条的宽度
     */
    public void setNormalStrokeWidth(int strokeWidth) {
        mNormalStrokeWidth = strokeWidth;
        mGradientDrawable.setStroke(mNormalStrokeWidth,mNormalStrokeColor);
    }
    /**
     * 设置线的颜色
     * @param strokeColor 线条的颜色
     */
    public void setNormalStrokeColor(int strokeColor) {
        mNormalStrokeColor = strokeColor;
        mGradientDrawable.setStroke(mNormalStrokeWidth,mNormalStrokeColor);
    }
    /**
     * 设置背景色
     * @param backgroundColor 背景颜色
     */
    public void setNormalBackgroundColor(int backgroundColor) {
        this.mNormalBackgroundColor = backgroundColor;
        mGradientDrawable.setColor(mNormalBackgroundColor);
    }
}
