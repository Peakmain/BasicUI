package com.peakmain.ui.loading;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.peakmain.ui.R;
import com.peakmain.ui.dialog.AlertDialog;
import com.peakmain.ui.utils.SizeUtils;

/**
 * author ：Peakmain
 * createTime：2020/3/17
 * mail:2726449200@qq.com
 * describe：
 */
public class ShapeLoadingView extends LinearLayout {
    // 上面的形状
    private ShapeView mShapeView;
    // 中间的阴影
    private View mShadowView;
    private int mTranslationDistance = 0;
    // 动画执行的时间
    private final long ANIMATOR_DURATION = 350;
    // 是否停止动画
    private boolean mIsStopAnimator = false;
    private AlertDialog mDialog;
    private AlertDialog.Builder mBuilder;
    private TextView mTvShapeName;

    public ShapeLoadingView(Context context) {
        this(context, null);
    }

    public ShapeLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTranslationDistance = SizeUtils.dp2px(getContext(), 80);
        initLayout();
    }

    /**
     * 初始化加载布局
     */
    private void initLayout() {
        // 1. 记载写好的 ui_loading_view
        // 1.1 实例化View
        // View loadView = inflate(getContext(),R.layout.ui_loading_view,null);
        // 1.2 添加到该View
        // addView(loadView);
        // 找一下 插件式换肤资源加载的那一节的内容
        // this 代表把 ui_loading_view 加载到 LoadingView 中
        inflate(getContext(), R.layout.ui_shape_view, this);

        mShapeView = findViewById(R.id.shape_view);
        mShadowView = findViewById(R.id.shadow_view);
        mTvShapeName=findViewById(R.id.tv_shape_name);
        post(new Runnable() {
            @Override
            public void run() {
                // onResume 之后View绘制流程执行完毕之后（View的绘制流程源码分析那一章）
                startFallAnimator();
            }
        });
        // onCreate() 方法中执行 ，布局文件解析 反射创建实例
    }

    // 开始下落动画
    private void startFallAnimator() {
        if (mIsStopAnimator) {
            return;
        }
        // 动画作用在谁的身上
        // 下落位移动画
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mShapeView, "translationY", 0, mTranslationDistance);
        // 配合中间阴影缩小
        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(mShadowView, "scaleX", 1f, 0.3f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(ANIMATOR_DURATION);
        // 下落的速度应该是越来越快
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.playTogether(translationAnimator, scaleAnimator);
        animatorSet.start();
        // 下落完之后就上抛了，监听动画执行完毕
        // 是一种思想，在 Adapter 中的 BannerView 写过
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 改变形状
                mShapeView.exchange();
                // 下落完之后就上抛了
                startUpAnimator();
                // 开始旋转
            }
        });
    }

    /**
     * 开始执行上抛动画
     */
    private void startUpAnimator() {
        if (mIsStopAnimator) {
            return;
        }
        // 动画作用在谁的身上
        // 下落位移动画
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mShapeView, "translationY", mTranslationDistance, 0);
        // 配合中间阴影缩小
        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(mShadowView, "scaleX", 0.3f, 1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(ANIMATOR_DURATION);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.playTogether(translationAnimator, scaleAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 上抛完之后就下落了
                startFallAnimator();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                // 开始旋转
                startRotationAnimator();
            }
        });
        // 执行 -> 监听的 onAnimationStart 方法
        animatorSet.start();
    }

    /**
     * 上抛的时候需要旋转
     */
    private void startRotationAnimator() {
        ObjectAnimator rotationAnimator = null;
        switch (mShapeView.getCurrentShape()) {
            case Circle:
            case Square:
                // 180
                rotationAnimator = ObjectAnimator.ofFloat(mShapeView, "rotation", 0, 180);
                break;
            case Triangle:
                // 120
                rotationAnimator = ObjectAnimator.ofFloat(mShapeView, "rotation", 0, -120);
                break;
            default:
                break;
        }
        rotationAnimator.setDuration(ANIMATOR_DURATION);
        rotationAnimator.setInterpolator(new DecelerateInterpolator());
        rotationAnimator.start();
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
                    .setWidthAndHeight(SizeUtils.getScreenWidth(getContext()) * 2 / 3, SizeUtils.getScreenHeight(getContext()) / 3);
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

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(View.INVISIBLE);// 不要再去排放和计算，少走一些系统的源码（View的绘制流程）
        // 清理动画
        mShapeView.clearAnimation();
        mShadowView.clearAnimation();
        // 把LoadingView从父布局移除
        ViewGroup parent = (ViewGroup) getParent();
        if (parent != null) {
            parent.removeView(this);// 从父布局移除
            removeAllViews();// 移除自己所有的View
        }
        mIsStopAnimator = true;
    }
    public void setLoadingName(CharSequence name){
        mTvShapeName.setText(name);
        invalidate();
    }
}
