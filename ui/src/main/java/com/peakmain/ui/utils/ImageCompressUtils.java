package com.peakmain.ui.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * author ：Peakmain
 * createTime：2020/3/27
 * mail:2726449200@qq.com
 * describe：ndk实现图片压缩
 */
public class ImageCompressUtils {
    private static final String TAG="ImageCompressUtils";
    static {
        System.loadLibrary("compress-lib");
    }

    //单例
    private ImageCompressUtils() {

    }

    private static class Holder {
        private static ImageCompressUtils instance = new ImageCompressUtils();
    }

    public static ImageCompressUtils getInstance() {
        return Holder.instance;
    }

    private int srcWidth;
    private int srcHeight;

    public void compressImage(Bitmap bitmap, int quality, String fileName) {
        compressBitmap(bitmap, quality, fileName);
    }

    private native static int compressBitmap(Bitmap bitmap, int quality, String fileName);

    public Bitmap decodeFile(String path) {
        //先获取宽度
        BitmapFactory.Options options = new BitmapFactory.Options();
        //不加载图片内存只拿宽高
        options.inJustDecodeBounds = true;
        options.inSampleSize=1;
        BitmapFactory.decodeFile(path,options);
        srcWidth=options.outWidth;
        srcHeight=options.outHeight;
        options.inSampleSize = ImageCompute.computeSize(srcWidth,srcHeight);
        options.inJustDecodeBounds = false;

        Bitmap tagBitmap =  BitmapFactory.decodeFile(path, options);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        Log.e(TAG,"srcWidth:"+srcWidth+",srcHeight:"+srcHeight);
        try {
            if (Checker.getInstance().isJPG(new FileInputStream(path))) {
                tagBitmap = ImageCompute.rotatingImage(tagBitmap, Checker.getInstance().getOrientation(new FileInputStream(path)));
            }
            tagBitmap.compress(Bitmap.CompressFormat.JPEG, 75, stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tagBitmap;
    }
}
