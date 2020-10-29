package com.peakmain.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.peakmain.ui.adapter.flow.BaseFlowAdapter;
import com.peakmain.ui.adapter.flow.FlowObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/2/29
 * mail:2726449200@qq.com
 * describe：自定义view之流式布局
 */
public class FlowLayout extends ViewGroup {
    private List<List<View>> mChildViews = new ArrayList<>();
    private BaseFlowAdapter mAdapter;
    // 是否可以滚动
    private boolean mScrollable;
    // 测量得到的高度
    private int mMeasuredHeight;
    // 整个流式布局控件的实际高度
    private int mRealHeight;
    // 已经滚动过的高度
    private int mScrolledHeight = 0;
    // 本次滑动开始的Y坐标位置
    private int mStartY;
    // 本次滑动的偏移量
    private int mOffsetY;
    // 在ACTION_MOVE中，视第一次触发为手指按下，从第二次触发开始计入正式滑动
    private boolean mPointerDown;

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 2.1 onMeasure() 指定宽高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 清空集合
        mChildViews.clear();

        int childCount = getChildCount();

        // 获取到宽度
        int width = MeasureSpec.getSize(widthMeasureSpec);
        mMeasuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        // 高度需要计算
        int height = getPaddingTop() + getPaddingBottom();

        // 一行的宽度
        int lineWidth = getPaddingLeft();

        ArrayList<View> childViews = new ArrayList<>();
        mChildViews.add(childViews);

        // 子View高度不一致的情况下
        int maxHeight = 0;

        for (int i = 0; i < childCount; i++) {

            // 2.1.1 for循环测量子View
            View childView = getChildAt(i);

            if (childView.getVisibility() == GONE) {
                continue;
            }

            //调用子View的onMeasure
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);

            // margin值 ViewGroup.LayoutParams 没有 就用系统的MarginLayoutParams
            // LinearLayout有自己的 LayoutParams  会复写一个非常重要的方法
            MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();

            if (lineWidth + (childView.getMeasuredWidth() + params.rightMargin + params.leftMargin) > width) {
                // 换行,累加高度  加上一行条目中最大的高度
                height += maxHeight;
                lineWidth = childView.getMeasuredWidth() + params.rightMargin + params.leftMargin;
                childViews = new ArrayList<>();
                mChildViews.add(childViews);
            } else {
                lineWidth += childView.getMeasuredWidth() + params.rightMargin + params.leftMargin;
                maxHeight = Math.max(childView.getMeasuredHeight() + params.bottomMargin + params.topMargin, maxHeight);
            }

            childViews.add(childView);
        }

        height += maxHeight;
        mRealHeight = height;
        mScrollable = mRealHeight > mMeasuredHeight;
        // 2.1.2 根据子View计算和指定自己的宽高
        setMeasuredDimension(getMeasuredWidth(), height);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(LayoutParams p) {
        return super.generateLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }


    /**
     * 摆放子View
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left, top = getPaddingTop(), right, bottom;

        for (List<View> childViews : mChildViews) {
            left = getPaddingLeft();
            int maxHeight = 0;
            for (View childView : childViews) {

                if (childView.getVisibility() == GONE) {
                    continue;
                }

                MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
                left += params.leftMargin;
                int childTop = top + params.topMargin;
                right = left + childView.getMeasuredWidth();
                bottom = childTop + childView.getMeasuredHeight();


                // 摆放
                childView.layout(left, childTop, right, bottom);
                // left 叠加
                left += childView.getMeasuredWidth() + params.rightMargin;

                // 不断的叠加top值
                int childHeight = childView.getMeasuredHeight() + params.topMargin + params.bottomMargin;
                maxHeight = Math.max(maxHeight, childHeight);
            }

            top += maxHeight;
        }
    }

    private class AdapterDataSetObserver extends FlowObserver {

        @Override
        public void notifyDataChange() {
            FlowLayout.this.notiftyDataChange();
        }
    }

    private AdapterDataSetObserver mObserver;

    public void notiftyDataChange() {
        setAdapter(mAdapter);
        requestLayout();
    }

    /**
     * 设置Adapter
     *
     * @param adapter
     */
    public void setAdapter(BaseFlowAdapter adapter) {
        if (adapter == null) {
            return;
        }
        if (mAdapter != null && mObserver != null) {
            adapter.unregisterDataSetObserver(mObserver);
        }
        // 清空所有子View
        removeAllViews();
        mAdapter = adapter;
        mObserver = new AdapterDataSetObserver();
        mAdapter.registerDataSetObserver(mObserver);
        // 获取数量
        int childCount = mAdapter.getCount();
        for (int i = 0; i < childCount; i++) {
            // 通过位置获取View
            View childView = mAdapter.getView(i, this);
            addView(childView);
        }
    }

    /**
     * 滚动事件的处理，当布局可以滚动（内容高度大于测量高度）时，对手势操作进行处理
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mScrollable) {
            int currY = (int) event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                // 当第一次触发ACTION_MOVE事件时，视为手指按下；以后的ACTION_MOVE事件才视为滚动事件
                case MotionEvent.ACTION_MOVE:
                    if (!mPointerDown) {
                        mStartY = currY;
                        mPointerDown = true;
                    } else {
                        mOffsetY = mStartY - currY;
                        this.scrollTo(0, mScrolledHeight + mOffsetY);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    mScrolledHeight += mOffsetY;
                    if (mScrolledHeight + mOffsetY < 0) {
                        this.scrollTo(0, 0);
                        mScrolledHeight = 0;
                    } else if (mScrolledHeight + mOffsetY + mMeasuredHeight > mRealHeight) {
                        this.scrollTo(0, mRealHeight - mMeasuredHeight);
                        mScrolledHeight = mRealHeight - mMeasuredHeight;
                    }
                    mPointerDown = false;
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
                intercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                intercepted = true;
                break;
        }
        return intercepted;
    }
}
