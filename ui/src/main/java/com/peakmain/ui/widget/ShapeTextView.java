package com.peakmain.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
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
    //类似与Xml布局里的shape
    private GradientDrawable mGradientDrawable;

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
        mNormalBackgroundColor = a.getColor(R.styleable.ShapeTextView_shapeTvBackgroundColor, 0);
        //获取线条宽度
        mNormalStrokeWidth = a.getDimensionPixelSize(R.styleable.ShapeTextView_shapeTvStrokeWidth, 0);
        //获取线条颜色
        mNormalStrokeColor = a.getColor(R.styleable.ShapeTextView_shapeTvStrokeColor, 0);
        //获取弧度
        mRadius = a.getDimensionPixelSize(R.styleable.ShapeTextView_shapeTvRadius, 0);
        setStroke();
        setBackgroundDrawable(mGradientDrawable);
        setGravity(Gravity.CENTER);
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Drawable[] drawables = getCompoundDrawables();
        //图片在文字左侧居中
        Drawable drawableLeft = drawables[0];
        if (drawableLeft != null) {
            float textWidth = getPaint().measureText(getText().toString());
            int drawablePadding = getCompoundDrawablePadding();
            int drawableWidth;
            drawableWidth = drawableLeft.getIntrinsicWidth();
            float paddingWidth = textWidth + drawableWidth + drawablePadding;
            setPadding(0, getPaddingTop(), (int) (getWidth() - paddingWidth), getPaddingBottom());
            //设置偏移
            canvas.translate((getWidth() - paddingWidth) / 2, 0);
        }
        //图片在文字顶部居中
        Drawable drawableTop = drawables[1];
        if (drawableTop != null) {
            float textHeight = getPaint().measureText(getText().toString());
            int drawablePadding = getCompoundDrawablePadding();
            int drawableHeight;
            drawableHeight = drawableTop.getIntrinsicHeight();
            float paddingHeight = textHeight + drawableHeight + drawablePadding;
            setPadding(getPaddingLeft(), 0, getPaddingRight(), (int) (getHeight() - paddingHeight));
            canvas.translate(0, (getHeight() - paddingHeight) / 2);
        }
        //图片在文字右侧居中
        Drawable drawableRight = drawables[2];
        if (drawableRight != null) {
            float textWidth = getPaint().measureText(getText().toString());
            int drawablePadding = getCompoundDrawablePadding();
            int drawableWidth;
            drawableWidth = drawableRight.getIntrinsicWidth();
            float paddingWidth = textWidth + drawableWidth + drawablePadding;
            setPadding(0, getPaddingTop(), (int) (getWidth() - paddingWidth), getPaddingBottom());
            canvas.translate((getWidth() - paddingWidth) / 2, 0);
        }
        // 图片在文字底部居中
        Drawable drawableBottom= drawables[3];
        if (drawableBottom != null) {
            float textHeight = getPaint().measureText(getText().toString());
            int drawablePadding = getCompoundDrawablePadding();
            int drawableHeight;
            drawableHeight = drawableBottom.getIntrinsicHeight();
            float paddingHeight = textHeight + drawableHeight + drawablePadding;
            setPadding(getPaddingLeft(),0,getPaddingRight(), (int)(getHeight() - paddingHeight));
            canvas.translate(0,(getHeight() - paddingHeight) / 2);
        }
        super.onDraw(canvas);
    }

    /**
     * 设置背景色 以及线条宽度和颜色
     */
    private void setStroke() {
        //线条
        mGradientDrawable.setStroke(mNormalStrokeWidth, mNormalBackgroundColor);
        mGradientDrawable.setCornerRadius(mRadius);
        mGradientDrawable.setColor(mNormalBackgroundColor);
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
    }
}
