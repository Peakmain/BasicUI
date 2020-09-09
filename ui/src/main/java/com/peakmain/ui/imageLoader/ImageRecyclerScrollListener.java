package com.peakmain.ui.imageLoader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

/**
 * author ：Peakmain
 * createTime：2020/9/9
 * mail:2726449200@qq.com
 * describe：recylcerview在空闲的时候加载图片
 */
public class ImageRecyclerScrollListener extends RecyclerView.OnScrollListener {
    private Context mContext;

    public ImageRecyclerScrollListener(Context context) {
        this.mContext = context;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        //空闲的时候加载图片
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            ImageLoader.getInstance().resumeRequest(mContext);
        } else {
            ImageLoader.getInstance().pauseRequest(mContext);
        }
    }
}
