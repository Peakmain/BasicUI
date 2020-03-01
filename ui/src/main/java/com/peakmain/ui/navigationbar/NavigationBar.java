package com.peakmain.ui.navigationbar;

import android.content.Context;
import android.view.ViewGroup;

/**
 * @author ：Peakmain
 * version ：1.0
 * createTime ：2018/11/26  下午 12:17
 * mail : 2726449200@qq.com
 * describe ：
 */
public class NavigationBar extends AbsNavigationBar {
    NavigationBar(Builder builder) {
        super(builder);
    }
    public static class Builder extends AbsNavigationBar.Builder<NavigationBar.Builder> {

        public Builder(Context context, int layoutId, ViewGroup parent) {
            super(context, layoutId, parent);
        }

        @Override
        public NavigationBar create() {
            return new NavigationBar(this);
        }
    }
}
