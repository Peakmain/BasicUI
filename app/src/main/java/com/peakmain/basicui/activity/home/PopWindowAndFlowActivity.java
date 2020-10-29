package com.peakmain.basicui.activity.home;

import android.widget.Button;
import android.widget.TextView;

import com.peakmain.basicui.R;
import com.peakmain.basicui.adapter.FlowLayoutAdapter;
import com.peakmain.basicui.base.BaseActivity;
import com.peakmain.ui.widget.CustomPopupWindow;
import com.peakmain.ui.widget.FlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/3/11
 * mail:2726449200@qq.com
 * describe：
 */
public class PopWindowAndFlowActivity extends BaseActivity {
    private FlowLayout mFlowLayout;
    private Button mButton;
    private FlowLayoutAdapter mAdapter;
    private List<String> mList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pop_window_flow;

    }

    @Override
    protected void initView() {
        mButton = findViewById(R.id.bt_test);
        mFlowLayout = findViewById(R.id.flow_layout);
        mNavigationBuilder
                .setTitleText("popwindow和flowlayout的使用")
                .create();
    }

    @Override
    protected void initData() {
        mList = new ArrayList<>();
        for (int i = 8; i < 50; i++) {
            mList.add("自定" + i + "标签");
        }
        showButton();
        showFlowLayout();
    }

    private void showFlowLayout() {
        mAdapter = new FlowLayoutAdapter(this, mList);
        mFlowLayout.setAdapter(mAdapter);
    }

    private void showButton() {
        TextView textView = new TextView(this);
        textView.setText(R.string.app_name);

        CustomPopupWindow customPopupWindow = new CustomPopupWindow.PopupWindowBuilder(this)
                .setView(textView)
                .setBgDarkAlpha(0.7f)
                .create();

        mButton.setOnClickListener(customPopupWindow::showAsDropDown);
    }
}
