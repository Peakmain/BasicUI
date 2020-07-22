package com.peakmain.ui.recyclerview.adapter;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peakmain.ui.recyclerview.listener.OnItemClickListener;
import com.peakmain.ui.recyclerview.listener.OnLongClickListener;

import java.util.Collection;
import java.util.List;

/**
 * author ：Peakmain
 * version : 1.0
 * createTime：2019/2/25
 * mail:2726449200@qq.com
 * describe：
 */
public abstract class CommonRecyclerAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    protected Context mContext;//上下文
    protected LayoutInflater mInflater;
    protected List<T> mData;//数据
    private int mLayoutId;
    //多布局支持
    private MultiTypeSupport mMultiTypeSupport;
    protected View itemView;

    public CommonRecyclerAdapter(Context context, List<T> data, int layoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mData = data;
        this.mLayoutId = layoutId;
    }

    /**
     * 多布局支持
     */
    public CommonRecyclerAdapter(Context context, List<T> data, MultiTypeSupport<T> multiTypeSupport) {
        this(context, data, -1);
        this.mMultiTypeSupport = multiTypeSupport;
    }

    /**
     * 根据当前位置获取不同的viewType
     */
    @SuppressWarnings("unchecked")
    @Override
    public int getItemViewType(int position) {
        if (mMultiTypeSupport != null) {
            return mMultiTypeSupport.getLayoutId(mData.get(position), position);
        }
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //多布局支持
        if (mMultiTypeSupport != null) {
            mLayoutId = viewType;
        }
        itemView = mInflater.inflate(mLayoutId, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // 设置点击和长按事件
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(holder.getAdapterPosition());
                }
            });
        }
        if (mLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mLongClickListener.onLongClick(holder.getAdapterPosition());
                }
            });
        }
        convert(holder, mData.get(position));
    }

    /**
     * 利用抽象方法回传出去，每个不一样的Adapter去设置
     *
     * @param item 当前的数据
     */
    public abstract void convert(ViewHolder holder, T item);

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 给条目设置点击和长按事件
     */
    public OnItemClickListener mItemClickListener;
    public OnLongClickListener mLongClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener longClickListener) {
        this.mLongClickListener = longClickListener;
    }

    public T getItem(int position) {
        if (position >= 0 && position < mData.size()) {
            return mData.get(position);
        } else {
            return null;
        }
    }

    /**
     * 添加数据
     */
    public void addData(T data) {
        mData.add(data);
        notifyItemInserted(mData.size() - 1);
    }

    public void addData(@IntRange(from = 0) int position, T data) {
        mData.add(position, data);
        notifyItemInserted(position);
    }

    public void addData(Collection<? extends T> datas) {
        mData.addAll(datas);
        notifyItemRangeInserted(mData.size() - datas.size(), datas.size());
    }


    public int getDataSize() {
        return mData.size();
    }

    /**
     * 替换数据
     */
    public void replaceData(Collection<? extends T> newData) {
        if (mData != newData) {
            mData.clear();
            mData.addAll(newData);
        }
        notifyDataSetChanged();
    }

    /**
     * 移除数据
     */
    public void removeData(@IntRange(from = 0) int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 返回数据
     */
    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }
}
