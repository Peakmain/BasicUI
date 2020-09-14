package com.peakmain.basicui.activity.utils;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.peakmain.basicui.R;
import com.peakmain.basicui.activity.home.recylcer.bean.GroupBean;
import com.peakmain.basicui.activity.home.recylcer.data.PesudoImageData;
import com.peakmain.basicui.adapter.BaseRecyclerStringAdapter;
import com.peakmain.basicui.base.BaseActivity;
import com.peakmain.ui.dialog.AlertDialog;
import com.peakmain.ui.imageLoader.ImageLoader;
import com.peakmain.ui.imageLoader.glide.GlideLoader;
import com.peakmain.ui.navigationbar.DefaultNavigationBar;
import com.peakmain.ui.recyclerview.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/9/14
 * mail:2726449200@qq.com
 * describe：
 */
public class GlideActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private List<String> mBean;
    private BaseRecyclerStringAdapter mAdapter;
    private ImageView mImageView;

    @Override
    protected int getLayoutId() {
        return R.layout.basic_linear_recycler_view;
    }

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mImageView = findViewById(R.id.imageView);
        new DefaultNavigationBar.Builder(this, findViewById(android.R.id.content))
                .hideLeftText()
                .hideRightView()
                .setTitleText("Glide图片切换封装")
                .setToolbarBackgroundColor(R.color.colorAccent)
                .create();
    }

    @Override
    protected void initData() {
        mBean = new ArrayList<>();
        mBean.add("glide简单使用");
        mBean.add("glide默认占位图");
        mBean.add("glide圆角图片");
        mBean.add("glide指定图片的大小");
        mAdapter = new BaseRecyclerStringAdapter(this, mBean);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        mRecyclerView.setAdapter(mAdapter);
        List<GroupBean> data = PesudoImageData.getInstance().getData();
        mAdapter.setOnItemClickListener(position -> {
            switch (position) {
                case 0:
                    ImageLoader.getInstance().displayImage(this, data.get(0).getUrl(), mImageView);
                    break;
                case 1:
                    ImageLoader.getInstance().displayImage(this, data.get(1).getUrl(), mImageView, R.mipmap.ic_default_portrait);
                    break;
                case 2:
                    ImageLoader.getInstance().displayImageRound(this, data.get(2).getUrl(), mImageView,50 ,0);
                    break;
                case 3:
                    ImageLoader.getInstance().displayImage(this,data.get(4).getUrl(),mImageView,800,800,0);
                    break;
                default:
                    break;
            }
        });
    }
}
