package com.peakmain.ui.dialog;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * author peakmain
 * createdata：2019/7/9
 * mail:2726449200@qq.com
 * desiption:Layout helper classe
 */
class ViewHelper {
    private View mContentView;
    //The reduce findViewById
    private SparseArray<WeakReference<View>> mViews;

    public ViewHelper(Context context, int viewLayoutResId) {
        this();
        mContentView = LayoutInflater.from(context).inflate(viewLayoutResId, null);
    }

    public ViewHelper() {
        mViews = new SparseArray<>();
    }

    public void setText(int viewId, CharSequence text) {
        TextView textView = getView(viewId);
        if (textView != null) {
            textView.setText(text);
        }
    }

    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        if (view != null) {
            view.setOnClickListener(listener);
        }
    }

    protected  <T extends View> T getView(int viewId) {
        WeakReference<View> weakReference = mViews.get(viewId);
        View view = null;
        if (weakReference != null) {
            view = weakReference.get();
        }
        if (view == null) {
            view = mContentView.findViewById(viewId);
            if (view != null) {
                mViews.put(viewId, new WeakReference<>(view));
            }
        }
        return (T) view;
    }

    public void setContentView(View contentView) {
        this.mContentView = contentView;
    }

    public View getContentView() {
        return mContentView;
    }
}
