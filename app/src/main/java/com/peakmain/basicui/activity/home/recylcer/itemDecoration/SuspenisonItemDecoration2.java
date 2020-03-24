package com.peakmain.basicui.activity.home.recylcer.itemDecoration;

import android.content.Context;

import com.peakmain.basicui.activity.home.recylcer.bean.GroupBean;
import com.peakmain.ui.recyclerview.itemdecoration.BaseSuspenisonItemDecoration2;

import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/3/23
 * mail:2726449200@qq.com
 * describe：
 */
public class SuspenisonItemDecoration2 extends BaseSuspenisonItemDecoration2<GroupBean> {
    public SuspenisonItemDecoration2(Builder builder) {
        super(builder);
    }

    @Override
    public String getTopText(List<GroupBean> data, int position) {
        return data.get(position).getTime();
    }
    public static class Builder extends BaseSuspenisonItemDecoration2.Builder<Builder,GroupBean> {
        public Builder(Context context, List<GroupBean> data) {
            super(context, data);
        }

        @Override
        public SuspenisonItemDecoration2 create() {
            return new SuspenisonItemDecoration2(this);
        }
    }
}
