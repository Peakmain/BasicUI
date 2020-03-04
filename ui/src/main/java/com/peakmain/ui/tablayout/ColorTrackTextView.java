package com.peakmain.ui.tablayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.peakmain.ui.R;

/**
 * author: peakmain
 * createdata：2019/7/12
 * mail: 2726449200@qq.com
 * desiption:
 */
public class ColorTrackTextView extends AppCompatTextView {
    // 1. 实现一个文字两种颜色 - 绘制不变色字体的画笔,原始的颜色
    private Paint mOriginPaint;
    // 2. 实现一个文字两种颜色 - 绘制变色字体的画笔,变色的颜色
    private Paint mChangePaint;
    //当前的进度
    private float mCurrentProgress = 0.0f;
    // 3.实现不同朝向
    private Direction mDirection = Direction.LEFT_TO_RIGHT;
    //4.默认不显示下划线
    private boolean mIsShowUnderLine;

    public enum Direction {
        //从左到右
        LEFT_TO_RIGHT,
        //从右到左
        RIGHT_TO_LEFT
    }

    public ColorTrackTextView(Context context) {
        this(context, null);
    }

    public ColorTrackTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorTrackTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ColorTrackTextView);
        int changeColor = ta.getColor(R.styleable.ColorTrackTextView_changeTextColor, getTextColors().getDefaultColor());
        int originColor = ta.getColor(R.styleable.ColorTrackTextView_originTextColor, getTextColors().getDefaultColor());
        mIsShowUnderLine = ta.getBoolean(R.styleable.ColorTrackTextView_isShowTextUnderLine, false);
        mOriginPaint = getPaintByColor(originColor);
        mChangePaint = getPaintByColor(changeColor);
        ta.recycle();
    }

    /**
     * 通过颜色返回画笔
     */
    private Paint getPaintByColor(int color) {
        Paint paint = new Paint();
        //设置颜色
        paint.setColor(color);
        //设置抗锯齿
        paint.setAntiAlias(true);
        //设置防抖动
        paint.setDither(true);
        //设置字体的大小
        paint.setTextSize(getTextSize());
        return paint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        //中间值
        int middle = (int) (mCurrentProgress * getWidth());
        //从左到右绘制
        if (mDirection == Direction.LEFT_TO_RIGHT) {
            //绘制变色
            //左边是红色右边是蓝色
            drawText(canvas, mChangePaint, 0, middle);
            drawText(canvas, mOriginPaint, middle, getWidth());
        } //从右到左绘制,左边是蓝色，右边是红色
        else if (mDirection == Direction.RIGHT_TO_LEFT) {
            // 右边是红色左边是蓝色
            drawText(canvas, mChangePaint, getWidth() - middle, getWidth());
            // 绘制变色
            drawText(canvas, mOriginPaint, 0, getWidth() - middle);
        }
    }

    /**
     * 绘制Text
     */
    private void drawText(Canvas canvas, Paint paint, int start, int end) {
        canvas.save();
        //绘制不变色
        Rect rect = new Rect(start, 0, end, getHeight());
        canvas.clipRect(rect);
        String text = getText().toString();
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        //获取文字的宽度
        float x = getWidth() / 2 - bounds.width() / 2;
        //基线
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        int baseLine = dy + getHeight() / 2;
        canvas.drawText(text, x, baseLine, paint);
        if (mIsShowUnderLine && paint != mOriginPaint) {
            canvas.drawLine(start, getHeight(), end, getHeight(), paint);
        }

        canvas.restore();
    }

    /**
     * 设置方向
     */
    public void setDirection(Direction direction) {
        mDirection = direction;
    }

    /**
     * 设置当前进度
     */
    public void setCurrentProgress(float currentProgress) {
        mCurrentProgress = currentProgress;
        invalidate();
    }
    /**
     * 设置改变颜色
     */
    public void setChangeColor(int changeColor) {
        this.mChangePaint.setColor(changeColor);
    }

    /**
     * 设置原始的原色
     */
    public void setOriginColor(int originColor) {
        this.mOriginPaint.setColor(originColor);
    }
    /**
     * 设置是否显示下划线
     *
     * @param showUnderLine true代表显示 false代表不显示
     */
    public void setShowUnderLine(boolean showUnderLine) {
        mIsShowUnderLine = showUnderLine;
    }
}
