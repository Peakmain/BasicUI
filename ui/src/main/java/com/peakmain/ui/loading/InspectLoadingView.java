package com.peakmain.ui.loading;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LinearInterpolator;

import com.peakmain.ui.R;

/**
 * author ：Peakmain
 * createTime：2020/3/17
 * mail:2726449200@qq.com
 * describe：视察动画
 */
public class InspectLoadingView extends View {
    // 旋转动画执行的时间
    private final long ROTATION_ANIMATION_TIME = 1400;
    private boolean mInitParams;
    private float mRotationRadius;//绕着旋转的动画的半径
    private float mCircleRadius;//小圆的半径
    private float mCurrentRotationAngle;
    // 小圆的颜色列表
    private int[] mCircleColors;
    //整体颜色背景
    private int mSplashColor = Color.WHITE;
    // 代表当前状态所画动画
    private LoadingState mLoadingState;
    //画笔
    private Paint mPaint;
    private int cententX, cententY;
    private float mCurrentRotationRadius;//当前半径
    // 空心圆初始半径
    private float mHoleRadius = 0F;
    // 屏幕对角线的一半
    private float mDiagonalDist;
    private boolean isShow;

    public InspectLoadingView(Context context) {
        this(context, null);
    }

    public InspectLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InspectLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCircleColors = context.getResources().getIntArray(R.array.inspect_circle_color);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isShow){
            if (!mInitParams) {
                initParams();
            }
            if (mLoadingState == null) {
                mLoadingState = new RotationState();
            }
            mLoadingState.drawable(canvas);
        }
    }

    public void show() {
        this.isShow=true;
        invalidate();
    }

    //初始化参数
    private void initParams() {
        mRotationRadius = getMeasuredWidth() / 4;
        // 每个小圆的半径 = 大圆半径的 1/8
        mCircleRadius = mRotationRadius / 8;
        mInitParams = true;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        cententX = getWidth() / 2;
        cententY = getHeight() / 2;
        mDiagonalDist = (float) Math.sqrt(cententX * cententX + cententY * cententY);
    }

    /**
     * 消失:给外部调用
     */
    public void hide() {
        //开始聚合动画
        //关闭动画
        if (mLoadingState instanceof RotationState) {
            RotationState rotationState = (RotationState) mLoadingState;
            rotationState.cancel();
        }
        mLoadingState = new MergeState();
    }


    public abstract class LoadingState {
        public abstract void drawable(Canvas canvas);
    }

    /**
     * 展开动画
     */
    public class ExpendState extends LoadingState {
        ValueAnimator mAnimator;

        public ExpendState() {
            mAnimator = ObjectAnimator.ofFloat(0, mDiagonalDist);//从0到圆的对角线的一半
            mAnimator.setDuration(ROTATION_ANIMATION_TIME);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mHoleRadius = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            mAnimator.start();
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if(animation!=null){
                        animation.cancel();
                    }
                }
            });
        }

        @Override
        public void drawable(Canvas canvas) {
            //画笔的宽度
            float strokeWidth = mDiagonalDist - mHoleRadius;
            mPaint.setStrokeWidth(strokeWidth);
            mPaint.setColor(mSplashColor);
            mPaint.setStyle(Paint.Style.STROKE);
            float radius = strokeWidth / 2 + mHoleRadius;
            canvas.drawCircle(cententX, cententY, radius, mPaint);
        }
    }

    /**
     * 聚合动画
     */
    public class MergeState extends LoadingState {
        ValueAnimator mAnimator;

        public MergeState() {
            //从外圆的半径到中心位置
            mAnimator = ObjectAnimator.ofFloat(mRotationRadius, 0);
            mAnimator.setDuration(ROTATION_ANIMATION_TIME / 2);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentRotationRadius = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            // 开始的时候向后然后向前甩
            mAnimator.setInterpolator(new AnticipateInterpolator(5f));
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if(animation!=null){
                        animation.cancel();
                    }
                    mLoadingState = new ExpendState();
                }
            });
            //不断重复使用
            //mAnimator.setRepeatCount(-1);
            mAnimator.start();
        }

        @Override
        public void drawable(Canvas canvas) {
            //绘制白色背景
            canvas.drawColor(mSplashColor);
            //画6个圆
            //没份的角度
            double precentAngle = 2 * Math.PI / mCircleColors.length;
            for (int i = 0; i < mCircleColors.length; i++) {
                mPaint.setColor(mCircleColors[i]);
                //当前的角度=初始化的角度+旋转的角度
                double currentAngle = precentAngle * i + mCurrentRotationAngle;
                float cx = (float) (cententX + mCurrentRotationRadius * Math.cos(currentAngle));
                float cy = (float) (cententY + mCurrentRotationRadius * Math.sin(currentAngle));
                canvas.drawCircle(cx, cy, mCircleRadius, mPaint);
            }
        }
    }

    /**
     * 旋转动画
     */
    public class RotationState extends LoadingState {
        ValueAnimator mAnimator;

        public RotationState() {
            //0-360度
            mAnimator = ObjectAnimator.ofFloat(0, 2 * (float) Math.PI);
            mAnimator.setDuration(ROTATION_ANIMATION_TIME);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentRotationAngle = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            //不断重复使用
            mAnimator.setRepeatCount(-1);
            mAnimator.start();
        }

        @Override
        public void drawable(Canvas canvas) {
            //绘制白色背景
            canvas.drawColor(mSplashColor);
            //画6个圆
            //没份的角度
            double precentAngle = 2 * Math.PI / mCircleColors.length;
            for (int i = 0; i < mCircleColors.length; i++) {
                mPaint.setColor(mCircleColors[i]);
                //当前的角度=初始化的角度+旋转的角度
                double currentAngle = precentAngle * i + mCurrentRotationAngle;
                float cx = (float) (cententX + mRotationRadius * Math.cos(currentAngle));
                float cy = (float) (cententY + mRotationRadius * Math.sin(currentAngle));
                canvas.drawCircle(cx, cy, mCircleRadius, mPaint);
            }
        }

        public void cancel() {
            mAnimator.cancel();
        }
    }

    public void setCircleColors(int[] circleColors) {
        mCircleColors = circleColors;
    }
}
