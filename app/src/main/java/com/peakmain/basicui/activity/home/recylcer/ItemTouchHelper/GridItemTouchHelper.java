package com.peakmain.basicui.activity.home.recylcer.ItemTouchHelper;

import android.support.v7.widget.RecyclerView;

import com.peakmain.basicui.activity.home.recylcer.bean.GroupBean;
import com.peakmain.ui.recyclerview.itemTouch.BaseItemTouchHelper;

import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/3/25
 * mail:2726449200@qq.com
 * describe：
 */
public class GridItemTouchHelper extends BaseItemTouchHelper<GroupBean> {


    public GridItemTouchHelper(RecyclerView.Adapter adapter, List<GroupBean> data) {
        super(adapter, data);
    }
}
