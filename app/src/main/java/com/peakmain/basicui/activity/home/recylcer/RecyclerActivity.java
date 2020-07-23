package com.peakmain.basicui.activity.home.recylcer;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;

import com.peakmain.basicui.R;
import com.peakmain.basicui.activity.home.recylcer.activity.GridGroupingActivity;
import com.peakmain.basicui.activity.home.recylcer.activity.ItemTouchActivity;
import com.peakmain.basicui.activity.home.recylcer.activity.LinearGroupingActivity;
import com.peakmain.basicui.activity.home.recylcer.activity.MultiStateLayoutActivity;
import com.peakmain.basicui.activity.home.recylcer.activity.PullDownActivity1;
import com.peakmain.basicui.activity.home.recylcer.activity.PullDownActivity2;
import com.peakmain.basicui.activity.home.recylcer.activity.SuspenisonGridActivity;
import com.peakmain.basicui.activity.home.recylcer.activity.SuspenisonLinearActivity;
import com.peakmain.basicui.adapter.BaseRecyclerStringAdapter;
import com.peakmain.basicui.base.BaseActivity;
import com.peakmain.basicui.utils.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/3/22
 * mail:2726449200@qq.com
 * describe：
 */
public class RecyclerActivity extends BaseActivity {
    private BaseRecyclerStringAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private List<String> mData;

    @Override
    protected int getLayoutId() {
        return R.layout.basic_linear_recycler_view;
    }

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mNavigationBuilder.setTitleText("Recycleview的使用").create();

    }

    @Override
    protected void initData() {
        mData = new ArrayList<>();
        mData.add("GridLayout分组");
        mData.add("LinearLayout分组");
        mData.add("LinearLayout的悬浮");
        mData.add("GridLayout分组的悬浮");
        mData.add("itemTouchHelp方法实现删除和拖拽");
        mData.add("Recycleriew封装的下拉刷新和加载更多1");
        mData.add("Recycleriew封装的下拉刷新和加载更多2");
        mData.add("多状态布局");
        mAdapter = new BaseRecyclerStringAdapter(this, mData);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(position -> {
            switch (position) {
                case 0:
                    ActivityUtil.gotoActivity(this, GridGroupingActivity.class);
                    break;
                case 1:
                    ActivityUtil.gotoActivity(this, LinearGroupingActivity.class);
                    break;
                case 2:
                    ActivityUtil.gotoActivity(this, SuspenisonLinearActivity.class);
                    break;
                case 3:
                    ActivityUtil.gotoActivity(this, SuspenisonGridActivity.class);
                    break;
                case 4:
                    ActivityUtil.gotoActivity(this, ItemTouchActivity.class);
                    break;
                case 5:
                    ActivityUtil.gotoActivity(this, PullDownActivity1.class);
                    break;
                case 6:
                    ActivityUtil.gotoActivity(this, PullDownActivity2.class);
                    break;
                case 7:
                    ActivityUtil.gotoActivity(this, MultiStateLayoutActivity.class);
                    break;
                default:
                    break;
            }
        });
    }
}
