package com.peakmain.basicui.adapter;

import android.content.Context;

import com.peakmain.basicui.R;
import com.peakmain.ui.recyclerview.adapter.CommonRecyclerAdapter;
import com.peakmain.ui.recyclerview.adapter.ViewHolder;

import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/3/9
 * mail:2726449200@qq.com
 * describe：
 */
public class BaseRecyclerStringAdapter extends CommonRecyclerAdapter<String> {
    public BaseRecyclerStringAdapter(Context context, List<String> data) {
        super(context, data, R.layout.item_recyclerview_home);
    }

    @Override
    public void convert(ViewHolder holder, String item) {
        holder.setText(R.id.tv_title, item);
    }
}
