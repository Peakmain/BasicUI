package com.peakmain.ui.recyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * author ：Peakmain
 * version : 1.0
 * createTime：2019/2/25
 * mail:2726449200@qq.com
 * describe：
 */
public class ViewHolder extends RecyclerView.ViewHolder {
    // 用来存放子View减少findViewById的次数
    private SparseArray<View> mViews;
    public ViewHolder(View itemView) {
        super(itemView);
        mViews=new SparseArray<>();
    }
    /**
     * 通过id获取view
     */
    @SuppressWarnings("unchecked")
    public <T extends View>T getView(int viewId){
        View view = mViews.get(viewId);
        if(view==null){
            view=itemView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (T) view;
    }
    /**
     * 设置TextView文本
     */
    public ViewHolder setText(int viewId,CharSequence text){
        TextView textView=getView(viewId);
        textView.setText(text);
        return this;
    }
    public ViewHolder setTextColor(int viewId,int color){
        TextView textView=getView(viewId);
        textView.setTextColor(color);
        return this;
    }
    /**
     *设置view的Visibilty
     */
    public ViewHolder setViewVisibility(int viewId,int visibility){
        getView(viewId).setVisibility(visibility);
        return this;
    }
    /**
     * 设置ImageView的本地资源
     */
    public ViewHolder setImageResource(int viewId,int resourceId){
        ImageView imageView=getView(viewId);
        imageView.setImageResource(resourceId);
        return this;
    }
    public ViewHolder setBackground(int viewId,int resourceId){
        getView(viewId).setBackgroundResource(resourceId);
        return this;
    }
    /**
     * 设置条目点击事件
     */
    public void setOnItemClickListener(View.OnClickListener listener) {
        itemView.setOnClickListener(listener);
    }
    /**
     * 设置条目某个view的点击事件
     */
    public void setOnItemClickListener(int viewId,View.OnClickListener listener){
        getView(viewId).setOnClickListener(listener);
    }
    /**
     * 设置条目长按事件
     */
    public void setOnItemLongClickListener(View.OnLongClickListener listener) {
        itemView.setOnLongClickListener(listener);
    }
    /**
     * 设置条目某个view的长按事件
     */
    public void setOnItemLongClickListener(int viewId,View.OnClickListener listener){
        getView(viewId).setOnClickListener(listener);
    }
    /**
     * 设置图片通过路径
     */
    public ViewHolder setImageByUrl(int viewId,HolderImageLoader imageLoader){
        ImageView imageView = getView(viewId);
        if(imageLoader==null){
            throw new NullPointerException("imageLoader is null!");
        }
        imageLoader.displayImage(imageView.getContext(),imageView,imageLoader.getImagePath());
        return this;
    }

    /**
     * 交给第三方自己处理
     */
    public static abstract class HolderImageLoader {
        private String mImagePath;

        public HolderImageLoader(String imagePath) {
            this.mImagePath = imagePath;
        }


        public String getImagePath() {
            return mImagePath;
        }

        public abstract void displayImage(Context context, ImageView imageView, String imagePath);
    }
}
