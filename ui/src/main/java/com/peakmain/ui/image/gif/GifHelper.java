package com.peakmain.ui.image.gif;

import android.graphics.Bitmap;

/**
 * author ：Peakmain
 * createTime：2021/8/6
 * mail:2726449200@qq.com
 * describe：gif播放帮助类
 */
public class GifHelper {
    static {
        System.loadLibrary("compress-lib");
    }

    private long mGifHelper;

    public int getWidth() {
        return getWidth(mGifHelper);
    }

    public int getHeight() {
        return getHeight(mGifHelper);
    }

    //渲染图片
    public int updateFrame(Bitmap bitmap) {
        return updateFrame(mGifHelper, bitmap);
    }

    private GifHelper(long gifHander) {
        this.mGifHelper = gifHander;
    }

    public static GifHelper load(String path) {
        long gifHander = loadGif(path);
        if (gifHander == -1) {
            return null;
        }
        return new GifHelper(gifHander);
    }

    public static native long loadGif(String path);

    public static native int getWidth(long gifHelper);

    public static native int getHeight(long gifHelper);

    public static native int updateFrame(long gifHelper, Bitmap bitmap);
}
