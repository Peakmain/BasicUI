package com.peakmain.ui.tablayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.peakmain.ui.R;
import com.peakmain.ui.utils.SizeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/3/3
 * mail:2726449200@qq.com
 * describe：仿今日头条自定义的TabLayout
 */
public abstract class BaseTabLayout<T> extends HorizontalScrollView {
    //容器
    private LinearLayout mLinearLayout;
    //布局的容器
    private ArrayList<ColorTrackTextView> mIndicators;
    private ViewPager mViewPager;
    //上一次位置
    private int lastIndex = 0;
    //当前位置
    private int currIndex = 0;
    //判断是否是点击事件
    private boolean isTabClick = false;
    //当前点击的位置
    private int mPosition = -1;

    private ValueAnimator scrollAnimator;
    //变色的颜色
    private int mChangeColor;
    //原始的颜色
    private int mOriginColor;
    //是否显示下划线，默认是不显示
    private boolean mIsShowUnderLine;

    public BaseTabLayout(Context context) {
        this(context, null);
    }

    public BaseTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BaseTabLayout);
        mOriginColor = ta.getColor(R.styleable.BaseTabLayout_originColor, Color.BLUE);
        mChangeColor = ta.getColor(R.styleable.BaseTabLayout_changeColor, Color.RED);
        mIsShowUnderLine = ta.getBoolean(R.styleable.BaseTabLayout_isShowUnderLine, false);
        ta.recycle();
        //消除边界反光
        setOverScrollMode(OVER_SCROLL_NEVER);
        //垂直方向的水平滚动条是否显示
        setVerticalScrollBarEnabled(false);
        //水平方向的水平滚动条是否显示
        setHorizontalScrollBarEnabled(false);
        mIndicators = new ArrayList<>();

    }

    /**
     * 初始化可变色的指示器
     *
     * @param bean 换成List<String>也可以，看自己项目需要
     */
    public void initIndicator(List<T> bean, ViewPager viewPager) {
        this.mViewPager = viewPager;
        if (bean == null) {
            return;
        }
        initLinearLayout();
        for (int i = 0; i < bean.size(); i++) {
            // 动态添加颜色跟踪的TextView
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //不可用具体的数字，目的主要是为了适配
            params.leftMargin = SizeUtils.getScreenWidth() / 25;
            final ColorTrackTextView colorTrackTextView = new ColorTrackTextView(getContext());
            //设置两种颜色
            colorTrackTextView.setOriginColor(mOriginColor);
            colorTrackTextView.setChangeColor(mChangeColor);
            colorTrackTextView.setShowUnderLine(mIsShowUnderLine);
            String name = setTableTitle(bean, i);
            colorTrackTextView.setText(name);
            colorTrackTextView.setLayoutParams(params);
            //把新的加入LinearLayout容器
            mLinearLayout.addView(colorTrackTextView);
            final int finalI = i;
            colorTrackTextView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnTabItemClickListener != null) {
                        mOnTabItemClickListener.onTabItem(finalI);
                        isTabClick = true;
                        mPosition = finalI;
                    }
                }
            });
            //添加到集合容器中
            mIndicators.add(colorTrackTextView);
        }
        setViewPagerListener(bean);
    }

    protected void setViewPagerListener(final List<T> bean) {
        if (null == bean || bean.size() == 0) {
            return;
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int previousScrollState;
            private int scrollState;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currIndex = position;
                if (!isTabClick) {
                    //获取左边
                    ColorTrackTextView left = mIndicators.get(position);
                    //设置朝向
                    left.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
                    // 设置进度  positionOffset 是从 0 一直变化到 1
                    left.setCurrentProgress(1 - positionOffset);
                    if (position < bean.size() - 1) {
                        //获取右边
                        ColorTrackTextView right = mIndicators.get(position + 1);
                        right.setDirection(ColorTrackTextView.Direction.LEFT_TO_RIGHT);
                        right.setCurrentProgress(positionOffset);
                    }
                } else {
                    //当前位置设置为红色，其他全部设置为黑色
                    for (int i = 0; i < mIndicators.size(); i++) {
                        ColorTrackTextView colorTrackTextView = mIndicators.get(i);
                        if (i == position) {
                            colorTrackTextView.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
                            colorTrackTextView.setCurrentProgress(1);
                        } else {
                            colorTrackTextView.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
                            colorTrackTextView.setCurrentProgress(0);
                        }
                    }
                    setScrollPosition(position, positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {
                lastIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                this.previousScrollState = this.scrollState;
                this.scrollState = state;
                if (!isTabClick) {
                    if ((currIndex != (mIndicators.size() - 1)) && (currIndex != 0)) {
                        scrollToChild(currIndex);
                    }
                }
                if (state == 0) {
                    isTabClick = false;
                }
            }
        });
    }

    protected void setScrollPosition(int position, float positionOffset) {
        int roundedPosition = Math.round((float) position + positionOffset);
        if (roundedPosition >= 0 && roundedPosition < this.mLinearLayout.getChildCount()) {

            if (this.scrollAnimator != null && this.scrollAnimator.isRunning()) {
                this.scrollAnimator.cancel();
            }
            this.smoothScrollTo(this.calculateScrollxForTab(position, positionOffset), 0);
        }
    }

    private int calculateScrollxForTab(int position, float positionOffset) {
        View selectedChild = this.mLinearLayout.getChildAt(position);
        View nextChild = position + 1 < this.mLinearLayout.getChildCount() ? this.mLinearLayout.getChildAt(position + 1) : null;
        int selectedWidth = selectedChild != null ? selectedChild.getWidth() : 0;
        int nextWidth = nextChild != null ? nextChild.getWidth() : 0;
        int scrollBase = selectedChild.getLeft() + selectedWidth / 2 - this.getWidth() / 2;
        int scrollOffset = (int) ((float) (selectedWidth + nextWidth) * 0.5F * positionOffset);
        return ViewCompat.getLayoutDirection(this) == 0 ? scrollBase + scrollOffset : scrollBase - scrollOffset;
    }

    private void scrollToChild(int position) {
        //获取屏幕的宽度
        int screenWidth = SizeUtils.getScreenWidth();
        //计算控件居正中时距离左侧屏幕的距离
        int middleLeftPosition = (screenWidth - mLinearLayout.getChildAt(position).getWidth()) / 2;
        //正中间位置需要向左偏移的距离
        int offset = mLinearLayout.getChildAt(position).getLeft() - middleLeftPosition;
        smoothScrollTo(offset, 0);
    }

    /**
     * 点击onTable事件的回掉
     */

    public interface OnTabItemClickListener {
        void onTabItem(int postition);
    }

    private OnTabItemClickListener mOnTabItemClickListener;

    public void setOnTabItemClickListener(OnTabItemClickListener onTabItemClickListener) {
        mOnTabItemClickListener = onTabItemClickListener;
    }

    /**
     * 显示的名字
     */
    public abstract String setTableTitle(List<T> bean, int position);

    /**
     * 初始化标签容器
     */
    private void initLinearLayout() {
        mLinearLayout = new LinearLayout(getContext());
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLinearLayout.setLayoutParams(params);
        addView(mLinearLayout);
    }


}
