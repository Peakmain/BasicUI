package com.peakmain.ui;

import android.app.Application;
import android.support.v4.content.FileProvider;

import com.peakmain.ui.constants.BasicUIUtils;

/**
 * author ：Peakmain
 * createTime：2020/12/24
 * mail:2726449200@qq.com
 * describe：
 */
public class BasicUIProvider extends FileProvider {
    @Override
    public boolean onCreate() {
        BasicUIUtils.init((Application) getContext().getApplicationContext());
        return true;
    }
}
