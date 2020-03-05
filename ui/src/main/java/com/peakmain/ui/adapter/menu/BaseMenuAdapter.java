package com.peakmain.ui.adapter.menu;

import android.view.View;
import android.view.ViewGroup;

import com.peakmain.ui.adapter.BaseAdapter;

/**
 * author ：Peakmain
 * createTime：2020/3/4
 * mail:2726449200@qq.com
 * describe：多条目菜单筛选的适配器
 */
public abstract class BaseMenuAdapter extends BaseAdapter {
    //订阅用户放到这个集合
    private MenuObserver mObserver;

    /**
     * 订阅注册
     */
    public void registerDataSetObserver(MenuObserver observer) {
        mObserver = observer;
    }

    /**
     * 解注册
     */
    public void unregisterDataSetObserver(MenuObserver observer) {
        mObserver = null;
    }

    public void closeMenu() {
        if (mObserver != null) {
            mObserver.closeMenu();
        }
    }
    /**
     * 获取Menu的view
     */
    public abstract View getMenuView(int position, ViewGroup parent);

    /**
     * 打开菜单
     */
    public abstract void openMenu(View tabView);

    /**
     * 关闭菜单
     */
    public abstract void closeMenu(View tabView);
}
