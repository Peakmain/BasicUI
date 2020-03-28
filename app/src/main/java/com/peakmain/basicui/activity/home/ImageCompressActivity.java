package com.peakmain.basicui.activity.home;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.peakmain.basicui.BuildConfig;
import com.peakmain.basicui.R;
import com.peakmain.ui.compress.ImageCompressUtils;
import com.peakmain.ui.compress.OnCompressListener;

import java.util.ArrayList;
import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/3/27
 * mail:2726449200@qq.com
 * describe：
 */
public class ImageCompressActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mIvImage;
    /**
     * 压缩
     */
    private Button mBtCompress;
    private TextView mTvResult;
    private Bitmap mBitmap;
    private String mPath;
    private List<String> mImageLists;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_compress);
        initView();
        initData();
    }

    protected void initView() {
        mIvImage = findViewById(R.id.iv_image);
        mBtCompress = findViewById(R.id.bt_compress);
        mBtCompress.setOnClickListener(this);
        mTvResult = findViewById(R.id.tv_result);
    }

    protected void initData() {
        mImageLists = new ArrayList<>();
        String directory = Environment.getExternalStorageDirectory()+"/截屏";
        String path1 = directory + "/1.jpg";
        String path2 = directory + "/2.jpg";
        String path3 = directory + "/3.jpg";
        mImageLists.add(path1);
        mImageLists.add(path2);
        mImageLists.add(path3);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_compress:
                //设置输出文件目录
                String directory = Environment.getExternalStorageDirectory() + "/peakmain";
                ImageCompressUtils.with(this)
                        .load(mImageLists)//设置加载图片集合
                        .ignoreCompress(100)//设置忽略的图片大小单位是kb
                        .setQuality(90)//设置压缩质量
                        .setOutFileDir(directory)//设置输出文件目录
                        .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {
                                Log.e(BuildConfig.TAG,"开始压缩");
                            }

                            @Override
                            public void onSuccess(List<String> list) {
                                Log.e(BuildConfig.TAG,"压缩完成");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(BuildConfig.TAG,"压缩错误"+e.getMessage());
                            }
                        }).startCompress();
                break;
            default:
                break;
        }
    }
}
