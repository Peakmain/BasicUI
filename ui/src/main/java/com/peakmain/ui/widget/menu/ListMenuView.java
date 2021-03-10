package com.peakmain.ui.widget.menu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.peakmain.ui.adapter.menu.BaseMenuAdapter;
import com.peakmain.ui.adapter.menu.MenuObserver;

/**
 * author ：Peakmain
 * createTime：2020/3/4
 * mail:2726449200@qq.com
 * describe：
 */
public class ListMenuView extends LinearLayout implements View.OnClickListener {

    private Context mContext;
    // 1.1 创建头部用来存放 Tab
    private LinearLayout mMenuTabView;
    // 1.2 创建 FrameLayout 用来存放 = 阴影（View） + 菜单内容布局(FrameLayout)
    private FrameLayout mMenuMiddleView;
    // 阴影
    private View mShadowView;
    // 创建菜单用来存放菜单内容
    private FrameLayout mMenuContainerView;
    // 阴影的颜色
    private int mShadowColor = 0x88888888;
    private BaseMenuAdapter mAdapter;
    private int mMenuContainerHeight;

    private int DURATION_TIME = 350;
    private int mCurrentPosition = -1;//当前位置
    // 动画是否在执行
    private boolean mAnimatorExecute;

    public ListMenuView(Context context) {
        this(context, null);
    }

    public ListMenuView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListMenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initLayout();
    }

    /**
     * 初始化控件
     */
    private void initLayout() {
        //整体是个垂直布局
        setOrientation(VERTICAL);
        //创建头部用来存放Tab
        mMenuTabView = new LinearLayout(mContext);
        mMenuTabView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        addView(mMenuTabView);
        // 1.2 创建 FrameLayout 用来存放 = 阴影（View） + 菜单内容布局(FrameLayout)
        mMenuMiddleView = new FrameLayout(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        params.weight = 1;
        mMenuMiddleView.setLayoutParams(params);
        addView(mMenuMiddleView);
        // 创建阴影 可以不用设置 LayoutParams 默认就是 MATCH_PARENT ，MATCH_PARENT
        mShadowView = new View(mContext);
        mShadowView.setBackgroundColor(mShadowColor);
        mShadowView.setVisibility(GONE);
        mShadowView.setAlpha(0);
        mMenuMiddleView.addView(mShadowView);
        mShadowView.setOnClickListener(this);

        //创建菜单用来存放菜单内容
        mMenuContainerView = new FrameLayout(mContext);
        mMenuContainerView.setBackgroundColor(Color.WHITE);
        mMenuMiddleView.addView(mMenuContainerView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (mMenuContainerHeight == 0 && heightSize > 0) {
            ViewGroup.LayoutParams params = mMenuContainerView.getLayoutParams();
            mMenuContainerHeight = (int) (heightSize * 75f / 100);
            params.height = mMenuContainerHeight;
            mMenuContainerView.setLayoutParams(params);
            // 进来的时候阴影不显示 ，内容也是不显示的（把它移上去）
            mMenuContainerView.setTranslationY(-mMenuContainerHeight);
        }
    }
    /**
     * 具体的观察者
     */
    private class AdapterMenuObserver extends MenuObserver {

        @Override
        public void closeMenu() {
            ListMenuView.this.closeMenu();
        }
    }
    private AdapterMenuObserver mObserver;
    public void setAdapter(BaseMenuAdapter adapter) {
        if(mAdapter!=null&&mObserver!=null){
            mAdapter.unregisterDataSetObserver(mObserver);
        }
        this.mAdapter = adapter;
        if (adapter == null) {
            throw new RuntimeException("adapter is null");
        }
        mObserver=new AdapterMenuObserver();
        mAdapter.registerDataSetObserver(mObserver);
        int count = adapter.count;
        for (int i = 0; i < count; i++) {
            // 获取菜单的Tab
            View tabView = adapter.getView(i, mMenuTabView);
            LinearLayout.LayoutParams params = (LayoutParams) tabView.getLayoutParams();
            params.weight = 1;
            tabView.setLayoutParams(params);
            mMenuTabView.addView(tabView);
            //设置点击事件
            setTabClick(tabView, i);
            //获取菜单内容
            View menuView = mAdapter.getMenuView(i, mMenuContainerView);
            menuView.setVisibility(GONE);
            mMenuContainerView.addView(menuView);
        }
    }

    private void setTabClick(final View tabView, final int position) {
        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPosition == -1) {
                    openMenu(tabView, position);
                } else {
                    //已经打开了
                    if (mCurrentPosition == position) {//如果点击的是同一个位置则关闭
                        closeMenu();
                    } else {
                        //切换显示
                        View currentMenu = mMenuContainerView.getChildAt(mCurrentPosition);
                        currentMenu.setVisibility(GONE);
                        mAdapter.closeMenu(mMenuTabView.getChildAt(mCurrentPosition));
                        mCurrentPosition = position;
                        currentMenu = mMenuContainerView.getChildAt(mCurrentPosition);
                        currentMenu.setVisibility(VISIBLE);
                        mAdapter.openMenu(mMenuTabView.getChildAt(mCurrentPosition));
                    }
                }
            }
        });
    }

    public void closeMenu() {
        if (mAnimatorExecute) {
            return;
        }
        //设置阴影不可见
        mShadowView.setVisibility(GONE);
        // 获取当前位置显示当前菜单，菜单是加到了菜单容器
        final View menuView = mMenuContainerView.getChildAt(mCurrentPosition);
        menuView.setVisibility(GONE);
        //位移动画
        ObjectAnimator translationAnimation =
                ObjectAnimator.ofFloat(mMenuContainerView, "translationY", 0, -mMenuContainerHeight);
        translationAnimation.setDuration(DURATION_TIME);
        translationAnimation.start();
        //阴影的透明度变化
        ObjectAnimator rotationAniamtion = ObjectAnimator.ofFloat(mShadowView, "alpha", 1f, 0f);
        rotationAniamtion.setDuration(DURATION_TIME);
        rotationAniamtion.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentPosition = -1;
                mAnimatorExecute = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mAnimatorExecute = true;
                mAdapter.closeMenu(mMenuTabView.getChildAt(mCurrentPosition));
            }
        });
        rotationAniamtion.start();
    }

    /**
     * 打开菜单
     */
    private void openMenu(final View tabView, final int postition) {
        if (mAnimatorExecute) {
            return;
        }
        //设置阴影为可见
        mShadowView.setVisibility(VISIBLE);

        // 获取当前位置显示当前菜单，菜单是加到了菜单容器
        final View menuView = mMenuContainerView.getChildAt(postition);
        menuView.setVisibility(VISIBLE);
        //位移动画
        ObjectAnimator translationAnimation =
                ObjectAnimator.ofFloat(mMenuContainerView, "translationY", -mMenuContainerHeight, 0);
        translationAnimation.setDuration(DURATION_TIME);
        translationAnimation.start();
        //阴影的透明度变化
        ObjectAnimator rotationAniamtion = ObjectAnimator.ofFloat(mShadowView, "alpha", 0f, 1f);
        rotationAniamtion.setDuration(DURATION_TIME);
        rotationAniamtion.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mCurrentPosition = postition;
                mAnimatorExecute = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mAnimatorExecute = true;
                mAdapter.openMenu(tabView);
            }
        });
        rotationAniamtion.start();
    }

    @Override
    public void onClick(View v) {
        closeMenu();
    }
}
