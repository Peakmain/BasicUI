package com.peakmain.ui.navigationbar;


import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.peakmain.ui.R;


/**
 * @author ：Peakmain
 * version ：1.0
 * createTime ：2018/11/26  下午 12:15
 * mail : 2726449200@qq.com
 * describe ：
 */
public class DefaultNavigationBar extends AbsNavigationBar<DefaultNavigationBar.Builder> {

    private ActionBar mActionBar;
    private Toolbar mToolbar;

    DefaultNavigationBar(Builder builder) {
        super(builder);
    }

    @Override
    public void attachNavigationParams() {
        super.attachNavigationParams();
        Context context = getBuilder().mContext;
        // 处理左边文字
        TextView leftView = findViewById(R.id.tv_left);
        leftView.setVisibility(getBuilder().mLeftVisible);


        //处理标题
        TextView titleView = findViewById(R.id.tv_title);

        titleView.setVisibility(getBuilder().mTitleVisible);

        if (context instanceof AppCompatActivity) {
            mToolbar = findViewById(R.id.view_root);
            ((AppCompatActivity) context).setSupportActionBar(mToolbar);
            mToolbar.setNavigationOnClickListener(getBuilder().mNavigationOnClickListener);
            try {
                mToolbar.setBackgroundColor(ContextCompat.getColor(getBuilder().mContext, getBuilder().mToolbarBackgroundColor));
            } catch (Exception e) {
                mToolbar.setBackgroundColor(getBuilder().mToolbarBackgroundColor);
            }
            mActionBar = ((AppCompatActivity) context).getSupportActionBar();
            if (mActionBar == null) {
                return;
            }
            mActionBar.setDisplayShowTitleEnabled(getBuilder().mShowTitle);
            mActionBar.setDisplayHomeAsUpEnabled(getBuilder().mShowHomeAsUp);
        }
        //右边图片处理
        ImageView rightView = findViewById(R.id.iv_right);
        rightView.setVisibility(getBuilder().mRightViewVisible);
        //右边图片设置资源
        if (getBuilder().mRightResId != 0) {
            rightView.setImageResource(getBuilder().mRightResId);
            ViewGroup.LayoutParams layoutParams = rightView.getLayoutParams();
            layoutParams.height = getBuilder().mRightResHeight;
            layoutParams.width = getBuilder().mRightResWidth;
            rightView.setLayoutParams(layoutParams);
        }
    }

