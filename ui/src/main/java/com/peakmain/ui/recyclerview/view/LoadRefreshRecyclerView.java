package com.peakmain.ui.recyclerview.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.peakmain.ui.recyclerview.creator.LoadViewCreator;

/**
 * @author ：Peakmain
 * version ：1.0
 * createTime ：2018/11/21 0021 下午 3:13
 * mail : 2726449200@qq.com
 * describe ：下拉刷新上拉加载更多的RecyclerView
 */
public class LoadRefreshRecyclerView extends RefreshRecyclerView {
    // 上拉加载更多的辅助类
    private LoadViewCreator mLoadCreator;
    // 上拉加载更多头部的高度
    private int mLoadViewHeight = 0;
    // 上拉加载更多的头部View
    private View mLoadView;
    // 手指按下的Y位置
    private int mFingerDownY;
    // 当前是否正在拖动
    private boolean mCurrentDrag = false;
    // 当前的状态
    private int mCurrentLoadStatus;
    // 默认状态
    public int LOAD_STATUS_NORMAL = 0x0011;
    // 上拉加载更多状态
    public static int LOAD_STATUS_PULL_DOWN_REFRESH = 0x0022;
    // 松开加载更多状态
    public static int LOAD_STATUS_LOOSEN_LOADING = 0x0033;
    // 正在加载更多状态
    public int LOAD_STATUS_LOADING = 0x0044;

    public LoadRefreshRecyclerView(Context context) {
        super(context);
    }

    public LoadRefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadRefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // 辅助类——加载列表的不同风格样式
    public void addLoadViewCreator(LoadViewCreator loadCreator) {
        this.mLoadCreator = loadCreator;
        addRefreshView();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        addRefreshView();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录手指按下的位置 ,之所以写在dispatchTouchEvent那是因为如果我们处理了条目点击事件，
                // 那么就不会进入onTouchEvent里面，所以只能在这里获取
                mFingerDownY = (int) ev.getRawY();
                break;

            case MotionEvent.ACTION_UP:
                if (mCurrentDrag) {
                    restoreLoadView();
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 重置当前加载更多状态
     */
    private void restoreLoadView() {
        int currentBottomMargin = ((MarginLayoutParams) mLoadView.getLayoutParams()).bottomMargin;
        int finalBottomMargin = 0;
        if (mCurrentLoadStatus == LOAD_STATUS_LOOSEN_LOADING) {
            mCurrentLoadStatus = LOAD_STATUS_LOADING;

            if (mLoadCreator != null) {
                if (mListener != null && mListener.isLoadMore()) {
                    mLoadCreator.onLoading();
                } else {
                    mLoadCreator.onFinishLoadData();
                }
            }
            if (mListener != null) {
                if (mListener.isLoadMore()) {
                    mListener.onLoad();
                }
            }
        }
        Log.e("TAG", "是否加载更多:" + mListener.isLoadMore());
        if (mListener != null && mLoadCreator != null && !mListener.isLoadMore()) {
            mLoadCreator.onFinishLoadData();
        }
        if (mCurrentDrag) {
            int distance = currentBottomMargin - finalBottomMargin;

            // 回弹到指定位置
            ValueAnimator animator = ObjectAnimator.ofFloat(currentBottomMargin, finalBottomMargin).setDuration(distance);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float currentTopMargin = (float) animation.getAnimatedValue();
                    setLoadViewMarginBottom((int) currentTopMargin);
                }
            });
            animator.start();
            mCurrentDrag = false;
        }
    }

    /**
     * 重置加载的view
     */
    @Override
    public void restLoadView() {
       updateLoadStatus(1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                // 如果是在最底部才处理，否则不需要处理
                if (canScrollDown() || mCurrentLoadStatus == LOAD_STATUS_LOADING
                        || mLoadCreator == null || mLoadView == null || !mListener.isLoadMore()) {
                    // 如果没有到达最顶端，也就是说还可以向上滚动就什么都不处理
                    return super.onTouchEvent(e);
                }

                mLoadViewHeight = mLoadView.getMeasuredHeight();
                // 解决上拉加载更多自动滚动问题
                if (mCurrentDrag) {
                    scrollToPosition(getAdapter().getItemCount() - 1);
                }

                // 获取手指触摸拖拽的距离
                int distanceY = (int) ((e.getRawY() - mFingerDownY) * mDragIndex);
                // 如果是已经到达头部，并且不断的向下拉，那么不断的改变refreshView的marginTop的值
                if (distanceY < 0) {
                    setLoadViewMarginBottom(-distanceY);
                    updateLoadStatus(-distanceY);
                    mCurrentDrag = true;
                    return true;
                }
                break;
            default:
                break;
        }

        return super.onTouchEvent(e);
    }

    /**
     * 更新加载的状态
     */
    private void updateLoadStatus(int distanceY) {
        if (distanceY <= 0) {
            mCurrentLoadStatus = LOAD_STATUS_NORMAL;
        } else if (distanceY < mLoadViewHeight) {
            mCurrentLoadStatus = LOAD_STATUS_PULL_DOWN_REFRESH;
        } else {
            mCurrentLoadStatus = LOAD_STATUS_LOOSEN_LOADING;
        }

        if (mLoadCreator != null) {
            mLoadCreator.onPull(distanceY, mLoadViewHeight, mCurrentLoadStatus);
        }
    }

    /**
     * 添加底部加载更多View
     */
    private void addRefreshView() {
        Adapter adapter = getAdapter();
        if (adapter != null && mLoadCreator != null) {
            // 添加底部加载更多View
            View loadView = mLoadCreator.getLoadView(getContext(), this);
            if (loadView != null) {
                addFooterView(loadView);
                this.mLoadView = loadView;
            }
        }
    }

    /**
     * 设置加载View的marginBottom
     */
    public void setLoadViewMarginBottom(int marginBottom) {
        MarginLayoutParams params = (MarginLayoutParams) mLoadView.getLayoutParams();
        if (marginBottom < 0) {
            marginBottom = 0;
        }
        params.bottomMargin = marginBottom;
        mLoadView.setLayoutParams(params);
    }


    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     * 判断是不是滚动到了最底部
     */
    public boolean canScrollDown() {
        return ViewCompat.canScrollVertically(this, 1);
    }

    /**
     * 停止加载更多
     */
    public void onStopLoad() {
        mCurrentLoadStatus = LOAD_STATUS_NORMAL;
        if (mLoadCreator != null) {
            mLoadCreator.onStopLoad();
        }
        restoreLoadView();
    }

    // 处理加载更多回调监听
    private OnLoadMoreListener mListener;

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.mListener = listener;
    }

    public interface OnLoadMoreListener {
        void onLoad();

        boolean isLoadMore();
    }
}
