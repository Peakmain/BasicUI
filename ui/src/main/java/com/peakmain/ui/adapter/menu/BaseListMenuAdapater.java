package com.peakmain.ui.adapter.menu;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.peakmain.ui.R;

import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/3/4
 * mail:2726449200@qq.com
 * describe：基本条目的适配器
 */
public abstract class BaseListMenuAdapater extends BaseMenuAdapter {
    //标题的集合
    private List<String> mTitles;
    //上下文
    private Context mContext;

    public BaseListMenuAdapater(Context context, List<String> titles) {
        mContext = context;
        mTitles = titles;
    }

    @Override
    public View getView(int position, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(getTitleLayoutId(), parent, false);
        TextView textView = view.findViewById(R.id.tv_menu_tab_title);
        textView.setText(mTitles.get(position));
        return view;
    }

    public abstract int getTitleLayoutId();

    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Override
    public View getMenuView(int position, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(getMenuLayoutId(), parent, false);
        setMenuContent(view,position);
        return view;
    }

    protected abstract void setMenuContent(View menuView,int position);

    protected abstract int getMenuLayoutId();

    @Override
    public void openMenu(View tabView) {
        TextView textView = tabView.findViewById(R.id.tv_menu_tab_title);
        textView.setTextColor(Color.parseColor("#FF4081"));
    }

    @Override
    public void closeMenu(View tabView) {
        TextView textView = tabView.findViewById(R.id.tv_menu_tab_title);
        textView.setTextColor(Color.BLACK);
    }
}
