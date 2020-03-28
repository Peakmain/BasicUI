package com.peakmain.ui.compress;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.UiThread;
import android.text.TextUtils;
import android.util.Log;

import com.peakmain.ui.BuildConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/3/28
 * mail:2726449200@qq.com
 * describe：图片压缩工具
 */
public class ImageCompressUtils implements Handler.Callback {
    //开始
    private static final int COMPRESS_START = 101;
    //成功
    private static final int COMPRESS_SUCCESS = 102;
    //失败
    private static final int COMPRESS_ERROR = 103;

    private static final String DEFAULT_DISK_CACHE_DIR = "basic_disk_cache";
    //所有图片的路径
    private List<String> mPaths;
    private int mLeastCompressSize;
    private String mOutFileDir;
    private OnCompressListener mCompressListener;
    private Handler mHandler;
    private int mQuality;

    public ImageCompressUtils(Builder builder) {
        this.mPaths = builder.mPaths;
        this.mLeastCompressSize = builder.mLeastCompressSize;
        this.mOutFileDir = builder.mOutFileDir;
        this.mCompressListener = builder.mCompressListener;
        mQuality = builder.mQuality;
        mHandler = new Handler(Looper.getMainLooper(),this);
    }

    public static Builder with(Context context) {
        return new Builder(context);
    }

    private File getImageCacheDir(Context context) {
        return getImageCacheDir(context, DEFAULT_DISK_CACHE_DIR);
    }

    private File getImageCacheDir(Context context, String cacheName) {
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir != null) {
            File result = new File(cacheDir, cacheName);
            if (!result.mkdirs() && (!result.exists() || !result.isDirectory())) {
                return null;
            }
            return result;
        }
        Log.e(BuildConfig.TAG, "default disk cache dir is null");
        return null;
    }

    private File getImageCacheFile(Context context, String suffix) {
        if (TextUtils.isEmpty(mOutFileDir)) {
            mOutFileDir = getImageCacheDir(context).getAbsolutePath();
        }

        String cacheBuilder = mOutFileDir + "/" +
                System.currentTimeMillis() +
                (int) (Math.random() * 1000) +
                (TextUtils.isEmpty(suffix) ? ".jpg" : suffix);

        return new File(cacheBuilder);
    }

    @UiThread
    private void startCompress(final Context context) {
        if (mPaths == null || mPaths.size() == 0 && mCompressListener != null) {
            mCompressListener.onError(new NullPointerException("image file cannot be null"));
        }
        //存储压缩后的图片集合
        final List<String> result = new ArrayList<>();
        //原始图片的集合
        List<String> originalPath = new ArrayList<>(mPaths);
        mHandler.sendMessage(mHandler.obtainMessage(COMPRESS_START));

        for (final String path : originalPath) {
            if (Checker.isImage(path)) {
                //如果是图片
                //保证 同一时间只有一个Task在后台运行
                AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() {
                    @Override
                    public void run() {
                        boolean isNeedCompress = Checker.isNeedCompress(mLeastCompressSize, path);
                        File file;
                        if (isNeedCompress) {
                            CompressUtils compressUtils = new CompressUtils();
                            Bitmap bitmap = compressUtils.decodeFile(path);
                            /* file = getImageCacheFile(context, Checker.checkSuffix(path));*/
                            compressUtils
                                    .compressImage(bitmap, mQuality, path);

                        } else {
                            file = new File(path);
                        }
                        if (!result.contains(path)) {
                            //添加到已经缓存的集合中
                            result.add(path);
                        }
                        //判断压缩是否已经结束
                        if (mPaths.size() == result.size()) {
                            mHandler.sendMessage(mHandler.obtainMessage(COMPRESS_SUCCESS, result));
                        }
                    }
                });
            } else {
                mCompressListener.onError(new IllegalArgumentException("This path is not a picture and cannot be read..."));
                break;
            }
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (mCompressListener == null) {
            return false;
        }
        switch (msg.what) {
            case COMPRESS_START:
                mCompressListener.onStart();
                break;
            case COMPRESS_ERROR:
                mCompressListener.onError((Throwable) msg.obj);
                break;
            case COMPRESS_SUCCESS:
                Log.e(BuildConfig.TAG, "成功了");
                mCompressListener.onSuccess((List<String>) msg.obj);
                break;
            default:
                break;
        }
        return false;
    }

    public static class Builder {
        private Context mContext;
        private List<String> mPaths;
        private int mLeastCompressSize = 100;
        private OnCompressListener mCompressListener;
        //输出文件目录
        private String mOutFileDir;
        //默认100
        private int mQuality = 100;

        public Builder(Context context) {
            this.mContext = context;
            this.mPaths = new ArrayList<>();
        }

        public Builder load(String path) {
            this.mPaths.add(path);
            return this;
        }

        public Builder setQuality(int quality) {
            mQuality = quality;
            return this;
        }

        public Builder load(List<String> paths) {
            this.mPaths.addAll(paths);
            return this;
        }

        public Builder setCompressListener(OnCompressListener listener) {
            this.mCompressListener = listener;
            return this;
        }

        public Builder setOutFileDir(String outFileDir) {
            this.mOutFileDir = outFileDir;
            return this;
        }

        //小于100，忽略图片压缩
        public Builder ignoreCompress(int size) {
            this.mLeastCompressSize = size;
            return this;
        }

        private ImageCompressUtils build() {
            return new ImageCompressUtils(this);
        }

        public void startCompress() {
            build().startCompress(mContext);
        }

    }


}
