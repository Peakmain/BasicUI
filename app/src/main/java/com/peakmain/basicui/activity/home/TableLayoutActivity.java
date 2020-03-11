package com.peakmain.basicui.activity.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.peakmain.basicui.R;
import com.peakmain.basicui.fragment.ItemFragment;
import com.peakmain.basicui.base.BaseActivity;
import com.peakmain.basicui.view.TableLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/3/11
 * mail:2726449200@qq.com
 * describe：
 */
public class TableLayoutActivity extends BaseActivity {
    private ViewPager mViewPager;
    private TableLayout mTabLayout;
    private List<String> mData;
    private String[] arr = {"新闻","直播", "推荐","抗击肺炎", "视频", "图片", "段子", "精华"};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_table_layout;
    }

    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.view_pager);
        mTabLayout = findViewById(R.id.tab_layout);
        mNavigationBuilder.setTitleText("仿今日头条的TableLayout").create();
    }

    @Override
    protected void initData() {
        mData = new ArrayList<>();
        mData = Arrays.asList(arr);
        mViewPager.setAdapter(new FragmentPagerAdapter(this.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

                return ItemFragment.newInstance(arr[position]);
            }

            @Override
            public int getCount() {
                return arr.length;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                super.destroyItem(container, position, object);
            }

        });
        mTabLayout.initIndicator(mData, mViewPager);
        mTabLayout.setOnTabItemClickListener(position -> mViewPager.setCurrentItem(position));
    }
}
