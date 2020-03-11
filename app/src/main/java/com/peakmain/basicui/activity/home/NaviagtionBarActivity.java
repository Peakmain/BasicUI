package com.peakmain.basicui.activity.home;

import android.view.ViewGroup;

import com.peakmain.basicui.R;
import com.peakmain.basicui.base.BaseActivity;
import com.peakmain.basicui.view.RadioCancelButton;
import com.peakmain.ui.navigationbar.DefaultNavigationBar;
import com.peakmain.ui.widget.ShapeTextView;

/**
 * author ：Peakmain
 * createTime：2020/3/11
 * mail:2726449200@qq.com
 * describe：
 */
public class NaviagtionBarActivity extends BaseActivity {
    /**
     * 显示右边箭头
     */
    private RadioCancelButton mRadioShowRightArrow;
    /**
     * 设置左边文字为"\"返回\""并设置颜色为white
     */
    private RadioCancelButton mRadioSetLeftText;
    /**
     * 隐藏左边文字
     */
    private RadioCancelButton mRadioHideLeftText;
    /**
     * 隐藏标题
     */
    private RadioCancelButton mRadioHideTitle;

    /**
     * 修改背景颜色
     */
    private RadioCancelButton mRadioAlertBackgroundColor;
    /**
     * 显示返回按钮
     */
    private RadioCancelButton mRadioShowBackButton;
    /**
     * 显示toolbar中默认自带的title
     */
    private RadioCancelButton mRadioShowToolbarTitle;
    /**
     * 修改右边图片资源
     */
    private RadioCancelButton mRadioAlertRightImage;
    /**
     * 确定
     */
    private ShapeTextView mStvSure;
    private DefaultNavigationBar.Builder mBuilder;
    private DefaultNavigationBar mNavigationBar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_navigation_bar;
    }

    @Override
    protected void initView() {
        mRadioShowRightArrow = findViewById(R.id.radio_show_right_arrow);
        mRadioSetLeftText = findViewById(R.id.radio_set_left_text);
        mRadioHideLeftText = findViewById(R.id.radio_hide_left_text);
        mRadioHideTitle = findViewById(R.id.radio_hide_title);
        mRadioAlertBackgroundColor = findViewById(R.id.radio_alert_background_color);
        mRadioShowBackButton = findViewById(R.id.radio_show_back_button);
        mRadioShowToolbarTitle = findViewById(R.id.radio_show_toolbar_title);
        mRadioAlertRightImage = findViewById(R.id.radio_alert_right_image);
        mStvSure = findViewById(R.id.stv_sure);
        mStvSure.setOnClickListener(v -> showNavigationBar());
    }

    /**
     * 显示navigationbar
     */
    private void showNavigationBar() {
        boolean radioShowRightArrowChecked = mRadioShowRightArrow.isChecked();
        boolean radioSetLeftTextChecked = mRadioSetLeftText.isChecked();
        boolean radioHideLeftTextChecked = mRadioHideLeftText.isChecked();
        boolean radioHideTitleChecked = mRadioHideTitle.isChecked();
        boolean radioAlertBackgroundColorChecked = mRadioAlertBackgroundColor.isChecked();
        boolean radioShowBackButtonChecked = mRadioShowBackButton.isChecked();
        boolean radioShowToolbarTitleChecked = mRadioShowToolbarTitle.isChecked();
        boolean radioAlertRightImageChecked = mRadioAlertRightImage.isChecked();



        if (mNavigationBar != null) {
            ViewGroup parent = (ViewGroup) mRadioShowRightArrow.getParent();
            if (parent != null) {
                parent.removeViewAt(0);
            }
        }
        mBuilder = new DefaultNavigationBar
                .Builder(this, findViewById(R.id.view_root))
        .setLeftText("左边");

        if (!radioShowRightArrowChecked) {
            mBuilder.hideRightView();
        }
        if (radioHideLeftTextChecked) {
            mBuilder.hideLeftText();
        } else if (radioSetLeftTextChecked) {
            mBuilder.setLeftText("返回");
        }
        if (radioHideTitleChecked) {
            mBuilder.hideTitleText();
        } else {
            mBuilder.setTitleText("navigationBar的使用");
        }
        if (radioAlertBackgroundColorChecked) {
            mBuilder.setToolbarBackgroundColor(R.color.colorAccent);
        }
        mBuilder.setDisplayHomeAsUpEnabled(radioShowBackButtonChecked)
                .setNavigationOnClickListener(v -> finish());

        mBuilder.setDisplayShowTitleEnabled(radioShowToolbarTitleChecked);

        if (radioAlertRightImageChecked) {
            mBuilder.setRightResId(R.drawable.ic_et_close);
        }

        mNavigationBar = mBuilder.create();
    }

    @Override
    protected void initData() {

    }

}
