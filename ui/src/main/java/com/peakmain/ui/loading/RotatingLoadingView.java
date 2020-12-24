package com.peakmain.ui.loading;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.peakmain.ui.R;
import com.peakmain.ui.dialog.AlertDialog;
import com.peakmain.ui.utils.SizeUtils;

/**
 * author ：Peakmain
 * createTime：2020/3/17
 * mail:2726449200@qq.com
 * describe：视察动画
 */
public class RotatingLoadingView extends View {
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
    private RotationState mLoadingState;
    //画笔
    private Paint mPaint;
    private int cententX, cententY;
    private AlertDialog mDialog;
    private AlertDialog.Builder mBuilder;

    public RotatingLoadingView(Context context) {
        this(context, null);
    }

    public RotatingLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotatingLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCircleColors = context.getResources().getIntArray(R.array.rotation_loading_view);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!mInitParams) {
            initParams();
        }
        if (mLoadingState == null) {
            mLoadingState = new RotationState();
        }
        mLoadingState.drawable(canvas);
    }

    public void show() {
        if (mBuilder == null) {
            mBuilder = new AlertDialog.Builder(getContext())
                    .setContentView(this)
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            setVisibility(INVISIBLE);
                        }
                    })
                    .setCancelable(false)
                    .setWidthAndHeight(SizeUtils.getScreenWidth() * 2 / 3, SizeUtils.getScreenHeight() / 3);
        }
        mDialog = mBuilder.show();
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
    }

    /**
     * 消失:给外部调用
     */
    public void hide() {
        //开始聚合动画
        //关闭动画
        if (mDialog != null) {
            mLoadingState.cancel();
            mDialog.dismiss();
        }
        setVisibility(INVISIBLE);
    }


    public abstract class LoadingState {
        public abstract void drawable(Canvas canvas);
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
