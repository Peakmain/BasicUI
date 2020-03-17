package com.peakmain.ui.loading;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.peakmain.ui.R;

/**
 * author ：Peakmain
 * createTime：2020/3/17
 * mail:2726449200@qq.com
 * describe：仿老版58同城加载loading
 */
public class ShapeView extends View {
    private Paint mPaint;
    private Shape mCurrentShape = Shape.Circle;
    private Path mPath;
    private int mCircleColor= Color.parseColor("#aa72d572");
    private int mRectColor=Color.parseColor("#aa738ffe");
    private int mTriangleColor=Color.parseColor("#aae84e40");

    public enum Shape {
        //三种形状
        Circle, Square, Triangle
    }

    public ShapeView(Context context) {
        this(context, null);
    }

    public ShapeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public ShapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ShapeView);
        mCircleColor=ta.getColor(R.styleable.ShapeView_svCircleColor,mCircleColor);
        mRectColor=ta.getColor(R.styleable.ShapeView_svRectColor,mRectColor);
        mTriangleColor=ta.getColor(R.styleable.ShapeView_svTriangleColor,mTriangleColor);
        ta.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //保证是正方形
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(Math.min(width, height), Math.min(width, height));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int center = getWidth() / 2;
        switch (mCurrentShape) {
            case Circle:
                mPaint.setColor(mCircleColor);
                canvas.drawCircle(center, center, center, mPaint);
                break;
            case Square:
                mPaint.setColor(mRectColor);
                canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
                break;
            case Triangle:
                //绘制等边三角形
                mPaint.setColor(mTriangleColor);
                if (mPath == null) {
                    mPath = new Path();
                    mPath.moveTo(center, 0);
                    mPath.lineTo(0, (float) (center * Math.sqrt(3)));
                    mPath.lineTo(getWidth(), (float) (center * Math.sqrt(3)));
                    mPath.close();
                }
                canvas.drawPath(mPath, mPaint);
                break;
            default:
                break;
        }
    }

    /**
     * 切换形状
     */
    public void exchange() {
        switch (mCurrentShape) {
            case Circle:
                mCurrentShape = Shape.Square;
                break;
            case Square:
                mCurrentShape = Shape.Triangle;
                break;
            case Triangle:
                mCurrentShape = Shape.Circle;
                break;
            default:
                break;
        }
        invalidate();
    }

    public Shape getCurrentShape() {
        return mCurrentShape;
    }
}
