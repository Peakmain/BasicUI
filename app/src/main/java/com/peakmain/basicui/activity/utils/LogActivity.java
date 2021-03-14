package com.peakmain.basicui.activity.utils;

import android.widget.Button;

import com.peakmain.basicui.R;
import com.peakmain.basicui.base.BaseActivity;
import com.peakmain.ui.utils.log.LogManager;
import com.peakmain.ui.utils.log.PLog;

/**
 * author ：Peakmain
 * createTime：2021/3/14
 * mail:2726449200@qq.com
 * describe：
 */
public class LogActivity extends BaseActivity {

    private Button mBtnPrintLog;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_log_view;

    }

    @Override
    protected void initView() {
        mBtnPrintLog = findViewById(R.id.btn_print_log);
        LogManager.getInstance().showPrintView(this);
        mBtnPrintLog.setOnClickListener(v -> {
            PLog.e("测试打印");
        });
    }

    @Override
    protected void initData() {

    }
}
