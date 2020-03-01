package com.peakmain.ui.navigationbar;

import android.view.View;
import android.view.ViewGroup;

/**
 * @author ：Peakmain
 * version ：1.0
 * createTime ：2018/7/27  下午 3:14
 * mail : 2726449200@qq.com
 * describe ：
 */
public interface INavigation {
    /**
     * 创建View
     */
     void createNavigationBar();
    /**
     * 添加参数
     */
     void attachNavigationParams();
     void attachParent(View navigationBarView, ViewGroup parent);
}
