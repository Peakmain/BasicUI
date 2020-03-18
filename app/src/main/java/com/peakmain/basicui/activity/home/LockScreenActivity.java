package com.peakmain.basicui.activity.home;

import com.peakmain.basicui.R;
import com.peakmain.basicui.base.BaseActivity;
import com.peakmain.basicui.utils.ToastUtils;
import com.peakmain.ui.widget.lock.LockScreenView;

/**
 * author ：Peakmain
 * createTime：2020/3/15
 * mail:2726449200@qq.com
 * describe：锁屏的activity
 */
public class LockScreenActivity extends BaseActivity {
    private LockScreenView mLockScreenView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_lock_pattern;
    }

    @Override
    protected void initView() {
        mLockScreenView = findViewById(R.id.lock_pattern);
        mNavigationBuilder.setTitleText("九宫格解锁").create();
        //mLockScreenView.hideArrow();
    }

    @Override
    protected void initData() {
        mLockScreenView.setOnLockSuccessListener(new LockScreenView.OnLockSuccessListener() {

            @Override
            public String getLockResult() {
                return "123456";
            }

            @Override
            public void onLockSuccess(String pwd) {
                ToastUtils.showShort("密码正确，正在探索BasicUI......");
                finish();
            }
        });
    }
}
