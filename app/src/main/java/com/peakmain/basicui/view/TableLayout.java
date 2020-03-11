package com.peakmain.basicui.view;

import android.content.Context;
import android.util.AttributeSet;

import com.peakmain.ui.tablayout.BaseTabLayout;

import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/3/11
 * mail:2726449200@qq.com
 * describe：
 */
public class TableLayout extends BaseTabLayout<String> {

    public TableLayout(Context context) {
        super(context);
    }

    public TableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public String setTableTitle(List<String> bean, int position) {
        return bean.get(position);
    }
}
