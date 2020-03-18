package com.peakmain.ui.widget.lock;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.peakmain.ui.R;
import com.peakmain.ui.utils.MathUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/3/15
 * mail:2726449200@qq.com
 * describe：锁屏view
 */
public class LockScreenView extends View {
    private boolean isInit = false;
    //二维数组初始化int[3][3]
    private Point[][] mPoints = new Point[3][3];
    //外圆的半径
    private double mDotRadius = 0;
    //画笔
    private Paint mLinePaint, mPressedPaint, mErrorPaint, mNormalPaint, mArrowPaint;
    //颜色
    private int mOuterPressedColor = 0xff8cbad8;
    private int mInnerPressedColor = 0xff0596f6;
    private int mOuterNormalColor = 0xffd9d9d9;
    private int mInnerNormalColor = 0xff929292;
    private int mOuterErrorColor = 0xff901032;
    private int mInnerErrorColor = 0xffea0945;
    private boolean isTouchPoint = false;
    // 选中的所有点
    private List<Point> mSelectPoints = new ArrayList<>();
    private boolean mIsHideArrow=false;

    public LockScreenView(Context context) {
        this(context, null);
    }

    public LockScreenView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LockScreenView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LockScreenView);
        mOuterNormalColor = ta.getColor(R.styleable.LockScreenView_lpNormalOutColor, mOuterNormalColor);
        mOuterPressedColor = ta.getColor(R.styleable.LockScreenView_lpPressOutColor, mOuterPressedColor);
        mOuterErrorColor = ta.getColor(R.styleable.LockScreenView_lpErrorOutColor, mOuterErrorColor);

        mInnerNormalColor = ta.getColor(R.styleable.LockScreenView_lpNormalInnerColor, mInnerNormalColor);
        mInnerPressedColor = ta.getColor(R.styleable.LockScreenView_lpPressInnerColor, mInnerPressedColor);
        mInnerErrorColor = ta.getColor(R.styleable.LockScreenView_lpErrorInnerColor, mInnerErrorColor);
        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //初始化九宫格，防止onDraw回调多次
        if (!isInit) {
            initPaint();
            initDot();
            isInit = true;
        }
        // 绘制九个宫格
        drawShow(canvas);
    }

    private void drawShow(Canvas canvas) {
        for (int i = 0; i < 3; i++) {
            for (Point point : mPoints[i]) {
                int status = point.getStatus();
                if (status == point.STATUS_NORMAL) {
                    //先绘制外圆
                    mNormalPaint.setColor(mOuterNormalColor);
                    canvas.drawCircle((float) point.centerX, (float) point.centerY, (float) mDotRadius, mNormalPaint);
                    //后绘制内圆
                    mNormalPaint.setColor(mInnerNormalColor);
                    canvas.drawCircle((float) point.centerX, (float) point.centerY, (float) mDotRadius / 6.0f, mNormalPaint);
                }
                if (status == point.STATUS_PRESSED) {
                    mPressedPaint.setColor(mOuterPressedColor);
                    canvas.drawCircle((float) point.centerX, (float) point.centerY, (float) mDotRadius, mPressedPaint);
                    mPressedPaint.setColor(mInnerPressedColor);
                    canvas.drawCircle((float) point.centerX, (float) point.centerY, (float) mDotRadius / 6, mPressedPaint);
                }
                if (status == point.STATUS_ERROR) {
                    mErrorPaint.setColor(mOuterErrorColor);
                    canvas.drawCircle((float) point.centerX, (float) point.centerY, (float) mDotRadius, mErrorPaint);
                    mErrorPaint.setColor(mInnerErrorColor);
                    canvas.drawCircle((float) point.centerX, (float) point.centerY, (float) mDotRadius / 6, mErrorPaint);

                }
            }
        }
        //绘制两个点之间的连线和箭头
        drawLine(canvas);
    }


    /**
     * 绘制两个点之间的连线以及箭头
     */
    private void drawLine(Canvas canvas) {
        if (mSelectPoints.size() >= 1) {
            //两个点之间绘制一条线
            Point lastPoint = mSelectPoints.get(0);
            for (int i = 0; i < mSelectPoints.size(); i++) {
                drawLine(lastPoint, mSelectPoints.get(i), canvas, mLinePaint);
                //两个点之间绘制箭头
                if(!mIsHideArrow){
                    drawArrow(canvas, mArrowPaint, lastPoint, mSelectPoints.get(i), (float) mDotRadius / 5, 38);
                }
                lastPoint = mSelectPoints.get(i);
            }
            // 绘制最后一个点到手指当前位置的连线
            // 如果手指在内圆里面就不要绘制
            boolean isInnerPoint = MathUtils.checkInRound(lastPoint.centerX, lastPoint.centerY, (float) (mDotRadius / 4.0), mMovingX, mMovingY);
            if (!isInnerPoint && isTouchPoint) {
                drawLine(lastPoint, new Point((int) mMovingX, (int) mMovingY, -1), canvas, mLinePaint);
            }
        }
    }

    // 手指触摸的位置
    private float mMovingX = 0f;
    private float mMovingY = 0f;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mMovingX = event.getX();
        mMovingY = event.getY();
        Point point;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                for (Point selectPoint : mSelectPoints) {
                    selectPoint.setStatus(selectPoint.STATUS_NORMAL);
                }
                mSelectPoints = new ArrayList<>();
                //判断手指是不是在一个圆里面
                point = getPoint();
                if (point != null) {
                    isTouchPoint = true;
                    mSelectPoints.add(point);
                    //改变当前的状态
                    point.setStatus(point.STATUS_PRESSED);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isTouchPoint) {
                    point = getPoint();
                    if (point != null) {
                        if (!mSelectPoints.contains(point)) {
                            mSelectPoints.add(point);
                        }
                        //改变当前的一个状态
                        point.setStatus(point.STATUS_PRESSED);
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isTouchPoint = false;
                StringBuilder sb = new StringBuilder();
                for (Point selectPoint : mSelectPoints) {
                    int index = selectPoint.index + 1;
                    sb.append(index);
                }
                if (mLockSuccessListener != null) {
                    String result = mLockSuccessListener.getLockResult();
                    if (result.equals(sb.toString())) {
                        mLockSuccessListener.onLockSuccess(result);
                        break;
                    }
                }
                for (Point selectPoint : mSelectPoints) {
                    selectPoint.setStatus(selectPoint.STATUS_ERROR);
                }
                invalidate();
                break;
            default:
                break;
        }

        return true;
    }

    /**
     *
     */
    public interface OnLockSuccessListener {
        String getLockResult();

        void onLockSuccess(String pwd);
    }

    public OnLockSuccessListener mLockSuccessListener;

    public void setOnLockSuccessListener(OnLockSuccessListener lockSuccessListener) {
        mLockSuccessListener = lockSuccessListener;
    }

    /**
     * 获取点
     */
    private Point getPoint() {
        for (int i = 0; i < 3; i++) {
            for (Point point : mPoints[i]) {
                if (MathUtils.checkInRound(point.centerX, point.centerY,
                        (float) mDotRadius, mMovingX, mMovingY)) {
                    return point;
                }
            }
        }
        return null;
    }

    /**
     * 画箭头
     */
    private void drawArrow(Canvas canvas, Paint paint, Point start, Point end, float arrowHeight, int angle) {
        //两点之间的距离
        double d = MathUtils.distance(start.centerX, start.centerY, end.centerX, end.centerY);
        float sinB = (float) ((end.centerX - start.centerX) / d);
        float cosB = (float) ((end.centerY - start.centerY) / d);
        float tanA = (float) Math.tan(Math.toRadians(angle));
        float h = (float) (d - arrowHeight - mDotRadius * 1.1);
        float l = arrowHeight * tanA;
        float a = l * sinB;
        float b = l * cosB;
        float x0 = h * sinB;
        float y0 = h * cosB;
        float x1 = start.centerX + (h + arrowHeight) * sinB;
        float y1 = start.centerY + (h + arrowHeight) * cosB;
        float x2 = start.centerX + x0 - b;
        float y2 = start.centerY + y0 + a;
        float x3 = start.centerX + x0 + b;
        float y3 = start.centerY + y0 - a;
        Path path = new Path();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawLine(Point start, Point end, Canvas canvas, Paint paint) {
        //两点之间的距离
        double pointDistance = MathUtils.distance(start.centerX, start.centerY, end.centerX, end.centerY);
        double dx = end.centerX - start.centerX;
        double dy = end.centerY - start.centerY;
        float rx = (float) (dx / pointDistance * (mDotRadius / 6.0));
        float ry = (float) (dy / pointDistance * (mDotRadius / 6.0));
        canvas.drawLine(start.centerX + rx, start.centerY + ry, end.centerX - rx, end.centerY - ry, paint);
    }

    /**
     * 初始化点
     */
    private void initDot() {
        //宽高
        int width = getWidth();
        int height = getHeight();
        int squareWidth = width / 3;
        // 兼容横竖屏
        int offsetX = 0;
        int offsetY = 0;
        if (height > width) {
            //横屏
            offsetY = (height - width) / 2;
            height = width;
        } else {
            offsetX = (width - height) / 2;
            width = height;
        }
        mDotRadius = width / 12;
        mPoints[0][0] = new Point(offsetX + squareWidth / 2, offsetY + squareWidth / 2, 0);
        mPoints[0][1] = new Point(offsetX + squareWidth * 3 / 2, offsetY + squareWidth / 2, 1);
        mPoints[0][2] = new Point(offsetX + squareWidth * 5 / 2, offsetY + squareWidth / 2, 2);
        mPoints[1][0] = new Point(offsetX + squareWidth / 2, offsetY + squareWidth * 3 / 2, 3);
        mPoints[1][1] = new Point(offsetX + squareWidth * 3 / 2, offsetY + squareWidth * 3 / 2, 4);
        mPoints[1][2] = new Point(offsetX + squareWidth * 5 / 2, offsetY + squareWidth * 3 / 2, 5);
        mPoints[2][0] = new Point(offsetX + squareWidth / 2, offsetY + squareWidth * 5 / 2, 6);
        mPoints[2][1] = new Point(offsetX + squareWidth * 3 / 2, offsetY + squareWidth * 5 / 2, 7);
        mPoints[2][2] = new Point(offsetX + squareWidth * 5 / 2, offsetY + squareWidth * 5 / 2, 8);
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        // new Paint 对象 ，设置 paint 颜色
        // 线的画笔
        mLinePaint = new Paint();
        mLinePaint.setColor(mInnerPressedColor);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth((float) (mDotRadius / 9.0f));
        // 按下的画笔
        mPressedPaint = new Paint();
        mPressedPaint.setStyle(Paint.Style.STROKE);
        mPressedPaint.setAntiAlias(true);
        mPressedPaint.setStrokeWidth((float) (mDotRadius / 6.0f));
        // 错误的画笔
        mErrorPaint = new Paint();
        mErrorPaint.setStyle(Paint.Style.STROKE);
        mErrorPaint.setAntiAlias(true);
        mErrorPaint.setStrokeWidth((float) (mDotRadius / 6.0f));
        // 默认的画笔
        mNormalPaint = new Paint();
        mNormalPaint.setStyle(Paint.Style.STROKE);
        mNormalPaint.setAntiAlias(true);
        mNormalPaint.setStrokeWidth((float) (mDotRadius / 9.0f));
        // 箭头的画笔
        mArrowPaint = new Paint();
        mArrowPaint.setColor(mInnerPressedColor);
        mArrowPaint.setStyle(Paint.Style.FILL);
        //抗锯齿
        mArrowPaint.setAntiAlias(true);
    }

    private class Point {
        private int centerX;
        private int centerY;
        private int index;
        private final int STATUS_NORMAL = 1;
        private final int STATUS_PRESSED = 2;
        private final int STATUS_ERROR = 3;
        //当前点的状态，有三种状态
        private int status = STATUS_NORMAL;

        private Point(int centerX, int centerY, int index) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.index = index;
        }

        private int getStatus() {
            return status;
        }

        private void setStatus(int status) {
            this.status = status;
        }
    }
    public void hideArrow(){
        this.mIsHideArrow=true;
        invalidate();
    }

}
