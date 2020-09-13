package com.peakmain.basicui.activity.utils;

import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.peakmain.basicui.MainActivity;
import com.peakmain.basicui.R;
import com.peakmain.basicui.adapter.BaseRecyclerStringAdapter;
import com.peakmain.basicui.base.BaseActivity;
import com.peakmain.basicui.utils.ToastUtils;
import com.peakmain.ui.navigationbar.DefaultNavigationBar;
import com.peakmain.ui.recyclerview.listener.OnItemClickListener;
import com.peakmain.ui.utils.LogUtils;
import com.peakmain.ui.utils.network.HttpUtils;
import com.peakmain.ui.utils.network.callback.DownloadCallback;
import com.peakmain.ui.utils.network.callback.EngineCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author ：Peakmain
 * createTime：2020/9/13
 * mail:2726449200@qq.com
 * describe：okhttp工具类
 */
public class OkHttpActivity extends BaseActivity {
    private List<String> mBean;
    private BaseRecyclerStringAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.basic_linear_recycler_view;
    }

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        new DefaultNavigationBar.Builder(this, findViewById(R.id.view_root))
                .hideLeftText()
                .hideRightView()
                .setTitleText("okhttp网络引擎切换工具类")
                .setToolbarBackgroundColor(R.color.colorAccent)
                .create();
    }

    @Override
    protected void initData() {
        mBean = new ArrayList<>();
        mBean.add("get方法请求");
        mBean.add("post方法请求");
        mBean.add("单线程下载");
        mBean.add("多线程下载");
        mAdapter = new BaseRecyclerStringAdapter(this, mBean);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(position -> {
            switch (position) {
                case 0:
                    HttpUtils.with(OkHttpActivity.this)
                            .url("http://i.jandan.net/")
                            .addParams("oxwlxojflwblxbsapi", "jandan.get_pic_comments")
                            .addParams("page", "1")
                            .execture(new EngineCallBack() {
                                @Override
                                public void onError(Exception e) {
                                    LogUtils.e(e.getMessage());
                                }

                                @Override
                                public void onSuccess(String result) {
                                    ToastUtils.showShort(result);
                                }

                            });
                    break;
                case 1:
                    HttpUtils.with(OkHttpActivity.this)
                            .url("https://www.wanandroid.com/user/login")
                            .addParams("username", "peakmain")
                            .addParams("password", "123456")
                            .post()
                            .execture(new EngineCallBack() {
                                @Override
                                public void onError(Exception e) {
                                    LogUtils.e(e.getMessage());
                                }

                                @Override
                                public void onSuccess(String result) {
                                    ToastUtils.showShort(result);
                                }
                            });
                    break;
                case 2:
                    File file = new File(Environment.getExternalStorageDirectory(), "test.apk");
                    if (file.exists()) {
                        file.delete();
                    }
                    HttpUtils.with(OkHttpActivity.this)
                            .url("http://imtt.dd.qq.com/16891/apk/87B3504EE9CE9DC51E9F295976F29724.apk")
                            .downloadSingle()
                            .file(file)
                            .exectureDownload(new DownloadCallback() {
                                @Override
                                public void onFailure(Exception e) {
                                    LogUtils.e(e.getMessage());
                                }

                                @Override
                                public void onSucceed(File file) {
                                    ToastUtils.showShort("file下载完成");
                                    LogUtils.e("文件保存的位置:" + file.getAbsolutePath());
                                }

                                @Override
                                public void onProgress(int progress) {
                                    LogUtils.e("单线程下载apk的进度:" + progress);
                                }
                            });
                    break;
                case 3:
                    file = new File(Environment.getExternalStorageDirectory(), "test.apk");
                    if (file.exists()) {
                        file.delete();
                    }
                    HttpUtils.with(OkHttpActivity.this)
                            .url("http://imtt.dd.qq.com/16891/apk/87B3504EE9CE9DC51E9F295976F29724.apk")
                            .downloadMutil()
                            .file(file)
                            .exectureDownload(new DownloadCallback() {
                                @Override
                                public void onFailure(Exception e) {
                                    LogUtils.e(e.getMessage());
                                }

                                @Override
                                public void onSucceed(File file) {
                                    LogUtils.e(file.getAbsolutePath() + "," + file.getName());
                                    Toast.makeText(OkHttpActivity.this, "下载完成", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onProgress(int progress) {
                                   LogUtils.e(progress + "%");
                                }
                            });
                    break;
            }
        });
    }
}
