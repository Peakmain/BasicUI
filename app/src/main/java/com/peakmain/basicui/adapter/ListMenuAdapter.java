package com.peakmain.basicui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.peakmain.basicui.R;
import com.peakmain.ui.adapter.menu.BaseListMenuAdapter;

import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/3/4
 * mail:2726449200@qq.com
 * describe：
 */
public class ListMenuAdapter extends BaseListMenuAdapter {
    private final List<String> mTitles;
    private final Context mContext;
    private List<String> mRecommendSortList;
    private List<String> mBrandList;

    public ListMenuAdapter(Context context, List<String> titles, List<String> recommendSortList,List<String>brandList) {
        super(context, titles);
        this.mTitles = titles;
        this.mContext = context;
        this.mRecommendSortList = recommendSortList;
        this.mBrandList=brandList;
    }

    @Override
    public int getTitleLayoutId() {
        return R.layout.ui_list_data_screen_tab;
    }

    @Override
    protected void setMenuContent(View menuView, final int position) {
        if (menuView == null) return;
        if (position == 0) {
            RecyclerView recyclerView = menuView.findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.setAdapter(new MenuRecommendSortAdapter(mContext, mRecommendSortList));
        } else if (position == 1) {
            RecyclerView recyclerView = menuView.findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
            recyclerView.setAdapter(new MenuBrandAdapter(mContext, mBrandList));
        } else {
            TextView tv = menuView.findViewById(R.id.tv_menu_tab_content);
            tv.setText(mTitles.get(position));
            tv.setOnClickListener(v -> {
                Toast.makeText(mContext, mTitles.get(position), Toast.LENGTH_LONG).show();
                closeMenu();
            });
        }
    }


    @Override
    protected int getMenuLayoutId(int position) {
        if (position == 0)
            return R.layout.layout_menu_recommend_sort;
        else if (position == 1)
            return R.layout.layout_menu_brand;
        else
            return R.layout.ui_list_data_screen_menu_100;
    }
}
