package com.peakmain.ui.recyclerview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peakmain.ui.R;
import com.peakmain.ui.recyclerview.adapter.WrapRecyclerAdapter;
import com.peakmain.ui.recyclerview.listener.OnItemClickListener;

import java.util.ArrayList;

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
    public static final int NULL_RESOURCE_ID = -1;
    //五种状态
    public static final int STATUS_CONTENT = 0x00;
    public static final int STATUS_LOADING = 0x01;
    public static final int STATUS_EMPTY = 0x02;
    public static final int STATUS_ERROR = 0x03;
    public static final int STATUS_NO_NETWORK = 0x04;
    // 增加一些通用功能
    // 空列表数据应该显示的空View
    // 正在加载数据页面，也就是正在获取后台接口页面
    //view
    private View mEmptyView;
    private View mErrorView;
    private View mLoadingView;
    private View mNoNetworkView;
    private View mContentView;
    //布局的id
    private int mEmptyViewResId;
    private int mErrorViewResId;
    private int mLoadingViewResId;
    private int mNoNetworkViewResId;
    private LayoutInflater mInflater;

    private final ArrayList<Integer> mOtherIds = new ArrayList<>();
    private final ArrayList<View> mStatusView = new ArrayList<>();
    /**
     * 当前view的状态
     */
    private int mViewStatus;
    /**
     * 默认设置是全屏
     */
    private static final RecyclerView.LayoutParams DEFAULT_LAYOUT_PARAMS =
            new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.MATCH_PARENT);
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
       /* switch (mViewStatus) {
            case STATUS_CONTENT:
                break;
            case STATUS_ERROR:
                mErrorView.setVisibility(VISIBLE);
                break;
            case STATUS_NO_NETWORK:
                mNoNetworkView.setVisibility(VISIBLE);
                break;
            case STATUS_LOADING:
                mLoadingView.setVisibility(VISIBLE);
                break;
            default:
                break;
        }*/
    }

    public WrapRecyclerView(Context context) {
        this(context, null);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MultipleStatusView, defStyle, 0);
        mEmptyViewResId = a.getResourceId(R.styleable.MultipleStatusView_emptyView, R.layout.layout_empty_view);
        mErrorViewResId = a.getResourceId(R.styleable.MultipleStatusView_errorView, R.layout.layout_error_view);
        mLoadingViewResId = a.getResourceId(R.styleable.MultipleStatusView_loadingView, R.layout.layout_loading_view);
        mNoNetworkViewResId = a.getResourceId(R.styleable.MultipleStatusView_noNetworkView, R.layout.layout_network_view);
        a.recycle();
        mInflater = LayoutInflater.from(context);
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

    private View inflateView(int layoutId) {
        return mInflater.inflate(layoutId, null);
    }

    /**
     * 添加一个空列表数据页面
     */
    public final void showEmptyView() {
        if (mEmptyView != null) {
            showEmpty(mEmptyView, DEFAULT_LAYOUT_PARAMS);
        } else {
            showEmpty(mEmptyViewResId, DEFAULT_LAYOUT_PARAMS);
        }
    }


    /**
     * 显示空视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    private void showEmpty(int layoutId, RecyclerView.LayoutParams layoutParams) {
        showEmpty(inflateView(layoutId), layoutParams);
    }

    /**
     * 显示空视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    private void showEmpty(View view, LayoutParams layoutParams) {
        mViewStatus = STATUS_EMPTY;
        if (mEmptyView == null) {
            mEmptyView = view;
            View emptyRetryView = mEmptyView.findViewById(R.id.empty_retry_view);
            if (null != mOnRetryClickListener && null != emptyRetryView) {
                emptyRetryView.setOnClickListener(mOnRetryClickListener);
            }
            mOtherIds.add(mEmptyView.getId());
            ViewGroup parent = (ViewGroup) getParent();
            parent.addView(view, 0, layoutParams);

        }
        mStatusView.add(mEmptyView);
        showViewById(mEmptyView.getId());
    }

    /**
     * 显示错误视图
     */
    public final void showError() {
        if (mErrorView != null) {
            showError(mErrorView, DEFAULT_LAYOUT_PARAMS);
        } else {
            showError(mErrorViewResId, DEFAULT_LAYOUT_PARAMS);
        }
    }

    /**
     * 显示错误视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    public final void showError(int layoutId, ViewGroup.LayoutParams layoutParams) {
        showError(inflateView(layoutId), layoutParams);
    }

    /**
     * 显示错误视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    public final void showError(View view, ViewGroup.LayoutParams layoutParams) {
        mViewStatus = STATUS_ERROR;
        if (null == mErrorView) {
            mErrorView = view;
            View errorRetryView = mErrorView.findViewById(R.id.error_retry_view);
            if (null != mOnRetryClickListener && null != errorRetryView) {
                errorRetryView.setOnClickListener(mOnRetryClickListener);
            }
            mOtherIds.add(mErrorView.getId());
            ViewGroup parent = (ViewGroup) getParent();
            parent.addView(view, 0, layoutParams);
        }
        mStatusView.add(mErrorView);
        showViewById(mErrorView.getId());
    }

    /**
     * 显示加载中视图
     */
    public final void showLoading() {

        if (mLoadingView != null) {
            showLoading(mLoadingView, DEFAULT_LAYOUT_PARAMS);
        } else {
            showLoading(mLoadingViewResId, DEFAULT_LAYOUT_PARAMS);
        }
    }


    /**
     * 显示加载中视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    public final void showLoading(int layoutId, ViewGroup.LayoutParams layoutParams) {
        showLoading(inflateView(layoutId), layoutParams);
    }

    /**
     * 显示加载中视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    public final void showLoading(View view, ViewGroup.LayoutParams layoutParams) {
        mViewStatus = STATUS_LOADING;
        if (null == mLoadingView) {
            mLoadingView = view;
            mOtherIds.add(mLoadingView.getId());
            ViewGroup parent = (ViewGroup) getParent();
            parent.addView(view, 0, layoutParams);
        }
        mStatusView.add(mLoadingView);
        showViewById(mLoadingView.getId());
    }

    /**
     * 显示无网络视图
     */
    public final void showNoNetwork() {
        if (mNoNetworkView != null) {
            showNoNetwork(mNoNetworkView, DEFAULT_LAYOUT_PARAMS);
        } else {
            showNoNetwork(mNoNetworkViewResId, DEFAULT_LAYOUT_PARAMS);
        }
    }

    /**
     * 显示无网络视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    public final void showNoNetwork(int layoutId, ViewGroup.LayoutParams layoutParams) {
        showNoNetwork(inflateView(layoutId), layoutParams);
    }

    /**
     * 显示无网络视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    public final void showNoNetwork(View view, ViewGroup.LayoutParams layoutParams) {
        mViewStatus = STATUS_NO_NETWORK;
        if (null == mNoNetworkView) {
            mNoNetworkView = view;
            View noNetworkRetryView = mNoNetworkView.findViewById(R.id.no_network_retry_view);
            if (null != mOnRetryClickListener && null != noNetworkRetryView) {
                noNetworkRetryView.setOnClickListener(mOnRetryClickListener);
            }
            mOtherIds.add(mNoNetworkView.getId());
            ViewGroup parent = (ViewGroup) getParent();
            parent.addView(view, 0, layoutParams);
        }
        mStatusView.add(mNoNetworkView);
        showViewById(mNoNetworkView.getId());
    }
    private void showViewById(int viewId) {
        setVisibility(GONE);
        for (View view : mStatusView) {
            if(view.getId() == viewId){
                view.setVisibility(VISIBLE);
            }else{
                view.setVisibility(GONE);
            }
        }
    }

    //重试的点击事件
    private OnClickListener mOnRetryClickListener;

    /**
     * 设置重试点击事件
     *
     * @param onRetryClickListener 重试点击事件
     */
    public void setOnRetryClickListener(OnClickListener onRetryClickListener) {
        this.mOnRetryClickListener = onRetryClickListener;
    }

    /**
     * 添加一个正在加载数据的页面
     */
    public void addLoadingView(View loadingView) {
        this.mLoadingView = loadingView;
        mLoadingView.setVisibility(View.VISIBLE);
    }


    /**
     * 设置自定的空view
     *
     * @param emptyView 布局view
     */
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    /**
     * 设置自定的空view
     *
     * @param emptyViewResId R.layout.xx
     */
    public void setEmptyView(int emptyViewResId) {
        this.mEmptyViewResId = emptyViewResId;
    }

    /**
     * 设置自定义view的错误view
     *
     * @param errorView 布局view
     */
    public void setErrorView(View errorView) {
        mErrorView = errorView;
    }

    /**
     * 设置自定义view的错误view
     *
     * @param errorViewResId 布局view的id
     */
    public void setErrorView(int errorViewResId) {
        mErrorViewResId = errorViewResId;
    }

    /**
     * 设置自定义view的正在加载的view
     *
     * @param loadingView 布局view
     */
    public void setLoadingView(View loadingView) {
        mLoadingView = loadingView;
    }

    /**
     * 设置自定义view的正在加载的view
     *
     * @param loadingViewResId 布局view的id
     */
    public void setLoadingView(int loadingViewResId) {
        mLoadingViewResId = loadingViewResId;
    }

    /**
     * 设置自定义view的没有网络的view
     *
     * @param noNetworkView 布局view
     */
    public void setNoNetworkView(View noNetworkView) {
        mNoNetworkView = noNetworkView;
    }

    /**
     * 设置自定义view的没有网络的view
     *
     * @param noNetworkViewResId 布局view的id
     */
    public void setNoNetworkView(int noNetworkViewResId) {
        mNoNetworkViewResId = noNetworkViewResId;
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
