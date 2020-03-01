package com.peakmain.ui.recyclerview.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.peakmain.ui.recyclerview.adapter.WrapRecyclerAdapter;
import com.peakmain.ui.recyclerview.listener.OnItemClickListener;

/**
 * author: peakmain
 * createdata：2019/7/17
 * mail: 2726449200@qq.com
 * desiption:
 */
public class WrapRecyclerView extends RecyclerView {
    // 包裹了一层的头部底部Adapter
    private WrapRecyclerAdapter mWrapRecyclerAdapter;
    // 这个是列表数据的Adapter
    private Adapter mAdapter;

    // 增加一些通用功能
    // 空列表数据应该显示的空View
    // 正在加载数据页面，也就是正在获取后台接口页面
    private View mEmptyView, mLoadingView;
    private AdapterDataObserver mDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            if (mAdapter == null) {
                return;
            }
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyDataSetChanged没效果
            if (mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter.notifyDataSetChanged();
            }
            dataChanged();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyDataSetChanged没效果
            if (mWrapRecyclerAdapter != mAdapter)
                mWrapRecyclerAdapter.notifyItemRemoved(positionStart);
            dataChanged();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            if (mAdapter == null) {
                return;
            }
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemMoved没效果
            if (mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter.notifyItemMoved(fromPosition, toPosition);
            }
            dataChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            if (mAdapter == null) {
                return;
            }
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemChanged没效果
            if (mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter.notifyItemChanged(positionStart);
            }
            dataChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            if (mAdapter == null) {
                return;
            }
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemChanged没效果
            if (mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter.notifyItemChanged(positionStart, payload);
            }
            dataChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            if (mAdapter == null) {
                return;
            }
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemInserted没效果
            if (mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter.notifyItemInserted(positionStart);
            }
            dataChanged();
        }
    };

    /**
     * adapter数据改变的方法
     */
    private void dataChanged() {
        if (mAdapter.getItemCount() == 0) {
            // 没有数据
            if (mEmptyView != null) {
                mEmptyView.setVisibility(VISIBLE);
            }
        } else {
            if (mEmptyView != null) {
                mEmptyView.setVisibility(GONE);
            }
        }
    }

    public WrapRecyclerView(Context context) {
        super(context);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterAdapterDataObserver(mDataObserver);
            mAdapter = null;
        }
        mAdapter = adapter;
        if (mAdapter instanceof WrapRecyclerAdapter) {
            mWrapRecyclerAdapter = (WrapRecyclerAdapter) adapter;
        } else {
            mWrapRecyclerAdapter = new WrapRecyclerAdapter(mAdapter);
        }
        super.setAdapter(mWrapRecyclerAdapter);
        // 注册一个观察者
        mAdapter.registerAdapterDataObserver(mDataObserver);

        // 解决GridLayout添加头部和底部也要占据一行
        mWrapRecyclerAdapter.adjustSpanSize(this);

        //加载数据页面
        if (mLoadingView != null && mLoadingView.getVisibility() == View.VISIBLE) {
            mLoadingView.setVisibility(View.GONE);
        }

        if (mItemClickListener != null) {
            mWrapRecyclerAdapter.setOnItemClickListener(mItemClickListener);
        }

        if (mLongClickListener != null) {
            mWrapRecyclerAdapter.setOnLongClickListener(mLongClickListener);
        }


    }

    // 添加头部
    public void addHeaderView(View view) {
        // 如果没有Adapter那么就不添加，也可以选择抛异常提示
        // 让他必须先设置Adapter然后才能添加，这里是仿照ListView的处理方式
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.addHeaderView(view);
        }
    }

    // 添加底部
    public void addFooterView(View view) {
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.addFooterView(view);
        }
    }

    // 移除头部
    public void removeHeaderView(View view) {
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.removeHeaderView(view);
        }
    }

    // 移除底部
    public void removeFooterView(View view) {
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.removeFooterView(view);
        }
    }

    /**
     * 添加一个空列表数据页面
     */
    public void addEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
    }

    /**
     * 添加一个正在加载数据的页面
     */
    public void addLoadingView(View loadingView) {
        this.mLoadingView = loadingView;
        mLoadingView.setVisibility(View.VISIBLE);
    }

    public OnItemClickListener mItemClickListener;
    public com.peakmain.ui.recyclerview.listener.OnLongClickListener mLongClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;

        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.setOnItemClickListener(mItemClickListener);
        }
    }

    public void setOnLongListener(com.peakmain.ui.recyclerview.listener.OnLongClickListener longClickListener) {
        this.mLongClickListener = longClickListener;

        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.setOnLongClickListener(mLongClickListener);
        }
    }
}