    /**
     * 是否显示返回按钮
     *
     * @param showHomeAsUp true显示返回按钮 false不返回
     */
    public DefaultNavigationBar setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(showHomeAsUp);
        }
        return this;
    }

    /**
     * 是否显示返回按钮和是否显示toolbar自带的title
     *
     * @param id 标题栏背景颜色资源id
     */
    public DefaultNavigationBar setToolbarBackgroundColor(@ColorRes int id) {
        if (mToolbar != null) {
            try {
                mToolbar.setBackgroundColor(ContextCompat.getColor(getBuilder().mContext, id));
            } catch (Exception e) {
                throw new RuntimeException("Toolbar background color is vaild");
            }
        }
        return this;
    }

    /**
     * 是否显示toolbar中默认自带的title
     *
     * @param showTitleAsUp true代表显示 false代表不显示
     */
    public DefaultNavigationBar setDisplayShowTitleEnabled(boolean showTitleAsUp) {
        if (mActionBar != null) {
            mActionBar.setDisplayShowTitleEnabled(showTitleAsUp);
        }
        return this;
    }

    /**
     * 设置返回按钮的点击事件
     */
    public DefaultNavigationBar setNavigationOnClickListener(View.OnClickListener onClickListener) {
        if (mToolbar != null) {
            mToolbar.setNavigationOnClickListener(onClickListener);
        }
        return this;
    }

    /**
     * 设置左边文字
     */
    public DefaultNavigationBar setLeftText(String text) {
        ((TextView) findViewById(R.id.tv_left)).setText(text);
        return this;
    }

    /**
     * 设置左边文字的点击事件
     */
    public DefaultNavigationBar setLeftClickListener(View.OnClickListener onClickListener) {
        findViewById(R.id.tv_left).setOnClickListener(onClickListener);
        return this;
    }

    /**
     * 设置左边文字颜色
     */
    public DefaultNavigationBar setLeftTextColor(@ColorRes int colorRes) {
        ((TextView) findViewById(R.id.tv_left)).setTextColor(ContextCompat.getColor(getBuilder().mContext, colorRes));
        return this;
    }

    /**
     * 隐藏左边文字
     */
    public DefaultNavigationBar hideLeftText() {
        findViewById(R.id.tv_left).setVisibility(View.GONE);
        return this;
    }

    /**
     * 隐藏标题
     */
    public DefaultNavigationBar hideTitleText() {
        findViewById(R.id.tv_title).setVisibility(View.GONE);
        return this;
    }

    /**
     * 显示标题文字
     */
    public DefaultNavigationBar setTitleText(String title) {
        ((TextView) findViewById(R.id.tv_title)).setText(title);
        return this;
    }

    /**
     * 设置标题事件
     */
    public DefaultNavigationBar setTitleClickListener(View.OnClickListener onClickListener) {
        findViewById(R.id.tv_title).setOnClickListener(onClickListener);
        return this;
    }

    /**
     * 设置标题颜色
     */
    public DefaultNavigationBar setTitleTextColor(@ColorRes int colorRes) {
        ((TextView) findViewById(R.id.tv_title)).setTextColor(ContextCompat.getColor(getBuilder().mContext, colorRes));
        return this;
    }

    /**
     * 隐藏右边图片
     */
    public DefaultNavigationBar hideRightView() {
        findViewById(R.id.iv_right).setVisibility(View.GONE);
        return this;
    }

    /**
     * 设置右边图片的点击事件
     */
    public DefaultNavigationBar setRightViewClickListener(View.OnClickListener onClickListener) {
        findViewById(R.id.iv_right).setOnClickListener(onClickListener);
        return this;
    }

    /**
     * 设置右边资源的图片
     *
     * @param rightResId 右边资源的id
     */
    public DefaultNavigationBar setRightResId(int rightResId) {
        ((ImageView) findViewById(R.id.iv_right)).setImageResource(rightResId);
        return this;
    }

    public static class Builder extends AbsNavigationBar.Builder<DefaultNavigationBar.Builder> {

        private int mLeftVisible = View.VISIBLE;
        private DefaultNavigationBar mDefaultNavigationBar;


        private int mTitleVisible = View.VISIBLE;
        //返回按钮的点击事件
        private View.OnClickListener mNavigationOnClickListener;
        private int mToolbarBackgroundColor;
        private boolean mShowHomeAsUp;
        private boolean mShowTitle;
        private int mRightViewVisible = View.VISIBLE;

        //修改图片资源
        private int mRightResId = 0;
        //设置图片资源的高度和宽度
        private int mRightResHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        private int mRightResWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        public Builder(Context context, ViewGroup parent) {
            super(context, R.layout.ui_defualt_navigation_bar, parent);

        }


        /**
         * 设置左边文字
         *
         * @param text 文字
         */
        public Builder setLeftText(CharSequence text) {
            setText(R.id.tv_left, text);
            return this;
        }


        public Builder setLeftClickListener(View.OnClickListener onClickListener) {
            setOnClickListener(R.id.tv_left, onClickListener);
            return this;
        }

        /**
         * 设置左边文字的颜色
         */
        public Builder setLeftTextColor(int color) {
            setTextColor(R.id.tv_left, color);
            return this;
        }

        /**
         * 隐藏左边文字
         */
        public Builder hideLeftText() {
            mLeftVisible = View.GONE;
            return this;
        }

        /**
         * 设置标题文字
         */
        public Builder setTitleText(CharSequence text) {
            setText(R.id.tv_title, text);
            return this;
        }

        /**
         * 设置标题文字的点击事件
         */
        public Builder setTitleClickListener(View.OnClickListener onClickListener) {
            setOnClickListener(R.id.tv_title, onClickListener);
            return this;
        }

        /**
         * 设置标题文字的颜色
         */
        public Builder setTitleTextColor(int color) {
            setTextColor(R.id.tv_title, color);
            return this;
        }


        /**
         * 隐藏左边文字
         */
        public Builder hideTitleText() {
            mTitleVisible = View.GONE;
            return this;
        }

        /**
         * 隐藏右边图片
         */
        public Builder hideRightView() {
            mRightViewVisible = View.GONE;
            return this;
        }

        /**
         * 设置右边图片的点击事件
         */
        public Builder setRightViewClickListener(View.OnClickListener onClickListener) {
            setOnClickListener(R.id.iv_right, onClickListener);
            return this;
        }

        /**
         * 是否显示返回按钮和是否显示toolbar自带的title
         *
         * @param id 标题栏背景颜色资源id
         */
        public Builder setToolbarBackgroundColor(@ColorRes int id) {
            this.mToolbarBackgroundColor = id;
            return this;
        }

        /**
         * 是否显示返回按钮
         *
         * @param showHomeAsUp true显示返回按钮 false不返回
         */
        public Builder setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
            this.mShowHomeAsUp = showHomeAsUp;
            return this;
        }

        /**
         * 是否显示toolbar中默认自带的title
         *
         * @param showTitle true代表显示 false代表不显示
         */
        public Builder setDisplayShowTitleEnabled(boolean showTitle) {
            this.mShowTitle = showTitle;
            return this;
        }

        /**
         * 设置返回按钮的点击事件
         */
        public Builder setNavigationOnClickListener(View.OnClickListener onClickListener) {
            this.mNavigationOnClickListener = onClickListener;
            return this;
        }

        /**
         * 设置右边资源的图片
         *
         * @param rightResId 右边资源的id
         */
        public Builder setRightResId(int rightResId) {
            mRightResId = rightResId;
            return this;
        }

        /**
         * 设置右边资源的图片
         *
         * @param rightResId 右边资源的id
         */
        public Builder setRightResId(int rightResId, int rightResWidth, int rightResHeight) {
            mRightResId = rightResId;
            mRightResWidth = rightResWidth;
            mRightResHeight = rightResHeight;
            return this;
        }

        @Override
        public DefaultNavigationBar create() {
            if (mDefaultNavigationBar == null) {
                mDefaultNavigationBar = new DefaultNavigationBar(this);
            }
            return mDefaultNavigationBar;
        }

    }
}
