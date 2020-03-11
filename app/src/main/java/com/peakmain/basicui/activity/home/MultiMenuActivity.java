package com.peakmain.basicui.activity.home;

import com.peakmain.basicui.R;
import com.peakmain.basicui.adapter.ListMenuAdapter;
import com.peakmain.basicui.base.BaseActivity;
import com.peakmain.ui.widget.menu.ListMenuView;

import java.util.ArrayList;
import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/3/11
 * mail:2726449200@qq.com
 * describe：
 */
public class MultiMenuActivity extends BaseActivity {
    private ListMenuAdapter mAdapter;
    private ListMenuView mMenuView;
    private List<String> mData;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_menu_list;
    }

    @Override
    protected void initView() {
        mMenuView = findViewById(R.id.list_data_screen_view);
        mNavigationBuilder.setTitleText("仿58同城多条目菜单删选").create();
    }

    @Override
    protected void initData() {
        mData = new ArrayList<>();
        mData.add("类型");
        mData.add("品牌");
        mData.add("价格");
        mData.add("更多");
        mAdapter = new ListMenuAdapter(this, mData);
        mMenuView.setAdapter(mAdapter);
    }
}
