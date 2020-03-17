package com.peakmain.basicui.adapter.address;

import android.content.Context;

import com.peakmain.basicui.bean.SearchCityBean;
import com.peakmain.ui.adapter.address.BaseAddressSelectAdapter;

import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/3/16
 * mail:2726449200@qq.com
 * describe：
 */
public class JdAddressAreaSelectAdapter extends BaseAddressSelectAdapter<SearchCityBean> {
    public JdAddressAreaSelectAdapter(Context context, List<SearchCityBean> data) {
        super(context, data);
    }

    @Override
    public String getAddressText(SearchCityBean item) {
        return "区："+item.getName();
    }


}
