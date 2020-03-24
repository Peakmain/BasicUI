package com.peakmain.basicui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.peakmain.basicui.R;
import com.peakmain.basicui.activity.home.recylcer.bean.GroupBean;
import com.peakmain.ui.imageLoader.ImageLoader;
import com.peakmain.ui.recyclerview.adapter.ViewHolder;
import com.peakmain.ui.recyclerview.group.BaseGroupRecyclerAdapter;

import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/3/22
 * mail:2726449200@qq.com
 * describe：
 */
public class GroupLinearAdapter extends BaseGroupRecyclerAdapter<GroupBean> {
    public GroupLinearAdapter(Context context, List<GroupBean> data) {
        super(context, data, R.layout.item_recycler_group, R.layout.item_group_head);
    }

    @Override
    protected void convertHead(ViewHolder holder, GroupBean item) {
        holder.setText(R.id.tv_index, item.header);
    }

    @Override
    public void convert(ViewHolder holder, GroupBean item) {
        ImageView imageView = holder.getView(R.id.iv_image);
        ImageLoader.getInstance().displayImage(mContext, item.getUrl(), imageView, 0);
    }
}
