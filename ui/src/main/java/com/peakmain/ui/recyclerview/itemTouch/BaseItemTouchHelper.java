package com.peakmain.ui.recyclerview.itemTouch;

import android.graphics.Color;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.peakmain.ui.recyclerview.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/3/25
 * mail:2726449200@qq.com
 * describe：
 */
public abstract class BaseItemTouchHelper<T> {
    private final RecyclerView.Adapter<ViewHolder> mAdapter;
    private List<T> mDatas;
    private int mSwipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
    private int mGridDragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
    private int mLinearDragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT;

    public BaseItemTouchHelper(RecyclerView.Adapter<ViewHolder> adapter, List<T> data) {
        this.mAdapter = adapter;
        mDatas = data == null ? new ArrayList<T>() : data;
    }

    private final ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {

        @Override
        public int getMovementFlags(@Nullable RecyclerView recyclerView, @Nullable RecyclerView.ViewHolder viewHolder) {
            if (recyclerView == null) return 0;
            //不做处理的时候默认都是0
            int dragFlags;
            //左划删除和右滑删除
            int swipeFlags = mSwipeFlags;
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                dragFlags = mGridDragFlags;
            } else {
                dragFlags = mLinearDragFlags;
            }
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                //设置高亮
                viewHolder.itemView.setBackgroundColor(Color.YELLOW);
            }
        }

        @Override
        public boolean onMove(@Nullable RecyclerView recyclerView, @Nullable RecyclerView.ViewHolder viewHolder, @Nullable RecyclerView.ViewHolder target) {
            if (viewHolder == null || target == null) return false;
            //获取原来的位置
            int fromPosition = viewHolder.getBindingAdapterPosition();
            //获取目标的位置
            int targetPosition = target.getBindingAdapterPosition();
            //替换
            mAdapter.notifyItemMoved(fromPosition, targetPosition);
            //数据没有发生变化(mDatas中数据)
            if (fromPosition > targetPosition) {//1>0
                for (int i = fromPosition; i > targetPosition; i--) {//向上移动
                    Collections.swap(mDatas, i, i - 1);//向前移动

                }
            } else {
                for (int i = fromPosition; i < targetPosition; i++) {//向下拖拽
                    Collections.swap(mDatas, i, i + 1);//向后移
                }
            }

            return false;
        }

        @Override
        public void onSwiped(@Nullable RecyclerView.ViewHolder viewHolder, int direction) {
            if (viewHolder == null) return;
            int currentSwipePosition = viewHolder.getBindingAdapterPosition();
            mAdapter.notifyItemRemoved(currentSwipePosition);
            mDatas.remove(currentSwipePosition);

        }

        @Override
        public void clearView(@Nullable RecyclerView recyclerView, @Nullable RecyclerView.ViewHolder viewHolder) {
            if (recyclerView == null || viewHolder == null) return;
            if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE && !recyclerView.isComputingLayout()) {
                mAdapter.notifyItemRangeChanged(0, mDatas.size());
            }
            //动画执行完毕，恢复
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#ECECEC"));
            // 侧滑和拖动其实都是使用的ViewCompat这一个类
            viewHolder.itemView.setTranslationX(0);
            if (mOnDataUpdatedListener != null) {
                mOnDataUpdatedListener.onDataUpdated(mDatas);
            }
        }
    });

    public ItemTouchHelper getItemTouchHelper() {
        return mItemTouchHelper;
    }

    /**
     * Attaches the ItemTouchHelper to the provided RecyclerView. If TouchHelper is already
     * attached to a RecyclerView, it will first detach from the previous one. You can call this
     * method with {@code null} to detach it from the current RecyclerView.
     *
     * @param recyclerView The RecyclerView instance to which you want to add this helper or
     *                     {@code null} if you want to remove ItemTouchHelper from the current
     *                     RecyclerView.
     */
    public void attachToRecyclerView(RecyclerView recyclerView) {
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    /**
     * 设置GridLayoutManager 的拖拽flags
     *
     * @param gridDragFlags 默认是 ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
     * @return BaseItemTouchHelper
     */
    public BaseItemTouchHelper<T> setGridDragFlags(int gridDragFlags) {
        mGridDragFlags = gridDragFlags;
        return this;
    }

    /**
     * 设置LinearLayoutManager 的拖拽flags
     *
     * @param linearDragFlags 默认是 ItemTouchHelper.LEFT  | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
     * @return BaseItemTouchHelper
     */
    public BaseItemTouchHelper<T> setLinearDragFlags(int linearDragFlags) {
        mLinearDragFlags = linearDragFlags;
        return this;
    }

    /**
     * 设置滑动删除的flags
     *
     * @param swipeFlags 默认是 ItemTouchHelper.LEFT  | ItemTouchHelper.RIGHT;
     * @return BaseItemTouchHelper
     */
    public BaseItemTouchHelper<T> setSwipeFlags(int swipeFlags) {
        mSwipeFlags = swipeFlags;
        return this;
    }

    /**
     * 设置数据
     *
     * @param data List<T> datas
     * @return BaseItemTouchHelper
     */
    public BaseItemTouchHelper<T> setData(List<T> data) {
        mDatas = data;
        return this;
    }


    /**
     * 通知外部数据更新
     */
    public interface OnDataUpdatedListener<T> {
        void onDataUpdated(List<T> datas);
    }

    private OnDataUpdatedListener<T> mOnDataUpdatedListener;

    public BaseItemTouchHelper<T> setOnDataUpdatedListener(OnDataUpdatedListener<T> onDataUpdatedListener) {
        mOnDataUpdatedListener = onDataUpdatedListener;
        return this;
    }
}
