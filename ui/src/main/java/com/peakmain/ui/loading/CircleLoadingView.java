package com.peakmain.ui.loading;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.peakmain.ui.BuildConfig;
import com.peakmain.ui.dialog.AlertDialog;
import com.peakmain.ui.utils.SizeUtils;

/**
 * author ：Peakmain
 * createTime：2020/3/4
 * mail:2726449200@qq.com
 * describe：圆形加载的loading
 */
public class CircleLoadingView extends RelativeLayout {
    //三个view
    private CircleView mLeftView, mMiddleView, mRightView;
    //距离
    private int mTranslationDistance = 30;
    //时间
    private final long ANIMATION_DRUATION = 350;
    //默认false，只有销毁的时候为true
    private boolean isStopAnimation = false;
    private AlertDialog mDialog;
    private AlertDialog.Builder mBuilder;

    public CircleLoadingView(Context context) {
        this(context, null);
    }

    public CircleLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //设置相隔距离
        mTranslationDistance = SizeUtils.dp2px(mTranslationDistance);
        //设置背景
        setBackgroundColor(Color.WHITE);
        //绘制左边的圆形
        mLeftView = getCircleView();
        mLeftView.exchangeColor(Color.BLUE);
        //绘制右边的圆
        mRightView = getCircleView();
        mRightView.exchangeColor(Color.GREEN);
        //绘制中间的圆
        mMiddleView = getCircleView();
        mMiddleView.exchangeColor(Color.RED);
        //添加圆
        addView(mLeftView);
        addView(mRightView);
        addView(mMiddleView);

        post(new Runnable() {
            @Override
            public void run() {
                expendAnimation();
            }
        });
    }

    //展开动画
    private void expendAnimation() {
        //如果停止动画就直接返回
        if (isStopAnimation) {
            return;
        }
        //开启左边位移动画
        ObjectAnimator leftTranslationAnimator = ObjectAnimator.ofFloat(mLeftView, "translationX", 0, -mTranslationDistance);
        //开启右边位移动画
        ObjectAnimator rightTranslatioAnimation = ObjectAnimator.ofFloat(mRightView, "translationX", 0, mTranslationDistance);
        //两个动画一起
        AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIMATION_DRUATION);
        set.playTogether(leftTranslationAnimator, rightTranslatioAnimation);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //动画结束之后往里面跑
                innerAnimation();
            }
        });
        set.start();
    }

    private void innerAnimation() {
        if (isStopAnimation) {
            return;
        }
        //开启左边位移动画
        ObjectAnimator leftTranslationAnimator = ObjectAnimator.ofFloat(mLeftView, "translationX", -mTranslationDistance, 0);
        //开启右边位移动画
        ObjectAnimator rightTranslatioAnimation = ObjectAnimator.ofFloat(mRightView, "translationX", mTranslationDistance, 0);
        //两个动画一起
        AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIMATION_DRUATION);
        set.playTogether(leftTranslationAnimator, rightTranslatioAnimation);
        set.setInterpolator(new AccelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //切换颜色 左边的给中间 中间的给右边  右边的给左边
                int mLeftViewColor = mLeftView.getColor();
                int mRightViewColor = mRightView.getColor();
                int mMiddleViewColor = mMiddleView.getColor();
                mLeftView.exchangeColor(mRightViewColor);
                mRightView.exchangeColor(mMiddleViewColor);
                mMiddleView.exchangeColor(mLeftViewColor);
                expendAnimation();
            }
        });
        set.start();
    }

    private CircleView getCircleView() {
        CircleView circleView = new CircleView(getContext());
        LayoutParams params = new LayoutParams(SizeUtils.dp2px( 10), SizeUtils.dp2px( 10));
        params.addRule(CENTER_IN_PARENT);
        circleView.setLayoutParams(params);
        return circleView;
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(INVISIBLE);
        //清理所有动画
        mLeftView.clearAnimation();
        mRightView.clearAnimation();
        ViewGroup parent = (ViewGroup) getParent();
        if (parent != null) {
            //父布局中移除当前view
            parent.removeView(mLeftView);
            parent.removeView(mRightView);
            //移除自己的
            removeAllViews();
        }
        isStopAnimation = true;
        Log.e("TAG", "动画取消");
    }

    /**
     * 显示loading
     */
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
                    .setWidthAndHeight(SizeUtils.getScreenWidth() / 2, SizeUtils.getScreenHeight() / 5);
        }
        mDialog = mBuilder.show();
    }

    /**
     * 隐藏loading
     */
    public void hide() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
        setVisibility(INVISIBLE);
    }

    /**
     * 设置是否可以取消
     */
    public CircleLoadingView setCancelable(boolean flag) {
        if (mDialog != null) {
            mDialog.setCancelable(flag);
        }
        return this;
    }

    /**
     * 设置宽高
     */
    public CircleLoadingView setWidthAndHeight(int width, int height) {
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
                    .setWidthAndHeight(width, height);
        } else {
            Log.e(BuildConfig.TAG, "Please call Method before show Method");
        }
        return this;
    }
}
