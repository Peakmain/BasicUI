package com.peakmain.basicui.activity.home;

import android.util.Log;
import android.view.View;

import com.peakmain.basicui.BuildConfig;
import com.peakmain.basicui.R;
import com.peakmain.basicui.base.BaseActivity;
import com.peakmain.basicui.view.RadioCancelButton;
import com.peakmain.basicui.utils.ToastUtils;
import com.peakmain.ui.dialog.AlertDialog;
import com.peakmain.ui.navigationbar.DefaultNavigationBar;
import com.peakmain.ui.widget.ShapeTextView;

/**
 * author ：Peakmain
 * createTime：2020/3/9
 * mail:2726449200@qq.com
 * describe：
 */
public class DialogActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 是否设置宽度全屏
     */
    private RadioCancelButton mRadioSetWidthFull;
    /**
     * 是否添加默认动画
     */
    private RadioCancelButton mRadioSetDefaultAnimation;
    /**
     * 是否从底部弹出
     */
    private RadioCancelButton mRadioSetFromBottom;
    /**
     * 点击外部是否可以取消
     */
    private RadioCancelButton mRadioSetCanCancel;
    /**
     * 确定
     */
    private ShapeTextView mStvSure;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dialog_demo;
    }

    @Override
    public void initView() {
        mRadioSetWidthFull = findViewById(R.id.radio_set_width_full);
        mRadioSetDefaultAnimation = findViewById(R.id.radio_set_default_animation);
        mRadioSetFromBottom = findViewById(R.id.radio_set_from_bottom);
        mRadioSetCanCancel = findViewById(R.id.radio_set_can_cancel);
        mStvSure = findViewById(R.id.stv_sure);
        mStvSure.setOnClickListener(this);
        //添加NavigationBar
        new DefaultNavigationBar.Builder(this, findViewById(R.id.view_root))
                .setDisplayHomeAsUpEnabled(true)
                .setToolbarBackgroundColor(R.color.colorAccent)
                .setTitleText("dialog的使用")
                .setNavigationOnClickListener(v -> finish())
                .hideRightView()
                .create();
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stv_sure:
                showDialog();
                break;
            default:
                break;
        }
    }

    private void showDialog() {
        boolean radioSetWidthFullChecked = mRadioSetWidthFull.isChecked();
        boolean radioSetDefaultAnimationChecked = mRadioSetDefaultAnimation.isChecked();
        boolean radioSetFromBottomChecked = mRadioSetFromBottom.isChecked();
        boolean radioSetCanCancelChecked = mRadioSetCanCancel.isChecked();
        Log.e(BuildConfig.TAG, radioSetWidthFullChecked + "," + radioSetDefaultAnimationChecked + ","
                + radioSetFromBottomChecked + "," + radioSetCanCancelChecked);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setContentView(R.layout.dialog_show)
                .setOnClickListener(R.id.tv_save_image, v1 -> ToastUtils.showShort("保存图片成功"))
                .setOnClickListener(R.id.tv_forword, v13 -> ToastUtils.showShort("转发"));

        builder.setCancelable(radioSetCanCancelChecked);
        if (radioSetWidthFullChecked) {
            builder.setFullWidth();
        }
        if (radioSetDefaultAnimationChecked) {
            builder.addDefaultAnimation();
        }
        if (radioSetFromBottomChecked) {
            builder.fromButtom(radioSetFromBottomChecked);
        }
        AlertDialog dialog = builder.show();
        dialog.setOnClickListener(R.id.tv_cancel, v12 -> dialog.dismiss());
    }
}
