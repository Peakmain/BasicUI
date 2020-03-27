package com.peakmain.basicui.activity.home;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.peakmain.basicui.R;
import com.peakmain.ui.utils.ImageCompressUtils;

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
        mPath = Environment.getExternalStorageDirectory() + "/截屏/test.jpg";
        mBitmap = ImageCompressUtils.getInstance().decodeFile(mPath);
        mIvImage.setImageBitmap(mBitmap);
        int byteCount = mBitmap.getByteCount();
        mTvResult.setText(byteCount+"");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_compress:
                ImageCompressUtils.getInstance().compressImage(mBitmap, 60, mPath);
                /*mIvImage.setImageBitmap(mBitmap);
                mTvResult.setText(mBitmap.getByteCount()+"");*/
                break;
            default:
                break;
        }
    }
}
