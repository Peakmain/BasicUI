package com.peakmain.basicui.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.peakmain.basicui.R;
import com.peakmain.basicui.activity.home.AutoEditTextActivity;
import com.peakmain.basicui.activity.home.CustomPasswordKeyboardActivity;
import com.peakmain.basicui.activity.home.DataBaseActivity;
import com.peakmain.basicui.activity.home.DialogActivity;
import com.peakmain.basicui.activity.home.ImageCompressActivity;
import com.peakmain.basicui.activity.home.LockScreenActivity;
import com.peakmain.basicui.activity.home.MultiMenuActivity;
import com.peakmain.basicui.activity.home.NaviagtionBarActivity;
import com.peakmain.basicui.activity.home.PopWindowAndFlowActivity;
import com.peakmain.basicui.activity.home.recylcer.RecyclerActivity;
import com.peakmain.basicui.activity.home.ShapeTextViewActivity;
import com.peakmain.basicui.activity.home.TableLayoutActivity;
import com.peakmain.basicui.activity.home.loading.LoadingActivity;
import com.peakmain.basicui.adapter.BaseRecyclerStringAdapter;
import com.peakmain.basicui.base.BaseFragmnet;
import com.peakmain.basicui.utils.ActivityUtil;
import com.peakmain.ui.navigationbar.DefaultNavigationBar;
import com.peakmain.ui.recyclerview.itemdecoration.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/3/9
 * mail:2726449200@qq.com
 * describe：
 */
public class HomeFragment extends BaseFragmnet {
    private RecyclerView mRecyclerView;

    private List<String> mHomeDataBean;
    private BaseRecyclerStringAdapter mAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
        new DefaultNavigationBar.Builder(getContext(), view.findViewById(R.id.view_root))
                .hideLeftText()
                .hideRightView()
                .setTitleText("首页")
                .setToolbarBackgroundColor(R.color.colorAccent)
                .create();
    }

    @Override
    protected void initData() {
        mHomeDataBean = new ArrayList<>();
        mHomeDataBean.add("Dialog");
        mHomeDataBean.add("NavigationBar");
        mHomeDataBean.add("PopupWindow+流式布局");
        mHomeDataBean.add("AudioDeleteEditText");
        mHomeDataBean.add("ShapeTextView");
        mHomeDataBean.add("仿今日头条的TableLayout");
        mHomeDataBean.add("loading效果");
        mHomeDataBean.add("仿58同城多条目菜单删选");
        mHomeDataBean.add("自定义支付密码键盘");
        mHomeDataBean.add("九宫格解锁");
        mHomeDataBean.add("RecyclerView的使用");
        mHomeDataBean.add("图片压缩");
        mHomeDataBean.add("数据库");

        mAdapter = new BaseRecyclerStringAdapter(getContext(), mHomeDataBean);
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(position -> {
            switch (position) {
                case 0:
                    //dialog
                    ActivityUtil.gotoActivity(getContext(), DialogActivity.class);
                    break;
                case 1:
                    //navigationBar
                    ActivityUtil.gotoActivity(getContext(), NaviagtionBarActivity.class);
                    break;
                case 2:
                    ActivityUtil.gotoActivity(getContext(), PopWindowAndFlowActivity.class);
                    break;
                case 3:
                    ActivityUtil.gotoActivity(getContext(), AutoEditTextActivity.class);
                    break;
                case 4:
                    ActivityUtil.gotoActivity(getContext(), ShapeTextViewActivity.class);
                    break;
                case 5:
                    ActivityUtil.gotoActivity(getContext(), TableLayoutActivity.class);
                    break;
                case 6:
                    ActivityUtil.gotoActivity(getContext(), LoadingActivity.class);
                    break;
                case 7:
                    ActivityUtil.gotoActivity(getContext(), MultiMenuActivity.class);
                    break;
                case 8:
                    ActivityUtil.gotoActivity(getContext(), CustomPasswordKeyboardActivity.class);
                    break;
                case 9:
                    ActivityUtil.gotoActivity(getContext(), LockScreenActivity.class);
                    break;
                case 10:
                    ActivityUtil.gotoActivity(getContext(), RecyclerActivity.class);
                    break;
                case 11:
                    ActivityUtil.gotoActivity(getContext(), ImageCompressActivity.class);
                    break;
                case 12:
                    ActivityUtil.gotoActivity(getContext(), DataBaseActivity.class);
                    break;
                default:
                    break;
            }
        });
    }
}
