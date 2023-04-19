package com.peakmain.basicui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.peakmain.basicui.R;
import com.peakmain.basicui.bean.CategoryRightBean;
import com.peakmain.ui.adapter.menu.BaseListMenuAdapter;
import com.peakmain.ui.recyclerview.listener.OnItemClickListener;

import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/3/4
 * mail:2726449200@qq.com
 * describe：
 */
public class ListMenuAdapter extends BaseListMenuAdapter {
    private final Context mContext;
    private final List<String> mLeftMenuList;
    private final List<CategoryRightBean> mCategoryRightBeans;
    private final List<String> mRecommendSortList;
    private final List<String> mBrandList;
    private final List<String> mCityList;

    public ListMenuAdapter(Context context, List<String> titles, List<String> recommendSortList
            , List<String> brandList, List<String> cityList
            , List<String> leftMenuList, List<CategoryRightBean> categoryRightBeans) {
        super(context, titles);
        this.mContext = context;
        this.mRecommendSortList = recommendSortList;
        this.mBrandList = brandList;
        this.mCityList = cityList;
        this.mLeftMenuList = leftMenuList;
        this.mCategoryRightBeans = categoryRightBeans;
    }

    @Override
    public void openMenu(@NonNull View tabView) {
        super.openMenu(tabView);
        ((ImageView) tabView.findViewById(R.id.iv_down)).setImageResource(R.drawable.ic_triangle_up);
    }

    @Override
    public void closeMenu(@NonNull View tabView) {
        TextView textView = tabView.findViewById(R.id.tv_menu_tab_title);
        textView.setTextColor(ContextCompat.getColor(mContext,
                R.color.color_272A2B));
        ((ImageView) tabView.findViewById(R.id.iv_down)).setImageResource(R.drawable.ic_triangle_down);
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
            TextView tvBrandTitle = menuView.findViewById(R.id.tv_brand_title);
            tvBrandTitle.setText("品牌偏好");
            RecyclerView recyclerView = menuView.findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
            recyclerView.setAdapter(new MenuBrandAdapter(mContext, mBrandList));
        } else if (position == 2) {
            TextView tvBrandTitle = menuView.findViewById(R.id.tv_brand_title);
            tvBrandTitle.setText("热门住宿地");
            RecyclerView recyclerView = menuView.findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
            recyclerView.setAdapter(new MenuHotCityAdapter(mContext, mCityList));
        } else {
            RecyclerView rvLeft = menuView.findViewById(R.id.rv_left);
            rvLeft.setLayoutManager(new LinearLayoutManager(mContext));
            MenuLeftRecyclerAdapter leftRecyclerAdapter =
                    new MenuLeftRecyclerAdapter(mContext, mLeftMenuList);
            rvLeft.setAdapter(leftRecyclerAdapter);

            RecyclerView rvRight = menuView.findViewById(R.id.rv_right);
            rvRight.setLayoutManager(new LinearLayoutManager(mContext));
            rvRight.setAdapter(new MenuRightRecyclerAdapter(mContext, mCategoryRightBeans));
            LinearLayoutManager rvRightLayoutManager = (LinearLayoutManager) rvRight.getLayoutManager();
            leftRecyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    int selectPosition = leftRecyclerAdapter.mSelectPosition;
                    if (selectPosition == position) return;
                    leftRecyclerAdapter.setSelectItem(position);

                    rvRightLayoutManager
                            .scrollToPositionWithOffset(position, 0);
                }
            });
            rvRight.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int firstVisibleItemPosition = rvRightLayoutManager.findFirstVisibleItemPosition();
                    if (firstVisibleItemPosition != -1) {
                        rvLeft.smoothScrollToPosition(firstVisibleItemPosition);
                        leftRecyclerAdapter.setSelectItem(firstVisibleItemPosition);
                    }
                }
            });
        }
    }


    @Override
    protected int getMenuLayoutId(int position) {
        if (position == 0)
            return R.layout.layout_menu_recommend_sort;
        else if (position == 1 || position == 2)
            return R.layout.layout_menu_brand;
        else
            return R.layout.layout_categorize_screen;
    }
}
